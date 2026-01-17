package com.tqk.orderservice.dto.response.dashboard;

import lombok.Data;

@Data
public class TodayRevenueResponse {
    private long todayRevenue;
    private double percentChange;
}
