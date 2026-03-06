package ltd.idcu.est.utils.common;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.List;
import java.util.Set;

public class ArrayUtilsTest {

    @Test
    public void testIsEmpty() {
        Assertions.assertTrue(ArrayUtils.isEmpty((Object[]) null));
        Assertions.assertTrue(ArrayUtils.isEmpty(new Object[0]));
        Assertions.assertFalse(ArrayUtils.isEmpty(new Object[]{"a"}));
        Assertions.assertTrue(ArrayUtils.isEmpty((int[]) null));
        Assertions.assertTrue(ArrayUtils.isEmpty(new int[0]));
        Assertions.assertFalse(ArrayUtils.isEmpty(new int[]{1}));
    }

    @Test
    public void testIsNotEmpty() {
        Assertions.assertFalse(ArrayUtils.isNotEmpty((Object[]) null));
        Assertions.assertFalse(ArrayUtils.isNotEmpty(new Object[0]));
        Assertions.assertTrue(ArrayUtils.isNotEmpty(new Object[]{"a"}));
    }

    @Test
    public void testGetLength() {
        Assertions.assertEquals(0, ArrayUtils.getLength(null));
        Assertions.assertEquals(3, ArrayUtils.getLength(new Object[]{"a", "b", "c"}));
        Assertions.assertEquals(3, ArrayUtils.getLength(new int[]{1, 2, 3}));
    }

    @Test
    public void testIsSameLength() {
        Object[] arr1 = new Object[]{"a", "b"};
        Object[] arr2 = new Object[]{"x", "y"};
        Object[] arr3 = new Object[]{"a"};
        Assertions.assertTrue(ArrayUtils.isSameLength(arr1, arr2));
        Assertions.assertFalse(ArrayUtils.isSameLength(arr3, arr2));
        Assertions.assertTrue(ArrayUtils.isSameLength((Object[]) null, (Object[]) null));
        Assertions.assertFalse(ArrayUtils.isSameLength(new Object[0], (Object[]) null));
    }

    @Test
    public void testIsEquals() {
        Object[] arr1 = new Object[]{"a", "b"};
        Object[] arr2 = new Object[]{"a", "b"};
        Object[] arr3 = new Object[]{"a"};
        Object[] arr4 = new Object[]{"b"};
        Assertions.assertTrue(ArrayUtils.isEquals(arr1, arr2));
        Assertions.assertFalse(ArrayUtils.isEquals(arr3, arr4));
        Assertions.assertTrue(ArrayUtils.isEquals((Object[]) null, (Object[]) null));
        Assertions.assertFalse(ArrayUtils.isEquals(new Object[0], (Object[]) null));
    }

    @Test
    public void testClone() {
        String[] array = {"a", "b", "c"};
        String[] cloned = ArrayUtils.clone(array);
        Assertions.assertNotSame(array, cloned);
        Assertions.assertArrayEquals(array, cloned);
        Assertions.assertNull(ArrayUtils.clone((Object[]) null));
    }

    @Test
    public void testNullToEmpty() {
        Object[] empty = new Object[0];
        Object[] result = ArrayUtils.nullToEmpty((Object[]) null);
        Assertions.assertArrayEquals(empty, result);
        String[] array = {"a"};
        Assertions.assertSame(array, ArrayUtils.nullToEmpty(array));
    }

    @Test
    public void testEmptyToNull() {
        Assertions.assertNull(ArrayUtils.emptyToNull((Object[]) new Object[0]));
        Assertions.assertNull(ArrayUtils.emptyToNull((Object[]) null));
        String[] array = {"a"};
        Assertions.assertSame(array, ArrayUtils.emptyToNull(array));
    }

    @Test
    public void testIndexOf() {
        String[] array = {"a", "b", "c", "a"};
        Assertions.assertEquals(0, ArrayUtils.indexOf(array, "a"));
        Assertions.assertEquals(1, ArrayUtils.indexOf(array, "b"));
        Assertions.assertEquals(-1, ArrayUtils.indexOf(array, "x"));
        Assertions.assertEquals(-1, ArrayUtils.indexOf(null, "a"));
        Assertions.assertEquals(3, ArrayUtils.indexOf(array, "a", 1));
    }

    @Test
    public void testLastIndexOf() {
        String[] array = {"a", "b", "c", "a"};
        Assertions.assertEquals(3, ArrayUtils.lastIndexOf(array, "a"));
        Assertions.assertEquals(-1, ArrayUtils.lastIndexOf(array, "x"));
    }

    @Test
    public void testContains() {
        String[] array = {"a", "b", "c"};
        Assertions.assertTrue(ArrayUtils.contains(array, "a"));
        Assertions.assertFalse(ArrayUtils.contains(array, "x"));
        Assertions.assertFalse(ArrayUtils.contains(null, "a"));
    }

    @Test
    public void testAdd() {
        String[] array = {"a", "b"};
        String[] result = ArrayUtils.add(array, "c");
        Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, result);
        result = ArrayUtils.add(null, "a");
        Assertions.assertArrayEquals(new String[]{"a"}, result);
    }

    @Test
    public void testAddAtIndex() {
        String[] array = {"a", "c"};
        String[] result = ArrayUtils.add(array, 1, "b");
        Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    @Test
    public void testAddAll() {
        String[] array1 = {"a", "b"};
        String[] array2 = {"c", "d"};
        String[] result = ArrayUtils.addAll(array1, array2);
        Assertions.assertArrayEquals(new String[]{"a", "b", "c", "d"}, result);
    }

    @Test
    public void testRemove() {
        String[] array = {"a", "b", "c"};
        String[] result = ArrayUtils.remove(array, 1);
        Assertions.assertArrayEquals(new String[]{"a", "c"}, result);
    }

    @Test
    public void testRemoveElement() {
        String[] array = {"a", "b", "c", "b"};
        String[] result = ArrayUtils.removeElement(array, "b");
        Assertions.assertArrayEquals(new String[]{"a", "c", "b"}, result);
    }

    @Test
    public void testRemoveAll() {
        String[] array = {"a", "b", "c", "b"};
        String[] result = ArrayUtils.removeAll(array, "b");
        Assertions.assertArrayEquals(new String[]{"a", "c"}, result);
    }

    @Test
    public void testSubarray() {
        String[] array = {"a", "b", "c", "d", "e"};
        String[] result = ArrayUtils.subarray(array, 1, 4);
        Assertions.assertArrayEquals(new String[]{"b", "c", "d"}, result);
    }

    @Test
    public void testReverse() {
        String[] array = {"a", "b", "c"};
        String[] result = ArrayUtils.reverse(array);
        Assertions.assertArrayEquals(new String[]{"c", "b", "a"}, result);
        Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, array);
    }

    @Test
    public void testReverseInPlace() {
        String[] array = {"a", "b", "c"};
        ArrayUtils.reverseInPlace(array);
        Assertions.assertArrayEquals(new String[]{"c", "b", "a"}, array);
    }

    @Test
    public void testShuffle() {
        String[] array = {"a", "b", "c", "d", "e"};
        String[] result = ArrayUtils.shuffle(array);
        Assertions.assertEquals(array.length, result.length);
        for (String s : array) {
            Assertions.assertTrue(ArrayUtils.contains(result, s));
        }
    }

    @Test
    public void testSort() {
        String[] array = {"c", "a", "b"};
        String[] result = ArrayUtils.sort(array);
        Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    @Test
    public void testUnique() {
        String[] array = {"a", "b", "a", "c", "b"};
        String[] result = ArrayUtils.unique(array);
        Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    @Test
    public void testFirstLast() {
        String[] array = {"a", "b", "c"};
        Assertions.assertEquals("a", ArrayUtils.first(array));
        Assertions.assertEquals("c", ArrayUtils.last(array));
        Assertions.assertNull(ArrayUtils.first(null));
        Assertions.assertNull(ArrayUtils.last(new Object[0]));
    }

    @Test
    public void testGet() {
        String[] array = {"a", "b", "c"};
        Assertions.assertEquals("a", ArrayUtils.get(array, 0));
        Assertions.assertEquals("c", ArrayUtils.get(array, 2));
        Assertions.assertNull(ArrayUtils.get(array, 10));
        Assertions.assertEquals("default", ArrayUtils.get(array, 10, "default"));
    }

    @Test
    public void testToList() {
        String[] array = {"a", "b", "c"};
        List<String> list = ArrayUtils.toList(array);
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals("a", list.get(0));
        Assertions.assertTrue(ArrayUtils.toList(null).isEmpty());
    }

    @Test
    public void testToSet() {
        String[] array = {"a", "b", "a"};
        Set<String> set = ArrayUtils.toSet(array);
        Assertions.assertEquals(2, set.size());
        Assertions.assertTrue(set.contains("a"));
        Assertions.assertTrue(set.contains("b"));
    }

    @Test
    public void testToString() {
        String[] array = {"a", "b", "c"};
        Assertions.assertEquals("[a, b, c]", ArrayUtils.toString(array));
        Assertions.assertEquals("null", ArrayUtils.toString(null));
        Assertions.assertEquals("[]", ArrayUtils.toString(new Object[0]));
    }
}
