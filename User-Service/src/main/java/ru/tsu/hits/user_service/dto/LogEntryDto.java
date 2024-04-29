package ru.tsu.hits.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntryDto implements Serializable {
    private LocalDateTime timestamp;
    private String method;
    private String path;
    private Integer status;
    private Long duration;
    private String serviceId;

    // Optional fields for errors
    private String error;
    private String type;
}
