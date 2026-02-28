package com.tqk.cartservice.dto.response.cart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponse {
    private Integer id;
    private Integer accountId;
    private List<CartItemResponse> items = new ArrayList<>();
    private int totalItems;
}
