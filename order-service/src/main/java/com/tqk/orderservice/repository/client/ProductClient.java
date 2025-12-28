package com.tqk.orderservice.repository.client;

import com.tqk.orderservice.dto.response.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", contextId = "productClient", url = "http://product-service:8080")
public interface ProductClient {
    @PostMapping("/products/by-variant-ids")
    List<ProductResponse> getProductsByIds(@RequestBody List<Integer> ids);
}
