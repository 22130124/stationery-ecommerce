package com.tqk.categoryservice.dto.response;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryResponse {
    private Integer id;
    private Integer parentId;
    private String name;
    private String slug;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private List<CategoryResponse> children;
}
