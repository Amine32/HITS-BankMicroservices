package ru.tsu.hits.core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.core_service.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}