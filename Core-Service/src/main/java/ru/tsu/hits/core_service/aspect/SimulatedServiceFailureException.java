package ru.tsu.hits.core_service.aspect;

public class SimulatedServiceFailureException extends RuntimeException {
    public SimulatedServiceFailureException(String message) {
        super(message);
    }
}

