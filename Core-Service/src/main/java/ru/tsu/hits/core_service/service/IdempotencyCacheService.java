package ru.tsu.hits.core_service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IdempotencyCacheService {

    private final Cache<String, Object> idempotencyCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .maximumSize(10000)
            .build();

    public void storeResponse(String key, Object response) {
        idempotencyCache.put(key, response);
    }

    public Object getResponse(String key) {
        return idempotencyCache.getIfPresent(key);
    }
}
