package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);
}