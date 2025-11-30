package com.tqk.orderservice.controller;

import com.tqk.orderservice.dto.request.AddOrderRequest;
import com.tqk.orderservice.dto.request.UpdateOrderRequest;
import com.tqk.orderservice.dto.response.OrderResponse;
import com.tqk.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestHeader("X-Account-Id") Integer accountId) {
        List<OrderResponse> orders = orderService.getOrders(accountId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestHeader("X-Account-Id") Integer accountId, @RequestBody AddOrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(accountId, orderRequest);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateOrderStatus(
            @RequestHeader("X-Account-Id") Integer accountId,
            @PathVariable Integer id,
            @RequestBody UpdateOrderRequest request) {

        OrderResponse order = orderService.updateOrderStatus(accountId, id, request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> cancelOrder(
            @RequestHeader("X-Account-Id") Integer accountId,
            @PathVariable Integer id) {

        OrderResponse order = orderService.cancelOrder(accountId, id);
        return ResponseEntity.ok(order);
    }
}
