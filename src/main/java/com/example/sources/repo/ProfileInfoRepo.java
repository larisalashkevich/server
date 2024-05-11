package com.example.sources.repo;

import com.example.sources.domain.ProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileInfoRepo extends JpaRepository<ProfileInfo, Long> {
    ProfileInfo findByAccount_Id(Long id);
}
