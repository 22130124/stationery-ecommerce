package com.tqk.supplierservice.controller;

import com.tqk.supplierservice.dto.request.AddUpdateBrandRequest;
import com.tqk.supplierservice.dto.response.BrandResponse;
import com.tqk.supplierservice.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<?> getActiveBrands() {
        List<BrandResponse> brands = brandService.getActiveBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/by-supplier/{supplierId}")
    public ResponseEntity<?> getBrandsBySupplierId(@PathVariable Integer supplierId) {
        List<BrandResponse> brands = brandService.getBySupplierId(supplierId);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        BrandResponse brand = brandService.getById(id);
        return ResponseEntity.ok(brand);
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody AddUpdateBrandRequest request) {
        BrandResponse brand = brandService.createBrand(request);
        return ResponseEntity.ok(brand);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Integer id, @RequestBody AddUpdateBrandRequest request) {
        BrandResponse brand = brandService.updateBrand(id, request);
        return ResponseEntity.ok(brand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id) {
        Integer deletedId = brandService.deleteBrand(id);
        return ResponseEntity.ok(deletedId);
    }
}
