package com.example.sources.model.request.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordRequest {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
