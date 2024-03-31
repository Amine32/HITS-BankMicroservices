package ru.tsu.hits.loan_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.loan_service.model.Payment;
import ru.tsu.hits.loan_service.service.PaymentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.createPayment(payment));
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