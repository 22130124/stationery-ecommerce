package com.tqk.aiservice.dto.response;

import lombok.Data;

@Data
public class GeminiResponse {
    private String keyword;
    private String color;
    private Integer priceMin;
    private Integer priceMax;
}
