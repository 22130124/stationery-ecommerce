package com.tqk.productservice.dto.request;

import com.tqk.productservice.model.product.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String slug;
    private Integer supplierId;
    private Integer brandId;
    private Integer categoryId;
    private String origin;
    private String description;
    private String status;
    private List<ProductVariantRequest> variants = new ArrayList<>();
    private int stock;
}
