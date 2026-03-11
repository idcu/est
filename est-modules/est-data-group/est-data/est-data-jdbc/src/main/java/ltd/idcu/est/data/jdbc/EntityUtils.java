package ltd.idcu.est.data.jdbc;

import ltd.idcu.est.data.api.*;

import java.lang.reflect.Field;
import java.util.*;

public class EntityUtils {

    private EntityUtils() {
    }

    public static Object getIdValue(Object entity) {
        if (entity == null) {
            return null;
        }
        try {
            Class<?> entityClass = entity.getClass();
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return field.get(entity);
                }
            }
        } catch (Exception e) {
            throw new DataException("Failed to get id value", e);
        }
        return null;
    }

    public static void setIdValue(Object entity, Object id) {
        if (entity == null || id == null) {
            return;
        }
        try {
            Class<?> entityClass = entity.getClass();
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    field.set(entity, id);
                    return;
                }
            }
        } catch (Exception e) {
            throw new DataException("Failed to set id value", e);
        }
    }

    public static String getTableName(Class<?> entityClass) {
        if (entityClass == null) {
            return null;
        }
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        if (entityAnnotation != null && !entityAnnotation.tableName().isEmpty()) {
            return entityAnnotation.tableName();
        }
        return camelToSnake(entityClass.getSimpleName());
    }

    public static String getIdFieldName(Class<?> entityClass) {
        if (entityClass == null) {
            return null;
        }
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        return "id";
    }

    public static Map<String, Object> toMap(Object entity) {
        if (entity == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<>();
        try {
            Class<?> entityClass = entity.getClass();
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value != null) {
                    String columnName = getColumnName(field);
                    map.put(columnName, value);
                }
            }
        } catch (Exception e) {
            throw new DataException("Failed to convert entity to map", e);
        }
        return map;
    }

    public static <T> T fromMap(Class<T> entityClass, Map<String, Object> map) {
        if (entityClass == null || map == null) {
            return null;
        }
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for (Field field : entityClass.getDeclaredFields()) {
                String columnName = getColumnName(field);
                if (map.containsKey(columnName)) {
                    field.setAccessible(true);
                    field.set(entity, map.get(columnName));
                }
            }
            return entity;
        } catch (Exception e) {
            throw new DataException("Failed to convert map to entity", e);
        }
    }

    public static String getColumnName(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
            return columnAnnotation.name();
        }
        return camelToSnake(field.getName());
    }

    public static String getColumnName(Class<?> entityClass, String fieldName) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            return getColumnName(field);
        } catch (NoSuchFieldException e) {
            return camelToSnake(fieldName);
        }
    }

    private static String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static boolean isNew(Object entity) {
        return getIdValue(entity) == null;
    }

    public static void copyNonNullProperties(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        try {
            Class<?> sourceClass = source.getClass();
            Class<?> targetClass = target.getClass();
            for (Field sourceField : sourceClass.getDeclaredFields()) {
                if (sourceField.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                sourceField.setAccessible(true);
                Object value = sourceField.get(source);
                if (value != null) {
                    try {
                        Field targetField = targetClass.getDeclaredField(sourceField.getName());
                        targetField.setAccessible(true);
                        targetField.set(target, value);
                    } catch (NoSuchFieldException ignored) {
                    }
                }
            }
        } catch (Exception e) {
            throw new DataException("Failed to copy non-null properties", e);
        }
    }
}
