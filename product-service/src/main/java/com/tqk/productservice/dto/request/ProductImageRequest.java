package com.tqk.productservice.dto.request;

import lombok.Data;

@Data
public class ProductImageRequest {
    private String url;
    private String publicId;
    private Boolean defaultStatus = false;
}
