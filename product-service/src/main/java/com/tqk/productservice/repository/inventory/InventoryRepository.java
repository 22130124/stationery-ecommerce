package com.tqk.productservice.repository.inventory;

import com.tqk.productservice.model.inventory.Inventory;
import com.tqk.productservice.model.product.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findByProductVariant(ProductVariant productVariant);

    @Query(value = """
                SELECT i.stock
                FROM inventory i
                WHERE variant_id = :variantId
            """, nativeQuery = true)
    Integer findStockForUpdate(Integer variantId);

    @Modifying
    @Transactional
    @Query(value = """
                UPDATE inventory
                SET stock = stock - :quantity
                WHERE variant_id = :variantId
            """, nativeQuery = true)
    void decreaseStock(Integer variantId, Integer quantity);

    @Modifying
    @Transactional
    @Query(value = """
                UPDATE inventory
                SET stock = stock + :quantity
                WHERE variant_id = :variantId
            """, nativeQuery = true)
    void increaseStock(Integer variantId, Integer quantity);
}