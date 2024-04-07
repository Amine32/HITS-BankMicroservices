package ru.tsu.hits.loan_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.loan_service.model.Payment;
import ru.tsu.hits.loan_service.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPayment(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> getAllPaymentsForLoan(Long loanId) {
        return paymentRepository.findAllByLoanId(loanId);
    }
}