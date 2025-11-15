package com.tqk.categoryservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryResponse {
    private Integer id;
    private String name;
    private String slug;
    private boolean activeStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CategoryResponse> children;
}
