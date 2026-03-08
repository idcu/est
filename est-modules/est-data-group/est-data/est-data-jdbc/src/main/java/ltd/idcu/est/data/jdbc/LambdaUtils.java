package ltd.idcu.est.data.jdbc;

import ltd.idcu.est.features.data.api.SFunction;
import ltd.idcu.est.features.data.api.Column;
import ltd.idcu.est.features.data.api.Transient;
import ltd.idcu.est.features.data.api.DataException;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public class LambdaUtils {

    public static class LambdaInfo<T> {
        public final Class<T> entityClass;
        public final String fieldName;
        public final String columnName;
        public final Class<?> fieldType;
        public final Field field;
        
        public LambdaInfo(Class<T> entityClass, String fieldName, String columnName, Class<?> fieldType, Field field) {
            this.entityClass = entityClass;
            this.fieldName = fieldName;
            this.columnName = columnName;
            this.fieldType = fieldType;
            this.field = field;
        }
    }

    public static <T> String getColumnName(SFunction<T, ?> func) {
        LambdaInfo<T> info = resolveLambda(func);
        return info.columnName;
    }

    public static <T> String getFieldName(SFunction<T, ?> func) {
        LambdaInfo<T> info = resolveLambda(func);
        return info.fieldName;
    }

    public static <T> Class<?> getFieldType(SFunction<T, ?> func) {
        LambdaInfo<T> info = resolveLambda(func);
        return info.fieldType;
    }

    public static <T> LambdaInfo<T> resolveLambda(SFunction<T, ?> func) {
        Objects.requireNonNull(func, "Lambda function cannot be null");
        
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(func);
            String methodName = serializedLambda.getImplMethodName();
            
            String fieldName;
            if (methodName.startsWith("get")) {
                fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            } else if (methodName.startsWith("is")) {
                fieldName = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
            } else {
                fieldName = methodName;
            }
            
            String className = serializedLambda.getImplClass().replace("/", ".");
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName(className);
            
            Field field = findField(clazz, fieldName);
            if (field == null) {
                throw new DataException("Field '" + fieldName + "' not found in class " + clazz.getName());
            }
            
            validateField(clazz, field, fieldName);
            
            String columnName = resolveColumnName(field, fieldName);
            Class<?> fieldType = field.getType();
            
            return new LambdaInfo<>(clazz, fieldName, columnName, fieldType, field);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            throw new DataException("Failed to resolve lambda expression: " + e.getMessage(), e);
        }
    }

    private static void validateField(Class<?> entityClass, Field field, String fieldName) {
        if (field.isAnnotationPresent(Transient.class)) {
            throw new DataException("Field '" + fieldName + "' in class " + entityClass.getName() 
                    + " is marked as @Transient and cannot be used in queries");
        }
        
        if (Modifier.isStatic(field.getModifiers())) {
            throw new DataException("Field '" + fieldName + "' in class " + entityClass.getName() 
                    + " is static and cannot be used in queries");
        }
    }

    private static String resolveColumnName(Field field, String fieldName) {
        Column column = field.getAnnotation(Column.class);
        if (column != null && !column.name().isEmpty()) {
            return column.name();
        }
        return camelToSnake(fieldName);
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    public static <T, V> void validateType(SFunction<T, V> func, V value) {
        if (value == null) {
            return;
        }
        
        LambdaInfo<T> info = resolveLambda(func);
        Class<?> expectedType = info.fieldType;
        Class<?> actualType = value.getClass();
        
        if (!isCompatibleType(expectedType, actualType)) {
            throw new DataException("Type mismatch for field '" + info.fieldName + "': " +
                    "expected " + expectedType.getName() + ", " +
                    "actual " + actualType.getName());
        }
    }

    public static boolean isCompatibleType(Class<?> expectedType, Class<?> actualType) {
        if (expectedType.isAssignableFrom(actualType)) {
            return true;
        }
        
        if (expectedType.isPrimitive()) {
            return isWrapperTypeForPrimitive(expectedType, actualType);
        }
        
        if (actualType.isPrimitive()) {
            return isWrapperTypeForPrimitive(actualType, expectedType);
        }
        
        if (Number.class.isAssignableFrom(expectedType) && Number.class.isAssignableFrom(actualType)) {
            return true;
        }
        
        return false;
    }

    private static boolean isWrapperTypeForPrimitive(Class<?> primitiveType, Class<?> wrapperType) {
        if (primitiveType == int.class) return wrapperType == Integer.class;
        if (primitiveType == long.class) return wrapperType == Long.class;
        if (primitiveType == double.class) return wrapperType == Double.class;
        if (primitiveType == float.class) return wrapperType == Float.class;
        if (primitiveType == boolean.class) return wrapperType == Boolean.class;
        if (primitiveType == char.class) return wrapperType == Character.class;
        if (primitiveType == byte.class) return wrapperType == Byte.class;
        if (primitiveType == short.class) return wrapperType == Short.class;
        return false;
    }

    private static String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static <T> void checkEntityClass(Class<T> expectedClass, SFunction<T, ?> func) {
        LambdaInfo<T> info = resolveLambda(func);
        if (!expectedClass.equals(info.entityClass)) {
            throw new DataException("Lambda function refers to " + info.entityClass.getName() + 
                    ", but expected " + expectedClass.getName());
        }
    }
}
