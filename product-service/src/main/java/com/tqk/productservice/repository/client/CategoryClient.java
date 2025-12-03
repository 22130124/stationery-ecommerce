package com.tqk.productservice.repository.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service", url = "http://category-service:8080")
public interface CategoryClient {
    @GetMapping("/categories/get-id-by-slug/{slug}")
    Integer getCategoryIdBySlug(@PathVariable String slug);
}
