package ru.tsu.hits.loan_service.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.tsu.hits.loan_service.dto.LogEntryDto;
import ru.tsu.hits.loan_service.service.KafkaLogPublisher;

import java.time.LocalDateTime;

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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws JsonProcessingException {
        long duration = System.currentTimeMillis() - startTime.get();
        startTime.remove();

        log.info("Request to {} completed in {} ms, status: {}", request.getRequestURI(), duration, response.getStatus());

        // Retrieve the exception from request attribute if ex is null
        Exception storedEx = (Exception) request.getAttribute("exception");
        if (storedEx != null) {
            logError(request, response, duration, storedEx);
        } else if (ex != null) {
            logError(request, response, duration, ex);
        } else {
            logInfo(request, response, duration);
        }
    }

    private void logInfo(HttpServletRequest request, HttpServletResponse response, long duration) throws JsonProcessingException {
        LogEntryDto logMessage = createLogMessage(request, response, duration);
        System.out.println("Log Message: " + logMessage);
        kafkaLogPublisher.publishLog(logMessage);
    }

    private void logError(HttpServletRequest request, HttpServletResponse response, long duration, Exception ex) throws JsonProcessingException {
        LogEntryDto logMessage = createLogMessage(request, response, duration);
        logMessage.setError(ex.getMessage());
        logMessage.setType(ex.getClass().getSimpleName());

        System.out.println("Error Log Message: " + logMessage);
        kafkaLogPublisher.publishLog(logMessage);
    }

    private LogEntryDto createLogMessage(HttpServletRequest request, HttpServletResponse response, long duration) {
        LogEntryDto logEntryDto = new LogEntryDto();
        logEntryDto.setTimestamp(LocalDateTime.now());
        logEntryDto.setMethod(request.getMethod());
        logEntryDto.setPath(request.getRequestURI());
        logEntryDto.setStatus(response.getStatus());
        logEntryDto.setDuration(duration);
        logEntryDto.setServiceId("UserService");
        return logEntryDto;
    }
}

