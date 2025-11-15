package com.tqk.productservice.model;

import com.tqk.productservice.dto.response.ProductImageResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_images")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(name = "url")
    private String url;

    @Column(name = "defaultStatus")
    private boolean defaultStatus;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ProductImageResponse convertToDto() {
        ProductImageResponse dto = new ProductImageResponse();
        dto.setId(this.id);
        dto.setUrl(this.url);
        dto.setDefaultStatus(this.defaultStatus);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);
        return dto;
    }
}
