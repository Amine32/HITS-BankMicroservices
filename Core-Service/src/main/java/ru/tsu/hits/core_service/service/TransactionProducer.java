package ru.tsu.hits.core_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.TransactionMessage;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.transactions}")
    private String transactionsTopic;

    public void sendTransactionMessage(TransactionMessage message) {
        try {
            // Convert the TransactionMessage to a JSON string
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(transactionsTopic, jsonMessage);
        } catch (JsonProcessingException e) {
            // Handle possible serialization errors
            System.err.println("Error serializing TransactionMessage: " + e.getMessage());
        }
    }
}

