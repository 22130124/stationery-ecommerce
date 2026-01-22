package com.tqk.orderservice.service;

import com.tqk.orderservice.dto.request.inventory.ReserveRequest;
import com.tqk.orderservice.dto.request.order.AddOrderItemRequest;
import com.tqk.orderservice.dto.request.order.AddOrderRequest;
import com.tqk.orderservice.dto.request.order.UpdateOrderRequest;
import com.tqk.orderservice.dto.response.order.OrderDetailResponse;
import com.tqk.orderservice.dto.response.order.OrderItemResponse;
import com.tqk.orderservice.dto.response.order.OrderResponse;
import com.tqk.orderservice.dto.response.payment.PaymentResult;
import com.tqk.orderservice.dto.response.product.ProductResponse;
import com.tqk.orderservice.dto.response.profile.ProfileResponse;
import com.tqk.orderservice.exception.OutOfStockException;
import com.tqk.orderservice.model.Order;
import com.tqk.orderservice.model.OrderItem;
import com.tqk.orderservice.repository.OrderItemRepository;
import com.tqk.orderservice.repository.OrderRepository;
import com.tqk.orderservice.repository.client.CartClient;
import com.tqk.orderservice.repository.client.InventoryClient;
import com.tqk.orderservice.repository.client.ProductClient;
import com.tqk.orderservice.repository.client.ProfileClient;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;

import static com.tqk.orderservice.model.Order.PaymentStatus.PAID;
import static com.tqk.orderservice.model.Order.PaymentStatus.UNPAID;
import static com.tqk.orderservice.model.Order.ShippingStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartClient cartClient;
    private final ProfileClient profileClient;
    private final VnPayService vnPayService;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    // Hàm lấy ra danh sách đơn hàng theo account id/null nếu muốn lấy tất cả đơn hàng trong hệ thống
    public List<OrderDetailResponse> getOrders(Integer accountId) {
        List<Order> orders;

        // Nếu không truyền vào accountId, mặc định lấy ra danh sách tất cả đơn hàng
        if (accountId == null) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        }

        // Nếu orders rỗng thì trả về kết quả ngay
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        // Nếu orders không rỗng thì tiến hành convert sang dto để trả về kết quả
        List<OrderResponse> orderResponseList = new ArrayList<>();
        return convertOrdersToDto(orders);
    }

    // Hàm lấy ra thông tin chi tiết của một đơn hàng
    public OrderDetailResponse getOrderDetail(String orderCode) {
        // Lấy ra thông tin order từ database
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // Lấy thông tin về profile người dùng mua hàng
        ProfileResponse profileResponse = profileClient.getProfileByAccount(order.getAccountId());

        // Lấy ra các thông tin chi tiết về sản phẩm trong đơn hàng (hình ảnh sản phẩm, tên sản phẩm,...) dựa vào variant_id
        List<Integer> ids = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            ids.add(item.getVariantId());
        }
        List<ProductResponse> productResponseList = productClient.getProductsByVariantIds(ids);

        // Chuyển đổi đơn hàng hiện tại sang Dto
        OrderResponse orderResponse = convertOrderToDto(order);

        // Gán thông tin sản phẩm chi tiết vào từng item trong dto
        for (int i = 0; i < productResponseList.size(); i++) {
            orderResponse.getOrderItems().get(i).setProduct(productResponseList.get(i));
        }

        // Tạo đối tượng trả về
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setProfile(profileResponse);
        orderDetailResponse.setOrder(orderResponse);

        return orderDetailResponse;
    }

    // Hàm xử lý tạo một đơn hàng mới
    @Transactional
    public OrderResponse createOrder(Integer accountId, AddOrderRequest request) {
        // 1. Tính tổng tiền
        // Đồng thời tạo Map items phục vụ cho việc gọi inventory client để giữ số lượng trong kho
        // Đồng thời tạo danh sách các variantIds để phục vụ resetCart sau khi tạo đơn hàng
        int totalAmount = 0;
        Map<Integer, Integer> mapItems = new HashMap<>();

        List<Integer> variantIds = new ArrayList<>();
        for (AddOrderItemRequest orderItem : request.getOrderItems()) {
            totalAmount += orderItem.getPrice() * orderItem.getQuantity();
            mapItems.put(orderItem.getVariantId(), orderItem.getQuantity());
            variantIds.add(orderItem.getVariantId());
        }

        // 2. Tạo Order
        Order order = new Order();
        order.setAccountId(accountId);
        order.setTotalAmount(totalAmount);
        order.setShippingStatus(READY_TO_PICK);
        order.setPaymentStatus(UNPAID);
        order = orderRepository.save(order);

        // 3. Tạo reserve request để yêu cầu kho hàng giữ số lượng sản phẩm cho đơn hàng này
        ReserveRequest reserveRequest = new ReserveRequest();
        reserveRequest.setOrderCode(order.getCode());
        reserveRequest.setItems(mapItems);
        try {
            // 4. Gọi API đến kho hàng để yêu cầu giữ sản phẩm để chờ thanh toán
            inventoryClient.reserve(reserveRequest);

            // Cập nhật trạng thái đơn hàng thành ready-to-pick
            order.setShippingStatus(WAITING_PAYMENT);

            // 5. Lưu thông tin các items
            for (AddOrderItemRequest o : request.getOrderItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductId(o.getProductId());
                orderItem.setVariantId(o.getVariantId());
                orderItem.setPrice(o.getPrice());
                orderItem.setQuantity(o.getQuantity());
                orderItemRepository.save(orderItem);
            }

            // 6. Reset lại giỏ hàng
            cartClient.resetCart(accountId, variantIds);

            // 7. Trả về response
            return convertOrderToDto(order);
        } catch (FeignException.Conflict e) { // Kho hàng không đủ số lượng
            // Hủy bỏ đơn hàng hiện tại
            orderRepository.delete(order);
            throw new OutOfStockException("Số lượng sản phẩm không đủ");
        } catch (FeignException e) { // Lỗi server
            orderRepository.delete(order);
            throw new RuntimeException("Lỗi khi xử lý ở kho hàng");
        }
    }


    // Hàm cập nhật trạng thái giao hàng
    @Transactional
    public OrderResponse updateShippingStatus(String orderCode, UpdateOrderRequest request) {
        // 1. Lấy order
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Cập nhật trạng thái
        switch (request.getStatus()) {
            case "SHIPPING":
                order.setShippingStatus(SHIPPING);
                break;
            case "DELIVERED":
                order.setShippingStatus(DELIVERED);
                break;
            case "CANCELLED":
                inventoryClient.release(order.getCode());
                order.setShippingStatus(CANCELLED);
                break;
        }

        // 3. Lưu
        orderRepository.save(order);

        return convertOrderToDto(order);
    }

    // Hàm cập nhật trạng thái đơn hàng khi thanh toán thành công
    @Transactional
    public void updatePaymentSucess(String orderCode) {
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        inventoryClient.confirm(order.getCode());

        order.setPaymentStatus(PAID);
        order.setShippingStatus(READY_TO_PICK);
        orderRepository.save(order);
    }

    // Hàm xử lý hủy đơn hàng
    @Transactional
    public OrderResponse cancelOrder(Integer accountId, String orderCode) {
        // 1. Lấy order
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Kiểm tra quyền sở hữu
        if (!order.getAccountId().equals(accountId)) {
            throw new RuntimeException("Không có quyền hủy đơn này");
        }

        // 3. Chỉ cho hủy khi đang đang lấy hàng hoặc chờ thanh toán
        if (order.getShippingStatus() != READY_TO_PICK && order.getShippingStatus() != WAITING_PAYMENT) {
            throw new RuntimeException("Trạng thái hiện tại không thể hủy đơn");
        }

        // 4. Gọi tới inventory để release số lượng
        inventoryClient.release(order.getCode());

        // 5. Cập nhật trạng thái
        order.setShippingStatus(CANCELLED);
        orderRepository.save(order);

        return convertOrderToDto(order);
    }

    // Hàm tạo yêu cầu thanh toán
    public String createPaymentUrl(String orderCode) {
        // Lấy thông tin đơn hàng
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng có code là " + orderCode));

        // số tiền thanh toán
        int amount = order.getTotalAmount();

        return vnPayService.createPaymentUrl(orderCode, amount);
    }

    // Hàm xử lý kết quả thanh toán
    @Transactional
    public PaymentResult processVnPayReturn(Map<String, String> params) {

        int result = vnPayService.verifyPayment(params);

        if (result == 1) { // Thành công
            String orderCode = params.get("vnp_TxnRef");

            updatePaymentSucess(orderCode);

            return new PaymentResult(true, "Thanh toán thành công cho đơn hàng: " + orderCode);
        }

        if (result == 0) {
            return new PaymentResult(false, "Thanh toán thất bại hoặc bị hủy bỏ");
        }

        return new PaymentResult(false, "Lỗi xác thực chữ ký");
    }

    // Hàm cập nhật trạng thái hết hạn cho đơn hàng
    @Transactional
    public void setExpired(String orderCode) {
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng có code là " + orderCode));
        order.setShippingStatus(EXPIRED);
        orderRepository.save(order);
    }

    // Hàm chuyển đổi đơn hàng sang dto
    private OrderResponse convertOrderToDto(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setCode(order.getCode());
        orderResponse.setAccountId(order.getAccountId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setShippingStatus(order.getShippingStatus().name());
        orderResponse.setPaymentStatus(order.getPaymentStatus().name());
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setCreatedAt(
                order.getCreatedAt()
                        .atZone(ZoneId.of("UTC"))
                        .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                        .toLocalDateTime()
        );
        orderResponse.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemResponse> orederItemList = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = convertOrderItemToDto(orderItem);
            orederItemList.add(orderItemResponse);
        }
        orderResponse.setOrderItems(orederItemList);

        return orderResponse;
    }

    // Hàm chuyển đổi item trong đơn hàng sang dto
    private OrderItemResponse convertOrderItemToDto(OrderItem orderItem) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setPrice(orderItem.getPrice());
        dto.setQuantity(orderItem.getQuantity());
        dto.setCreatedAt(orderItem.getCreatedAt());
        dto.setUpdatedAt(orderItem.getUpdatedAt());
        return dto;
    }

    // Hàm tạo danh sách List<OrderDetailResponse>
    private List<OrderDetailResponse> convertOrdersToDto(List<Order> orders) {
        List<OrderDetailResponse> response = new ArrayList<>();
        for (Order order : orders) {
            response.add(getOrderDetail(order.getCode()));
        }
        return response;
    }
}
