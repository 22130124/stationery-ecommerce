package com.tqk.cartservice.service;

import com.tqk.cartservice.dto.request.AddCartItemRequest;
import com.tqk.cartservice.dto.response.cart.CartItemResponse;
import com.tqk.cartservice.dto.response.cart.CartResponse;
import com.tqk.cartservice.dto.response.product.ProductImageResponse;
import com.tqk.cartservice.dto.response.product.ProductResponse;
import com.tqk.cartservice.dto.response.product.ProductVariantResponse;
import com.tqk.cartservice.exception.CartNotFoundException;
import com.tqk.cartservice.exception.CartItemNotFoundException;
import com.tqk.cartservice.model.Cart;
import com.tqk.cartservice.model.CartItem;
import com.tqk.cartservice.repository.CartItemRepository;
import com.tqk.cartservice.repository.CartRepository;
import com.tqk.cartservice.repository.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    public CartResponse getCartByAccountId(Integer accountId) {
        // Lấy ra giỏ hàng, nếu giỏ hàng người dùng chưa có thì tạo mới
        Cart cart = cartRepository.findByAccountId(accountId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setAccountId(accountId);
                    return cartRepository.save(newCart);
                });

        // Lấy ra danh sách items trong giỏ hàng
        List<CartItem> cartItems = cart.getItems();

        // Nếu có items thì gọi product client để lấy các thông tin của sản phẩm và biến thể
        List<CartItemResponse> cartItemResponseList = null;
        if (cartItems != null) {
            // Chuẩn bị danh sách các biến thể để gọi api
            List<Integer> variantIds = new ArrayList<>();
            for (CartItem item : cartItems) {
                variantIds.add(item.getVariantId());
            }

            // Tiến hành gọi api để lấy dữ liệu sản phẩm
            List<ProductResponse> productResponseList = productClient.getProductsByIds(variantIds);

            cartItemResponseList = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                for (ProductResponse productResponse : productResponseList) {
                    if (Objects.equals(cartItem.getProductId(), productResponse.getId())
                            && Objects.equals(cartItem.getVariantId(), productResponse.getVariants().getFirst().getId())) {
                        cartItemResponseList.add(convertCartItemToDto(cartItem, productResponse));
                        break;
                    }
                }
            }
        }

        return cart.convertToDto(cartItemResponseList);
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
    public CartResponse removeCartItem(Integer accountId, Integer itemId) {
        // 1. Lấy cart của user
        Cart cart = cartRepository.findByAccountId(accountId)
                .orElseThrow(() -> new CartNotFoundException("Không tìm thấy giỏ hàng của tài khoản với id: " + accountId));

        // 2. Tìm item trong giỏ hàng của cart
        CartItem cartItem = cartItemRepository.findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException("Không tồn tại sản phẩm: " + itemId + " trong giỏ hàng"));

        // 3. Xóa
        cart.getItems().remove(cartItem);

        return getCartByAccountId(accountId);
    }

    @Transactional
    public CartResponse updateCartItem(Integer accountId, Integer itemId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        // Lấy giỏ hàng
        Cart cart = cartRepository.findByAccountId(accountId)
                .orElseThrow(() -> new CartNotFoundException("Không tìm thấy giỏ hàng của tài khoản với id: " + accountId));

        // Tìm item trong giỏ hàng
        CartItem cartItem = cartItemRepository.findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException("Không tồn tại sản phẩm: " + itemId + " trong giỏ hàng"));

        // Cập nhật số lượng
        cartItem.setQuantity(newQuantity);

        return getCartByAccountId(accountId);
    }

    private CartItemResponse convertCartItemToDto(CartItem cartItem, ProductResponse product) {
        CartItemResponse dto = new CartItemResponse();

        // Gán id
        dto.setId(cartItem.getId());

        // Tên sản phẩm
        dto.setProductName(product.getName());

        // Biến thể
        ProductVariantResponse variant = product.getVariants().getFirst();
        dto.setVariantId(variant.getId());
        dto.setVariantName(variant.getName());

        // Ảnh mặc định
        for (ProductImageResponse image : variant.getImages()) {
            if (image.isDefaultStatus()) dto.setDefaultImage(image);
            break;
        }

        // Giá biến thể
        dto.setBasePrice(variant.getBasePrice());
        dto.setDiscountPrice(variant.getDiscountPrice());

        // Số lượng
        dto.setQuantity(cartItem.getQuantity());

        return dto;
    }

    private CartResponse convertCartToDto(Cart cart, List<CartItemResponse> cartItemResponseList) {
        CartResponse dto = new CartResponse();
        dto.setId(cart.getId());
        dto.setAccountId(cart.getAccountId());
        dto.setItems(cartItemResponseList);
        return dto;
    }
}
