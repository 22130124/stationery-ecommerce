package com.tqk.brandservice.repository;

import com.tqk.brandservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByAccountId(Integer accountId);
}
