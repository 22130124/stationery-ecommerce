package com.tqk.orderservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "shipping_status")
    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public enum ShippingStatus {
        PENDING,
        WAITING_PAYMENT,
        READY_TO_PICK,
        SHIPPING,
        DELIVERED,
        CANCELLED,
        EXPIRED
    }

    public enum PaymentStatus {
        UNPAID,
        PAID
    }

    // Hàm tạo ra mã code của đơn hàng
    @PrePersist
    public void generateOrderCode() {
        if (this.code == null) {
            String date = java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
            String random = java.util.UUID.randomUUID()
                    .toString()
                    .substring(0, 6)
                    .toUpperCase();

            this.code = "HD-" + date + "-" + random;
        }
    }
}
