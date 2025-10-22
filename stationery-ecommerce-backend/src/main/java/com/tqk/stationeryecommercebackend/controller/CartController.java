package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.cart.CartItemResponse;
import com.tqk.stationeryecommercebackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/get-cart-items")
    public ResponseEntity<?> getCartItems(@RequestParam List<Integer> variantIds) {
        List<CartItemResponse> cartItems = cartService.getCartItems(variantIds);
        Map<String, Object> response = Map.of("cartItems", cartItems);
        return ResponseEntity.ok(response);
    }
}
