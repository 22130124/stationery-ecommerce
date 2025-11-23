package com.tqk.cartservice.dto.request;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private Integer productId;
    private Integer variantId;
    private Integer quantity;
}