package com.tqk.brandservice.repository;

import com.tqk.brandservice.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
}
