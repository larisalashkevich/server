package com.example.sources.service;

import com.example.sources.domain.Account;
import com.example.sources.domain.ProfileInfo;
import com.example.sources.model.request.profile_info.CreateProfileInfoRequest;
import com.example.sources.model.request.profile_info.UpdateProfileInfoRequest;
import com.example.sources.model.response.profile_info.ProfileInfoResponse;
import com.example.sources.repo.AccountRepo;
import com.example.sources.repo.ProfileInfoRepo;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.ProfileInfoNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileInfoService {
    private AccountRepo accountRepo;
    private ProfileInfoRepo profileInfoRepo;

    public ProfileInfoResponse create(CreateProfileInfoRequest request) throws AccountNotFoundException {
        Optional<Account> account = accountRepo.findById(request.getAccountId());

        if(!account.isPresent())
            throw new AccountNotFoundException(request.getAccountId().toString());

        ProfileInfo personalInfo = ProfileInfo.builder()
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .lastname(request.getLastname())
                .address(request.getAddress())
                .telephone(request.getTelephone())
                .account(account.get())
                .build();

        return new ProfileInfoResponse(profileInfoRepo.save(personalInfo));
    }
    public List<ProfileInfoResponse> readAll(){
        List<ProfileInfo> infos = profileInfoRepo.findAll();

        List<ProfileInfoResponse> response = new ArrayList<>();
        for(ProfileInfo info : infos)
            response.add(new ProfileInfoResponse(info));

        return response;
    }
    public ProfileInfoResponse readById(Long id) throws ProfileInfoNotFoundException {
        Optional<ProfileInfo> info = profileInfoRepo.findById(id);

        if(!info.isPresent())
            throw new ProfileInfoNotFoundException(id.toString());

        return new ProfileInfoResponse(info.get());
    }

    public ProfileInfoResponse readByAccountId(Long id) throws ProfileInfoNotFoundException {
        ProfileInfo info = profileInfoRepo.findByAccount_Id(id);

        if(info == null)
            throw new ProfileInfoNotFoundException(id.toString());

        return new ProfileInfoResponse(info);
    }
    public ProfileInfoResponse update(UpdateProfileInfoRequest request) throws ProfileInfoNotFoundException {
        Optional<ProfileInfo> oldState = profileInfoRepo.findById(request.getId());

        if(!oldState.isPresent())
            throw new ProfileInfoNotFoundException(request.getId().toString());

        ProfileInfo newState = oldState.get();
        Hibernate.initialize(newState.getAccount());
        newState.setFirstname(request.getFirstname());
        newState.setSurname(request.getSurname());
        newState.setLastname(request.getLastname());
        newState.setAddress(request.getAddress());
        newState.setTelephone(request.getTelephone());

        newState = profileInfoRepo.save(newState);

        return new ProfileInfoResponse(newState);
    }

    public void delete(Long id) throws ProfileInfoNotFoundException {
        Optional<ProfileInfo> info = profileInfoRepo.findById(id);

        if(!info.isPresent())
            throw new ProfileInfoNotFoundException(id.toString());

        profileInfoRepo.deleteById(id);
    }
}
