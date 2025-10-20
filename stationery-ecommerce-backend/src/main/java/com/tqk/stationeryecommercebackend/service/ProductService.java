package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.product.ProductResponse;
import com.tqk.stationeryecommercebackend.model.Product;
import com.tqk.stationeryecommercebackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findByIsActiveTrue();
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (!products.isEmpty()) {
            products.forEach(product -> productResponseList.add(product.convertToDto()));
        }
        return productResponseList;
    }
}
