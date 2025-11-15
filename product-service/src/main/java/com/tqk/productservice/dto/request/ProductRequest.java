package com.tqk.productservice.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ProductRequest {
    private String name;
    private String slug;
    private Integer supplierId;
    private Integer brandId;
    private Integer categoryId;
    private String origin;
    private String description;
    private List<ProductImageRequest> images;
    private List<ProductVariantRequest> variants;
}
