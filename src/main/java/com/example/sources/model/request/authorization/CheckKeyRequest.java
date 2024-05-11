package com.example.sources.model.request.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckKeyRequest {
    private String username;
    private String password;
    private String registrationKey;
}
