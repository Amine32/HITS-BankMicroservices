package ru.tsu.hits.core_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.model.Account;
import ru.tsu.hits.core_service.model.Transaction;
import ru.tsu.hits.core_service.model.TransactionType;
import ru.tsu.hits.core_service.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void recordTransaction(Long accountId, BigDecimal amount, TransactionType type) {
        Transaction transaction = new Transaction();

        transaction.setAccountId(accountId);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }
}