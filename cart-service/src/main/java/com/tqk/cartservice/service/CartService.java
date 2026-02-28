package com.tqk.cartservice.service;

import com.tqk.cartservice.dto.request.AddCartItemRequest;
import com.tqk.cartservice.dto.response.cart.CartItemResponse;
import com.tqk.cartservice.dto.response.cart.CartResponse;
import com.tqk.cartservice.dto.response.product.ProductResponse;
import com.tqk.cartservice.exception.ExceptionCode;
import com.tqk.cartservice.model.Cart;
import com.tqk.cartservice.model.CartItem;
import com.tqk.cartservice.repository.CartItemRepository;
import com.tqk.cartservice.repository.CartRepository;
import com.tqk.cartservice.repository.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    @Transactional
    public CartResponse getCartByAccountId(Integer accountId) {
        // Lấy ra giỏ hàng, nếu giỏ hàng người dùng chưa có thì tạo mới
        Cart cart = cartRepository.findByAccountId(accountId).orElseGet(() -> createEmptyCart(accountId));

        // Lấy ra danh sách items trong giỏ hàng
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        CartResponse cartResponse = cart.convertToDto();

        // Nếu có items thì gọi product client để lấy các thông tin của sản phẩm và biến thể
        if (!cartItems.isEmpty()) {
            // Chuẩn bị danh sách các biến thể để gọi api
            List<Integer> variantIds = new ArrayList<>();
            for (CartItem item : cartItems) {
                variantIds.add(item.getVariantId());
            }

            // Tiến hành gọi api để lấy dữ liệu sản phẩm
            List<ProductResponse> productResponseList = productClient.getProductsByIds(variantIds);

            for (int i = 0; i < productResponseList.size(); i++) {
                CartItemResponse cartItemResponse = cartResponse.getItems().get(i);
                ProductResponse productResponse = productResponseList.get(i);
                cartItemResponse.setProduct(productResponse);
                if (productResponse.getDefaultVariant().getDiscountPrice() != null) {
                    cartItemResponse.setFinalPrice(productResponse.getDefaultVariant().getDiscountPrice());
                } else {
                    cartItemResponse.setFinalPrice(productResponse.getDefaultVariant().getBasePrice());
                }
            }
        }

        cartResponse.setTotalItems(cartItems.size());

        return cartResponse;
    }

    @Transactional
    protected Cart createEmptyCart(Integer accountId) {
        Cart cart = new Cart();
        cart.setAccountId(accountId);
        return cartRepository.save(cart);
    }

    @Transactional
    public CartResponse addCartItem(Integer accountId, AddCartItemRequest request) {

        // 1. Lấy cart của user
        Cart cart = cartRepository.findByAccountId(accountId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setAccountId(accountId);
                    return cartRepository.save(newCart);
                });

        // 2. Kiểm tra item đã tồn tại trong giỏ chưa
        CartItem existingItem = cartItemRepository.findByCartIdAndVariantId(cart.getId(), request.getVariantId())
                .orElse(null);

        if (existingItem != null) {
            // Nếu đã tồn tại item này thì cập nhật cộng dồn số lượng
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            // Thêm mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(request.getProductId());
            newItem.setVariantId(request.getVariantId());
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }

        return getCartByAccountId(accountId);
    }

    @Transactional
    public CartResponse removeCartItem(Integer accountId, Integer variantId) {
        // Lấy cart của user
        Cart cart = cartRepository.findByAccountId(accountId).orElseGet(() -> createEmptyCart(accountId));

        // Tiến hành xóa item
        cartItemRepository.deleteByCartAndVariantId(cart, variantId);

        // Cập nhật lại cache
        cart.getItems().removeIf(item -> item.getVariantId().equals(variantId));

        return getCartByAccountId(accountId);
    }

    @Transactional
    public CartResponse updateCartItem(Integer accountId, Integer variantId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        // Lấy giỏ hàng
        Cart cart = cartRepository.findByAccountId(accountId).orElseGet(() -> createEmptyCart(accountId));

        // Tìm item trong giỏ hàng
        CartItem cartItem = cartItemRepository.findByCartIdAndVariantId(cart.getId(), variantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.ITEM_NOT_FOUND.name()));

        // Cập nhật số lượng
        cartItem.setQuantity(newQuantity);

        return getCartByAccountId(accountId);
    }

    @Transactional
    public void resetCart(Integer accountId, List<Integer> variantIds) {
        // 1. Lấy cart của user
        Cart cart = cartRepository.findByAccountId(accountId).orElseGet(() -> createEmptyCart(accountId));

        // 2. Xóa các item trong danh sách được chỉ định
        cartItemRepository.deleteByCartAndVariantIdIn(cart, variantIds);
    }

    private CartResponse convertCartToDto(Cart cart, List<CartItemResponse> cartItemResponseList) {
        CartResponse dto = new CartResponse();
        dto.setId(cart.getId());
        dto.setAccountId(cart.getAccountId());
        dto.setItems(cartItemResponseList);
        return dto;
    }
}
