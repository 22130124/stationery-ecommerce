package com.tqk.authservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountResponse {
    private Integer id;
    private String email;
    private String role;
    private boolean activeStatus;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
