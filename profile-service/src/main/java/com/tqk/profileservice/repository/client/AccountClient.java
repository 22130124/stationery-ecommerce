package com.tqk.profileservice.repository.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://auth-service:8080")
public interface AccountClient {
    @GetMapping("/auth/get-email/{id}")
    String getEmail(@PathVariable Integer id);
}
