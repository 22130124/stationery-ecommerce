package com.tqk.categoryservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tqk.categoryservice.dto.response.CategoryResponse;
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
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "status")
    private CategoryStatus status;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    public enum CategoryStatus {
        ACTIVE,
        INACTIVE;

        @JsonCreator
        public static CategoryStatus from(String value) {
            return CategoryStatus.valueOf(value.toUpperCase());
        }
    }

    public CategoryResponse convertToDto() {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(this.id);
        dto.setParentId(this.parent != null ? this.parent.getId() : null);
        dto.setName(this.name);
        dto.setSlug(this.slug);
        dto.setStatus(String.valueOf(this.status));
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);
        List<CategoryResponse> childrenDto = new ArrayList<>();
        this.children.forEach(category -> childrenDto.add(category.convertToDto()));
        dto.setChildren(childrenDto);
        return dto;
    }
}
