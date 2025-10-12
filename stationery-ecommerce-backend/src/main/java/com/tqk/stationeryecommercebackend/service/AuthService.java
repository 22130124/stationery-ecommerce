package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.exception.AuthException;
import com.tqk.stationeryecommercebackend.model.Account;
import com.tqk.stationeryecommercebackend.model.AuthProvider;
import com.tqk.stationeryecommercebackend.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.tqk.stationeryecommercebackend.repository.AccountRepository;
import com.tqk.stationeryecommercebackend.repository.AuthProviderRepository;
import com.tqk.stationeryecommercebackend.request.AuthRequest;

import javax.validation.ConstraintViolationException;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final AuthProviderRepository authProviderRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(AccountRepository accountRepository, AuthProviderRepository authProviderRepository, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.authProviderRepository = authProviderRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AuthException("Username or password is incorrect"));

        AuthProvider authProvider = authProviderRepository.findByAccount(account).orElseThrow(() -> new AuthException("Username or password is incorrect"));

        if (!password.equals(authProvider.getPassword())) {
            throw new AuthException("Username or password is incorrect");
        }

        return jwtTokenProvider.generateToken(account);
    }

    @Transactional
    public Integer signUp(AuthRequest request) {
        try {
            String username = request.getUsername();

            if (accountRepository.existsByUsername(username)) {
                throw new AuthException("Username is already in use");
            }

            String password = request.getPassword();
            Account account = new Account();
            account.setUsername(username);
            account.setRole("USER");
            account.setIsActive(true);
            accountRepository.save(account);

            AuthProvider authProvider = new AuthProvider();
            authProvider.setAccount(account);
            authProvider.setPassword(password);
            authProvider.setProvider("local");
            authProviderRepository.save(authProvider);

            return account.getId();
        } catch (DataIntegrityViolationException e) {
            throw new AuthException("Invalid data: some required fields are missing or invalid");
        } catch (ConstraintViolationException e) {
            throw new AuthException("Database constraint violation: " + e.getMessage());
        }
    }
}
