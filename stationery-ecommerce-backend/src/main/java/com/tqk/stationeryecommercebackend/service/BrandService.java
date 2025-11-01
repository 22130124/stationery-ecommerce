package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.brand.BrandResponse;
import com.tqk.stationeryecommercebackend.exception.ProductNotFoundException;
import com.tqk.stationeryecommercebackend.exception.SupplierNotFoundException;
import com.tqk.stationeryecommercebackend.model.Brand;
import com.tqk.stationeryecommercebackend.model.ProductVariant;
import com.tqk.stationeryecommercebackend.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BrandService {
    private BrandRepository brandRepository;
    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandResponse> getBrandsBySupplierId(Integer supplierId) {
        List<Brand> brands = brandRepository.findBySupplierId(supplierId).orElseThrow(() -> new SupplierNotFoundException("Không tìm thấy nhà cung cấp với id: " + supplierId));
        List<BrandResponse> brandResponses = new ArrayList<>();
        if (!brands.isEmpty()) {
            for (Brand brand : brands) {
                brandResponses.add(brand.convertToDto());
            }
        }
        return brandResponses;
    }
}
