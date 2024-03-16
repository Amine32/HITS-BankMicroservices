package ru.tsu.hits.core_service.dto;

import lombok.Data;
import ru.tsu.hits.core_service.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private Long accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
}