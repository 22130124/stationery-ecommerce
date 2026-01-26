package com.tqk.profileservice.dto.response;

import com.tqk.profileservice.model.Profile;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileResponse {
    private Integer id;
    private Integer accountId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatarUrl;
    private String avatarPublicId;
    private Profile.ProfileStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
