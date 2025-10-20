package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.category.CategoryResponse;
import com.tqk.stationeryecommercebackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<CategoryResponse> categories = categoryService.getCategories();
        Map<String, Object> response = Map.of("categories", categories);
        return ResponseEntity.ok(response);
    }
}
