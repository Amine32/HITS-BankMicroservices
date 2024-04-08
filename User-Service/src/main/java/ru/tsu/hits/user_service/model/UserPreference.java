package ru.tsu.hits.user_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
public class UserPreference {

    @Id
    private Long userId;

    @Column
    private String theme; // "dark" or "light"

    @ElementCollection
    private Set<Long> hiddenAccountIds = new HashSet<>();
}
