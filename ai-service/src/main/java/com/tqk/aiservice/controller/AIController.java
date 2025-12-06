package com.tqk.aiservice.controller;

import com.tqk.aiservice.dto.request.ChatRequest;
import com.tqk.aiservice.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeProduct(@RequestBody ChatRequest request) throws IOException {
        return ResponseEntity.ok(aiService.analyzeRequest(request));
    }

    @PostMapping("/search-products")
    public ResponseEntity<?> searchProducts(@RequestBody ChatRequest request) throws Exception {
        return ResponseEntity.ok(aiService.searchProductsWithContext(request));
    }
}
