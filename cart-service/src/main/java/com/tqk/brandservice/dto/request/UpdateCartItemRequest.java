package com.tqk.brandservice.dto.request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Long productId;
    private Long variantId;
    private Integer quantity;
}
