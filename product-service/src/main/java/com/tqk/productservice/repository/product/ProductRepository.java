package com.tqk.productservice.repository.product;

//import com.tqk.productservice.model.Category;
import com.tqk.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySlug(String slug);

    Product findTopByOrderByIdDesc();

    Page<Product> findByCategoryIdAndActiveStatusTrue(Integer categoryId, Pageable pageable);

    Page<Product> findByActiveStatusTrue(Pageable pageable);
}
