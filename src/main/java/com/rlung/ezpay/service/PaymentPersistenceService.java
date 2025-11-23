package com.rlung.ezpay.service;

import com.rlung.ezpay.entity.Payment;
import com.rlung.ezpay.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentPersistenceService {

    private final PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Payment saveInNewTransaction(Payment payment) {
        return paymentRepository.saveAndFlush(payment);
    }
}