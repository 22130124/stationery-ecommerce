package com.tqk.productservice.controller;

import com.tqk.productservice.dto.request.ProductRequest;
import com.tqk.productservice.dto.response.ProductListResponse;
import com.tqk.productservice.dto.response.ProductResponse;
import com.tqk.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
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

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        Map<String, Object> response = Map.of("message", "Sản phẩm đã được xóa thành công");
        return ResponseEntity.ok(response);
    }


    // ========== USER ===========

    @GetMapping()
    public ResponseEntity<?> getProductsByActiveStatus(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                       @RequestParam(name = "limit", required = false, defaultValue = "12") int size) {
        ProductListResponse result = productService.getProductsByActiveStatus(page, size);
        Map<String, Object> response = Map.of(
                "products", result.getProducts(),
                "currentPage", result.getCurrentPage(),
                "totalPages", result.getTotalPages(),
                "totalItems", result.getTotalItems()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-category")
    public ResponseEntity<?> getProductsByCategory(@RequestParam(name = "categorySlug", required = false) String categorySlug,
                                                   @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                   @RequestParam(name = "limit", required = false, defaultValue = "12") int size) {
        ProductListResponse result = productService.getByActiveStatusAndCategory(categorySlug, page, size);
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

    @PostMapping(value = "/by-variant-ids", consumes = "application/json")
    public ResponseEntity<List<ProductResponse>> getProductsByVariantIds(@RequestBody List<Integer> variantIds) {
        return ResponseEntity.ok(productService.getProductsByVariantIds(variantIds));
    }

    @GetMapping("/test-client/{slug}")
    public ResponseEntity<?> getCategoryIdBySlug(@PathVariable String slug) {
        Integer categoryId = productService.getCategoryIdBySlug(slug);
        return ResponseEntity.ok(categoryId);
    }

    // ========== AI SEARCH ===========
    @GetMapping("/search-by-ai")
    public ResponseEntity<?> searchProducts(@RequestParam(required = false) Integer categoryId,
                                            @RequestParam(required = false) Integer brandId,
                                            @RequestParam(required = false) List<String> colors,
                                            @RequestParam(required = false) Integer minPrice,
                                            @RequestParam(required = false) Integer maxPrice,
                                            @RequestParam(required = false) List<String> extras
    ) {
        List<ProductResponse> products = productService.searchProducts(categoryId, brandId, colors, minPrice, maxPrice, extras);
        return ResponseEntity.ok(products);
    }
}
