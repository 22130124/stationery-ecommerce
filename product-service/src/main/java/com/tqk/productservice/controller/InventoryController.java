package com.tqk.productservice.controller;

import com.tqk.productservice.dto.request.inventory.ReserveRequest;
import com.tqk.productservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody ReserveRequest request) {
        inventoryService.reserve(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/confirm/{orderCode}")
    public ResponseEntity<?> confirm(@PathVariable("orderCode") String orderCode) {
        inventoryService.confirm(orderCode);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/release/{orderCode}")
    public ResponseEntity<?> release(@PathVariable("orderCode") String orderCode) {
        inventoryService.release(orderCode);
        return ResponseEntity.ok().build();
    }
}
