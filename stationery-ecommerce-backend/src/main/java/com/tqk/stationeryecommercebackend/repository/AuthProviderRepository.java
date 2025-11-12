package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.Account;
import com.tqk.stationeryecommercebackend.model.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.Optional;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Integer> {
    Optional<AuthProvider> findByAccount(Account account);
    Optional<AuthProvider> findByAccount_Email(String email);
}
