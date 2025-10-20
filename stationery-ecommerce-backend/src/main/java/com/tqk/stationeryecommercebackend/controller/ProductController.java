package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.product.ProductResponse;
import com.tqk.stationeryecommercebackend.service.ProductService;
import jakarta.persistence.EntityListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@EntityListeners(AuditingEntityListener.class)
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProducts() {
        List<ProductResponse> products = productService.getProducts();
        Map<String, Object> response = Map.of("products", products);
        return ResponseEntity.ok(response); 
    }
}
