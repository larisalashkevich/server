package com.example.sources.controller;

import com.example.sources.domain.Account;
import com.example.sources.model.request.authorization.SignUpRequest;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.model.response.MessageResponse;
import com.example.sources.service.AccountService;
import com.example.sources.utils.exception.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody SignUpRequest dto){
        return new ResponseEntity<>(accountService.create(dto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Account>> readAll(){
        return new ResponseEntity<>(accountService.readAll(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Account> update(@RequestBody Account account){
        return new ResponseEntity<>(accountService.update(account), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id){
        try {
            accountService.delete(id);
        }
        catch (Exception e){
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> ban(@PathVariable Long id){
        try{
            accountService.ban(id);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Account doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new MessageResponse("Account status changed"), HttpStatus.OK);
    }
}
