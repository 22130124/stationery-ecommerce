package com.tqk.stationeryecommercebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tqk.stationeryecommercebackend.request.LoginRequest;
import com.tqk.stationeryecommercebackend.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        Map<String, Object> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }
}
