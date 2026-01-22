package com.tqk.orderservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayService {

    @Value("${vnpay.tmn-code}")
    private String tmnCode;

    @Value("${vnpay.hash-secret}")
    private String hashSecret;

    @Value("${vnpay.url}")
    private String vnpUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    public String createPaymentUrl(String orderCode, int amount) {
        try {
            int finalAmount = amount * 100;

            Map<String, String> params = new TreeMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", tmnCode);
            params.put("vnp_Amount", String.valueOf(finalAmount));
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", orderCode);
            params.put("vnp_OrderInfo", "Thanh toan don hang " + orderCode);
            params.put("vnp_OrderType", "other");
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", returnUrl);

            TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(tz);

            params.put("vnp_CreateDate", sdf.format(new Date()));

            Calendar expire = Calendar.getInstance(tz);
            expire.add(Calendar.MINUTE, 15);
            params.put("vnp_ExpireDate", sdf.format(expire.getTime()));

            params.put("vnp_IpAddr", "127.0.0.1");

            // Build queryString (encoded)
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                queryString.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .append("&");
            }
            queryString.setLength(queryString.length() - 1); // Remove last '&'

            // SIGN ON ENCODED QUERY → GIỐNG C#
            String signData = queryString.toString();
            String secureHash = hmacSHA512(hashSecret, signData);

            return vnpUrl + "?" + signData + "&vnp_SecureHash=" + secureHash;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo VNPAY URL", e);
        }
    }

    public int verifyPayment(Map<String, String> params) {
        try {
            // Lấy vnp_SecureHash từ params và xóa nó khỏi map để tính toán hash
            String vnp_SecureHash = params.get("vnp_SecureHash");
            if (vnp_SecureHash == null) {
                return 0; // Không có hash để kiểm tra
            }

            // Tạo bản sao để xử lý, tránh ảnh hưởng map gốc
            Map<String, String> vnp_Params = new TreeMap<>(params);
            vnp_Params.remove("vnp_SecureHash");
            vnp_Params.remove("vnp_SecureHashType");

            // Build lại chuỗi hash dữ liệu (giống hệt lúc tạo URL thanh toán)
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty()) {
                    queryString.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                            .append("&");
                }
            }
            // Xóa dấu & cuối cùng
            if (!queryString.isEmpty()) {
                queryString.setLength(queryString.length() - 1);
            }

            // Hash chuỗi vừa tạo
            String signValue = hmacSHA512(hashSecret, queryString.toString());

            // So sánh hash tính được với hash nhận được
            if (signValue.equals(vnp_SecureHash)) {
                // Checksum hợp lệ, tiếp tục kiểm tra trạng thái giao dịch
                if ("00".equals(params.get("vnp_ResponseCode"))) {
                    return 1; // Thanh toán thành công
                } else {
                    return 0; // Giao dịch thất bại / Hủy bỏ
                }
            } else {
                return -1; // Chữ ký không hợp lệ (Có thể bị giả mạo)
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(secretKey);
        byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder(hashBytes.length * 2);
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}