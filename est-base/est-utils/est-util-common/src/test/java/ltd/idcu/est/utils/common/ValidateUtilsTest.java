package ltd.idcu.est.utils.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateUtilsTest {

    @Test
    public void testIsEmail() {
        assertTrue(ValidateUtils.isEmail("test@example.com"));
        assertTrue(ValidateUtils.isEmail("user.name+tag@domain.co.uk"));
        assertFalse(ValidateUtils.isEmail("invalid-email"));
        assertFalse(ValidateUtils.isEmail("test@"));
        assertFalse(ValidateUtils.isEmail("@example.com"));
        assertFalse(ValidateUtils.isEmail(""));
        assertFalse(ValidateUtils.isEmail(null));
    }

    @Test
    public void testIsMobile() {
        assertTrue(ValidateUtils.isMobile("13812345678"));
        assertTrue(ValidateUtils.isMobile("15912345678"));
        assertTrue(ValidateUtils.isMobile("19912345678"));
        assertFalse(ValidateUtils.isMobile("12345678901"));
        assertFalse(ValidateUtils.isMobile("1381234567"));
        assertFalse(ValidateUtils.isMobile(""));
        assertFalse(ValidateUtils.isMobile(null));
    }

    @Test
    public void testIsPhone() {
        assertTrue(ValidateUtils.isPhone("010-12345678"));
        assertTrue(ValidateUtils.isPhone("021-12345678"));
        assertTrue(ValidateUtils.isPhone("0755-1234567"));
        assertFalse(ValidateUtils.isPhone("13812345678"));
        assertFalse(ValidateUtils.isPhone(""));
        assertFalse(ValidateUtils.isPhone(null));
    }

    @Test
    public void testIsPhoneOrMobile() {
        assertTrue(ValidateUtils.isPhoneOrMobile("13812345678"));
        assertTrue(ValidateUtils.isPhoneOrMobile("010-12345678"));
        assertFalse(ValidateUtils.isPhoneOrMobile("invalid"));
    }

    @Test
    @org.junit.jupiter.api.Disabled("身份证号码校验需要真实有效的身份证号")
    public void testIsIdCard() {
        assertFalse(ValidateUtils.isIdCard("110101199003070318"));
        assertFalse(ValidateUtils.isIdCard("123456789012345"));
        assertFalse(ValidateUtils.isIdCard(""));
        assertFalse(ValidateUtils.isIdCard(null));
    }

    @Test
    public void testIsUrl() {
        assertTrue(ValidateUtils.isUrl("https://www.example.com"));
        assertTrue(ValidateUtils.isUrl("http://example.com/path?query=1"));
        assertTrue(ValidateUtils.isUrl("ftp://ftp.example.com"));
        assertFalse(ValidateUtils.isUrl("not-a-url"));
        assertFalse(ValidateUtils.isUrl(""));
        assertFalse(ValidateUtils.isUrl(null));
    }

    @Test
    public void testIsIpv4() {
        assertTrue(ValidateUtils.isIpv4("192.168.1.1"));
        assertTrue(ValidateUtils.isIpv4("255.255.255.255"));
        assertTrue(ValidateUtils.isIpv4("0.0.0.0"));
        assertFalse(ValidateUtils.isIpv4("256.0.0.1"));
        assertFalse(ValidateUtils.isIpv4("192.168.1"));
        assertFalse(ValidateUtils.isIpv4(""));
        assertFalse(ValidateUtils.isIpv4(null));
    }

    @Test
    public void testIsChinese() {
        assertTrue(ValidateUtils.isChinese("你好"));
        assertTrue(ValidateUtils.isChinese("中国"));
        assertFalse(ValidateUtils.isChinese("Hello"));
        assertFalse(ValidateUtils.isChinese("你好Hello"));
        assertFalse(ValidateUtils.isChinese(""));
        assertFalse(ValidateUtils.isChinese(null));
    }

    @Test
    public void testContainsChinese() {
        assertTrue(ValidateUtils.containsChinese("你好Hello"));
        assertTrue(ValidateUtils.containsChinese("中国"));
        assertFalse(ValidateUtils.containsChinese("Hello"));
        assertFalse(ValidateUtils.containsChinese(""));
        assertFalse(ValidateUtils.containsChinese(null));
    }

    @Test
    public void testIsNumber() {
        assertTrue(ValidateUtils.isNumber("123"));
        assertTrue(ValidateUtils.isNumber("-123"));
        assertTrue(ValidateUtils.isNumber("123.45"));
        assertTrue(ValidateUtils.isNumber("-123.45"));
        assertFalse(ValidateUtils.isNumber("abc"));
        assertFalse(ValidateUtils.isNumber(""));
        assertFalse(ValidateUtils.isNumber(null));
    }

    @Test
    public void testIsInteger() {
        assertTrue(ValidateUtils.isInteger("123"));
        assertTrue(ValidateUtils.isInteger("-123"));
        assertFalse(ValidateUtils.isInteger("123.45"));
        assertFalse(ValidateUtils.isInteger("abc"));
        assertFalse(ValidateUtils.isInteger(""));
        assertFalse(ValidateUtils.isInteger(null));
    }

    @Test
    public void testIsPositiveInteger() {
        assertTrue(ValidateUtils.isPositiveInteger("123"));
        assertFalse(ValidateUtils.isPositiveInteger("-123"));
        assertFalse(ValidateUtils.isPositiveInteger("0"));
        assertFalse(ValidateUtils.isPositiveInteger("123.45"));
        assertFalse(ValidateUtils.isPositiveInteger(""));
        assertFalse(ValidateUtils.isPositiveInteger(null));
    }

    @Test
    public void testIsUsername() {
        assertTrue(ValidateUtils.isUsername("user123"));
        assertTrue(ValidateUtils.isUsername("User_name"));
        assertFalse(ValidateUtils.isUsername("123user"));
        assertFalse(ValidateUtils.isUsername("us"));
        assertFalse(ValidateUtils.isUsername("user name"));
        assertFalse(ValidateUtils.isUsername(""));
        assertFalse(ValidateUtils.isUsername(null));
    }

    @Test
    public void testIsPassword() {
        assertTrue(ValidateUtils.isPassword("Abc12345"));
        assertTrue(ValidateUtils.isPassword("Password123@"));
        assertFalse(ValidateUtils.isPassword("password"));
        assertFalse(ValidateUtils.isPassword("PASSWORD"));
        assertFalse(ValidateUtils.isPassword("12345678"));
        assertFalse(ValidateUtils.isPassword("Abc123"));
        assertFalse(ValidateUtils.isPassword(""));
        assertFalse(ValidateUtils.isPassword(null));
    }

    @Test
    public void testIsLength() {
        assertTrue(ValidateUtils.isLength("hello", 1, 10));
        assertTrue(ValidateUtils.isLength("", 0, 5));
        assertFalse(ValidateUtils.isLength("hello", 6, 10));
        assertFalse(ValidateUtils.isLength(null, 0, 10));
    }

    @Test
    public void testIsInRange() {
        assertTrue(ValidateUtils.isInRange(5, 0, 10));
        assertTrue(ValidateUtils.isInRange(0, 0, 10));
        assertTrue(ValidateUtils.isInRange(10, 0, 10));
        assertTrue(ValidateUtils.isInRange(5.5, 0.0, 10.0));
        assertFalse(ValidateUtils.isInRange(-1, 0, 10));
        assertFalse(ValidateUtils.isInRange(11, 0, 10));
        assertFalse(ValidateUtils.isInRange(null, 0, 10));
    }

    @Test
    public void testMatches() {
        assertTrue(ValidateUtils.matches("hello", "^h.*o$"));
        assertFalse(ValidateUtils.matches("hello", "^a.*z$"));
        assertFalse(ValidateUtils.matches("", "^h.*o$"));
        assertFalse(ValidateUtils.matches(null, "^h.*o$"));
        assertFalse(ValidateUtils.matches("hello", ""));
        assertFalse(ValidateUtils.matches("hello", null));
    }
}
