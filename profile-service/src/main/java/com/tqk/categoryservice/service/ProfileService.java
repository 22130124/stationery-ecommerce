package com.tqk.categoryservice.service;

import com.tqk.categoryservice.dto.response.ProfileResponse;
import com.tqk.categoryservice.exception.ProfileNotFoundException;
import com.tqk.categoryservice.model.Profile;
import com.tqk.categoryservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileResponse getProfileByAccountId(Integer accountId) {
        Profile profile = profileRepository.findByAccountId(accountId).orElseThrow(() -> new ProfileNotFoundException("Không tìm thấy hồ sơ người dùng có account id là " + accountId));
        return profile.convertToDto();
    }
}
