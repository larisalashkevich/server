package com.example.sources.model.response;

import com.example.sources.domain.RegistrationKey;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationKeyResponse {
    private Long id;
    private String key;
    private String username;
    private String role;

    public RegistrationKeyResponse(RegistrationKey registrationKey){
        this.id = registrationKey.getId();
        this.key = registrationKey.getKey();
        this.username = registrationKey.getUsername();
        this.role = registrationKey.getRole().toString();
    }
}
