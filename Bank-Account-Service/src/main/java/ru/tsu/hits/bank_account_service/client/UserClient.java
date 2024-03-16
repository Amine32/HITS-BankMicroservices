package ru.tsu.hits.bank_account_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tsu.hits.bank_account_service.dto.UserDto;

@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(@Value("${user.service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public Mono<UserDto> getUser(Long userId) {
        return webClient.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}