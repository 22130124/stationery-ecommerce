package com.tqk.orderservice.dto.response.order;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Integer id;
    private Integer accountId;
    private int totalAmount;
    private Integer shippingStatus;
    private Integer paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> orderItems;
}
