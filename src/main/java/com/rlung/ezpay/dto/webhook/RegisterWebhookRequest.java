package com.rlung.ezpay.dto.webhook;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request body for registering a webhook endpoint.")
public class RegisterWebhookRequest {

    @Schema(
            description = "The URL your server will receive webhook POST requests on.",
            example = "https://myserver.com/webhook",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "URL must not be blank")
    private String url;
}
