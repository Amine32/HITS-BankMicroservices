package ru.tsu.hits.core_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.TransactionMessage;

@Service
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionService transactionService;

    @KafkaListener(topics = "${spring.kafka.topic.transactions}", groupId = "${spring.kafka.groupId}")
    public void consume(TransactionMessage message) {
        transactionService.processTransactionMessage(message);
    }
}

