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

    List<CartItem> findByCartOrderByAddedAtDesc(Cart card);

    @Transactional
    void deleteByCartAndIdIn(Cart cart, List<Integer> ids);

    @Transactional
    void deleteByCartAndVariantId(Cart cart, Integer variantId);

    List<CartItem> findByCart(Cart cart);

    void deleteByCartAndVariantIdIn(Cart cart, List<Integer> variantIds);
}
