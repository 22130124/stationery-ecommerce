package com.tqk.orderservice.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenuePointResponse {
    private String label;
    private long revenue;
}
