package com.tqk.stationeryecommercebackend.dto.order.responses;

import java.time.LocalDateTime;

public class PaymentResponse {
    private Integer id;
    private double amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime paidAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
