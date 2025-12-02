package com.tqk.brandservice.dto.request;

import lombok.Data;

@Data
public class AddUpdateRequest {
    private Integer supplierId;
    private String name;
    private boolean activeStatus;
}
