package com.tqk.stationeryecommercebackend.controller;

import com.tqk.stationeryecommercebackend.dto.supplier.SupplierResponse;
import com.tqk.stationeryecommercebackend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    @GetMapping
    public ResponseEntity<?> getSuppliers() {
        List<SupplierResponse> suppliers = supplierService.getSuppliers();
        Map<String, Object> response = Map.of("suppliers", suppliers);
        return ResponseEntity.ok(response);
    }
}
