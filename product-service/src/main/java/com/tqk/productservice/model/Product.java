package com.tqk.productservice.model;

import com.tqk.productservice.dto.response.ProductImageResponse;
import com.tqk.productservice.dto.response.ProductResponse;
import com.tqk.productservice.dto.response.ProductVariantResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "origin")
    private String origin;

    @Column(name = "slug")
    private String slug;

    @Column(name = "rating")
    private double rating;

    @Column(name = "active_status")
    private boolean activeStatus;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductImage> images = new ArrayList<>();

    public ProductResponse convertToDto() {
        ProductResponse dto = new ProductResponse();
        dto.setId(this.id);
        dto.setCode(this.code);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setCategoryId(this.categoryId);
        dto.setSupplierId(this.supplierId);
        dto.setBrandId(this.brandId);
        dto.setOrigin(this.origin);
        dto.setSlug(this.slug);
        dto.setRating(this.rating);
        dto.setActiveStatus(this.activeStatus);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);

        List<ProductImageResponse> imagesResponses = new ArrayList<>();
        boolean isSetDefaultImage = false;
        if (this.images != null && !this.images.isEmpty()) {
            for (ProductImage productImage : this.images) {
                if (productImage.getVariant() == null) {
                    imagesResponses.add(productImage.convertToDto());
                    if (productImage.isDefaultStatus()) {
                        isSetDefaultImage = true;
                        dto.setDefaultImage(productImage.convertToDto());
                    }
                }
            }
        }

        List<ProductVariantResponse> variantResponses = new ArrayList<>();
        if (this.variants != null && !this.variants.isEmpty()) {
            for (ProductVariant productVariant : this.variants) {
                ProductVariantResponse productVariantResponse = productVariant.convertToDto();
                variantResponses.add(productVariantResponse);
                if (productVariant.isDefaultStatus()) {
                    dto.setDefaultVariant(productVariantResponse);
                    if (!isSetDefaultImage) {
                        for (ProductImage productImage : productVariant.getImages()) {
                            if (productImage.isDefaultStatus()) {
                                isSetDefaultImage = true;
                                dto.setDefaultImage(productImage.convertToDto());
                                break;
                            }
                        }
                    }
                }
            }
        }
        dto.setVariants(variantResponses);

        dto.setImages(imagesResponses);
        return dto;
    }
}
