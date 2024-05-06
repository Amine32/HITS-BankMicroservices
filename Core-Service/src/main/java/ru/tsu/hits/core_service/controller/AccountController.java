package ru.tsu.hits.core_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.core_service.dto.AccountTransferDto;
import ru.tsu.hits.core_service.model.Account;
import ru.tsu.hits.core_service.model.Currency;
import ru.tsu.hits.core_service.service.AccountService;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{id}")
    public Account createAccount(@PathVariable Long id, @RequestBody Currency currency, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return accountService.checkAndCreateAccount(id, currency, idempotencyKey);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId) or hasAuthority('EMPLOYEE')")
    public Account getAccount(@PathVariable String id) throws AccountNotFoundException {
        return accountService.getAccount(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId) or hasAuthority('EMPLOYEE')")
    public Account updateAccount(@PathVariable String id, @RequestBody Account account) {
        if (!id.equals(account.getId())) {
            throw new RuntimeException("Account id mismatch");
        }
        return accountService.updateAccount(account);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId) or hasAuthority('EMPLOYEE')")
    public void deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
    }

    @PostMapping("/{id}/deposit")
    @PreAuthorize("@accountService.isUserAccountOwner(#id, authentication.principal.userId)")
    public Account depositMoney(@PathVariable String id, @RequestBody BigDecimal amount, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return accountService.performIdempotentDeposit(id, amount, idempotencyKey);
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("@accountService.isUserAccountOwner(#id, authentication.principal.userId)")
    public Account withdrawMoney(@PathVariable String id, @RequestBody BigDecimal amount, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return accountService.performIdempotentWithdrawal(id, amount, idempotencyKey);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasAuthority('EMPLOYEE')")
    public List<Account> getUserAccounts(@PathVariable Long userId) {
        return accountService.getUserAccounts(userId);
    }

    @GetMapping("/primary/{userId}")
    @PreAuthorize("hasRole('TRUSTED_SERVICE') or hasAuthority('EMPLOYEE')")
    public String getPrimaryAccountId(@PathVariable Long userId) {
        return accountService.getPrimaryAccountId(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/transfer")
    public void transferFunds(@RequestBody AccountTransferDto transferDto, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        accountService.performIdempotentTransfer(transferDto, idempotencyKey);
    }

    @PostMapping("/transfer/from-master/{toAccountId}")
    @PreAuthorize("hasRole('TRUSTED_SERVICE')")
    public void transferFromMaster(@PathVariable String toAccountId, @RequestBody BigDecimal amount) {
        accountService.transferFromMasterAccount(toAccountId, amount);
    }

    @PostMapping("/transfer/to-master/{fromAccountId}")
    @PreAuthorize("hasRole('TRUSTED_SERVICE')")
    public void transferToMaster(@PathVariable String fromAccountId, @RequestBody BigDecimal amount) {
        accountService.transferToMasterAccount(fromAccountId, amount);
    }

}