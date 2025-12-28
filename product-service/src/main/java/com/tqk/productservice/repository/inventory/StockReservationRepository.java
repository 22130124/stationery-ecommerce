package com.tqk.productservice.repository.inventory;

import com.tqk.productservice.model.inventory.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockReservationRepository extends JpaRepository<StockReservation, Integer> {
    List<StockReservation> findByOrderId(Integer orderId);

    boolean existsByOrderIdAndVariantId(Integer orderId, Integer variantId);

    @Modifying
    @Query("""
        update StockReservation
        set status = 'CONFIRMED'
        where orderId = :orderId
        and status = 'RESERVED'
        """)
    void confirmByOrderId(@Param("orderId") Integer orderId);

    @Modifying
    @Query("""
        update StockReservation
        set status = 'RELEASED'
        where orderId = :orderId
        and status = 'RESERVED'
        """)
    void releaseByOrderId(@Param("orderId") Integer orderId);

    @Query("""
        SELECT r
        FROM StockReservation r
        WHERE r.status = :status
          AND r.expiresAt < :now
    """)
    List<StockReservation> findExpiredReservations(
            @Param("status") StockReservation.StockReservationStatus status,
            @Param("now") LocalDateTime now
    );
}
