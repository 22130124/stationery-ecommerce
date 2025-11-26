package com.tqk.categoryservice.dto.request;

import com.tqk.categoryservice.dto.response.OrderItemResponse;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemResponse> orderItems;
}
