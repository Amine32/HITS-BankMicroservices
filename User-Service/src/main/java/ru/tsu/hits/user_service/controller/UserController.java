package ru.tsu.hits.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.user_service.dto.UserDto;
import ru.tsu.hits.user_service.dto.converter.UserDtoConverter;
import ru.tsu.hits.user_service.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    @Autowired
    public UserController(UserService userService, UserDtoConverter userDtoConverter) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userDtoConverter.convertToDto(userService.createUser(userDtoConverter.convertToEntity(userDto))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.getUser(id).map(userDtoConverter::convertToDto);
        return userDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        if (!id.equals(userDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userDtoConverter.convertToDto(userService.updateUser(userDtoConverter.convertToEntity(userDto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserDto userDto) {
        Optional<String> token = userService.authenticate(userDto.getUsername(), userDto.getPassword());
        return token.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}