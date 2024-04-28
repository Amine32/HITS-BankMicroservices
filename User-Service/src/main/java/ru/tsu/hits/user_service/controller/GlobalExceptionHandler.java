package ru.tsu.hits.user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.tsu.hits.user_service.service.KafkaLogPublisher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final KafkaLogPublisher kafkaLogPublisher;

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
        String errorLog = createErrorLog(ex, request);
        kafkaLogPublisher.publishLog(errorLog);

        log.error("Error occurred processing request: {}", request.getDescription(false), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    private String createErrorLog(Exception ex, WebRequest request) {
        Map<String, Object> errorLogMap = new LinkedHashMap<>();
        errorLogMap.put("timestamp", LocalDateTime.now());
        errorLogMap.put("error", ex.getMessage());
        errorLogMap.put("type", ex.getClass().getSimpleName());
        errorLogMap.put("path", request.getDescription(false));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        errorLogMap.put("stackTrace", sw.toString());

        // Convert the map to a JSON string
        try {
            return new ObjectMapper().writeValueAsString(errorLogMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing error log", e);
        }
    }
}

