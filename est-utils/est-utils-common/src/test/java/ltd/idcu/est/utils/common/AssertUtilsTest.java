package ltd.idcu.est.utils.common;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssertUtilsTest {

    @Test
    public void testIsTrue() {
        AssertUtils.isTrue(true, "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isTrue(false, "test message");
        });
    }

    @Test
    public void testIsTrueWithSupplier() {
        AssertUtils.isTrue(true, () -> "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isTrue(false, () -> "test message");
        });
    }

    @Test
    public void testIsFalse() {
        AssertUtils.isFalse(false, "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isFalse(true, "test message");
        });
    }

    @Test
    public void testIsNull() {
        AssertUtils.isNull(null, "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNull("not null", "test message");
        });
    }

    @Test
    public void testNotNull() {
        AssertUtils.notNull("test", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notNull(null, "test message");
        });
    }

    @Test
    public void testRequireNonNull() {
        String result = AssertUtils.requireNonNull("test", "message");
        Assertions.assertEquals("test", result);
        Assertions.assertThrows(NullPointerException.class, () -> {
            AssertUtils.requireNonNull(null, "test message");
        });
    }

    @Test
    public void testRequireNonNullElse() {
        String result1 = AssertUtils.requireNonNullElse("test", "default");
        Assertions.assertEquals("test", result1);
        
        String result2 = AssertUtils.requireNonNullElse(null, "default");
        Assertions.assertEquals("default", result2);
    }

    @Test
    public void testRequireNonNullElseGet() {
        String result1 = AssertUtils.requireNonNullElseGet("test", () -> "default");
        Assertions.assertEquals("test", result1);
        
        String result2 = AssertUtils.requireNonNullElseGet(null, () -> "default");
        Assertions.assertEquals("default", result2);
    }

    @Test
    public void testHasLength() {
        AssertUtils.hasLength("test", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.hasLength("", "test message");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.hasLength(null, "test message");
        });
    }

    @Test
    public void testHasText() {
        AssertUtils.hasText("test", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.hasText("   ", "test message");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.hasText(null, "test message");
        });
    }

    @Test
    public void testDoesNotContain() {
        AssertUtils.doesNotContain("test", "xyz", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.doesNotContain("test", "es", "test message");
        });
    }

    @Test
    public void testNotEmptyArray() {
        AssertUtils.notEmpty(new Object[]{"test"}, "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty(new Object[0], "test message");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty((Object[]) null, "test message");
        });
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = new ArrayList<>();
        list.add("test");
        AssertUtils.notEmpty(list, "message");
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty(new ArrayList<>(), "test message");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty((List<?>) null, "test message");
        });
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        AssertUtils.notEmpty(map, "message");
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty(new HashMap<>(), "test message");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty((Map<?, ?>) null, "test message");
        });
    }

    @Test
    public void testNoNullElements() {
        AssertUtils.noNullElements(new Object[]{"test1", "test2"}, "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.noNullElements(new Object[]{"test", null}, "test message");
        });
    }

    @Test
    public void testNoNullElementsCollection() {
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        AssertUtils.noNullElements(list, "message");
        
        List<String> listWithNull = new ArrayList<>();
        listWithNull.add("test");
        listWithNull.add(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.noNullElements(listWithNull, "test message");
        });
    }

    @Test
    public void testIsInstanceOf() {
        AssertUtils.isInstanceOf(String.class, "test", "message");
        AssertUtils.isInstanceOf(String.class, "test");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isInstanceOf(Integer.class, "test", "test message");
        });
    }

    @Test
    public void testIsAssignable() {
        AssertUtils.isAssignable(CharSequence.class, String.class, "message");
        AssertUtils.isAssignable(CharSequence.class, String.class);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isAssignable(Integer.class, String.class, "test message");
        });
    }

    @Test
    public void testState() {
        AssertUtils.state(true, "message");
        Assertions.assertThrows(IllegalStateException.class, () -> {
            AssertUtils.state(false, "test message");
        });
    }

    @Test
    public void testInRange() {
        AssertUtils.inRange(5, 1, 10, "message");
        AssertUtils.inRange(5L, 1L, 10L, "message");
        AssertUtils.inRange(5.0, 1.0, 10.0, "message");
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.inRange(0, 1, 10, "test message");
        });
    }

    @Test
    public void testIsPositive() {
        AssertUtils.isPositive(5, "message");
        AssertUtils.isPositive(5L, "message");
        AssertUtils.isPositive(5.0, "message");
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isPositive(0, "test message");
        });
    }

    @Test
    public void testIsNegative() {
        AssertUtils.isNegative(-5, "message");
        AssertUtils.isNegative(-5L, "message");
        AssertUtils.isNegative(-5.0, "message");
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNegative(0, "test message");
        });
    }

    @Test
    public void testNotEmptyString() {
        AssertUtils.notEmpty("test", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty("", "test message");
        });
    }

    @Test
    public void testNotBlank() {
        AssertUtils.notBlank("test", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notBlank("   ", "test message");
        });
    }

    @Test
    public void testMatches() {
        AssertUtils.matches("test123", "^[a-z]+[0-9]+$", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.matches("test", "^[0-9]+$", "test message");
        });
    }

    @Test
    public void testEquals() {
        AssertUtils.equals("test", "test", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.equals("test", "other", "test message");
        });
    }

    @Test
    public void testNotEquals() {
        AssertUtils.notEquals("test", "other", "message");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEquals("test", "test", "test message");
        });
    }

    @Test
    public void testCheckNotNull() {
        String result = AssertUtils.checkNotNull("test", "message");
        Assertions.assertEquals("test", result);
        Assertions.assertThrows(NullPointerException.class, () -> {
            AssertUtils.checkNotNull(null, "test message");
        });
    }

    @Test
    public void testCheckElementIndex() {
        Assertions.assertEquals(1, AssertUtils.checkElementIndex(1, 5, "message"));
        Assertions.assertEquals(1, AssertUtils.checkElementIndex(1, 5));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            AssertUtils.checkElementIndex(10, 5, "test message");
        });
    }

    @Test
    public void testCheckPositionIndex() {
        Assertions.assertEquals(1, AssertUtils.checkPositionIndex(1, 5, "message"));
        Assertions.assertEquals(1, AssertUtils.checkPositionIndex(1, 5));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            AssertUtils.checkPositionIndex(10, 5, "test message");
        });
    }

    @Test
    public void testCheckPositionIndexes() {
        AssertUtils.checkPositionIndexes(1, 3, 5);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            AssertUtils.checkPositionIndexes(3, 1, 5);
        });
    }
}
