package com.tqk.authservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.tqk.authservice.dto.request.AuthRequest;
import com.tqk.authservice.dto.response.AccountResponse;
import com.tqk.authservice.dto.response.LoginResponse;
import com.tqk.authservice.exception.ExceptionCode;
import com.tqk.authservice.model.Account;
import com.tqk.authservice.model.AuthProvider;
import com.tqk.authservice.model.PasswordResetToken;
import com.tqk.authservice.repository.AccountRepository;
import com.tqk.authservice.repository.AuthProviderRepository;
import com.tqk.authservice.repository.PasswordResetTokenRepository;
import com.tqk.authservice.repository.client.ProfileClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final AuthProviderRepository authProviderRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final ProfileClient profileClient;
    private final JavaMailSender mailSender;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION}")
    private long jwtExpiration;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    public LoginResponse login(AuthRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Account account = accountRepository.findByEmail(email).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.INVALID_CREDENTIALS.name()));

        AuthProvider authProvider = authProviderRepository.findByAccount(account).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.INVALID_CREDENTIALS.name()));

        if (!passwordEncoder.matches(password, authProvider.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.INVALID_CREDENTIALS.name());
        }

        if (!account.isVerified()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.ACCOUNT_NOT_VERIFIED.name());
        }

        if (!account.isActiveStatus()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.ACCOUNT_LOCKED.name());
        }

        LoginResponse response = new LoginResponse();
        response.setToken(generateToken(account));
        response.setEmail(account.getEmail());
        response.setRole(account.getRole());

        return response;
    }

    @Transactional
    public String loginGoogle(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.GOOGLE_TOKEN_INVALID.name());
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleUserId = payload.getSubject();

            // Tìm account, nếu không có thì tạo mới
            Account account = accountRepository.findByEmail(email)
                    .orElseGet(() -> createNewGoogleAccount(email, googleUserId));

            return generateToken(account);

        } catch (GeneralSecurityException | IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionCode.GOOGLE_TOKEN_VERIFICATION_FAILED.name());
        }
    }

    private Account createNewGoogleAccount(String email, String googleUserId) {
        // Tạo một Account mới
        Account account = new Account();
        account.setEmail(email);
        account.setRole("USER");
        account.setActiveStatus(true);
        accountRepository.save(account);

        // Tạo AuthProvider tương ứng
        AuthProvider authProvider = new AuthProvider();
        authProvider.setAccount(account);
        authProvider.setProvider("google");
        authProvider.setProviderId(googleUserId);
        authProviderRepository.save(authProvider);

        // Tạo một profile mới
        profileClient.createProfile(account.getId());

        return account;
    }

    @Transactional
    public Integer signUp(AuthRequest request) {
        String email = request.getEmail();

        if (accountRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.EMAIL_ALREADY_EXISTS.name());
        }

        String password = request.getPassword();
        Account account = new Account();
        account.setEmail(email);
        account.setRole("USER");
        account.setActiveStatus(true);
        account.setVerified(false);
        accountRepository.save(account);

        AuthProvider authProvider = new AuthProvider();
        authProvider.setAccount(account);
        authProvider.setProvider("email");
        authProvider.setPassword(passwordEncoder.encode(password));
        authProviderRepository.save(authProvider);

        emailVerificationService.sendVerificationEmail(account);

        return account.getId();
    }

    public String generateToken(Account account) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        long now = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", account.getRole());
        claims.put("account_id", account.getId());

        return Jwts
                .builder()
                .claims(claims)
                .subject(account.getEmail())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtExpiration))
                .signWith(key)
                .compact();
    }

    public LoginResponse getInfoByAccountId(Integer accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.ACCOUNT_NOT_FOUND.name()));
        LoginResponse response = new LoginResponse();
        response.setEmail(account.getEmail());
        response.setRole(account.getRole());
        return response;
    }

    public String getEmail(Integer id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.ACCOUNT_NOT_FOUND.name()));
        return account.getEmail();
    }

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(Account account : accounts) {
            accountResponseList.add(account.convertToDto());
        }
        return accountResponseList;
    }

    public Integer changeStatus(Integer id, boolean status) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.ACCOUNT_NOT_FOUND.name()));
        account.setActiveStatus(status);
        accountRepository.save(account);
        return account.getId();
    }

    public Integer changeRole(Integer id, String role) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionCode.ACCOUNT_NOT_FOUND.name()));
        account.setRole(role);
        accountRepository.save(account);
        return account.getId();
    }

    @Transactional
    public void sendResetPasswordEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElse(null);

        if (account == null) return;

        AuthProvider provider = authProviderRepository.findByAccount(account).orElseThrow();

        if (!"email".equals(provider.getProvider())) return;

        passwordResetTokenRepository.deleteByAccount(account);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setAccount(account);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(resetToken);

        String resetUrl = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(account.getEmail());
        mail.setSubject("Đặt lại mật khẩu");
        mail.setText("Click để đặt lại mật khẩu: " + resetUrl);

        mailSender.send(mail);
    }
}
