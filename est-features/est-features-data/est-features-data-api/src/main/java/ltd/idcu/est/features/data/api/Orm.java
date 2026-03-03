package ltd.idcu.est.features.data.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Orm {
    
    <T> Optional<T> find(Class<T> entityClass, Object id);
    
    <T> List<T> findAll(Class<T> entityClass);
    
    <T> T save(T entity);
    
    <T> T update(T entity);
    
    <T> void delete(T entity);
    
    <T> int deleteById(Class<T> entityClass, Object id);
    
    <T> Query<T> query(Class<T> entityClass);
    
    <T> long count(Class<T> entityClass);
    
    <T> boolean exists(Class<T> entityClass, Object id);
    
    <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value);
    
    <T> Optional<T> findOneByField(Class<T> entityClass, String fieldName, Object value);
    
    <T> List<T> findByFields(Class<T> entityClass, Map<String, Object> fields);
    
    <T> List<T> executeQuery(Class<T> entityClass, String sql, Object... params);
    
    int executeUpdate(String sql, Object... params);
    
    long executeInsert(String sql, Object... params);
    
    String getTableName(Class<?> entityClass);
    
    String getIdFieldName(Class<?> entityClass);
}
