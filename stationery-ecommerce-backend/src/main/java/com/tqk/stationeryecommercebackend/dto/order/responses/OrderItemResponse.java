package com.tqk.stationeryecommercebackend.dto.order.responses;

import com.tqk.stationeryecommercebackend.dto.product.responses.ProductVariantResponse;
import com.tqk.stationeryecommercebackend.dto.product.responses.ProductResponse;

public class OrderItemResponse {
    private Integer id;
    private ProductResponse product;
    private ProductVariantResponse variant;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public ProductVariantResponse getVariant() {
        return variant;
    }

    public void setVariant(ProductVariantResponse variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

