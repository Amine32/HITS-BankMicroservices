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

    private LocalDateTime timestamp;
    private String method;
    private String path;
    private Integer status;
    private Long duration;
    private String serviceId;
    private String error;        // Optional: Used if it's an error log
    private String type;         // Optional: Used if it's an error log
}
