package com.tqk.brandservice.controller;

import com.tqk.brandservice.dto.request.AddUpdateRequest;
import com.tqk.brandservice.dto.response.SupplierResponse;
import com.tqk.brandservice.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

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
    public ResponseEntity<?> addSupplier(@RequestBody AddUpdateRequest request) {
        SupplierResponse supplier = supplierService.addSupplier(request);
        return ResponseEntity.ok(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Integer id, @RequestBody AddUpdateRequest request) {
        SupplierResponse supplier = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(supplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Integer id) {
        Integer supplierId = supplierService.deleteSupplier(id);
        return ResponseEntity.ok(supplierId);
    }
}
    