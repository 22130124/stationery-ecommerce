package com.tqk.supplierservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddUpdateBrandRequest {
    private Integer supplierId;
    @NotBlank(message = "Tên thương hiệu không được để trống")
    private String name;
    private String status;
}
