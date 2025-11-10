package com.tqk.stationeryecommercebackend.dto.product.responses;

import com.tqk.stationeryecommercebackend.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListResponse {
    private List<ProductResponse> products;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public ProductListResponse(List<Product> products, int currentPage, int totalPages, long totalItems) {
        this.products = convertToDTO(products);
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    private List<ProductResponse> convertToDTO(List<Product> products) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : products) {
            productResponseList.add(product.convertToDto());
        }
        return productResponseList;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
