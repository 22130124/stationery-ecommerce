package com.tqk.productservice.service;

import com.tqk.productservice.dto.request.ProductImageRequest;
import com.tqk.productservice.dto.request.ProductRequest;
import com.tqk.productservice.dto.request.ProductVariantRequest;
import com.tqk.productservice.dto.request.UpdateInventoryRequest;
import com.tqk.productservice.dto.response.*;
import com.tqk.productservice.exception.ProductNotFoundException;
import com.tqk.productservice.model.*;
import com.tqk.productservice.repository.product.*;
import com.tqk.productservice.repository.client.CategoryClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductVariantColorRepository productVariantColorRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final CategoryClient categoryClient;

    private static final String CODE_PREFIX = "SP";
    private static final int INITIAL_CODE_NUMBER = 1000;

    // Hàm lấy ra danh sách sản phẩm đang hoạt động (activeStatus = true)
    public List<ProductResponse> getAllForAdmin() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (!products.isEmpty()) {
            for (Product product : products) {
                ProductResponse productResponse = product.convertToDto();
                int totalStock = 0;
                for (ProductVariant variant : product.getVariants()) {
                    totalStock += variant.getProductInventory().getStock();
                }
                productResponse.setTotalStock(totalStock);
                productResponseList.add(productResponse);
            }
        }
        return productResponseList;
    }

    // Hàm lấy ra danh sách sản phẩm theo danh mục (có phân trang)
    public ProductListResponse getByActiveStatusAndCategory(String categorySlug, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Product> productPage;
            if (categorySlug.equalsIgnoreCase("ALL")) {
                return getProductsByActiveStatusAndPageable(page, size);
            }
            Integer categoryId = categoryClient.getCategoryIdBySlug(categorySlug);

            productPage = productRepository.findByCategoryIdAndActiveStatusTrue(categoryId, pageable);

            ProductListResponse productListResponse = new ProductListResponse();
            List<ProductResponse> productResponses = productPage.getContent().stream().map(Product::convertToDto).toList();
            productListResponse.setProducts(productResponses);
            productListResponse.setTotalPages(productPage.getTotalPages());
            productListResponse.setTotalItems(productPage.getTotalElements());
            productListResponse.setCurrentPage(productPage.getNumber() + 1);

            return productListResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ProductListResponse getProductsByActiveStatusAndPageable(int page, int size) {
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

    public List<ProductResponse> getProductsByActiveStatus() {
        List<Product> products = productRepository.findByActiveStatusTrue();

        List<ProductResponse> productResponses = products.stream().map(Product::convertToDto).toList();

        return productResponses;
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

            // Lưu màu sắc của variant
            if (variantRequest.getColors() != null && !variantRequest.getColors().isEmpty()) {
                for (String color : variantRequest.getColors()) {
                    ProductVariantColor productVariantColor = new ProductVariantColor();
                    productVariantColor.setProduct(savedProduct);
                    productVariantColor.setVariant(savedVariant);
                    productVariantColor.setColor(color);
                    productVariantColorRepository.save(productVariantColor);
                }
            }

            savedVariants.add(savedVariant);
        }

        // Lưu Product Images (Ảnh chung)
        if (productRequest.getImages() != null && !productRequest.getImages().

                isEmpty()) {
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

        // Xóa biến thể cũ không còn trong request
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

                // Xóa các thông tin về màu sắc
                List<ProductVariantColor> oldColors = productVariantColorRepository.findByVariant(existingVariant);
                productVariantColorRepository.deleteAll(oldColors);

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

            // Xử lý màu sắc
            if (variantRequest.getColors() != null && !variantRequest.getColors().isEmpty()) {
                List<ProductVariantColor> oldColors = productVariantColorRepository.findByVariant(savedVariant);
                productVariantColorRepository.deleteAll(oldColors);
                for (String color : variantRequest.getColors()) {
                    ProductVariantColor productVariantColor = new ProductVariantColor();
                    productVariantColor.setProduct(updatedProduct);
                    productVariantColor.setVariant(savedVariant);
                    productVariantColor.setColor(color);
                    productVariantColorRepository.save(productVariantColor);
                }
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

    @Transactional
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm với id: " + productId));

        // Xóa tất cả biến thể, ảnh, màu sắc liên quan
        List<ProductVariant> variants = productVariantRepository.findByProduct(product);
        for (ProductVariant variant : variants) {
            List<ProductImage> variantImages = productImageRepository.findByVariant(variant);
            productImageRepository.deleteAll(variantImages);
            List<ProductVariantColor> variantColors = productVariantColorRepository.findByVariant(variant);
            productVariantColorRepository.deleteAll(variantColors);
        }
        productVariantRepository.deleteAll(variants);

        // Xóa sản phẩm
        productRepository.delete(product);
    }

    public List<ProductResponse> getProductsByVariantIds(List<Integer> variantIds) {
        List<ProductResponse> products = new ArrayList<>();
        Product product;
        ProductVariant variant;
        for (Integer variantId : variantIds) {
            variant = productVariantRepository.findById(variantId).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm có id biến thể là: " + variantId));
            product = variant.getProduct();
            products.add(product.convertToDtoWithSpecificVariant(variant.convertToDto()));
        }
        return products;
    }

    public Integer getCategoryIdBySlug(String slug) {
        return categoryClient.getCategoryIdBySlug(slug);
    }

    // Hàm truy vấn sản phẩm phục vụ cho tính năng chatbot
    public List<ProductResponse> searchProducts(Integer categoryId, Integer brandId, List<String> colors, Integer minPrice, Integer maxPrice, List<String> extra) {
        int hasColors = (colors != null && !colors.isEmpty()) ? 1 : 0;
        List<Product> products = productRepository.searchProductsWithScore(categoryId, brandId, colors, minPrice, maxPrice, extra, hasColors);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(product.convertToDto());
        }
        return productResponses;
    }

    public int getStock(Integer variantId) {
        ProductVariant productVariant = productVariantRepository.findById(variantId).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm có id biến thể là: " + variantId));
        ProductInventory productInventory = productInventoryRepository.findByProductVariant(productVariant);
        return productInventory.getStock();
    }

    public int updateInventory(String type, UpdateInventoryRequest request) {
        ProductVariant productVariant = productVariantRepository.findById(request.getVariantId()).orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm có id biến thể là: " + request.getVariantId()));
        ProductInventory productInventory = productInventoryRepository.findByProductVariant(productVariant);
        if (type.equalsIgnoreCase("replace")) {
            productInventory.setStock(request.getQuantity());
        } else if (type.equalsIgnoreCase("increase")) {
            productInventory.setStock(productInventory.getStock() + request.getQuantity());
        } else if (type.equalsIgnoreCase("decrease")) {
            productInventory.setStock(productInventory.getStock() - request.getQuantity());
        }
        productInventoryRepository.save(productInventory);
        return productInventory.getStock();
    }
}
