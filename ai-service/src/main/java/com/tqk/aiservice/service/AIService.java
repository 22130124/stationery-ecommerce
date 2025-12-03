package com.tqk.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.tqk.aiservice.dto.response.GeminiResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private Client client;
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public GeminiResponse analyzeProductQuery(String userMessage) throws IOException {
        // Tạo prompt yêu cầu trả về JSON
        String prompt = """
            Bạn là AI hỗ trợ mua sắm. Hãy phân tích câu hỏi và trả về JSON thuần (không markdown).
            Cấu trúc JSON:
            {
              "keyword": "tên sản phẩm",
              "color": "màu sắc (hoặc null)",
              "priceMin": số (hoặc null),
              "priceMax": số (hoặc null)
            }
            Câu hỏi: %s
            """.formatted(userMessage);

        // Gọi Gemini qua SDK
        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                null
        );

        // Lấy kết quả dạng chuỗi (String)
        String rawResponse = response.text();

        // Parse chuỗi đó thành Object
        return parseResponse(rawResponse);
    }

    private GeminiResponse parseResponse(String rawJson) throws IOException {
        // Làm sạch dữ liệu json
        String cleanJson = rawJson.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        return mapper.readValue(cleanJson, GeminiResponse.class);
    }
}