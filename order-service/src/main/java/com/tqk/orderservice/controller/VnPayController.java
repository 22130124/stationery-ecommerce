package com.tqk.orderservice.controller;

import com.tqk.orderservice.service.OrderService;
import com.tqk.orderservice.service.VnPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VnPayController {
    private final OrderService orderService;
    private final VnPayService vnPayService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam Integer orderId) {
        String paymentUrl = orderService.createPaymentUrl(orderId);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/return")
    public ResponseEntity<?> handleVnPayReturn(@RequestParam Map<String, String> params) {
        try {
            String result = orderService.processVnPayReturn(params);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xử lý thanh toán: " + e.getMessage());
        }
    }
}
