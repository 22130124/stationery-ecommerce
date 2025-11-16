package com.tqk.authservice.repository;

import com.tqk.authservice.model.Account;
import com.tqk.authservice.model.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Integer> {
    Optional<AuthProvider> findByAccount(Account account);
}
