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
            ProductResponse product = productService.createProduct(productRequest);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductRequest productRequest) {
        try {
            ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getProductsByCategorySlugAndPagination(@RequestParam(name = "categorySlug", required = false, defaultValue = "all") String categorySlug,
                                                                @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                                @RequestParam(name = "limit", required = false, defaultValue = "12") int size) {
        ProductListResponse result = productService.getByCategorySlugAndPagination(categorySlug, page, size);
        Map<String, Object> response = Map.of(
                "products", result.getProducts(),
                "currentPage", result.getCurrentPage(),
                "totalPages", result.getTotalPages(),
                "totalItems", result.getTotalItems()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> getProductBySlug(@PathVariable("slug") String slug) {
        ProductResponse product = productService.getBySlug(slug);
        Map<String, Object> response = Map.of("product", product);
        return ResponseEntity.ok(response);
    }
}
