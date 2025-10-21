package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.dto.product.ProductResponse;
import com.tqk.stationeryecommercebackend.model.Category;
import com.tqk.stationeryecommercebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsActiveTrue();

    Page<Product> findByCategory(Category category, Pageable pageable);

    Optional<Product> findBySlug(String slug);
}
