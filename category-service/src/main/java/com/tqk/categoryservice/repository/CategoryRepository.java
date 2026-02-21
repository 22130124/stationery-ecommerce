package com.tqk.categoryservice.repository;

import com.tqk.categoryservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    List<Category> findByStatusAndParentIsNull(Category.CategoryStatus status);

    Optional<Category> findBySlug(String slug);

    List<Category> findByParentIsNull();
}
