package com.tqk.productservice.repository.product;

//import com.tqk.productservice.model.Category;

import com.tqk.productservice.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySlugAndStatusNot(String slug, Product.ProductStatus status);

    Product findTopByOrderByIdDesc();

    List<Product> findByStatusNot(Product.ProductStatus productStatus);

    Page<Product> findByCategoryIdAndStatusNot(Integer categoryId, Product.ProductStatus status, Pageable pageable);

    Page<Product> findByStatus(Product.ProductStatus status, Pageable pageable);

    List<Product> findByStatus(Product.ProductStatus status);

    boolean existsBySlugAndStatusNot(String slug, Product.ProductStatus status);

    @Query(value = """
            SELECT DISTINCT p.*
            FROM products p
            LEFT JOIN product_variants pv ON pv.product_id = p.id
            LEFT JOIN product_variant_colors pvc ON pvc.product_id = p.id
            WHERE p.status = 'ACTIVE'
                AND (:categoryId IS NULL OR p.category_id = :categoryId)
                AND (:brandId IS NULL OR p.brand_id = :brandId)
                /* nếu hasColors = 1 thì filter theo màu */
                AND (:hasColors = 0 OR pvc.color IN (:colors))
                /* filter theo priceMin nếu có */
                AND (:priceMin IS NULL OR COALESCE(pv.discount_price, pv.base_price) >= :priceMin)
                /* filter theo priceMax nếu có */
                AND (:priceMax IS NULL OR COALESCE(pv.discount_price, pv.base_price) <= :priceMax)
            ORDER BY p.id DESC
            """,
            nativeQuery = true)
    List<Product> searchProductsWithScore(
            @Param("categoryId") Integer categoryId,
            @Param("brandId") Integer brandId,
            @Param("colors") List<String> colors,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("extras") List<String> extras,
            @Param("hasColors") int hasColors
    );
}
