package ltd.idcu.est.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class ObjectUtils {

    public static final Object NULL = new Object();

    private ObjectUtils() {
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean allNull(Object... array) {
        if (array == null) {
            return true;
        }
        for (Object obj : array) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean allNotNull(Object... array) {
        if (array == null) {
            return false;
        }
        for (Object obj : array) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean anyNull(Object... array) {
        if (array == null) {
            return true;
        }
        for (Object obj : array) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }
        return false;
    }

    private static boolean arrayEquals(Object array1, Object array2) {
        if (array1 instanceof Object[] && array2 instanceof Object[]) {
            return Arrays.deepEquals((Object[]) array1, (Object[]) array2);
        }
        if (array1 instanceof boolean[] && array2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) array1, (boolean[]) array2);
        }
        if (array1 instanceof byte[] && array2 instanceof byte[]) {
            return Arrays.equals((byte[]) array1, (byte[]) array2);
        }
        if (array1 instanceof char[] && array2 instanceof char[]) {
            return Arrays.equals((char[]) array1, (char[]) array2);
        }
        if (array1 instanceof double[] && array2 instanceof double[]) {
            return Arrays.equals((double[]) array1, (double[]) array2);
        }
        if (array1 instanceof float[] && array2 instanceof float[]) {
            return Arrays.equals((float[]) array1, (float[]) array2);
        }
        if (array1 instanceof int[] && array2 instanceof int[]) {
            return Arrays.equals((int[]) array1, (int[]) array2);
        }
        if (array1 instanceof long[] && array2 instanceof long[]) {
            return Arrays.equals((long[]) array1, (long[]) array2);
        }
        if (array1 instanceof short[] && array2 instanceof short[]) {
            return Arrays.equals((short[]) array1, (short[]) array2);
        }
        return false;
    }

    public static boolean notEqual(Object o1, Object o2) {
        return !equals(o1, o2);
    }

    public static int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass().isArray()) {
            return arrayHashCode(obj);
        }
        return obj.hashCode();
    }

    private static int arrayHashCode(Object array) {
        if (array instanceof Object[]) {
            return Arrays.deepHashCode((Object[]) array);
        }
        if (array instanceof boolean[]) {
            return Arrays.hashCode((boolean[]) array);
        }
        if (array instanceof byte[]) {
            return Arrays.hashCode((byte[]) array);
        }
        if (array instanceof char[]) {
            return Arrays.hashCode((char[]) array);
        }
        if (array instanceof double[]) {
            return Arrays.hashCode((double[]) array);
        }
        if (array instanceof float[]) {
            return Arrays.hashCode((float[]) array);
        }
        if (array instanceof int[]) {
            return Arrays.hashCode((int[]) array);
        }
        if (array instanceof long[]) {
            return Arrays.hashCode((long[]) array);
        }
        if (array instanceof short[]) {
            return Arrays.hashCode((short[]) array);
        }
        return 0;
    }

    public static int hash(Object... values) {
        return Arrays.deepHashCode(values);
    }

    public static String identityToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
    }

    public static <T> T defaultIfNull(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static <T> T defaultIfNull(T object, Supplier<T> defaultSupplier) {
        AssertUtils.notNull(defaultSupplier, "Default supplier must not be null");
        return object != null ? object : defaultSupplier.get();
    }

    public static <T> T getIfNull(T object, Supplier<T> supplier) {
        return defaultIfNull(object, supplier);
    }

    public static <T> T firstNonNull(T... values) {
        if (values != null) {
            for (T value : values) {
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T clone(T obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Cloneable) {
            try {
                return (T) ClassUtils.invokeMethodQuietly(ClassUtils.findMethod(obj.getClass(), "clone"), obj);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static <T extends Serializable> T cloneSerializable(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
            }
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static byte[] serialize(Serializable obj) {
        if (obj == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static <T extends Serializable> T deserialize(byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof CharSequence) {
            return obj.toString();
        }
        if (obj.getClass().isArray()) {
            return arrayToString(obj);
        }
        return obj.toString();
    }

    private static String arrayToString(Object array) {
        if (array instanceof Object[]) {
            return Arrays.deepToString((Object[]) array);
        }
        if (array instanceof boolean[]) {
            return Arrays.toString((boolean[]) array);
        }
        if (array instanceof byte[]) {
            return Arrays.toString((byte[]) array);
        }
        if (array instanceof char[]) {
            return Arrays.toString((char[]) array);
        }
        if (array instanceof double[]) {
            return Arrays.toString((double[]) array);
        }
        if (array instanceof float[]) {
            return Arrays.toString((float[]) array);
        }
        if (array instanceof int[]) {
            return Arrays.toString((int[]) array);
        }
        if (array instanceof long[]) {
            return Arrays.toString((long[]) array);
        }
        if (array instanceof short[]) {
            return Arrays.toString((short[]) array);
        }
        return "[]";
    }

    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : toString(obj);
    }

    public static <T> T[] nullToEmpty(T[] array) {
        return array == null ? (T[]) new Object[0] : array;
    }

    public static <T extends CharSequence> T nullToEmpty(T cs) {
        return cs == null ? (T) "" : cs;
    }

    public static <T> T[] emptyToNull(T[] array) {
        return isEmpty(array) ? null : array;
    }

    public static <T extends CharSequence> T emptyToNull(T cs) {
        return isEmpty(cs) ? null : cs;
    }

    public static <T> int compare(T c1, T c2) {
        return compare(c1, c2, false);
    }

    public static <T> int compare(T c1, T c2, boolean nullGreater) {
        if (c1 == c2) {
            return 0;
        }
        if (c1 == null) {
            return nullGreater ? 1 : -1;
        }
        if (c2 == null) {
            return nullGreater ? -1 : 1;
        }
        if (c1 instanceof Comparable) {
            return ((Comparable<T>) c1).compareTo(c2);
        }
        return 0;
    }

    public static <T extends Comparable<? super T>> T min(T... values) {
        AssertUtils.notEmpty(values, "Values must not be empty");
        T min = values[0];
        for (T value : values) {
            if (compare(value, min) < 0) {
                min = value;
            }
        }
        return min;
    }

    public static <T extends Comparable<? super T>> T max(T... values) {
        AssertUtils.notEmpty(values, "Values must not be empty");
        T max = values[0];
        for (T value : values) {
            if (compare(value, max) > 0) {
                max = value;
            }
        }
        return max;
    }

    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }
        if (obj instanceof Optional) {
            return ((Optional<?>) obj).isPresent() ? 1 : 0;
        }
        return 0;
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static boolean isEmptyArray(Object array) {
        return array == null || Array.getLength(array) == 0;
    }

    public static Class<?> getComponentType(Object array) {
        if (array == null) {
            return null;
        }
        return array.getClass().getComponentType();
    }

    public static boolean contains(Object array, Object valueToFind) {
        if (array == null) {
            return false;
        }
        if (!array.getClass().isArray()) {
            return false;
        }
        int length = Array.getLength(array);
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            Object object = Array.get(array, i);
            if (Objects.equals(object, valueToFind)) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(Object array, Object valueToFind) {
        if (array == null) {
            return -1;
        }
        if (!array.getClass().isArray()) {
            return -1;
        }
        int length = Array.getLength(array);
        if (length == 0) {
            return -1;
        }
        for (int i = 0; i < length; i++) {
            Object object = Array.get(array, i);
            if (Objects.equals(object, valueToFind)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> T requireNonNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    public static <T> T requireNonNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    public static <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {
        return Objects.requireNonNull(obj, messageSupplier);
    }

    public static <T> T requireNonNullElse(T obj, T defaultObj) {
        return Objects.requireNonNullElse(obj, defaultObj);
    }

    public static <T> T requireNonNullElseGet(T obj, Supplier<? extends T> supplier) {
        return Objects.requireNonNullElseGet(obj, supplier);
    }

    public static boolean isSameType(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.getClass() == o2.getClass();
    }

    public static boolean isInstanceOf(Class<?> type, Object obj) {
        if (type == null || obj == null) {
            return false;
        }
        return type.isInstance(obj);
    }

    public static <T> Optional<T> toOptional(T obj) {
        return Optional.ofNullable(obj);
    }

    public static <T> Optional<T> toOptionalIfEmpty(T obj) {
        if (isEmpty(obj)) {
            return Optional.empty();
        }
        return Optional.of(obj);
    }
}
