package ru.tsu.hits.bank_account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.bank_account_service.dto.TransactionDto;
import ru.tsu.hits.bank_account_service.dto.converter.TransactionDtoConverter;
import ru.tsu.hits.bank_account_service.service.TransactionService;

import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionDtoConverter transactionDtoConverter;

    @Autowired
    public TransactionController(TransactionService transactionService, TransactionDtoConverter transactionDtoConverter) {
        this.transactionService = transactionService;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionDtoConverter.convertToDto(transactionService.createTransaction(transactionDtoConverter.convertToEntity(transactionDto))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id) {
        Optional<TransactionDto> transactionDto = transactionService.getTransaction(id).map(transactionDtoConverter::convertToDto);
        return transactionDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable Long id, @RequestBody TransactionDto transactionDto) {
        if (!id.equals(transactionDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(transactionDtoConverter.convertToDto(transactionService.updateTransaction(transactionDtoConverter.convertToEntity(transactionDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}