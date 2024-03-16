package ru.tsu.hits.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String passportNumber;

    @Column(nullable = false)
    private String passportSeries;
}