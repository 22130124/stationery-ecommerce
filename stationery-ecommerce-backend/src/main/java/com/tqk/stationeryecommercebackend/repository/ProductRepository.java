package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.Category;
import com.tqk.stationeryecommercebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrue();

    Page<Product> findByCategory(Category category, Pageable pageable);
}
