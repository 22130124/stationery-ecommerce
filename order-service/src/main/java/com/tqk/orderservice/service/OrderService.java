package com.tqk.orderservice.service;

import com.tqk.orderservice.dto.request.AddOrderRequest;
import com.tqk.orderservice.dto.request.UpdateOrderRequest;
import com.tqk.orderservice.dto.response.OrderItemResponse;
import com.tqk.orderservice.dto.response.OrderResponse;
import com.tqk.orderservice.model.Order;
import com.tqk.orderservice.model.OrderItem;
import com.tqk.orderservice.repository.OrderItemRepository;
import com.tqk.orderservice.repository.OrderRepository;
import com.tqk.orderservice.repository.client.CartClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartClient cartClient;

    // Lấy ra tất cả danh sách đơn hàng
    @Transactional
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(Order::convertToDto)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrders(Integer accountId) {
        List<Order> orders = orderRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        return orders.stream()
                .map(Order::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse createOrder(Integer accountId, AddOrderRequest request) {

        // 1. Tính tổng tiền
        double total = request.getOrderItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        // 2. Tạo Order
        Order order = new Order();
        order.setAccountId(accountId);
        order.setTotalAmount(total);
        order.setStatus(1);
        order = orderRepository.save(order);

        // 3. Lưu OrderItems
        for (OrderItemResponse o : request.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(o.getProductId());
            orderItem.setVariantId(o.getVariantId());
            orderItem.setPrice(o.getPrice());
            orderItem.setQuantity(o.getQuantity());
            orderItemRepository.save(orderItem);
        }

        // 4. Reset lại giỏ hàng
        cartClient.resetCart(accountId);

        // 5. Trả về response
        return order.convertToDto();
    }

    @Transactional
    public OrderResponse updateOrderStatus(Integer accountId, Integer id, UpdateOrderRequest request) {

        // 1. Lấy order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Cập nhật trạng thái
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        // 3. Lưu
        orderRepository.save(order);

        return order.convertToDto();
    }

    @Transactional
    public OrderResponse cancelOrder(Integer accountId, Integer orderId) {

        // 1. Lấy order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Kiểm tra quyền sở hữu
        if (!order.getAccountId().equals(accountId)) {
            throw new RuntimeException("Không có quyền hủy đơn này");
        }

        // 3. Chỉ cho hủy khi đang 'Đang lấy hàng'
        if (!(order.getStatus() == 1)) {
            throw new RuntimeException("Chỉ đơn hàng ở trạng thái Đang lấy hàng mới được hủy");
        }

        // 4. Cập nhật trạng thái
        order.setStatus(0);
        orderRepository.save(order);

        return order.convertToDto();
    }
}
