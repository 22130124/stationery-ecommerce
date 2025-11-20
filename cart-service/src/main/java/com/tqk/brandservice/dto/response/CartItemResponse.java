package com.tqk.brandservice.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private Integer productId;
    private Integer variantId;
    private int quantity;
}
