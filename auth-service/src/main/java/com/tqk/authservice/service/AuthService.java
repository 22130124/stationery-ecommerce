package com.tqk.authservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.tqk.authservice.dto.request.AuthRequest;
import com.tqk.authservice.dto.response.AccountResponse;
import com.tqk.authservice.dto.response.LoginResponse;
import com.tqk.authservice.exception.AuthException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new AuthException("Thông tin đăng nhập không chính xác"));

        AuthProvider authProvider = authProviderRepository.findByAccount(account).orElseThrow(() -> new AuthException("Thông tin đăng nhập không chính xác"));

        if (!passwordEncoder.matches(password, authProvider.getPassword())) {
            throw new AuthException("Thông tin đăng nhập không chính xác");
        }

        if (!account.isVerified()) {
            throw new AuthException("Vui lòng vào hộp thư email để kích hoạt tài khoản");
        }

        if (!account.isActiveStatus()) {
            throw new AuthException("Tài khoản đã bị khóa. Vui lòng liên hệ qua gmail 22130124@st.hcmuaf.edu.vn để được hỗ trợ");
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
                throw new AuthException("Token Google ID không hợp lệ.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleUserId = payload.getSubject();

            // Tìm account, nếu không có thì tạo mới
            Account account = accountRepository.findByEmail(email)
                    .orElseGet(() -> createNewGoogleAccount(email, googleUserId));

            return generateToken(account);

        } catch (GeneralSecurityException | IOException e) {
            throw new AuthException("Lỗi khi xác thực Google token: " + e.getMessage());
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
            throw new AuthException("Email đã tồn tại");
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

    public String getEmail(Integer id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AuthException("Không tìm thấy tài khoản có id: " + id));
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
        Account account = accountRepository.findById(id).orElseThrow(() -> new AuthException("Không tìm thấy tài khoản có id: " + id));
        account.setActiveStatus(status);
        accountRepository.save(account);
        return account.getId();
    }

    public Integer changeRole(Integer id, String role) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AuthException("Không tìm thấy tài khoản có id: " + id));
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
