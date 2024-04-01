package ru.tsu.hits.loan_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.loan_service.model.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByOwnerId(Long ownerId);

    List<Loan> findAllByIsClosedFalse();
}