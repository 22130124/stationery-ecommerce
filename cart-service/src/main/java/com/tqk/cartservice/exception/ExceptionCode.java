package com.tqk.cartservice.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    ITEM_NOT_FOUND("Không tồn tại sản phẩm trong giỏ hàng"),
    INTERNAL_ERROR("Có lỗi xảy ra, vui lòng thử lại sau");

    private final String defaultMessage;

    ExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}

