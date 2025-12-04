package com.tqk.aiservice.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponse {
    private List<GeminiResponseItem> items;
}