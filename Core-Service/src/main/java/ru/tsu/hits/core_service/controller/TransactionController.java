package ru.tsu.hits.core_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.core_service.model.Transaction;
import ru.tsu.hits.core_service.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{accountId}")
    public List<Transaction> getTransactions(@PathVariable String accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }
}