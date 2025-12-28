package com.tqk.productservice.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductListResponse {
    private List<ProductResponse> products = new ArrayList<>();
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
