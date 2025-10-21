package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.product.ProductListResponse;
import com.tqk.stationeryecommercebackend.dto.product.ProductResponse;
import com.tqk.stationeryecommercebackend.service.ProductService;
import jakarta.persistence.EntityListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@EntityListeners(AuditingEntityListener.class)
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/admin")
    public ResponseEntity<?> getAll() {
        List<ProductResponse> products = productService.getProducts();
        Map<String, Object> response = Map.of("products", products);
        return ResponseEntity.ok(response); 
    }

    @GetMapping
    public ResponseEntity<?> getProductsByCategoryAndPagination(@RequestParam(name = "categorySlug", required = false, defaultValue = "all") String categorySlug,
                                                              @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                              @RequestParam(name = "limit", required = false, defaultValue = "12") int size) {
        ProductListResponse result = productService.getProductsByCategoryAndPagination(categorySlug, page, size);
        Map<String, Object> response = Map.of(
                "products", result.getProducts(),
                "currentPage", result.getCurrentPage(),
                "totalPages", result.getTotalPages(),
                "totalItems", result.getTotalItems()
        );
        return ResponseEntity.ok(response);
    }
}
