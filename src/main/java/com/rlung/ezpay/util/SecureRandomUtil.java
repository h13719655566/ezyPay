package com.rlung.ezpay.util;

import java.security.SecureRandom;

public class SecureRandomUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
}
