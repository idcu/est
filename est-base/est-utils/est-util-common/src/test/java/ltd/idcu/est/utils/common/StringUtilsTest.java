package ltd.idcu.est.utils.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class StringUtilsTest {

    @Test
    void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("test"));
    }

    @Test
    void testIsNotEmpty() {
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("test"));
    }

    @Test
    void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("   "));
        assertFalse(StringUtils.isBlank("test"));
        assertFalse(StringUtils.isBlank("  test  "));
    }

    @Test
    void testIsNotBlank() {
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertTrue(StringUtils.isNotBlank("test"));
    }

    @Test
    void testTrim() {
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("test", StringUtils.trim("test"));
        assertEquals("test", StringUtils.trim("  test  "));
    }

    @Test
    void testTrimToNull() {
        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("   "));
        assertEquals("test", StringUtils.trimToNull("test"));
        assertEquals("test", StringUtils.trimToNull("  test  "));
    }

    @Test
    void testTrimToEmpty() {
        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("   "));
        assertEquals("test", StringUtils.trimToEmpty("test"));
        assertEquals("test", StringUtils.trimToEmpty("  test  "));
    }

    @Test
    void testDefaultIfEmpty() {
        assertEquals("default", StringUtils.defaultIfEmpty(null, "default"));
        assertEquals("default", StringUtils.defaultIfEmpty("", "default"));
        assertEquals("test", StringUtils.defaultIfEmpty("test", "default"));
        assertEquals(" ", StringUtils.defaultIfEmpty(" ", "default"));
    }

    @Test
    void testDefaultIfBlank() {
        assertEquals("default", StringUtils.defaultIfBlank(null, "default"));
        assertEquals("default", StringUtils.defaultIfBlank("", "default"));
        assertEquals("default", StringUtils.defaultIfBlank("   ", "default"));
        assertEquals("test", StringUtils.defaultIfBlank("test", "default"));
        assertEquals("  test  ", StringUtils.defaultIfBlank("  test  ", "default"));
    }

    @Test
    void testEquals() {
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "test"));
        assertFalse(StringUtils.equals("test", null));
        assertTrue(StringUtils.equals("test", "test"));
        assertFalse(StringUtils.equals("test", "TEST"));
    }

    @Test
    void testEqualsIgnoreCase() {
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase(null, "test"));
        assertFalse(StringUtils.equalsIgnoreCase("test", null));
        assertTrue(StringUtils.equalsIgnoreCase("test", "test"));
        assertTrue(StringUtils.equalsIgnoreCase("test", "TEST"));
        assertFalse(StringUtils.equalsIgnoreCase("test", "tests"));
    }

    @Test
    void testCompare() {
        assertEquals(0, StringUtils.compare(null, null));
        assertEquals(-1, StringUtils.compare(null, "test"));
        assertEquals(1, StringUtils.compare("test", null));
        assertEquals(0, StringUtils.compare("test", "test"));
        assertTrue(StringUtils.compare("a", "b") < 0);
        assertTrue(StringUtils.compare("b", "a") > 0);
    }

    @Test
    void testCompareWithNullIsLess() {
        assertEquals(0, StringUtils.compare(null, null, true));
        assertEquals(-1, StringUtils.compare(null, "test", true));
        assertEquals(1, StringUtils.compare("test", null, true));
        assertEquals(0, StringUtils.compare("test", "test", true));
        
        assertEquals(0, StringUtils.compare(null, null, false));
        assertEquals(1, StringUtils.compare(null, "test", false));
        assertEquals(-1, StringUtils.compare("test", null, false));
    }

    @Test
    void testCompareIgnoreCase() {
        assertEquals(0, StringUtils.compareIgnoreCase(null, null));
        assertEquals(-1, StringUtils.compareIgnoreCase(null, "test"));
        assertEquals(1, StringUtils.compareIgnoreCase("test", null));
        assertEquals(0, StringUtils.compareIgnoreCase("test", "test"));
        assertEquals(0, StringUtils.compareIgnoreCase("test", "TEST"));
        assertTrue(StringUtils.compareIgnoreCase("a", "B") < 0);
        assertTrue(StringUtils.compareIgnoreCase("B", "a") > 0);
    }

    @Test
    void testStartsWith() {
        assertTrue(StringUtils.startsWith(null, null));
        assertFalse(StringUtils.startsWith(null, "test"));
        assertFalse(StringUtils.startsWith("test", null));
        assertTrue(StringUtils.startsWith("test", "te"));
        assertFalse(StringUtils.startsWith("test", "TE"));
        assertFalse(StringUtils.startsWith("test", "est"));
    }

    @Test
    void testStartsWithIgnoreCase() {
        assertTrue(StringUtils.startsWithIgnoreCase(null, null));
        assertFalse(StringUtils.startsWithIgnoreCase(null, "test"));
        assertFalse(StringUtils.startsWithIgnoreCase("test", null));
        assertTrue(StringUtils.startsWithIgnoreCase("test", "te"));
        assertTrue(StringUtils.startsWithIgnoreCase("test", "TE"));
        assertFalse(StringUtils.startsWithIgnoreCase("test", "est"));
        assertFalse(StringUtils.startsWithIgnoreCase("test", "longprefix"));
    }

    @Test
    void testEndsWith() {
        assertTrue(StringUtils.endsWith(null, null));
        assertFalse(StringUtils.endsWith(null, "test"));
        assertFalse(StringUtils.endsWith("test", null));
        assertTrue(StringUtils.endsWith("test", "st"));
        assertFalse(StringUtils.endsWith("test", "ST"));
        assertFalse(StringUtils.endsWith("test", "te"));
    }

    @Test
    void testEndsWithIgnoreCase() {
        assertTrue(StringUtils.endsWithIgnoreCase(null, null));
        assertFalse(StringUtils.endsWithIgnoreCase(null, "test"));
        assertFalse(StringUtils.endsWithIgnoreCase("test", null));
        assertTrue(StringUtils.endsWithIgnoreCase("test", "st"));
        assertTrue(StringUtils.endsWithIgnoreCase("test", "ST"));
        assertFalse(StringUtils.endsWithIgnoreCase("test", "te"));
        assertFalse(StringUtils.endsWithIgnoreCase("test", "longsuffix"));
    }

    @Test
    void testContains() {
        assertFalse(StringUtils.contains(null, null));
        assertFalse(StringUtils.contains(null, "test"));
        assertFalse(StringUtils.contains("test", null));
        assertTrue(StringUtils.contains("test", "es"));
        assertFalse(StringUtils.contains("test", "ES"));
        assertFalse(StringUtils.contains("test", "xyz"));
    }

    @Test
    void testContainsIgnoreCase() {
        assertFalse(StringUtils.containsIgnoreCase(null, null));
        assertFalse(StringUtils.containsIgnoreCase(null, "test"));
        assertFalse(StringUtils.containsIgnoreCase("test", null));
        assertTrue(StringUtils.containsIgnoreCase("test", "es"));
        assertTrue(StringUtils.containsIgnoreCase("test", "ES"));
        assertFalse(StringUtils.containsIgnoreCase("test", "xyz"));
    }

    @Test
    void testIndexOf() {
        assertEquals(-1, StringUtils.indexOf(null, null));
        assertEquals(-1, StringUtils.indexOf(null, "test"));
        assertEquals(-1, StringUtils.indexOf("test", null));
        assertEquals(1, StringUtils.indexOf("test", "es"));
        assertEquals(-1, StringUtils.indexOf("test", "xyz"));
    }

    @Test
    void testLastIndexOf() {
        assertEquals(-1, StringUtils.lastIndexOf(null, null));
        assertEquals(-1, StringUtils.lastIndexOf(null, "test"));
        assertEquals(-1, StringUtils.lastIndexOf("test", null));
        assertEquals(1, StringUtils.lastIndexOf("test", "es"));
        assertEquals(5, StringUtils.lastIndexOf("testtest", "es"));
        assertEquals(-1, StringUtils.lastIndexOf("test", "xyz"));
    }

    @Test
    void testSubstring() {
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("test", StringUtils.substring("test", 0));
        assertEquals("est", StringUtils.substring("test", 1));
        assertEquals("st", StringUtils.substring("test", -2));
        assertEquals("test", StringUtils.substring("test", -5));
        assertEquals("", StringUtils.substring("test", 10));
    }

    @Test
    void testSubstringWithRange() {
        assertNull(StringUtils.substring(null, 0, 2));
        assertEquals("", StringUtils.substring("", 0, 2));
        assertEquals("te", StringUtils.substring("test", 0, 2));
        assertEquals("es", StringUtils.substring("test", 1, 3));
        assertEquals("st", StringUtils.substring("test", 2, 10));
        assertEquals("", StringUtils.substring("test", 3, 1));
        assertEquals("es", StringUtils.substring("test", 1, -1));
    }

    @Test
    void testLeft() {
        assertNull(StringUtils.left(null, 2));
        assertEquals("", StringUtils.left("test", 0));
        assertEquals("", StringUtils.left("test", -1));
        assertEquals("te", StringUtils.left("test", 2));
        assertEquals("test", StringUtils.left("test", 10));
    }

    @Test
    void testRight() {
        assertNull(StringUtils.right(null, 2));
        assertEquals("", StringUtils.right("test", 0));
        assertEquals("", StringUtils.right("test", -1));
        assertEquals("st", StringUtils.right("test", 2));
        assertEquals("test", StringUtils.right("test", 10));
    }

    @Test
    void testMid() {
        assertNull(StringUtils.mid(null, 0, 2));
        assertEquals("", StringUtils.mid("test", 0, 0));
        assertEquals("", StringUtils.mid("test", 0, -1));
        assertEquals("es", StringUtils.mid("test", 1, 2));
        assertEquals("est", StringUtils.mid("test", 1, 10));
        assertEquals("", StringUtils.mid("test", 10, 2));
        assertEquals("test", StringUtils.mid("test", -1, 10));
    }

    @Test
    void testSplit() {
        assertArrayEquals(new String[0], StringUtils.split(null, ","));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a,b,c", ","));
        assertArrayEquals(new String[]{"a", "b", "c"}, StringUtils.split("a,b,,c", ","));
    }

    @Test
    void testJoinArray() {
        Object[] nullArray = null;
        assertNull(StringUtils.join(nullArray, ","));
        assertEquals("", StringUtils.join(new Object[0], ","));
        assertEquals("a,b,c", StringUtils.join(new Object[]{"a", "b", "c"}, ","));
        assertEquals("a,,c", StringUtils.join(new Object[]{"a", null, "c"}, ","));
    }

    @Test
    void testJoinIterable() {
        assertNull(StringUtils.join((Iterable<?>) null, ","));
        Iterable<String> iterable = Arrays.asList("a", "b", "c");
        assertEquals("a,b,c", StringUtils.join(iterable, ","));
    }

    @Test
    void testRepeat() {
        assertNull(StringUtils.repeat(null, 2));
        assertEquals("", StringUtils.repeat("test", 0));
        assertEquals("", StringUtils.repeat("test", -1));
        assertEquals("test", StringUtils.repeat("test", 1));
        assertEquals("testtest", StringUtils.repeat("test", 2));
    }

    @Test
    void testCapitalize() {
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("Test", StringUtils.capitalize("test"));
        assertEquals("Test", StringUtils.capitalize("Test"));
    }

    @Test
    void testUncapitalize() {
        assertNull(StringUtils.uncapitalize(null));
        assertEquals("", StringUtils.uncapitalize(""));
        assertEquals("test", StringUtils.uncapitalize("Test"));
        assertEquals("test", StringUtils.uncapitalize("test"));
    }

    @Test
    void testUpperCase() {
        assertNull(StringUtils.upperCase(null));
        assertEquals("", StringUtils.upperCase(""));
        assertEquals("TEST", StringUtils.upperCase("test"));
        assertEquals("TEST", StringUtils.upperCase("TEST"));
    }

    @Test
    void testLowerCase() {
        assertNull(StringUtils.lowerCase(null));
        assertEquals("", StringUtils.lowerCase(""));
        assertEquals("test", StringUtils.lowerCase("Test"));
        assertEquals("test", StringUtils.lowerCase("test"));
    }

    @Test
    void testReverse() {
        assertNull(StringUtils.reverse(null));
        assertEquals("", StringUtils.reverse(""));
        assertEquals("tset", StringUtils.reverse("test"));
    }

    @Test
    void testReplace() {
        assertNull(StringUtils.replace(null, "a", "b"));
        assertEquals("test", StringUtils.replace("test", null, "b"));
        assertEquals("test", StringUtils.replace("test", "a", null));
        assertEquals("tbst", StringUtils.replace("test", "e", "b"));
        assertEquals("tXXt", StringUtils.replace("test", "es", "XX"));
    }

    @Test
    void testRemove() {
        assertNull(StringUtils.remove(null, "a"));
        assertEquals("", StringUtils.remove("", "a"));
        assertEquals("tst", StringUtils.remove("test", "e"));
        assertEquals("test", StringUtils.remove("test", "x"));
    }

    @Test
    void testRemoveStart() {
        assertNull(StringUtils.removeStart(null, "a"));
        assertEquals("", StringUtils.removeStart("", "a"));
        assertEquals("st", StringUtils.removeStart("test", "te"));
        assertEquals("test", StringUtils.removeStart("test", "x"));
    }

    @Test
    void testRemoveEnd() {
        assertNull(StringUtils.removeEnd(null, "a"));
        assertEquals("", StringUtils.removeEnd("", "a"));
        assertEquals("te", StringUtils.removeEnd("test", "st"));
        assertEquals("test", StringUtils.removeEnd("test", "x"));
    }

    @Test
    void testPadLeft() {
        assertEquals("  test", StringUtils.padLeft("test", 6, ' '));
        assertEquals("test", StringUtils.padLeft("test", 4, ' '));
        assertEquals("test", StringUtils.padLeft("test", 2, ' '));
        assertEquals("0000", StringUtils.padLeft(null, 4, '0'));
    }

    @Test
    void testPadRight() {
        assertEquals("test  ", StringUtils.padRight("test", 6, ' '));
        assertEquals("test", StringUtils.padRight("test", 4, ' '));
        assertEquals("test", StringUtils.padRight("test", 2, ' '));
        assertEquals("0000", StringUtils.padRight(null, 4, '0'));
    }

    @Test
    void testCenter() {
        assertEquals(" test ", StringUtils.center("test", 6, ' '));
        assertEquals("  test  ", StringUtils.center("test", 8, ' '));
        assertEquals("test", StringUtils.center("test", 4, ' '));
        assertEquals("0000", StringUtils.center(null, 4, '0'));
    }

    @Test
    void testTruncate() {
        assertNull(StringUtils.truncate(null, 10));
        assertEquals("test", StringUtils.truncate("test", 10));
        assertEquals("te...", StringUtils.truncate("testtest", 5));
        assertEquals("...", StringUtils.truncate("testtest", 3));
    }

    @Test
    void testLength() {
        assertEquals(0, StringUtils.length(null));
        assertEquals(0, StringUtils.length(""));
        assertEquals(4, StringUtils.length("test"));
    }

    @Test
    void testCountMatches() {
        assertEquals(0, StringUtils.countMatches(null, "a"));
        assertEquals(0, StringUtils.countMatches("test", null));
        assertEquals(0, StringUtils.countMatches("", "a"));
        assertEquals(1, StringUtils.countMatches("test", "e"));
        assertEquals(2, StringUtils.countMatches("testtest", "es"));
    }

    @Test
    void testIsNumeric() {
        assertFalse(StringUtils.isNumeric(null));
        assertFalse(StringUtils.isNumeric(""));
        assertFalse(StringUtils.isNumeric(" "));
        assertTrue(StringUtils.isNumeric("123"));
        assertFalse(StringUtils.isNumeric("123a"));
        assertFalse(StringUtils.isNumeric("12.3"));
    }

    @Test
    void testIsAlpha() {
        assertFalse(StringUtils.isAlpha(null));
        assertFalse(StringUtils.isAlpha(""));
        assertFalse(StringUtils.isAlpha(" "));
        assertTrue(StringUtils.isAlpha("abc"));
        assertFalse(StringUtils.isAlpha("abc123"));
    }

    @Test
    void testIsAlphanumeric() {
        assertFalse(StringUtils.isAlphanumeric(null));
        assertFalse(StringUtils.isAlphanumeric(""));
        assertFalse(StringUtils.isAlphanumeric(" "));
        assertTrue(StringUtils.isAlphanumeric("abc123"));
        assertFalse(StringUtils.isAlphanumeric("abc123!"));
    }

    @Test
    void testIsWhitespace() {
        assertFalse(StringUtils.isWhitespace(null));
        assertTrue(StringUtils.isWhitespace(""));
        assertTrue(StringUtils.isWhitespace(" "));
        assertTrue(StringUtils.isWhitespace("   "));
        assertFalse(StringUtils.isWhitespace(" test "));
    }

    @Test
    void testToString() {
        assertEquals("null", StringUtils.toString(null));
        assertEquals("test", StringUtils.toString("test"));
        assertEquals("123", StringUtils.toString(123));
    }

    @Test
    void testDefaultString() {
        assertEquals("", StringUtils.defaultString(null));
        assertEquals("test", StringUtils.defaultString("test"));
        assertEquals("default", StringUtils.defaultString(null, "default"));
        assertEquals("test", StringUtils.defaultString("test", "default"));
    }

    @Test
    void testStrip() {
        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("test", StringUtils.strip("  test  "));
        assertEquals("test", StringUtils.strip("  test  ", null));
        assertEquals("test", StringUtils.strip("xtestx", "x"));
    }

    @Test
    void testStripStart() {
        assertNull(StringUtils.stripStart(null, null));
        assertEquals("", StringUtils.stripStart("", null));
        assertEquals("test  ", StringUtils.stripStart("  test  ", null));
        assertEquals("testx", StringUtils.stripStart("xtestx", "x"));
    }

    @Test
    void testStripEnd() {
        assertNull(StringUtils.stripEnd(null, null));
        assertEquals("", StringUtils.stripEnd("", null));
        assertEquals("  test", StringUtils.stripEnd("  test  ", null));
        assertEquals("xtest", StringUtils.stripEnd("xtestx", "x"));
    }

    @Test
    void testChomp() {
        assertNull(StringUtils.chomp(null));
        assertEquals("", StringUtils.chomp(""));
        assertEquals("test", StringUtils.chomp("test\n"));
        assertEquals("test", StringUtils.chomp("test\r\n"));
        assertEquals("test", StringUtils.chomp("test\r"));
        assertEquals("test", StringUtils.chomp("test"));
    }

    @Test
    void testChop() {
        assertNull(StringUtils.chop(null));
        assertEquals("", StringUtils.chop(""));
        assertEquals("", StringUtils.chop("x"));
        assertEquals("tes", StringUtils.chop("test"));
        assertEquals("test", StringUtils.chop("test\n"));
        assertEquals("test", StringUtils.chop("test\r\n"));
    }
}
