package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
