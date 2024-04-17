package ru.tsu.hits.core_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.TransactionMessage;
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
    private final SimpMessagingTemplate messagingTemplate;
    private final TransactionProducer transactionProducer;

    public void recordTransaction(String accountId, BigDecimal amount, TransactionType type) {
        TransactionMessage message = new TransactionMessage(accountId, amount, type.toString());
        transactionProducer.sendTransactionMessage(message);
    }

    public List<Transaction> getTransactionsByAccountId(String accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    public void processTransactionMessage(TransactionMessage message) {
        // Convert message to Transaction entity and save
        Transaction transaction = new Transaction();
        transaction.setAccountId(message.getAccountId());
        transaction.setAmount(message.getAmount());
        transaction.setTransactionType(TransactionType.valueOf(message.getTransactionType()));
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Notify WebSocket subscribers
        messagingTemplate.convertAndSend("/topic/transactions", transaction);
    }
}