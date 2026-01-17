package com.tqk.orderservice.dto.response.dashboard;

import com.tqk.orderservice.dto.response.product.ProductResponse;
import lombok.Data;

/**
 * Đây là đối tượng trả về cho chức năng dashboard - top sản phẩm bán chạy
 * product: thông tin sản phẩm
 * quantity: tổng số lượng bán được
 */

@Data
public class ProductSoldResponse {
    private ProductResponse product;
    private int quantity;
}
