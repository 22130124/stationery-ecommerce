package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.category.CategoryResponse;
import com.tqk.stationeryecommercebackend.model.Category;
import com.tqk.stationeryecommercebackend.repository.CategoryRepository;
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
        List<Category> categories = categoryRepository.findByIsActiveTrueAndParentIsNull();
        List<CategoryResponse> categoriesDto = new ArrayList<>();
        categories.forEach(category -> categoriesDto.add(category.convertToDto()));
        return categoriesDto;
    }
}
