package com.tqk.productservice.service;

import com.tqk.productservice.dto.response.ProductListResponse;
import com.tqk.productservice.dto.response.ProductResponse;
import com.tqk.productservice.model.product.Product;
import com.tqk.productservice.repository.product.ProductRepository;
import com.tqk.productservice.util.TextNormalizeUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final EntityManager entityManager;

    // Hàm truy vấn sản phẩm phục vụ cho tính năng chatbot
    public List<ProductResponse> searchProductsForAI(Integer categoryId, Integer brandId, List<String> colors, Integer minPrice, Integer maxPrice, List<String> extra) {
        int hasColors = (colors != null && !colors.isEmpty()) ? 1 : 0;
        List<Product> products = productRepository.searchProductsWithScore(categoryId, brandId, colors, minPrice, maxPrice, extra, hasColors);
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (!products.isEmpty()) {
            productResponseList = productService.convertProductListToDto(products);
        }
        return productResponseList;
    }

    public ProductListResponse searchByName(String keyword, int page, int size) {
        ProductListResponse productListResponse = new ProductListResponse();
    
        // Normalize input (biến tất cả thành chữ thường không dấu)
        String normalized = TextNormalizeUtil.normalize(keyword);

        // Tokenize
        String[] words = normalized.split("\\s+");

        /*
        Chỉ giữ các từ có 2 ký tự trở lên
        Lý do: giả sử một từ chỉ có "a" hoặc "b",... thì sản phẩm nào cũng khớp
         */
        List<String> tokens = Arrays.stream(words)
                .filter(word -> word.length() >= 2)
                .toList();

        // Nếu token rỗng thì trả về mãng rỗng
        if (tokens.isEmpty()) {
            productListResponse.setProducts(List.of());
            productListResponse.setCurrentPage(0);
            productListResponse.setTotalPages(0);
            productListResponse.setTotalItems(0);
            return productListResponse;
        }

        // Đếm tổng số token
        int total = tokens.size();

        // Xác định ngưỡng để trả về kết quả
        double threshold = 0.5; // match 50% keyword là được

        // Tính toán tổng số từ tối thiểu cần phải match dựa trên threshold
        int requiredMatch = (int) Math.ceil(total * threshold);

        // Biến offset phục vụ cho phân trang
        int offset = (page - 1) * size;

        // Build match_count
        StringBuilder matchExpr = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            matchExpr.append(String.format(
                    "(CASE WHEN p.name COLLATE utf8mb4_general_ci LIKE :t%d THEN 1 ELSE 0 END)",
                    i
            ));
            if (i < tokens.size() - 1) matchExpr.append(" + ");
        }

        // Build query lấy data
        String dataSql = """
        SELECT *
        FROM (
            SELECT p.*, %s AS match_count
            FROM products p
        ) t
        WHERE match_count >= :requiredMatch
        ORDER BY match_count DESC
        LIMIT :limit OFFSET :offset
        """.formatted(matchExpr);

        Query dataQuery = entityManager.createNativeQuery(dataSql, Product.class);

        String countSql = """
        SELECT COUNT(*)
        FROM (
            SELECT p.id, %s AS match_count
            FROM products p
        ) t
        WHERE match_count >= :requiredMatch
        """.formatted(matchExpr);

        Query countQuery = entityManager.createNativeQuery(countSql);

        for (int i = 0; i < tokens.size(); i++) {
            String value = "%" + tokens.get(i) + "%";
            dataQuery.setParameter("t" + i, value);
            countQuery.setParameter("t" + i, value);
        }

        dataQuery.setParameter("requiredMatch", requiredMatch);
        countQuery.setParameter("requiredMatch", requiredMatch);

        dataQuery.setParameter("limit", size);
        dataQuery.setParameter("offset", offset);

        // ===== execute =====
        List<Product> products = dataQuery.getResultList();
        long totalItems = ((Number) countQuery.getSingleResult()).longValue();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        ProductListResponse response = new ProductListResponse();
        response.setProducts(productService.convertProductListToDto(products));
        response.setTotalItems(totalItems);
        response.setTotalPages(totalPages);
        response.setCurrentPage(page);

        return response;
    }
}
