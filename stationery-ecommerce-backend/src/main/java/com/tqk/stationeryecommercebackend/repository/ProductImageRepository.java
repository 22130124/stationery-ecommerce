package com.tqk.stationeryecommercebackend.repository;

import com.tqk.stationeryecommercebackend.model.Product;
import com.tqk.stationeryecommercebackend.model.ProductImage;
import com.tqk.stationeryecommercebackend.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByVariant(ProductVariant savedVariant);

    List<ProductImage> findByProductAndVariantIsNull(Product updatedProduct);
}
