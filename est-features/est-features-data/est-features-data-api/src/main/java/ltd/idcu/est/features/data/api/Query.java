package ltd.idcu.est.features.data.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Query<T> {
    
    Query<T> where(String field, Object value);
    
    Query<T> where(String field, String operator, Object value);
    
    Query<T> whereIn(String field, Iterable<?> values);
    
    Query<T> whereNotIn(String field, Iterable<?> values);
    
    Query<T> whereNull(String field);
    
    Query<T> whereNotNull(String field);
    
    Query<T> whereLike(String field, String pattern);
    
    Query<T> whereBetween(String field, Object start, Object end);
    
    Query<T> and();
    
    Query<T> or();
    
    Query<T> orderBy(String field);
    
    Query<T> orderBy(String field, boolean ascending);
    
    Query<T> limit(int limit);
    
    Query<T> offset(int offset);
    
    Query<T> select(String... fields);
    
    Query<T> distinct();
    
    Query<T> groupBy(String... fields);
    
    Query<T> having(String field, String operator, Object value);
    
    List<T> get();
    
    Optional<T> first();
    
    T firstOrNull();
    
    Optional<T> find(Object id);
    
    long count();
    
    boolean exists();
    
    Object max(String field);
    
    Object min(String field);
    
    Object sum(String field);
    
    Object avg(String field);
    
    int update(Map<String, Object> values);
    
    int delete();
    
    String toSql();
    
    Map<String, Object> getBindings();
}
