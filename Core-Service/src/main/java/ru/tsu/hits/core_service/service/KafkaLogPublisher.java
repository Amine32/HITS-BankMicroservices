package ru.tsu.hits.core_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaLogPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.monitoring}")
    private String logTopic;

    public void publishLog(String logMessage) {
        kafkaTemplate.send(logTopic, logMessage);
    }
}

