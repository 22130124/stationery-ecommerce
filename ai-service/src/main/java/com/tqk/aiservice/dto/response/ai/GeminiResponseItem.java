package com.tqk.aiservice.dto.response.ai;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponseItem {
    private Integer brandId;
    private List<String> colors;
    private Integer priceMin;
    private Integer priceMax;
    private List<String> extras;
    private Integer categoryId;
}
