package com.tqk.categoryservice.controller;

import com.tqk.categoryservice.dto.response.CategoryResponse;
import com.tqk.categoryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<CategoryResponse> categories = categoryService.getCategories();
        Map<String, Object> response = Map.of("categories", categories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        Map<String, Object> response = Map.of("category", category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<?> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse category = categoryService.getCategoryBySlug(slug);
        Map<String, Object> response = Map.of("category", category);
        return ResponseEntity.ok(response);
    }
}
