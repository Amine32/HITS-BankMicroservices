package ru.tsu.hits.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.user_service.dto.CreateUpdateUserDto;
import ru.tsu.hits.user_service.model.User;
import ru.tsu.hits.user_service.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User createUser(CreateUpdateUserDto createUserDto) {
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());
        user.setRole(createUserDto.getRole());
        user.setActive(true); // Assuming a new user should be active by default
        return userRepository.save(user);
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

    public User authenticate(String email, String password) {
        return userRepository.findByEmail(email).map(user -> {
            if (user.getPassword().equals(password)) {
                System.out.println("User found - Authentication successful");
                return user;
            } else {
                System.out.println("Authentication failed - Incorrect password");
                throw new RuntimeException("Incorrect password."); // Or a more specific exception
            }
        }).orElseThrow(() -> new RuntimeException("User not found.")); // Or a more specific exception
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean blockUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false); // Assuming 'active' means not blocked. Adjust if your logic is different.
            userRepository.save(user);
            return true;
        }
        return false;
    }
}