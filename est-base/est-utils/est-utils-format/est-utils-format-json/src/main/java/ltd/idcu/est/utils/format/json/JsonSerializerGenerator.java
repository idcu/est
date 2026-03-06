package ltd.idcu.est.utils.format.json;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface JsonSerializerGenerator {
    
    <T> JsonSerializer<T> generateSerializer(Class<T> clazz);
    
    <T> JsonDeserializer<T> generateDeserializer(Class<T> clazz);
}

class ReflectiveJsonSerializerGenerator implements JsonSerializerGenerator {
    
    private static final Map<Class<?>, JsonSerializer<?>> serializerCache = new ConcurrentHashMap<>();
    private static final Map<Class<?>, JsonDeserializer<?>> deserializerCache = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> JsonSerializer<T> generateSerializer(Class<T> clazz) {
        return (JsonSerializer<T>) serializerCache.computeIfAbsent(clazz, 
            c -> new ReflectiveJsonSerializer<>(c));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> JsonDeserializer<T> generateDeserializer(Class<T> clazz) {
        return (JsonDeserializer<T>) deserializerCache.computeIfAbsent(clazz, 
            c -> new ReflectiveJsonDeserializer<>(c));
    }
    
    static class ReflectiveJsonSerializer<T> implements JsonSerializer<T> {
        
        private final Class<T> clazz;
        private final Field[] fields;
        
        ReflectiveJsonSerializer(Class<T> clazz) {
            this.clazz = clazz;
            this.fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
        }
        
        @Override
        public String serialize(T obj) {
            ObjectNode node = new ObjectNode();
            try {
                for (Field field : fields) {
                    String name = field.getName();
                    Object value = field.get(obj);
                    node.set(name, toJsonNode(value));
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize " + clazz.getName(), e);
            }
            return node.toJson();
        }
        
        private JsonNode toJsonNode(Object value) {
            if (value == null) {
                return NullNode.getInstance();
            }
            if (value instanceof String) {
                return new TextNode((String) value);
            }
            if (value instanceof Integer) {
                return new IntNode((Integer) value);
            }
            if (value instanceof Long) {
                return new LongNode((Long) value);
            }
            if (value instanceof Double) {
                return new DoubleNode((Double) value);
            }
            if (value instanceof Boolean) {
                return BooleanNode.valueOf((Boolean) value);
            }
            return new TextNode(value.toString());
        }
    }
    
    static class ReflectiveJsonDeserializer<T> implements JsonDeserializer<T> {
        
        private final Class<T> clazz;
        private final Field[] fields;
        
        ReflectiveJsonDeserializer(Class<T> clazz) {
            this.clazz = clazz;
            this.fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public T deserialize(Object value) {
            try {
                T obj = clazz.getDeclaredConstructor().newInstance();
                if (value instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) value;
                    for (Field field : fields) {
                        Object fieldValue = map.get(field.getName());
                        if (fieldValue != null) {
                            field.set(obj, convertValue(fieldValue, field.getType()));
                        }
                    }
                }
                return obj;
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize to " + clazz.getName(), e);
            }
        }
        
        @SuppressWarnings("unchecked")
        private Object convertValue(Object value, Class<?> targetType) {
            if (targetType.isInstance(value)) {
                return value;
            }
            if (targetType == String.class) {
                return String.valueOf(value);
            }
            if (targetType == Integer.class || targetType == int.class) {
                if (value instanceof Number) {
                    return ((Number) value).intValue();
                }
                return Integer.parseInt(String.valueOf(value));
            }
            if (targetType == Long.class || targetType == long.class) {
                if (value instanceof Number) {
                    return ((Number) value).longValue();
                }
                return Long.parseLong(String.valueOf(value));
            }
            if (targetType == Double.class || targetType == double.class) {
                if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                }
                return Double.parseDouble(String.valueOf(value));
            }
            if (targetType == Boolean.class || targetType == boolean.class) {
                if (value instanceof Boolean) {
                    return value;
                }
                return Boolean.parseBoolean(String.valueOf(value));
            }
            return value;
        }
    }
}

class JsonSerializerGeneratorFactory {
    
    private static JsonSerializerGenerator generator = new ReflectiveJsonSerializerGenerator();
    
    public static void setGenerator(JsonSerializerGenerator gen) {
        generator = gen;
    }
    
    public static JsonSerializerGenerator getGenerator() {
        return generator;
    }
}
