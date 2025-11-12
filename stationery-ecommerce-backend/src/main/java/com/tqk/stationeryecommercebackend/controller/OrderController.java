package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.order.requests.OrderRequest;
import com.tqk.stationeryecommercebackend.dto.order.responses.OrderResponse;
import com.tqk.stationeryecommercebackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Tạo đơn hàng mới
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            OrderResponse order = orderService.createOrder(orderRequest);
            Map<String, Object> response = Map.of(
                    "order", order,
                    "message", "Order created successfully"
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = Map.of("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
