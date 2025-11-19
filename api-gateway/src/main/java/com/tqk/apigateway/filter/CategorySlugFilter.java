package com.tqk.apigateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CategorySlugFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    public CategorySlugFilter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Chỉ xử lý path /products/by-category
        if (!request.getURI().getPath().contains("/products/by-category")) {
            return chain.filter(exchange);
        }

        // Lấy query param categorySlug
        String categorySlug = request.getQueryParams().getFirst("categorySlug");

        // Nếu không có slug thì đi tiếp như bình thường
        if (categorySlug == null || categorySlug.isEmpty()) {
            return chain.filter(exchange);
        }

        if (categorySlug.equalsIgnoreCase("all")) {
            // Build request mới trỏ tới /products (getAllForUser)
            URI newUri = UriComponentsBuilder
                    .fromUri(request.getURI())
                    .replacePath("/products")
                    .replaceQueryParam("categorySlug")
                    .build(true)
                    .toUri();

            ServerHttpRequest newRequest = request.mutate().uri(newUri).build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        }

        // Gọi category-service để lấy categoryId
        return webClient.get()
                .uri("http://category-service:8080/categories/by-slug/" + categorySlug)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(categoryData -> {

                    Integer categoryId = categoryData.get("category").get("id").asInt();

                    // Build lại request URL, thay categorySlug bằng categoryId
                    URI newUri = UriComponentsBuilder
                            .fromUri(request.getURI())
                            .replaceQueryParam("categorySlug")
                            .replaceQueryParam("categoryId", categoryId)
                            .build(true)
                            .toUri();

                    ServerHttpRequest newRequest = request.mutate().uri(newUri).build();

                    return chain.filter(exchange.mutate().request(newRequest).build());
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
