package com.rlung.ezpay.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.CreditCardNumber;

@Data
public class PaymentRequest {

    @Schema(description = "Payer's first name", example = "John")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Payer's last name", example = "Doe")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Zip code", example = "2000")
    @NotBlank(message = "Zip code is required")
    @Size(min = 3, max = 10, message = "Zip code length must be between 3 and 10 characters")
    private String zipCode;

    @Schema(description = "Credit card number (plain text)", example = "4111111111111111")
    @NotBlank(message = "Card number is required")
    @CreditCardNumber(message = "Invalid credit card number")
    private String cardNumber;

    @Schema(description = "Payment amount in cents (e.g. 1000 = 10.00 AUD)", example = "1000")
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1 cent")
    private Long amount;

    @Schema(description = "Currency code (ISO 4217)", example = "AUD")
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "[A-Z]{3}", message = "Currency must be a 3-letter ISO code")
    private String currency;
}
