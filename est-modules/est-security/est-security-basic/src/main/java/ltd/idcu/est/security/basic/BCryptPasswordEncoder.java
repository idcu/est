package ltd.idcu.est.security.basic;

import ltd.idcu.est.security.api.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class BCryptPasswordEncoder implements PasswordEncoder {
    
    private static final int DEFAULT_STRENGTH = 10;
    private static final int BCRYPT_HASH_LENGTH = 60;
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    
    private final int strength;
    private final SecureRandom random;
    private final boolean usePBKDF2;
    
    public BCryptPasswordEncoder() {
        this(DEFAULT_STRENGTH);
    }
    
    public BCryptPasswordEncoder(int strength) {
        this(strength, true);
    }
    
    public BCryptPasswordEncoder(int strength, boolean usePBKDF2) {
        if (strength < 4 || strength > 31) {
            throw new IllegalArgumentException("Strength must be between 4 and 31");
        }
        this.strength = strength;
        this.random = new SecureRandom();
        this.usePBKDF2 = usePBKDF2;
    }
    
    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        
        if (usePBKDF2) {
            return encodePBKDF2(rawPassword.toString());
        } else {
            return encodeSimple(rawPassword.toString());
        }
    }
    
    private String encodePBKDF2(String password) {
        try {
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            String saltStr = Base64.getEncoder().encodeToString(salt);
            String hashStr = Base64.getEncoder().encodeToString(hash);
            
            return "$pbkdf2$" + ITERATION_COUNT + "$" + saltStr + "$" + hashStr;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password encoding failed", e);
        }
    }
    
    private String encodeSimple(String password) {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        String saltStr = Base64.getEncoder().encodeToString(salt);
        String hash = simpleHash(password, saltStr);
        
        return "$simple$" + saltStr + "$" + hash;
    }
    
    private String simpleHash(String password, String salt) {
        int hash = 7;
        String combined = salt + password + salt;
        for (int i = 0; i < combined.length(); i++) {
            hash = hash * 31 + combined.charAt(i);
        }
        hash = hash * (int) Math.pow(2, strength);
        return Integer.toHexString(Math.abs(hash));
    }
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        
        if (encodedPassword.startsWith("$pbkdf2$")) {
            return matchesPBKDF2(rawPassword.toString(), encodedPassword);
        } else if (encodedPassword.startsWith("$simple$")) {
            return matchesSimple(rawPassword.toString(), encodedPassword);
        }
        
        return false;
    }
    
    private boolean matchesPBKDF2(String password, String encodedPassword) {
        try {
            String[] parts = encodedPassword.split("\\$");
            if (parts.length != 5) {
                return false;
            }
            
            int iterations = Integer.parseInt(parts[2]);
            byte[] salt = Base64.getDecoder().decode(parts[3]);
            byte[] storedHash = Base64.getDecoder().decode(parts[4]);
            
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            byte[] computedHash = factory.generateSecret(spec).getEncoded();
            
            return constantTimeEquals(storedHash, computedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean matchesSimple(String password, String encodedPassword) {
        try {
            String[] parts = encodedPassword.split("\\$");
            if (parts.length != 4) {
                return false;
            }
            
            String salt = parts[2];
            String storedHash = parts[3];
            String computedHash = simpleHash(password, salt);
            
            return storedHash.equals(computedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
    
    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        
        if (encodedPassword.startsWith("$pbkdf2$")) {
            String[] parts = encodedPassword.split("\\$");
            if (parts.length >= 3) {
                try {
                    int iterations = Integer.parseInt(parts[2]);
                    return iterations < ITERATION_COUNT;
                } catch (NumberFormatException e) {
                    return true;
                }
            }
        }
        
        return encodedPassword.startsWith("$simple$");
    }
    
    public static BCryptPasswordEncoder create() {
        return new BCryptPasswordEncoder();
    }
    
    public static BCryptPasswordEncoder create(int strength) {
        return new BCryptPasswordEncoder(strength);
    }
}
