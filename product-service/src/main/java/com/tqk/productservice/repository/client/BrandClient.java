package com.tqk.productservice.repository.client;

import com.tqk.productservice.dto.response.brand.BrandResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "brand-service", url = "http://brand-service:8080")
public interface BrandClient {
    @GetMapping("/brands/{brandId}")
    BrandResponse getBrandById(@PathVariable Integer brandId);
}
