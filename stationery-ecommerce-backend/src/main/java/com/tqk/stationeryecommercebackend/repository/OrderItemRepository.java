package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
