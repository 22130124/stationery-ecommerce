package com.tqk.profileservice.service;

import com.tqk.profileservice.dto.request.AvatarUpdateRequest;
import com.tqk.profileservice.dto.request.ProfileUpdateRequest;
import com.tqk.profileservice.dto.response.ProfileResponse;
import com.tqk.profileservice.exception.ExceptionCode;
import com.tqk.profileservice.model.Profile;
import com.tqk.profileservice.repository.ProfileRepository;
import com.tqk.profileservice.repository.client.AccountClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.tqk.profileservice.model.Profile.ProfileStatus.COMPLETED;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final AccountClient accountClient;

    public Profile createProfile(Integer accountId) {
        Profile profile = new Profile();
        profile.setAccountId(accountId);
        profileRepository.save(profile);
        return profile;
    }

    /**
     * Tìm profile của accountId tương ứng
     * Nếu trường hợp profile chưa tồn tại thì tạo profile mới (emtpy) cho accountId này
     */
    public ProfileResponse getProfileResponseByAccountId(Integer accountId) {
        Profile profile = profileRepository.findByAccountId(accountId).orElseGet(() -> createProfile(accountId));
        return convertToDto(profile);
    }

    public Profile getProfileByAccountId(Integer accountId) {
        return profileRepository.findByAccountId(accountId).orElseGet(() -> createProfile(accountId));
    }

    public ProfileResponse updateProfle(Integer accountId, ProfileUpdateRequest request) {
        Profile profile = getProfileByAccountId(accountId);

        // Kiểm tra trùng lặp số điện thoại
        if (profileRepository.existsByPhone(request.getPhone()) && !profile.getPhone().equalsIgnoreCase(request.getPhone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.PHONE_ALREADY_EXISTS.name());
        }

        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setFullName(request.getFullName());

        // Nếu profile đã đầy đủ thông tin thì đánh dấu là đã hoàn thiện profile
        if (profile.getFullName() != null && profile.getAddress() != null && profile.getPhone() != null ) {
            profile.setStatus(COMPLETED);
        }

        profileRepository.save(profile);
        return convertToDto(profile);
    }

    public ProfileResponse updateAvatar(Integer accountId, AvatarUpdateRequest request) {
        Profile profile = getProfileByAccountId(accountId);
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
        dto.setStatus(profile.getStatus());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}
