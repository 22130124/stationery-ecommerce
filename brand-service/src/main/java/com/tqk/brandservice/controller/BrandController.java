package com.tqk.brandservice.controller;

import com.tqk.brandservice.dto.request.AddUpdateRequest;
import com.tqk.brandservice.dto.response.BrandResponse;
import com.tqk.brandservice.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addBrand(@RequestBody AddUpdateRequest request) {
        BrandResponse brand = brandService.addBrand(request);
        return ResponseEntity.ok(brand);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Integer id, @RequestBody AddUpdateRequest request) {
        BrandResponse brand = brandService.updateBrand(id, request);
        return ResponseEntity.ok(brand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id) {
        Integer deletedId = brandService.deleteBrand(id);
        return ResponseEntity.ok(deletedId);
    }
}
