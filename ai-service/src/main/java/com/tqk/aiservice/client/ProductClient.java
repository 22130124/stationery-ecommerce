package com.tqk.aiservice.client;

import com.tqk.aiservice.dto.response.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", url = "http://product-service:8080")
public interface ProductClient {
    @GetMapping("/products/search-by-ai")
    List<ProductResponse> searchProducts(
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("keyword") String keyword,
            @RequestParam("color") String color,
            @RequestParam("minPrice") Integer minPrice,
            @RequestParam("maxPrice") Integer maxPrice,
            @RequestParam("extra") String extra
    );
}
