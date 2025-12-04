package com.tqk.aiservice.controller;

import com.tqk.aiservice.dto.request.ProductSearchRequest;
import com.tqk.aiservice.dto.response.ai.GeminiResponse;
import com.tqk.aiservice.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeProduct(@RequestBody Map<String, String> request) {
        // Lấy câu hỏi từ JSON gửi lên
        String userQuestion = request.get("question");

        if (userQuestion == null || userQuestion.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Câu hỏi không được để trống");
        }

        try {
            // Gọi Service xử lý
            GeminiResponse response = aiService.analyzeProductQuery(userQuestion);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi khi gọi Gemini: " + e.getMessage());
        }
    }

    @PostMapping("/search-products")
    public ResponseEntity<?> searchProducts(@RequestBody ProductSearchRequest request) throws Exception {
        return ResponseEntity.ok(aiService.searchProductsForUserMessage(request));
    }
}
