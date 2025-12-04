package com.tqk.aiservice.client;

import com.tqk.aiservice.dto.response.brand.BrandResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "brand-service", url = "http://brand-service:8080")
public interface BrandClient {
    @GetMapping("/brands")
    List<BrandResponse> getActiveBrands();
}
