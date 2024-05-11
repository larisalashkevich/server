package com.example.sources.model.request.profile_info;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProfileInfoRequest {
    private Long id;
    private String firstname;
    private String surname;
    private String lastname;
    private String telephone;
    private String address;
}
