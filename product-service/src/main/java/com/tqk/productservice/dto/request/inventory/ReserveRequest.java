package com.tqk.productservice.dto.request.inventory;

import lombok.Data;

import java.util.Map;

@Data
public class ReserveRequest {
    private String orderCode;
    private Map<Integer, Integer> items; // Map<variantId, quantity>
}
