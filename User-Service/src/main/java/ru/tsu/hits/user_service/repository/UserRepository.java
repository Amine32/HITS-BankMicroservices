package ru.tsu.hits.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.user_service.model.User;
import ru.tsu.hits.user_service.model.UserRole;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByEmailAndRole(String email, UserRole role);
}