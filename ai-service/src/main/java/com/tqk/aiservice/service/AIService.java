package com.tqk.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.tqk.aiservice.client.CategoryClient;
import com.tqk.aiservice.client.ProductClient;
import com.tqk.aiservice.dto.request.ProductSearchRequest;
import com.tqk.aiservice.dto.response.GeminiResponse;
import com.tqk.aiservice.dto.response.GeminiResponseItem;
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

    @PostConstruct
    public void init() {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public GeminiResponse analyzeProductQuery(String userMessage) throws IOException {
        // Lấy danh sách categories thật
        List<CategoryResponse> categories = categoryClient.getActiveCategories();

        // Lấy danh sách leaf-category để AI match dễ nhất
        List<String> leafList = extractLeafCategories(categories);
        String leafJson = mapper.writeValueAsString(leafList);

        // Tạo prompt yêu cầu trả về JSON
        String prompt = """
                Bạn là AI phân tích nhu cầu mua sắm.
                
                Người dùng có thể yêu cầu nhiều sản phẩm trong một câu.
                
                Danh sách danh mục hiện có trong hệ thống (danh mục con – leaf categories):
                
                %s
                
                TRẢ VỀ JSON THUẦN (không markdown), theo cấu trúc:
                
                {
                  "items": [
                    {
                      "keyword": "tên sản phẩm",
                      "color": "đen / đỏ / xanh hoặc null",
                      "priceMin": số hoặc null,
                      "priceMax": số hoặc null,
                      "extra": "keyword mô tả thêm hoặc null",
                      "categoryId": số ID danh mục (BAT BUOC)
                    }
                  ]
                }
                
                Quy tắc xác định categoryId:
                - Hãy chọn category phù hợp nhất trong danh sách leaf
                - Ví dụ:
                    'bút bi', 'viết bi', 'cây viết' -> id danh mục Bút bi
                    'bút chì', 'chì vẽ' -> id danh mục Bút chì
                    'tập', 'vở', 'vở ô ly' -> id của danh mục Tập - Vở
                - Luôn trả categoryId chính xác, không được để null.
                
                Chỉ trả về JSON hợp lệ.
                
                Câu hỏi: %s
                """.formatted(leafJson, userMessage);

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

    public List<ProductResponse> searchProductsForUserMessage(ProductSearchRequest request) throws IOException {
        String message = request.getMessage();

        // Phân tích câu hỏi
        GeminiResponse result = analyzeProductQuery(message);

        // Sanity check
        if (result == null || result.getItems() == null || result.getItems().isEmpty()) {
            return List.of();
        }

        List<ProductResponse> finalResult = new ArrayList<>();

        // Với mỗi item thì gọi product-service
        for (GeminiResponseItem item : result.getItems()) {
            List<ProductResponse> products = productClient.searchProducts(
                    item.getCategoryId(),
                    normalize(item.getKeyword()),
                    normalize(item.getColor()),
                    item.getPriceMin(),
                    item.getPriceMax(),
                    normalize(item.getExtra())
            );
            finalResult.addAll(products);
        }

        return finalResult;
    }

    // Hàm tiện ích nếu giá trị trả về của gemini là chuỗi rỗng không phải null
    private String normalize(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    // Hàm tiện ích tách các category con (leaf) thành list "id - name" để AI dễ hiểu
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
}