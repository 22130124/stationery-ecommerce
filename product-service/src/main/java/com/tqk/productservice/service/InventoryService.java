package com.tqk.productservice.service;

import com.tqk.productservice.dto.request.inventory.ReserveRequest;
import com.tqk.productservice.exception.OutOfStockException;
import com.tqk.productservice.model.inventory.StockReservation;
import com.tqk.productservice.repository.inventory.InventoryRepository;
import com.tqk.productservice.repository.inventory.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.tqk.productservice.model.inventory.StockReservation.StockReservationStatus.RESERVED;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final StockReservationRepository stockReservationRepository;

    @Transactional
    public void reserve(ReserveRequest request) {
        // Duyệt qua từng item
        Integer variantId, quantity;
        for (Map.Entry<Integer, Integer> entry : request.getItems().entrySet()) {
            // Lấy ra giá trị variantID
            variantId = entry.getKey();

            // Nếu đã reserve cho variantId của orderCode này thì không reserve lại nữa
            if (stockReservationRepository.existsByOrderCodeAndVariantId(request.getOrderCode(), variantId)) {
                continue; // đã reserve rồi
            }

            // Lấy ra giá trị quantity
            quantity = entry.getValue();

            // Lấy ra stock hiện tại trong kho
            Integer stock = inventoryRepository.findStockForUpdate(variantId);

            // Nếu không đủ số lượng thì ném lỗi
            if (stock < quantity) {
                throw new OutOfStockException("Số lượng sản phẩm không đủ");
            }

            // Nếu đủ số lượng thì trừ số lượng trong kho
            inventoryRepository.decreaseStock(variantId, quantity);

            // Giữ số lượng đã bị trừ cho đơn hàng hiện tại
            StockReservation stockReservation = new StockReservation();
            stockReservation.setOrderCode(request.getOrderCode());
            stockReservation.setVariantId(variantId);
            stockReservation.setQuantity(quantity);
            stockReservation.setStatus(RESERVED);
            stockReservation.setExpiresAt(LocalDateTime.now().plusMinutes(60));
            stockReservationRepository.save(stockReservation);
        }
    }

    @Transactional
    public void confirm(String orderCode) {
        stockReservationRepository.confirmByOrderCode(orderCode);
    }

    @Transactional
    public void release(String orderCode) {
        List<StockReservation> stockReservations = stockReservationRepository.findByOrderCode(orderCode);
        // Cộng lại số lượng đã vào kho ban đầu
        for (StockReservation stockReservation : stockReservations) {
            if (stockReservation.getStatus() == RESERVED) {
                Integer variantId = stockReservation.getVariantId();
                Integer quantity = stockReservation.getQuantity();
                inventoryRepository.increaseStock(variantId, quantity);
            }
        }

        // Cập nhật trạng thái released trong bảng stock reservations
        stockReservationRepository.releaseByOrderCode(orderCode);
    }
}
