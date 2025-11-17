package com.tqk.authservice.service;

import com.tqk.authservice.exception.AuthException;
import com.tqk.authservice.model.Account;
import com.tqk.authservice.model.EmailVerificationToken;
import com.tqk.authservice.repository.AccountRepository;
import com.tqk.authservice.repository.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailVerificationService(EmailVerificationTokenRepository tokenRepository,
                                    AccountRepository accountRepository,
                                    JavaMailSender mailSender) {
        this.tokenRepository = tokenRepository;
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
    }

    // Hàm xử lý gửi email xác nhận
    public void sendVerificationEmail(Account account) {
        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAccount(account);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // token 24h
        tokenRepository.save(verificationToken);

        String verificationUrl = "http://localhost:3000/verify?token=" + token;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(account.getEmail());
        mail.setSubject("Xác thực email của bạn");
        mail.setText("Click link để kích hoạt tài khoản: " + verificationUrl);

        mailSender.send(mail);
    }

    // Hàm xử lý xác minh token
    public void verifyAccount(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Token xác thực không hợp lệ"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthException("Token đã hết hạn");
        }

        Account account = verificationToken.getAccount();
        account.setVerified(true);
        accountRepository.save(account);

        tokenRepository.delete(verificationToken);
    }
}
