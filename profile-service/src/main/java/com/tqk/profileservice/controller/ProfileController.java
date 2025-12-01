package com.tqk.profileservice.controller;

import com.tqk.profileservice.dto.response.ProfileResponse;
import com.tqk.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrenProfile(@RequestHeader("X-Account-Id") Integer accountId) {
        ProfileResponse profile = profileService.getProfileByAccountId(accountId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<?> getProfileByAccountId(@PathVariable Integer accountId) {
        ProfileResponse profile = profileService.getProfileByAccountId(accountId);
        return ResponseEntity.ok(profile);
    }
}
