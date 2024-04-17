package ru.tsu.hits.core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor //for kafka deserialization
public class TransactionMessage implements Serializable {
    private String accountId;
    private BigDecimal amount;
    private String transactionType;
}

