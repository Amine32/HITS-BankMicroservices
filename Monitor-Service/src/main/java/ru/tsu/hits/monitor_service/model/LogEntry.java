package ru.tsu.hits.monitor_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "log_entries")
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceId;
    private String logLevel;
    private String message;
    private LocalDateTime timestamp;

    private String traceId;
    private String errorDetails;
    private Long responseTime;
}
