package com.tqk.productservice.repository.product;

import com.tqk.productservice.model.Product;
import com.tqk.productservice.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {
    List<ProductVariant> findByProduct(Product updatedProduct);
}
