package com.tqk.apigateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CategorySlugFilter implements GlobalFilter {

    private final WebClient webClient;

    public CategorySlugFilter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (!path.startsWith("/products")) {
            return chain.filter(exchange);
        }
        // Lấy param category=<slug> từ request
        String categorySlug = exchange.getRequest().getQueryParams().getFirst("category");
        if (categorySlug == null || categorySlug.isEmpty()) {
            return chain.filter(exchange); // bỏ qua filter
        }
        if ("all".equalsIgnoreCase(categorySlug)) {
            // forward đến endpoint lấy tất cả sản phẩm
            URI newUri = UriComponentsBuilder
                    .fromUri(exchange.getRequest().getURI())
                    .replacePath("/products")
                    .replaceQueryParam("category")
                    .build()
                    .toUri();

            ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
            return chain.filter(exchange.mutate().request(newRequest).build());

        } else {
            // gọi category-service để lấy id
            return webClient.get()
                    .uri("http://category-service:8080/categories/by-slug/" + categorySlug)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404, // nếu 404 Not Found
                            response -> Mono.empty() // trả về empty Mono để tiếp tục
                    )
                    .bodyToMono(JsonNode.class)
                    .onErrorResume(e -> Mono.empty()) // bắt tất cả lỗi khác
                    .flatMap(json -> {
                        URI newUri;
                        if (json == null || json.get("id") == null) {
                            // slug không tồn tại, bỏ categoryId
                            newUri = UriComponentsBuilder
                                    .fromUri(exchange.getRequest().getURI())
                                    .replaceQueryParam("categoryId")
                                    .replaceQueryParam("category")
                                    .build()
                                    .toUri();
                        } else {
                            // slug hợp lệ, thêm categoryId
                            String categoryId = json.get("id").asText();
                            newUri = UriComponentsBuilder
                                    .fromUri(exchange.getRequest().getURI())
                                    .replaceQueryParam("categoryId", categoryId)
                                    .replaceQueryParam("category")
                                    .build()
                                    .toUri();
                        }
                        ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
                        return chain.filter(exchange.mutate().request(newRequest).build());
                    });
        }
    }
}

