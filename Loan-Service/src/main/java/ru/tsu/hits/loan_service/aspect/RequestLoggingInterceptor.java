package ru.tsu.hits.loan_service.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.tsu.hits.loan_service.service.KafkaLogPublisher;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final KafkaLogPublisher kafkaLogPublisher;

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long duration = System.currentTimeMillis() - startTime.get();
        startTime.remove();
        log.info("Request to {} completed in {} ms, status: {}", request.getRequestURI(), duration, response.getStatus());

        String logMessage = createLogMessage(request, response, duration);
        kafkaLogPublisher.publishLog(logMessage);
    }

    private String createLogMessage(HttpServletRequest request, HttpServletResponse response, long duration) {
        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("timestamp", LocalDateTime.now());
        logMap.put("method", request.getMethod());
        logMap.put("path", request.getRequestURI());
        logMap.put("status", response.getStatus());
        logMap.put("duration", duration);

        // Convert the map to a JSON string
        try {
            return new ObjectMapper().writeValueAsString(logMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing log message", e);
        }
    }
}

