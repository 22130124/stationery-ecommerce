package com.tqk.brandservice.service;

import com.tqk.brandservice.dto.response.BrandResponse;
import com.tqk.brandservice.exception.BrandNotFoundException;
import com.tqk.brandservice.model.Brand;
import com.tqk.brandservice.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandResponse> getBySupplierId(Integer supplierId) {
        List<Brand> brands = brandRepository.findBySupplierId(supplierId).orElseThrow(() -> new BrandNotFoundException("Không tìm thấy thương hiệu của nhà cung cấp có id: " + supplierId));
        List<BrandResponse> brandResponses = new ArrayList<>();
        for (Brand brand : brands) {
            brandResponses.add(brand.convertToDto());
        }
        return brandResponses;
    }

    public BrandResponse getById(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new BrandNotFoundException("Không tìm thấy thương hiệu với id: " + id));
        return brand.convertToDto();
    }
}
