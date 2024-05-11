package com.example.sources.controller;

import com.example.sources.model.request.profile_info.CreateProfileInfoRequest;
import com.example.sources.model.request.profile_info.UpdateProfileInfoRequest;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.service.ProfileInfoService;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.ProfileInfoNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/profile_info")
public class ProfileInfoController {
    private final ProfileInfoService profileInfoService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateProfileInfoRequest request){
        try{
            return new ResponseEntity<>(profileInfoService.create(request), HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ACCOUNT_ID", "Account doesn't exists with id: " + request.getAccountId()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> readAll(){
        return new ResponseEntity<>(profileInfoService.readAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(profileInfoService.readById(id), HttpStatus.OK);
        } catch (ProfileInfoNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "ProfileInfo doesn't exists with id: " + id), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/account/{id}")
    public ResponseEntity<?> readByAccountId(@PathVariable Long id){
        try{
            return new ResponseEntity<>(profileInfoService.readByAccountId(id), HttpStatus.OK);
        } catch (ProfileInfoNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ACCOUNT_ID", "ProfileInfo doesn't exists with accountId: " + id), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateProfileInfoRequest request){
        try{
            return new ResponseEntity<>(profileInfoService.update(request), HttpStatus.OK);
        } catch (ProfileInfoNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "ProfileInfo doesn't exists with id: " + request.getId()), HttpStatus.BAD_REQUEST);
        }
    }
}
