package ru.tsu.hits.user_service.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Aspect
@Component
public class UnstableServiceAspect {

    private final Random random = new Random();

    @Before("execution(* ru.tsu.hits..controller.*.*(..))")
    public void simulateServiceInstability() {
        int minute = LocalDateTime.now().getMinute();
        int errorProbability = minute % 2 == 0 ? 90 : 50; // 90% error rate on even minutes, 50% otherwise

        if (random.nextInt(100) < errorProbability) {
            throw new RuntimeException("Simulated service failure");
        }
    }
}
