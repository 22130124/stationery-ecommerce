package com.tqk.authservice.service;

import com.tqk.authservice.dto.request.ForgotPasswordRequest;
import com.tqk.authservice.dto.request.ResetPasswordRequest;
import com.tqk.authservice.exception.AuthException;
import com.tqk.authservice.model.Account;
import com.tqk.authservice.model.AuthProvider;
import com.tqk.authservice.model.PasswordResetToken;
import com.tqk.authservice.repository.AccountRepository;
import com.tqk.authservice.repository.AuthProviderRepository;
import com.tqk.authservice.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final AccountRepository accountRepository;
    private final AuthProviderRepository authProviderRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @Transactional
    public void sendResetPasswordEmail(ForgotPasswordRequest request) {
        System.out.println("Email nhận được: " + request.getEmail());
        Account account = accountRepository.findByEmail(request.getEmail()).orElse(null);
        if (account == null) {
            System.out.println("Không tìm thấy tài khoản này");
            return;
        }

        AuthProvider provider = authProviderRepository.findByAccount(account).orElseThrow();

        if (!"email".equals(provider.getProvider())) {
            System.out.println("Tài khoản này không phải đăng nhập bằng email");
            return;
        }

        tokenRepository.deleteByAccount(account);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setAccount(account);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        String url = "http://localhost:3000/reset-password?token=" + token;

        emailSenderService.send(account.getEmail(), "Đặt lại mật khẩu",
                "Click để đặt lại mật khẩu: " + url
        );
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AuthException("Token không hợp lệ"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("Token đã hết hạn");
        }

        AuthProvider provider = authProviderRepository.findByAccount(resetToken.getAccount()).orElseThrow();

        provider.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authProviderRepository.save(provider);

        tokenRepository.delete(resetToken);
    }
}
