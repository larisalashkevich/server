package com.example.sources.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }

    public static Role fromString(String str){
        Role result = ADMIN;
        if(str.equals("USER"))
            result = USER;

        return result;
    }
}
