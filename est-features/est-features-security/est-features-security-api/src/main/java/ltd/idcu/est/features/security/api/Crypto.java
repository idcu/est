package ltd.idcu.est.features.security.api;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface Crypto {
    
    String encrypt(String plainText);
    
    String decrypt(String cipherText);
    
    String hash(String input);
    
    boolean verify(String input, String hash);
    
    String sign(String data);
    
    boolean verifySignature(String data, String signature);
    
    SecretKey generateSecretKey();
    
    KeyPair generateKeyPair();
    
    PublicKey getPublicKey();
    
    PrivateKey getPrivateKey();
}
