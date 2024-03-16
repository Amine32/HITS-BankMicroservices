package ru.tsu.hits.core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.core_service.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}