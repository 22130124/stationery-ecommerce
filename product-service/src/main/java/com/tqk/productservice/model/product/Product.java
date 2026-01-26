package com.tqk.productservice.model.product;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

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

    public enum ProductStatus {
        ACTIVE,
        INACTIVE,
        DELETED;

        @JsonCreator
        public static ProductStatus from(String value) {
            return ProductStatus.valueOf(value.toUpperCase());
        }
    }
}
