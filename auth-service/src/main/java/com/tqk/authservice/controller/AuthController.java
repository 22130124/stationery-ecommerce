package com.tqk.authservice.controller;

import com.tqk.authservice.dto.request.AuthRequest;
import com.tqk.authservice.dto.request.GoogleAuthRequest;
import com.tqk.authservice.service.AuthService;
import com.tqk.authservice.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public AuthController(AuthService authService, EmailVerificationService emailVerificationService) {
        this.authService = authService;
        this.emailVerificationService = emailVerificationService;
    }


    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        Map<String, String> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleAuthRequest request) {
        String token = authService.loginGoogle(request.getToken());
        Map<String, String> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    private ResponseEntity<?> signUp(@RequestBody AuthRequest request) {
        Integer accountId = authService.signUp(request);
        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        emailVerificationService.verifyAccount(token);
        return ResponseEntity.ok(Map.of("message", "Xác minh tài khoản thành công"));
    }
}
