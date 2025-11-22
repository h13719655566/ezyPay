package com.rlung.ezpay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //pay_xxxxxxxxxxxxxxxx
    @Column(nullable = false, unique = true)
    private String paymentId; // public external id

    private String firstName;
    private String lastName;
    private String zipCode;
    private String last4;

    @Column(nullable = false, length = 512)
    private String encryptedCardNumber;

    private LocalDateTime createdAt;
}
