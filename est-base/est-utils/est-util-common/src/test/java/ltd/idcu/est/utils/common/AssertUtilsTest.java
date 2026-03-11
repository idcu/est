package ltd.idcu.est.utils.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class AssertUtilsTest {

    @Test
    void testIsTrue() {
        AssertUtils.isTrue(true, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isTrue(false, "error"));
        
        AssertUtils.isTrue(true, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isTrue(false, () -> "error"));
    }

    @Test
    void testIsFalse() {
        AssertUtils.isFalse(false, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isFalse(true, "error"));
        
        AssertUtils.isFalse(false, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isFalse(true, () -> "error"));
    }

    @Test
    void testIsNull() {
        AssertUtils.isNull(null, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isNull("not null", "error"));
        
        AssertUtils.isNull(null, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isNull("not null", () -> "error"));
    }

    @Test
    void testNotNull() {
        AssertUtils.notNull("not null", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notNull(null, "error"));
        
        AssertUtils.notNull("not null", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notNull(null, () -> "error"));
    }

    @Test
    void testRequireNonNull() {
        assertEquals("test", AssertUtils.requireNonNull("test", "message"));
        assertThrows(NullPointerException.class, () -> AssertUtils.requireNonNull(null, "error"));
        
        assertEquals("test", AssertUtils.requireNonNull("test", () -> "message"));
        assertThrows(NullPointerException.class, () -> AssertUtils.requireNonNull(null, () -> "error"));
    }

    @Test
    void testRequireNonNullElse() {
        assertEquals("test", AssertUtils.requireNonNullElse("test", "default"));
        assertEquals("default", AssertUtils.requireNonNullElse(null, "default"));
        assertThrows(NullPointerException.class, () -> AssertUtils.requireNonNullElse(null, null));
    }

    @Test
    void testRequireNonNullElseGet() {
        assertEquals("test", AssertUtils.requireNonNullElseGet("test", () -> "default"));
        assertEquals("default", AssertUtils.requireNonNullElseGet(null, () -> "default"));
        assertThrows(NullPointerException.class, () -> AssertUtils.requireNonNullElseGet(null, null));
        assertThrows(NullPointerException.class, () -> AssertUtils.requireNonNullElseGet(null, () -> null));
    }

    @Test
    void testHasLength() {
        AssertUtils.hasLength("test", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.hasLength(null, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.hasLength("", "error"));
        
        AssertUtils.hasLength("test", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.hasLength(null, () -> "error"));
    }

    @Test
    void testHasText() {
        AssertUtils.hasText("test", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.hasText(null, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.hasText("", "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.hasText("   ", "error"));
        
        AssertUtils.hasText("test", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.hasText(null, () -> "error"));
    }

    @Test
    void testDoesNotContain() {
        AssertUtils.doesNotContain("test", "xyz", "message");
        AssertUtils.doesNotContain(null, "xyz", "message");
        AssertUtils.doesNotContain("test", null, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.doesNotContain("test", "es", "error"));
        
        AssertUtils.doesNotContain("test", "xyz", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.doesNotContain("test", "es", () -> "error"));
    }

    @Test
    void testNotEmptyArray() {
        AssertUtils.notEmpty(new Object[]{"a"}, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty(null, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty(new Object[0], "error"));
        
        AssertUtils.notEmpty(new Object[]{"a"}, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty(null, () -> "error"));
    }

    @Test
    void testNotEmptyCollection() {
        AssertUtils.notEmpty(Arrays.asList("a"), "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty((Collection<?>) null, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty(new ArrayList<>(), "error"));
        
        AssertUtils.notEmpty(Arrays.asList("a"), () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty((Collection<?>) null, () -> "error"));
    }

    @Test
    void testNotEmptyMap() {
        AssertUtils.notEmpty(Map.of("a", 1), "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty((Map<?, ?>) null, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty(new HashMap<>(), "error"));
        
        AssertUtils.notEmpty(Map.of("a", 1), () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty((Map<?, ?>) null, () -> "error"));
    }

    @Test
    void testNoNullElementsArray() {
        AssertUtils.noNullElements(new Object[]{"a", "b"}, "message");
        AssertUtils.noNullElements((Object[]) null, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.noNullElements(new Object[]{"a", null}, "error"));
        
        AssertUtils.noNullElements(new Object[]{"a", "b"}, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.noNullElements(new Object[]{"a", null}, () -> "error"));
    }

    @Test
    void testNoNullElementsCollection() {
        AssertUtils.noNullElements(Arrays.asList("a", "b"), "message");
        AssertUtils.noNullElements((Collection<?>) null, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.noNullElements(Arrays.asList("a", null), "error"));
        
        AssertUtils.noNullElements(Arrays.asList("a", "b"), () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.noNullElements(Arrays.asList("a", null), () -> "error"));
    }

    @Test
    void testIsInstanceOf() {
        AssertUtils.isInstanceOf(String.class, "test", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isInstanceOf(String.class, 123, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isInstanceOf(null, "test", "error"));
        
        AssertUtils.isInstanceOf(String.class, "test", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isInstanceOf(String.class, 123, () -> "error"));
        
        AssertUtils.isInstanceOf(String.class, "test");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isInstanceOf(String.class, 123));
    }

    @Test
    void testIsAssignable() {
        AssertUtils.isAssignable(CharSequence.class, String.class, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isAssignable(String.class, Integer.class, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isAssignable(null, String.class, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isAssignable(String.class, null, "error"));
        
        AssertUtils.isAssignable(CharSequence.class, String.class, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isAssignable(String.class, Integer.class, () -> "error"));
        
        AssertUtils.isAssignable(CharSequence.class, String.class);
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isAssignable(String.class, Integer.class));
    }

    @Test
    void testState() {
        AssertUtils.state(true, "message");
        assertThrows(IllegalStateException.class, () -> AssertUtils.state(false, "error"));
        
        AssertUtils.state(true, () -> "message");
        assertThrows(IllegalStateException.class, () -> AssertUtils.state(false, () -> "error"));
    }

    @Test
    void testInRange() {
        AssertUtils.inRange(5, 1, 10, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.inRange(0, 1, 10, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.inRange(11, 1, 10, "error"));
        
        AssertUtils.inRange(5, 1, 10, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.inRange(0, 1, 10, () -> "error"));
        
        AssertUtils.inRange(5L, 1L, 10L, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.inRange(0L, 1L, 10L, "error"));
        
        AssertUtils.inRange(5L, 1L, 10L, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.inRange(0L, 1L, 10L, () -> "error"));
        
        AssertUtils.inRange(5.0, 1.0, 10.0, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.inRange(0.0, 1.0, 10.0, "error"));
        
        AssertUtils.inRange(5.0, 1.0, 10.0, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.inRange(0.0, 1.0, 10.0, () -> "error"));
    }

    @Test
    void testIsPositive() {
        AssertUtils.isPositive(5, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isPositive(0, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isPositive(-1, "error"));
        
        AssertUtils.isPositive(5, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isPositive(0, () -> "error"));
        
        AssertUtils.isPositive(5L, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isPositive(0L, "error"));
        
        AssertUtils.isPositive(5L, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isPositive(0L, () -> "error"));
        
        AssertUtils.isPositive(5.0, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isPositive(0.0, "error"));
        
        AssertUtils.isPositive(5.0, () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isPositive(0.0, () -> "error"));
    }

    @Test
    void testIsNegative() {
        AssertUtils.isNegative(-5, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isNegative(0, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isNegative(1, "error"));
        
        AssertUtils.isNegative(-5L, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isNegative(0L, "error"));
        
        AssertUtils.isNegative(-5.0, "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.isNegative(0.0, "error"));
    }

    @Test
    void testNotEmptyString() {
        AssertUtils.notEmpty("test", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty(null, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEmpty("", "error"));
    }

    @Test
    void testNotBlank() {
        AssertUtils.notBlank("test", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notBlank(null, "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notBlank("", "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notBlank("   ", "error"));
    }

    @Test
    void testMatches() {
        AssertUtils.matches("test", "^test$", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.matches(null, "^test$", "error"));
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.matches("other", "^test$", "error"));
        
        AssertUtils.matches("test", "^test$", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.matches("other", "^test$", () -> "error"));
    }

    @Test
    void testEquals() {
        AssertUtils.equals("a", "a", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.equals("a", "b", "error"));
        
        AssertUtils.equals("a", "a", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.equals("a", "b", () -> "error"));
    }

    @Test
    void testNotEquals() {
        AssertUtils.notEquals("a", "b", "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEquals("a", "a", "error"));
        
        AssertUtils.notEquals("a", "b", () -> "message");
        assertThrows(IllegalArgumentException.class, () -> AssertUtils.notEquals("a", "a", () -> "error"));
    }

    @Test
    void testCheckNotNull() {
        assertEquals("test", AssertUtils.checkNotNull("test", "message"));
        assertThrows(NullPointerException.class, () -> AssertUtils.checkNotNull(null, "error"));
        
        assertEquals("test", AssertUtils.checkNotNull("test", () -> "message"));
        assertThrows(NullPointerException.class, () -> AssertUtils.checkNotNull(null, () -> "error"));
    }

    @Test
    void testCheckElementIndex() {
        assertEquals(2, AssertUtils.checkElementIndex(2, 5, "message"));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkElementIndex(-1, 5, "error"));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkElementIndex(5, 5, "error"));
        
        assertEquals(2, AssertUtils.checkElementIndex(2, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkElementIndex(5, 5));
    }

    @Test
    void testCheckPositionIndex() {
        assertEquals(2, AssertUtils.checkPositionIndex(2, 5, "message"));
        assertEquals(5, AssertUtils.checkPositionIndex(5, 5, "message"));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkPositionIndex(-1, 5, "error"));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkPositionIndex(6, 5, "error"));
        
        assertEquals(2, AssertUtils.checkPositionIndex(2, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkPositionIndex(6, 5));
    }

    @Test
    void testCheckPositionIndexes() {
        AssertUtils.checkPositionIndexes(1, 3, 5);
        AssertUtils.checkPositionIndexes(0, 5, 5);
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkPositionIndexes(-1, 3, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkPositionIndexes(1, 6, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> AssertUtils.checkPositionIndexes(3, 1, 5));
    }
}
