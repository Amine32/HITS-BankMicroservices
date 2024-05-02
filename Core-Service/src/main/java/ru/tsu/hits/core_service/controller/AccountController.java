package ru.tsu.hits.core_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.core_service.dto.AccountTransferDto;
import ru.tsu.hits.core_service.model.Account;
import ru.tsu.hits.core_service.model.Currency;
import ru.tsu.hits.core_service.security.JwtUtil;
import ru.tsu.hits.core_service.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{id}")
    public ResponseEntity<Account> createAccount(@PathVariable Long id, @RequestBody Currency currency, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        Object response = accountService.checkAndCreateAccount(id, currency, idempotencyKey);
        if (response instanceof Account) {
            return ResponseEntity.ok((Account) response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId) or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        Optional<Account> account = accountService.getAccount(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId) or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Account> updateAccount(@PathVariable String id, @RequestBody Account account) {
        if (!id.equals(account.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountService.updateAccount(account));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@accountService.isUserAccountOwner(#id,authentication.principal.userId) or hasAuthority('EMPLOYEE')")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deposit")
    @PreAuthorize("@accountService.isUserAccountOwner(#id, authentication.principal.userId)")
    public ResponseEntity<Account> depositMoney(@PathVariable String id, @RequestBody BigDecimal amount, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return accountService.performIdempotentDeposit(id, amount, idempotencyKey);
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("@accountService.isUserAccountOwner(#id, authentication.principal.userId)")
    public ResponseEntity<Account> withdrawMoney(@PathVariable String id, @RequestBody BigDecimal amount, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return accountService.performIdempotentWithdrawal(id, amount, idempotencyKey);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/primary/{userId}")
    @PreAuthorize("hasRole('TRUSTED_SERVICE') or hasAuthority('EMPLOYEE')")
    public ResponseEntity<String> getPrimaryAccountId(@PathVariable Long userId) {
        String accountId = accountService.getPrimaryAccountId(userId);
        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/transfer")
    public void transferFunds(@RequestBody AccountTransferDto transferDto, HttpServletRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        String jwt = jwtUtil.getJwtFromRequest(request);
        Long userId = jwtUtil.getUserIdFromJwtToken(jwt);

        accountService.performIdempotentTransfer(transferDto, userId, idempotencyKey);
    }

    @PostMapping("/transfer/from-master/{toAccountId}")
    @PreAuthorize("hasRole('TRUSTED_SERVICE')")
    public ResponseEntity<?> transferFromMaster(@PathVariable String toAccountId, @RequestBody BigDecimal amount) {
        accountService.transferFromMasterAccount(toAccountId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer/to-master/{fromAccountId}")
    @PreAuthorize("hasRole('TRUSTED_SERVICE')")
    public ResponseEntity<?> transferToMaster(@PathVariable String fromAccountId, @RequestBody BigDecimal amount) {
        accountService.transferToMasterAccount(fromAccountId, amount);
        return ResponseEntity.ok().build();
    }

}