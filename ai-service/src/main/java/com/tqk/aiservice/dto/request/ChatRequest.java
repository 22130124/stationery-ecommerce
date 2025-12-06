package com.tqk.aiservice.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    private List<String> messages;
}

