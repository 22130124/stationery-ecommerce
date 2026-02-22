package com.tqk.supplierservice.service;

import com.tqk.supplierservice.dto.request.AddUpdateBrandRequest;
import com.tqk.supplierservice.dto.response.BrandResponse;
import com.tqk.supplierservice.exception.ExceptionCode;
import com.tqk.supplierservice.model.Brand;
import com.tqk.supplierservice.model.Supplier;
import com.tqk.supplierservice.repository.BrandRepository;
import com.tqk.supplierservice.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.tqk.supplierservice.model.Brand.BrandStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final SupplierRepository supplierRepository;

    public List<BrandResponse> getBySupplierId(Integer supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.SUPPLIER_NOT_FOUND.name()));
        return convertToDto(supplier.getBrands());
    }

    public BrandResponse getById(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ExceptionCode.BRAND_NOT_FOUND.name()));
        return convertToDto(brand);
    }

    @Transactional
    public BrandResponse updateBrand(Integer id, AddUpdateBrandRequest request) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ExceptionCode.BRAND_NOT_FOUND.name()));
        brand.setName(request.getName());
        brand.setStatus(Brand.BrandStatus.valueOf(request.getStatus()));
        brandRepository.save(brand);
        return convertToDto(brand);
    }

    @Transactional
    public BrandResponse createBrand(AddUpdateBrandRequest request) {
        Brand brand = new Brand();
        if (request.getName().isBlank()) {
            return null;
        }
        // Tìm supplier tương ứng
        Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ExceptionCode.SUPPLIER_NOT_FOUND.name()));
        brand.setSupplier(supplier);
        brand.setName(request.getName());
        brand.setStatus(Brand.BrandStatus.valueOf(request.getStatus()));
        brandRepository.save(brand);
        return convertToDto(brand);
    }

    @Transactional
    public Integer deleteBrand(Integer id) {
        brandRepository.deleteById(id);
        return id;
    }

    public List<BrandResponse> getActiveBrands() {
        List<Brand> brands = brandRepository.findByStatus(ACTIVE);
        List<BrandResponse> brandResponses = new ArrayList<>();
        for (Brand brand : brands) {
            brandResponses.add(convertToDto(brand));
        }
        return brandResponses;
    }

    public String getNameById(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ExceptionCode.BRAND_NOT_FOUND.name()));
        return brand.getName();
    }

    public BrandResponse convertToDto(Brand brand) {
        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setName(brand.getName());
        response.setStatus(String.valueOf(brand.getStatus()));
        response.setCreatedAt(brand.getCreatedAt());
        response.setUpdatedAt(brand.getUpdatedAt());
        return response;
    }

    public List<BrandResponse> convertToDto(List<Brand> brands) {
        List<BrandResponse> response = new ArrayList<>();
        for (Brand brand : brands) {
            response.add(convertToDto(brand));
        }
        return response;
    }
}
