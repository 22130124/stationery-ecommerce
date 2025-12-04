package com.tqk.aiservice.dto.response.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVariantResponse {
    private Integer id;
    private String name;
    private Double basePrice;
    private Double discountPrice;
    private boolean activeStatus;
    private boolean defaultStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductImageResponse> images;
}
