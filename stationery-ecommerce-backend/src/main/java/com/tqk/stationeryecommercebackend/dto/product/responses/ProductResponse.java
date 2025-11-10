package com.tqk.stationeryecommercebackend.dto.product.responses;

import com.tqk.stationeryecommercebackend.dto.brand.BrandResponse;
import com.tqk.stationeryecommercebackend.dto.category.CategoryResponse;
import com.tqk.stationeryecommercebackend.dto.supplier.SupplierResponse;

import java.time.LocalDateTime;
import java.util.List;

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
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductVariantResponse> variants;
    private ProductVariantResponse defaultVariant;
    private ProductImageResponse defaultImage;
    private List<ProductImageResponse> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public SupplierResponse getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierResponse supplier) {
        this.supplier = supplier;
    }

    public BrandResponse getBrand() {
        return brand;
    }

    public void setBrand(BrandResponse brand) {
        this.brand = brand;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ProductVariantResponse> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantResponse> variants) {
        this.variants = variants;
    }

    public ProductVariantResponse getDefaultVariant() {
        return defaultVariant;
    }

    public void setDefaultVariant(ProductVariantResponse defaultVariant) {
        this.defaultVariant = defaultVariant;
    }

    public ProductImageResponse getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(ProductImageResponse defaultImage) {
        this.defaultImage = defaultImage;
    }

    public List<ProductImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ProductImageResponse> images) {
        this.images = images;
    }
}
