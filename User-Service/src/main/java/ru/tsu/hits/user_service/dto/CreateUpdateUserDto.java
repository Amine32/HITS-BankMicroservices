package ru.tsu.hits.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.user_service.model.UserRole;

import java.util.Set;


@Data
@AllArgsConstructor
public class CreateUpdateUserDto {

    private String email;
    private String password;
    private Set<UserRole> roles;
}
