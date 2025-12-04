package com.tqk.aiservice.client;

import com.tqk.aiservice.dto.response.category.CategoryResponse;
import com.tqk.aiservice.dto.response.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "category-service", url = "http://category-service:8080")
public interface CategoryClient {
    @GetMapping("/categories")
    List<CategoryResponse> getActiveCategories();
}
