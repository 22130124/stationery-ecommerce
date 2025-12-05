package com.tqk.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.tqk.aiservice.client.BrandClient;
import com.tqk.aiservice.client.CategoryClient;
import com.tqk.aiservice.client.ProductClient;
import com.tqk.aiservice.dto.request.ProductSearchRequest;
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

    public GeminiResponse analyzeProductQuery(String userMessage) throws IOException {
        // Lấy danh sách categories
        List<CategoryResponse> categories = categoryClient.getActiveCategories();

        // Lấy danh sách brands
        List<BrandResponse> brands = brandClient.getActiveBrands();

        // Lấy danh sách leaf để AI match dễ nhất
        List<String> extractedCategoryList = extractLeafCategories(categories);
        List<String> extractedBrandList = extractBrands(brands);

        String categoriesJson = mapper.writeValueAsString(extractedCategoryList);
        String brandsJson = mapper.writeValueAsString(extractedBrandList);

        // Tạo prompt yêu cầu trả về JSON
        String prompt = """
                Bạn là AI phân tích nhu cầu mua sắm.
                
                Người dùng có thể yêu cầu:
                - Một hoặc nhiều sản phẩm trong một câu.
                - Một hoặc nhiều màu sắc cho cùng một sản phẩm.
                - Cách gọi sản phẩm có thể khác nhau (bút bi / viết bi / cây viết / viết…).
                
                Danh sách danh mục hiện có trong hệ thống (danh mục con – leaf categories):
                
                %s
                
                Danh sách thương hiệu hiện có trong hệ thống:
                
                %s
                
                Hãy TRẢ VỀ JSON THUẦN (không markdown), theo cấu trúc:
                
                {
                  "items": [
                    {
                      "brandId": số hoặc null,
                      "colors": ["đỏ", "xanh dương"] hoặc [],
                      "priceMin": số hoặc null,
                      "priceMax": số hoặc null,
                      "extras": ["từ khóa quan trọng"] hoặc [],
                      "categoryId": số hoặc null
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
                - categoryId có thể null nếu người dùng không nêu rõ loại sản phẩm muốn mua.
                
                Quy tắc xác định brandId:
                - Chọn thương hiệu phù hợp nhất từ danh sách đã cung cấp.
                - brandId có thể null nếu người dùng không có nhu cầu tìm kiếm về thương hiệu hoặc không có thương hiệu đó tồn tại trong hệ thống
                
                Quy tắc phân tích extra:
                - Tách các từ khóa quan trọng từ mô tả sản phẩm hoặc câu người dùng.
                - Loại bỏ từ dừng (như "dùng để", "những", "trong", "các",...).
                - Trả về mảng các từ khóa quan trọng trong extra.
                - Nếu không có từ khóa → trả về mảng rỗng [].
                
                Chỉ trả về JSON hợp lệ.
                
                Câu hỏi: %s
                """.formatted(categoriesJson, brandsJson, userMessage);

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
                    item.getBrandId(),
                    item.getColors(),
                    item.getPriceMin(),
                    item.getPriceMax(),
                    item.getExtras()
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

    // Hàm tiện ích tách id và tên thương hiệu (brand) phục vụ cho việc gọi Gemini
    private List<String> extractBrands(List<BrandResponse> list) {
        List<String> result = new ArrayList<>();
        for (BrandResponse b : list) {
                result.add(b.getId() + " - " + b.getName());
        }
        return result;
    }
}