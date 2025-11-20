package com.tqk.brandservice.service;

import com.tqk.brandservice.dto.response.CartResponse;
import com.tqk.brandservice.model.Cart;
import com.tqk.brandservice.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartResponse getCartByAccountId(Integer accountId) {
        Cart cart = cartRepository.findByAccountId(accountId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setAccountId(accountId);
                    return cartRepository.save(newCart);
                });

        return cart.convertToDto();
    }
}
