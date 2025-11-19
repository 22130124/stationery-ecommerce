package com.tqk.brandservice.controller;

import com.tqk.brandservice.dto.response.SupplierResponse;
import com.tqk.brandservice.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Map<String, Object> response = Map.of("suppliers", suppliers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        SupplierResponse supplier = supplierService.getById(id);
        Map<String, Object> response = Map.of("supplier", supplier);
        return ResponseEntity.ok(response);
    }
}
