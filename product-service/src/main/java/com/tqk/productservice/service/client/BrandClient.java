package com.tqk.productservice.service.client;

import com.tqk.productservice.dto.response.BrandResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BrandClient {
    private final WebClient webClient;

    public BrandClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://brand-service:8083/api/brands").build();
    }

    public BrandResponse getById(Integer id) {
        return webClient
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(BrandResponse.class)
                .block();
    }
}
