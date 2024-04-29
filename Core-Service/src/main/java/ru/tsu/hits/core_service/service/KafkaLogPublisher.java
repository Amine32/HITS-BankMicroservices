package ru.tsu.hits.core_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.LogEntryDto;

@Service
@RequiredArgsConstructor
public class KafkaLogPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.monitoring}")
    private String logTopic;

    public void publishLog(LogEntryDto logMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonMessage = mapper.writeValueAsString(logMessage);
        kafkaTemplate.send(logTopic, jsonMessage);
    }
}

