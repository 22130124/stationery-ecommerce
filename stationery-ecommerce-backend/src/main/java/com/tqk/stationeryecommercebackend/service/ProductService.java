package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.product.requests.ProductImageRequest;
import com.tqk.stationeryecommercebackend.dto.product.requests.ProductRequest;
import com.tqk.stationeryecommercebackend.dto.product.requests.ProductVariantRequest;
import com.tqk.stationeryecommercebackend.dto.product.responses.ProductListResponse;
import com.tqk.stationeryecommercebackend.dto.product.responses.ProductResponse;
import com.tqk.stationeryecommercebackend.exception.ProductNotFoundException;
import com.tqk.stationeryecommercebackend.model.*;
import com.tqk.stationeryecommercebackend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;
    private final SupplierRepository  supplierRepository;
    private final BrandRepository brandRepository;

    private static final String CODE_PREFIX = "SP";
    private static final int INITIAL_CODE_NUMBER = 1000;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductVariantRepository productVariantRepository, ProductImageRepository productImageRepository, SupplierRepository supplierRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.productImageRepository = productImageRepository;
        this.supplierRepository = supplierRepository;
        this.brandRepository = brandRepository;
    }

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findByIsActiveTrue();
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (!products.isEmpty()) {
            products.forEach(product -> productResponseList.add(product.convertToDto()));
        }
        return productResponseList;
    }

    public ProductListResponse getProductsByCategoryAndPagination(String categorySlug, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage;
        if(categorySlug.equalsIgnoreCase("all")) {
            productPage = productRepository.findAll(pageable);
        } else {
            Category category = categoryRepository.findBySlug(categorySlug);
            productPage = productRepository.findByCategory(category, pageable);
        }

        return new ProductListResponse(
                productPage.getContent(),
                productPage.getNumber() + 1,
                productPage.getTotalPages(),
                productPage.getTotalElements()
        );
    }

    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với slug: " + slug));
        return product.convertToDto();
    }

    public List<ProductVariant> getProductVariantsByIds(List<Integer> variantIds) {
        List<ProductVariant> variants = new ArrayList<>();
        for(Integer id : variantIds) {
            ProductVariant variant = productVariantRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy biến thể với id: " + id));
            variants.add(variant);
        }
        return variants;
    }

    @Transactional
    public ProductResponse saveProduct(ProductRequest productRequest) {
        // 1. Tìm các Entity tham chiếu
        Category category = categoryRepository.findById(productRequest.getCategoryId().intValue())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Supplier supplier = supplierRepository.findById(productRequest.getSupplierId().intValue())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        Brand brand = brandRepository.findById(productRequest.getBrandId().intValue())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        // 2. Tạo và lưu Product Entity
        Product product = new Product();
        product.setCode(generateProductCode());
        product.setName(productRequest.getName());
        product.setSlug(productRequest.getSlug());
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setBrand(brand);
        product.setIsActive(true);
        product.setRating(0);

        Product savedProduct = productRepository.save(product);

        // 3. Lưu Product Variants
        List<ProductVariant> savedVariants = new ArrayList<>();

        for (ProductVariantRequest variantRequest : productRequest.getVariants()) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(savedProduct);
            variant.setName(variantRequest.getName());
            variant.setBasePrice(variantRequest.getBasePrice());
            variant.setDiscountPrice(variantRequest.getDiscountPrice());
            variant.setIsActive(variantRequest.getIsActive());
            variant.setIsDefault(variantRequest.getIsDefault());

            ProductVariant savedVariant = productVariantRepository.save(variant);

            // 4. Lưu Product Images cho Variant
            if (variantRequest.getImages() != null && !variantRequest.getImages().isEmpty()) {
                saveProductImages(variantRequest.getImages(), savedProduct, savedVariant);
            }

            savedVariants.add(savedVariant);
        }

        // 5. Lưu Product Images (Ảnh chung)
        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            productRequest.getImages().getFirst().setIsDefault(true);
            saveProductImages(productRequest.getImages(), savedProduct, null);
        }

        // Cập nhật variants
        savedProduct.setVariants(savedVariants);

        // 6. Trả về DTO
        return savedProduct.convertToDto();
    }

    // Hàm hỗ trợ lưu ảnh
    private void saveProductImages(List<ProductImageRequest> images, Product product, ProductVariant variant) {
        images.forEach(imageRequest -> {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setVariant(variant);
            productImage.setUrl(imageRequest.getUrl());
            productImage.setIsDefault(imageRequest.getIsDefault() != null ? imageRequest.getIsDefault() : false);
            productImageRepository.save(productImage);
        });
    }

    private String generateProductCode() {
        Product latestProduct = productRepository.findTopByOrderByIdDesc();

        int nextNumber = INITIAL_CODE_NUMBER + 1;

        if (latestProduct != null && latestProduct.getCode() != null) {
            String latestCode = latestProduct.getCode();
            if (latestCode.startsWith(CODE_PREFIX)) {
                try {
                    // Lấy phần số của mã code
                    String numberPart = latestCode.substring(CODE_PREFIX.length());
                    int lastNumber = Integer.parseInt(numberPart);
                    nextNumber = lastNumber + 1;
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Invalid product code");
                }
            }
        }

        // Định dạng lại thành chuỗi code sản phẩm
        return CODE_PREFIX + nextNumber;
    }
}
