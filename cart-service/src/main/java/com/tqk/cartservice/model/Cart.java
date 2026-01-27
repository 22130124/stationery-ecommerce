package com.tqk.cartservice.model;

import com.tqk.cartservice.dto.response.cart.CartItemResponse;
import com.tqk.cartservice.dto.response.cart.CartResponse;
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
@Table(name = "carts")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();

    public CartResponse convertToDto() {
        CartResponse dto = new CartResponse();
        dto.setId(this.id);
        dto.setAccountId(this.accountId);
        for (CartItem item : this.items) {
            dto.getItems().add(item.convertToDto());
        }
        return dto;
    }
}

