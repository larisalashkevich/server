package com.example.sources.service;

import com.example.sources.domain.Account;
import com.example.sources.domain.Role;
import com.example.sources.model.request.authorization.SignUpRequest;
import com.example.sources.repo.AccountRepo;
import com.example.sources.utils.exception.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepo accountRepo;

    public Account create(SignUpRequest dto){
        Account account = Account.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(Role.fromString("ADMIN"))
                .build();

        return accountRepo.save(account);
    }

    public List<Account> readAll(){
        return accountRepo.findAll();
    }

    public Account update(Account account){
        return accountRepo.save(account);
    }

    public void delete(Long id){
        accountRepo.deleteById(id);
    }

    public void ban(Long id) throws AccountNotFoundException {
        Optional<Account> account = accountRepo.findById(id);
        if(!account.isPresent())
            throw new AccountNotFoundException(id.toString());

        Account newState = account.get();
        newState.setActive(!newState.isActive());

        accountRepo.save(newState);
    }
}
