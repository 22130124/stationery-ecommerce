package com.tqk.productservice.service.client;

import com.tqk.productservice.dto.response.CategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CategoryClient {
    private final WebClient webClient;

    public CategoryClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://category-service:8081/api/categories").build();
    }

    public CategoryResponse getById(Integer id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(CategoryResponse.class)
                .block();
    }

    public CategoryResponse getBySlug(String slug) {
        return webClient.get()
                .uri("/slug/{slug}", slug)
                .retrieve()
                .bodyToMono(CategoryResponse.class)
                .block();
    }
}
