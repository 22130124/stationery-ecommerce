package com.tqk.orderservice.repository.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service", url = "http://cart-service:8080")
public interface CartClient {

    @DeleteMapping("/cart/reset-cart")
    void resetCart(@RequestHeader("X-Account-Id") Integer accountId);
}

