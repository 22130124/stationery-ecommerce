package com.tqk.supplierservice.repository;

import com.tqk.supplierservice.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    List<Brand> findByStatus(Brand.BrandStatus status);
}
