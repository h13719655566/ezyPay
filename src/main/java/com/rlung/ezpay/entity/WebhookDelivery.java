package com.rlung.ezpay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "webhook_delivery",
        indexes = {
                @Index(name = "idx_delivery_retry", columnList = "success, next_retry_at")
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long endpointId;

    @Column(nullable = false)
    private String paymentId;

    // payment.created etc
    @Column(nullable = false)
    private String eventType;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private Integer attempt;

    private Integer statusCode;

    @Lob
    private String responseBody;

    @Column(nullable = false)
    private Boolean success;

    private LocalDateTime nextRetryAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
