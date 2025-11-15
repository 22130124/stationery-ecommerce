package com.tqk.brandservice.service.client;

import com.tqk.brandservice.dto.response.SupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SupplierClient {
    private final WebClient webClient;

    @Autowired
    public SupplierClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://supplier-service:8082/api/suppliers").build();
    }

    public SupplierResponse getById(Integer id) {
        return webClient.get().uri("/{id}", id).retrieve().bodyToMono(SupplierResponse.class).block();
    }
}
