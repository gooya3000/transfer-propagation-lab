package com.example.transferservice.transfer.dto;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public record TransferCommand(
        String idempotencyKey,
        String senderAccountNo,
        String receiverAccountNo,
        BigDecimal amount
) {

    public TransferCommand {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (senderAccountNo != null && senderAccountNo.equals(receiverAccountNo)) {
            throw new IllegalArgumentException("sender and receiver must be different");
        }
    }

    public String payloadHash() {
        String payload = senderAccountNo + "|" + receiverAccountNo + "|" + amount.toPlainString();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.toHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    private static final class HexFormat {

        private static final char[] HEX = "0123456789abcdef".toCharArray();

        private HexFormat() {
        }

        private static String toHex(byte[] bytes) {
            char[] result = new char[bytes.length * 2];
            for (int i = 0; i < bytes.length; i++) {
                int value = bytes[i] & 0xff;
                result[i * 2] = HEX[value >>> 4];
                result[i * 2 + 1] = HEX[value & 0x0f];
            }
            return new String(result);
        }
    }
}
