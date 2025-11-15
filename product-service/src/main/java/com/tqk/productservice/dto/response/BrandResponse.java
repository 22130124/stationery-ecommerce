package com.tqk.productservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BrandResponse {
    private Integer id;
    private SupplierResponse supplier;
    private String name;
    private boolean activeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
