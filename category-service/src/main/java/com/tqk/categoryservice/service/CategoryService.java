package com.tqk.categoryservice.service;

import com.tqk.categoryservice.dto.response.CategoryResponse;
import com.tqk.categoryservice.exception.CategoryNotFoundException;
import com.tqk.categoryservice.model.Category;
import com.tqk.categoryservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findByActiveStatusTrueAndParentIsNull();
        List<CategoryResponse> categoriesDto = new ArrayList<>();
        categories.forEach(category -> categoriesDto.add(category.convertToDto()));
        return categoriesDto;
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy danh mục với ID: " + id));
        return category.convertToDto();
    }

    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug).orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy danh mục với slug: " + slug));
        return category.convertToDto();
    }
}
