package com.tqk.categoryservice.repository;

import com.tqk.categoryservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByAccountIdOrderByCreatedAtDesc(Integer accountId);
}
