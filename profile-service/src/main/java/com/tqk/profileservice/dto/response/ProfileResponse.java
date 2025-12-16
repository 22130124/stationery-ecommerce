package com.tqk.profileservice.dto.response;

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
    private String avatarPublicId;
    private boolean completedStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
