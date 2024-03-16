package ru.tsu.hits.core_service.dto;

import lombok.Data;
import ru.tsu.hits.core_service.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private Long userId;
    private AccountType accountType;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private boolean isActive;
}