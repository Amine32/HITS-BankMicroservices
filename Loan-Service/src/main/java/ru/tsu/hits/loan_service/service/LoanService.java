package ru.tsu.hits.loan_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tsu.hits.loan_service.dto.LoanApplicationDto;
import ru.tsu.hits.loan_service.model.*;
import ru.tsu.hits.loan_service.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    private final CoreServiceClient coreServiceClient;
    private final LoanRateService loanRateService;
    private final PaymentService paymentService;

    @Transactional
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

        Long primaryAccountId = coreServiceClient.getPrimaryAccountId(application.getOwnerId());
        coreServiceClient.postTransaction(primaryAccountId, application.getAmount(), "LOAN");

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

        paymentService.createPayment(Payment.builder()
                .loan(loan)
                .paymentAmount(amount)
                .paymentDate(LocalDateTime.now())
                .build());

        Long primaryAccountId = coreServiceClient.getPrimaryAccountId(loan.getOwnerId());
        coreServiceClient.postTransaction(primaryAccountId, amount, "LOAN_PAYMENT");

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
}
