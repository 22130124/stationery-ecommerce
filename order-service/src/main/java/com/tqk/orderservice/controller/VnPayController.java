package com.tqk.orderservice.controller;

import com.tqk.orderservice.dto.response.payment.PaymentResult;
import com.tqk.orderservice.service.OrderService;
import com.tqk.orderservice.service.VnPayService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VnPayController {
    private final OrderService orderService;
    private final VnPayService vnPayService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam Integer orderId) {
        String paymentUrl = orderService.createPaymentUrl(orderId);
        Map<String, String> response = Map.of("paymentUrl", paymentUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/return")
    public void handleVnPayReturn(
            @RequestParam Map<String, String> params,
            HttpServletResponse response
    ) throws IOException {

        PaymentResult result = orderService.processVnPayReturn(params);

        String feBaseUrl = "http://localhost:3000/notify";

        if (result.isSuccess()) {
            // Redirect success
            String redirectUrl =
                    feBaseUrl
                    + "?type=success"
                    + "&message=" + URLEncoder.encode(result.getMessage(), StandardCharsets.UTF_8)
                    + "&redirect=/order-history"
                    + "&sec=5";

            response.sendRedirect(redirectUrl);
        } else {
            // Redirect error
            String redirectUrl =
                    feBaseUrl
                    + "?type=error"
                    + "&message=" + URLEncoder.encode(result.getMessage(), StandardCharsets.UTF_8);

            response.sendRedirect(redirectUrl);
        }
    }
}
