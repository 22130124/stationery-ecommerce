package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.cart.CartItemResponse;
import com.tqk.stationeryecommercebackend.model.ProductVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private ProductService productService;

    @Autowired
    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public List<CartItemResponse> getCartItems(List<Integer> variantIds) {
        List<ProductVariant> variants = productService.getProductVariantsByIds(variantIds);
        List<CartItemResponse> cartItems = new ArrayList<>();
        for(ProductVariant variant : variants) {
            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setProductId(variant.getProduct().getId());
            cartItemResponse.setProductName(variant.getProduct().getName());
            cartItemResponse.setBrandName(variant.getProduct().getBrand().getName());
            cartItemResponse.setVariant(variant.convertToDto());
            cartItems.add(cartItemResponse);
        }
        return cartItems;
    }
}
