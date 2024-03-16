package ru.tsu.hits.core_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.core_service.dto.AccountDto;
import ru.tsu.hits.core_service.dto.converter.AccountDtoConverter;
import ru.tsu.hits.core_service.service.AccountService;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountDtoConverter accountDtoConverter;

    @Autowired
    public AccountController(AccountService accountService, AccountDtoConverter accountDtoConverter) {
        this.accountService = accountService;
        this.accountDtoConverter = accountDtoConverter;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountDtoConverter.convertToDto(accountService.createAccount(accountDtoConverter.convertToEntity(accountDto))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id) {
        Optional<AccountDto> accountDto = accountService.getAccount(id).map(accountDtoConverter::convertToDto);
        return accountDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        if (!id.equals(accountDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountDtoConverter.convertToDto(accountService.updateAccount(accountDtoConverter.convertToEntity(accountDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}