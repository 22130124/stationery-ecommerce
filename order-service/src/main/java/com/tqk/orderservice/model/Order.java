package com.tqk.orderservice.model;

import com.tqk.orderservice.dto.response.OrderResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public OrderResponse convertToDto() {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(id);
        orderResponse.setAccountId(accountId);
        orderResponse.setTotalAmount(totalAmount);
        orderResponse.setStatus(status);
        orderResponse.setCreatedAt(createdAt);
        orderResponse.setUpdatedAt(updatedAt);
        orderResponse.setOrderItems(orderItems.stream().map(OrderItem::convertToDto).collect(Collectors.toList()));
        return orderResponse;
    }
}
