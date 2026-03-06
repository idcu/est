package ltd.idcu.est.utils.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Optional;

public final class NumberUtils {

    public static final long ZERO_LONG = 0L;
    public static final int ZERO_INT = 0;
    public static final double ZERO_DOUBLE = 0.0;
    public static final float ZERO_FLOAT = 0.0f;
    public static final short ZERO_SHORT = (short) 0;
    public static final byte ZERO_BYTE = (byte) 0;

    private static final int LONG_BYTES = Long.BYTES;
    private static final int INTEGER_BYTES = Integer.BYTES;
    private static final int SHORT_BYTES = Short.BYTES;

    private NumberUtils() {
    }

    public static boolean isDigits(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int toInt(String str) {
        return toInt(str, ZERO_INT);
    }

    public static int toInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Integer toIntObject(String str) {
        return toIntObject(str, null);
    }

    public static Integer toIntObject(String str, Integer defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long toLong(String str) {
        return toLong(str, ZERO_LONG);
    }

    public static long toLong(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Long toLongObject(String str) {
        return toLongObject(str, null);
    }

    public static Long toLongObject(String str, Long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double toDouble(String str) {
        return toDouble(str, ZERO_DOUBLE);
    }

    public static double toDouble(String str, double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double toDoubleObject(String str) {
        return toDoubleObject(str, null);
    }

    public static Double toDoubleObject(String str, Double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float toFloat(String str) {
        return toFloat(str, ZERO_FLOAT);
    }

    public static float toFloat(String str, float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Float toFloatObject(String str) {
        return toFloatObject(str, null);
    }

    public static Float toFloatObject(String str, Float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static short toShort(String str) {
        return toShort(str, ZERO_SHORT);
    }

    public static short toShort(String str, short defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Short toShortObject(String str) {
        return toShortObject(str, null);
    }

    public static Short toShortObject(String str, Short defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Short.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static byte toByte(String str) {
        return toByte(str, ZERO_BYTE);
    }

    public static byte toByte(String str, byte defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Byte toByteObject(String str) {
        return toByteObject(str, null);
    }

    public static Byte toByteObject(String str, Byte defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Byte.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static BigDecimal toBigDecimal(String str) {
        return toBigDecimal(str, null);
    }

    public static BigDecimal toBigDecimal(String str, BigDecimal defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static BigInteger toBigInteger(String str) {
        return toBigInteger(str, null);
    }

    public static BigInteger toBigInteger(String str, BigInteger defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return new BigInteger(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Optional<Integer> toIntOptional(String str) {
        if (str == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> toLongOptional(String str) {
        if (str == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> toDoubleOptional(String str) {
        if (str == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Double.parseDouble(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static int min(int... array) {
        AssertUtils.notNull(array, "Array must not be null");
        AssertUtils.isTrue(array.length > 0, "Array must not be empty");
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static long min(long... array) {
        AssertUtils.notNull(array, "Array must not be null");
        AssertUtils.isTrue(array.length > 0, "Array must not be empty");
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static double min(double... array) {
        AssertUtils.notNull(array, "Array must not be null");
        AssertUtils.isTrue(array.length > 0, "Array must not be empty");
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Double.compare(array[i], min) < 0) {
                min = array[i];
            }
        }
        return min;
    }

    public static int max(int... array) {
        AssertUtils.notNull(array, "Array must not be null");
        AssertUtils.isTrue(array.length > 0, "Array must not be empty");
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static long max(long... array) {
        AssertUtils.notNull(array, "Array must not be null");
        AssertUtils.isTrue(array.length > 0, "Array must not be empty");
        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static double max(double... array) {
        AssertUtils.notNull(array, "Array must not be null");
        AssertUtils.isTrue(array.length > 0, "Array must not be empty");
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Double.compare(array[i], max) > 0) {
                max = array[i];
            }
        }
        return max;
    }

    public static int sum(int... array) {
        AssertUtils.notNull(array, "Array must not be null");
        int sum = 0;
        for (int value : array) {
            sum += value;
        }
        return sum;
    }

    public static long sum(long... array) {
        AssertUtils.notNull(array, "Array must not be null");
        long sum = 0;
        for (long value : array) {
            sum += value;
        }
        return sum;
    }

    public static double sum(double... array) {
        AssertUtils.notNull(array, "Array must not be null");
        double sum = 0.0;
        for (double value : array) {
            sum += value;
        }
        return sum;
    }

    public static double round(double value, int scale) {
        return round(value, scale, RoundingMode.HALF_UP);
    }

    public static double round(double value, int scale, RoundingMode roundingMode) {
        AssertUtils.notNull(roundingMode, "RoundingMode must not be null");
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(scale, roundingMode);
        return bd.doubleValue();
    }

    public static BigDecimal round(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal round(BigDecimal value, int scale, RoundingMode roundingMode) {
        AssertUtils.notNull(value, "BigDecimal must not be null");
        AssertUtils.notNull(roundingMode, "RoundingMode must not be null");
        return value.setScale(scale, roundingMode);
    }

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isInRange(long value, long min, long max) {
        return value >= min && value <= max;
    }

    public static boolean isInRange(double value, double min, double max) {
        return Double.compare(value, min) >= 0 && Double.compare(value, max) <= 0;
    }

    public static boolean isInRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        AssertUtils.notNull(value, "Value must not be null");
        AssertUtils.notNull(min, "Min must not be null");
        AssertUtils.notNull(max, "Max must not be null");
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static double clamp(double value, double min, double max) {
        if (Double.compare(value, min) < 0) {
            return min;
        }
        if (Double.compare(value, max) > 0) {
            return max;
        }
        return value;
    }

    public static BigDecimal clamp(BigDecimal value, BigDecimal min, BigDecimal max) {
        AssertUtils.notNull(value, "Value must not be null");
        AssertUtils.notNull(min, "Min must not be null");
        AssertUtils.notNull(max, "Max must not be null");
        if (value.compareTo(min) < 0) {
            return min;
        }
        if (value.compareTo(max) > 0) {
            return max;
        }
        return value;
    }

    public static String toHex(byte[] data) {
        AssertUtils.notNull(data, "Data must not be null");
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] fromHex(String hex) {
        AssertUtils.hasText(hex, "Hex string must not be blank");
        int len = hex.length();
        AssertUtils.isTrue(len % 2 == 0, "Hex string length must be even");
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static String toBinary(long value) {
        return Long.toBinaryString(value);
    }

    public static String toBinary(int value) {
        return Integer.toBinaryString(value);
    }

    public static String toOctal(long value) {
        return Long.toOctalString(value);
    }

    public static String toOctal(int value) {
        return Integer.toOctalString(value);
    }

    public static String toHex(long value) {
        return Long.toHexString(value);
    }

    public static String toHex(int value) {
        return Integer.toHexString(value);
    }

    public static long fromBinary(String binary) {
        AssertUtils.hasText(binary, "Binary string must not be blank");
        return Long.parseLong(binary, 2);
    }

    public static long fromOctal(String octal) {
        AssertUtils.hasText(octal, "Octal string must not be blank");
        return Long.parseLong(octal, 8);
    }

    public static long hexToLong(String hex) {
        AssertUtils.hasText(hex, "Hex string must not be blank");
        return Long.parseLong(hex, 16);
    }

    public static byte[] longToBytes(long value) {
        byte[] result = new byte[LONG_BYTES];
        for (int i = LONG_BYTES - 1; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }

    public static long bytesToLong(byte[] bytes) {
        AssertUtils.notNull(bytes, "Bytes must not be null");
        AssertUtils.isTrue(bytes.length >= LONG_BYTES, "Bytes length must be at least " + LONG_BYTES);
        long result = 0;
        for (int i = 0; i < LONG_BYTES; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xFF);
        }
        return result;
    }

    public static byte[] intToBytes(int value) {
        byte[] result = new byte[INTEGER_BYTES];
        for (int i = INTEGER_BYTES - 1; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return result;
    }

    public static int bytesToInt(byte[] bytes) {
        AssertUtils.notNull(bytes, "Bytes must not be null");
        AssertUtils.isTrue(bytes.length >= INTEGER_BYTES, "Bytes length must be at least " + INTEGER_BYTES);
        int result = 0;
        for (int i = 0; i < INTEGER_BYTES; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xFF);
        }
        return result;
    }

    public static boolean isPositive(Number number) {
        if (number == null) {
            return false;
        }
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).signum() > 0;
        }
        if (number instanceof BigInteger) {
            return ((BigInteger) number).signum() > 0;
        }
        return number.doubleValue() > 0;
    }

    public static boolean isNegative(Number number) {
        if (number == null) {
            return false;
        }
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).signum() < 0;
        }
        if (number instanceof BigInteger) {
            return ((BigInteger) number).signum() < 0;
        }
        return number.doubleValue() < 0;
    }

    public static boolean isZero(Number number) {
        if (number == null) {
            return false;
        }
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).signum() == 0;
        }
        if (number instanceof BigInteger) {
            return ((BigInteger) number).signum() == 0;
        }
        return Double.compare(number.doubleValue(), 0.0) == 0;
    }
}
