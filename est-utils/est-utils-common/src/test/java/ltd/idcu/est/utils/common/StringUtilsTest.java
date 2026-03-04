package ltd.idcu.est.utils.common;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class StringUtilsTest {

    @Test
    public void testIsEmpty() {
        Assertions.assertTrue(StringUtils.isEmpty(null));
        Assertions.assertTrue(StringUtils.isEmpty(""));
        Assertions.assertFalse(StringUtils.isEmpty(" "));
        Assertions.assertFalse(StringUtils.isEmpty("test"));
    }

    @Test
    public void testIsNotEmpty() {
        Assertions.assertFalse(StringUtils.isNotEmpty(null));
        Assertions.assertFalse(StringUtils.isNotEmpty(""));
        Assertions.assertTrue(StringUtils.isNotEmpty(" "));
        Assertions.assertTrue(StringUtils.isNotEmpty("test"));
    }

    @Test
    public void testIsBlank() {
        Assertions.assertTrue(StringUtils.isBlank(null));
        Assertions.assertTrue(StringUtils.isBlank(""));
        Assertions.assertTrue(StringUtils.isBlank(" "));
        Assertions.assertTrue(StringUtils.isBlank("  \t\n  "));
        Assertions.assertFalse(StringUtils.isBlank("test"));
    }

    @Test
    public void testIsNotBlank() {
        Assertions.assertFalse(StringUtils.isNotBlank(null));
        Assertions.assertFalse(StringUtils.isNotBlank(""));
        Assertions.assertFalse(StringUtils.isNotBlank(" "));
        Assertions.assertTrue(StringUtils.isNotBlank("test"));
    }

    @Test
    public void testTrim() {
        Assertions.assertNull(StringUtils.trim(null));
        Assertions.assertEquals("", StringUtils.trim(""));
        Assertions.assertEquals("test", StringUtils.trim("  test  "));
        Assertions.assertEquals("test", StringUtils.trim("test"));
    }

    @Test
    public void testTrimToNull() {
        Assertions.assertNull(StringUtils.trimToNull(null));
        Assertions.assertNull(StringUtils.trimToNull(""));
        Assertions.assertNull(StringUtils.trimToNull("   "));
        Assertions.assertEquals("test", StringUtils.trimToNull("  test  "));
    }

    @Test
    public void testTrimToEmpty() {
        Assertions.assertEquals("", StringUtils.trimToEmpty(null));
        Assertions.assertEquals("", StringUtils.trimToEmpty(""));
        Assertions.assertEquals("", StringUtils.trimToEmpty("   "));
        Assertions.assertEquals("test", StringUtils.trimToEmpty("  test  "));
    }

    @Test
    public void testDefaultIfEmpty() {
        Assertions.assertEquals("default", StringUtils.defaultIfEmpty(null, "default"));
        Assertions.assertEquals("default", StringUtils.defaultIfEmpty("", "default"));
        Assertions.assertEquals("test", StringUtils.defaultIfEmpty("test", "default"));
    }

    @Test
    public void testDefaultIfBlank() {
        Assertions.assertEquals("default", StringUtils.defaultIfBlank(null, "default"));
        Assertions.assertEquals("default", StringUtils.defaultIfBlank("", "default"));
        Assertions.assertEquals("default", StringUtils.defaultIfBlank("   ", "default"));
        Assertions.assertEquals("test", StringUtils.defaultIfBlank("test", "default"));
    }

    @Test
    public void testEquals() {
        Assertions.assertTrue(StringUtils.equals(null, null));
        Assertions.assertFalse(StringUtils.equals(null, "test"));
        Assertions.assertFalse(StringUtils.equals("test", null));
        Assertions.assertTrue(StringUtils.equals("test", "test"));
        Assertions.assertFalse(StringUtils.equals("test", "TEST"));
    }

    @Test
    public void testEqualsIgnoreCase() {
        Assertions.assertTrue(StringUtils.equalsIgnoreCase(null, null));
        Assertions.assertFalse(StringUtils.equalsIgnoreCase(null, "test"));
        Assertions.assertTrue(StringUtils.equalsIgnoreCase("test", "TEST"));
        Assertions.assertTrue(StringUtils.equalsIgnoreCase("Test", "tEsT"));
    }

    @Test
    public void testStartsWith() {
        Assertions.assertTrue(StringUtils.startsWith("test", "te"));
        Assertions.assertFalse(StringUtils.startsWith("test", "st"));
        Assertions.assertTrue(StringUtils.startsWith(null, null));
        Assertions.assertFalse(StringUtils.startsWith("test", null));
        Assertions.assertFalse(StringUtils.startsWith(null, "test"));
    }

    @Test
    public void testStartsWithIgnoreCase() {
        Assertions.assertTrue(StringUtils.startsWithIgnoreCase("Test", "te"));
        Assertions.assertTrue(StringUtils.startsWithIgnoreCase("TEST", "te"));
        Assertions.assertFalse(StringUtils.startsWithIgnoreCase("test", "ST"));
    }

    @Test
    public void testEndsWith() {
        Assertions.assertTrue(StringUtils.endsWith("test", "st"));
        Assertions.assertFalse(StringUtils.endsWith("test", "te"));
        Assertions.assertTrue(StringUtils.endsWith(null, null));
        Assertions.assertFalse(StringUtils.endsWith("test", null));
    }

    @Test
    public void testEndsWithIgnoreCase() {
        Assertions.assertTrue(StringUtils.endsWithIgnoreCase("Test", "ST"));
        Assertions.assertTrue(StringUtils.endsWithIgnoreCase("TEST", "st"));
        Assertions.assertFalse(StringUtils.endsWithIgnoreCase("test", "TE"));
    }

    @Test
    public void testContains() {
        Assertions.assertTrue(StringUtils.contains("test", "es"));
        Assertions.assertFalse(StringUtils.contains("test", "xyz"));
        Assertions.assertFalse(StringUtils.contains(null, "test"));
        Assertions.assertFalse(StringUtils.contains("test", null));
    }

    @Test
    public void testContainsIgnoreCase() {
        Assertions.assertTrue(StringUtils.containsIgnoreCase("Test", "ES"));
        Assertions.assertTrue(StringUtils.containsIgnoreCase("TEST", "es"));
        Assertions.assertFalse(StringUtils.containsIgnoreCase("test", "XYZ"));
    }

    @Test
    public void testIndexOf() {
        Assertions.assertEquals(1, StringUtils.indexOf("test", "es"));
        Assertions.assertEquals(-1, StringUtils.indexOf("test", "xyz"));
        Assertions.assertEquals(-1, StringUtils.indexOf(null, "test"));
    }

    @Test
    public void testLastIndexOf() {
        Assertions.assertEquals(6, StringUtils.lastIndexOf("test test", "es"));
        Assertions.assertEquals(8, StringUtils.lastIndexOf("test test", "t"));
        Assertions.assertEquals(-1, StringUtils.lastIndexOf("test", "xyz"));
    }

    @Test
    public void testSubstring() {
        Assertions.assertEquals("test", StringUtils.substring("test", 0));
        Assertions.assertEquals("st", StringUtils.substring("test", 2));
        Assertions.assertEquals("est", StringUtils.substring("test", -3));
        Assertions.assertNull(StringUtils.substring(null, 0));
    }

    @Test
    public void testSubstringWithEnd() {
        Assertions.assertEquals("es", StringUtils.substring("test", 1, 3));
        Assertions.assertEquals("te", StringUtils.substring("test", 0, -2));
        Assertions.assertNull(StringUtils.substring(null, 0, 2));
    }

    @Test
    public void testLeft() {
        Assertions.assertEquals("te", StringUtils.left("test", 2));
        Assertions.assertEquals("test", StringUtils.left("test", 10));
        Assertions.assertEquals("", StringUtils.left("test", 0));
        Assertions.assertNull(StringUtils.left(null, 2));
    }

    @Test
    public void testRight() {
        Assertions.assertEquals("st", StringUtils.right("test", 2));
        Assertions.assertEquals("test", StringUtils.right("test", 10));
        Assertions.assertEquals("", StringUtils.right("test", 0));
        Assertions.assertNull(StringUtils.right(null, 2));
    }

    @Test
    public void testMid() {
        Assertions.assertEquals("es", StringUtils.mid("test", 1, 2));
        Assertions.assertEquals("est", StringUtils.mid("test", 1, 10));
        Assertions.assertEquals("", StringUtils.mid("test", 10, 2));
        Assertions.assertNull(StringUtils.mid(null, 1, 2));
    }

    @Test
    public void testSplit() {
        String[] result1 = StringUtils.split("a,b,c", ",");
        Assertions.assertEquals(3, result1.length);
        Assertions.assertEquals("a", result1[0]);
        Assertions.assertEquals("b", result1[1]);
        Assertions.assertEquals("c", result1[2]);
        
        String[] result2 = StringUtils.split(null, ",");
        Assertions.assertEquals(0, result2.length);
        
        String[] result3 = StringUtils.split("a b c", null);
        Assertions.assertEquals(3, result3.length);
    }

    @Test
    public void testJoin() {
        String[] array = {"a", "b", "c"};
        Assertions.assertEquals("a,b,c", StringUtils.join(array, ","));
        Assertions.assertEquals("abc", StringUtils.join(array, ""));
        Assertions.assertNull(StringUtils.join((Object[]) null, ","));
        Assertions.assertEquals("", StringUtils.join(new Object[0], ","));
    }

    @Test
    public void testRepeat() {
        Assertions.assertEquals("ababab", StringUtils.repeat("ab", 3));
        Assertions.assertEquals("", StringUtils.repeat("ab", 0));
        Assertions.assertEquals("", StringUtils.repeat("ab", -1));
        Assertions.assertEquals("ab", StringUtils.repeat("ab", 1));
        Assertions.assertNull(StringUtils.repeat(null, 3));
    }

    @Test
    public void testCapitalize() {
        Assertions.assertEquals("Test", StringUtils.capitalize("test"));
        Assertions.assertEquals("Test", StringUtils.capitalize("Test"));
        Assertions.assertEquals("", StringUtils.capitalize(""));
        Assertions.assertNull(StringUtils.capitalize(null));
    }

    @Test
    public void testUncapitalize() {
        Assertions.assertEquals("test", StringUtils.uncapitalize("Test"));
        Assertions.assertEquals("test", StringUtils.uncapitalize("test"));
        Assertions.assertEquals("", StringUtils.uncapitalize(""));
        Assertions.assertNull(StringUtils.uncapitalize(null));
    }

    @Test
    public void testUpperCase() {
        Assertions.assertEquals("TEST", StringUtils.upperCase("test"));
        Assertions.assertEquals("TEST", StringUtils.upperCase("TEST"));
        Assertions.assertNull(StringUtils.upperCase(null));
    }

    @Test
    public void testLowerCase() {
        Assertions.assertEquals("test", StringUtils.lowerCase("TEST"));
        Assertions.assertEquals("test", StringUtils.lowerCase("test"));
        Assertions.assertNull(StringUtils.lowerCase(null));
    }

    @Test
    public void testReverse() {
        Assertions.assertEquals("tset", StringUtils.reverse("test"));
        Assertions.assertEquals("", StringUtils.reverse(""));
        Assertions.assertNull(StringUtils.reverse(null));
    }

    @Test
    public void testReplace() {
        Assertions.assertEquals("tXXt", StringUtils.replace("test", "es", "XX"));
        Assertions.assertNull(StringUtils.replace(null, "es", "XX"));
    }

    @Test
    public void testReplaceAll() {
        Assertions.assertEquals("tXXt", StringUtils.replaceAll("test", "es", "XX"));
        Assertions.assertEquals("tXXt", StringUtils.replaceAll("test", "[es]", "X"));
    }

    @Test
    public void testReplaceFirst() {
        Assertions.assertEquals("tXXt", StringUtils.replaceFirst("test", "es", "XX"));
        Assertions.assertEquals("tXtest", StringUtils.replaceFirst("testest", "es", "X"));
    }

    @Test
    public void testRemove() {
        Assertions.assertEquals("tt", StringUtils.remove("test", "es"));
        Assertions.assertEquals("test", StringUtils.remove("test", "xyz"));
        Assertions.assertEquals("", StringUtils.remove("", "es"));
    }

    @Test
    public void testRemoveStart() {
        Assertions.assertEquals("st", StringUtils.removeStart("test", "te"));
        Assertions.assertEquals("test", StringUtils.removeStart("test", "xyz"));
    }

    @Test
    public void testRemoveEnd() {
        Assertions.assertEquals("te", StringUtils.removeEnd("test", "st"));
        Assertions.assertEquals("test", StringUtils.removeEnd("test", "xyz"));
    }

    @Test
    public void testPadLeft() {
        Assertions.assertEquals("  test", StringUtils.padLeft("test", 6, ' '));
        Assertions.assertEquals("00test", StringUtils.padLeft("test", 6, '0'));
        Assertions.assertEquals("test", StringUtils.padLeft("test", 2, ' '));
        Assertions.assertEquals("  ", StringUtils.padLeft(null, 2, ' '));
    }

    @Test
    public void testPadRight() {
        Assertions.assertEquals("test  ", StringUtils.padRight("test", 6, ' '));
        Assertions.assertEquals("test00", StringUtils.padRight("test", 6, '0'));
        Assertions.assertEquals("test", StringUtils.padRight("test", 2, ' '));
    }

    @Test
    public void testCenter() {
        Assertions.assertEquals(" test  ", StringUtils.center("test", 7, ' '));
        Assertions.assertEquals("test", StringUtils.center("test", 2, ' '));
    }

    @Test
    public void testTruncate() {
        Assertions.assertEquals("test", StringUtils.truncate("test", 10));
        Assertions.assertEquals("te...", StringUtils.truncate("test test", 5));
        Assertions.assertNull(StringUtils.truncate(null, 10));
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(0, StringUtils.length(null));
        Assertions.assertEquals(0, StringUtils.length(""));
        Assertions.assertEquals(4, StringUtils.length("test"));
    }

    @Test
    public void testCountMatches() {
        Assertions.assertEquals(4, StringUtils.countMatches("test test", "t"));
        Assertions.assertEquals(2, StringUtils.countMatches("test test", "es"));
        Assertions.assertEquals(0, StringUtils.countMatches("test", "xyz"));
        Assertions.assertEquals(0, StringUtils.countMatches(null, "test"));
    }

    @Test
    public void testIsNumeric() {
        Assertions.assertTrue(StringUtils.isNumeric("123"));
        Assertions.assertFalse(StringUtils.isNumeric("12a3"));
        Assertions.assertFalse(StringUtils.isNumeric(""));
        Assertions.assertFalse(StringUtils.isNumeric(null));
    }

    @Test
    public void testIsAlpha() {
        Assertions.assertTrue(StringUtils.isAlpha("abc"));
        Assertions.assertFalse(StringUtils.isAlpha("a1c"));
        Assertions.assertFalse(StringUtils.isAlpha(""));
        Assertions.assertFalse(StringUtils.isAlpha(null));
    }

    @Test
    public void testIsAlphanumeric() {
        Assertions.assertTrue(StringUtils.isAlphanumeric("a1c"));
        Assertions.assertFalse(StringUtils.isAlphanumeric("a!c"));
        Assertions.assertFalse(StringUtils.isAlphanumeric(""));
        Assertions.assertFalse(StringUtils.isAlphanumeric(null));
    }

    @Test
    public void testIsWhitespace() {
        Assertions.assertTrue(StringUtils.isWhitespace("   "));
        Assertions.assertTrue(StringUtils.isWhitespace("\t\n"));
        Assertions.assertFalse(StringUtils.isWhitespace("test"));
        Assertions.assertFalse(StringUtils.isWhitespace(null));
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("null", StringUtils.toString(null));
        Assertions.assertEquals("test", StringUtils.toString("test"));
        Assertions.assertEquals("123", StringUtils.toString(123));
    }

    @Test
    public void testDefaultString() {
        Assertions.assertEquals("", StringUtils.defaultString(null));
        Assertions.assertEquals("test", StringUtils.defaultString("test"));
        Assertions.assertEquals("default", StringUtils.defaultString(null, "default"));
    }

    @Test
    public void testStrip() {
        Assertions.assertEquals("test", StringUtils.strip("  test  "));
        Assertions.assertEquals("test", StringUtils.strip("  test  ", null));
    }

    @Test
    public void testStripStart() {
        Assertions.assertEquals("test  ", StringUtils.stripStart("  test  ", null));
        Assertions.assertEquals("testx", StringUtils.stripStart("xtestx", "x"));
    }

    @Test
    public void testStripEnd() {
        Assertions.assertEquals("  test", StringUtils.stripEnd("  test  ", null));
        Assertions.assertEquals("xtest", StringUtils.stripEnd("xtestx", "x"));
    }

    @Test
    public void testAbbreviate() {
        Assertions.assertEquals("test", StringUtils.abbreviate("test", 10));
        Assertions.assertEquals("tes...", StringUtils.abbreviate("test test", 6));
        Assertions.assertNull(StringUtils.abbreviate(null, 10));
    }

    @Test
    public void testChomp() {
        Assertions.assertEquals("test", StringUtils.chomp("test\n"));
        Assertions.assertEquals("test", StringUtils.chomp("test\r\n"));
        Assertions.assertEquals("test", StringUtils.chomp("test\r"));
        Assertions.assertEquals("", StringUtils.chomp("\n"));
    }

    @Test
    public void testChop() {
        Assertions.assertEquals("tes", StringUtils.chop("test"));
        Assertions.assertEquals("", StringUtils.chop("t"));
        Assertions.assertEquals("", StringUtils.chop(""));
        Assertions.assertNull(StringUtils.chop(null));
    }
}
