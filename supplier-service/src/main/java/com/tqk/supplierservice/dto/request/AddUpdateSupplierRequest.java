package com.tqk.supplierservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddUpdateSupplierRequest {
    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    private String name;
    private String status;
}
