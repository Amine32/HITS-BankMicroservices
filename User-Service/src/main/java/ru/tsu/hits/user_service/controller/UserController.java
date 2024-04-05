package ru.tsu.hits.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.user_service.dto.CreateUpdateUserDto;
import ru.tsu.hits.user_service.dto.LoginRequest;
import ru.tsu.hits.user_service.model.User;
import ru.tsu.hits.user_service.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUpdateUserDto createUserDto) {
        User user = userService.createUser(createUserDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!id.equals(user.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticate(@RequestBody LoginRequest loginDetails) {
        try {
            User authenticatedUser = userService.authenticate(loginDetails.getEmail(), loginDetails.getPassword());
            return ResponseEntity.ok(authenticatedUser);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        boolean success = userService.blockUser(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}