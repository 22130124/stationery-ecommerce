package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.request.GoogleAuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tqk.stationeryecommercebackend.request.AuthRequest;
import com.tqk.stationeryecommercebackend.service.AuthService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    private ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        String token = authService.login(request);
        Map<String, Object> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@Valid @RequestBody GoogleAuthRequest request) {
        String token = authService.loginWithGoogle(request.getToken());
        Map<String, Object> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    private ResponseEntity<?> signUp(@Valid @RequestBody AuthRequest request) {
        Integer accountId = authService.signUp(request);
        Map<String, Object> response = Map.of("accountId", accountId);
        return ResponseEntity.ok(response);
    }
}
