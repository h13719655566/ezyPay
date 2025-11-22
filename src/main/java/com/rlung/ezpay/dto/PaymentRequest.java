package com.rlung.ezpay.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String firstName;
    private String lastName;
    private String zipCode;
    private String cardNumber;
    private Long amount;     // smallest unit (e.g. 100 = $1.00)
    private String currency; // "USD", "AUD", "TWD"
}
