package com.tqk.brandservice.controller;

import com.tqk.brandservice.dto.response.BrandResponse;
import com.tqk.brandservice.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<?> getBrandsBySupplierId(@PathVariable Integer supplierId) {
        List<BrandResponse> brands = brandService.getBySupplierId(supplierId);
        Map<String, Object> response = Map.of("brands", brands);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        BrandResponse brand = brandService.getById(id);
        return ResponseEntity.ok(brand);
    }
}
