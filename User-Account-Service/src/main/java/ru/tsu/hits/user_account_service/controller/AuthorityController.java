package ru.tsu.hits.user_account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.user_account_service.model.AuthorityEntity;
import ru.tsu.hits.user_account_service.service.AuthorityService;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class AuthorityController {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorityEntity>> getAllRoles() {
        List<AuthorityEntity> roles = authorityService.findAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<AuthorityEntity> createRole(@RequestBody AuthorityEntity authority) {
        AuthorityEntity createdAuthority = authorityService.createRole(authority);
        return ResponseEntity.ok(createdAuthority);
    }

    // Add endpoints as needed for updating and deleting roles
}

