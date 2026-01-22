package com.tqk.orderservice.controller;

import com.tqk.orderservice.dto.request.order.AddOrderRequest;
import com.tqk.orderservice.dto.request.order.UpdateOrderRequest;
import com.tqk.orderservice.dto.response.order.OrderDetailResponse;
import com.tqk.orderservice.dto.response.order.OrderResponse;
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
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrders(null));
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestHeader("X-Account-Id") Integer accountId) {
        List<OrderDetailResponse> orders = orderService.getOrders(accountId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<?> getOrderDetail(@PathVariable String orderCode) {
        OrderDetailResponse detail = orderService.getOrderDetail(orderCode);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestHeader("X-Account-Id") Integer accountId, @RequestBody AddOrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(accountId, orderRequest);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/admin/{orderCode}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderCode, @RequestBody UpdateOrderRequest request) {

        OrderResponse order = orderService.updateShippingStatus(orderCode, request);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/cancel/{orderCode}")
    public ResponseEntity<?> cancelOrder(
            @RequestHeader("X-Account-Id") Integer accountId,
            @PathVariable String orderCode) {

        OrderResponse order = orderService.cancelOrder(accountId, orderCode);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/set-expired/{orderCode}")
    public ResponseEntity<?> setExpired(@PathVariable String orderCode) {
        orderService.setExpired(orderCode);
        return ResponseEntity.ok().build();
    }
}