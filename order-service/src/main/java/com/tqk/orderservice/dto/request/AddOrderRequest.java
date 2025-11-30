package com.tqk.orderservice.dto.request;

import com.tqk.orderservice.dto.response.OrderItemResponse;
import lombok.Data;

import java.util.List;

@Data
public class AddOrderRequest {
    private List<OrderItemResponse> orderItems;
}
