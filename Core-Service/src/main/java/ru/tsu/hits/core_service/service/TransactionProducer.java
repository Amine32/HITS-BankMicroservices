package ru.tsu.hits.core_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.TransactionMessage;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final KafkaTemplate<String, TransactionMessage> kafkaTemplate;

    @Value("${kafka.topic.transactions}")
    private String transactionsTopic;

    public void sendTransactionMessage(TransactionMessage message) {
        kafkaTemplate.send(transactionsTopic, message);
    }
}

