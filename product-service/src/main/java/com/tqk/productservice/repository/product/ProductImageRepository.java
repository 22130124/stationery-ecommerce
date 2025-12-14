package com.tqk.productservice.repository.product;

import com.tqk.productservice.dto.response.ProductImageResponse;
import com.tqk.productservice.model.Product;
import com.tqk.productservice.model.ProductImage;
import com.tqk.productservice.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByVariant(ProductVariant savedVariant);

    List<ProductImage> findByProductAndVariantIsNull(Product updatedProduct);

    ProductImage findByVariantAndDefaultStatusTrue(ProductVariant variant);
}
