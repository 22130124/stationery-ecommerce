package com.tqk.orderservice.repository.client;

import com.tqk.orderservice.dto.response.profile.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "http://profile-service:8080")
public interface ProfileClient {
    @GetMapping("/profiles/by-account/{accountId}")
    ProfileResponse getProfileByAccount(@PathVariable Integer accountId);
}

