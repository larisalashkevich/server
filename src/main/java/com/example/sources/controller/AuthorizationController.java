package com.example.sources.controller;

import com.example.sources.domain.Account;
import com.example.sources.domain.ProfileInfo;
import com.example.sources.domain.RegistrationKey;
import com.example.sources.domain.Role;
import com.example.sources.model.request.authorization.*;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.model.response.JwtResponse;
import com.example.sources.model.response.MessageResponse;
import com.example.sources.repo.AccountRepo;
import com.example.sources.repo.ProfileInfoRepo;
import com.example.sources.service.MailService;
import com.example.sources.service.RegistrationKeyService;
import com.example.sources.service.UserService;
import com.example.sources.utils.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthorizationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepo accountRepo;
    private final ProfileInfoRepo profileInfoRepo;
    private final MailService mailService;
    private final RegistrationKeyService registrationKeyService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Account account = accountRepo.findByUsername(loginRequest.getUsername());
        if(account == null)
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("USERNAME","Error: Account isn`t exists"));

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        account = (Account) authentication.getPrincipal();

        if(!account.isActive()){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("ACTIVE","Error: Account was been banned"));
        }

        return ResponseEntity.ok(new JwtResponse(jwt, account.getId(), account.getUsername(), account.getRole().toString(), account.isActive()));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request){
        Account account = accountRepo.findByUsername(request.getUsername());
        if(account != null){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("USERNAME","Error: Account already exists"));
        }

        String key = registrationKeyService.generateKey(request.getUsername(), Role.USER);

        String title = "Ваш код регистрации.";
        String text = "Ваш код регистрации: " + key + "\n";
        text += "Никому не сообщайте и не передавайте данный код во избежание кражи ваших личных данных!";

        mailService.send(title, text, request.getUsername());

        return ResponseEntity.ok(new MessageResponse("Check key sent to " + request.getUsername()));
    }

    @PostMapping("/checkKey")
    public ResponseEntity<?> checkKey(@RequestBody CheckKeyRequest request){
        Account account = accountRepo.findByUsername(request.getUsername());
        if(account != null){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("USERNAME","Error: Account already exists"));
        }

        RegistrationKey registrationKey = registrationKeyService.matchKey(request.getUsername(), request.getRegistrationKey());
        if(registrationKey == null)
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("KEY","Error: Invalid registration key"));

        account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .role(registrationKey.getRole())
                .build();

        account = accountRepo.save(account);

        ProfileInfo profileInfo = ProfileInfo.builder()
                .account(account)
                .build();

        profileInfo = profileInfoRepo.save(profileInfo);

        registrationKeyService.clearKeys(request.getUsername());

        return ResponseEntity.ok(new MessageResponse("Account created"));
    }

    @PostMapping("/init")
    public ResponseEntity<?> init(@RequestBody InitRequest initRequest){
        Account account = accountRepo.findByRole(Role.ADMIN);
        if(account != null){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("ROLE","Error: Server already initialized"));
        }

        Account newAccount = Account.builder()
                .username(initRequest.getUsername())
                .password(passwordEncoder.encode(initRequest.getPassword()))
                .active(true)
                .role(Role.ADMIN)
                .build();

        accountRepo.save(newAccount);

        return ResponseEntity.ok(new MessageResponse("Account created"));
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        Optional<Account> account = accountRepo.findById(request.getId());
        if(!account.isPresent()){
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("ID","Error: Account doesn't exists"));
        }

        Account newState = account.get();
        if(!passwordEncoder.matches(request.getOldPassword(), newState.getPassword()))
            return ResponseEntity.badRequest().body(new ErrorMessageResponse("PASSWORD","Error: Invalid password"));

        newState.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepo.save(newState);

        return  ResponseEntity.ok(new MessageResponse("Password changed"));
    }
}
