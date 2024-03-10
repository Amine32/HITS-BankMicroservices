package ru.tsu.hits.user_account_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.user_account_service.model.AuthorityEntity;
import ru.tsu.hits.user_account_service.repository.AuthorityRepository;

import java.util.List;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Transactional
    public AuthorityEntity createRole(AuthorityEntity authority) {
        return authorityRepository.save(authority);
    }

    @Transactional(readOnly = true)
    public List<AuthorityEntity> findAll() {
        return authorityRepository.findAll();
    }

    // Additional methods for updating and deleting authorities
}

