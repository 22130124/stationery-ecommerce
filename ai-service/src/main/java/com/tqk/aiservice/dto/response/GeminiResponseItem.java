package com.tqk.aiservice.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponseItem {
    private String keyword;
    private List<String> colors;
    private Integer priceMin;
    private Integer priceMax;
    private String extra;
    private Integer categoryId;
}
