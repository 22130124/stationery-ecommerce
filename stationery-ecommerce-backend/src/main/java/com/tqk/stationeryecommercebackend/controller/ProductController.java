package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.product.requests.ProductRequest;
import com.tqk.stationeryecommercebackend.dto.product.responses.ProductListResponse;
import com.tqk.stationeryecommercebackend.dto.product.responses.ProductResponse;
import com.tqk.stationeryecommercebackend.service.ProductService;
import jakarta.persistence.EntityListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/admin")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            ProductResponse product = productService.saveProduct(productRequest);
            Map<String, Object> response = Map.of("product", product, "message", "Product created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = Map.of("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductRequest productRequest) {
        try {
            ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
            Map<String, Object> response = Map.of(
                    "product", updatedProduct,
                    "message", "Product updated successfully"
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = Map.of("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/{slug}")
    public ResponseEntity<?> getProductBySlug(@PathVariable("slug") String slug) {
        ProductResponse product = productService.getProductBySlug(slug);
        Map<String, Object> response = Map.of("product", product);
        return ResponseEntity.ok(response);
    }
}
