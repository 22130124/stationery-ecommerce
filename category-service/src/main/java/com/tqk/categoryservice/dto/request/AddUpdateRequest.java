package com.tqk.categoryservice.dto.request;

import lombok.Data;

@Data
public class AddUpdateRequest {
    private String name;
    private Integer parentId;
    private String slug;
    private String status;
}
