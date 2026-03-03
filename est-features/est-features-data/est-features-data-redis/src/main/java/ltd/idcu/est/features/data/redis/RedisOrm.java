package ltd.idcu.est.features.data.redis;

import ltd.idcu.est.features.data.api.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisOrm implements Orm {
    
    private final RedisClient client;
    private final String keyPrefix;
    private final Map<Class<?>, RedisRepository<?, ?>> repositoryCache;
    private final Map<Class<?>, EntityMetadata> metadataCache;
    
    public RedisOrm(RedisClient client) {
        this(client, "");
    }
    
    public RedisOrm(RedisClient client, String keyPrefix) {
        this.client = client;
        this.keyPrefix = keyPrefix != null ? keyPrefix : "";
        this.repositoryCache = new ConcurrentHashMap<>();
        this.metadataCache = new ConcurrentHashMap<>();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> find(Class<T> entityClass, Object id) {
        RedisRepository<T, Object> repository = getRepository(entityClass);
        return repository.findById((Object) id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> entityClass) {
        throw new UnsupportedOperationException("Use scan or keys pattern for all entities");
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        RedisRepository<T, Object> repository = (RedisRepository<T, Object>) getRepository(entity.getClass());
        return repository.save(entity);
    }
    
    @Override
    public <T> T update(T entity) {
        return save(entity);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> void delete(T entity) {
        if (entity == null) {
            return;
        }
        RedisRepository<T, Object> repository = (RedisRepository<T, Object>) getRepository(entity.getClass());
        repository.delete(entity);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> int deleteById(Class<T> entityClass, Object id) {
        RedisRepository<T, Object> repository = getRepository(entityClass);
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            return 1;
        }
        return 0;
    }
    
    @Override
    public <T> Query<T> query(Class<T> entityClass) {
        throw new UnsupportedOperationException("Query not supported for Redis. Use findByField methods.");
    }
    
    @Override
    public <T> long count(Class<T> entityClass) {
        return client.dbSize();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean exists(Class<T> entityClass, Object id) {
        RedisRepository<T, Object> repository = getRepository(entityClass);
        return repository.existsById(id);
    }
    
    @Override
    public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value) {
        throw new UnsupportedOperationException("Use hash structures for field-based queries");
    }
    
    @Override
    public <T> Optional<T> findOneByField(Class<T> entityClass, String fieldName, Object value) {
        throw new UnsupportedOperationException("Use hash structures for field-based queries");
    }
    
    @Override
    public <T> List<T> findByFields(Class<T> entityClass, Map<String, Object> fields) {
        throw new UnsupportedOperationException("Use hash structures for field-based queries");
    }
    
    @Override
    public <T> List<T> executeQuery(Class<T> entityClass, String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported for Redis");
    }
    
    @Override
    public int executeUpdate(String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported for Redis");
    }
    
    @Override
    public long executeInsert(String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported for Redis");
    }
    
    @Override
    public String getTableName(Class<?> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        return metadata.keyPrefix;
    }
    
    @Override
    public String getIdFieldName(Class<?> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        return metadata.idFieldName;
    }
    
    public <T> T saveWithTtl(T entity, long ttl, TimeUnit timeUnit) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        @SuppressWarnings("unchecked")
        RedisRepository<T, Object> repository = (RedisRepository<T, Object>) getRepository(entity.getClass());
        return repository.saveWithTtl(entity, ttl, timeUnit);
    }
    
    public boolean setExpire(Class<?> entityClass, Object id, long ttl, TimeUnit timeUnit) {
        @SuppressWarnings("unchecked")
        RedisRepository<Object, Object> repository = (RedisRepository<Object, Object>) getRepository(entityClass);
        return repository.setExpire(id, ttl, timeUnit);
    }
    
    public long getTtl(Class<?> entityClass, Object id) {
        @SuppressWarnings("unchecked")
        RedisRepository<Object, Object> repository = (RedisRepository<Object, Object>) getRepository(entityClass);
        return repository.getTtl(id);
    }
    
    @SuppressWarnings("unchecked")
    private <T> RedisRepository<T, Object> getRepository(Class<T> entityClass) {
        return (RedisRepository<T, Object>) repositoryCache.computeIfAbsent(entityClass, 
                clazz -> createRepository((Class<T>) clazz));
    }
    
    private <T> RedisRepository<T, Object> createRepository(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        String prefix = keyPrefix.isEmpty() ? metadata.keyPrefix : keyPrefix + ":" + metadata.keyPrefix;
        
        RedisRepository.EntitySerializer<T> serializer = createSerializer(entityClass);
        RedisRepository.EntityIdExtractor<T, Object> idExtractor = entity -> {
            try {
                if (metadata.idField != null) {
                    metadata.idField.setAccessible(true);
                    return (Object) metadata.idField.get(entity);
                }
            } catch (Exception ignored) {
            }
            return null;
        };
        
        return new RedisRepository<>(client, prefix, serializer, idExtractor, null);
    }
    
    @SuppressWarnings("unchecked")
    private <T> RedisRepository.EntitySerializer<T> createSerializer(Class<T> entityClass) {
        return new RedisRepository.EntitySerializer<T>() {
            @Override
            public String serialize(T entity) {
                return serializeEntity(entity);
            }
            
            @Override
            public T deserialize(String value) {
                return (T) deserializeEntity(value, entityClass);
            }
        };
    }
    
    private EntityMetadata getEntityMetadata(Class<?> entityClass) {
        return metadataCache.computeIfAbsent(entityClass, this::extractMetadata);
    }
    
    private EntityMetadata extractMetadata(Class<?> entityClass) {
        EntityMetadata metadata = new EntityMetadata();
        
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        if (entityAnnotation != null && !entityAnnotation.tableName().isEmpty()) {
            metadata.keyPrefix = entityAnnotation.tableName();
        } else {
            metadata.keyPrefix = entityClass.getSimpleName().toLowerCase();
        }
        
        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                metadata.idField = field;
                metadata.idFieldName = field.getName();
                break;
            }
        }
        
        return metadata;
    }
    
    private String serializeEntity(Object entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        java.lang.reflect.Field[] fields = entity.getClass().getDeclaredFields();
        boolean first = true;
        
        for (java.lang.reflect.Field field : fields) {
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            
            try {
                field.setAccessible(true);
                Object value = field.get(entity);
                
                if (!first) {
                    sb.append(",");
                }
                first = false;
                
                sb.append("\"").append(field.getName()).append("\":");
                
                if (value == null) {
                    sb.append("null");
                } else if (value instanceof String) {
                    sb.append("\"").append(escapeJson((String) value)).append("\"");
                } else if (value instanceof Number || value instanceof Boolean) {
                    sb.append(value);
                } else {
                    sb.append("\"").append(escapeJson(value.toString())).append("\"");
                }
            } catch (Exception ignored) {
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    private Object deserializeEntity(String value, Class<?> entityClass) {
        try {
            Object entity = entityClass.getDeclaredConstructor().newInstance();
            Map<String, String> data = parseJson(value);
            
            for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                
                String fieldValue = data.get(field.getName());
                if (fieldValue != null) {
                    field.setAccessible(true);
                    Object converted = convertValue(fieldValue, field.getType());
                    field.set(entity, converted);
                }
            }
            
            return entity;
        } catch (Exception e) {
            throw new DataException("Failed to deserialize entity", e);
        }
    }
    
    private Map<String, String> parseJson(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        
        if (json == null || !json.startsWith("{") || !json.endsWith("}")) {
            return result;
        }
        
        json = json.substring(1, json.length() - 1);
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean inKey = true;
        boolean inString = false;
        boolean inValue = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                continue;
            }
            
            if (!inString) {
                if (c == ':') {
                    inKey = false;
                    inValue = true;
                    continue;
                }
                if (c == ',') {
                    result.put(key.toString().trim(), value.toString().trim());
                    key = new StringBuilder();
                    value = new StringBuilder();
                    inKey = true;
                    inValue = false;
                    continue;
                }
            }
            
            if (inKey) {
                key.append(c);
            } else if (inValue) {
                value.append(c);
            }
        }
        
        if (key.length() > 0) {
            result.put(key.toString().trim(), value.toString().trim());
        }
        
        return result;
    }
    
    private Object convertValue(String value, Class<?> type) {
        if (value == null || value.equals("null")) {
            return null;
        }
        
        if (type == String.class) {
            return value;
        }
        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        }
        if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        }
        if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        }
        if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        }
        if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        
        return value;
    }
    
    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    private static class EntityMetadata {
        String keyPrefix;
        java.lang.reflect.Field idField;
        String idFieldName;
    }
}
