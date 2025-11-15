package com.tqk.categoryservice.model;

import com.tqk.categoryservice.dto.response.CategoryResponse;
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

    @Column(name = "active_status")
    private boolean activeStatus;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    public CategoryResponse convertToDto() {
        CategoryResponse dto = new CategoryResponse();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setSlug(this.slug);
        dto.setActiveStatus(this.activeStatus);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);
        List<CategoryResponse> childrenDto = new ArrayList<>();
        this.children.forEach(category -> childrenDto.add(category.convertToDto()));
        dto.setChildren(childrenDto);
        return dto;
    }
}
