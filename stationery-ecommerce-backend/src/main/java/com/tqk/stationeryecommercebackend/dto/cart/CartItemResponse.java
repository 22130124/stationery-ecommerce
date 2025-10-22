package com.tqk.stationeryecommercebackend.dto.cart;

import com.tqk.stationeryecommercebackend.dto.product.ProductVariantResponse;

public class CartItemResponse {
    private Integer productId;
    private String productName;
    private String brandName;
    private ProductVariantResponse variant;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public ProductVariantResponse getVariant() {
        return variant;
    }

    public void setVariant(ProductVariantResponse variant) {
        this.variant = variant;
    }
}
