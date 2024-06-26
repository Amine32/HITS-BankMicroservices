package ru.tsu.hits.core_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.core_service.dto.AccountTransactionDto;
import ru.tsu.hits.core_service.model.Transaction;
import ru.tsu.hits.core_service.model.TransactionType;
import ru.tsu.hits.core_service.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public void recordTransaction(@RequestBody AccountTransactionDto dto) {
        transactionService.recordTransaction(dto.getAccountId(), dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()));
    }
}