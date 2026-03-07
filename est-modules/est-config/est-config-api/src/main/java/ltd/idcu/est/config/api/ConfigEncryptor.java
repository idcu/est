package ltd.idcu.est.config.api;

public interface ConfigEncryptor {
    String encrypt(String plaintext);

    String decrypt(String ciphertext);

    void setKey(String key);

    String getKey();
}
