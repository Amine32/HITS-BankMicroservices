package ru.tsu.hits.core_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.core_service.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByOwnerId(Long ownerId);

    Optional<Account> findByIdAndOwnerId(Long accountId, Long userId);
}