package com.tqk.productservice.dto.response;

import com.tqk.productservice.dto.response.brand.BrandResponse;
import com.tqk.productservice.dto.response.category.CategoryResponse;
import com.tqk.productservice.dto.response.supplier.SupplierResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponse {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private CategoryResponse category;
    private SupplierResponse supplier;
    private BrandResponse brand;
    private String origin;
    private String slug;
    private double rating;
    private boolean activeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductVariantResponse> variants = new ArrayList<>();
    private ProductVariantResponse defaultVariant;
    private ProductImageResponse defaultImage;
    private int totalStock;
}
