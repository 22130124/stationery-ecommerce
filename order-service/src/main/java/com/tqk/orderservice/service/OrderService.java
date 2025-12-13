package com.tqk.orderservice.service;

import com.tqk.orderservice.dto.request.AddOrderItemRequest;
import com.tqk.orderservice.dto.request.AddOrderRequest;
import com.tqk.orderservice.dto.request.UpdateOrderRequest;
import com.tqk.orderservice.dto.response.order.OrderDetailResponse;
import com.tqk.orderservice.dto.response.order.OrderItemResponse;
import com.tqk.orderservice.dto.response.order.OrderResponse;
import com.tqk.orderservice.dto.response.payment.PaymentResult;
import com.tqk.orderservice.dto.response.product.ProductResponse;
import com.tqk.orderservice.dto.response.profile.ProfileResponse;
import com.tqk.orderservice.model.Order;
import com.tqk.orderservice.model.OrderItem;
import com.tqk.orderservice.repository.OrderItemRepository;
import com.tqk.orderservice.repository.OrderRepository;
import com.tqk.orderservice.repository.client.CartClient;
import com.tqk.orderservice.repository.client.ProductClient;
import com.tqk.orderservice.repository.client.ProfileClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartClient cartClient;
    private final ProfileClient profileClient;
    private final VnPayService vnPayService;
    private final ProductClient productClient;

    public String createPaymentUrl(Integer orderId) {
        // Lấy thông tin đơn hàng
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // số tiền thanh toán
        int amount = order.getTotalAmount();

        return vnPayService.createPaymentUrl(orderId, amount);
    }

    // Lấy ra tất cả danh sách đơn hàng
    @Transactional
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(Order::convertToDto)
                .collect(Collectors.toList());
    }

    // Hàm lấy ra danh sách đơn hàng theo account id
    public List<OrderResponse> getOrders(Integer accountId) {
        List<Order> orders = orderRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        return orders.stream()
                .map(Order::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse createOrder(Integer accountId, AddOrderRequest request) {

        // 1. Tính tổng tiền
        int totalAmount = request.getOrderItems().stream().mapToInt(item -> item.getPrice()).sum();


        // 2. Tạo Order
        Order order = new Order();
        order.setAccountId(accountId);
        order.setTotalAmount(totalAmount);
        order.setShippingStatus(0);
        order.setPaymentStatus(0);
        order = orderRepository.save(order);

        // 3. Lưu OrderItems
        for (AddOrderItemRequest o : request.getOrderItems()) {
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
    public OrderResponse updateShippingStatus(Integer accountId, Integer id, UpdateOrderRequest request) {

        // 1. Lấy order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Cập nhật trạng thái
        if (request.getStatus() != null) {
            order.setShippingStatus(request.getStatus());
        }

        // 3. Lưu
        orderRepository.save(order);

        return order.convertToDto();
    }

    @Transactional
    public void updatePaymentStatus(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setPaymentStatus(1);
        orderRepository.save(order);
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
        if (!(order.getShippingStatus() == 1)) {
            throw new RuntimeException("Chỉ đơn hàng ở trạng thái Đang lấy hàng mới được hủy");
        }

        // 4. Cập nhật trạng thái
        order.setShippingStatus(0);
        orderRepository.save(order);

        return order.convertToDto();
    }

    public OrderDetailResponse getOrderDetail(Integer id) {
        // Lấy ra thông tin order từ database
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // Lấy thông tin về profile người dùng mua hàng
        ProfileResponse profileResponse = profileClient.getProfileByAccount(order.getAccountId());

        // Lấy ra các thông tin chi tiết về sản phẩm trong đơn hàng (hình ảnh sản phẩm, tên sản phẩm,...) dựa vào variant_id
        List<Integer> ids = new ArrayList<>();
        for(OrderItem item : order.getOrderItems()) {
            ids.add(item.getVariantId());
        }
        System.out.println("Variant Ids: " + ids);
        List<ProductResponse> productResponseList = productClient.getProductsByIds(ids);

        System.out.println("Kết quả gọi API: " + !productResponseList.isEmpty());

        // Chuyển đổi đơn hàng hiện tại sang Dto
        OrderResponse orderResponse = order.convertToDto();

        // Gán thông tin sản phẩm chi tiết vào từng item trong dto
        for(int i = 0; i < productResponseList.size(); i++) {
            orderResponse.getOrderItems().get(i).setProduct(productResponseList.get(i));
        }

        // Tạo đối tượng trả về
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setProfile(profileResponse);
        orderDetailResponse.setOrder(orderResponse);

        return orderDetailResponse;
    }

    // Hàm xử lý kết quả thanh toán
    public PaymentResult processVnPayReturn(Map<String, String> params) {

        int result = vnPayService.verifyPayment(params);

        if (result == 1) { // Thành công
            Integer orderId = Integer.parseInt(params.get("vnp_TxnRef"));

            updatePaymentStatus(orderId);

            return new PaymentResult(true, "Thanh toán thành công cho đơn hàng: " + orderId);
        }

        if (result == 0) {
            return new PaymentResult(false, "Thanh toán thất bại hoặc bị hủy bỏ");
        }

        return new PaymentResult(false, "Lỗi xác thực chữ ký");
    }

    public List<ProductResponse> test(List<Integer> ids) {
        return productClient.getProductsByIds(ids);
    }
}
