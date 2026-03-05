package ltd.idcu.est.utils.common;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ClassUtils {

    private static final char PACKAGE_SEPARATOR_CHAR = '.';
    private static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    private static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);
    private static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static final int INITIAL_BUFFER_SIZE = 4096;

    private ClassUtils() {
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            try {
                cl = ClassUtils.class.getClassLoader();
            } catch (Throwable ex) {
            }
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                }
            }
        }
        return cl;
    }

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, getDefaultClassLoader());
    }

    public static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
        AssertUtils.notBlank(className, "Class name must not be blank");

        ClassLoader loaderToUse = classLoader;
        if (loaderToUse == null) {
            loaderToUse = getDefaultClassLoader();
        }

        try {
            return Class.forName(className, false, loaderToUse);
        } catch (ClassNotFoundException ex) {
            int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
            if (lastDotIndex != -1) {
                String innerClassName = className.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR_CHAR +
                        className.substring(lastDotIndex + 1);
                try {
                    return Class.forName(innerClassName, false, loaderToUse);
                } catch (ClassNotFoundException ex2) {
                }
            }
            throw ex;
        }
    }

    public static Class<?> resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
        try {
            return forName(className, classLoader);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
        }
    }

    public static boolean isPresent(String className) {
        return isPresent(className, getDefaultClassLoader());
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            forName(className, classLoader);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    public static Class<?> resolvePrimitiveClassName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return switch (name) {
            case "boolean" -> boolean.class;
            case "byte" -> byte.class;
            case "char" -> char.class;
            case "short" -> short.class;
            case "int" -> int.class;
            case "long" -> long.class;
            case "float" -> float.class;
            case "double" -> double.class;
            case "void" -> void.class;
            default -> null;
        };
    }

    public static boolean isPrimitiveWrapper(Class<?> type) {
        if (type == null) {
            return false;
        }
        return type == Boolean.class || type == Byte.class || type == Character.class ||
                type == Short.class || type == Integer.class || type == Long.class ||
                type == Float.class || type == Double.class || type == Void.class;
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        if (type == null) {
            return false;
        }
        return type.isPrimitive() || isPrimitiveWrapper(type);
    }

    public static Class<?> primitiveToWrapper(Class<?> primitiveType) {
        if (primitiveType == null || !primitiveType.isPrimitive()) {
            return primitiveType;
        }
        if (primitiveType == boolean.class) return Boolean.class;
        if (primitiveType == byte.class) return Byte.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == short.class) return Short.class;
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == double.class) return Double.class;
        if (primitiveType == void.class) return Void.class;
        return primitiveType;
    }

    public static Class<?> wrapperToPrimitive(Class<?> wrapperType) {
        if (wrapperType == null || wrapperType.isPrimitive()) {
            return wrapperType;
        }
        if (wrapperType == Boolean.class) return boolean.class;
        if (wrapperType == Byte.class) return byte.class;
        if (wrapperType == Character.class) return char.class;
        if (wrapperType == Short.class) return short.class;
        if (wrapperType == Integer.class) return int.class;
        if (wrapperType == Long.class) return long.class;
        if (wrapperType == Float.class) return float.class;
        if (wrapperType == Double.class) return double.class;
        if (wrapperType == Void.class) return void.class;
        return null;
    }

    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        AssertUtils.notNull(lhsType, "Left-hand side type must not be null");
        AssertUtils.notNull(rhsType, "Right-hand side type must not be null");

        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }

        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = wrapperToPrimitive(rhsType);
            return lhsType == resolvedPrimitive;
        } else {
            Class<?> resolvedWrapper = primitiveToWrapper(rhsType);
            return lhsType.isAssignableFrom(resolvedWrapper);
        }
    }

    public static boolean isAssignableValue(Class<?> type, Object value) {
        AssertUtils.notNull(type, "Type must not be null");
        return value == null ? !type.isPrimitive() : isAssignable(type, value.getClass());
    }

    public static String getShortName(Class<?> clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    public static String getShortName(String className) {
        if (StringUtils.isEmpty(className)) {
            return "";
        }
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
        return shortName;
    }

    public static String getShortNameAsProperty(Class<?> clazz) {
        String shortName = getShortName(clazz);
        int dotIndex = shortName.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (dotIndex != -1) {
            shortName = shortName.substring(dotIndex + 1);
        }
        return StringUtils.uncapitalize(shortName);
    }

    public static String getQualifiedName(Class<?> clazz) {
        AssertUtils.notNull(clazz, "Class must not be null");
        return clazz.getName();
    }

    public static String getPackageName(Class<?> clazz) {
        AssertUtils.notNull(clazz, "Class must not be null");
        return getPackageName(clazz.getName());
    }

    public static String getPackageName(String fqClassName) {
        AssertUtils.notNull(fqClassName, "Class name must not be null");
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        return lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "";
    }

    public static String convertClassNameToResourcePath(String className) {
        AssertUtils.notNull(className, "Class name must not be null");
        return className.replace(PACKAGE_SEPARATOR_CHAR, '/');
    }

    public static String convertResourcePathToClassName(String resourcePath) {
        AssertUtils.notNull(resourcePath, "Resource path must not be null");
        return resourcePath.replace('/', PACKAGE_SEPARATOR_CHAR);
    }

    public static String getClassFileName(Class<?> clazz) {
        AssertUtils.notNull(clazz, "Class must not be null");
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        return className.substring(lastDotIndex + 1) + ".class";
    }

    public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName) {
        AssertUtils.notNull(clazz, "Class must not be null");
        AssertUtils.notNull(resourceName, "Resource name must not be null");
        if (!resourceName.startsWith("/")) {
            return classPackageAsResourcePath(clazz) + "/" + resourceName;
        }
        return classPackageAsResourcePath(clazz) + resourceName;
    }

    public static String classPackageAsResourcePath(Class<?> clazz) {
        if (clazz == null || clazz.getPackage() == null) {
            return "";
        }
        return clazz.getPackage().getName().replace(PACKAGE_SEPARATOR_CHAR, '/');
    }

    public static InputStream getResourceAsStream(Class<?> clazz, String resourceName) {
        AssertUtils.notNull(clazz, "Class must not be null");
        AssertUtils.notNull(resourceName, "Resource name must not be null");
        return clazz.getResourceAsStream(resourceName);
    }

    public static byte[] getResourceAsBytes(Class<?> clazz, String resourceName) throws IOException {
        try (InputStream is = getResourceAsStream(clazz, resourceName)) {
            if (is == null) {
                return null;
            }
            return readAllBytes(is);
        }
    }

    public static String getResourceAsString(Class<?> clazz, String resourceName) throws IOException {
        byte[] bytes = getResourceAsBytes(clazz, resourceName);
        return bytes != null ? new String(bytes) : null;
    }

    private static byte[] readAllBytes(InputStream is) throws IOException {
        byte[] buffer = new byte[INITIAL_BUFFER_SIZE];
        int read;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        while ((read = is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    public static boolean isInnerClass(Class<?> clazz) {
        return clazz != null && clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers());
    }

    public static boolean isAnonymousClass(Class<?> clazz) {
        return clazz != null && clazz.isAnonymousClass();
    }

    public static boolean isLocalClass(Class<?> clazz) {
        return clazz != null && clazz.isLocalClass();
    }

    public static boolean isMemberClass(Class<?> clazz) {
        return clazz != null && clazz.isMemberClass();
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    public static boolean isEnum(Class<?> clazz) {
        return clazz != null && clazz.isEnum();
    }

    public static boolean isInterface(Class<?> clazz) {
        return clazz != null && clazz.isInterface();
    }

    public static boolean isAbstract(Class<?> clazz) {
        return clazz != null && Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isFinal(Class<?> clazz) {
        return clazz != null && Modifier.isFinal(clazz.getModifiers());
    }

    public static boolean isPublic(Class<?> clazz) {
        return clazz != null && Modifier.isPublic(clazz.getModifiers());
    }

    public static boolean isStatic(Class<?> clazz) {
        return clazz != null && Modifier.isStatic(clazz.getModifiers());
    }

    public static boolean isConcreteClass(Class<?> clazz) {
        return clazz != null && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        AssertUtils.notNull(clazz, "Class must not be null");
        return clazz.getConstructor(parameterTypes);
    }

    public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        AssertUtils.notNull(clazz, "Class must not be null");
        Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        AssertUtils.notNull(clazz, "Class must not be null");
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new InstantiationException("Cannot instantiate interface or abstract class: " + clazz.getName());
        }
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes, Object[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        AssertUtils.notNull(clazz, "Class must not be null");
        Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(args);
    }

    public static <T> T newInstanceQuietly(Class<T> clazz) {
        try {
            return newInstance(clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        AssertUtils.notNull(clazz, "Class must not be null");
        AssertUtils.notBlank(methodName, "Method name must not be blank");

        Class<?> searchType = clazz;
        while (searchType != null && searchType != Object.class) {
            Method[] methods = searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName()) &&
                        (parameterTypes == null || Arrays.equals(parameterTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static List<Method> findMethods(Class<?> clazz, String methodName) {
        AssertUtils.notNull(clazz, "Class must not be null");
        AssertUtils.notBlank(methodName, "Method name must not be blank");

        List<Method> result = new ArrayList<>();
        Class<?> searchType = clazz;
        while (searchType != null && searchType != Object.class) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    result.add(method);
                }
            }
            searchType = searchType.getSuperclass();
        }
        return result;
    }

    public static Method[] getAllDeclaredMethods(Class<?> clazz) {
        AssertUtils.notNull(clazz, "Class must not be null");
        List<Method> methods = new ArrayList<>();
        Class<?> searchType = clazz;
        while (searchType != null && searchType != Object.class) {
            methods.addAll(Arrays.asList(searchType.getDeclaredMethods()));
            searchType = searchType.getSuperclass();
        }
        return methods.toArray(new Method[0]);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) throws InvocationTargetException, IllegalAccessException {
        AssertUtils.notNull(method, "Method must not be null");
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    public static Object invokeMethodQuietly(Method method, Object target, Object... args) {
        try {
            return invokeMethod(method, target, args);
        } catch (Exception e) {
            return null;
        }
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        return findField(clazz, fieldName, null);
    }

    public static Field findField(Class<?> clazz, String fieldName, Class<?> fieldType) {
        AssertUtils.notNull(clazz, "Class must not be null");
        AssertUtils.isTrue(fieldName != null || fieldType != null, "Either field name or field type must not be null");

        Class<?> searchType = clazz;
        while (searchType != null && searchType != Object.class) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((fieldName == null || fieldName.equals(field.getName())) &&
                        (fieldType == null || fieldType.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static Field[] getAllDeclaredFields(Class<?> clazz) {
        AssertUtils.notNull(clazz, "Class must not be null");
        List<Field> fields = new ArrayList<>();
        Class<?> searchType = clazz;
        while (searchType != null && searchType != Object.class) {
            fields.addAll(Arrays.asList(searchType.getDeclaredFields()));
            searchType = searchType.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    public static void setField(Field field, Object target, Object value) throws IllegalAccessException {
        AssertUtils.notNull(field, "Field must not be null");
        field.setAccessible(true);
        field.set(target, value);
    }

    public static void setFieldQuietly(Field field, Object target, Object value) {
        try {
            setField(field, target, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Field field, Object target) throws IllegalAccessException {
        AssertUtils.notNull(field, "Field must not be null");
        field.setAccessible(true);
        return (T) field.get(target);
    }

    public static <T> T getFieldQuietly(Field field, Object target) {
        try {
            return getField(field, target);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Optional<Type> getGenericSuperclassTypeArgument(Class<?> clazz, int typeIndex) {
        AssertUtils.notNull(clazz, "Class must not be null");
        Type superType = clazz.getGenericSuperclass();
        if (superType instanceof ParameterizedType parameterizedType) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeIndex >= 0 && typeIndex < typeArguments.length) {
                return Optional.of(typeArguments[typeIndex]);
            }
        }
        return Optional.empty();
    }

    public static Optional<Type> getGenericInterfaceTypeArgument(Class<?> clazz, Class<?> interfaceClass, int typeIndex) {
        AssertUtils.notNull(clazz, "Class must not be null");
        AssertUtils.notNull(interfaceClass, "Interface class must not be null");

        Type[] interfaceTypes = clazz.getGenericInterfaces();
        for (Type interfaceType : interfaceTypes) {
            if (interfaceType instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class<?> rawClass && interfaceClass.isAssignableFrom(rawClass)) {
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    if (typeIndex >= 0 && typeIndex < typeArguments.length) {
                        return Optional.of(typeArguments[typeIndex]);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static Class<?> getUserClass(Object instance) {
        AssertUtils.notNull(instance, "Instance must not be null");
        return getUserClass(instance.getClass());
    }

    public static Class<?> getUserClass(Class<?> clazz) {
        AssertUtils.notNull(clazz, "Class must not be null");
        if (clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                return superclass;
            }
        }
        return clazz;
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR);
    }
}
