package com.tqk.cartservice.controller;

import com.tqk.cartservice.dto.request.AddCartItemRequest;
import com.tqk.cartservice.dto.response.cart.CartResponse;
import com.tqk.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping
    public ResponseEntity<?> addOrUpdateCartItem(
            @RequestHeader("X-Account-Id") Integer accountId,
            @RequestBody AddCartItemRequest request) {
        CartResponse cart = cartService.addCartItem(accountId, request);
        return ResponseEntity.ok(Map.of("cart", cart));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateCartItem(
            @RequestHeader("X-Account-Id") Integer accountId,
            @PathVariable Integer itemId,
            @RequestBody Map<String, Integer> body) {

        Integer newQuantity = body.get("quantity");
        if (newQuantity == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không có giá trị nào để cập nhật giỏ hàng"));
        }

        CartResponse cart = cartService.updateCartItem(accountId, itemId, newQuantity);
        return ResponseEntity.ok(Map.of("cart", cart));
    }

    // Xóa một item trong giỏ hàng
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeCartItem(
            @RequestHeader("X-Account-Id") Integer accountId, @PathVariable Integer itemId) {

        CartResponse cart = cartService.removeCartItem(accountId, itemId);
        Map<String, Object> response = Map.of("cart", cart);
        return ResponseEntity.ok(response);
    }

    // Xóa toàn bộ items trong giỏ hàng
    @DeleteMapping("/reset-cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetCart(@RequestHeader("X-Account-Id") Integer accountId, @RequestBody List<Integer> itemIds) {
        cartService.resetCart(accountId, itemIds);
    }
}
