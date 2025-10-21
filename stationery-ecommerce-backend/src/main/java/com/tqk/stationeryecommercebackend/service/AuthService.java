package com.tqk.stationeryecommercebackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.tqk.stationeryecommercebackend.exception.AuthException;
import com.tqk.stationeryecommercebackend.model.Account;
import com.tqk.stationeryecommercebackend.model.AuthProvider;
import com.tqk.stationeryecommercebackend.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tqk.stationeryecommercebackend.repository.AccountRepository;
import com.tqk.stationeryecommercebackend.repository.AuthProviderRepository;
import com.tqk.stationeryecommercebackend.dto.auth.AuthRequest;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final AuthProviderRepository authProviderRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client-id}")
    private String googleClientId;

    @Autowired
    public AuthService(AccountRepository accountRepository, AuthProviderRepository authProviderRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.authProviderRepository = authProviderRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AuthException("Username or password is incorrect"));

        AuthProvider authProvider = authProviderRepository.findByAccount(account).orElseThrow(() -> new AuthException("Username or password is incorrect"));

        if (!passwordEncoder.matches(password, authProvider.getPassword())) {
            throw new AuthException("Username or password is incorrect");
        }

        return jwtTokenProvider.generateToken(account);
    }

    @Transactional
    public String loginWithGoogle(String idTokenString) {
        try {
            // Xác thực ID Token từ Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new AuthException("Invalid Google ID token.");
            }

            // Lấy thông tin người dùng từ payload
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleUserId = payload.getSubject(); // ID duy nhất của người dùng Google

            // Tìm tài khoản bằng email. Nếu không tồn tại, tạo mới.
            Account account = accountRepository.findByEmail(email) // Tìm bằng email thay vì username
                    .orElseGet(() -> createNewGoogleAccount(email, googleUserId));

            // Tạo và trả về JWT token của ứng dụng
            return jwtTokenProvider.generateToken(account);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error verifying Google token", e);
        }
    }

    private Account createNewGoogleAccount(String email, String googleUserId) {
        // Tạo một Account mới
        Account account = new Account();
        account.setEmail(email);
        account.setRole("USER");
        account.setIsActive(true);
        accountRepository.save(account);

        // Tạo AuthProvider tương ứng
        AuthProvider authProvider = new AuthProvider();
        authProvider.setAccount(account);
        authProvider.setProvider("google");
        authProvider.setProviderId(googleUserId);
        authProviderRepository.save(authProvider);

        return account;
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
            authProvider.setProvider("local");
            authProvider.setPassword(passwordEncoder.encode(password));
            authProviderRepository.save(authProvider);

            return account.getId();
        } catch (DataIntegrityViolationException e) {
            throw new AuthException("Invalid data: some required fields are missing or invalid");
        } catch (ConstraintViolationException e) {
            throw new AuthException("Database constraint violation: " + e.getMessage());
        }
    }
}
