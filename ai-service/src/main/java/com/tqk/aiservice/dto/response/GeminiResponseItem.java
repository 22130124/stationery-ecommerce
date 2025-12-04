package com.tqk.aiservice.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponseItem {
    private String brandName;
    private List<String> colors;
    private Integer priceMin;
    private Integer priceMax;
    private List<String> extras;
    private Integer categoryId;
}
