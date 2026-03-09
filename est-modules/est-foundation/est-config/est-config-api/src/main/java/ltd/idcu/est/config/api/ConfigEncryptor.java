package ltd.idcu.est.config.api;

public interface ConfigEncryptor {
    String encrypt(String plaintext);
    String decrypt(String ciphertext);
    void setKey(String base64Key);
    String getKey();
}
