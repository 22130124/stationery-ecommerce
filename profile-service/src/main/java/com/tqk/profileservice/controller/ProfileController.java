package com.tqk.profileservice.controller;

import com.tqk.profileservice.dto.request.AvatarUpdateRequest;
import com.tqk.profileservice.dto.request.ProfileUpdateRequest;
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

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestHeader("X-Account-Id") Integer accountId, @RequestBody ProfileUpdateRequest request){
        ProfileResponse profile = profileService.updateProfle(accountId, request);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@RequestHeader("X-Account-Id") Integer accountId, @RequestBody AvatarUpdateRequest request) {
        ProfileResponse profile = profileService.updateAvatar(accountId, request);
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody Integer accountId) {
        profileService.createProfile(accountId);
        return ResponseEntity.ok().build();
    }
}
