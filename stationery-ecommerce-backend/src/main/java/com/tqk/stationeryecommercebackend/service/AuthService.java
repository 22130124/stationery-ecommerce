package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.exception.AuthException;
import com.tqk.stationeryecommercebackend.model.Account;
import com.tqk.stationeryecommercebackend.model.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tqk.stationeryecommercebackend.repository.AccountRepository;
import com.tqk.stationeryecommercebackend.repository.AuthProviderRepository;
import com.tqk.stationeryecommercebackend.request.LoginRequest;

@Service
public class AuthService {
    private AccountRepository accountRepository;
    private AuthProviderRepository authProviderRepository;

    @Autowired
    public AuthService(AccountRepository accountRepository, AuthProviderRepository authProviderRepository) {
        this.accountRepository = accountRepository;
        this.authProviderRepository = authProviderRepository;
    }

    public String login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AuthException("Username or password is incorrect"));

        AuthProvider authProvider = authProviderRepository.findByAccount(account).orElseThrow(() -> new AuthException("Username or password is incorrect"));

        if (!password.equals(authProvider.getPassword())) {
            throw new AuthException("Username or password is incorrect");
        }

        return "123";
    }
}
