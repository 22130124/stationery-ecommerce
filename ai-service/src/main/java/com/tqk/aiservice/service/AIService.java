package com.tqk.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.tqk.aiservice.client.BrandClient;
import com.tqk.aiservice.client.CategoryClient;
import com.tqk.aiservice.client.ProductClient;
import com.tqk.aiservice.dto.request.ChatRequest;
import com.tqk.aiservice.dto.request.PromptRequest;
import com.tqk.aiservice.dto.response.ai.ChatResponse;
import com.tqk.aiservice.dto.response.ai.GeminiResponse;
import com.tqk.aiservice.dto.response.ai.GeminiResponseItem;
import com.tqk.aiservice.dto.response.brand.BrandResponse;
import com.tqk.aiservice.dto.response.category.CategoryResponse;
import com.tqk.aiservice.dto.response.product.ProductResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private Client client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProductClient productClient;
    private final CategoryClient categoryClient;
    private final BrandClient brandClient;

    @PostConstruct
    public void init() {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    // Hảm để test kết quả trả về từ Gemini
    public GeminiResponse analyzeRequest(ChatRequest request) throws IOException {
        List<String> messages = request.getMessages();

        if (messages == null || messages.isEmpty()) {
            throw new RuntimeException("Danh sách messages rỗng");
        }

        // Chuẩn bị categories & brands
        List<CategoryResponse> categories = categoryClient.getActiveCategories();
        List<BrandResponse> brands = brandClient.getActiveBrands();
        List<String> leafCategories = extractLeafCategories(categories);
        List<String> leafBrands = extractBrands(brands);

        // Build prompt chứa messages (chứa toàn bộ lịch sử chat cần thiết cho phân tích)
        String prompt = PromptRequest.buildConversationPrompt(messages, leafCategories, leafBrands);

        // Gọi Gemini để phân tích prompt
        return analyzePrompt(prompt);
    }

    public ChatResponse searchProductsWithContext(ChatRequest request) throws IOException {
        List<String> messages = request.getMessages();

        if (messages == null || messages.isEmpty()) {
            throw new RuntimeException("Danh sách messages rỗng");
        }

        // Chuẩn bị categories & brands
        List<CategoryResponse> categories = categoryClient.getActiveCategories();
        List<BrandResponse> brands = brandClient.getActiveBrands();
        List<String> leafCategories = extractLeafCategories(categories);
        List<String> leafBrands = extractBrands(brands);

        // Build prompt chứa messages (chứa toàn bộ lịch sử chat cần thiết cho phân tích)
        String prompt = PromptRequest.buildConversationPrompt(messages, leafCategories, leafBrands);

        // Gọi Gemini để phân tích prompt
        GeminiResponse geminiResponse = analyzePrompt(prompt);

        // Nếu Gemini kết luận yêu cầu mới nhất của người dùng không liên quan tới các yêu cầu trước thì chỉ phân tích yêu cầu mới
        return searchProducts(geminiResponse);
    }

    public GeminiResponse analyzePrompt(String prompt) throws IOException {
        // Gọi đến mô hình gemini để phân tích promt
        GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, null);

        // Xử lý làm sạch kết quả trả về từ Gemin
        String rawText = response.text();
        String cleanText = "";
        if (rawText != null) {
            cleanText = rawText.replaceAll("```json", "").replaceAll("```", "").trim();
        }
        // Chuyển json thành đối tượng java và trả về kết quả
        return mapper.readValue(cleanText, GeminiResponse.class);
    }

    public ChatResponse searchProducts(GeminiResponse geminiResponse) throws IOException {
        ChatResponse chatResponse = new ChatResponse();

        // Trường hợp chit chat hoặc thiếu thông tin cần thiết cho việc truy vấn
        if (geminiResponse.getMessage() != null) {
            chatResponse.setMessage(geminiResponse.getMessage());
            chatResponse.setRelated(geminiResponse.isRelated());
            return chatResponse;
        }

        // Trường hợp người dùng hỏi về sản phẩm
        List<ProductResponse> productResponseList = new ArrayList<>();

        // Với mỗi item thì gọi product-service
        for (GeminiResponseItem item : geminiResponse.getItems()) {
            List<ProductResponse> products = productClient.searchProducts(
                    item.getCategoryId(),
                    item.getBrandId(),
                    item.getColors(),
                    item.getPriceMin(),
                    item.getPriceMax(),
                    item.getExtras()
            );
            productResponseList.addAll(products);
        }

        chatResponse.setMessage("Đây là sản phẩm phù hợp với yêu cầu của bạn");
        chatResponse.setProducts(productResponseList);
        chatResponse.setRelated(geminiResponse.isRelated());

        return chatResponse;
    }

    // Hàm tiện ích tách các category con (node lá) thành list gồm id và name để AI dễ hiểu
    private List<String> extractLeafCategories(List<CategoryResponse> list) {
        List<String> result = new ArrayList<>();
        for (CategoryResponse c : list) {
            if (c.getChildren() == null || c.getChildren().isEmpty()) {
                result.add(c.getId() + " - " + c.getName());
            } else {
                result.addAll(extractLeafCategories(c.getChildren()));
            }
        }
        return result;
    }

    // Hàm tiện ích tách id và tên thương hiệu (brand) phục vụ cho việc gọi Gemini
    private List<String> extractBrands(List<BrandResponse> list) {
        List<String> result = new ArrayList<>();
        for (BrandResponse b : list) {
            result.add(b.getId() + " - " + b.getName());
        }
        return result;
    }
}