package com.rlung.ezpay.event;

import com.rlung.ezpay.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCreatedEvent {
    private final Payment payment;
}