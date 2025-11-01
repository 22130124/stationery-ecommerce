package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.supplier.SupplierResponse;
import com.tqk.stationeryecommercebackend.model.Supplier;
import com.tqk.stationeryecommercebackend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
