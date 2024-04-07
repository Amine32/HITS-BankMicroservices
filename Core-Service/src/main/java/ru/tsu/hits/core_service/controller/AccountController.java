package ru.tsu.hits.core_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.core_service.model.Account;
import ru.tsu.hits.core_service.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{id}")
    public ResponseEntity<Account> createAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.createAccount(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId) or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccount(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        if (!id.equals(account.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountService.updateAccount(account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deposit")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId)")
    public ResponseEntity<Account> depositMoney(@PathVariable Long id, @RequestBody BigDecimal amount) {
        Account account = accountService.depositMoney(id, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("@accountService.isUserAccountOwner(#id, authentication.principal.userId)")
    public ResponseEntity<Account> withdrawMoney(@PathVariable Long id, @RequestBody BigDecimal amount) {
        Account account = accountService.withdrawMoney(id, amount);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/primary/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Long> getPrimaryAccountId(@PathVariable Long userId) {
        Long accountId = accountService.getPrimaryAccountId(userId);
        return ResponseEntity.ok(accountId);
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<Account> addMoney(@PathVariable Long id, @RequestBody BigDecimal amount) {
        Account account = accountService.addMoney(id, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{id}/subtract")
    public ResponseEntity<Account> subtractMoney(@PathVariable Long id, @RequestBody BigDecimal amount) {
        Account account = accountService.subtractMoney(id, amount);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

}