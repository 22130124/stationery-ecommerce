package com.tqk.productservice.service;

import com.tqk.productservice.dto.request.ProductImageRequest;
import com.tqk.productservice.dto.request.ProductRequest;
import com.tqk.productservice.dto.request.ProductVariantRequest;
import com.tqk.productservice.dto.request.UpdateInventoryRequest;
import com.tqk.productservice.dto.response.*;
import com.tqk.productservice.dto.response.brand.BrandResponse;
import com.tqk.productservice.dto.response.category.CategoryResponse;
import com.tqk.productservice.dto.response.supplier.SupplierResponse;
import com.tqk.productservice.exception.ExceptionCode;
import com.tqk.productservice.model.inventory.Inventory;
import com.tqk.productservice.model.product.Product;
import com.tqk.productservice.model.product.ProductImage;
import com.tqk.productservice.model.product.ProductVariant;
import com.tqk.productservice.model.product.ProductVariantColor;
import com.tqk.productservice.repository.client.BrandClient;
import com.tqk.productservice.repository.client.CategoryClient;
import com.tqk.productservice.repository.client.SupplierClient;
import com.tqk.productservice.repository.inventory.InventoryRepository;
import com.tqk.productservice.repository.product.ProductImageRepository;
import com.tqk.productservice.repository.product.ProductRepository;
import com.tqk.productservice.repository.product.ProductVariantColorRepository;
import com.tqk.productservice.repository.product.ProductVariantRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.tqk.productservice.model.product.Product.ProductStatus.ACTIVE;
import static com.tqk.productservice.model.product.Product.ProductStatus.DELETED;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductVariantColorRepository productVariantColorRepository;
    private final InventoryRepository productInventoryRepository;
    private final CategoryClient categoryClient;
    private final SupplierClient supplierClient;
    private final BrandClient brandClient;
    private final EntityManager entityManager;

    private static final String CODE_PREFIX = "SP";
    private static final int INITIAL_CODE_NUMBER = 1000;
    private final InventoryService inventoryService;

    // Hàm lấy ra danh sách sản phẩm đang hoạt động (activeStatus = true)
    public List<ProductResponse> getAllForAdmin() {
        List<Product> products = productRepository.findByStatusNot(DELETED);
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (!products.isEmpty()) {
            productResponseList = convertProductListToDto(products);
        }
        return productResponseList;
    }

    // Hàm lấy ra danh sách sản phẩm theo danh mục (có phân trang)
    public ProductListResponse getByActiveStatusAndCategory(String categorySlug, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Product> productPage;
            if (categorySlug.equalsIgnoreCase("all")) {
                return getProductsByStatusAndPageable(ACTIVE, page, size);
            }
            Integer categoryId = categoryClient.getCategoryIdBySlug(categorySlug);

            productPage = productRepository.findByCategoryIdAndStatusNot(categoryId, DELETED, pageable);

            ProductListResponse productListResponse = new ProductListResponse();

            productListResponse.setProducts(convertProductListToDto(productPage.getContent()));
            productListResponse.setTotalPages(productPage.getTotalPages());
            productListResponse.setTotalItems(productPage.getTotalElements());
            productListResponse.setCurrentPage(productPage.getNumber() + 1);

            return productListResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ProductListResponse getProductsByStatusAndPageable(Product.ProductStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage;
        productPage = productRepository.findByStatus(status, pageable);

        ProductListResponse productListResponse = new ProductListResponse();
        productListResponse.setProducts(convertProductListToDto(productPage.getContent()));
        productListResponse.setTotalPages(productPage.getTotalPages());
        productListResponse.setTotalItems(productPage.getTotalElements());
        productListResponse.setCurrentPage(productPage.getNumber() + 1);

        return productListResponse;
    }

    // Hàm lấy ra sản phẩm theo slug
    public ProductResponse getBySlug(String slug) {
        Product product = productRepository.findBySlugAndStatusNot(slug, DELETED).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.PRODUCT_NOT_FOUND.name()));
        return convertProductToDto(product, null);
    }

    /**
     * Hàm lấy ra danh sách các sản phẩm đang hoạt động (status = ACTIVE)
     * Không phân trang
     * Mục đích: Sử dụng cho chức năng gợi ý sản phẩm
     */
    public List<ProductResponse> getActiveProducts() {
        List<Product> products = productRepository.findByStatus(ACTIVE);
        return convertProductListToDto(products);
    }

    // Hàm tạo sản phẩm mới
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Tạo và lưu Product Entity
        Product product = new Product();
        product.setCode(generateProductCode());
        product.setName(request.getName());
        // Kiểm tra xem có trùng slug hay không
        if (productRepository.existsBySlugAndStatusNot(request.getSlug(), DELETED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.SLUG_ALREADY_EXISTS.name());
        }
        product.setSlug(request.getSlug());
        product.setCategoryId(request.getCategoryId());
        product.setSupplierId(request.getSupplierId());
        product.setBrandId(request.getBrandId());
        product.setStatus(request.getStatus() == null ? ACTIVE : request.getStatus());
        product.setRating(0);
        product.setOrigin(request.getOrigin());
        product.setDescription(request.getDescription());

        Product savedProduct = productRepository.save(product);

        // Lưu Product Variants
        List<ProductVariant> savedVariants = new ArrayList<>();

        for (ProductVariantRequest variantRequest : request.getVariants()) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(savedProduct);
            variant.setName(variantRequest.getName());
            variant.setBasePrice(variantRequest.getBasePrice());
            variant.setDiscountPrice(variantRequest.getDiscountPrice());
            variant.setActiveStatus(variantRequest.getActiveStatus());
            variant.setDefaultStatus(variantRequest.getDefaultStatus());

            ProductVariant savedVariant = productVariantRepository.save(variant);

            // Lưu Product Images cho Variant
            // Kiểm tra nếu request thiếu ảnh biến thể
            if (variantRequest.getImages() == null || variantRequest.getImages().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.MISSING_VARIANT_IMAGE.name());
            }

            saveProductImages(variantRequest.getImages(), savedProduct, savedVariant);

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

            // Tạo dữ liệu trong kho cho biến thể
            inventoryService.addNewVariant(savedVariant);
        }

        // Cập nhật variants
        savedProduct.setVariants(savedVariants);

        // Trả về DTO
        return convertProductToDto(savedProduct, null);
    }

    // Hàm cập nhật thông tin mới cho sản phẩm
    @Transactional
    public ProductResponse updateProduct(Integer productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.PRODUCT_NOT_FOUND.name()));

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
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.VARIANT_NOT_FOUND.name()));
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
            // Kiểm tra nếu request thiếu ảnh biến thể
            if (variantRequest.getImages() == null || variantRequest.getImages().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.MISSING_VARIANT_IMAGE.name());
            }

            List<ProductImage> oldImages = productImageRepository.findByVariant(savedVariant);
            productImageRepository.deleteAll(oldImages);
            saveProductImages(variantRequest.getImages(), updatedProduct, savedVariant);

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

        return convertProductToDto(updatedProduct, null);
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
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionCode.INVALID_PRODUCT_CODE.name());
                }
            }
        }

        // Định dạng lại thành chuỗi code sản phẩm
        return CODE_PREFIX + nextNumber;
    }

    /**
     * Phuơng thức xử lý yêu cầu xóa sản phẩm
     * Chỉ gán status là DELETED, không xóa hoàn toàn ra khỏi cơ sở dữ liệu
     */
    @Transactional
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.PRODUCT_NOT_FOUND.name()));

        product.setStatus(DELETED);
        productRepository.save(product);
    }

    // Phương thức lấy ra danh sách sản phẩm dựa vào danh sách id sản phẩm cho trước
    public List<ProductResponse> getProductsByIds(List<Integer> ids) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        Product product;
        for (Integer id : ids) {
            product = productRepository.findById(id).orElse(null);
            if (product != null) {
                ProductResponse productResponse = convertProductToDto(product, null);
                productResponseList.add(productResponse);
            }
        }
        return productResponseList;
    }

    // Phương thức lấy ra danh sách sản phẩm dựa vào danh sách id biến thể cho trước
    public List<ProductResponse> getProductsByVariantIds(List<Integer> variantIds) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        Product product;
        ProductVariant variant;
        for (Integer variantId : variantIds) {
            variant = productVariantRepository.findById(variantId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.VARIANT_NOT_FOUND.name()));
            product = variant.getProduct();
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.PRODUCT_NOT_FOUND.name());
            }
            ProductResponse productResponse = convertProductToDto(product, variant);
            productResponseList.add(productResponse);
        }
        return productResponseList;
    }

    public Integer getCategoryIdBySlug(String slug) {
        return categoryClient.getCategoryIdBySlug(slug);
    }

    public int getStock(Integer variantId) {
        ProductVariant productVariant = productVariantRepository.findById(variantId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.VARIANT_NOT_FOUND.name()));
        Inventory inventory = productInventoryRepository.findByProductVariant(productVariant);
        if (inventory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.INVENTORY_NOT_FOUND.name());
        }
        return inventory.getStock();
    }

    /**
     * Phương thức cập nhật số lượng biến thể trong kho
     * Bao gồm:
     * - replace: thay thế số lượng cũ bằng số lượng mới
     * - increase: tăng thêm số lượng
     * - decrease: giảm bớt số lượng
     */
    public int updateInventory(String type, UpdateInventoryRequest request) {
        // Tìm biến thể cần cập nhật số lượng trong kho
        ProductVariant productVariant = productVariantRepository.findById(request.getVariantId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.VARIANT_NOT_FOUND.name()));
        // Lấy ra đối tượng kho của biến thể
        Inventory inventory = productVariant.getInventory();
        if (inventory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionCode.INVENTORY_NOT_FOUND.name());
        }
        // Kiểm tra kiểu cập nhật (replace/increse/decrease)
        switch (type.toLowerCase()) {
            case "replace":
                inventory.setStock(request.getQuantity());
                break;
            case "increase":
                inventory.setStock(inventory.getStock() + request.getQuantity());
                break;
            case "decrease":
                inventory.setStock(inventory.getStock() - request.getQuantity());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_UPDATE_STOCK_TYPE.name());
        }
        productInventoryRepository.save(inventory);
        return inventory.getStock();
    }

    private ProductResponse convertProductToDto(Product product, ProductVariant specificVariant) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setOrigin(product.getOrigin());
        dto.setSlug(product.getSlug());
        dto.setRating(product.getRating());
        dto.setStatus(product.getStatus());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (specificVariant != null) {
            ProductVariantResponse variantResponse = convertVariantToDto(specificVariant);
            dto.setDefaultVariant(variantResponse);
            dto.setDefaultImage(variantResponse.getDefaultImage());
        } else {
            List<ProductVariantResponse> variantResponseList = new ArrayList<>();
            if (product.getVariants() != null && !product.getVariants().isEmpty()) {
                for (ProductVariant productVariant : product.getVariants()) {
                    ProductVariantResponse productVariantResponse = convertVariantToDto(productVariant);
                    variantResponseList.add(productVariantResponse);
                    if (productVariant.isDefaultStatus()) {
                        dto.setDefaultVariant(productVariantResponse);
                        for (ProductImage productImage : productVariant.getImages()) {
                            if (productImage.isDefaultStatus()) {
                                dto.setDefaultImage(convertImageToDto(productImage));
                                break;
                            }
                        }
                    }
                }
            }
            dto.setVariants(variantResponseList);
        }

        // Gán thông tin tên category, supplier, brand
        CategoryResponse category = categoryClient.getCategoryById(product.getCategoryId());
        SupplierResponse supplier = supplierClient.getSupplierById(product.getSupplierId());
        BrandResponse brand = brandClient.getBrandById(product.getBrandId());
        dto.setCategory(category);
        dto.setSupplier(supplier);
        dto.setBrand(brand);

        // Tính toán tổng số sản phẩm còn trong kho
        int totalStock = 0;
        for (ProductVariant variant : product.getVariants()) {
            totalStock += variant.getInventory().getStock();
        }
        dto.setTotalStock(totalStock);

        return dto;
    }

    public List<ProductResponse> convertProductListToDto(List<Product> products) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = convertProductToDto(product, null);
            productResponseList.add(productResponse);
        }
        return productResponseList;
    }

    private ProductVariantResponse convertVariantToDto(ProductVariant variant) {
        ProductVariantResponse dto = new ProductVariantResponse();
        dto.setId(variant.getId());
        dto.setName(variant.getName());
        dto.setBasePrice(variant.getBasePrice());
        dto.setDiscountPrice(variant.getDiscountPrice());
        dto.setActiveStatus(variant.isActiveStatus());
        dto.setDefaultStatus(variant.isDefaultStatus());
        dto.setCreatedAt(variant.getCreatedAt());
        dto.setUpdatedAt(variant.getUpdatedAt());
        List<ProductImageResponse> imageResponseList = new ArrayList<>();
        if (!variant.getImages().isEmpty()) {
            imageResponseList = convertImageListToDto(variant.getImages());
            // Tìm ra ảnh mặc định
            ProductImage defaultImage = productImageRepository.findByVariantAndDefaultStatusTrue(variant);
            dto.setDefaultImage(convertImageToDto(defaultImage));
        }
        dto.setImages(imageResponseList);

        List<ProductVariantColorResponse> colorResponseList = new ArrayList<>();
        if (!variant.getColors().isEmpty()) {
            colorResponseList = convertColorListToDto(variant.getColors());
        }
        dto.setColors(colorResponseList);

        dto.setStock(variant.getInventory().getStock());
        return dto;
    }

    private ProductImageResponse convertImageToDto(ProductImage image) {
        ProductImageResponse dto = new ProductImageResponse();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setDefaultStatus(image.isDefaultStatus());
        dto.setCreatedAt(image.getCreatedAt());
        dto.setUpdatedAt(image.getUpdatedAt());
        return dto;
    }

    private List<ProductImageResponse> convertImageListToDto(List<ProductImage> images) {
        List<ProductImageResponse> productImageResponseList = new ArrayList<>();
        for (ProductImage image : images) {
            productImageResponseList.add(convertImageToDto(image));
        }
        return productImageResponseList;
    }

    private ProductVariantColorResponse convertColorToDto(ProductVariantColor color) {
        ProductVariantColorResponse dto = new ProductVariantColorResponse();
        dto.setId(color.getId());
        dto.setColor(color.getColor());
        dto.setColor(color.getColor());
        return dto;
    }

    private List<ProductVariantColorResponse> convertColorListToDto(List<ProductVariantColor> colors) {
        List<ProductVariantColorResponse> productVariantColorResponseList = new ArrayList<>();
        for (ProductVariantColor color : colors) {
            productVariantColorResponseList.add(convertColorToDto(color));
        }
        return productVariantColorResponseList;
    }
}
