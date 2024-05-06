package ru.tsu.hits.core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionDto {

    private String token;

    private String topic;
}
