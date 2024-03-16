package ru.tsu.hits.bank_account_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.tsu.hits.bank_account_service.client.UserClient;
import ru.tsu.hits.bank_account_service.model.Account;
import ru.tsu.hits.bank_account_service.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserClient userClient;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserClient userClient) {
        this.accountRepository = accountRepository;
        this.userClient = userClient;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Mono<Account> getAccountWithUser(Long id) {
        Optional<Account> accountOptional = getAccount(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            return userClient.getUser(account.getUserId())
                    .map(user -> {
                        account.setUserId(user.getId());
                        return account;
                    });
        }
        return Mono.empty();
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}