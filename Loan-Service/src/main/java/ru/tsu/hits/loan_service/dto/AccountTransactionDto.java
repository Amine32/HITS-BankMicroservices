package ru.tsu.hits.loan_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountTransactionDto {

    private Long accountId;

    private BigDecimal amount;

    private String transactionType;
}
