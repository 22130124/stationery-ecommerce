package com.tqk.cartservice.model;


import com.tqk.cartservice.dto.response.cart.CartItemResponse;
import com.tqk.cartservice.dto.response.product.ProductVariantResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "variant_id")
    private Integer variantId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "added_at")
    @LastModifiedDate
    private LocalDateTime addedAt;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItemResponse convertToDto() {
        CartItemResponse dto = new CartItemResponse();
        dto.setId(this.id);
        dto.setProductId(this.productId);
        dto.setVariantId(this.variantId);
        dto.setQuantity(this.quantity);
        return dto;
    }
}

