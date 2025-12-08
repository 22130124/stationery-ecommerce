package com.tqk.aiservice.dto.response.ai;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponse {
    private List<GeminiResponseItem> items;
    private String message;
    private boolean related;
}