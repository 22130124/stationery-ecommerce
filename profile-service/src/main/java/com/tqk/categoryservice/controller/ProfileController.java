package com.tqk.categoryservice.controller;

import com.tqk.categoryservice.dto.response.ProfileResponse;
import com.tqk.categoryservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/by-account")
    public ResponseEntity<?> getProfileByAccountId(@RequestHeader("X-Account-Id") Integer accountId) {
        ProfileResponse profile = profileService.getProfileByAccountId(accountId);
        Map<String, Object> response = Map.of("profile", profile);
        return ResponseEntity.ok(response);
    }
}
