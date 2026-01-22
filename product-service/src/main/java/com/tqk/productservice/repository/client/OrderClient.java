package com.tqk.productservice.repository.client;

import com.tqk.productservice.dto.response.brand.BrandResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "order-service", url = "http://order-service:8080")
public interface OrderClient {
    @PutMapping("/orders/set-expired/{orderCode}")
    BrandResponse setExpired(@PathVariable String orderCode);
}
