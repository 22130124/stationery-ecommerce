package com.tqk.orderservice.model;

import com.tqk.orderservice.dto.response.OrderItemResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "variant_id")
    private Integer variantId;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public OrderItemResponse convertToDto() {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setId(id);
        orderItemResponse.setProductId(productId);
        orderItemResponse.setVariantId(variantId);
        orderItemResponse.setPrice(price);
        orderItemResponse.setQuantity(quantity);
        orderItemResponse.setCreatedAt(createdAt);
        orderItemResponse.setUpdatedAt(updatedAt);
        return orderItemResponse;
    }
}
