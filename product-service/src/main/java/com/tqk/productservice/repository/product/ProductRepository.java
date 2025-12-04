package com.tqk.productservice.repository.product;

//import com.tqk.productservice.model.Category;

import com.tqk.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySlug(String slug);

    Product findTopByOrderByIdDesc();

    Page<Product> findByCategoryIdAndActiveStatusTrue(Integer categoryId, Pageable pageable);

    Page<Product> findByActiveStatusTrue(Pageable pageable);

//    @Query(value = """
//            SELECT\s
//                p.*,
//                (
//                    (CASE WHEN :keyword IS NOT NULL AND LOWER(p.name) LIKE CONCAT('%', LOWER(:keyword), '%') THEN 5 ELSE 0 END) +
//                    (CASE WHEN :keyword IS NOT NULL AND LOWER(p.description) LIKE CONCAT('%', LOWER(:keyword), '%') THEN 3 ELSE 0 END) +
//                    (CASE WHEN :extra   IS NOT NULL AND LOWER(p.description) LIKE CONCAT('%', LOWER(:extra),   '%') THEN 2 ELSE 0 END) +
//                    (CASE WHEN :color   IS NOT NULL AND v.color_match = 1 THEN 1 ELSE 0 END) +
//                    (CASE WHEN (:priceMin IS NOT NULL OR :priceMax IS NOT NULL) AND v.price_match = 1 THEN 3 ELSE 0 END)
//                ) AS score
//            FROM products p
//            LEFT JOIN (
//                SELECT\s
//                    pv.product_id,
//                    MAX(CASE WHEN LOWER(pv.name) LIKE CONCAT('%', LOWER(:color), '%') THEN 1 ELSE 0 END) AS color_match,
//                    MAX(CASE\s
//                          WHEN (:priceMin IS NULL OR pv.discount_price >= :priceMin)
//                           AND (:priceMax IS NULL OR pv.discount_price <= :priceMax)
//                          THEN 1 ELSE 0 END
//                    ) AS price_match
//                FROM product_variants pv
//                GROUP BY pv.product_id
//            ) v ON v.product_id = p.id
//            WHERE p.active_status = true
//              AND (:priceMin IS NULL OR v.price_match = 1)
//              AND (:priceMax IS NULL OR v.price_match = 1)
//            ORDER BY score DESC
//            """,
//            nativeQuery = true)
//    List<Product> searchProductsWithScore(
//            @Param("categoryId") Integer categoryId,
//            @Param("keyword") String keyword,
//            @Param("color") String color,
//            @Param("priceMin") Integer priceMin,
//            @Param("priceMax") Integer priceMax,
//            @Param("extra") String extra
//    );

    @Query(value = """
        SELECT p.*
        FROM products p
        WHERE p.active_status = true
          AND p.category_id = :categoryId
        ORDER BY p.id DESC
        """,
            nativeQuery = true)
    List<Product> searchProductsWithScore(
            @Param("categoryId") Integer categoryId,
            @Param("keyword") String keyword,
            @Param("color") String color,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("extra") String extra
    );

}
