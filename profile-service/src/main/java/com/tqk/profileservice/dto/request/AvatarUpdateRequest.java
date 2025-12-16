package com.tqk.profileservice.dto.request;

import lombok.Data;

@Data
public class AvatarUpdateRequest {
    private String avatarUrl;
    private String avatarPublicId;
}
