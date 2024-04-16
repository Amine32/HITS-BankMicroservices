package ru.tsu.hits.loan_service.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomPrincipal {
    private String username;
    private Long userId;
}
