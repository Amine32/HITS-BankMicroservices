package ru.tsu.hits.loan_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.loan_service.dto.LoanRateDto;
import ru.tsu.hits.loan_service.model.LoanRate;
import ru.tsu.hits.loan_service.model.Payment;
import ru.tsu.hits.loan_service.service.PaymentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return paymentService.createPaymentWithIdempotency(payment, idempotencyKey);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPayment(id);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        if (!id.equals(payment.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(paymentService.updatePayment(payment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<Payment>> getAllPaymentsForLoan(@PathVariable Long loanId) {
        List<Payment> payments = paymentService.getAllPaymentsForLoan(loanId);
        return ResponseEntity.ok(payments);
    }
}