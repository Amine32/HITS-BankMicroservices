package ru.tsu.hits.bank_account_service.dto;

import lombok.Data;
import ru.tsu.hits.bank_account_service.model.TransactionType;

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