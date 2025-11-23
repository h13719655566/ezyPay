package com.rlung.ezpay.service;

import com.rlung.ezpay.entity.Payment;
import com.rlung.ezpay.repo.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class PaymentRetryTest {

    @Autowired
    private PaymentRepository repo;

    @Autowired
    private PaymentPersistenceService persistenceService;

    @AfterEach
    public void tearDown() {
        repo.deleteAll();
    }

    /**
     * This test performs the following:
     * 1. Inserts the first Payment record (paymentId = "pay_test") → success
     * 2. Attempts to insert another Payment with the same paymentId → unique constraint violation
     * 3. Tries again with a new paymentId ("pay_test2") → expected to succeed
     *
     * If Hibernate marks the transaction as rollback-only, step #3 will fail.
     * Otherwise, step #3 should succeed.
     */

    @Test
    public void testRetryAfterDuplicateKey() {

        // Step 1: first insert
        Payment p1 = Payment.builder()
                .paymentId("pay_test")
                .firstName("A")
                .lastName("A")
                .zipCode("00000")
                .encryptedCardNumber("xxx")
                .last4("1234")
                .amount(100L)
                .currency("USD")
                .createdAt(LocalDateTime.now())
                .build();
        persistenceService.saveInNewTransaction(p1);


        // Step 2: second insert（unique key）
        boolean duplicateThrown = false;
        try {
            Payment p2 = Payment.builder()
                    .paymentId("pay_test")
                    .firstName("B")
                    .lastName("B")
                    .zipCode("00000")
                    .encryptedCardNumber("xxx")
                    .last4("5678")
                    .amount(100L)
                    .currency("USD")
                    .createdAt(LocalDateTime.now())
                    .build();

            persistenceService.saveInNewTransaction(p2);

        } catch (Exception e) {
            duplicateThrown = true;
        }

        assert duplicateThrown : "Expected duplicate key exception!";


        // Step 3: new paymentId（if Tx rollback-only，here going to show err）
        Payment p3 = Payment.builder()
                .paymentId("pay_test2")   // 新 ID
                .firstName("C")
                .lastName("C")
                .zipCode("00000")
                .encryptedCardNumber("xxx")
                .last4("9999")
                .amount(100L)
                .currency("USD")
                .createdAt(LocalDateTime.now())
                .build();

        persistenceService.saveInNewTransaction(p3);

        boolean exists = repo.existsByPaymentId("pay_test2");

        org.junit.jupiter.api.Assertions.assertTrue(exists, "new data should in db");

    }
}
