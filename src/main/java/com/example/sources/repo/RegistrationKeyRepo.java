package com.example.sources.repo;

import com.example.sources.domain.RegistrationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationKeyRepo extends JpaRepository<RegistrationKey, Long> {
    RegistrationKey findByUsername(String username);
    RegistrationKey findByUsernameAndKey(String username, String key);

}
