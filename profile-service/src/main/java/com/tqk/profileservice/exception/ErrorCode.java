package com.tqk.profileservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    PHONE_ALREADY_EXISTS("Số điện thoại đã được sử dụng"),
    INTERNAL_ERROR("Có lỗi xảy ra, vui lòng thử lại sau");

    private final String defaultMessage;

    ErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}

