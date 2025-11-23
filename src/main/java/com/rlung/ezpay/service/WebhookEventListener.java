package com.rlung.ezpay.service;

import com.rlung.ezpay.entity.Payment;
import com.rlung.ezpay.event.PaymentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookEventListener {

    private final WebhookTaskService taskService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCreated(PaymentCreatedEvent event) {
        Payment p = event.getPayment();
        log.info("[WebhookEventListener] AFTER_COMMIT PaymentCreatedEvent received. paymentId={}",
                p.getPaymentId());

        taskService.createWebhookTasks(p);
    }
}
