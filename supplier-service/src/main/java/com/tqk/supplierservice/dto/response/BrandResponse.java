package com.tqk.supplierservice.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class BrandResponse {
    private Integer id;
    private String name;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
