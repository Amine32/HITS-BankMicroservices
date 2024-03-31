package ru.tsu.hits.loan_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.loan_service.model.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByLoanId(Long loanId);
}
