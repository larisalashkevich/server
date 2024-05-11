package com.example.sources.service;

import com.example.sources.domain.RegistrationKey;
import com.example.sources.domain.Role;
import com.example.sources.repo.AccountRepo;
import com.example.sources.repo.RegistrationKeyRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@AllArgsConstructor
public class RegistrationKeyService {
    private final RegistrationKeyRepo registrationKeyRepo;
    private final AccountRepo accountRepo;

    private String getKey(){
        Random random = new Random((new Date().getTime()));
        String str = "";
        for(int i=0; i<16; ++i){
            str += (char)(Math.abs(random.nextInt()%26) + (random.nextInt()%2==0?  + 'a' : 'A'));

        }

        return str;
    }

    public String generateKey(String username, Role role){
        RegistrationKey key = registrationKeyRepo.findByUsername(username);
        if(key == null)
            key = RegistrationKey.builder()
                    .username(username)
                    .build();

        key.setRole(role);
        key.setKey(getKey());

        registrationKeyRepo.save(key);

        return key.getKey();
    }

    public RegistrationKey matchKey(String username, String key){
        return registrationKeyRepo.findByUsernameAndKey(username, key);
    }

    public void clearKeys(String username){
        RegistrationKey key = registrationKeyRepo.findByUsername(username);
        registrationKeyRepo.delete(key);
    }
}
