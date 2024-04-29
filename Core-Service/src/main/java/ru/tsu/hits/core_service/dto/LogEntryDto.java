package ru.tsu.hits.core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor // For Jackson deserialization
public class LogEntryDto implements Serializable {
    private String serviceId;
    private String logLevel;
    private String message;
    private LocalDateTime timestamp;
    private String traceId;
    private String errorDetails;
    private Long responseTime;

    private String method;
    private String path;
    private Integer status;
    private Long duration;
}
