package com.tqk.brandservice.controller;

import com.tqk.brandservice.dto.response.CartResponse;
import com.tqk.brandservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    // Lấy giỏ hàng hiện tại
    @GetMapping
    public ResponseEntity<?> getCart(
            @RequestHeader("X-Account-Id") Integer accountId) {

        CartResponse cart = cartService.getCartByAccountId(accountId);
        Map<String, Object> response = Map.of("cart", cart);
        return ResponseEntity.ok(response);
    }
}
