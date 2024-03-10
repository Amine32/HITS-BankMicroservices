package ru.tsu.hits.user_account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.user_account_service.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
