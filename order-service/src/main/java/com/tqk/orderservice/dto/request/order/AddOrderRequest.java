package com.tqk.orderservice.dto.request.order;

import lombok.Data;

import java.util.List;

@Data
public class AddOrderRequest {
    private List<AddOrderItemRequest> orderItems;
}
