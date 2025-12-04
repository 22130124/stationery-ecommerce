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
            @RequestParam("brandId") Integer brandId,
            @RequestParam("colors") List<String> colors,
            @RequestParam("minPrice") Integer minPrice,
            @RequestParam("maxPrice") Integer maxPrice,
            @RequestParam("extras") List<String> extras
    );
}
