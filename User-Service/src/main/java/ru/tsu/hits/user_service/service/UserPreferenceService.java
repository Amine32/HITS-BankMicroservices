package ru.tsu.hits.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tsu.hits.user_service.model.UserPreference;
import ru.tsu.hits.user_service.repository.UserPreferenceRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final IdempotencyCacheService idempotencyCacheService;

    public UserPreference getUserPreferences(Long userId) {
        return userPreferenceRepository.findById(userId).orElse(new UserPreference());
    }

    public UserPreference saveUserPreferences(Long userId, String theme, Set<String> hiddenAccountIds) {
        UserPreference preferences = new UserPreference();
        preferences.setUserId(userId);
        preferences.setTheme(theme);
        preferences.setHiddenAccountIds(hiddenAccountIds);
        return userPreferenceRepository.save(preferences);
    }

    public void createUserPreference(Long userId) {
        UserPreference preference = new UserPreference();

        preference.setUserId(userId);
        preference.setTheme("light"); // Default theme

        userPreferenceRepository.save(preference);
    }

    public ResponseEntity<UserPreference> updateUserPreferencesWithIdempotency(Long userId, UserPreference preferences, String idempotencyKey) {
        if (idempotencyCacheService.getResponse(idempotencyKey) != null) {
            return ResponseEntity.ok((UserPreference) idempotencyCacheService.getResponse(idempotencyKey));
        }

        UserPreference updatedPreferences = saveUserPreferences(userId, preferences.getTheme(), preferences.getHiddenAccountIds());
        idempotencyCacheService.storeResponse(idempotencyKey, updatedPreferences);
        return ResponseEntity.ok(updatedPreferences);
    }
}
