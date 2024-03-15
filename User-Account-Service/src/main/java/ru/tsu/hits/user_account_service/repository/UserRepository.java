package ru.tsu.hits.user_account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.user_account_service.model.User;
import ru.tsu.hits.user_account_service.model.UserRole;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndRole(String username, UserRole role);
}