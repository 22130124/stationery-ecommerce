package com.tqk.productservice.repository.product;

import com.tqk.productservice.model.ProductVariant;
import com.tqk.productservice.model.ProductVariantColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantColorRepository extends JpaRepository<ProductVariantColor,Integer> {
    List<ProductVariantColor> findByVariant(ProductVariant existingVariant);
}
