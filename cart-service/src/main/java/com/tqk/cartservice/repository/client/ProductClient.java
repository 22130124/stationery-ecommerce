package com.tqk.cartservice.repository.client;

import com.tqk.cartservice.dto.response.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "http://product-service:8080")
public interface ProductClient {
    @PostMapping("/products/internal/by-variant-ids")
    List<ProductResponse> getProductsByIds(@RequestBody List<Integer> ids);
}
