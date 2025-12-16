package com.tqk.profileservice.service;

import com.tqk.profileservice.dto.request.AvatarUpdateRequest;
import com.tqk.profileservice.dto.request.ProfileUpdateRequest;
import com.tqk.profileservice.dto.response.ProfileResponse;
import com.tqk.profileservice.exception.ProfileNotFoundException;
import com.tqk.profileservice.model.Profile;
import com.tqk.profileservice.repository.ProfileRepository;
import com.tqk.profileservice.repository.client.AccountClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final AccountClient accountClient;

    public void createProfile(Integer accountId) {
        Profile profile = new Profile();
        profile.setAccountId(accountId);
        profileRepository.save(profile);
    }

    public ProfileResponse getProfileByAccountId(Integer accountId) {
        Profile profile = profileRepository.findByAccountId(accountId).orElseThrow(() -> new ProfileNotFoundException("Không tìm thấy hồ sơ người dùng có account id là " + accountId));
        return convertToDto(profile);
    }

    public ProfileResponse updateProfle(Integer accountId, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findByAccountId(accountId).orElseThrow(() -> new ProfileNotFoundException("Không tìm thấy hồ sơ người dùng có account id là " + accountId));
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setFullName(request.getFullName());

        // Nếu profile đã đầy đủ thông tin thì đánh dấu là đã hoàn thiện profile
        if (profile.getFullName() != null && profile.getAddress() != null && profile.getPhone() != null ) {
            profile.setCompletedStatus(true);
        }

        profileRepository.save(profile);
        return convertToDto(profile);
    }

    public ProfileResponse updateAvatar(Integer accountId, AvatarUpdateRequest request) {
        Profile profile = profileRepository.findByAccountId(accountId).orElseThrow(() -> new ProfileNotFoundException("Không tìm thấy hồ sơ người dùng có account id là " + accountId));
        profile.setAvatarUrl(request.getAvatarUrl());
        profile.setAvatarPublicId(request.getAvatarPublicId());
        profileRepository.save(profile);
        return convertToDto(profile);
    }

    private ProfileResponse convertToDto(Profile profile) {
        String email = accountClient.getEmail(profile.getAccountId());
        ProfileResponse dto = new ProfileResponse();
        dto.setId(profile.getId());
        dto.setAccountId(profile.getAccountId());
        dto.setFullName(profile.getFullName());
        dto.setEmail(email);
        dto.setPhone(profile.getPhone());
        dto.setAddress(profile.getAddress());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setAvatarPublicId(profile.getAvatarPublicId());
        dto.setCompletedStatus(profile.isCompletedStatus());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}
