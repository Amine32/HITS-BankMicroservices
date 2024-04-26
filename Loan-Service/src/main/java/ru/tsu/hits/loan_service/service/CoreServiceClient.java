package ru.tsu.hits.loan_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoreServiceClient {

    private final WebClient.Builder webClientBuilder;

    private final String coreServiceUrl = "http://localhost:8080/core/api";

    public void transferFromMasterAccount(String toAccountId, BigDecimal amount) {
        webClientBuilder.build()
                .post()
                .uri(coreServiceUrl + "/accounts/transfer/from-master/" + toAccountId)
                .header("Service-Name", "Loan-Service")
                .bodyValue(amount)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void transferToMasterAccount(String fromAccountId, BigDecimal amount) {
        webClientBuilder.build()
                .post()
                .uri(coreServiceUrl + "/accounts/transfer/to-master/" + fromAccountId)
                .header("Service-Name", "Loan-Service")
                .bodyValue(amount)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public String getPrimaryAccountId(Long ownerId) {
        return webClientBuilder.build()
                .get()
                .uri(coreServiceUrl + "/accounts/primary/" + ownerId)
                .header("Service-Name", "Loan-Service")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}