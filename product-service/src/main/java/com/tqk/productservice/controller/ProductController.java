package com.tqk.productservice.controller;

import com.tqk.productservice.dto.request.ProductRequest;
import com.tqk.productservice.dto.response.ProductListResponse;
import com.tqk.productservice.dto.response.ProductResponse;
import com.tqk.productservice.service.ProductService;
import jakarta.persistence.EntityListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@EntityListeners(AuditingEntityListener.class)
public class ProductController {
    @Autowired
    private ProductService productService;

    // ============ ADMIN =============

    @GetMapping("/admin")
    public ResponseEntity<?> getAllForAdmin() {
        List<ProductResponse> products = productService.getAllForAdmin();
        Map<String, Object> response = Map.of("products", products);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse product = productService.createProduct(productRequest);
        Map<String, Object> response = Map.of("product", product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        Map<String, Object> response = Map.of("updatedProduct", updatedProduct);
        return ResponseEntity.ok(response);
    }

    // ========== USER ===========

    @GetMapping()
    public ResponseEntity<?> getAllForUser(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(name = "limit", required = false, defaultValue = "12") int size) {
        ProductListResponse result = productService.getAllForUser(page, size);
        Map<String, Object> response = Map.of(
                "products", result.getProducts(),
                "currentPage", result.getCurrentPage(),
                "totalPages", result.getTotalPages(),
                "totalItems", result.getTotalItems()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-category")
    public ResponseEntity<?> getProductsByCategory(@RequestParam(name = "categoryId", required = false) Integer categoryId,
                                                   @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                   @RequestParam(name = "limit", required = false, defaultValue = "12") int size) {
        ProductListResponse result = productService.getByCategory(categoryId, page, size);
        Map<String, Object> response = Map.of(
                "products", result.getProducts(),
                "currentPage", result.getCurrentPage(),
                "totalPages", result.getTotalPages(),
                "totalItems", result.getTotalItems()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<?> getProductBySlug(@PathVariable("slug") String slug) {
        ProductResponse product = productService.getBySlug(slug);
        Map<String, Object> response = Map.of("product", product);
        return ResponseEntity.ok(response);
    }
}
