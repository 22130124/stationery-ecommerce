package com.tqk.authservice.repository;

import com.tqk.authservice.model.Account;
import com.tqk.authservice.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByAccount(Account account);
}

