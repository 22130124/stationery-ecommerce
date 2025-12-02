package com.tqk.authservice.model;

import com.tqk.authservice.dto.response.AccountResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "active_status")
    private boolean activeStatus;

    private boolean verified;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public AccountResponse convertToDto() {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(id);
        accountResponse.setEmail(email);
        accountResponse.setRole(role);
        accountResponse.setActiveStatus(activeStatus);
        accountResponse.setVerified(verified);
        accountResponse.setCreatedAt(createdAt);
        accountResponse.setUpdatedAt(updatedAt);
        return accountResponse;
    }
}
