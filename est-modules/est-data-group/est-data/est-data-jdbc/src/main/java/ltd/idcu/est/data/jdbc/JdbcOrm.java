package ltd.idcu.est.data.jdbc;

import ltd.idcu.est.data.api.*;
import ltd.idcu.est.utils.common.AssertUtils;
import ltd.idcu.est.utils.common.ObjectUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcOrm implements Orm {
    
    private final ConnectionPool connectionPool;
    private final Map<Class<?>, EntityMetadata> metadataCache;
    private final Map<Class<?>, JdbcRepository<?, ?>> repositoryCache;
    
    private final ThreadLocal<Map<CacheKey, Object>> firstLevelCache;
    private final boolean cacheEnabled;
    
    public JdbcOrm(ConnectionPool connectionPool) {
        this(connectionPool, true);
    }
    
    public JdbcOrm(ConnectionPool connectionPool, boolean cacheEnabled) {
        this.connectionPool = connectionPool;
        this.metadataCache = new ConcurrentHashMap<>();
        this.repositoryCache = new ConcurrentHashMap<>();
        this.cacheEnabled = cacheEnabled;
        this.firstLevelCache = ThreadLocal.withInitial(HashMap::new);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> find(Class<T> entityClass, Object id) {
        if (cacheEnabled) {
            CacheKey cacheKey = new CacheKey(entityClass, id);
            Object cached = firstLevelCache.get().get(cacheKey);
            if (cached != null) {
                return Optional.of((T) cached);
            }
        }
        
        EntityMetadata metadata = getEntityMetadata(entityClass);
        String sql = buildFindByIdSql(metadata);
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EntityMapper<T> mapper = createMapper(entityClass, metadata);
                T entity = mapper.map(rs);
                if (cacheEnabled) {
                    CacheKey cacheKey = new CacheKey(entityClass, id);
                    firstLevelCache.get().put(cacheKey, entity);
                }
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataException("Failed to find entity by id: " + id, e);
        }
    }
    
    private String buildFindByIdSql(EntityMetadata metadata) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(metadata.tableName);
        sql.append(" WHERE ").append(metadata.idColumnName).append(" = ?");
        
        if (metadata.logicDeleteField != null) {
            sql.append(" AND ").append(metadata.logicDeleteColumnName)
               .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        return sql.toString();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(metadata.tableName);
        
        if (metadata.logicDeleteField != null) {
            sql.append(" WHERE ").append(metadata.logicDeleteColumnName)
               .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            EntityMapper<T> mapper = createMapper(entityClass, metadata);
            while (rs.next()) {
                T entity = mapper.map(rs);
                result.add(entity);
                if (cacheEnabled) {
                    Object id = getIdValue(entity, metadata);
                    if (id != null) {
                        CacheKey cacheKey = new CacheKey(entityClass, id);
                        firstLevelCache.get().put(cacheKey, entity);
                    }
                }
            }
            return result;
        } catch (SQLException e) {
            throw new DataException("Failed to find all entities", e);
        }
    }
    
    @Override
    public <T> T save(T entity) {
        AssertUtils.notNull(entity, "Entity cannot be null");
        
        EntityMetadata metadata = getEntityMetadata(entity.getClass());
        Object id = getIdValue(entity, metadata);
        
        if (ObjectUtils.isNull(id)) {
            return insert(entity, metadata);
        } else {
            return update(entity, metadata);
        }
    }
    
    @Override
    public <T> T update(T entity) {
        AssertUtils.notNull(entity, "Entity cannot be null");
        
        EntityMetadata metadata = getEntityMetadata(entity.getClass());
        return update(entity, metadata);
    }
    
    @Override
    public <T> void delete(T entity) {
        if (ObjectUtils.isNull(entity)) {
            return;
        }
        
        EntityMetadata metadata = getEntityMetadata(entity.getClass());
        Object id = getIdValue(entity, metadata);
        
        if (ObjectUtils.isNotNull(id)) {
            deleteById(entity.getClass(), id);
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> int deleteById(Class<T> entityClass, Object id) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        
        if (metadata.logicDeleteField != null) {
            return logicDeleteById(entityClass, id, metadata);
        }
        
        return physicalDeleteById(entityClass, id, metadata);
    }
    
    private <T> int logicDeleteById(Class<T> entityClass, Object id, EntityMetadata metadata) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(metadata.tableName);
        sql.append(" SET ").append(metadata.logicDeleteColumnName)
           .append(" = '").append(metadata.logicDeleteValue).append("'");
        sql.append(" WHERE ").append(metadata.idColumnName).append(" = ?");
        
        if (metadata.logicDeleteField != null) {
            sql.append(" AND ").append(metadata.logicDeleteColumnName)
               .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        if (metadata.versionField != null) {
            Optional<T> entityOpt = find(entityClass, id);
            if (entityOpt.isEmpty()) {
                return 0;
            }
            T entity = entityOpt.get();
            Object currentVersion = getFieldValue(entity, metadata.versionField);
            sql.append(" AND ").append(metadata.versionColumnName).append(" = ?");
            
            try (Connection conn = connectionPool.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                stmt.setObject(1, id);
                stmt.setObject(2, currentVersion);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0 && cacheEnabled) {
                    CacheKey cacheKey = new CacheKey(entityClass, id);
                    firstLevelCache.get().remove(cacheKey);
                }
                return affectedRows;
            } catch (SQLException e) {
                throw new DataException("Failed to logic delete entity by id: " + id, e);
            }
        }
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0 && cacheEnabled) {
                CacheKey cacheKey = new CacheKey(entityClass, id);
                firstLevelCache.get().remove(cacheKey);
            }
            return affectedRows;
        } catch (SQLException e) {
            throw new DataException("Failed to logic delete entity by id: " + id, e);
        }
    }
    
    private <T> int physicalDeleteById(Class<T> entityClass, Object id, EntityMetadata metadata) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(metadata.tableName);
        sql.append(" WHERE ").append(metadata.idColumnName).append(" = ?");
        
        if (metadata.logicDeleteField != null) {
            sql.append(" AND ").append(metadata.logicDeleteColumnName)
               .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0 && cacheEnabled) {
                CacheKey cacheKey = new CacheKey(entityClass, id);
                firstLevelCache.get().remove(cacheKey);
            }
            return affectedRows;
        } catch (SQLException e) {
            throw new DataException("Failed to delete entity by id: " + id, e);
        }
    }
    
    @Override
    public void clearCache() {
        firstLevelCache.get().clear();
    }
    
    @Override
    public <T> void evictCache(Class<T> entityClass, Object id) {
        CacheKey cacheKey = new CacheKey(entityClass, id);
        firstLevelCache.get().remove(cacheKey);
    }
    
    @Override
    public <T> int deleteAll(Class<T> entityClass) {
        EntityMetadata metadata = getEntityMetadata(entityClass);
        
        if (metadata.logicDeleteField != null) {
            return logicDeleteAll(entityClass, metadata);
        }
        
        return physicalDeleteAll(entityClass, metadata);
    }
    
    private <T> int logicDeleteAll(Class<T> entityClass, EntityMetadata metadata) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(metadata.tableName);
        sql.append(" SET ").append(metadata.logicDeleteColumnName)
           .append(" = '").append(metadata.logicDeleteValue).append("'");
        
        if (metadata.logicDeleteField != null) {
            sql.append(" WHERE ").append(metadata.logicDeleteColumnName)
               .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0 && cacheEnabled) {
                firstLevelCache.get().clear();
            }
            return affectedRows;
        } catch (SQLException e) {
            throw new DataException("Failed to logic delete all entities", e);
        }
    }
    
    private <T> int physicalDeleteAll(Class<T> entityClass, EntityMetadata metadata) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(metadata.tableName);
        
        if (metadata.logicDeleteField != null) {
            sql.append(" WHERE ").append(metadata.logicDeleteColumnName)
               .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0 && cacheEnabled) {
                firstLevelCache.get().clear();
            }
            return affectedRows;
        } catch (SQLException e) {
            throw new DataException("Failed to delete all entities", e);
        }
    }
    
    @Override
    public <T> LambdaUpdateWrapper<T> update(Class<T> entityClass) {
        return lambdaUpdate(entityClass);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Repository<T, ?> getRepository(Class<T> entityClass) {
        return getJdbcRepository(entityClass);
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public <T> Optional<T> findOneByFields(Class<T> entityClass, Map<String, Object> fields) {
        List<T> results = findByFields(entityClass, fields);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
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
        JdbcRepository<T, Object> repository = getJdbcRepository(entityClass);
        return repository.count();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean exists(Class<T> entityClass, Object id) {
        JdbcRepository<T, Object> repository = getJdbcRepository(entityClass);
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
    private <T> JdbcRepository<T, Object> getJdbcRepository(Class<T> entityClass) {
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
        fillAuditFieldsForInsert(entity, metadata);
        
        if (metadata.versionField != null) {
            setFieldValue(entity, metadata.versionField, 1);
        }
        
        if (metadata.logicDeleteField != null) {
            setFieldValue(entity, metadata.logicDeleteField, metadata.logicDeleteNotDeletedValue);
        }
        
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
            
            if (cacheEnabled) {
                Object id = getIdValue(entity, metadata);
                if (id != null) {
                    CacheKey cacheKey = new CacheKey(entity.getClass(), id);
                    firstLevelCache.get().put(cacheKey, entity);
                }
            }
            
            return entity;
        } catch (SQLException e) {
            throw new DataException("Failed to insert entity", e);
        }
    }
    
    private <T> T update(T entity, EntityMetadata metadata) {
        fillAuditFieldsForUpdate(entity, metadata);
        
        List<String> setClauses = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        Object currentVersion = null;
        Object newVersion = null;
        
        if (metadata.versionField != null) {
            currentVersion = getFieldValue(entity, metadata.versionField);
            if (currentVersion == null) {
                currentVersion = 0;
            }
            newVersion = (currentVersion instanceof Integer ? 
                    (Integer) currentVersion + 1 : (Long) currentVersion + 1);
        }
        
        for (ColumnMetadata column : metadata.columns) {
            if (!column.isId) {
                if (metadata.versionField != null && column.field.equals(metadata.versionField)) {
                    setClauses.add(column.columnName + " = ?");
                    values.add(newVersion);
                } else {
                    setClauses.add(column.columnName + " = ?");
                    values.add(getFieldValue(entity, column.field));
                }
            }
        }
        
        Object id = getIdValue(entity, metadata);
        
        StringBuilder sql = new StringBuilder("UPDATE " + metadata.tableName + 
                " SET " + String.join(", ", setClauses) + 
                " WHERE " + metadata.idColumnName + " = ?");
        
        if (metadata.versionField != null) {
            sql.append(" AND ").append(metadata.versionColumnName).append(" = ?");
            values.add(currentVersion);
        }
        
        if (metadata.logicDeleteField != null) {
            sql.append(" AND ").append(metadata.logicDeleteColumnName)
               .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            for (Object value : values) {
                stmt.setObject(paramIndex++, value);
            }
            stmt.setObject(paramIndex, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                if (metadata.versionField != null) {
                    throw new OptimisticLockingException(
                        "Optimistic locking failed. Entity may have been modified or deleted: " + 
                        entity.getClass().getSimpleName() + " with id: " + id);
                }
                throw new DataException("Failed to update entity, no rows affected");
            }
            
            if (metadata.versionField != null) {
                setFieldValue(entity, metadata.versionField, newVersion);
            }
            
            if (cacheEnabled) {
                CacheKey cacheKey = new CacheKey(entity.getClass(), id);
                firstLevelCache.get().put(cacheKey, entity);
            }
            
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
                field.isAnnotationPresent(ManyToOne.class) ||
                field.isAnnotationPresent(ManyToMany.class)) {
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
                } else if (field.isAnnotationPresent(ManyToMany.class)) {
                    ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
                    relation.type = "ManyToMany";
                    relation.joinTable = manyToMany.joinTable();
                    relation.joinColumn = manyToMany.joinColumn();
                    relation.inverseJoinColumn = manyToMany.inverseJoinColumn();
                    relation.mappedBy = manyToMany.mappedBy();
                    relation.lazy = manyToMany.lazy();
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
            
            if (field.isAnnotationPresent(Version.class)) {
                metadata.versionField = field;
                metadata.versionColumnName = column.columnName;
            }
            
            if (field.isAnnotationPresent(LogicDelete.class)) {
                LogicDelete logicDelete = field.getAnnotation(LogicDelete.class);
                metadata.logicDeleteField = field;
                metadata.logicDeleteColumnName = column.columnName;
                metadata.logicDeleteValue = logicDelete.value();
                metadata.logicDeleteNotDeletedValue = logicDelete.notDeletedValue();
            }
            
            if (field.isAnnotationPresent(CreatedBy.class)) {
                metadata.createdByField = field;
            }
            if (field.isAnnotationPresent(CreatedDate.class)) {
                metadata.createdDateField = field;
            }
            if (field.isAnnotationPresent(CreatedAt.class)) {
                metadata.createdAtField = field;
            }
            if (field.isAnnotationPresent(UpdatedBy.class)) {
                metadata.updatedByField = field;
            }
            if (field.isAnnotationPresent(LastModifiedDate.class)) {
                metadata.lastModifiedDateField = field;
            }
            if (field.isAnnotationPresent(UpdatedAt.class)) {
                metadata.updatedAtField = field;
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


        };
    }
    
    private <T> void fillAuditFieldsForInsert(T entity, EntityMetadata metadata) {
        String currentUser = AuditContext.getCurrentUser();
        java.util.Date now = new java.util.Date();
        
        if (metadata.createdByField != null && currentUser != null) {
            setFieldValue(entity, metadata.createdByField, currentUser);
        }
        if (metadata.createdDateField != null) {
            setFieldValue(entity, metadata.createdDateField, now);
        }
        if (metadata.createdAtField != null) {
            setFieldValue(entity, metadata.createdAtField, now);
        }
        if (metadata.updatedByField != null && currentUser != null) {
            setFieldValue(entity, metadata.updatedByField, currentUser);
        }
        if (metadata.lastModifiedDateField != null) {
            setFieldValue(entity, metadata.lastModifiedDateField, now);
        }
        if (metadata.updatedAtField != null) {
            setFieldValue(entity, metadata.updatedAtField, now);
        }
    }
    
    private <T> void fillAuditFieldsForUpdate(T entity, EntityMetadata metadata) {
        String currentUser = AuditContext.getCurrentUser();
        java.util.Date now = new java.util.Date();
        
        if (metadata.updatedByField != null && currentUser != null) {
            setFieldValue(entity, metadata.updatedByField, currentUser);
        }
        if (metadata.lastModifiedDateField != null) {
            setFieldValue(entity, metadata.lastModifiedDateField, now);
        }
        if (metadata.updatedAtField != null) {
            setFieldValue(entity, metadata.updatedAtField, now);
        }
    }
    
    private void setFieldValue(Object entity, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception ignored) {
        }
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
        Map<Class<?>, List<T>> groupedEntities = new HashMap<>();
        
        for (T entity : entities) {
            groupedEntities.computeIfAbsent(entity.getClass(), k -> new ArrayList<>()).add(entity);
        }
        
        for (Map.Entry<Class<?>, List<T>> entry : groupedEntities.entrySet()) {
            @SuppressWarnings("unchecked")
            Class<T> entityClass = (Class<T>) entry.getKey();
            List<T> group = entry.getValue();
            
            List<T> inserts = new ArrayList<>();
            List<T> updates = new ArrayList<>();
            EntityMetadata metadata = getEntityMetadata(entityClass);
            
            for (T entity : group) {
                Object id = getIdValue(entity, metadata);
                if (ObjectUtils.isNull(id)) {
                    inserts.add(entity);
                } else {
                    updates.add(entity);
                }
            }
            
            if (!inserts.isEmpty()) {
                result.addAll(batchInsert(inserts, metadata));
            }
            
            if (!updates.isEmpty()) {
                result.addAll(batchUpdate(updates, metadata));
            }
        }
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private <T> List<T> batchInsert(List<T> entities, EntityMetadata metadata) {
        if (entities.isEmpty()) {
            return entities;
        }
        
        for (T entity : entities) {
            fillAuditFieldsForInsert(entity, metadata);
            if (metadata.versionField != null) {
                setFieldValue(entity, metadata.versionField, 1);
            }
            if (metadata.logicDeleteField != null) {
                setFieldValue(entity, metadata.logicDeleteField, metadata.logicDeleteNotDeletedValue);
            }
        }
        
        List<String> columns = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        
        for (ColumnMetadata column : metadata.columns) {
            if (!column.isId || !column.autoGenerated) {
                columns.add(column.columnName);
                placeholders.add("?");
            }
        }
        
        String sql = "INSERT INTO " + metadata.tableName + 
                " (" + String.join(", ", columns) + ") VALUES (" + 
                String.join(", ", placeholders) + ")";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            for (T entity : entities) {
                int paramIndex = 1;
                for (ColumnMetadata column : metadata.columns) {
                    if (!column.isId || !column.autoGenerated) {
                        stmt.setObject(paramIndex++, getFieldValue(entity, column.field));
                    }
                }
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            
            if (metadata.idField != null && metadata.idAutoGenerated) {
                ResultSet rs = stmt.getGeneratedKeys();
                int index = 0;
                while (rs.next() && index < entities.size()) {
                    T entity = entities.get(index++);
                    setIdValue(entity, metadata, rs.getObject(1));
                }
            }
            
            if (cacheEnabled) {
                for (T entity : entities) {
                    Object id = getIdValue(entity, metadata);
                    if (id != null) {
                        CacheKey cacheKey = new CacheKey(entity.getClass(), id);
                        firstLevelCache.get().put(cacheKey, entity);
                    }
                }
            }
            
            return entities;
        } catch (SQLException e) {
            throw new DataException("Failed to batch insert entities", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> List<T> batchUpdate(List<T> entities, EntityMetadata metadata) {
        if (entities.isEmpty()) {
            return entities;
        }
        
        for (T entity : entities) {
            fillAuditFieldsForUpdate(entity, metadata);
        }
        
        List<String> setClauses = new ArrayList<>();
        
        for (ColumnMetadata column : metadata.columns) {
            if (!column.isId) {
                setClauses.add(column.columnName + " = ?");
            }
        }
        
        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + metadata.tableName + 
                " SET " + String.join(", ", setClauses) + 
                " WHERE " + metadata.idColumnName + " = ?");
        
        if (metadata.versionField != null) {
            sqlBuilder.append(" AND ").append(metadata.versionColumnName).append(" = ?");
        }
        
        if (metadata.logicDeleteField != null) {
            sqlBuilder.append(" AND ").append(metadata.logicDeleteColumnName)
                     .append(" = '").append(metadata.logicDeleteNotDeletedValue).append("'");
        }
        
        String sql = sqlBuilder.toString();
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            List<Object[]> versionPairs = new ArrayList<>();
            
            for (T entity : entities) {
                int paramIndex = 1;
                
                Object newVersion = null;
                Object currentVersion = null;
                
                if (metadata.versionField != null) {
                    currentVersion = getFieldValue(entity, metadata.versionField);
                    if (currentVersion == null) {
                        currentVersion = 0;
                    }
                    newVersion = (currentVersion instanceof Integer ? 
                            (Integer) currentVersion + 1 : (Long) currentVersion + 1);
                    versionPairs.add(new Object[]{currentVersion, newVersion});
                }
                
                for (ColumnMetadata column : metadata.columns) {
                    if (!column.isId) {
                        if (metadata.versionField != null && column.field.equals(metadata.versionField)) {
                            stmt.setObject(paramIndex++, newVersion);
                        } else {
                            stmt.setObject(paramIndex++, getFieldValue(entity, column.field));
                        }
                    }
                }
                
                Object id = getIdValue(entity, metadata);
                stmt.setObject(paramIndex++, id);
                
                if (metadata.versionField != null) {
                    stmt.setObject(paramIndex++, currentVersion);
                }
                
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            
            int versionIndex = 0;
            for (int i = 0; i < results.length; i++) {
                if (results[i] == 0) {
                    T entity = entities.get(i);
                    Object id = getIdValue(entity, metadata);
                    if (metadata.versionField != null) {
                        throw new OptimisticLockingException(
                            "Optimistic locking failed during batch update. Entity may have been modified or deleted: " + 
                            entity.getClass().getSimpleName() + " with id: " + id);
                    }
                    throw new DataException("Failed to update entity during batch, no rows affected");
                }
                
                if (metadata.versionField != null && versionIndex < versionPairs.size()) {
                    T entity = entities.get(i);
                    Object[] pair = versionPairs.get(versionIndex++);
                    setFieldValue(entity, metadata.versionField, pair[1]);
                }
            }
            
            if (cacheEnabled) {
                for (T entity : entities) {
                    Object id = getIdValue(entity, metadata);
                    if (id != null) {
                        CacheKey cacheKey = new CacheKey(entity.getClass(), id);
                        firstLevelCache.get().put(cacheKey, entity);
                    }
                }
            }
            
            return entities;
        } catch (SQLException e) {
            throw new DataException("Failed to batch update entities", e);
        }
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
    public <T> int updateBatchCaseWhen(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        
        Class<T> entityClass = (Class<T>) entities.get(0).getClass();
        EntityMetadata metadata = getEntityMetadata(entityClass);
        
        if (metadata.idField == null) {
            throw new DataException("Entity must have an @Id field for batch update");
        }
        
        List<ColumnMetadata> updatableColumns = new ArrayList<>();
        for (ColumnMetadata column : metadata.columns) {
            if (!column.isId) {
                updatableColumns.add(column);
            }
        }
        
        if (updatableColumns.isEmpty()) {
            return 0;
        }
        
        for (T entity : entities) {
            fillAuditFieldsForUpdate(entity, metadata);
        }
        
        StringBuilder sql = new StringBuilder("UPDATE ").append(metadata.tableName).append(" SET ");
        
        List<String> setClauses = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        
        for (ColumnMetadata column : updatableColumns) {
            StringBuilder caseWhen = new StringBuilder(column.columnName).append(" = CASE ");
            for (T entity : entities) {
                Object id = getIdValue(entity, metadata);
                Object value = getFieldValue(entity, column.field);
                caseWhen.append("WHEN ").append(metadata.idColumnName).append(" = ? THEN ? ");
                params.add(id);
                params.add(value);
            }
            caseWhen.append("ELSE ").append(column.columnName).append(" END");
            setClauses.add(caseWhen.toString());
        }
        
        sql.append(String.join(", ", setClauses));
        
        sql.append(" WHERE ").append(metadata.idColumnName).append(" IN (");
        sql.append(String.join(", ", Collections.nCopies(entities.size(), "?")));
        sql.append(")");
        
        for (T entity : entities) {
            params.add(getIdValue(entity, metadata));
        }
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Failed to execute batch update", e);
        }
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
        Field createdByField;
        Field createdDateField;
        Field createdAtField;
        Field updatedByField;
        Field lastModifiedDateField;
        Field updatedAtField;
        Field versionField;
        String versionColumnName;
        Field logicDeleteField;
        String logicDeleteColumnName;
        String logicDeleteValue;
        String logicDeleteNotDeletedValue;
    }
    
    private static class CacheKey {
        private final Class<?> entityClass;
        private final Object id;
        
        CacheKey(Class<?> entityClass, Object id) {
            this.entityClass = entityClass;
            this.id = id;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(entityClass, cacheKey.entityClass) && 
                   Objects.equals(id, cacheKey.id);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(entityClass, id);
        }
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
        String inverseJoinColumn;
        String joinTable;
        String mappedBy;
        boolean lazy;
    }
}
