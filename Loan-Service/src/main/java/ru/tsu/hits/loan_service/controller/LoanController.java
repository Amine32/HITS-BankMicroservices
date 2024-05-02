package ru.tsu.hits.loan_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.loan_service.dto.LoanApplicationDto;
import ru.tsu.hits.loan_service.dto.PaymentDto;
import ru.tsu.hits.loan_service.model.Loan;
import ru.tsu.hits.loan_service.service.LoanService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public Loan applyForLoan(@RequestBody LoanApplicationDto application, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return loanService.applyForLoanWithIdempotency(application, idempotencyKey);
    }

    @PostMapping("/{loanId}/repayments")
    public ResponseEntity<?> makeRepayment(@PathVariable Long loanId, @RequestBody PaymentDto paymentDto, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return loanService.repayLoanWithIdempotency(loanId, paymentDto.getPaymentAmount(), idempotencyKey);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable Long userId) {
        List<Loan> loans = loanService.getLoansByOwner(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/payoff/{loanId}")
    public ResponseEntity<?> payOff(@PathVariable Long loanId, @RequestBody BigDecimal amount, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return loanService.payOffLoanWithIdempotency(loanId, amount, idempotencyKey);
    }

    @GetMapping("/user/{userId}/overdue")
    @PreAuthorize("#userId == authentication.principal.userId or hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Loan>> getOverdueLoansByUser(@PathVariable Long userId) {
        List<Loan> loans = loanService.getOverdueLoansByUser(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/user/{userId}/credit-rating")
    @PreAuthorize("#userId == authentication.principal.userId or hasAuthority('EMPLOYEE')")
    public ResponseEntity<BigDecimal> getCreditRating(@PathVariable Long userId) {
        BigDecimal creditRating = loanService.calculateCreditRating(userId);
        return ResponseEntity.ok(creditRating);
    }
}