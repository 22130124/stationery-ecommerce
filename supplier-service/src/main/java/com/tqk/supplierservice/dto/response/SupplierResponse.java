package com.tqk.supplierservice.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SupplierResponse {
    private Integer id;
    private String name;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private List<BrandResponse> brands;
}
