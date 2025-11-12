package com.tqk.stationeryecommercebackend.service;

import com.tqk.stationeryecommercebackend.dto.order.requests.OrderItemRequest;
import com.tqk.stationeryecommercebackend.dto.order.requests.OrderRequest;
import com.tqk.stationeryecommercebackend.dto.order.responses.OrderItemResponse;
import com.tqk.stationeryecommercebackend.dto.order.responses.OrderResponse;
import com.tqk.stationeryecommercebackend.exception.ProductNotFoundException;
import com.tqk.stationeryecommercebackend.model.*;
import com.tqk.stationeryecommercebackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        ProductVariantRepository variantRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setStatus("pending");
        order.setPaymentStatus("unpaid");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequest itemReq : orderRequest.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemReq.getProductId()));

            ProductVariant variant = null;
            double unitPrice;

            if (itemReq.getVariantId() != null) {
                variant = variantRepository.findById(itemReq.getVariantId())
                        .orElseThrow(() -> new ProductNotFoundException("Variant not found: " + itemReq.getVariantId()));
                unitPrice = variant.getDiscountPrice() != null ? variant.getDiscountPrice() : variant.getBasePrice();
            } else {
                unitPrice = product.getVariants().stream()
                        .filter(ProductVariant::getIsDefault)
                        .findFirst()
                        .map(v -> v.getDiscountPrice() != null ? v.getDiscountPrice() : v.getBasePrice())
                        .orElseThrow(() -> new ProductNotFoundException("No default variant for product: " + product.getId()));
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setVariant(variant);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setTotalPrice(unitPrice * itemReq.getQuantity());

            orderItems.add(orderItem);
            totalAmount += unitPrice * itemReq.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return convertToDto(savedOrder);
    }

    private OrderResponse convertToDto(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setStatus(order.getStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        dto.setCustomer(order.getCustomer().convertToDto());

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse itemDto = new OrderItemResponse();
            itemDto.setId(item.getId());
            itemDto.setProduct(item.getProduct().convertToDto());
            if (item.getVariant() != null) itemDto.setVariant(item.getVariant().convertToDto());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setTotalPrice(item.getTotalPrice());
            itemResponses.add(itemDto);
        }
        dto.setOrderItems(itemResponses);

        dto.setPayments(new ArrayList<>());

        return dto;
    }
}
