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
                
                Người dùng có thể yêu cầu:
                - Một hoặc nhiều sản phẩm trong một câu.
                - Một hoặc nhiều màu sắc cho cùng một sản phẩm.
                - Cách gọi sản phẩm có thể khác nhau (bút bi / viết bi / cây viết / viết…).
                
                Danh sách danh mục hiện có trong hệ thống (danh mục con – leaf categories):
                
                %s
                
                Hãy TRẢ VỀ JSON THUẦN (không markdown), theo cấu trúc:
                
                {
                  "items": [
                    {
                      "keyword": "tên sản phẩm",
                      "colors": ["đỏ", "xanh dương"] hoặc [],
                      "priceMin": số hoặc null,
                      "priceMax": số hoặc null,
                      "extra": "thông tin mô tả thêm hoặc null",
                      "categoryId": số (bắt buộc)
                    }
                  ]
                }
                
                Quy tắc phân tích màu sắc:
                - Nếu người dùng nói: 
                    "màu xanh dương hoặc đỏ" → ["xanh dương", "đỏ"]
                    "đen và trắng" → ["đen", "trắng"]
                    "xanh, đỏ, vàng" → ["xanh", "đỏ", "vàng"]
                - Nếu không đề cập màu → trả về mảng rỗng [].
                
                Quy tắc xác định categoryId:
                - Chọn danh mục leaf phù hợp nhất từ danh sách đã cung cấp.
                - Ví dụ:
                    "bút bi", "viết bi", "cây viết" → categoryId của Bút Bi
                    "bút chì", "chì vẽ" → categoryId của Bút Chì
                    "tập", "vở", "vở ô ly" → categoryId của Tập – Vở
                - Không bao giờ được để categoryId = null.
                
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
                    item.getColors(),
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