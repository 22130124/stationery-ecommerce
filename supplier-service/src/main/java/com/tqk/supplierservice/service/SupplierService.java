package com.tqk.supplierservice.service;

import com.tqk.supplierservice.dto.request.AddUpdateSupplierRequest;
import com.tqk.supplierservice.dto.response.SupplierResponse;
import com.tqk.supplierservice.exception.ExceptionCode;
import com.tqk.supplierservice.model.Supplier;
import com.tqk.supplierservice.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final BrandService brandService;

    public List<SupplierResponse> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierResponse> supplierResponseList = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            supplierResponseList.add(convertToDto(supplier));
        }
        return supplierResponseList;
    }

    public SupplierResponse getById(Integer id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ExceptionCode.SUPPLIER_NOT_FOUND.name()));
        return convertToDto(supplier);
    }

    @Transactional
    public SupplierResponse updateSupplier(Integer id, AddUpdateSupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ExceptionCode.SUPPLIER_NOT_FOUND.name()));
        supplier.setName(request.getName());
        supplier.setStatus(Supplier.SupplierStatus.valueOf(request.getStatus()));
        supplierRepository.save(supplier);
        return convertToDto(supplier);
    }

    @Transactional
    public SupplierResponse createSupplier(AddUpdateSupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setStatus(Supplier.SupplierStatus.valueOf(request.getStatus()));
        supplierRepository.save(supplier);
        return convertToDto(supplier);
    }

    @Transactional
    public Integer deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
        return id;
    }

    public SupplierResponse convertToDto(Supplier supplier) {
        SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setName(supplier.getName());
        response.setStatus(String.valueOf(supplier.getStatus()));
        response.setCreatedAt(supplier.getCreatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());
        response.setBrands(brandService.convertToDto(supplier.getBrands()));
        return response;
    }
}
