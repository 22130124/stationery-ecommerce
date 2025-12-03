package com.tqk.aiservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service", url = "http://product-service:8080")
public interface ProductClient {
}
