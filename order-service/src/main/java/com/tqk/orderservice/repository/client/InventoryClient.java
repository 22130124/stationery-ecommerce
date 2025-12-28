package com.tqk.orderservice.repository.client;

import com.tqk.orderservice.dto.request.inventory.ReserveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", contextId = "inventoryClient", url = "http://product-service:8080")
public interface InventoryClient {
    @PostMapping("/inventory/reserve")
    void reserve(@RequestBody ReserveRequest request);

    @PutMapping("/inventory/release/{orderId}")
    void release(@PathVariable Integer orderId);

    @PutMapping("/inventory/confirm/{orderId}")
    void confirm(@PathVariable Integer orderId);
}
