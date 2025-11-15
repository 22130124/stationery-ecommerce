package com.tqk.brandservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplierResponse {
    private Integer id;
    private String name;
    private boolean activeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
