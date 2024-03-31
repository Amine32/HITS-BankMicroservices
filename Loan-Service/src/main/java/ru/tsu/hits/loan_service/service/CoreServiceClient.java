package ru.tsu.hits.loan_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tsu.hits.loan_service.dto.AccountTransactionDto;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoreServiceClient {

    private final WebClient.Builder webClientBuilder;

    private final String coreServiceUrl = "https://localhost:8080/core/api";

    public void postTransaction(Long accountId, BigDecimal amount, String transactionType) {
        AccountTransactionDto transactionDto = new AccountTransactionDto(accountId, amount, transactionType);

        webClientBuilder.build()
                .post()
                .uri(coreServiceUrl + "/transactions")
                .bodyValue(transactionDto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Long getPrimaryAccountId(Long ownerId) {
        return webClientBuilder.build()
                .get()
                .uri(coreServiceUrl + "/accounts/primary/" + ownerId)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }
}
