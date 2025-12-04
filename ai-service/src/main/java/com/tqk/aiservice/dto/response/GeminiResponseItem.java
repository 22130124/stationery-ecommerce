package com.tqk.aiservice.dto.response;

import lombok.Data;

@Data
public class GeminiResponseItem {
    private String keyword;
    private String color;
    private Integer priceMin;
    private Integer priceMax;
    private String extra;
    private Integer categoryId;
}
