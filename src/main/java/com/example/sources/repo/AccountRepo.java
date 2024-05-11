package com.example.sources.repo;

import com.example.sources.domain.Account;
import com.example.sources.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findByRole(Role role);
}
