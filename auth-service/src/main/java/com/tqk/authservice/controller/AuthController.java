package com.tqk.authservice.controller;

import com.tqk.authservice.dto.request.AuthRequest;
import com.tqk.authservice.dto.request.GoogleAuthRequest;
import com.tqk.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleAuthRequest request) {
        String token = authService.loginGoogle(request.getToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    private ResponseEntity<?> signUp(@RequestBody AuthRequest request) {
        Integer accountId = authService.signUp(request);
        return ResponseEntity.ok(accountId);
    }
}
