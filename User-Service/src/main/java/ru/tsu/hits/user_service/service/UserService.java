package ru.tsu.hits.user_service.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.user_service.dto.CreateUpdateUserDto;
import ru.tsu.hits.user_service.model.User;
import ru.tsu.hits.user_service.model.UserRole;
import ru.tsu.hits.user_service.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPreferenceService userPreferenceService;
    private final IdempotencyCacheService idempotencyCacheService;


    public User createUser(CreateUpdateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setRoles(createUserDto.getRoles()); // Now setting a set of roles
        user.setActive(true);

        User savedUser = userRepository.save(user);

        System.out.println("Saved user roles: " + user.getRoles());

        //create default user preference
        userPreferenceService.createUserPreference(savedUser.getId());

        return savedUser;
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean blockUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(!user.isActive());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.isActive(),
                getAuthorities(user.getRoles())); // Note the plural 'getRoles'
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<UserRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<User> createUserWithIdempotency(CreateUpdateUserDto createUserDto, String idempotencyKey) {
        if (idempotencyCacheService.getResponse(idempotencyKey) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = createUser(createUserDto); // Business logic for user creation
        idempotencyCacheService.storeResponse(idempotencyKey, user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<User> updateUserWithIdempotency(User user, String idempotencyKey) {
        if (idempotencyCacheService.getResponse(idempotencyKey) != null) {
            return ResponseEntity.ok(user);
        }

        User updatedUser = updateUser(user); // Business logic for user update
        idempotencyCacheService.storeResponse(idempotencyKey, updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @Getter
    @Setter
    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
        private final Long userId;

        public CustomUserDetails(Long userId, String username, String password, boolean enabled,
                                 Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, true, true, true, authorities);
            this.userId = userId;
        }
    }
}