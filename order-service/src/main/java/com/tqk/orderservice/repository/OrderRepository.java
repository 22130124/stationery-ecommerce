package com.tqk.orderservice.repository;

import com.tqk.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByAccountIdOrderByCreatedAtDesc(Integer accountId);
}
