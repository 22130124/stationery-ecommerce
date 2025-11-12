package com.tqk.stationeryecommercebackend.dto.order.requests;

import java.util.List;

public class OrderRequest {
    private Integer customerId;
    private String shippingAddress;
    private List<OrderItemRequest> items;

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
