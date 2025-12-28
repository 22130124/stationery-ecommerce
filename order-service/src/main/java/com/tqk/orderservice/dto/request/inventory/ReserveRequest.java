package com.tqk.orderservice.dto.request.inventory;

import lombok.Data;

import java.util.Map;

@Data
public class ReserveRequest {
    private Integer orderId;
    private Map<Integer, Integer> items; // Map<variantId, quantity>
}
