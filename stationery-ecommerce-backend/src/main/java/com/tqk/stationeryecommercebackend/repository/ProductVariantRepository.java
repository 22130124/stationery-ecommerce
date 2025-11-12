package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.Product;
import com.tqk.stationeryecommercebackend.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {
    List<ProductVariant> findByProduct(Product updatedProduct);
}
