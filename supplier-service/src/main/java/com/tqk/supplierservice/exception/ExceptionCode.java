package com.tqk.supplierservice.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    SUPPLIER_NOT_FOUND("Nhà cung cấp không tồn tại trong hệ thống"),
    BRAND_NOT_FOUND("Thương hiệu không tồn tại trong hệ thống"),
    SUPPLIER_SLUG_ALREADY_EXISTS("Slug nhà cung cấp đã tồn tại trong hệ thống"),
    BRAND_SLUG_ALREADY_EXISTS("Slug thương hiệu đã tồn tại trong hệ thống"),
    INTERNAL_ERROR("Có lỗi xảy ra, vui lòng thử lại sau");

    private final String defaultMessage;

    ExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}

