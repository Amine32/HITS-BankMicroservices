package ru.tsu.hits.user_account_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.user_account_service.exception.UserProfileNotFoundException;
import ru.tsu.hits.user_account_service.model.UserProfile;
import ru.tsu.hits.user_account_service.repository.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public UserProfile createOrUpdateUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Transactional(readOnly = true)
    public UserProfile findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException("Profile for user id " + userId + " not found"));
    }

    // Additional methods as needed
}

