package com.tqk.productservice.controller;

import com.tqk.productservice.dto.request.ProductRequest;
import com.tqk.productservice.dto.request.UpdateInventoryRequest;
import com.tqk.productservice.dto.response.ProductListResponse;
import com.tqk.productservice.dto.response.ProductResponse;
import com.tqk.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

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


    @GetMapping()
    public ResponseEntity<?> getProductsByActiveStatus() {
        List<ProductResponse> products = productService.getProductsByActiveStatus();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/by-category")
    public ResponseEntity<?> getProductsByCategory(@RequestParam(name = "categorySlug", required = false) String categorySlug,
                                                   @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                   @RequestParam(name = "limit", required = false, defaultValue = "25") int size) {
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
    public ResponseEntity<?> getProductsByVariantIds(@RequestBody List<Integer> variantIds) {
        return ResponseEntity.ok(productService.getProductsByVariantIds(variantIds));
    }

    @GetMapping("/test-client/{slug}")
    public ResponseEntity<?> getCategoryIdBySlug(@PathVariable String slug) {
        Integer categoryId = productService.getCategoryIdBySlug(slug);
        return ResponseEntity.ok(categoryId);
    }

    @GetMapping("/{variantId}/stock")
    public ResponseEntity<?> getProductStock(@PathVariable("variantId") Integer variantId) {
        int stock = productService.getStock(variantId);
        return ResponseEntity.ok(stock);
    }

    @PutMapping("/inventory")
    public ResponseEntity<?> updateInventory(@RequestBody UpdateInventoryRequest request) {
        int newStock = productService.updateInventory("replace", request);
        return ResponseEntity.ok(newStock);
    }

    @PutMapping("/inventory/increase")
    public ResponseEntity<?> increaseInventory(@RequestBody UpdateInventoryRequest request) {
        int newStock = productService.updateInventory("increase", request);
        return ResponseEntity.ok(newStock);
    }

    @PutMapping("/inventory/decrease")
    public ResponseEntity<?> decreaseInventory(@RequestBody UpdateInventoryRequest request) {
        int newStock = productService.updateInventory("decrease", request);
        return ResponseEntity.ok(newStock);
    }
}
