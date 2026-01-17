package com.tqk.orderservice.controller;

import com.tqk.orderservice.dto.response.dashboard.RevenuePointResponse;
import com.tqk.orderservice.dto.response.dashboard.ProductSoldResponse;
import com.tqk.orderservice.dto.response.dashboard.TodayOrdersResponse;
import com.tqk.orderservice.dto.response.dashboard.TodayRevenueResponse;
import com.tqk.orderservice.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(@RequestParam(name = "range", defaultValue = "7d") String range) {
        List<RevenuePointResponse> result = dashboardService.getRevenue(range);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top-products")
    public ResponseEntity<?> getTopProducts(@RequestParam(name = "range", defaultValue = "7d") String range,
                                            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<ProductSoldResponse> result = dashboardService.getTopProducts(range, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/today/revenue")
    public ResponseEntity<?> getTodayRevenue() {
        TodayRevenueResponse result = dashboardService.getTodayRevenue();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/today/orders")
    public ResponseEntity<?> getTodayTotalOrders() {
        TodayOrdersResponse result = dashboardService.getTodayOrders();
        return ResponseEntity.ok(result);
    }
}
