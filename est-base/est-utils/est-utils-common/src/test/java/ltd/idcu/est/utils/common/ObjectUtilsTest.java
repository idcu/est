package ltd.idcu.est.utils.common;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ObjectUtilsTest {

    @Test
    public void testIsEmpty() {
        Assertions.assertTrue(ObjectUtils.isEmpty(null));
        Assertions.assertTrue(ObjectUtils.isEmpty(""));
        Assertions.assertFalse(ObjectUtils.isEmpty(" "));
        Assertions.assertTrue(ObjectUtils.isEmpty(new Object[0]));
        Assertions.assertFalse(ObjectUtils.isEmpty(new Object[]{"a"}));
        Assertions.assertTrue(ObjectUtils.isEmpty(new ArrayList<>()));
        Assertions.assertTrue(ObjectUtils.isEmpty(new HashMap<>()));
        Assertions.assertTrue(ObjectUtils.isEmpty(Optional.empty()));
        Assertions.assertFalse(ObjectUtils.isEmpty(Optional.of("test")));
    }

    @Test
    public void testIsNotEmpty() {
        Assertions.assertFalse(ObjectUtils.isNotEmpty(null));
        Assertions.assertFalse(ObjectUtils.isNotEmpty(""));
        Assertions.assertTrue(ObjectUtils.isNotEmpty("test"));
    }

    @Test
    public void testIsNullIsNotNull() {
        Assertions.assertTrue(ObjectUtils.isNull(null));
        Assertions.assertFalse(ObjectUtils.isNull("test"));
        Assertions.assertFalse(ObjectUtils.isNotNull(null));
        Assertions.assertTrue(ObjectUtils.isNotNull("test"));
    }

    @Test
    public void testAllNull() {
        Assertions.assertTrue(ObjectUtils.allNull(null, null, null));
        Assertions.assertFalse(ObjectUtils.allNull(null, "test", null));
        Assertions.assertTrue(ObjectUtils.allNull((Object[]) null));
    }

    @Test
    public void testAllNotNull() {
        Assertions.assertTrue(ObjectUtils.allNotNull("a", "b", "c"));
        Assertions.assertFalse(ObjectUtils.allNotNull("a", null, "c"));
        Assertions.assertFalse(ObjectUtils.allNotNull((Object[]) null));
    }

    @Test
    public void testAnyNull() {
        Assertions.assertTrue(ObjectUtils.anyNull("a", null, "c"));
        Assertions.assertFalse(ObjectUtils.anyNull("a", "b", "c"));
        Assertions.assertTrue(ObjectUtils.anyNull((Object[]) null));
    }

    @Test
    public void testEquals() {
        Assertions.assertTrue(ObjectUtils.equals(null, null));
        Assertions.assertFalse(ObjectUtils.equals(null, "test"));
        Assertions.assertFalse(ObjectUtils.equals("test", null));
        Assertions.assertTrue(ObjectUtils.equals("test", "test"));
        Assertions.assertFalse(ObjectUtils.equals("test", "Test"));
        Assertions.assertTrue(ObjectUtils.equals(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        Assertions.assertFalse(ObjectUtils.equals(new int[]{1, 2}, new int[]{1, 2, 3}));
    }

    @Test
    public void testNotEqual() {
        Assertions.assertTrue(ObjectUtils.notEqual("a", "b"));
        Assertions.assertFalse(ObjectUtils.notEqual("a", "a"));
    }

    @Test
    public void testHashCode() {
        String str = "test";
        Assertions.assertEquals(str.hashCode(), ObjectUtils.hashCode(str));
        Assertions.assertEquals(0, ObjectUtils.hashCode(null));
        int[] array = {1, 2, 3};
        Assertions.assertTrue(ObjectUtils.hashCode(array) != 0);
    }

    @Test
    public void testHash() {
        int hash = ObjectUtils.hash("a", 1, true);
        Assertions.assertTrue(hash != 0);
    }

    @Test
    public void testIdentityToString() {
        Object obj = new Object();
        String result = ObjectUtils.identityToString(obj);
        Assertions.assertTrue(result.startsWith(obj.getClass().getName() + "@"));
        Assertions.assertEquals("null", ObjectUtils.identityToString(null));
    }

    @Test
    public void testDefaultIfNull() {
        Assertions.assertEquals("default", ObjectUtils.defaultIfNull(null, "default"));
        Assertions.assertEquals("test", ObjectUtils.defaultIfNull("test", "default"));
        Assertions.assertEquals("lazy", ObjectUtils.defaultIfNull(null, () -> "lazy"));
    }

    @Test
    public void testFirstNonNull() {
        Assertions.assertEquals("b", ObjectUtils.firstNonNull(null, "b", "c"));
        Assertions.assertEquals("a", ObjectUtils.firstNonNull("a", "b", "c"));
        Assertions.assertNull(ObjectUtils.firstNonNull(null, null, null));
        Assertions.assertNull(ObjectUtils.firstNonNull((Object[]) null));
    }

    @Test
    public void testCloneSerializable() {
        TestSerializable original = new TestSerializable("test", 123);
        TestSerializable cloned = ObjectUtils.cloneSerializable(original);
        Assertions.assertNotSame(original, cloned);
        Assertions.assertEquals(original.getName(), cloned.getName());
        Assertions.assertEquals(original.getValue(), cloned.getValue());
        Assertions.assertNull(ObjectUtils.cloneSerializable(null));
    }

    @Test
    public void testSerializeDeserialize() {
        TestSerializable original = new TestSerializable("test", 123);
        byte[] data = ObjectUtils.serialize(original);
        Assertions.assertNotNull(data);
        Assertions.assertTrue(data.length > 0);
        TestSerializable deserialized = ObjectUtils.deserialize(data);
        Assertions.assertNotSame(original, deserialized);
        Assertions.assertEquals(original.getName(), deserialized.getName());
        Assertions.assertEquals(original.getValue(), deserialized.getValue());
        Assertions.assertNull(ObjectUtils.serialize(null));
        Assertions.assertNull(ObjectUtils.deserialize(null));
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("test", ObjectUtils.toString("test"));
        Assertions.assertEquals("null", ObjectUtils.toString(null));
        Assertions.assertEquals("[1, 2, 3]", ObjectUtils.toString(new int[]{1, 2, 3}));
        Assertions.assertEquals("custom", ObjectUtils.toString(null, "custom"));
    }

    @Test
    public void testNullToEmpty() {
        Assertions.assertArrayEquals(new Object[0], ObjectUtils.nullToEmpty((Object[]) null));
        Assertions.assertEquals("", ObjectUtils.nullToEmpty((String) null));
        String str = "test";
        Assertions.assertSame(str, ObjectUtils.nullToEmpty(str));
    }

    @Test
    public void testEmptyToNull() {
        Assertions.assertNull(ObjectUtils.emptyToNull(new Object[0]));
        Assertions.assertNull(ObjectUtils.emptyToNull(""));
        String str = "test";
        Assertions.assertSame(str, ObjectUtils.emptyToNull(str));
    }

    @Test
    public void testCompare() {
        Assertions.assertEquals(0, ObjectUtils.compare("a", "a"));
        Assertions.assertTrue(ObjectUtils.compare("a", "b") < 0);
        Assertions.assertTrue(ObjectUtils.compare("b", "a") > 0);
        Assertions.assertEquals(-1, ObjectUtils.compare(null, "a"));
        Assertions.assertEquals(1, ObjectUtils.compare("a", null));
        Assertions.assertEquals(1, ObjectUtils.compare(null, "a", true));
    }

    @Test
    public void testMin() {
        Assertions.assertEquals("a", ObjectUtils.min("c", "a", "b"));
        Assertions.assertEquals(1, ObjectUtils.min(3, 1, 2));
    }

    @Test
    public void testMax() {
        Assertions.assertEquals("c", ObjectUtils.max("c", "a", "b"));
        Assertions.assertEquals(3, ObjectUtils.max(3, 1, 2));
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(0, ObjectUtils.length(null));
        Assertions.assertEquals(4, ObjectUtils.length("test"));
        Assertions.assertEquals(3, ObjectUtils.length(new Object[]{"a", "b", "c"}));
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        Assertions.assertEquals(2, ObjectUtils.length(list));
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        Assertions.assertEquals(1, ObjectUtils.length(map));
    }

    @Test
    public void testIsArray() {
        Assertions.assertTrue(ObjectUtils.isArray(new Object[0]));
        Assertions.assertTrue(ObjectUtils.isArray(new int[0]));
        Assertions.assertFalse(ObjectUtils.isArray(null));
        Assertions.assertFalse(ObjectUtils.isArray("test"));
    }

    @Test
    public void testIsEmptyArray() {
        Assertions.assertTrue(ObjectUtils.isEmptyArray(null));
        Assertions.assertTrue(ObjectUtils.isEmptyArray(new Object[0]));
        Assertions.assertFalse(ObjectUtils.isEmptyArray(new Object[]{"a"}));
    }

    @Test
    public void testGetComponentType() {
        Assertions.assertEquals(String.class, ObjectUtils.getComponentType(new String[0]));
        Assertions.assertNull(ObjectUtils.getComponentType(null));
    }

    @Test
    public void testContains() {
        Assertions.assertTrue(ObjectUtils.contains(new Object[]{"a", "b", "c"}, "a"));
        Assertions.assertFalse(ObjectUtils.contains(new Object[]{"a", "b", "c"}, "x"));
        Assertions.assertFalse(ObjectUtils.contains(null, "a"));
        Assertions.assertFalse(ObjectUtils.contains("not an array", "a"));
    }

    @Test
    public void testIndexOf() {
        Assertions.assertEquals(1, ObjectUtils.indexOf(new Object[]{"a", "b", "c"}, "b"));
        Assertions.assertEquals(-1, ObjectUtils.indexOf(new Object[]{"a", "b", "c"}, "x"));
        Assertions.assertEquals(-1, ObjectUtils.indexOf(null, "a"));
    }

    @Test
    public void testIsSameType() {
        Assertions.assertTrue(ObjectUtils.isSameType("a", "b"));
        Assertions.assertFalse(ObjectUtils.isSameType("a", 1));
        Assertions.assertTrue(ObjectUtils.isSameType(null, null));
        Assertions.assertFalse(ObjectUtils.isSameType(null, "a"));
    }

    @Test
    public void testIsInstanceOf() {
        Assertions.assertTrue(ObjectUtils.isInstanceOf(String.class, "test"));
        Assertions.assertFalse(ObjectUtils.isInstanceOf(Integer.class, "test"));
        Assertions.assertFalse(ObjectUtils.isInstanceOf(String.class, null));
        Assertions.assertFalse(ObjectUtils.isInstanceOf(null, "test"));
    }

    @Test
    public void testToOptional() {
        Optional<String> opt = ObjectUtils.toOptional("test");
        Assertions.assertTrue(opt.isPresent());
        Assertions.assertEquals("test", opt.get());
        Assertions.assertFalse(ObjectUtils.toOptional(null).isPresent());
    }

    @Test
    public void testToOptionalIfEmpty() {
        Optional<String> opt = ObjectUtils.toOptionalIfEmpty("test");
        Assertions.assertTrue(opt.isPresent());
        Assertions.assertEquals("test", opt.get());
        Assertions.assertFalse(ObjectUtils.toOptionalIfEmpty(null).isPresent());
        Assertions.assertFalse(ObjectUtils.toOptionalIfEmpty("").isPresent());
    }

    private static class TestSerializable implements Serializable {
        private final String name;
        private final int value;

        public TestSerializable(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}
