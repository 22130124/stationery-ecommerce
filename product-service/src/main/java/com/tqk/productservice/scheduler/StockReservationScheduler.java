package com.tqk.productservice.scheduler;

import com.tqk.productservice.model.inventory.StockReservation;
import com.tqk.productservice.repository.client.OrderClient;
import com.tqk.productservice.repository.inventory.InventoryRepository;
import com.tqk.productservice.repository.inventory.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockReservationScheduler {
    private final StockReservationRepository stockReservationRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderClient orderClient;

    @Transactional
    @Scheduled(fixedDelay = 60000) // mỗi 60s
    public void releaseExpiredReservations() {
        // Lấy ra các reservations đã hết hạn
        List<StockReservation> expired =
                stockReservationRepository.findExpiredReservations(
                        StockReservation.StockReservationStatus.RESERVED,
                        LocalDateTime.now()
                );

        // Tăng lại số lượng trong kho
        for (StockReservation stockReservation : expired) {
            inventoryRepository.increaseStock(
                    stockReservation.getVariantId(),
                    stockReservation.getQuantity()
            );

            // Cập nhật lại trạng thái RELEASED cho reservation
            stockReservation.setStatus(StockReservation.StockReservationStatus.RELEASED);
            stockReservationRepository.save(stockReservation);

            // Cập nhật trạng thái hết hạn cho đơn hàng
            orderClient.setExpired(stockReservation.getOrderCode());
        }
    }
}

