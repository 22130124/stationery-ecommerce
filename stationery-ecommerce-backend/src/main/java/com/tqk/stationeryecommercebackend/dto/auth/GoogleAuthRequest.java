package com.tqk.stationeryecommercebackend.dto.auth;

import javax.validation.constraints.NotBlank;

public class GoogleAuthRequest {
    @NotBlank(message = "Google token is required")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
