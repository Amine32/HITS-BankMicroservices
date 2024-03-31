package ru.tsu.hits.loan_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private Long loanId;
    private BigDecimal paymentAmount;
}
