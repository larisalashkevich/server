package com.example.sources.model.response.profile_info;

import com.example.sources.domain.ProfileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;
@Data
@AllArgsConstructor
public class ProfileInfoResponse {
    private Long id;
    private Long accountId;
    private String username;
    private String firstname;
    private String surname;
    private String lastname;
    private String telephone;
    private String address;
    private Boolean isActive;

    public ProfileInfoResponse(ProfileInfo profileInfo){
        Hibernate.initialize(profileInfo.getAccount());

        this.id = profileInfo.getId();
        this.accountId = profileInfo.getAccount().getId();
        this.isActive = profileInfo.getAccount().isActive();
        this.username = profileInfo.getAccount().getUsername();
        this.firstname = profileInfo.getFirstname();
        this.surname = profileInfo.getSurname();
        this.lastname = profileInfo.getLastname();
        this.telephone = profileInfo.getTelephone();
        this.address = profileInfo.getAddress();
    }
}
