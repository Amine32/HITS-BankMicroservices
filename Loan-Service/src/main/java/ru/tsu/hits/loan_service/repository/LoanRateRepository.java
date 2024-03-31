package ru.tsu.hits.loan_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.loan_service.model.LoanRate;

import java.util.Optional;

public interface LoanRateRepository extends JpaRepository<LoanRate, Long> {
    Optional<LoanRate> findByName(String name);
}
