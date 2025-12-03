package com.tqk.categoryservice.dto.request;

import com.tqk.categoryservice.model.Category;
import lombok.Data;

@Data
public class AddUpdateRequest {
    private String name;
    private Integer parentId;
    private String slug;
    private boolean activeStatus;
}
