package ru.tsu.hits.bank_account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.bank_account_service.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}