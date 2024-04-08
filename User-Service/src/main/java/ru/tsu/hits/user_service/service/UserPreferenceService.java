package ru.tsu.hits.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.user_service.model.UserPreference;
import ru.tsu.hits.user_service.repository.UserPreferenceRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreference getUserPreferences(Long userId) {
        return userPreferenceRepository.findById(userId).orElse(new UserPreference());
    }

    public UserPreference saveUserPreferences(Long userId, String theme, Set<Long> hiddenAccountIds) {
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
}
