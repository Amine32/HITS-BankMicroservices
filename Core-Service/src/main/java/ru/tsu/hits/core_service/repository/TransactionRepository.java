package ru.tsu.hits.core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.core_service.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAccountId(String id);
}