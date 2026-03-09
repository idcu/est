package ltd.idcu.est.utils.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtils {

    private static final String MD5 = "MD5";
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";
    private static final String SHA_512 = "SHA-512";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private EncryptUtils() {
    }

    public static String md5(String input) {
        return hash(input, MD5);
    }

    public static String md5(byte[] input) {
        return hash(input, MD5);
    }

    public static String sha1(String input) {
        return hash(input, SHA_1);
    }

    public static String sha1(byte[] input) {
        return hash(input, SHA_1);
    }

    public static String sha256(String input) {
        return hash(input, SHA_256);
    }

    public static String sha256(byte[] input) {
        return hash(input, SHA_256);
    }

    public static String sha512(String input) {
        return hash(input, SHA_512);
    }

    public static String sha512(byte[] input) {
        return hash(input, SHA_512);
    }

    public static String hash(String input, String algorithm) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        return hash(input.getBytes(StandardCharsets.UTF_8), algorithm);
    }

    public static String hash(byte[] input, String algorithm) {
        if (input == null || input.length == 0) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = digest.digest(input);
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found: " + algorithm, e);
        }
    }

    public static String base64Encode(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        return base64Encode(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64Encode(byte[] input) {
        if (input == null || input.length == 0) {
            return "";
        }
        return Base64.getEncoder().encodeToString(input);
    }

    public static String base64Decode(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static byte[] base64DecodeToBytes(String input) {
        if (StringUtils.isEmpty(input)) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(input);
    }

    public static String base64UrlEncode(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        return base64UrlEncode(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64UrlEncode(byte[] input) {
        if (input == null || input.length == 0) {
            return "";
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    public static String base64UrlDecode(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        byte[] decodedBytes = Base64.getUrlDecoder().decode(input);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static String generateRandomString(int length) {
        if (length <= 0) {
            return "";
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(SECURE_RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static byte[] generateRandomBytes(int length) {
        if (length <= 0) {
            return new byte[0];
        }
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    public static String generateSalt(int length) {
        return generateRandomString(length);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
