package ru.tsu.hits.core_service.model;

public enum Currency {
    RUB, // Russian Ruble
    USD, // US Dollar
    CNY; // Chinese Yuan

    public static boolean isValid(String currency) {
        try {
            Currency.valueOf(currency);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

