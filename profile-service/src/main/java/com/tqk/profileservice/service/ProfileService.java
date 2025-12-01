package com.tqk.profileservice.service;

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

    public ProfileResponse getProfileByAccountId(Integer accountId) {
        Profile profile = profileRepository.findByAccountId(accountId).orElseThrow(() -> new ProfileNotFoundException("Không tìm thấy hồ sơ người dùng có account id là " + accountId));
        String email = accountClient.getEmail(profile.getAccountId());
        return profile.convertToDto(email);
    }
}
