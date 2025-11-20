package com.tqk.cartservice.service;

import com.tqk.cartservice.dto.response.cart.CartItemResponse;
import com.tqk.cartservice.dto.response.cart.CartResponse;
import com.tqk.cartservice.dto.response.product.ProductImageResponse;
import com.tqk.cartservice.dto.response.product.ProductResponse;
import com.tqk.cartservice.dto.response.product.ProductVariantResponse;
import com.tqk.cartservice.model.Cart;
import com.tqk.cartservice.model.CartItem;
import com.tqk.cartservice.repository.CartRepository;
import com.tqk.cartservice.repository.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
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

    private CartItemResponse convertCartItemToDto(CartItem cartItem, ProductResponse product) {
        CartItemResponse dto = new CartItemResponse();

        // Tên sản phẩm
        dto.setProductName(product.getName());

        // Tên biến thể
        ProductVariantResponse variant = product.getVariants().getFirst();
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
}
