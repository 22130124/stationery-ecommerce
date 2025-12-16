package com.tqk.authservice.controller;

import com.tqk.authservice.dto.request.AuthRequest;
import com.tqk.authservice.dto.request.ForgotPasswordRequest;
import com.tqk.authservice.dto.request.GoogleAuthRequest;
import com.tqk.authservice.dto.request.ResetPasswordRequest;
import com.tqk.authservice.dto.response.AccountResponse;
import com.tqk.authservice.service.AuthService;
import com.tqk.authservice.service.EmailVerificationService;
import com.tqk.authservice.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    @GetMapping("/admin")
    public ResponseEntity<?> getAllAccounts(){
        List<AccountResponse> accounts = authService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/get-email/{id}")
    public ResponseEntity<?> getEmail(@PathVariable Integer id){
        String email = authService.getEmail(id);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
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
    public ResponseEntity<?> signUp(@RequestBody AuthRequest request) {
        Integer accountId = authService.signUp(request);
        return ResponseEntity.ok(accountId);
    }

    @PutMapping("/admin/lock/{id}")
    public ResponseEntity<?> lockAccount(@PathVariable Integer id){
        Integer accountId = authService.changeStatus(id, false);
        return ResponseEntity.ok(accountId);
    }

    @PutMapping("/admin/unlock/{id}")
    public ResponseEntity<?> unlockAccount(@PathVariable Integer id){
        Integer accountId = authService.changeStatus(id, true);
        return ResponseEntity.ok(accountId);
    }

    @PutMapping("/admin/to-admin/{id}")
    public ResponseEntity<?> toAdmin(@PathVariable Integer id){
        Integer accountId = authService.changeRole(id, "ADMIN");
        return ResponseEntity.ok(accountId);
    }

    @PutMapping("/admin/to-user/{id}")
    public ResponseEntity<?> toUser(@PathVariable Integer id){
        Integer accountId = authService.changeRole(id, "USER");
        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        emailVerificationService.verifyAccount(token);
        return ResponseEntity.ok(Map.of("message", "Xác minh tài khoản thành công"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendResetPasswordEmail(request);
        return ResponseEntity.ok(Map.of("message", "Nếu email tồn tại, vui lòng kiểm tra hộp thư"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công"));
    }

}
