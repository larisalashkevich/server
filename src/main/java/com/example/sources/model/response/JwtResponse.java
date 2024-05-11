package com.example.sources.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String role;
    private boolean active;

    public JwtResponse(String token, Long id, String username, String role, boolean active) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.role = role;
        this.active = active;
    }
}
