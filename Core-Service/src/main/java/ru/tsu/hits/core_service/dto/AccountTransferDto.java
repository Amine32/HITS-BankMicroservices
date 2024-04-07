package ru.tsu.hits.core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountTransferDto {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}