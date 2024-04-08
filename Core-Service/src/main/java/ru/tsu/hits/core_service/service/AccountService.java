package ru.tsu.hits.core_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.AccountTransferDto;
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

    @Value("${core.masterAccountId}")
    private Long masterAccountId;

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

        transactionService.recordTransaction(account.getId(), amount.negate(), TransactionType.WITHDRAWAL);

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

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public boolean isUserAccountOwner(Long accountId, Long userId) {
        return accountRepository.findByIdAndOwnerId(accountId, userId).isPresent();
    }

    @Transactional
    public void transferMoney(AccountTransferDto transferDto, Long userId) {
        Account fromAccount = accountRepository.findById(transferDto.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account toAccount = accountRepository.findById(transferDto.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        // Check if the user owns the source account
        if (!fromAccount.getOwnerId().equals(userId)) {
            throw new RuntimeException("User does not own the source account");
        }

        // Check for sufficient funds
        if (fromAccount.getBalance().compareTo(transferDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds in the source account");
        }

        // Perform the transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferDto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transferDto.getAmount()));

        // Save both the accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Record the transactions
        transactionService.recordTransaction(fromAccount.getId(), transferDto.getAmount().negate(), TransactionType.TRANSFER);
        transactionService.recordTransaction(toAccount.getId(), transferDto.getAmount(), TransactionType.TRANSFER);
    }

    @Transactional
    public void transferFromMasterAccount(Long toAccountId, BigDecimal amount) {
        Account masterAccount = findMasterAccount();

        if (masterAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in master account");
        }

        masterAccount.setBalance(masterAccount.getBalance().subtract(amount));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(masterAccount);
        accountRepository.save(toAccount);

        // Record the transaction from master to client account
        transactionService.recordTransaction(masterAccount.getId(), amount.negate(), TransactionType.TRANSFER);
    }

    @Transactional
    public void transferToMasterAccount(Long fromAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account masterAccount = findMasterAccount();

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in the source account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        masterAccount.setBalance(masterAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(masterAccount);

        transactionService.recordTransaction(masterAccount.getId(), amount, TransactionType.TRANSFER);
    }

    public Account findMasterAccount() {
        return accountRepository.findById(masterAccountId)
                .orElseThrow(() -> new RuntimeException("Master account not found"));
    }
}