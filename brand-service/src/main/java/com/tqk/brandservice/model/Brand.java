package com.tqk.brandservice.model;

import com.tqk.brandservice.dto.response.BrandResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "brands")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "active_status")
    private boolean activeStatus;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public BrandResponse convertToDto() {
        BrandResponse dto = new BrandResponse();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setActiveStatus(this.activeStatus);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);
        return dto;
    }
}
