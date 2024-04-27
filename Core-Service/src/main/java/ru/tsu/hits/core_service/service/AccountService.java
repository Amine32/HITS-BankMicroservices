package ru.tsu.hits.core_service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tsu.hits.core_service.dto.AccountTransferDto;
import ru.tsu.hits.core_service.model.Account;
import ru.tsu.hits.core_service.model.Currency;
import ru.tsu.hits.core_service.model.TransactionType;
import ru.tsu.hits.core_service.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final TransactionService transactionService;
    private final CurrencyConversionService currencyConversionService;
    private final IdempotencyCacheService idempotencyCacheService;
    private final AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static final int MAX_RETRY_COUNT = 5;

    @Value("${core.masterAccountId}")
    private String masterAccountId;

    public Account createAccount(Long ownerId, Currency currency) {
        Account account = new Account();
        account.setOwnerId(ownerId);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        account.setActive(true);
        account.setCurrency(currency != null ? currency : Currency.RUB);

        for (int retryCount = 0; retryCount < MAX_RETRY_COUNT; retryCount++) {
            String uniqueId = UUID.randomUUID().toString();
            account.setId(uniqueId);

            try {
                return accountRepository.save(account);
            } catch (DataIntegrityViolationException e) {
                logger.error("Attempt {} to save account failed due to ID collision.", retryCount + 1, e);
                if (retryCount == MAX_RETRY_COUNT - 1) {
                    logger.error("Maximum retry limit reached. Unable to create account due to repeated ID collisions.");
                    throw new RuntimeException("Failed to create account after " + MAX_RETRY_COUNT + " attempts due to ID collision.");
                }
            }
        }

        throw new RuntimeException("Account creation failed after retries.");
    }

    public Optional<Account> getAccount(String id) {
        return accountRepository.findById(id);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }

    public Account depositMoney(String id, BigDecimal amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));

        transactionService.recordTransaction(account.getId(), account.getOwnerId(), amount, TransactionType.DEPOSIT);

        return accountRepository.save(account);
    }

    public Account withdrawMoney(String id, BigDecimal amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));

        transactionService.recordTransaction(account.getId(), account.getOwnerId(), amount.negate(), TransactionType.WITHDRAWAL);

        return accountRepository.save(account);
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findAllByOwnerId(userId);
    }

    public String getPrimaryAccountId(Long userId) {
        List<Account> accounts = accountRepository.findAllByOwnerId(userId)
                .stream()
                .filter(account -> Currency.RUB.equals(account.getCurrency())) // Filter only RUB accounts
                .toList();

        return accounts.stream()
                .max(Comparator.comparing(Account::getBalance))
                .map(Account::getId)
                .orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public boolean isUserAccountOwner(String accountId, Long userId) {
        return accountRepository.findByIdAndOwnerId(accountId, userId).isPresent();
    }

    public void transferMoney(AccountTransferDto transferDto, Long userId) {
        Account fromAccount = accountRepository.findById(transferDto.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account toAccount = accountRepository.findById(transferDto.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        // Check if the user owns the source account
        if (!fromAccount.getOwnerId().equals(userId)) {
            throw new RuntimeException("User does not own the source account");
        }

        // Convert amount if necessary
        BigDecimal convertedAmount = currencyConversionService.convert(fromAccount.getCurrency(), toAccount.getCurrency(), transferDto.getAmount());

        // Check for sufficient funds
        if (fromAccount.getBalance().compareTo(transferDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds in the source account");
        }

        // Perform the transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferDto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(convertedAmount));

        // Save both the accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Record the transactions
        transactionService.recordTransaction(fromAccount.getId(), fromAccount.getOwnerId(), transferDto.getAmount().negate(), TransactionType.TRANSFER);
        transactionService.recordTransaction(toAccount.getId(), toAccount.getOwnerId(), transferDto.getAmount(), TransactionType.TRANSFER);
    }

    @Transactional
    public void transferFromMasterAccount(String toAccountId, BigDecimal amount) {
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

        transactionService.recordTransaction(masterAccount.getId(), masterAccount.getOwnerId(), amount.negate(), TransactionType.TRANSFER);
        transactionService.recordTransaction(toAccount.getId(), toAccount.getOwnerId(), amount, TransactionType.LOAN);

    }

    @Transactional
    public void transferToMasterAccount(String fromAccountId, BigDecimal amount) {
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

        transactionService.recordTransaction(masterAccount.getId(), masterAccount.getOwnerId(), amount, TransactionType.TRANSFER);
        transactionService.recordTransaction(fromAccount.getId(), fromAccount.getOwnerId(), amount, TransactionType.LOAN_PAYMENT);
    }

    public Account findMasterAccount() {
        return accountRepository.findById(masterAccountId)
                .orElseThrow(() -> new RuntimeException("Master account not found"));
    }

    @PostConstruct
    private void initializeMasterAccount() {
        if (!accountRepository.existsById(masterAccountId)) {
            Account masterAccount = new Account();
            masterAccount.setId(masterAccountId);
            masterAccount.setOwnerId(0L);
            masterAccount.setBalance(new BigDecimal("1000000000"));
            masterAccount.setCreatedAt(LocalDateTime.now());
            masterAccount.setActive(true);
            masterAccount.setCurrency(Currency.RUB);

            accountRepository.save(masterAccount);
        }
    }

    public Object checkAndCreateAccount(Long id, Currency currency, String idempotencyKey) {
        // Check if the idempotency key is already present
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return existingResponse;
        }

        Account newAccount = createAccount(id, currency);
        idempotencyCacheService.storeResponse(idempotencyKey, newAccount);
        return newAccount;
    }

    public ResponseEntity<?> performIdempotentTransfer(AccountTransferDto transferDto, Long userId, String idempotencyKey) {
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return ResponseEntity.ok(existingResponse);
        }

        try {
            transferMoney(transferDto, userId);
            idempotencyCacheService.storeResponse(idempotencyKey, "Transfer successful");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity<Account> performIdempotentDeposit(String accountId, BigDecimal amount, String idempotencyKey) {
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return ResponseEntity.ok((Account) existingResponse);
        }

        Account account = depositMoney(accountId, amount); // Actual deposit logic
        idempotencyCacheService.storeResponse(idempotencyKey, account);
        return ResponseEntity.ok(account);
    }

    public ResponseEntity<Account> performIdempotentWithdrawal(String accountId, BigDecimal amount, String idempotencyKey) {
        Object existingResponse = idempotencyCacheService.getResponse(idempotencyKey);
        if (existingResponse != null) {
            return ResponseEntity.ok((Account) existingResponse);
        }

        Account account = withdrawMoney(accountId, amount); // Actual withdrawal logic
        idempotencyCacheService.storeResponse(idempotencyKey, account);
        return ResponseEntity.ok(account);
    }
}