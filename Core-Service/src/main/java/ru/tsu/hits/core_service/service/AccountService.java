package ru.tsu.hits.core_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.model.Account;
import ru.tsu.hits.core_service.model.TransactionType;
import ru.tsu.hits.core_service.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public Account createAccount(Long ownerId) {
        Account account = new Account();
        account.setOwnerId(ownerId);
        account.setBalance(BigDecimal.ZERO);  // Default balance set to 0
        account.setCreatedAt(LocalDateTime.now());  // Set current time as the creation time
        account.setActive(true);  // Set the account as active by default

        return accountRepository.save(account);
    }

    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Account depositMoney(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));

        transactionService.recordTransaction(account.getId(), amount, TransactionType.DEPOSIT);

        return accountRepository.save(account);
    }

    public Account withdrawMoney(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));

        transactionService.recordTransaction(account.getId(), amount, TransactionType.WITHDRAWAL);

        return accountRepository.save(account);
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findAllByOwnerId(userId);
    }

    public Long getPrimaryAccountId(Long userId) {
        return getUserAccounts(userId).stream()
                .max(Comparator.comparing(Account::getBalance))
                .map(Account::getId)
                .orElse(null);
    }

}