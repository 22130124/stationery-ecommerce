package com.tqk.categoryservice.service;

import com.tqk.categoryservice.dto.request.AddUpdateRequest;
import com.tqk.categoryservice.dto.response.CategoryResponse;
import com.tqk.categoryservice.exception.CategoryNotFoundException;
import com.tqk.categoryservice.model.Category;
import com.tqk.categoryservice.repository.CategoryRepository;
import jakarta.transaction.Transactional;
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

    public List<CategoryResponse> getActiveCategories() {
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

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findByParentIsNull();
        List<CategoryResponse> categoriesDto = new ArrayList<>();
        categories.forEach(category -> categoriesDto.add(category.convertToDto()));
        return categoriesDto;
    }

    @Transactional
    public CategoryResponse createCategory(AddUpdateRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setParent(categoryRepository.findById(request.getParentId()).orElse(null));
        category.setActiveStatus(request.isActiveStatus());
        categoryRepository.save(category);
        return category.convertToDto();
    }

    @Transactional
    public CategoryResponse updateCategory(Integer id, AddUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy danh mục với ID: " + id));
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        if (request.getParentId() != null) {
            category.setParent(categoryRepository.findById(request.getParentId()).orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy danh mục với ID: " + id)));
        } else {
            category.setParent(null);
        }
        category.setActiveStatus(request.isActiveStatus());
        categoryRepository.save(category);
        return category.convertToDto();
    }

    @Transactional
    public Integer deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Không tìm thấy danh mục với ID: " + id));
        categoryRepository.delete(category);
        return id;
    }
}
