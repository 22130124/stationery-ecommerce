package com.tqk.authservice.repository.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "http://profile-service:8080")
public interface ProfileClient {
    @PostMapping("/profiles")
    ResponseEntity<Void> createProfile(@RequestBody Integer accountId);
}
