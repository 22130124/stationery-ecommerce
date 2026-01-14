package com.tqk.productservice.controller;

import com.tqk.productservice.dto.response.ProductListResponse;
import com.tqk.productservice.dto.response.ProductResponse;
import com.tqk.productservice.model.product.Product;
import com.tqk.productservice.service.ProductService;
import com.tqk.productservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String search,
                                    @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                    @RequestParam(name = "limit", required = false, defaultValue = "25") int size) {
        ProductListResponse products = searchService.searchByName(search, page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/by-ai")
    public ResponseEntity<?> searchProducts(@RequestParam(required = false) Integer categoryId,
                                            @RequestParam(required = false) Integer brandId,
                                            @RequestParam(required = false) List<String> colors,
                                            @RequestParam(required = false) Integer minPrice,
                                            @RequestParam(required = false) Integer maxPrice,
                                            @RequestParam(required = false) List<String> extras
    ) {
        List<ProductResponse> products = searchService.searchProductsForAI(categoryId, brandId, colors, minPrice, maxPrice, extras);
        return ResponseEntity.ok(products);
    }
}
