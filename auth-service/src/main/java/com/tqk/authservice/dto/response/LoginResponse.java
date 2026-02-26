package com.tqk.authservice.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String email;
    private String role;
}
