package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcOrm implements Orm {
    
    private final ConnectionPool connectionPool;
    private final Map<Class<?>, EntityMetadata> metadataCache;
    private final Map<Class<?>, JdbcRepository<?, ?>> repositoryCache;
    
    public JdbcOrm(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.metadataCache = new ConcurrentHashMap<>();
        this.repositoryCache = new ConcurrentHashMap<>();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> find(Class<T> entityClass, Object id) {
        JdbcRepository<T, Object> repository = getRepository(entityClass);
        return repository.findById(id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> entityClass) {
        JdbcRepository<T, Object> repository = getRepository(entityClass);
        return repository.findAll();
    }
    
    @Override
    public <T> T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        EntityMetadata metadata = getEntityMetadata(entity.getClass());
        Object id = getIdValue(entity, metadata);
        
        if (id == null) {
            return insert(entity, metadata);
        } else {
            return update(entity, metadata);
        }
    }
    
    @Override
    public <T> T update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        EntityMetadata metadata = getEntityMetadata(entity.getClass());
        return update(entity, metadata);
    }
    
    @Override
    public <T> void delete(T entity) {
        if (entity == null) {
            return;
        }
        
        EntityMetadata metadata = getEntityMetadata(entity.getClass());
        Object id = getIdValue(entity, metadata);
        
        if (id != null) {
            deleteById(entity.getClass(), id);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> int deleteById(Class<T> entityClass, Object id) {
        JdbcRepository<T, Object> repository = getRepository(entityClass);
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
        EntityMetadata metadata = getEntityMetadata(entityClass);
        try {
            Connection conn = connectionPool.getConnection();
            return new JdbcQuery<>(conn, entityClass, createMapper(entityClass, metadata), metadata.tableName);
        } catch (Exception e) {
            throw new DataException("Failed to create query", e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> long count(Class<T> entityClass) {
        JdbcRepository<T, Object> repository = getRepository(entityClass);
        return repository.count();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean exists(Class<T> entityClass, Object id) {
        JdbcRepository<T, Object> repository = getRepository(entityClass);
        return repository.existsById(id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        String columnName = getColumnName(metadata, fieldName);
        String sql = "SELECT * FROM " + metadata.tableName + " WHERE " + columnName + " = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            EntityMapper<T> mapper = createMapper(entityClass, metadata);
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to find by field: " + fieldName, e);
        }
    }
    
    @Override
    public <T> Optional<T> findOneByField(Class<T> entityClass, String fieldName, Object value) {
        List<T> results = findByField(entityClass, fieldName, value);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public <T> List<T> findByFields(Class<T> entityClass, Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            return findAll(entityClass);
        }
        
        EntityMetadata metadata = getEntityMetadata(entityClass);
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(metadata.tableName).append(" WHERE ");
        
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String columnName = getColumnName(metadata, entry.getKey());
            conditions.add(columnName + " = ?");
            params.add(entry.getValue());
        }
        
        sql.append(String.join(" AND ", conditions));
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            EntityMapper<T> mapper = createMapper(entityClass, metadata);
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to find by fields", e);
        }
    }
    
    @Override
    public <T> List<T> executeQuery(Class<T> entityClass, String sql, Object... params) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            EntityMapper<T> mapper = createMapper(entityClass, metadata);
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to execute query: " + sql, e);
        }
    }
    
    @Override
    public int executeUpdate(String sql, Object... params) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to execute update: " + sql, e);
        }
    }
    
    @Override
    public long executeInsert(String sql, Object... params) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new DataException("Failed to execute insert: " + sql, e);
        }
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
    private <T> JdbcRepository<T, Object> getRepository(Class<T> entityClass) {
        return (JdbcRepository<T, Object>) repositoryCache.computeIfAbsent(entityClass, 
                clazz -> createRepository((Class<T>) clazz));
    }
    
    private <T> JdbcRepository<T, Object> createRepository(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        EntityMapper<T> mapper = createMapper(entityClass, metadata);
        return new JdbcRepository<>(connectionPool, entityClass, mapper, 
                metadata.tableName, metadata.idColumnName);
    }
    
    private <T> T insert(T entity, EntityMetadata metadata) {
        List<String> columns = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        for (ColumnMetadata column : metadata.columns) {
            if (!column.isId || !column.autoGenerated) {
                columns.add(column.columnName);
                placeholders.add("?");
                values.add(getFieldValue(entity, column.field));
            }
        }
        
        String sql = "INSERT INTO " + metadata.tableName + 
                " (" + String.join(", ", columns) + ") VALUES (" + 
                String.join(", ", placeholders) + ")";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.executeUpdate();
            
            if (metadata.idField != null && metadata.idAutoGenerated) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    setIdValue(entity, metadata, rs.getObject(1));
                }
            }
            
            return entity;
        } catch (SQLException e) {
            throw new DataException("Failed to insert entity", e);
        }
    }
    
    private <T> T update(T entity, EntityMetadata metadata) {
        List<String> setClauses = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        for (ColumnMetadata column : metadata.columns) {
            if (!column.isId) {
                setClauses.add(column.columnName + " = ?");
                values.add(getFieldValue(entity, column.field));
            }
        }
        
        Object id = getIdValue(entity, metadata);
        values.add(id);
        
        String sql = "UPDATE " + metadata.tableName + 
                " SET " + String.join(", ", setClauses) + 
                " WHERE " + metadata.idColumnName + " = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new DataException("Failed to update entity", e);
        }
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
            metadata.tableName = camelToSnake(entityClass.getSimpleName());
        }
        
        metadata.columns = new ArrayList<>();
        metadata.relations = new ArrayList<>();
        
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            
            if (field.isAnnotationPresent(OneToOne.class) || 
                field.isAnnotationPresent(OneToMany.class) || 
                field.isAnnotationPresent(ManyToOne.class)) {
                RelationMetadata relation = new RelationMetadata();
                relation.field = field;
                relation.fieldName = field.getName();
                
                if (field.isAnnotationPresent(OneToOne.class)) {
                    OneToOne oneToOne = field.getAnnotation(OneToOne.class);
                    relation.type = "OneToOne";
                    relation.joinColumn = oneToOne.joinColumn();
                    relation.mappedBy = oneToOne.mappedBy();
                    relation.lazy = oneToOne.lazy();
                } else if (field.isAnnotationPresent(OneToMany.class)) {
                    OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                    relation.type = "OneToMany";
                    relation.mappedBy = oneToMany.mappedBy();
                    relation.lazy = oneToMany.lazy();
                } else if (field.isAnnotationPresent(ManyToOne.class)) {
                    ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                    relation.type = "ManyToOne";
                    relation.joinColumn = manyToOne.joinColumn();
                    relation.lazy = manyToOne.lazy();
                }
                
                metadata.relations.add(relation);
                continue;
            }
            
            ColumnMetadata column = new ColumnMetadata();
            column.field = field;
            column.fieldName = field.getName();
            
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
                column.columnName = columnAnnotation.name();
            } else {
                column.columnName = camelToSnake(field.getName());
            }
            
            if (field.isAnnotationPresent(Id.class)) {
                column.isId = true;
                metadata.idField = field;
                metadata.idFieldName = field.getName();
                metadata.idColumnName = column.columnName;
                Id idAnnotation = field.getAnnotation(Id.class);
                metadata.idAutoGenerated = idAnnotation.autoGenerated();
            }
            
            metadata.columns.add(column);
        }
        
        return metadata;
    }
    
    private <T> EntityMapper<T> createMapper(Class<T> entityClass, EntityMetadata metadata) {
        return new EntityMapper<T>() {
            @Override
            public T map(ResultSet rs) throws SQLException {
                try {
                    T entity = entityClass.getDeclaredConstructor().newInstance();
                    for (ColumnMetadata column : metadata.columns) {
                        Object value = rs.getObject(column.columnName);
                        if (value != null) {
                            column.field.setAccessible(true);
                            column.field.set(entity, value);
                        }
                    }
                    return entity;
                } catch (Exception e) {
                    throw new DataException("Failed to map entity", e);
                }
            }

            @Override
            public T map(ResultSet rs, int rowNum) throws SQLException {
                return map(rs);
            }
        };
    }
    
    private Object getIdValue(Object entity, EntityMetadata metadata) {
        try {
            if (metadata.idField != null) {
                metadata.idField.setAccessible(true);
                return metadata.idField.get(entity);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
    
    private void setIdValue(Object entity, EntityMetadata metadata, Object id) {
        try {
            if (metadata.idField != null) {
                metadata.idField.setAccessible(true);
                metadata.idField.set(entity, id);
            }
        } catch (Exception ignored) {
        }
    }
    
    private Object getFieldValue(Object entity, Field field) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            return null;
        }
    }
    
    private String getColumnName(EntityMetadata metadata, String fieldName) {
        for (ColumnMetadata column : metadata.columns) {
            if (column.fieldName.equals(fieldName)) {
                return column.columnName;
            }
        }
        return camelToSnake(fieldName);
    }
    
    private String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> LambdaQueryWrapper<T> lambdaQuery(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        try {
            Connection conn = connectionPool.getConnection();
            return new JdbcLambdaQueryWrapper<>(conn, entityClass, createMapper(entityClass, metadata), metadata.tableName);
        } catch (Exception e) {
            throw new DataException("Failed to create lambda query wrapper", e);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> LambdaUpdateWrapper<T> lambdaUpdate(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        try {
            Connection conn = connectionPool.getConnection();
            return new JdbcLambdaUpdateWrapper<>(conn, metadata.tableName);
        } catch (Exception e) {
            throw new DataException("Failed to create lambda update wrapper", e);
        }
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public <T> Page<T> page(Class<T> entityClass, Page<T> page) {
        Query<T> query = query(entityClass);
        return page(entityClass, page, query);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> page(Class<T> entityClass, Page<T> page, Query<T> query) {
        query.offset((int) page.getOffset());
        query.limit((int) page.getPageSize());
        List<T> records = query.get();
        long total = query(entityClass).count();
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> page(Class<T> entityClass, Page<T> page, LambdaQueryWrapper<T> wrapper) {
        wrapper.offset((int) page.getOffset());
        wrapper.limit((int) page.getPageSize());
        List<T> records = wrapper.get();
        long total = wrapper.count();
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }
    
    private static class EntityMetadata {
        String tableName;
        Field idField;
        String idFieldName;
        String idColumnName;
        boolean idAutoGenerated;
        List<ColumnMetadata> columns;
        List<RelationMetadata> relations;
    }
    
    private static class ColumnMetadata {
        Field field;
        String fieldName;
        String columnName;
        boolean isId;
        boolean autoGenerated;
    }
    
    private static class RelationMetadata {
        Field field;
        String fieldName;
        String type;
        String joinColumn;
        String mappedBy;
        boolean lazy;
    }
}
