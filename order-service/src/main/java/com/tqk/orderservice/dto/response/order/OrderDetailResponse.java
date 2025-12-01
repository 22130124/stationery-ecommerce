package com.tqk.orderservice.dto.response.order;

import com.tqk.orderservice.dto.response.profile.ProfileResponse;
import lombok.Data;

@Data
public class OrderDetailResponse {
    private OrderResponse order;
    private ProfileResponse profile;
}
