package com.tqk.brandservice.service;

import com.tqk.brandservice.dto.request.AddUpdateRequest;
import com.tqk.brandservice.dto.response.SupplierResponse;
import com.tqk.brandservice.exception.SupplierNotFoundException;
import com.tqk.brandservice.model.Supplier;
import com.tqk.brandservice.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {
    private SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }


    public List<SupplierResponse> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierResponse> supplierResponseList = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            supplierResponseList.add(supplier.convertToDto());
        }
        return supplierResponseList;
    }

    public SupplierResponse getById(Integer id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new SupplierNotFoundException("Không tìm thấy nhà cung cấp với id: " + id));
        return supplier.convertToDto();
    }

    public SupplierResponse updateSupplier(Integer id, AddUpdateRequest request) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new SupplierNotFoundException("Không tìm thấy nhà cung cấp với id: " + id));
        supplier.setName(request.getName());
        supplier.setActiveStatus(request.isActiveStatus());
        supplierRepository.save(supplier);
        return supplier.convertToDto();
    }

    public SupplierResponse addSupplier(AddUpdateRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setActiveStatus(request.isActiveStatus());
        supplierRepository.save(supplier);
        return supplier.convertToDto();
    }

    public Integer deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
        return id;
    }
}
