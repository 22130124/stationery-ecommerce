package com.tqk.productservice.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ProductVariantRequest {
    private Integer id;
    private String name;
    private Double basePrice;
    private Double discountPrice;
    private Boolean activeStatus;
    private Boolean defaultStatus;
    private List<ProductImageRequest> images;
    private List<String> colors;
}
