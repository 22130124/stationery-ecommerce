package com.tqk.orderservice.dto.response.dashboard;

import lombok.Data;

@Data
public class TodayOrdersResponse {
    private long todayOrders;
    private double percentChange;
}
