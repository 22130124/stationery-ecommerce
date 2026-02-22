package com.tqk.supplierservice.controller;

import com.tqk.supplierservice.dto.request.AddUpdateSupplierRequest;
import com.tqk.supplierservice.dto.response.SupplierResponse;
import com.tqk.supplierservice.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<?> getSuppliers() {
        List<SupplierResponse> suppliers = supplierService.getSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        SupplierResponse supplier = supplierService.getById(id);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody AddUpdateSupplierRequest request) {
        SupplierResponse supplier = supplierService.createSupplier(request);
        return ResponseEntity.ok(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Integer id, @RequestBody AddUpdateSupplierRequest request) {
        SupplierResponse supplier = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(supplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Integer id) {
        Integer supplierId = supplierService.deleteSupplier(id);
        return ResponseEntity.ok(supplierId);
    }
}
    