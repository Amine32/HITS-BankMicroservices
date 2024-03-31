package ru.tsu.hits.loan_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanApplicationDto {
    private Long ownerId;
    private BigDecimal amount;
    private Long rateId;
}
