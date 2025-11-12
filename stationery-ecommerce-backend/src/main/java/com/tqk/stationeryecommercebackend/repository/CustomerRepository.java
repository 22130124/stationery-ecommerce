package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
