package com.tqk.orderservice.service;

import com.tqk.orderservice.dto.response.dashboard.RevenuePointResponse;
import com.tqk.orderservice.dto.response.dashboard.ProductSoldResponse;
import com.tqk.orderservice.dto.response.dashboard.TodayOrdersResponse;
import com.tqk.orderservice.dto.response.dashboard.TodayRevenueResponse;
import com.tqk.orderservice.dto.response.product.ProductResponse;
import com.tqk.orderservice.model.Order;
import com.tqk.orderservice.repository.OrderRepository;
import com.tqk.orderservice.repository.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.tqk.orderservice.model.Order.PaymentStatus.PAID;
import static com.tqk.orderservice.model.Order.ShippingStatus.CANCELLED;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private static final List<Order.ShippingStatus> EXCLUDED_STATUSES =
            List.of(Order.ShippingStatus.CANCELLED, Order.ShippingStatus.EXPIRED);


    // Phương thức lấy ra danh sách lợi nhuận theo range
    public List<RevenuePointResponse> getRevenue(String range) {
        if (range == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "range is required");
        }

        range = range.trim().toLowerCase();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from;

        switch (range) {
            case "7d":
                from = now.toLocalDate().minusDays(6).atStartOfDay();
                return mapDaily(orderRepository.getDailyRevenue(from, EXCLUDED_STATUSES));

            case "30d":
                from = now.toLocalDate().minusDays(29).atStartOfDay();
                return mapDaily(orderRepository.getDailyRevenue(from, EXCLUDED_STATUSES));

            case "90d":
                from = now.toLocalDate().minusMonths(2).withDayOfMonth(1).atStartOfDay();
                return mapMonthly(orderRepository.getMonthlyRevenue(from, EXCLUDED_STATUSES));

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Range không hợp lệ");
        }
    }

    public List<ProductSoldResponse> getTopProducts(String range, int limit) {
        if (range == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "range is required");
        }

        range = range.trim().toLowerCase();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from;

        switch (range) {
            case "7d":
                from = now.toLocalDate().minusDays(6).atStartOfDay();
                break;
            case "30d":
                from = now.toLocalDate().minusDays(29).atStartOfDay();
                break;
            case "90d":
                from = now.toLocalDate().minusDays(89).atStartOfDay();
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Range không hợp lệ");
        }

        // Query DB lấy productId + tổng quantity
        List<Object[]> rows =
                orderRepository.getTotalSoldQuantityByProduct(from, EXCLUDED_STATUSES).stream().limit(limit).toList();

        if (rows.isEmpty()) {
            return Collections.emptyList();
        }

        // Tách productId & quantity (giữ thứ tự DESC)
        Map<Integer, Integer> quantityByProductId = new LinkedHashMap<>();
        List<Integer> productIds = new ArrayList<>();

        for (Object[] row : rows) {
            Integer productId = (Integer) row[0];
            Integer quantity = ((Number) row[1]).intValue();

            quantityByProductId.put(productId, quantity);
            productIds.add(productId);
        }

        // Gọi product-service lấy thông tin sản phẩm
        List<ProductResponse> products = productClient.getProductsByIds(productIds);

        // Map productId thành ProductResponse
        Map<Integer, ProductResponse> productMap = products.stream().collect(Collectors.toMap(ProductResponse::getId, p -> p));

        // Ghép thành ProductSoldResponse
        List<ProductSoldResponse> result = new ArrayList<>();

        for (Integer productId : productIds) {
            ProductResponse product = productMap.get(productId);

            if (product != null) {
                ProductSoldResponse response = new ProductSoldResponse();
                response.setProduct(product);
                response.setQuantity(quantityByProductId.get(productId));
                result.add(response);
            }
        }

        return result;
    }

    // Phương thức chuyển dữ liệu doanh thu theo ngày từ query sang DTO
    private List<RevenuePointResponse> mapDaily(List<Object[]> rows) {
        List<RevenuePointResponse> result = new ArrayList<>();

        // Mỗi row gồm:
        // row[0] = ngày (yyyy-MM-dd)
        // row[1] = tổng doanh thu của ngày đó
        for (Object[] row : rows) {
            String date = row[0].toString();
            long total = ((Number) row[1]).longValue();

            result.add(new RevenuePointResponse(date, total));
        }

        return result;
    }

    // Phương thức chuyển dữ liệu doanh thu theo tháng từ query sang DTO
    private List<RevenuePointResponse> mapMonthly(List<Object[]> rows) {
        List<RevenuePointResponse> result = new ArrayList<>();

        // Mỗi row gồm:
        // row[0] = tháng (yyyy-MM)
        // row[1] = tổng doanh thu của tháng đó
        for (Object[] row : rows) {
            String month = row[0].toString();
            long total = ((Number) row[1]).longValue();

            result.add(new RevenuePointResponse(month, total));
        }

        return result;
    }

    public TodayRevenueResponse getTodayRevenue() {
        // Lấy ra thời gian hiện tại
        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        // Lấy ra thời gian bắt đầu và kết thúc của hôm nay
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();

        // Lấy ra thời gian bắt đầu và kết thúc của hôm qua
        LocalDateTime yesterdayStart = today.minusDays(1).atStartOfDay();
        LocalDateTime yesterdayEnd = today.atStartOfDay();

        // Tính tổng doanh thu hôm nay
        long todayRevenue = orderRepository.sumRevenueBetween(todayStart, todayEnd, EXCLUDED_STATUSES);
        // Tính tổng doanh thu hôm qua
        long yesterdayRevenue = orderRepository.sumRevenueBetween(yesterdayStart, yesterdayEnd, EXCLUDED_STATUSES);
        // Tính toán sự chênh lệch % giữa hai ngày
        double percentChange = calculatePercent(todayRevenue, yesterdayRevenue);

        TodayRevenueResponse response = new TodayRevenueResponse();
        response.setTodayRevenue(todayRevenue);
        response.setPercentChange(percentChange);

        return response;
    }

    public TodayOrdersResponse getTodayOrders() {
        // Lấy ra thời gian hiện tại
        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        // Lấy ra thời gian bắt đầu và kết thúc của hôm nay
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();

        // Lấy ra thời gian bắt đầu và kết thúc của hôm qua
        LocalDateTime yesterdayStart = today.minusDays(1).atStartOfDay();
        LocalDateTime yesterdayEnd = today.atStartOfDay();

        // Tính tổng số đơn hàng hôm nay
        long todayOrders = orderRepository.countOrdersBetween(todayStart, todayEnd, EXCLUDED_STATUSES);
        // Tính tổng số đơn hàng hôm qua
        long yesterdayOrders = orderRepository.countOrdersBetween(yesterdayStart, yesterdayEnd, EXCLUDED_STATUSES);
        // Tính toán sự chênh lệch % giữa hai ngày
        double percentChange = calculatePercent(todayOrders, yesterdayOrders);

        TodayOrdersResponse response = new TodayOrdersResponse();
        response.setTodayOrders(todayOrders);
        response.setPercentChange(percentChange);

        return response;
    }

    private double calculatePercent(long today, long yesterday) {
        if (yesterday == 0) {
            return today > 0 ? 100.0 : 0.0;
        }
        return ((double) (today - yesterday) / yesterday) * 100;
    }
}
