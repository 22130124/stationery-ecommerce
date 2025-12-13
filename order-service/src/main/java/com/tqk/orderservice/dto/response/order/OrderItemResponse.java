package com.tqk.orderservice.dto.response.order;

import com.tqk.orderservice.dto.response.product.ProductResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemResponse {
    private Integer id;
    private Integer orderId;
    private ProductResponse product;
    private int price;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
