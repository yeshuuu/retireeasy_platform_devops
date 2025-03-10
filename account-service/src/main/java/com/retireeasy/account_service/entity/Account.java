package com.retireeasy.account_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long userId;
    private String accountType;
    private String planName;
    private Double balance = 0.0;
    private String status = "ACTIVE";
}