package com.tqk.productservice.repository.client;

import com.tqk.productservice.dto.response.supplier.SupplierResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service", url = "http://supplier-service:8080")
public interface SupplierClient {
    @GetMapping("/suppliers/{supplierId}")
    SupplierResponse getSupplierById(@PathVariable Integer supplierId);
}
