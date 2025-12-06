package com.tqk.aiservice.dto.response.ai;

import com.tqk.aiservice.dto.response.product.ProductResponse;
import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String message;
    private List<ProductResponse> products;
    private boolean related;
    private boolean chitChatStatus;
}
