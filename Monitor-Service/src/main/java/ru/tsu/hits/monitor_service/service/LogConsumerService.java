package ru.tsu.hits.monitor_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogConsumerService {

    private final LogProcessingService logProcessingService;

    @KafkaListener(topics = "logs", groupId = "monitoring-service")
    public void consume(String message) {
        logProcessingService.processLogMessage(message);
    }
}
