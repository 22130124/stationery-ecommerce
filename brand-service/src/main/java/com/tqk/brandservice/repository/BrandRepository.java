package com.tqk.brandservice.repository;

import com.tqk.brandservice.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<List<Brand>> findBySupplierId(Integer supplierId);
}
