package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.brand.BrandResponse;
import com.tqk.stationeryecommercebackend.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/{supplierId}")
    public ResponseEntity<?> getBrandsBySupplierId(@PathVariable Integer supplierId) {
        List<BrandResponse> brands = brandService.getBrandsBySupplierId(supplierId);
        Map<String, Object> response = Map.of("brands", brands);
        return ResponseEntity.ok(response);
    }
}
