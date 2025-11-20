package com.tqk.brandservice.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private Integer id;
    private Integer accountId;
    private List<CartItemResponse> items;
}
