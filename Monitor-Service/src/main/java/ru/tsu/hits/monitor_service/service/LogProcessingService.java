package ru.tsu.hits.monitor_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tsu.hits.monitor_service.dto.LogEntryDto;
import ru.tsu.hits.monitor_service.model.LogEntry;
import ru.tsu.hits.monitor_service.repository.LogEntryRepository;

@Service
@RequiredArgsConstructor
public class LogProcessingService {

    private final LogEntryRepository logEntryRepository;

    @KafkaListener(topics = "${spring.kafka.topic.monitoring}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) throws JsonProcessingException {
        System.out.println("Message: " + message);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        LogEntryDto logEntryDto = mapper.readValue(message, LogEntryDto.class);
        System.out.println("DTO: " + logEntryDto);
        LogEntry logEntry = convertToEntity(logEntryDto);
        logEntryRepository.save(logEntry);
    }

    private LogEntry convertToEntity(LogEntryDto dto) {
        LogEntry logEntry = new LogEntry();
        logEntry.setTimestamp(dto.getTimestamp());
        logEntry.setMethod(dto.getMethod());
        logEntry.setPath(dto.getPath());
        logEntry.setStatus(dto.getStatus());
        logEntry.setDuration(dto.getDuration());
        logEntry.setServiceId(dto.getServiceId());

        logEntry.setError(dto.getError());
        logEntry.setType(dto.getType());

        return logEntry;
    }
}
