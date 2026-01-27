package com.tqk.cartservice.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String code;
    private String message;

    public ExceptionResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
