package com.tqk.orderservice.repository;

import com.tqk.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByAccountIdOrderByCreatedAtDesc(Integer accountId);

    Optional<Order> findByCode(String orderId);
    /**
     * Tính doanh thu theo ngày
     * Gộp các đơn hàng PAID theo từng ngày và tính tổng tiền mỗi ngày
     */
    @Query("""
                SELECT FUNCTION('DATE', o.createdAt), SUM(o.totalAmount)
                FROM Order o
                WHERE o.shippingStatus NOT IN (:excludedStatuses)
                  AND o.createdAt >= :from
                GROUP BY FUNCTION('DATE', o.createdAt)
                ORDER BY FUNCTION('DATE', o.createdAt)
            """)
    List<Object[]> getDailyRevenue(@Param("from") LocalDateTime from,
                                   @Param("excludedStatuses") List<Order.ShippingStatus> excludedStatuses);

    /**
     * Tính doanh thu theo tháng
     * Gộp doanh thu theo từng tháng
     * %Y-%m: Vd: 2026-01, 2026-02,...
     */
    @Query("""
                SELECT FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m'), SUM(o.totalAmount)
                FROM Order o
                WHERE o.shippingStatus NOT IN (:excludedStatuses)
                  AND o.createdAt >= :from
                GROUP BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')
                ORDER BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')
            """)
    List<Object[]> getMonthlyRevenue(@Param("from") LocalDateTime from,
                                     @Param("excludedStatuses") List<Order.ShippingStatus> excludedStatuses);

    /**
     * Lấy danh sách sản phẩm kèm số lượng bán được
     * Phục vụ cho chức năng lấy ra top N sản phẩm bán chạy
     */
    @Query("""
                SELECT oi.productId, SUM(oi.quantity)
                FROM OrderItem oi
                JOIN oi.order o
                WHERE o.shippingStatus NOT IN (:excludedStatuses)
                  AND o.createdAt >= :from
                GROUP BY oi.productId
                ORDER BY SUM(oi.quantity) DESC
            """)
    List<Object[]> getTotalSoldQuantityByProduct(@Param("from") LocalDateTime from,
                                                 @Param("excludedStatuses") List<Order.ShippingStatus> excludedStatuses);

    /**
     * Tính tổng doanh thu theo khoảng thời gian (lấy ra doanh thu hôm nay, hôm qua,...)
     */
    @Query("""
                SELECT COALESCE(SUM(o.totalAmount), 0)
                FROM Order o
                WHERE o.shippingStatus NOT IN (:excludedStatuses)
                  AND o.createdAt >= :from
                  AND o.createdAt < :to
            """)
    long sumRevenueBetween(@Param("from") LocalDateTime from,
                           @Param("to") LocalDateTime to,
                           @Param("excludedStatuses") List<Order.ShippingStatus> excludedStatuses);

    /**
     * Tính tổng số đơn hàng theo khoảng thời gian (lấy ra tổng đơn hôm nay, hôm qua,...)
     */
    @Query("""
                SELECT COUNT(o)
                FROM Order o
                WHERE o.shippingStatus NOT IN (:excludedStatuses)
                  AND o.createdAt >= :from
                  AND o.createdAt < :to
            """)
    long countOrdersBetween(@Param("from") LocalDateTime from,
                            @Param("to") LocalDateTime to,
                            @Param("excludedStatuses") List<Order.ShippingStatus> excludedStatuses);
}
