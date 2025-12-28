package com.tqk.cartservice.repository;

import com.tqk.cartservice.model.Cart;
import com.tqk.cartservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    Optional<CartItem> findByIdAndCartId(Integer itemId, Integer id);

    Optional<CartItem> findByCartIdAndVariantId(Integer id, Integer variantId);

    @Transactional
    void deleteByCartAndVariantIdIn(Cart card, List<Integer> variantIds);
}
