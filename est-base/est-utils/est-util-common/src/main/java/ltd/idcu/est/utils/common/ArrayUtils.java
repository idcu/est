package ltd.idcu.est.utils.common;

import java.lang.reflect.Array;
import java.util.*;

public final class ArrayUtils {

    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private static final int INDEX_NOT_FOUND = -1;

    private ArrayUtils() {
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(boolean[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(double[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(float[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(long[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(short[] array) {
        return !isEmpty(array);
    }

    public static int getLength(Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    public static boolean isSameLength(Object[] array1, Object[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(boolean[] array1, boolean[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(byte[] array1, byte[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(char[] array1, char[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(double[] array1, double[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(float[] array1, float[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(int[] array1, int[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(long[] array1, long[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isSameLength(short[] array1, short[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null) {
            return false;
        }
        return array1.length == array2.length;
    }

    public static boolean isEquals(Object[] array1, Object[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(boolean[] array1, boolean[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(byte[] array1, byte[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(char[] array1, char[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(double[] array1, double[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(float[] array1, float[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(int[] array1, int[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(long[] array1, long[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static boolean isEquals(short[] array1, short[] array2) {
        return Arrays.equals(array1, array2);
    }

    public static <T> T[] clone(T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static boolean[] clone(boolean[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static byte[] clone(byte[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static char[] clone(char[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static double[] clone(double[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static float[] clone(float[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static int[] clone(int[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static long[] clone(long[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static short[] clone(short[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] nullToEmpty(T[] array) {
        return array == null ? (T[]) EMPTY_OBJECT_ARRAY : array;
    }

    public static boolean[] nullToEmpty(boolean[] array) {
        return array == null ? EMPTY_BOOLEAN_ARRAY : array;
    }

    public static byte[] nullToEmpty(byte[] array) {
        return array == null ? EMPTY_BYTE_ARRAY : array;
    }

    public static char[] nullToEmpty(char[] array) {
        return array == null ? EMPTY_CHAR_ARRAY : array;
    }

    public static double[] nullToEmpty(double[] array) {
        return array == null ? EMPTY_DOUBLE_ARRAY : array;
    }

    public static float[] nullToEmpty(float[] array) {
        return array == null ? EMPTY_FLOAT_ARRAY : array;
    }

    public static int[] nullToEmpty(int[] array) {
        return array == null ? EMPTY_INT_ARRAY : array;
    }

    public static long[] nullToEmpty(long[] array) {
        return array == null ? EMPTY_LONG_ARRAY : array;
    }

    public static short[] nullToEmpty(short[] array) {
        return array == null ? EMPTY_SHORT_ARRAY : array;
    }

    public static <T> T[] emptyToNull(T[] array) {
        return isEmpty(array) ? null : array;
    }

    public static boolean[] emptyToNull(boolean[] array) {
        return isEmpty(array) ? null : array;
    }

    public static byte[] emptyToNull(byte[] array) {
        return isEmpty(array) ? null : array;
    }

    public static char[] emptyToNull(char[] array) {
        return isEmpty(array) ? null : array;
    }

    public static double[] emptyToNull(double[] array) {
        return isEmpty(array) ? null : array;
    }

    public static float[] emptyToNull(float[] array) {
        return isEmpty(array) ? null : array;
    }

    public static int[] emptyToNull(int[] array) {
        return isEmpty(array) ? null : array;
    }

    public static long[] emptyToNull(long[] array) {
        return isEmpty(array) ? null : array;
    }

    public static short[] emptyToNull(short[] array) {
        return isEmpty(array) ? null : array;
    }

    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        if (objectToFind == null) {
            for (int i = start; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < array.length; i++) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(boolean[] array, boolean valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (array[i] == valueToFind) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(byte[] array, byte valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (array[i] == valueToFind) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(char[] array, char valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(char[] array, char valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (array[i] == valueToFind) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(double[] array, double valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(double[] array, double valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (Double.compare(array[i], valueToFind) == 0) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(float[] array, float valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(float[] array, float valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (Float.compare(array[i], valueToFind) == 0) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(int[] array, int valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(int[] array, int valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (array[i] == valueToFind) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(long[] array, long valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(long[] array, long valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (array[i] == valueToFind) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(short[] array, short valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    public static int indexOf(short[] array, short valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.max(startIndex, 0);
        for (int i = start; i < array.length; i++) {
            if (array[i] == valueToFind) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int lastIndexOf(Object[] array, Object objectToFind) {
        return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        int start = Math.min(startIndex, array.length - 1);
        if (objectToFind == null) {
            for (int i = start; i >= 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i >= 0; i--) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static boolean contains(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(boolean[] array, boolean valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(byte[] array, byte valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(char[] array, char valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(double[] array, double valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(float[] array, float valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(int[] array, int valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(long[] array, long valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    public static boolean contains(short[] array, short valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] cast(Object[] array, Class<?> newComponentType) {
        if (array.getClass().getComponentType() == newComponentType) {
            return (T[]) array;
        }
        T[] newArray = (T[]) Array.newInstance(newComponentType, array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] add(T[] array, T element) {
        if (array == null) {
            T[] newArray = (T[]) Array.newInstance(element != null ? element.getClass() : Object.class, 1);
            newArray[0] = element;
            return newArray;
        }
        Class<T> type = getComponentType(array);
        T[] newArray = create(type, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return newArray;
    }

    public static <T> T[] add(T[] array, int index, T element) {
        Class<T> type = getComponentType(array);
        int length = array.length;
        AssertUtils.isTrue(index >= 0 && index <= length, "Index: " + index + ", Length: " + length);
        T[] newArray = create(type, length + 1);
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, length - index);
        return newArray;
    }

    public static <T> T[] addAll(T[] array1, T[] array2) {
        if (array1 == null) {
            return clone(array2);
        }
        if (array2 == null) {
            return clone(array1);
        }
        Class<T> type = getComponentType(array1);
        T[] joinedArray = create(type, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static <T> T[] remove(T[] array, int index) {
        int length = array.length;
        AssertUtils.isTrue(index >= 0 && index < length, "Index: " + index + ", Length: " + length);
        Class<T> type = getComponentType(array);
        T[] newArray = create(type, length - 1);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, length - index - 1);
        return newArray;
    }

    public static <T> T[] removeElement(T[] array, Object element) {
        int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    public static <T> T[] removeAll(T[] array, Object... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        List<T> list = new ArrayList<>(Arrays.asList(array));
        for (Object value : values) {
            while (list.remove(value)) {
            }
        }
        return list.toArray(create(getComponentType(array), 0));
    }

    public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        int start = Math.max(startIndexInclusive, 0);
        int end = Math.min(Math.max(endIndexExclusive, 0), array.length);
        int newSize = end - start;
        Class<T> type = getComponentType(array);
        T[] subarray = create(type, newSize);
        System.arraycopy(array, start, subarray, 0, newSize);
        return subarray;
    }

    public static <T> T[] reverse(T[] array) {
        if (array == null) {
            return null;
        }
        T[] reversed = clone(array);
        int i = 0;
        int j = reversed.length - 1;
        T tmp;
        while (j > i) {
            tmp = reversed[j];
            reversed[j] = reversed[i];
            reversed[i] = tmp;
            j--;
            i++;
        }
        return reversed;
    }

    public static void reverseInPlace(Object[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static <T> T[] shuffle(T[] array) {
        return shuffle(array, new Random());
    }

    public static <T> T[] shuffle(T[] array, Random random) {
        if (array == null) {
            return null;
        }
        T[] shuffled = clone(array);
        shuffleInPlace(shuffled, random);
        return shuffled;
    }

    public static void shuffleInPlace(Object[] array) {
        shuffleInPlace(array, new Random());
    }

    public static void shuffleInPlace(Object[] array, Random random) {
        if (array == null) {
            return;
        }
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Object temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static <T> T[] sort(T[] array) {
        if (array == null) {
            return null;
        }
        T[] sorted = clone(array);
        Arrays.sort(sorted);
        return sorted;
    }

    public static <T> T[] sort(T[] array, Comparator<? super T> comparator) {
        if (array == null) {
            return null;
        }
        T[] sorted = clone(array);
        Arrays.sort(sorted, comparator);
        return sorted;
    }

    public static void sortInPlace(Object[] array) {
        if (array == null) {
            return;
        }
        Arrays.sort(array);
    }

    public static <T> void sortInPlace(T[] array, Comparator<? super T> comparator) {
        if (array == null) {
            return;
        }
        Arrays.sort(array, comparator);
    }

    public static <T> T[] unique(T[] array) {
        if (isEmpty(array)) {
            return clone(array);
        }
        Set<T> set = new LinkedHashSet<>(Arrays.asList(array));
        return set.toArray(create(getComponentType(array), 0));
    }

    public static <T> T first(T[] array) {
        return isEmpty(array) ? null : array[0];
    }

    public static <T> T last(T[] array) {
        return isEmpty(array) ? null : array[array.length - 1];
    }

    public static <T> T get(T[] array, int index) {
        return get(array, index, null);
    }

    public static <T> T get(T[] array, int index, T defaultValue) {
        if (isEmpty(array) || index < 0 || index >= array.length) {
            return defaultValue;
        }
        return array[index];
    }

    public static <T> List<T> toList(T[] array) {
        if (array == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(array));
    }

    public static <T> Set<T> toSet(T[] array) {
        if (array == null) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(array));
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getComponentType(T[] array) {
        return (Class<T>) array.getClass().getComponentType();
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] create(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    public static String toString(Object[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
