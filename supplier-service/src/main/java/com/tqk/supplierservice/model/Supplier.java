package com.tqk.supplierservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tqk.supplierservice.dto.response.SupplierResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SupplierStatus status;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @OneToMany(mappedBy = "supplier")
    private List<Brand> brands = new ArrayList<>();

    public enum SupplierStatus {
        ACTIVE,
        INACTIVE;

        @JsonCreator
        public static SupplierStatus from(String value) {
            return SupplierStatus.valueOf(value.toUpperCase());
        }
    }
}
