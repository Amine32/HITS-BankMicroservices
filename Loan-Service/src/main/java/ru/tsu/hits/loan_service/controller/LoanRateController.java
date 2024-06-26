package ru.tsu.hits.loan_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.loan_service.dto.LoanRateDto;
import ru.tsu.hits.loan_service.model.LoanRate;
import ru.tsu.hits.loan_service.service.LoanRateService;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class LoanRateController {

    private final LoanRateService loanRateService;

    @PostMapping
    public ResponseEntity<LoanRate> createRate(@RequestBody LoanRateDto loanRateDto) {
        LoanRate rate = loanRateService.createLoanRate(loanRateDto);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LoanRate>> getAllRates() {
        return ResponseEntity.ok(loanRateService.getAllLoanRates());
    }
}
