package ru.tsu.hits.api_gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/core")
    public ResponseEntity<String> coreFallback() {
        return ResponseEntity.internalServerError().body("Core Service is temporarily unavailable.");
    }

    @GetMapping("/fallback/user")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.internalServerError().body("User Service is temporarily unavailable.");
    }

    @GetMapping("/fallback/loan")
    public ResponseEntity<String> loanFallback() {
        return ResponseEntity.internalServerError().body("Loan Service is temporarily unavailable.");
    }
}
