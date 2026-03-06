package ltd.idcu.est.utils.common;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Optional;

public class NumberUtilsTest {

    @Test
    public void testIsDigits() {
        Assertions.assertTrue(NumberUtils.isDigits("123"));
        Assertions.assertTrue(NumberUtils.isDigits("0"));
        Assertions.assertFalse(NumberUtils.isDigits(null));
        Assertions.assertFalse(NumberUtils.isDigits(""));
        Assertions.assertFalse(NumberUtils.isDigits("12a3"));
        Assertions.assertFalse(NumberUtils.isDigits("12.3"));
    }

    @Test
    public void testIsNumber() {
        Assertions.assertTrue(NumberUtils.isNumber("123"));
        Assertions.assertTrue(NumberUtils.isNumber("123.45"));
        Assertions.assertTrue(NumberUtils.isNumber("-123"));
        Assertions.assertTrue(NumberUtils.isNumber("0"));
        Assertions.assertFalse(NumberUtils.isNumber(null));
        Assertions.assertFalse(NumberUtils.isNumber(""));
        Assertions.assertFalse(NumberUtils.isNumber("12a3"));
    }

    @Test
    public void testIsInteger() {
        Assertions.assertTrue(NumberUtils.isInteger("123"));
        Assertions.assertTrue(NumberUtils.isInteger("-123"));
        Assertions.assertTrue(NumberUtils.isInteger("0"));
        Assertions.assertFalse(NumberUtils.isInteger(null));
        Assertions.assertFalse(NumberUtils.isInteger(""));
        Assertions.assertFalse(NumberUtils.isInteger("123.45"));
        Assertions.assertFalse(NumberUtils.isInteger("12a3"));
    }

    @Test
    public void testToInt() {
        Assertions.assertEquals(123, NumberUtils.toInt("123"));
        Assertions.assertEquals(0, NumberUtils.toInt(null));
        Assertions.assertEquals(0, NumberUtils.toInt(""));
        Assertions.assertEquals(-1, NumberUtils.toInt("", -1));
        Assertions.assertEquals(456, NumberUtils.toInt("456", -1));
    }

    @Test
    public void testToIntObject() {
        Assertions.assertEquals(Integer.valueOf(123), NumberUtils.toIntObject("123"));
        Assertions.assertNull(NumberUtils.toIntObject(null));
        Assertions.assertNull(NumberUtils.toIntObject("invalid"));
        Assertions.assertEquals(Integer.valueOf(-1), NumberUtils.toIntObject("invalid", -1));
    }

    @Test
    public void testToLong() {
        Assertions.assertEquals(123L, NumberUtils.toLong("123"));
        Assertions.assertEquals(0L, NumberUtils.toLong(null));
        Assertions.assertEquals(-1L, NumberUtils.toLong("", -1L));
    }

    @Test
    public void testToDouble() {
        Assertions.assertEqualsWithDelta(123.45, NumberUtils.toDouble("123.45"), 0.0001);
        Assertions.assertEqualsWithDelta(0.0, NumberUtils.toDouble(null), 0.0001);
        Assertions.assertEqualsWithDelta(-1.0, NumberUtils.toDouble("", -1.0), 0.0001);
    }

    @Test
    public void testToBigDecimal() {
        Assertions.assertEquals(new BigDecimal("123.45"), NumberUtils.toBigDecimal("123.45"));
        Assertions.assertNull(NumberUtils.toBigDecimal(null));
        Assertions.assertNull(NumberUtils.toBigDecimal("invalid"));
        Assertions.assertEquals(new BigDecimal("-1"), NumberUtils.toBigDecimal("invalid", new BigDecimal("-1")));
    }

    @Test
    public void testToBigInteger() {
        Assertions.assertEquals(new BigInteger("123"), NumberUtils.toBigInteger("123"));
        Assertions.assertNull(NumberUtils.toBigInteger(null));
    }

    @Test
    public void testToIntOptional() {
        Assertions.assertTrue(NumberUtils.toIntOptional("123").isPresent());
        Assertions.assertEquals(123, NumberUtils.toIntOptional("123").get());
        Assertions.assertFalse(NumberUtils.toIntOptional(null).isPresent());
        Assertions.assertFalse(NumberUtils.toIntOptional("invalid").isPresent());
    }

    @Test
    public void testMin() {
        Assertions.assertEquals(1, NumberUtils.min(3, 1, 2, 5, 4));
        Assertions.assertEquals(-5, NumberUtils.min(-1, -5, 0, 3));
    }

    @Test
    public void testMax() {
        Assertions.assertEquals(5, NumberUtils.max(3, 1, 2, 5, 4));
        Assertions.assertEquals(3, NumberUtils.max(-1, -5, 0, 3));
    }

    @Test
    public void testSum() {
        Assertions.assertEquals(15, NumberUtils.sum(1, 2, 3, 4, 5));
        Assertions.assertEquals(0, NumberUtils.sum());
    }

    @Test
    public void testRound() {
        Assertions.assertEqualsWithDelta(123.46, NumberUtils.round(123.456, 2), 0.0001);
        Assertions.assertEqualsWithDelta(123.5, NumberUtils.round(123.45, 1, RoundingMode.HALF_UP), 0.0001);
    }

    @Test
    public void testIsInRange() {
        Assertions.assertTrue(NumberUtils.isInRange(5, 0, 10));
        Assertions.assertTrue(NumberUtils.isInRange(0, 0, 10));
        Assertions.assertTrue(NumberUtils.isInRange(10, 0, 10));
        Assertions.assertFalse(NumberUtils.isInRange(-1, 0, 10));
        Assertions.assertFalse(NumberUtils.isInRange(11, 0, 10));
    }

    @Test
    public void testClamp() {
        Assertions.assertEquals(5, NumberUtils.clamp(5, 0, 10));
        Assertions.assertEquals(0, NumberUtils.clamp(-5, 0, 10));
        Assertions.assertEquals(10, NumberUtils.clamp(15, 0, 10));
    }

    @Test
    public void testToHexFromHex() {
        byte[] data = new byte[]{0x01, 0x2F, (byte) 0xAB};
        String hex = NumberUtils.toHex(data);
        Assertions.assertEquals("012fab", hex);
        byte[] result = NumberUtils.fromHex(hex);
        Assertions.assertArrayEquals(data, result);
    }

    @Test
    public void testToBinaryFromBinary() {
        Assertions.assertEquals("1010", NumberUtils.toBinary(10));
        Assertions.assertEquals(10L, NumberUtils.fromBinary("1010"));
    }

    @Test
    public void testToOctalFromOctal() {
        Assertions.assertEquals("12", NumberUtils.toOctal(10));
        Assertions.assertEquals(10L, NumberUtils.fromOctal("12"));
    }

    @Test
    public void testLongToBytesBytesToLong() {
        long value = 1234567890L;
        byte[] bytes = NumberUtils.longToBytes(value);
        Assertions.assertEquals(value, NumberUtils.bytesToLong(bytes));
    }

    @Test
    public void testIntToBytesBytesToInt() {
        int value = 123456789;
        byte[] bytes = NumberUtils.intToBytes(value);
        Assertions.assertEquals(value, NumberUtils.bytesToInt(bytes));
    }

    @Test
    public void testIsPositive() {
        Assertions.assertTrue(NumberUtils.isPositive(5));
        Assertions.assertTrue(NumberUtils.isPositive(5.5));
        Assertions.assertTrue(NumberUtils.isPositive(new BigDecimal("10")));
        Assertions.assertFalse(NumberUtils.isPositive(0));
        Assertions.assertFalse(NumberUtils.isPositive(-5));
        Assertions.assertFalse(NumberUtils.isPositive(null));
    }

    @Test
    public void testIsNegative() {
        Assertions.assertTrue(NumberUtils.isNegative(-5));
        Assertions.assertTrue(NumberUtils.isNegative(-5.5));
        Assertions.assertFalse(NumberUtils.isNegative(0));
        Assertions.assertFalse(NumberUtils.isNegative(5));
        Assertions.assertFalse(NumberUtils.isNegative(null));
    }

    @Test
    public void testIsZero() {
        Assertions.assertTrue(NumberUtils.isZero(0));
        Assertions.assertTrue(NumberUtils.isZero(0.0));
        Assertions.assertTrue(NumberUtils.isZero(new BigDecimal("0")));
        Assertions.assertFalse(NumberUtils.isZero(5));
        Assertions.assertFalse(NumberUtils.isZero(null));
    }
}
