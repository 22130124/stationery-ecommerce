package com.tqk.productservice.repository.inventory;

import com.tqk.productservice.model.inventory.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockReservationRepository extends JpaRepository<StockReservation, Integer> {
    List<StockReservation> findByOrderCode(String orderCode);

    boolean existsByOrderCodeAndVariantId(String orderCode, Integer variantId);

    @Modifying
    @Query("""
        update StockReservation
        set status = 'CONFIRMED'
        where orderCode = :orderCode
        and status = 'RESERVED'
        """)
    void confirmByOrderCode(@Param("orderCode") String orderCode);

    @Modifying
    @Query("""
        update StockReservation
        set status = 'RELEASED'
        where orderCode = :orderCode
        and status = 'RESERVED'
        """)
    void releaseByOrderCode(@Param("orderCode") String orderCode);

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
