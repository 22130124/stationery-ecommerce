package com.tqk.productservice.model.product;

import com.tqk.productservice.model.inventory.Inventory;
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
@Table(name = "product_variants")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "name")
    private String name;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(name = "active_status")
    private boolean activeStatus;

    @Column(name = "default_status")
    private boolean defaultStatus;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "variant")
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "variant")
    private List<ProductVariantColor> colors = new ArrayList<>();

    @OneToOne(mappedBy = "productVariant")
    private Inventory inventory;
}
