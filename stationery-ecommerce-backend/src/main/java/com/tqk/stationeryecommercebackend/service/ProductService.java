package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.product.ProductListResponse;
import com.tqk.stationeryecommercebackend.dto.product.ProductResponse;
import com.tqk.stationeryecommercebackend.dto.product.ProductVariantResponse;
import com.tqk.stationeryecommercebackend.exception.ProductNotFoundException;
import com.tqk.stationeryecommercebackend.model.Category;
import com.tqk.stationeryecommercebackend.model.Product;
import com.tqk.stationeryecommercebackend.model.ProductVariant;
import com.tqk.stationeryecommercebackend.repository.CategoryRepository;
import com.tqk.stationeryecommercebackend.repository.ProductRepository;
import com.tqk.stationeryecommercebackend.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductVariantRepository productVariantRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
    }

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findByIsActiveTrue();
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (!products.isEmpty()) {
            products.forEach(product -> productResponseList.add(product.convertToDto()));
        }
        return productResponseList;
    }

    public ProductListResponse getProductsByCategoryAndPagination(String categorySlug, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage;
        if(categorySlug.equalsIgnoreCase("all")) {
            productPage = productRepository.findAll(pageable);
        } else {
            Category category = categoryRepository.findBySlug(categorySlug);
            productPage = productRepository.findByCategory(category, pageable);
        }

        return new ProductListResponse(
                productPage.getContent(),
                productPage.getNumber() + 1,
                productPage.getTotalPages(),
                productPage.getTotalElements()
        );
    }

    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với slug: " + slug));
        return product.convertToDto();
    }

    public List<ProductVariant> getProductVariantsByIds(List<Integer> variantIds) {
        List<ProductVariant> variants = new ArrayList<>();
        for(Integer id : variantIds) {
            ProductVariant variant = productVariantRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy biến thể với id: " + id));
            variants.add(variant);
        }
        return variants;
    }
}
