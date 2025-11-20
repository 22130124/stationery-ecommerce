package com.tqk.cartservice.repository;

import com.tqk.cartservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByAccountId(Integer accountId);
}
