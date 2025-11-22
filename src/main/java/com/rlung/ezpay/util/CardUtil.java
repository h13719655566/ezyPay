package com.rlung.ezpay.util;

public class CardUtil {

    public static String extractLast4(String cardNumber) {
        if (cardNumber != null) {
            cardNumber = cardNumber.trim();
        }

        if (cardNumber == null || cardNumber.length() < 4) {
            throw new IllegalArgumentException("Invalid card number for masking");
        }

        return cardNumber.substring(cardNumber.length() - 4);
    }
}
