package com.tqk.cartservice.repository;

import com.tqk.cartservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    Optional<CartItem> findByIdAndCartId(Integer itemId, Integer id);

    Optional<CartItem> findByCartIdAndVariantId(Integer id, Integer variantId);
}
