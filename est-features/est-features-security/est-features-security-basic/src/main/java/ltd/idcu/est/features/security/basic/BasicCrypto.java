package ltd.idcu.est.features.security.basic;

import ltd.idcu.est.features.security.api.*;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class BasicCrypto implements Crypto {
    
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_GCM_ALGORITHM = "AES/GCM/NoPadding";
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int ITERATION_COUNT = 65536;
    
    private final SecretKey secretKey;
    private KeyPair keyPair;
    private final String algorithm;
    private final int keyLength;
    private final SecureRandom secureRandom;
    
    public BasicCrypto() {
        this("AES", 256);
    }
    
    public BasicCrypto(String algorithm, int keyLength) {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        this.secureRandom = new SecureRandom();
        this.secretKey = generateSecretKeyInternal();
    }
    
    public BasicCrypto(String secretKey) {
        this.algorithm = "AES";
        this.keyLength = 256;
        this.secureRandom = new SecureRandom();
        this.secretKey = deriveKeyFromSecret(secretKey);
    }
    
    public BasicCrypto(String secretKey, String algorithm, int keyLength) {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        this.secureRandom = new SecureRandom();
        this.secretKey = deriveKeyFromSecret(secretKey);
    }
    
    private SecretKey deriveKeyFromSecret(String secret) {
        try {
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt, ITERATION_COUNT, keyLength);
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            
            return new SecretKeySpec(keyBytes, AES_ALGORITHM);
        } catch (Exception e) {
            throw new SecurityException("Failed to derive key from secret", "KEY_DERIVATION_ERROR", e);
        }
    }
    
    @Override
    public String encrypt(String plainText) {
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            
            byte[] cipherTextWithIv = new byte[GCM_IV_LENGTH + cipherText.length];
            System.arraycopy(iv, 0, cipherTextWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(cipherText, 0, cipherTextWithIv, GCM_IV_LENGTH, cipherText.length);
            
            return Base64.getEncoder().encodeToString(cipherTextWithIv);
        } catch (Exception e) {
            throw new SecurityException("Encryption failed", "ENCRYPTION_ERROR", e);
        }
    }
    
    @Override
    public String decrypt(String cipherText) {
        try {
            byte[] cipherTextWithIv = Base64.getDecoder().decode(cipherText);
            
            if (cipherTextWithIv.length < GCM_IV_LENGTH) {
                throw new SecurityException("Invalid cipher text", "INVALID_CIPHER_TEXT");
            }
            
            byte[] iv = Arrays.copyOfRange(cipherTextWithIv, 0, GCM_IV_LENGTH);
            byte[] cipherBytes = Arrays.copyOfRange(cipherTextWithIv, GCM_IV_LENGTH, cipherTextWithIv.length);
            
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            byte[] plainText = cipher.doFinal(cipherBytes);
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new SecurityException("Decryption failed", "DECRYPTION_ERROR", e);
        }
    }
    
    @Override
    public String hash(String input) {
        try {
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            
            MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
            digest.update(salt);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            byte[] hashWithSalt = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, hashWithSalt, 0, salt.length);
            System.arraycopy(hash, 0, hashWithSalt, salt.length, hash.length);
            
            return Base64.getEncoder().encodeToString(hashWithSalt);
        } catch (Exception e) {
            throw new SecurityException("Hashing failed", "HASH_ERROR", e);
        }
    }
    
    @Override
    public boolean verify(String input, String hash) {
        try {
            byte[] hashWithSalt = Base64.getDecoder().decode(hash);
            
            if (hashWithSalt.length < 16) {
                return false;
            }
            
            byte[] salt = Arrays.copyOfRange(hashWithSalt, 0, 16);
            
            MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
            digest.update(salt);
            byte[] computedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            byte[] storedHash = Arrays.copyOfRange(hashWithSalt, 16, hashWithSalt.length);
            
            return MessageDigest.isEqual(computedHash, storedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String sign(String data) {
        try {
            if (keyPair == null) {
                keyPair = generateKeyPair();
            }
            
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate(), secureRandom);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new SecurityException("Signing failed", "SIGN_ERROR", e);
        }
    }
    
    @Override
    public boolean verifySignature(String data, String signatureStr) {
        try {
            if (keyPair == null) {
                return false;
            }
            
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(keyPair.getPublic());
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            
            return signature.verify(Base64.getDecoder().decode(signatureStr));
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public SecretKey generateSecretKey() {
        return generateSecretKeyInternal();
    }
    
    private SecretKey generateSecretKeyInternal() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(keyLength, secureRandom);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new SecurityException("Key generation failed", "KEY_GENERATION_ERROR", e);
        }
    }
    
    @Override
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(2048, secureRandom);
            this.keyPair = keyPairGenerator.generateKeyPair();
            return this.keyPair;
        } catch (Exception e) {
            throw new SecurityException("Key pair generation failed", "KEY_PAIR_GENERATION_ERROR", e);
        }
    }
    
    @Override
    public PublicKey getPublicKey() {
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPublic();
    }
    
    @Override
    public PrivateKey getPrivateKey() {
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPrivate();
    }
    
    public String exportPublicKey() {
        PublicKey publicKey = getPublicKey();
        if (publicKey == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    
    public String exportPrivateKey() {
        PrivateKey privateKey = getPrivateKey();
        if (privateKey == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
    
    public void importPublicKey(String keyStr) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(spec);
            
            if (keyPair == null) {
                keyPair = new KeyPair(publicKey, null);
            } else {
                keyPair = new KeyPair(publicKey, keyPair.getPrivate());
            }
        } catch (Exception e) {
            throw new SecurityException("Public key import failed", "KEY_IMPORT_ERROR", e);
        }
    }
    
    public void importPrivateKey(String keyStr) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyStr);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(spec);
            
            if (keyPair == null) {
                keyPair = new KeyPair(null, privateKey);
            } else {
                keyPair = new KeyPair(keyPair.getPublic(), privateKey);
            }
        } catch (Exception e) {
            throw new SecurityException("Private key import failed", "KEY_IMPORT_ERROR", e);
        }
    }
    
    public static BasicCrypto of(String secretKey) {
        return new BasicCrypto(secretKey);
    }
    
    public static BasicCrypto of(String secretKey, String algorithm, int keyLength) {
        return new BasicCrypto(secretKey, algorithm, keyLength);
    }
}
