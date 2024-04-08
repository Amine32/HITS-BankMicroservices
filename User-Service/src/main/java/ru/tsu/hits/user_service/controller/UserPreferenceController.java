package ru.tsu.hits.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.user_service.model.UserPreference;
import ru.tsu.hits.user_service.service.UserPreferenceService;

@RestController
@RequestMapping("/api/userPreferences")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserPreference> getUserPreferences(@PathVariable Long userId) {
        UserPreference preferences = userPreferenceService.getUserPreferences(userId);
        return ResponseEntity.ok(preferences);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<UserPreference> updateUserPreferences(@PathVariable Long userId, @RequestBody UserPreference preferences) {
        UserPreference updatedPreferences = userPreferenceService.saveUserPreferences(userId, preferences.getTheme(), preferences.getHiddenAccountIds());
        return ResponseEntity.ok(updatedPreferences);
    }
}

