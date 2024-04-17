package ru.tsu.hits.core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountTransactionDto{

    private String accountId;

    private BigDecimal amount;

    private String transactionType;
}
