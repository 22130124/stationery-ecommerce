package com.tqk.cartservice.dto.response.cart;

import com.tqk.cartservice.dto.response.product.ProductImageResponse;
import lombok.Data;

@Data
public class CartItemResponse {
    private Integer id;
    private Integer productId;
    private Integer variantId;
    private String productName;
    private String variantName;
    private Double price;
    private Double basePrice;
    private Double discountPrice;
    private ProductImageResponse defaultImage;
    private int quantity;
}
