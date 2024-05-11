package com.example.sources.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RegistrationKey")
public class RegistrationKey {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Ключ не может быть пустым")
    private String key;
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    private Role role;
}
