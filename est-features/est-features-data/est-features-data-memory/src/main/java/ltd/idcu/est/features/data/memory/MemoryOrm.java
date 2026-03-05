package ltd.idcu.est.features.data.memory;

import ltd.idcu.est.features.data.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryOrm implements Orm {
    
    private final Map<Class<?>, MemoryRepository<?, ?>> repositories;
    private final Map<Class<?>, EntityMetadata> metadataCache;
    
    public MemoryOrm() {
        this.repositories = new ConcurrentHashMap<>();
        this.metadataCache = new ConcurrentHashMap<>();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> find(Class<T> entityClass, Object id) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return repository.findById(id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> entityClass) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return repository.findAll();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        MemoryRepository<T, Object> repository = (MemoryRepository<T, Object>) getRepository(entity.getClass());
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
        MemoryRepository<T, Object> repository = (MemoryRepository<T, Object>) getRepository(entity.getClass());
        repository.delete(entity);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> int deleteById(Class<T> entityClass, Object id) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            return 1;
        }
        return 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Query<T> query(Class<T> entityClass) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return new MemoryQuery<>(repository.findAll(), entityClass);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> long count(Class<T> entityClass) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return repository.count();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean exists(Class<T> entityClass, Object id) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return repository.existsById(id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return repository.findByPredicate(entity -> {
            Object fieldValue = getFieldValue(entity, fieldName);
            return Objects.equals(fieldValue, value);
        });
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> findOneByField(Class<T> entityClass, String fieldName, Object value) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return repository.findFirstByPredicate(entity -> {
            Object fieldValue = getFieldValue(entity, fieldName);
            return Objects.equals(fieldValue, value);
        });
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByFields(Class<T> entityClass, Map<String, Object> fields) {
        MemoryRepository<T, Object> repository = getRepository(entityClass);
        return repository.findByPredicate(entity -> {
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                Object fieldValue = getFieldValue(entity, entry.getKey());
                if (!Objects.equals(fieldValue, entry.getValue())) {
                    return false;
                }
            }
            return true;
        });
    }
    
    @Override
    public <T> List<T> executeQuery(Class<T> entityClass, String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported in memory ORM");
    }
    
    @Override
    public int executeUpdate(String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported in memory ORM");
    }
    
    @Override
    public long executeInsert(String sql, Object... params) {
        throw new UnsupportedOperationException("SQL execution not supported in memory ORM");
    }
    
    @Override
    public String getTableName(Class<?> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        return metadata.tableName;
    }
    
    @Override
    public String getIdFieldName(Class<?> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        return metadata.idFieldName;
    }
    
    @SuppressWarnings("unchecked")
    private <T> MemoryRepository<T, Object> getRepository(Class<T> entityClass) {
        return (MemoryRepository<T, Object>) repositories.computeIfAbsent(entityClass, 
                clazz -> createRepository((Class<T>) clazz));
    }
    
    private <T> MemoryRepository<T, Object> createRepository(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        
        MemoryRepository.IdGenerator<Object> idGenerator = () -> {
            if (metadata.idType == Long.class || metadata.idType == long.class) {
                return (Object) System.currentTimeMillis();
            } else if (metadata.idType == String.class) {
                return (Object) UUID.randomUUID().toString();
            } else if (metadata.idType == Integer.class || metadata.idType == int.class) {
                return (Object) (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
            }
            return null;
        };
        
        MemoryRepository.EntityIdExtractor<T, Object> idExtractor = entity -> {
            try {
                if (metadata.idField != null) {
                    metadata.idField.setAccessible(true);
                    return (Object) metadata.idField.get(entity);
                }
            } catch (Exception ignored) {
            }
            return null;
        };
        
        MemoryRepository.EntityIdSetter<T, Object> idSetter = (entity, id) -> {
            try {
                if (metadata.idField != null) {
                    metadata.idField.setAccessible(true);
                    metadata.idField.set(entity, id);
                }
            } catch (Exception ignored) {
            }
            return entity;
        };
        
        return new MemoryRepository<>(idGenerator, idExtractor, idSetter);
    }
    
    private EntityMetadata getEntityMetadata(Class<?> entityClass) {
        return metadataCache.computeIfAbsent(entityClass, this::extractMetadata);
    }
    
    private EntityMetadata extractMetadata(Class<?> entityClass) {
        EntityMetadata metadata = new EntityMetadata();
        
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        if (entityAnnotation != null && !entityAnnotation.tableName().isEmpty()) {
            metadata.tableName = entityAnnotation.tableName();
        } else {
            metadata.tableName = entityClass.getSimpleName().toLowerCase();
        }
        
        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                metadata.idField = field;
                metadata.idFieldName = field.getName();
                metadata.idType = field.getType();
                
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
                    metadata.idColumnName = columnAnnotation.name();
                } else {
                    metadata.idColumnName = field.getName();
                }
                break;
            }
        }
        
        return metadata;
    }
    
    private Object getFieldValue(Object entity, String fieldName) {
        try {
            java.lang.reflect.Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static class EntityMetadata {
        String tableName;
        java.lang.reflect.Field idField;
        String idFieldName;
        String idColumnName;
        Class<?> idType;
    }
    
    @Override
    public <T> LambdaQueryWrapper<T> lambdaQuery(Class<T> entityClass) {
        throw new UnsupportedOperationException("Lambda query not supported in memory ORM");
    }
    
    @Override
    public <T> LambdaUpdateWrapper<T> lambdaUpdate(Class<T> entityClass) {
        throw new UnsupportedOperationException("Lambda update not supported in memory ORM");
    }
    
    @Override
    public <T> List<T> saveBatch(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return entities;
        }
        
        List<T> result = new ArrayList<>();
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }
    
    @Override
    public <T> List<T> updateBatchById(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return entities;
        }
        
        List<T> result = new ArrayList<>();
        for (T entity : entities) {
            result.add(update(entity));
        }
        return result;
    }
    
    @Override
    public <T> int removeByIds(Class<T> entityClass, List<?> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        for (Object id : ids) {
            count += deleteById(entityClass, id);
        }
        return count;
    }
    
    @Override
    public <T> Page<T> page(Class<T> entityClass, Page<T> page) {
        return page(entityClass, page, query(entityClass));
    }
    
    @Override
    public <T> Page<T> page(Class<T> entityClass, Page<T> page, Query<T> query) {
        query.limit((int) page.getPageSize());
        query.offset((int) page.getOffset());
        
        long total = query.count();
        List<T> records = query.get();
        
        page.setTotal(total);
        page.setRecords(records);
        return page;
    }
    
    @Override
    public <T> Page<T> page(Class<T> entityClass, Page<T> page, LambdaQueryWrapper<T> wrapper) {
        throw new UnsupportedOperationException("Lambda page query not supported in memory ORM");
    }
}
