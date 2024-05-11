package com.example.sources.model.request.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitRequest {
    private String username;
    private String password;
}

