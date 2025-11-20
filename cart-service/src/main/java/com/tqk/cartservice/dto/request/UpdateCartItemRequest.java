package com.tqk.cartservice.dto.request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Long productId;
    private Long variantId;
    private Integer quantity;
}
