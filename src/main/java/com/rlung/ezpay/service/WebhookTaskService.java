package com.rlung.ezpay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlung.ezpay.entity.Payment;
import com.rlung.ezpay.entity.WebhookDelivery;
import com.rlung.ezpay.entity.WebhookEndpoint;
import com.rlung.ezpay.repo.WebhookDeliveryRepository;
import com.rlung.ezpay.repo.WebhookEndpointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookTaskService {

    private final WebhookEndpointRepository endpointRepo;
    private final WebhookDeliveryRepository deliveryRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createWebhookTasks(Payment p) {

        List<WebhookEndpoint> endpoints = endpointRepo.findAll();
        if (endpoints.isEmpty()) {
            log.info("[WebhookTaskService] No endpoints registered, skipping.");
            return;
        }

        String payloadJson;
        try {
            payloadJson = mapper.writeValueAsString(
                    new Payload(p.getPaymentId(), p.getAmount(), p.getCurrency())
            );
        } catch (Exception e) {
            log.error("[WebhookTaskService] Failed to serialize payload", e);
            return;
        }

        // 3. 建立 tasks
        List<WebhookDelivery> tasks = new ArrayList<>();
        for (WebhookEndpoint ep : endpoints) {
            tasks.add(
                    WebhookDelivery.builder()
                            .endpointId(ep.getId())
                            .paymentId(p.getPaymentId())
                            .eventType("payment.created")
                            .payload(payloadJson)
                            .attempt(0)
                            .success(false)
                            .nextRetryAt(LocalDateTime.now())
                            .build()
            );
        }

        deliveryRepo.saveAll(tasks);
        log.info("[WebhookTaskService] {} webhook tasks created for {}", tasks.size(), p.getPaymentId());
    }

    private record Payload(String paymentId, Long amount, String currency) {}
}
