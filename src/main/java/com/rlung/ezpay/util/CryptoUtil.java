package com.rlung.ezpay.util;

import com.rlung.ezpay.exception.EncryptionException; // 匯入剛剛建立的 Exception

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class CryptoUtil {

    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    private static final int AES_KEY_SIZE = 256;

    public static String generateKeyBase64() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(AES_KEY_SIZE);
        SecretKey key = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String encrypt(String plainText, String base64Key) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            SecretKeySpec key = new SecretKeySpec(keyBytes, AES);

            byte[] iv = SecureRandomUtil.randomBytes(IV_LENGTH);
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());

            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new EncryptionException("Error occurred while encrypting data", e);
        }
    }

    public static String decrypt(String cipherTextBase64, String base64Key) {
        try {
            byte[] combined = Base64.getDecoder().decode(cipherTextBase64);
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            SecretKeySpec key = new SecretKeySpec(keyBytes, AES);

            byte[] iv = new byte[IV_LENGTH];
            // avoid ArrayOutBound
            if (combined.length < IV_LENGTH) {
                throw new IllegalArgumentException("Invalid encrypted data format");
            }

            byte[] cipherText = new byte[combined.length - IV_LENGTH];

            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] decrypted = cipher.doFinal(cipherText);

            return new String(decrypted);

        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new EncryptionException("Error occurred while decrypting data", e);
        }
    }
}