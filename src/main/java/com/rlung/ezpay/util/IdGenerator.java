package com.rlung.ezpay.util;

import java.util.UUID;

public class IdGenerator {

    public static String generatePaymentId() {
        String random = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 18);

        return "pay_" + random;
    }
}
