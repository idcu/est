package ltd.idcu.est.utils.common;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.api.Test;

public class ValidateUtilsTest {

    @Test
    public void testIsEmail() {
        Assertions.assertTrue(ValidateUtils.isEmail("test@example.com"));
        Assertions.assertTrue(ValidateUtils.isEmail("user.name+tag@domain.co.uk"));
        Assertions.assertFalse(ValidateUtils.isEmail("invalid-email"));
        Assertions.assertFalse(ValidateUtils.isEmail("test@"));
        Assertions.assertFalse(ValidateUtils.isEmail("@example.com"));
        Assertions.assertFalse(ValidateUtils.isEmail(""));
        Assertions.assertFalse(ValidateUtils.isEmail(null));
    }

    @Test
    public void testIsMobile() {
        Assertions.assertTrue(ValidateUtils.isMobile("13812345678"));
        Assertions.assertTrue(ValidateUtils.isMobile("15912345678"));
        Assertions.assertTrue(ValidateUtils.isMobile("19912345678"));
        Assertions.assertFalse(ValidateUtils.isMobile("12345678901"));
        Assertions.assertFalse(ValidateUtils.isMobile("1381234567"));
        Assertions.assertFalse(ValidateUtils.isMobile(""));
        Assertions.assertFalse(ValidateUtils.isMobile(null));
    }

    @Test
    public void testIsPhone() {
        Assertions.assertTrue(ValidateUtils.isPhone("010-12345678"));
        Assertions.assertTrue(ValidateUtils.isPhone("021-12345678"));
        Assertions.assertTrue(ValidateUtils.isPhone("0755-1234567"));
        Assertions.assertFalse(ValidateUtils.isPhone("13812345678"));
        Assertions.assertFalse(ValidateUtils.isPhone(""));
        Assertions.assertFalse(ValidateUtils.isPhone(null));
    }

    @Test
    public void testIsPhoneOrMobile() {
        Assertions.assertTrue(ValidateUtils.isPhoneOrMobile("13812345678"));
        Assertions.assertTrue(ValidateUtils.isPhoneOrMobile("010-12345678"));
        Assertions.assertFalse(ValidateUtils.isPhoneOrMobile("invalid"));
    }

    @Test
    public void testIsIdCard() {
        Assertions.assertTrue(ValidateUtils.isIdCard("110101199003070319"));
        Assertions.assertFalse(ValidateUtils.isIdCard("110101199003070318"));
        Assertions.assertFalse(ValidateUtils.isIdCard("123456789012345"));
        Assertions.assertFalse(ValidateUtils.isIdCard(""));
        Assertions.assertFalse(ValidateUtils.isIdCard(null));
    }

    @Test
    public void testIsUrl() {
        Assertions.assertTrue(ValidateUtils.isUrl("https://www.example.com"));
        Assertions.assertTrue(ValidateUtils.isUrl("http://example.com/path?query=1"));
        Assertions.assertTrue(ValidateUtils.isUrl("ftp://ftp.example.com"));
        Assertions.assertFalse(ValidateUtils.isUrl("not-a-url"));
        Assertions.assertFalse(ValidateUtils.isUrl(""));
        Assertions.assertFalse(ValidateUtils.isUrl(null));
    }

    @Test
    public void testIsIpv4() {
        Assertions.assertTrue(ValidateUtils.isIpv4("192.168.1.1"));
        Assertions.assertTrue(ValidateUtils.isIpv4("255.255.255.255"));
        Assertions.assertTrue(ValidateUtils.isIpv4("0.0.0.0"));
        Assertions.assertFalse(ValidateUtils.isIpv4("256.0.0.1"));
        Assertions.assertFalse(ValidateUtils.isIpv4("192.168.1"));
        Assertions.assertFalse(ValidateUtils.isIpv4(""));
        Assertions.assertFalse(ValidateUtils.isIpv4(null));
    }

    @Test
    public void testIsChinese() {
        Assertions.assertTrue(ValidateUtils.isChinese("你好"));
        Assertions.assertTrue(ValidateUtils.isChinese("中国"));
        Assertions.assertFalse(ValidateUtils.isChinese("Hello"));
        Assertions.assertFalse(ValidateUtils.isChinese("你好Hello"));
        Assertions.assertFalse(ValidateUtils.isChinese(""));
        Assertions.assertFalse(ValidateUtils.isChinese(null));
    }

    @Test
    public void testContainsChinese() {
        Assertions.assertTrue(ValidateUtils.containsChinese("你好Hello"));
        Assertions.assertTrue(ValidateUtils.containsChinese("中国"));
        Assertions.assertFalse(ValidateUtils.containsChinese("Hello"));
        Assertions.assertFalse(ValidateUtils.containsChinese(""));
        Assertions.assertFalse(ValidateUtils.containsChinese(null));
    }

    @Test
    public void testIsNumber() {
        Assertions.assertTrue(ValidateUtils.isNumber("123"));
        Assertions.assertTrue(ValidateUtils.isNumber("-123"));
        Assertions.assertTrue(ValidateUtils.isNumber("123.45"));
        Assertions.assertTrue(ValidateUtils.isNumber("-123.45"));
        Assertions.assertFalse(ValidateUtils.isNumber("abc"));
        Assertions.assertFalse(ValidateUtils.isNumber(""));
        Assertions.assertFalse(ValidateUtils.isNumber(null));
    }

    @Test
    public void testIsInteger() {
        Assertions.assertTrue(ValidateUtils.isInteger("123"));
        Assertions.assertTrue(ValidateUtils.isInteger("-123"));
        Assertions.assertFalse(ValidateUtils.isInteger("123.45"));
        Assertions.assertFalse(ValidateUtils.isInteger("abc"));
        Assertions.assertFalse(ValidateUtils.isInteger(""));
        Assertions.assertFalse(ValidateUtils.isInteger(null));
    }

    @Test
    public void testIsPositiveInteger() {
        Assertions.assertTrue(ValidateUtils.isPositiveInteger("123"));
        Assertions.assertFalse(ValidateUtils.isPositiveInteger("-123"));
        Assertions.assertFalse(ValidateUtils.isPositiveInteger("0"));
        Assertions.assertFalse(ValidateUtils.isPositiveInteger("123.45"));
        Assertions.assertFalse(ValidateUtils.isPositiveInteger(""));
        Assertions.assertFalse(ValidateUtils.isPositiveInteger(null));
    }

    @Test
    public void testIsUsername() {
        Assertions.assertTrue(ValidateUtils.isUsername("user123"));
        Assertions.assertTrue(ValidateUtils.isUsername("User_name"));
        Assertions.assertFalse(ValidateUtils.isUsername("123user"));
        Assertions.assertFalse(ValidateUtils.isUsername("us"));
        Assertions.assertFalse(ValidateUtils.isUsername("user name"));
        Assertions.assertFalse(ValidateUtils.isUsername(""));
        Assertions.assertFalse(ValidateUtils.isUsername(null));
    }

    @Test
    public void testIsPassword() {
        Assertions.assertTrue(ValidateUtils.isPassword("Abc12345"));
        Assertions.assertTrue(ValidateUtils.isPassword("Password123@"));
        Assertions.assertFalse(ValidateUtils.isPassword("password"));
        Assertions.assertFalse(ValidateUtils.isPassword("PASSWORD"));
        Assertions.assertFalse(ValidateUtils.isPassword("12345678"));
        Assertions.assertFalse(ValidateUtils.isPassword("Abc123"));
        Assertions.assertFalse(ValidateUtils.isPassword(""));
        Assertions.assertFalse(ValidateUtils.isPassword(null));
    }

    @Test
    public void testIsLength() {
        Assertions.assertTrue(ValidateUtils.isLength("hello", 1, 10));
        Assertions.assertTrue(ValidateUtils.isLength("", 0, 5));
        Assertions.assertFalse(ValidateUtils.isLength("hello", 6, 10));
        Assertions.assertFalse(ValidateUtils.isLength(null, 0, 10));
    }

    @Test
    public void testIsInRange() {
        Assertions.assertTrue(ValidateUtils.isInRange(5, 0, 10));
        Assertions.assertTrue(ValidateUtils.isInRange(0, 0, 10));
        Assertions.assertTrue(ValidateUtils.isInRange(10, 0, 10));
        Assertions.assertTrue(ValidateUtils.isInRange(5.5, 0.0, 10.0));
        Assertions.assertFalse(ValidateUtils.isInRange(-1, 0, 10));
        Assertions.assertFalse(ValidateUtils.isInRange(11, 0, 10));
        Assertions.assertFalse(ValidateUtils.isInRange(null, 0, 10));
    }

    @Test
    public void testMatches() {
        Assertions.assertTrue(ValidateUtils.matches("hello", "^h.*o$"));
        Assertions.assertFalse(ValidateUtils.matches("hello", "^a.*z$"));
        Assertions.assertFalse(ValidateUtils.matches("", "^h.*o$"));
        Assertions.assertFalse(ValidateUtils.matches(null, "^h.*o$"));
        Assertions.assertFalse(ValidateUtils.matches("hello", ""));
        Assertions.assertFalse(ValidateUtils.matches("hello", null));
    }
}
