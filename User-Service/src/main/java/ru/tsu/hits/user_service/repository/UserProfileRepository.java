package ru.tsu.hits.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.user_service.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long userId);
}