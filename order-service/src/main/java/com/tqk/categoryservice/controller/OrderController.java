package com.tqk.categoryservice.controller;

import com.tqk.categoryservice.dto.request.OrderRequest;
import com.tqk.categoryservice.dto.response.OrderResponse;
import com.tqk.categoryservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestHeader("X-Account-Id") Integer accountId) {
        List<OrderResponse> orders = orderService.getOrders(accountId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestHeader("X-Account-Id") Integer accountId, @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(accountId, orderRequest);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(
            @RequestHeader("X-Account-Id") Integer accountId,
            @PathVariable Integer orderId,
            @RequestBody OrderRequest request) {

        OrderResponse order = orderService.updateOrder(accountId, orderId, request);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(
            @RequestHeader("X-Account-Id") Integer accountId,
            @PathVariable Integer orderId) {

        orderService.cancelOrder(accountId, orderId);
        return ResponseEntity.ok("Hủy đơn hàng thành công");
    }
}
