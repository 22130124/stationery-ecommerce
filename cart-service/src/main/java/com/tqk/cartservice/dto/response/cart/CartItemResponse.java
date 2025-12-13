package com.tqk.cartservice.dto.response.cart;

import com.tqk.cartservice.dto.response.product.ProductResponse;
import lombok.Data;

@Data
public class CartItemResponse {
    private Integer id;
    private Integer productId;
    private Integer variantId;
    private ProductResponse product;
    private Double finalPrice;
    private int quantity;
}
