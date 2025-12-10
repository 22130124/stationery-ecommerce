package com.tqk.productservice.dto.request;

import lombok.Data;

@Data
public class UpdateInventoryRequest {
    private Integer variantId;
    private Integer quantity;
}
