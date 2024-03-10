package ru.tsu.hits.user_account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.user_account_service.model.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
}
