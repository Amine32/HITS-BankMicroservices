package ru.tsu.hits.loan_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tsu.hits.loan_service.dto.LoanApplicationDto;
import ru.tsu.hits.loan_service.model.Loan;
import ru.tsu.hits.loan_service.model.LoanRate;
import ru.tsu.hits.loan_service.model.Payment;
import ru.tsu.hits.loan_service.repository.LoanRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    private final CoreServiceClient coreServiceClient;
    private final LoanRateService loanRateService;
    private final PaymentService paymentService;
    private final IdempotencyCacheService idempotencyCacheService;
    private final ObjectMapper objectMapper;

    public Loan applyForLoan(LoanApplicationDto application) {
        LoanRate rate = loanRateService.getLoanRateById(application.getRateId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan rate ID"));

        Loan loan = Loan.builder()
                .ownerId(application.getOwnerId())
                .rate(rate)
                .originalAmount(application.getAmount())
                .amountOwed(application.getAmount())
                .amountPaid(BigDecimal.ZERO)
                .duration(rate.getTermLength())
                .createdAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(rate.getTermLength()))
                .isClosed(false)
                .dailyPayment(calculateDailyPayment(application.getAmount(), rate.getInterestRate(), rate.getTermLength()))
                .build();

        String primaryAccountId = coreServiceClient.getPrimaryAccountId(application.getOwnerId());
        coreServiceClient.transferFromMasterAccount(primaryAccountId, application.getAmount());

        return loanRepository.save(loan);
    }


    private BigDecimal calculateDailyPayment(BigDecimal amount, BigDecimal interestRate, int termLength) {
        return amount.add(amount.multiply(interestRate.divide(new BigDecimal(100)))).divide(new BigDecimal(termLength), 2, BigDecimal.ROUND_HALF_UP);
    }

    public List<Loan> getLoansByOwner(Long ownerId) {
        return loanRepository.findAllByOwnerId(ownerId);
    }

    public Loan repayLoan(Long loanId, BigDecimal amount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan ID"));

        loan.setAmountPaid(loan.getAmountPaid().add(amount));
        loan.setAmountOwed(loan.getOriginalAmount().subtract(loan.getAmountPaid()));
        loan.setLastPaymentDate(LocalDateTime.now());

        if (loan.getAmountOwed().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setClosed(true);
            loan.setClosedAt(LocalDateTime.now());
        }

        String primaryAccountId = coreServiceClient.getPrimaryAccountId(loan.getOwnerId());
        coreServiceClient.transferToMasterAccount(primaryAccountId, amount);

        paymentService.createPayment(Payment.builder()
                .loan(loan)
                .paymentAmount(amount)
                .paymentDate(LocalDateTime.now())
                .build());

        return loanRepository.save(loan);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
    public void processDailyPayments() {
        List<Loan> activeLoans = loanRepository.findAllByIsClosedFalse();
        LocalDateTime today = LocalDateTime.now();

        for (Loan loan : activeLoans) {
            if (loan.getLastPaymentDate() == null || loan.getLastPaymentDate().toLocalDate().isBefore(today.toLocalDate())) {
                BigDecimal dailyPayment = loan.getDailyPayment();
                repayLoan(loan.getId(), dailyPayment);
            }
        }
    }

    @Transactional
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Transactional
    public List<Loan> getOverdueLoansByUser(Long ownerId) {
        return loanRepository.findAllByOwnerIdAndIsClosedFalse(ownerId).stream()
                .filter(loan -> loan.getDueDate().isBefore(LocalDateTime.now())
                        && loan.getAmountOwed().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateCreditRating(Long ownerId) {
        List<Loan> allLoans = loanRepository.findAllByOwnerId(ownerId);

        if (allLoans.isEmpty()) {
            return BigDecimal.valueOf(1000);
        }

        BigDecimal totalAmount = allLoans.stream()
                .map(Loan::getOriginalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal overdueAmount = allLoans.stream()
                .filter(loan -> loan.getDueDate().isBefore(LocalDateTime.now()))
                .map(Loan::getAmountOwed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal score = BigDecimal.ZERO;
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            score = BigDecimal.ONE.subtract(overdueAmount.divide(totalAmount, 2, RoundingMode.HALF_UP))
                    .multiply(BigDecimal.valueOf(1000));
        }

        return score.max(BigDecimal.ZERO);  // Ensure the score doesn't go below 0
    }

    public Loan applyForLoanWithIdempotency(LoanApplicationDto application, String idempotencyKey) {
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return objectMapper.convertValue(existingResponse, Loan.class);
        }

        Loan newLoan = applyForLoan(application);
        idempotencyCacheService.storeResponse(idempotencyKey, "Loan application successful");
        return newLoan;
    }

    public ResponseEntity<Object> repayLoanWithIdempotency(Long loanId, BigDecimal paymentAmount, String idempotencyKey) {
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return ResponseEntity.ok(existingResponse);
        }

        try {
            repayLoan(loanId, paymentAmount);
            idempotencyCacheService.storeResponse(idempotencyKey, "Loan repayment successful");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loan repayment failed");
        }
    }

    public ResponseEntity<?> payOffLoanWithIdempotency(Long loanId, BigDecimal amount, String idempotencyKey) {
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return ResponseEntity.ok(existingResponse);
        }

        Loan loan = repayLoan(loanId, amount);
        idempotencyCacheService.storeResponse(idempotencyKey, loan);
        return ResponseEntity.ok(loan);
    }
}
