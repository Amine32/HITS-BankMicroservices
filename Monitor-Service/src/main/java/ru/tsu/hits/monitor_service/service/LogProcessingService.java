package ru.tsu.hits.monitor_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.monitor_service.model.LogEntry;
import ru.tsu.hits.monitor_service.repository.LogEntryRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogProcessingService {

    private final LogEntryRepository logEntryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processLogMessage(String message) {
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            LogEntry logEntry = new LogEntry();

            logEntry.setServiceId(rootNode.path("serviceId").asText());
            logEntry.setLogLevel(rootNode.path("logLevel").asText());
            logEntry.setMessage(rootNode.path("message").asText());
            logEntry.setTimestamp(LocalDateTime.now()); // Assuming no timestamp in log message
            logEntry.setTraceId(rootNode.path("traceId").asText(null));
            logEntry.setErrorDetails(rootNode.path("errorDetails").asText(null));
            if (rootNode.hasNonNull("responseTime")) {
                logEntry.setResponseTime(rootNode.path("responseTime").asLong());
            }

            logEntryRepository.save(logEntry);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing log message", e);
        }
    }
}
