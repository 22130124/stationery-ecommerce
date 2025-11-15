package com.tqk.brandservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandResponse {
    private Integer id;
    private SupplierResponse supplier;
    private String name;
    private boolean activeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
