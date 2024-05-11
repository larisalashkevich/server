package com.example.sources.service;

import com.example.sources.domain.Account;
import com.example.sources.repo.AccountRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final AccountRepo accountRepo;
    public UserService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepo.findByUsername(username);

        if(account == null)
            throw new UsernameNotFoundException("Not found");

        return account;
    }
}
