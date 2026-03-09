package ltd.idcu.est.utils.common;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.api.Test;

public class EncryptUtilsTest {

    @Test
    public void testMd5() {
        String input = "Hello, EST!";
        String md5Hash = EncryptUtils.md5(input);
        Assertions.assertNotNull(md5Hash);
        Assertions.assertEquals(32, md5Hash.length());
        
        String sameInputHash = EncryptUtils.md5(input);
        Assertions.assertEquals(md5Hash, sameInputHash);
        
        String emptyHash = EncryptUtils.md5("");
        Assertions.assertEquals("", emptyHash);
    }

    @Test
    public void testSha1() {
        String input = "Hello, EST!";
        String sha1Hash = EncryptUtils.sha1(input);
        Assertions.assertNotNull(sha1Hash);
        Assertions.assertEquals(40, sha1Hash.length());
    }

    @Test
    public void testSha256() {
        String input = "Hello, EST!";
        String sha256Hash = EncryptUtils.sha256(input);
        Assertions.assertNotNull(sha256Hash);
        Assertions.assertEquals(64, sha256Hash.length());
    }

    @Test
    public void testSha512() {
        String input = "Hello, EST!";
        String sha512Hash = EncryptUtils.sha512(input);
        Assertions.assertNotNull(sha512Hash);
        Assertions.assertEquals(128, sha512Hash.length());
    }

    @Test
    public void testBase64EncodeDecode() {
        String input = "Hello, EST!";
        String encoded = EncryptUtils.base64Encode(input);
        Assertions.assertNotNull(encoded);
        Assertions.assertFalse(encoded.isEmpty());
        
        String decoded = EncryptUtils.base64Decode(encoded);
        Assertions.assertEquals(input, decoded);
    }

    @Test
    public void testBase64UrlEncodeDecode() {
        String input = "Hello, EST! This is a test with special chars: +/=";
        String encoded = EncryptUtils.base64UrlEncode(input);
        Assertions.assertNotNull(encoded);
        Assertions.assertFalse(encoded.isEmpty());
        Assertions.assertFalse(encoded.contains("+"));
        Assertions.assertFalse(encoded.contains("/"));
        Assertions.assertFalse(encoded.contains("="));
        
        String decoded = EncryptUtils.base64UrlDecode(encoded);
        Assertions.assertEquals(input, decoded);
    }

    @Test
    public void testGenerateRandomString() {
        String random = EncryptUtils.generateRandomString(10);
        Assertions.assertNotNull(random);
        Assertions.assertEquals(10, random.length());
        
        String random2 = EncryptUtils.generateRandomString(10);
        Assertions.assertNotEquals(random, random2);
        
        String empty = EncryptUtils.generateRandomString(0);
        Assertions.assertEquals("", empty);
    }

    @Test
    public void testGenerateRandomBytes() {
        byte[] bytes = EncryptUtils.generateRandomBytes(16);
        Assertions.assertNotNull(bytes);
        Assertions.assertEquals(16, bytes.length);
        
        byte[] bytes2 = EncryptUtils.generateRandomBytes(16);
        Assertions.assertNotNull(bytes2);
        Assertions.assertEquals(16, bytes2.length);
        Assertions.assertNotSame(bytes, bytes2);
    }

    @Test
    public void testGenerateSalt() {
        String salt = EncryptUtils.generateSalt(16);
        Assertions.assertNotNull(salt);
        Assertions.assertEquals(16, salt.length());
    }

    @Test
    public void testHashWithNull() {
        Assertions.assertEquals("", EncryptUtils.md5((String) null));
        Assertions.assertEquals("", EncryptUtils.md5((byte[]) null));
        Assertions.assertEquals("", EncryptUtils.sha1((String) null));
        Assertions.assertEquals("", EncryptUtils.base64Encode((String) null));
        Assertions.assertEquals("", EncryptUtils.base64Encode((byte[]) null));
    }
}
