package com.tqk.productservice.service;

import com.tqk.productservice.dto.request.ProductImageRequest;
import com.tqk.productservice.dto.request.ProductRequest;
import com.tqk.productservice.dto.request.ProductVariantRequest;
import com.tqk.productservice.dto.response.*;
import com.tqk.productservice.exception.ProductNotFoundException;
import com.tqk.productservice.model.Product;
import com.tqk.productservice.model.ProductImage;
import com.tqk.productservice.model.ProductVariant;
import com.tqk.productservice.repository.product.ProductImageRepository;
import com.tqk.productservice.repository.product.ProductRepository;
import com.tqk.productservice.repository.product.ProductVariantRepository;
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
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;

    private static final String CODE_PREFIX = "SP";
    private static final int INITIAL_CODE_NUMBER = 1000;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductVariantRepository productVariantRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
        this.productImageRepository = productImageRepository;
    }

    // Hàm lấy ra danh sách sản phẩm đang hoạt động (activeStatus = true)
    public List<ProductResponse> getAllForAdmin() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (!products.isEmpty()) {
            products.forEach(product -> productResponseList.add(product.convertToDto()));
        }
        return productResponseList;
    }

    // Hàm lấy ra danh sách sản phẩm theo danh mục (có phân trang)
    public ProductListResponse getByCategory(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage;
        productPage = productRepository.findByCategoryIdAndActiveStatusTrue(categoryId, pageable);

        ProductListResponse productListResponse = new ProductListResponse();
        List<ProductResponse> productResponses = productPage.getContent().stream().map(Product::convertToDto).toList();
        productListResponse.setProducts(productResponses);
        productListResponse.setTotalPages(productPage.getTotalPages());
        productListResponse.setTotalItems(productPage.getTotalElements());
        productListResponse.setCurrentPage(productPage.getNumber() + 1);

        return productListResponse;
    }

    public ProductListResponse getAllForUser(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage;
        productPage = productRepository.findByActiveStatusTrue(pageable);

        ProductListResponse productListResponse = new ProductListResponse();
        List<ProductResponse> productResponses = productPage.getContent().stream().map(Product::convertToDto).toList();
        productListResponse.setProducts(productResponses);
        productListResponse.setTotalPages(productPage.getTotalPages());
        productListResponse.setTotalItems(productPage.getTotalElements());
        productListResponse.setCurrentPage(productPage.getNumber() + 1);

        return productListResponse;
    }

    // Hàm lấy ra sản phẩm theo slug
    public ProductResponse getBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với slug: " + slug));
        return product.convertToDto();
    }

    // Hàm tạo sản phẩm mới
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        // Tạo và lưu Product Entity
        Product product = new Product();
        product.setCode(generateProductCode());
        product.setName(productRequest.getName());
        product.setSlug(productRequest.getSlug());
        product.setCategoryId(productRequest.getCategoryId());
        product.setSupplierId(productRequest.getSupplierId());
        product.setBrandId(productRequest.getBrandId());
        product.setActiveStatus(true);
        product.setRating(0);
        product.setOrigin(productRequest.getOrigin());
        product.setDescription(productRequest.getDescription());

        Product savedProduct = productRepository.save(product);

        // Lưu Product Variants
        List<ProductVariant> savedVariants = new ArrayList<>();

        for (ProductVariantRequest variantRequest : productRequest.getVariants()) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(savedProduct);
            variant.setName(variantRequest.getName());
            variant.setBasePrice(variantRequest.getBasePrice());
            variant.setDiscountPrice(variantRequest.getDiscountPrice());
            variant.setActiveStatus(variantRequest.getActiveStatus());
            variant.setDefaultStatus(variantRequest.getDefaultStatus());

            ProductVariant savedVariant = productVariantRepository.save(variant);

            // Lưu Product Images cho Variant
            if (variantRequest.getImages() != null && !variantRequest.getImages().isEmpty()) {
                saveProductImages(variantRequest.getImages(), savedProduct, savedVariant);
            }

            savedVariants.add(savedVariant);
        }

        // Lưu Product Images (Ảnh chung)
        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            saveProductImages(productRequest.getImages(), savedProduct, null);
        }

        // Cập nhật variants
        savedProduct.setVariants(savedVariants);

        // Trả về DTO
        return savedProduct.convertToDto();
    }

    // Hàm cập nhật thông tin mới cho sản phẩm
    @Transactional
    public ProductResponse updateProduct(Integer productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id: " + productId));

        // Cập nhật thông tin chung
        product.setName(productRequest.getName());
        product.setSlug(productRequest.getSlug());
        product.setCategoryId(productRequest.getCategoryId());
        product.setSupplierId(productRequest.getSupplierId());
        product.setBrandId(productRequest.getBrandId());
        product.setOrigin(productRequest.getOrigin());
        product.setDescription(productRequest.getDescription());

        Product updatedProduct = productRepository.save(product);

        // Xóa biến thể cũ không còn trong request ---
        List<Integer> requestVariantIds = new ArrayList<>();
        for (ProductVariantRequest vr : productRequest.getVariants()) {
            if (vr.getId() != null) requestVariantIds.add(vr.getId());
        }

        List<ProductVariant> existingVariants = productVariantRepository.findByProduct(updatedProduct);
        for (ProductVariant existingVariant : existingVariants) {
            if (!requestVariantIds.contains(existingVariant.getId())) {
                // Xóa ảnh liên quan trước
                List<ProductImage> oldImages = productImageRepository.findByVariant(existingVariant);
                productImageRepository.deleteAll(oldImages);

                productVariantRepository.delete(existingVariant);
            }
        }

        // Update biến thể cũ hoặc tạo biến thể mới nếu chưa có
        List<ProductVariant> updatedVariants = new ArrayList<>();
        for (ProductVariantRequest variantRequest : productRequest.getVariants()) {
            ProductVariant variant;
            if (variantRequest.getId() != null) {
                variant = productVariantRepository.findById(variantRequest.getId())
                        .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy biến thể với id: " + variantRequest.getId()));
            } else {
                variant = new ProductVariant();
                variant.setProduct(updatedProduct);
            }

            variant.setName(variantRequest.getName());
            variant.setBasePrice(variantRequest.getBasePrice());
            variant.setDiscountPrice(variantRequest.getDiscountPrice());
            variant.setActiveStatus(variantRequest.getActiveStatus());
            variant.setDefaultStatus(variantRequest.getDefaultStatus());

            ProductVariant savedVariant = productVariantRepository.save(variant);

            // Xử lý ảnh
            if (variantRequest.getImages() != null && !variantRequest.getImages().isEmpty()) {
                List<ProductImage> oldImages = productImageRepository.findByVariant(savedVariant);
                productImageRepository.deleteAll(oldImages);
                saveProductImages(variantRequest.getImages(), updatedProduct, savedVariant);
            }

            updatedVariants.add(savedVariant);
        }

        updatedProduct.setVariants(updatedVariants);

        // Cập nhật ảnh chung
        if (productRequest.getImages() != null && !productRequest.getImages().isEmpty()) {
            List<ProductImage> oldCommonImages = productImageRepository.findByProductAndVariantIsNull(updatedProduct);
            productImageRepository.deleteAll(oldCommonImages);
            saveProductImages(productRequest.getImages(), updatedProduct, null);
        }

        return updatedProduct.convertToDto();
    }

    // Hàm hỗ trợ lưu ảnh
    private void saveProductImages(List<ProductImageRequest> images, Product product, ProductVariant variant) {
        List<ProductImage> savedImages = new ArrayList<>();
        for (ProductImageRequest imageRequest : images) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setVariant(variant);
            productImage.setUrl(imageRequest.getUrl());
            productImage.setDefaultStatus(imageRequest.getDefaultStatus());
            ProductImage savedImage = productImageRepository.save(productImage);
            savedImages.add(savedImage);
        }

        if (variant != null) {
            variant.setImages(savedImages);
        } else {
            product.setImages(savedImages);
        }
    }

    // Hàm tạo mã sản phẩm
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
