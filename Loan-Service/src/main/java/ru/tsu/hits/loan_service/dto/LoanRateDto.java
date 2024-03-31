package ru.tsu.hits.loan_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanRateDto {
    private String name;
    private BigDecimal interestRate;
    private int termLength;
}
