package com.tqk.productservice.dto.response;

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
    private Integer supplierId;
    private Integer brandId;
    private Integer categoryId;
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
//    private List<ProductImageResponse> images = new ArrayList<>();
}
