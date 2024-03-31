package ru.tsu.hits.loan_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.loan_service.dto.LoanApplicationDto;
import ru.tsu.hits.loan_service.dto.PaymentDto;
import ru.tsu.hits.loan_service.model.Loan;
import ru.tsu.hits.loan_service.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<Loan> applyForLoan(@RequestBody LoanApplicationDto application) {
        Loan loan = loanService.applyForLoan(application);
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/{loanId}/repayments")
    public ResponseEntity<Loan> makeRepayment(@PathVariable Long loanId, @RequestBody PaymentDto paymentDto) {
        Loan loan = loanService.repayLoan(loanId, paymentDto.getPaymentAmount());
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoans(@PathVariable Long userId) {
        List<Loan> loans = loanService.getLoansByOwner(userId);
        return ResponseEntity.ok(loans);
    }
}