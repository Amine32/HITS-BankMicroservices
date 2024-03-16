package ru.tsu.hits.bank_account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.bank_account_service.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}