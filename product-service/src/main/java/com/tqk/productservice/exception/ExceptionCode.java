package com.tqk.productservice.exception;

import jakarta.annotation.Resource;
import lombok.Getter;

@Getter
public enum ExceptionCode {
    PRODUCT_NOT_FOUND("Sản phẩm không tồn tại trong hệ thống"),
    VARIANT_NOT_FOUND("Biến thể sản phẩm không tồn tại trong hệ thống"),
    INVENTORY_NOT_FOUND("Biến thể sản phẩm chưa có dữ liệu trong kho"),
    INVALID_PRODUCT_CODE("Mã sản phẩm không hợp lệ"),
    INVALID_UPDATE_STOCK_TYPE("Tham số kiểu cập nhật số lượng kho không hợp lệ"),
    OUT_OF_STOCK("Số lượng sản phẩm trong kho không đủ"),
    SLUG_ALREADY_EXISTS("Slug sản phẩm đã tồn tại trong hệ thống"),
    MISSING_VARIANT_IMAGE("Thiếu hình ảnh sản phẩm. Mỗi biến thể sản phẩm cần ít nhất một hình ảnh"),
    INTERNAL_ERROR("Có lỗi xảy ra, vui lòng thử lại sau");

    private final String defaultMessage;

    ExceptionCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}

