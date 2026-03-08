package ltd.idcu.est.data.mongodb;

import ltd.idcu.est.data.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MongoOrm implements Orm {
    
    private final MongoClient client;
    private final String database;
    private final Map<Class<?>, MongoRepository<?, ?>> repositoryCache;
    private final Map<Class<?>, EntityMetadata> metadataCache;
    
    public MongoOrm(MongoClient client) {
        this(client, null);
    }
    
    public MongoOrm(MongoClient client, String database) {
        this.client = client;
        this.database = database;
        this.repositoryCache = new ConcurrentHashMap<>();
        this.metadataCache = new ConcurrentHashMap<>();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> find(Class<T> entityClass, Object id) {
        MongoRepository<T, Object> repository = getRepository(entityClass);
        return repository.findById(id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> entityClass) {
        MongoRepository<T, Object> repository = getRepository(entityClass);
        return repository.findAll();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        MongoRepository<T, Object> repository = (MongoRepository<T, Object>) getRepository(entity.getClass());
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
        MongoRepository<T, Object> repository = (MongoRepository<T, Object>) getRepository(entity.getClass());
        repository.delete(entity);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> int deleteById(Class<T> entityClass, Object id) {
        MongoRepository<T, Object> repository = getRepository(entityClass);
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            return 1;
        }
        return 0;
    }
    
    @Override
    public <T> Query<T> query(Class<T> entityClass) {
        throw new UnsupportedOperationException("Query not yet implemented for MongoDB. Use findByField methods.");
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> long count(Class<T> entityClass) {
        MongoRepository<T, Object> repository = getRepository(entityClass);
        return repository.count();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean exists(Class<T> entityClass, Object id) {
        MongoRepository<T, Object> repository = getRepository(entityClass);
        return repository.existsById(id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value) {
        MongoRepository<T, Object> repository = getRepository(entityClass);
        return repository.findByField(fieldName, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> findOneByField(Class<T> entityClass, String fieldName, Object value) {
        List<T> results = findByField(entityClass, fieldName, value);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public <T> List<T> findByFields(Class<T> entityClass, Map<String, Object> fields) {
        throw new UnsupportedOperationException("Use filter-based queries for multiple fields");
    }
    
    @Override
    public <T> List<T> executeQuery(Class<T> entityClass, String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported for MongoDB");
    }
    
    @Override
    public int executeUpdate(String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported for MongoDB");
    }
    
    @Override
    public long executeInsert(String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported for MongoDB");
    }
    
    @Override
    public String getTableName(Class<?> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        return metadata.collectionName;
    }
    
    @Override
    public String getIdFieldName(Class<?> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        return metadata.idFieldName;
    }
    
    public <T> List<T> findWithLimit(Class<T> entityClass, int limit) {
        @SuppressWarnings("unchecked")
        MongoRepository<T, Object> repository = (MongoRepository<T, Object>) getRepository(entityClass);
        return repository.findWithLimit(limit);
    }
    
    public <T> List<T> findWithSkipAndLimit(Class<T> entityClass, int skip, int limit) {
        @SuppressWarnings("unchecked")
        MongoRepository<T, Object> repository = (MongoRepository<T, Object>) getRepository(entityClass);
        return repository.findWithSkipAndLimit(skip, limit);
    }
    
    public <T> long count(Class<T> entityClass, Map<String, Object> filter) {
        @SuppressWarnings("unchecked")
        MongoRepository<T, Object> repository = (MongoRepository<T, Object>) getRepository(entityClass);
        return repository.count(filter);
    }
    
    public <T> long deleteMany(Class<T> entityClass, Map<String, Object> filter) {
        @SuppressWarnings("unchecked")
        MongoRepository<T, Object> repository = (MongoRepository<T, Object>) getRepository(entityClass);
        return repository.deleteMany(filter);
    }
    
    @SuppressWarnings("unchecked")
    private <T> MongoRepository<T, Object> getRepository(Class<T> entityClass) {
        return (MongoRepository<T, Object>) repositoryCache.computeIfAbsent(entityClass, 
                clazz -> createRepository((Class<T>) clazz));
    }
    
    private <T> MongoRepository<T, Object> createRepository(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        
        MongoRepository.EntitySerializer<T> serializer = createSerializer(entityClass);
        MongoRepository.EntityIdExtractor<T, Object> idExtractor = entity -> {
            try {
                if (metadata.idField != null) {
                    metadata.idField.setAccessible(true);
                    return metadata.idField.get(entity);
                }
            } catch (Exception ignored) {
            }
            return null;
        };
        
        MongoRepository.EntityIdSetter<T, Object> idSetter = (entity, id) -> {
            try {
                if (metadata.idField != null) {
                    metadata.idField.setAccessible(true);
                    metadata.idField.set(entity, id);
                }
                return entity;
            } catch (Exception ignored) {
                return entity;
            }
        };
        
        return new MongoRepository<>(client, metadata.collectionName, serializer, idExtractor, idSetter, entityClass);
    }
    
    @SuppressWarnings("unchecked")
    private <T> MongoRepository.EntitySerializer<T> createSerializer(Class<T> entityClass) {
        return new MongoRepository.EntitySerializer<T>() {
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
            metadata.collectionName = entityAnnotation.tableName();
        } else {
            metadata.collectionName = entityClass.getSimpleName().toLowerCase();
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
        String collectionName;
        java.lang.reflect.Field idField;
        String idFieldName;
    }
    
    @Override
    public <T> LambdaQueryWrapper<T> lambdaQuery(Class<T> entityClass) {
        throw new UnsupportedOperationException("Lambda query not supported for MongoDB");
    }
    
    @Override
    public <T> LambdaUpdateWrapper<T> lambdaUpdate(Class<T> entityClass) {
        throw new UnsupportedOperationException("Lambda update not supported for MongoDB");
    }
    
    @Override
    public <T> List<T> saveBatch(List<T> entities) {
        throw new UnsupportedOperationException("Batch save not supported for MongoDB");
    }
    
    @Override
    public <T> List<T> updateBatchById(List<T> entities) {
        throw new UnsupportedOperationException("Batch update not supported for MongoDB");
    }
    
    @Override
    public <T> int updateBatchCaseWhen(List<T> entities) {
        throw new UnsupportedOperationException("Batch update not supported for MongoDB");
    }
    
    @Override
    public <T> int removeByIds(Class<T> entityClass, List<?> ids) {
        throw new UnsupportedOperationException("Batch delete not supported for MongoDB");
    }
    
    @Override
    public <T> Page<T> page(Class<T> entityClass, Page<T> page) {
        throw new UnsupportedOperationException("Pagination not supported for MongoDB");
    }
    
    @Override
    public <T> Page<T> page(Class<T> entityClass, Page<T> page, Query<T> query) {
        throw new UnsupportedOperationException("Pagination not supported for MongoDB");
    }
    
    @Override
    public <T> Page<T> page(Class<T> entityClass, Page<T> page, LambdaQueryWrapper<T> wrapper) {
        throw new UnsupportedOperationException("Pagination not supported for MongoDB");
    }
}
