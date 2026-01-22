package com.tqk.productservice.model.inventory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_reservations")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class StockReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "variant_id")
    private Integer variantId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private StockReservationStatus status;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public enum StockReservationStatus {
        RESERVED,
        CONFIRMED,
        RELEASED
    }
}
