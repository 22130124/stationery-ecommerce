package com.tqk.orderservice.dto.response.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemResponse {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer variantId;
    private int price;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
