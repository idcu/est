package ltd.idcu.est.utils.common;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class AssertUtils {

    private AssertUtils() {
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isFalse(boolean expression, Supplier<String> messageSupplier) {
        if (expression) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static <T> T requireNonNull(T object, String message) {
        return Objects.requireNonNull(object, message);
    }

    public static <T> T requireNonNull(T object, Supplier<String> messageSupplier) {
        return Objects.requireNonNull(object, messageSupplier);
    }

    public static <T> T requireNonNullElse(T object, T defaultObject) {
        return object != null ? object : Objects.requireNonNull(defaultObject, "defaultObject");
    }

    public static <T> T requireNonNullElseGet(T object, Supplier<T> supplier) {
        return object != null ? object : Objects.requireNonNull(Objects.requireNonNull(supplier, "supplier").get(), "supplier.get()");
    }

    public static void hasLength(String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text, Supplier<String> messageSupplier) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void hasText(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(String text, Supplier<String> messageSupplier) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void doesNotContain(String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array, Supplier<String> messageSupplier) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, Supplier<String> messageSupplier) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void noNullElements(Object[] array, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void noNullElements(Collection<?> collection, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void noNullElements(Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, Supplier<String> messageSupplier) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj) {
        isInstanceOf(type, obj, () -> "Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
                "] must be an instance of " + type.getName());
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, Supplier<String> messageSupplier) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, () -> subType + " is not assignable to " + superType);
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalStateException(nullSafeGet(messageSupplier));
        }
    }

    public static void inRange(int value, int min, int max, String message) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void inRange(int value, int min, int max, Supplier<String> messageSupplier) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void inRange(long value, long min, long max, String message) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void inRange(long value, long min, long max, Supplier<String> messageSupplier) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void inRange(double value, double min, double max, String message) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void inRange(double value, double min, double max, Supplier<String> messageSupplier) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isPositive(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(int value, Supplier<String> messageSupplier) {
        if (value <= 0) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isPositive(long value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(long value, Supplier<String> messageSupplier) {
        if (value <= 0) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isPositive(double value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPositive(double value, Supplier<String> messageSupplier) {
        if (value <= 0) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void isNegative(int value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(long value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNegative(double value, String message) {
        if (value >= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String str, String message) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void matches(String str, String regex, String message) {
        if (str == null || !str.matches(regex)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void matches(String str, String regex, Supplier<String> messageSupplier) {
        if (str == null || !str.matches(regex)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void equals(Object obj1, Object obj2, String message) {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void equals(Object obj1, Object obj2, Supplier<String> messageSupplier) {
        if (!Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static void notEquals(Object obj1, Object obj2, String message) {
        if (Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEquals(Object obj1, Object obj2, Supplier<String> messageSupplier) {
        if (Objects.equals(obj1, obj2)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    public static <T> T checkNotNull(T reference, String message) {
        if (reference == null) {
            throw new NullPointerException(message);
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, Supplier<String> messageSupplier) {
        if (reference == null) {
            throw new NullPointerException(nullSafeGet(messageSupplier));
        }
        return reference;
    }

    public static int checkElementIndex(int index, int size, String message) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(message);
        }
        return index;
    }

    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "Index " + index + " out of bounds for size " + size);
    }

    public static int checkPositionIndex(int index, int size, String message) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(message);
        }
        return index;
    }

    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, "Index " + index + " out of bounds for size " + size);
    }

    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException("Positions [" + start + ", " + end + ") out of bounds for size " + size);
        }
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return messageSupplier != null ? messageSupplier.get() : null;
    }
}
