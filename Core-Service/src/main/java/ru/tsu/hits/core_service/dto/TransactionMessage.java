package ru.tsu.hits.core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionMessage implements Serializable {
    private Long accountId;
    private BigDecimal amount;
    private String transactionType;
}

