package ltd.idcu.est.utils.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptUtilsTest {

    @Test
    public void testMd5() {
        String input = "Hello, EST!";
        String md5Hash = EncryptUtils.md5(input);
        assertNotNull(md5Hash);
        assertEquals(32, md5Hash.length());
        
        String sameInputHash = EncryptUtils.md5(input);
        assertEquals(md5Hash, sameInputHash);
        
        String emptyHash = EncryptUtils.md5("");
        assertEquals("", emptyHash);
    }

    @Test
    public void testSha1() {
        String input = "Hello, EST!";
        String sha1Hash = EncryptUtils.sha1(input);
        assertNotNull(sha1Hash);
        assertEquals(40, sha1Hash.length());
    }

    @Test
    public void testSha256() {
        String input = "Hello, EST!";
        String sha256Hash = EncryptUtils.sha256(input);
        assertNotNull(sha256Hash);
        assertEquals(64, sha256Hash.length());
    }

    @Test
    public void testSha512() {
        String input = "Hello, EST!";
        String sha512Hash = EncryptUtils.sha512(input);
        assertNotNull(sha512Hash);
        assertEquals(128, sha512Hash.length());
    }

    @Test
    public void testBase64EncodeDecode() {
        String input = "Hello, EST!";
        String encoded = EncryptUtils.base64Encode(input);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        
        String decoded = EncryptUtils.base64Decode(encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testBase64UrlEncodeDecode() {
        String input = "Hello, EST! This is a test with special chars: +/=";
        String encoded = EncryptUtils.base64UrlEncode(input);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        assertFalse(encoded.contains("+"));
        assertFalse(encoded.contains("/"));
        assertFalse(encoded.contains("="));
        
        String decoded = EncryptUtils.base64UrlDecode(encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testGenerateRandomString() {
        String random = EncryptUtils.generateRandomString(10);
        assertNotNull(random);
        assertEquals(10, random.length());
        
        String random2 = EncryptUtils.generateRandomString(10);
        assertNotEquals(random, random2);
        
        String empty = EncryptUtils.generateRandomString(0);
        assertEquals("", empty);
    }

    @Test
    public void testGenerateRandomBytes() {
        byte[] bytes = EncryptUtils.generateRandomBytes(16);
        assertNotNull(bytes);
        assertEquals(16, bytes.length);
        
        byte[] bytes2 = EncryptUtils.generateRandomBytes(16);
        assertNotNull(bytes2);
        assertEquals(16, bytes2.length);
        assertNotSame(bytes, bytes2);
    }

    @Test
    public void testGenerateSalt() {
        String salt = EncryptUtils.generateSalt(16);
        assertNotNull(salt);
        assertEquals(16, salt.length());
    }

    @Test
    public void testHashWithNull() {
        assertEquals("", EncryptUtils.md5((String) null));
        assertEquals("", EncryptUtils.md5((byte[]) null));
        assertEquals("", EncryptUtils.sha1((String) null));
        assertEquals("", EncryptUtils.base64Encode((String) null));
        assertEquals("", EncryptUtils.base64Encode((byte[]) null));
    }
}
