package com.tqk.authservice.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    ACCOUNT_NOT_FOUND("Tài khoản không tồn tại"),
    ACCOUNT_LOCKED("Tài khoản đã bị khóa"),
    ACCOUNT_NOT_VERIFIED("Tài khoản chưa được xác thực"),
    EMAIL_ALREADY_EXISTS("Số điện thoại đã được sử dụng"),
    EMAIL_TOKEN_INVALID("Token xác thực email không hợp lệ"),
    EMAIL_TOKEN_EXPIRED("Token xác thực email đã hết hạn"),
    PASSWORD_TOKEN_INVALID("Token xác thực mật khẩu không hợp lệ"),
    PASSWORD_TOKEN_EXPIRED("Token xác thực mật khẩu đã hết hạn"),
    GOOGLE_TOKEN_VERIFICATION_FAILED("Lỗi khi xác thực Google Token"),
    GOOGLE_TOKEN_INVALID("Google token không hợp lệ"),
    INVALID_CREDENTIALS("Thông tin đăng nhập không chính xác"),
    INTERNAL_ERROR("Có lỗi xảy ra, vui lòng thử lại sau");

    private final String defaultMessage;

    ExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}

