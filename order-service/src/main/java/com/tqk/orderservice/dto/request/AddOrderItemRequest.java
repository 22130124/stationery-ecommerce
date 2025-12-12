package com.tqk.orderservice.dto.request;

import lombok.Data;

@Data
public class AddOrderItemRequest {
    private Integer productId;
    private Integer variantId;
    private int price;
    private Integer quantity;
}
