package ru.tsu.hits.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.user_service.model.UserPreference;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
}

