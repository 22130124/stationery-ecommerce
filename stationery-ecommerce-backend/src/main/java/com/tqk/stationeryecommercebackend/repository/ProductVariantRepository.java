package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {
}
