package com.tqk.authservice.repository;

import com.tqk.authservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
}