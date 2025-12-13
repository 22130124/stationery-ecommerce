package com.tqk.orderservice.dto.response.product;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductImageResponse {
    private Integer id;
    private String url;
    private boolean defaultStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
