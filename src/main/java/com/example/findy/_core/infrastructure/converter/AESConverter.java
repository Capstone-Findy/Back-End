package com.example.findy._core.infrastructure.converter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.example.findy._core.environment.SecurityProperties;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AESConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES";
    private final byte[] secretKeyBytes;

    public AESConverter(SecurityProperties securityProperties) {
        // Base64로 인코딩된 키를 디코딩하여 바이트 배열로 저장
        this.secretKeyBytes = Base64.getDecoder().decode(securityProperties.encryption().secretKey());
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return attribute;
        }
        return encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return dbData;
        }
        return decrypt(dbData);
    }

    private String encrypt(String value) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKeyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting value", e);
        }
    }

    private String decrypt(String value) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKeyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(value));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting value", e);
        }
    }
} 