package ru.tsu.hits.loan_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanDto {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private boolean isActive;
}