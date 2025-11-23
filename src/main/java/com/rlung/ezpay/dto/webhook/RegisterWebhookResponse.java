package com.rlung.ezpay.dto.webhook;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response returned after successfully registering a webhook endpoint.")
public class RegisterWebhookResponse {

    @Schema(
            description = "Internal ID for this webhook endpoint."
    )
    private Long id;

    @Schema(
            description = "The URL that will receive webhook POST requests.",
            example = "https://merchant.example.com/webhook"
    )
    private String url;

    @Schema(
            description = "A Base64-encoded secret used to verify HMAC signatures on webhook events.",
            example = "AVxk9lj2Q49pR9uXKj48z1s3dLr09asdf89Q=="
    )
    private String secret;
}
