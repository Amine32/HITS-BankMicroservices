package ru.tsu.hits.core_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.TransactionMessage;

@Service
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.transactions}", groupId = "${spring.kafka.groupId}")
    public void consume(String jsonMessage) {
        try {
            TransactionMessage message = objectMapper.readValue(jsonMessage, TransactionMessage.class);
            transactionService.processTransactionMessage(message);
        } catch (JsonProcessingException e) {
            System.err.println("Error deserializing TransactionMessage from JSON: " + e.getMessage());
        }
    }
}

