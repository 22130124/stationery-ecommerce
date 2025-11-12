package com.tqk.stationeryecommercebackend.dto.order.requests;

public class OrderItemRequest {
    private Integer productId;
    private Integer variantId;
    private int quantity;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getVariantId() { return variantId; }
    public void setVariantId(Integer variantId) { this.variantId = variantId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
