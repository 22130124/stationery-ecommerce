package com.tqk.productservice.repository.product;

//import com.tqk.productservice.model.Category;
import com.tqk.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByActiveStatusTrue();

    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    Optional<Product> findBySlug(String slug);

    Product findTopByOrderByIdDesc();
}
