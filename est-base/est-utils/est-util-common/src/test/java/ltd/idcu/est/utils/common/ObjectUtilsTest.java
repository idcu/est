package ltd.idcu.est.utils.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.util.*;

public class ObjectUtilsTest {

    @Test
    void testIsEmpty() {
        assertTrue(ObjectUtils.isEmpty(null));
        assertTrue(ObjectUtils.isEmpty(""));
        assertFalse(ObjectUtils.isEmpty("test"));
        assertTrue(ObjectUtils.isEmpty(new Object[0]));
        assertFalse(ObjectUtils.isEmpty(new Object[]{"a"}));
        assertTrue(ObjectUtils.isEmpty(new ArrayList<>()));
        assertFalse(ObjectUtils.isEmpty(Arrays.asList("a")));
        assertTrue(ObjectUtils.isEmpty(new HashMap<>()));
        assertFalse(ObjectUtils.isEmpty(Map.of("a", 1)));
        assertTrue(ObjectUtils.isEmpty(Optional.empty()));
        assertFalse(ObjectUtils.isEmpty(Optional.of("test")));
        assertFalse(ObjectUtils.isEmpty(new Object()));
    }

    @Test
    void testIsNotEmpty() {
        assertFalse(ObjectUtils.isNotEmpty(null));
        assertFalse(ObjectUtils.isNotEmpty(""));
        assertTrue(ObjectUtils.isNotEmpty("test"));
    }

    @Test
    void testIsNull() {
        assertTrue(ObjectUtils.isNull(null));
        assertFalse(ObjectUtils.isNull("test"));
    }

    @Test
    void testIsNotNull() {
        assertFalse(ObjectUtils.isNotNull(null));
        assertTrue(ObjectUtils.isNotNull("test"));
    }

    @Test
    void testAllNull() {
        assertTrue(ObjectUtils.allNull());
        assertTrue(ObjectUtils.allNull((Object[]) null));
        assertTrue(ObjectUtils.allNull(null, null));
        assertFalse(ObjectUtils.allNull(null, "test"));
        assertFalse(ObjectUtils.allNull("test", null));
        assertFalse(ObjectUtils.allNull("a", "b"));
    }

    @Test
    void testAllNotNull() {
        assertFalse(ObjectUtils.allNotNull((Object[]) null));
        assertTrue(ObjectUtils.allNotNull("a", "b"));
        assertFalse(ObjectUtils.allNotNull(null, "b"));
        assertFalse(ObjectUtils.allNotNull("a", null));
    }

    @Test
    void testAnyNull() {
        assertTrue(ObjectUtils.anyNull((Object[]) null));
        assertTrue(ObjectUtils.anyNull(null, "test"));
        assertTrue(ObjectUtils.anyNull("test", null));
        assertFalse(ObjectUtils.anyNull("a", "b"));
    }

    @Test
    void testEquals() {
        assertTrue(ObjectUtils.equals(null, null));
        assertFalse(ObjectUtils.equals(null, "test"));
        assertFalse(ObjectUtils.equals("test", null));
        assertTrue(ObjectUtils.equals("test", "test"));
        assertFalse(ObjectUtils.equals("test", "other"));
        assertTrue(ObjectUtils.equals(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(ObjectUtils.equals(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertTrue(ObjectUtils.equals(new Object[]{"a", "b"}, new Object[]{"a", "b"}));
    }

    @Test
    void testNotEqual() {
        assertFalse(ObjectUtils.notEqual(null, null));
        assertTrue(ObjectUtils.notEqual(null, "test"));
        assertTrue(ObjectUtils.notEqual("test", null));
        assertFalse(ObjectUtils.notEqual("test", "test"));
        assertTrue(ObjectUtils.notEqual("test", "other"));
    }

    @Test
    void testHashCode() {
        assertEquals(0, ObjectUtils.hashCode(null));
        assertEquals("test".hashCode(), ObjectUtils.hashCode("test"));
        assertEquals(Arrays.hashCode(new int[]{1, 2, 3}), ObjectUtils.hashCode(new int[]{1, 2, 3}));
    }

    @Test
    void testHash() {
        assertEquals(Arrays.deepHashCode(new Object[]{"a", 1, true}), ObjectUtils.hash("a", 1, true));
    }

    @Test
    void testIdentityToString() {
        assertEquals("null", ObjectUtils.identityToString(null));
        Object obj = new Object();
        String expected = obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
        assertEquals(expected, ObjectUtils.identityToString(obj));
    }

    @Test
    void testDefaultIfNull() {
        assertEquals("default", ObjectUtils.defaultIfNull(null, "default"));
        assertEquals("test", ObjectUtils.defaultIfNull("test", "default"));
        assertEquals("default", ObjectUtils.defaultIfNull(null, () -> "default"));
        assertEquals("test", ObjectUtils.defaultIfNull("test", () -> "default"));
    }

    @Test
    void testGetIfNull() {
        assertEquals("default", ObjectUtils.getIfNull(null, () -> "default"));
        assertEquals("test", ObjectUtils.getIfNull("test", () -> "default"));
    }

    @Test
    void testFirstNonNull() {
        assertNull(ObjectUtils.firstNonNull());
        assertNull(ObjectUtils.firstNonNull((Object[]) null));
        assertNull(ObjectUtils.firstNonNull(null, null));
        assertEquals("a", ObjectUtils.firstNonNull(null, "a", "b"));
        assertEquals("a", ObjectUtils.firstNonNull("a", null, "b"));
    }

    @Test
    void testCloneSerializable() {
        assertNull(ObjectUtils.cloneSerializable(null));
        ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        ArrayList<String> cloned = ObjectUtils.cloneSerializable(list);
        assertEquals(list, cloned);
        assertNotSame(list, cloned);
    }

    @Test
    void testSerializeAndDeserialize() {
        assertNull(ObjectUtils.serialize(null));
        assertNull(ObjectUtils.deserialize(null));
        String original = "test string";
        byte[] data = ObjectUtils.serialize(original);
        assertNotNull(data);
        String deserialized = ObjectUtils.deserialize(data);
        assertEquals(original, deserialized);
    }

    @Test
    void testToString() {
        assertEquals("null", ObjectUtils.toString(null));
        assertEquals("test", ObjectUtils.toString("test"));
        assertEquals("[1, 2, 3]", ObjectUtils.toString(new int[]{1, 2, 3}));
        assertEquals("[a, b]", ObjectUtils.toString(new Object[]{"a", "b"}));
        assertEquals("custom", ObjectUtils.toString(new Object() {
            @Override
            public String toString() {
                return "custom";
            }
        }));
    }

    @Test
    void testToStringWithNullStr() {
        assertEquals("n/a", ObjectUtils.toString(null, "n/a"));
        assertEquals("test", ObjectUtils.toString("test", "n/a"));
    }

    @Test
    void testNullToEmpty() {
        Object[] nullArray = null;
        Object[] emptyArray = ObjectUtils.nullToEmpty(nullArray);
        assertNotNull(emptyArray);
        assertEquals(0, emptyArray.length);
        Object[] original = new Object[]{"a", "b"};
        assertSame(original, ObjectUtils.nullToEmpty(original));
        
        assertEquals("", ObjectUtils.nullToEmpty((String) null));
        assertEquals("test", ObjectUtils.nullToEmpty("test"));
    }

    @Test
    void testEmptyToNull() {
        assertNull(ObjectUtils.emptyToNull(new Object[0]));
        Object[] original = new Object[]{"a", "b"};
        assertSame(original, ObjectUtils.emptyToNull(original));
        
        assertNull(ObjectUtils.emptyToNull(""));
        assertEquals("test", ObjectUtils.emptyToNull("test"));
    }

    @Test
    void testCompare() {
        assertEquals(0, ObjectUtils.compare(null, null));
        assertEquals(-1, ObjectUtils.compare(null, "a"));
        assertEquals(1, ObjectUtils.compare("a", null));
        assertEquals(0, ObjectUtils.compare("a", "a"));
        assertTrue(ObjectUtils.compare("a", "b") < 0);
        assertTrue(ObjectUtils.compare("b", "a") > 0);
        
        assertEquals(0, ObjectUtils.compare(null, null, true));
        assertEquals(1, ObjectUtils.compare(null, "a", true));
        assertEquals(-1, ObjectUtils.compare("a", null, true));
    }

    @Test
    void testMin() {
        assertEquals(1, ObjectUtils.min(3, 1, 2));
        assertEquals("a", ObjectUtils.min("c", "a", "b"));
    }

    @Test
    void testMax() {
        assertEquals(3, ObjectUtils.max(1, 3, 2));
        assertEquals("c", ObjectUtils.max("a", "c", "b"));
    }

    @Test
    void testLength() {
        assertEquals(0, ObjectUtils.length(null));
        assertEquals(4, ObjectUtils.length("test"));
        assertEquals(3, ObjectUtils.length(new Object[]{"a", "b", "c"}));
        assertEquals(2, ObjectUtils.length(Arrays.asList("a", "b")));
        assertEquals(1, ObjectUtils.length(Map.of("a", 1)));
        assertEquals(1, ObjectUtils.length(Optional.of("test")));
        assertEquals(0, ObjectUtils.length(Optional.empty()));
        assertEquals(0, ObjectUtils.length(new Object()));
    }

    @Test
    void testIsArray() {
        assertFalse(ObjectUtils.isArray(null));
        assertFalse(ObjectUtils.isArray("test"));
        assertTrue(ObjectUtils.isArray(new Object[0]));
        assertTrue(ObjectUtils.isArray(new int[0]));
    }

    @Test
    void testIsEmptyArray() {
        assertTrue(ObjectUtils.isEmptyArray(null));
        assertTrue(ObjectUtils.isEmptyArray(new Object[0]));
        assertFalse(ObjectUtils.isEmptyArray(new Object[]{"a"}));
    }

    @Test
    void testGetComponentType() {
        assertNull(ObjectUtils.getComponentType(null));
        assertEquals(Object.class, ObjectUtils.getComponentType(new Object[0]));
        assertEquals(int.class, ObjectUtils.getComponentType(new int[0]));
    }

    @Test
    void testContains() {
        assertFalse(ObjectUtils.contains(null, "a"));
        assertFalse(ObjectUtils.contains("not array", "a"));
        assertFalse(ObjectUtils.contains(new Object[0], "a"));
        assertTrue(ObjectUtils.contains(new Object[]{"a", "b", "c"}, "b"));
        assertFalse(ObjectUtils.contains(new Object[]{"a", "b", "c"}, "d"));
    }

    @Test
    void testIndexOf() {
        assertEquals(-1, ObjectUtils.indexOf(null, "a"));
        assertEquals(-1, ObjectUtils.indexOf("not array", "a"));
        assertEquals(-1, ObjectUtils.indexOf(new Object[0], "a"));
        assertEquals(1, ObjectUtils.indexOf(new Object[]{"a", "b", "c"}, "b"));
        assertEquals(-1, ObjectUtils.indexOf(new Object[]{"a", "b", "c"}, "d"));
    }

    @Test
    void testRequireNonNull() {
        assertThrows(NullPointerException.class, () -> ObjectUtils.requireNonNull(null));
        assertEquals("test", ObjectUtils.requireNonNull("test"));
        
        assertThrows(NullPointerException.class, () -> ObjectUtils.requireNonNull(null, "message"));
        assertEquals("test", ObjectUtils.requireNonNull("test", "message"));
        
        assertThrows(NullPointerException.class, () -> ObjectUtils.requireNonNull(null, () -> "message"));
        assertEquals("test", ObjectUtils.requireNonNull("test", () -> "message"));
    }

    @Test
    void testRequireNonNullElse() {
        assertEquals("default", ObjectUtils.requireNonNullElse(null, "default"));
        assertEquals("test", ObjectUtils.requireNonNullElse("test", "default"));
    }

    @Test
    void testRequireNonNullElseGet() {
        assertEquals("default", ObjectUtils.requireNonNullElseGet(null, () -> "default"));
        assertEquals("test", ObjectUtils.requireNonNullElseGet("test", () -> "default"));
    }

    @Test
    void testIsSameType() {
        assertTrue(ObjectUtils.isSameType(null, null));
        assertFalse(ObjectUtils.isSameType(null, "test"));
        assertFalse(ObjectUtils.isSameType("test", null));
        assertTrue(ObjectUtils.isSameType("a", "b"));
        assertFalse(ObjectUtils.isSameType("a", 1));
    }

    @Test
    void testIsInstanceOf() {
        assertFalse(ObjectUtils.isInstanceOf(null, null));
        assertFalse(ObjectUtils.isInstanceOf(String.class, null));
        assertFalse(ObjectUtils.isInstanceOf(null, "test"));
        assertTrue(ObjectUtils.isInstanceOf(String.class, "test"));
        assertFalse(ObjectUtils.isInstanceOf(Integer.class, "test"));
    }

    @Test
    void testToOptional() {
        assertEquals(Optional.empty(), ObjectUtils.toOptional(null));
        assertEquals(Optional.of("test"), ObjectUtils.toOptional("test"));
    }

    @Test
    void testToOptionalIfEmpty() {
        assertEquals(Optional.empty(), ObjectUtils.toOptionalIfEmpty(null));
        assertEquals(Optional.empty(), ObjectUtils.toOptionalIfEmpty(""));
        assertEquals(Optional.of("test"), ObjectUtils.toOptionalIfEmpty("test"));
    }
}
