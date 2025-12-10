package com.tqk.productservice.repository.product;

import com.tqk.productservice.model.ProductInventory;
import com.tqk.productservice.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Integer> {
    ProductInventory findByProductVariant(ProductVariant productVariant);
}
