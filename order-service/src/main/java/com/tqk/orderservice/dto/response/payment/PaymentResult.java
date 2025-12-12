package com.tqk.orderservice.dto.response.payment;

import lombok.Data;

@Data
public class PaymentResult {
    private boolean success;
    private String message;

    public PaymentResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
