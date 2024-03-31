package ru.tsu.hits.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tsu.hits.user_service.model.UserRole;


@Data
@AllArgsConstructor
public class CreateUpdateUserDto {

    private String email;
    private String password;
    private UserRole role;
}
