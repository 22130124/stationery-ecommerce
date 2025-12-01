package com.tqk.orderservice.dto.response.profile;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileResponse {
    private Integer id;
    private Integer accountId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
