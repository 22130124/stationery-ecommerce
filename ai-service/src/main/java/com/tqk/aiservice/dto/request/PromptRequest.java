package com.tqk.aiservice.dto.request;

import java.util.List;

public class PromptRequest {

    public static String buildConversationPrompt(List<String> messages, List<String> categories, List<String> brands) {
        return """
        Bạn là AI phân tích yêu cầu mua sắm cho trang thương mại điện tử bán văn phòng phẩm và dụng cụ học sinh.

        Nhiệm vụ của bạn:
        - Đọc toàn bộ lịch sử cuộc trò chuyện giữa người dùng và hệ thống.
        - Xác định xem yêu cầu mới nhất có liên quan đến các yêu cầu trước hay không.
        - Nếu các yêu cầu trước hỏi về sản phẩm, yêu cầu mới nhất cũng hỏi về sản phẩm thì là liên quan với nhau và gộp điều kiện truy vấn (related = true).
        - Nếu các yêu cầu trước hỏi về sản phẩm, yêu cầu mới nhất trò chuyện chit chat (và ngược lại) thì related = false.
        - Nếu các yêu cầu trước là chit chat, yêu cầu mới nhất cũng là chit chat thì related = true.
        - Nếu là trò chuyện chit chat thì items = [].

        Danh sách danh mục (category) của hệ thống:
        %s

        Danh sách thương hiệu (brand) của hệ thống:
        %s
        
        Quy tắc phân tích màu sắc:
        - Nếu người dùng nói: 
            "màu xanh dương hoặc đỏ" → ["xanh dương", "đỏ"]
            "đen và trắng" → ["đen", "trắng"]
            "xanh, đỏ, vàng" → ["xanh", "đỏ", "vàng"]
        - Nếu không đề cập màu → trả về mảng rỗng [].
                
        Quy tắc xác định categoryId:
            - Chọn (id) danh mục phù hợp nhất từ danh sách đã cung cấp.
            - categoryId có thể null nếu người dùng không nêu rõ loại sản phẩm muốn mua.
                
        Quy tắc xác định brandId:
            - Chọn (id) thương hiệu phù hợp nhất từ danh sách đã cung cấp.
            - brandId có thể null nếu người dùng không có nhu cầu tìm kiếm về thương hiệu hoặc không có thương hiệu đó tồn tại trong hệ thống
                
        Quy tắc phân tích extra:
            - Tách các từ khóa quan trọng từ mô tả sản phẩm hoặc câu người dùng.
            - Loại bỏ từ dừng (như "dùng để", "những", "trong", "các",...).
            - Trả về mảng các từ khóa quan trọng trong extra.
            - Nếu không có từ khóa → trả về mảng rỗng [].
            
        Quy tắc gán giá trị cho message:
            - Nếu yêu cầu của người dùng mơ hồ, ví dụ "tôi muốn mua một sản phẩm màu đỏ" thì cần làm rõ người dùng muốn tìm loại sản phẩm nào.
              Gán message có dạng "Bạn muốn tìm sản phẩm thuộc danh mục nào nào? Hãy cung cấp chi tiết để tôi có thể hỗ trợ bạn tìm kiếm tốt hơn"
            - Nếu giả sử người dùng trò chuyện không liên quan đến tư vấn sản phẩm (chit chat) thì hãy cố gắng trò chuyện với người dùng. Tin nhắn phản hồi sẽ gán vào "message".
            - Nếu người dùng đã bắt đầu hỏi về sản phẩm với đầy đủ thông tin cần thiết thì gán message là null

        Hãy trả về JSON (không markdown, không giải thích gì thêm) theo cấu trúc:
        {
          "items": [
            {
              "brandId": number or null,
              "colors": ["đỏ","xanh"] or [],
              "priceMin": number or null,
              "priceMax": number or null,
              "extras": ["từ khóa"],
              "categoryId": number or null,
            }
          ],
          "related": true/false,
          "message": Thông điệp phản hồi cho người dùng hoặc null
        }

        Đây là lịch sử hội thoại:
        %s
        """.formatted(
                categories.toString(),
                brands.toString(),
                messages.toString()
        );
    }
}
