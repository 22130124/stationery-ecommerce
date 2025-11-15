package com.tqk.productservice.dto.response;

import com.tqk.productservice.model.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductListResponse {
    private List<ProductResponse> products = new ArrayList<>();
    private int currentPage;
    private int totalPages;
    private long totalItems;

    private List<ProductResponse> convertToDTO(List<Product> products) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : products) {
            productResponseList.add(product.convertToDto());
        }
        return productResponseList;
    }
}
