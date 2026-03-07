package ltd.idcu.est.config.impl;

import ltd.idcu.est.config.api.ConfigEncryptor;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Base64;

public class AesConfigEncryptorTest {

    @Test
    public void testEncryptDecrypt() {
        ConfigEncryptor encryptor = new AesConfigEncryptor("test-secret-key-123456");
        
        String plaintext = "Hello, World!";
        String encrypted = encryptor.encrypt(plaintext);
        
        Assertions.assertNotNull(encrypted);
        Assertions.assertNotEquals(plaintext, encrypted);
        
        String decrypted = encryptor.decrypt(encrypted);
        Assertions.assertEquals(plaintext, decrypted);
    }

    @Test
    public void testDifferentPlaintexts() {
        ConfigEncryptor encryptor = new AesConfigEncryptor("test-secret-key-123456");
        
        String text1 = "password123";
        String text2 = "another-secret";
        
        String encrypted1 = encryptor.encrypt(text1);
        String encrypted2 = encryptor.encrypt(text2);
        
        Assertions.assertNotEquals(encrypted1, encrypted2);
        Assertions.assertEquals(text1, encryptor.decrypt(encrypted1));
        Assertions.assertEquals(text2, encryptor.decrypt(encrypted2));
    }

    @Test
    public void testEmptyString() {
        ConfigEncryptor encryptor = new AesConfigEncryptor("test-secret-key-123456");
        
        String encrypted = encryptor.encrypt("");
        Assertions.assertNotNull(encrypted);
        Assertions.assertEquals("", encryptor.decrypt(encrypted));
    }

    @Test
    public void testSpecialCharacters() {
        ConfigEncryptor encryptor = new AesConfigEncryptor("test-secret-key-123456");
        
        String specialText = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        String encrypted = encryptor.encrypt(specialText);
        Assertions.assertEquals(specialText, encryptor.decrypt(encrypted));
    }

    @Test
    public void testLongText() {
        ConfigEncryptor encryptor = new AesConfigEncryptor("test-secret-key-123456");
        
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("Lorem ipsum dolor sit amet. ");
        }
        
        String encrypted = encryptor.encrypt(longText.toString());
        Assertions.assertEquals(longText.toString(), encryptor.decrypt(encrypted));
    }
}
