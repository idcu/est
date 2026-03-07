package ltd.idcu.est.features.data.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface Query<T> {
    
    Query<T> where(String field, Object value);
    
    Query<T> where(String field, String operator, Object value);
    
    Query<T> whereIn(String field, Iterable<?> values);
    
    Query<T> whereNotIn(String field, Iterable<?> values);
    
    Query<T> whereNull(String field);
    
    Query<T> whereNotNull(String field);
    
    Query<T> whereLike(String field, String pattern);
    
    Query<T> whereLikeLeft(String field, String pattern);
    
    Query<T> whereLikeRight(String field, String pattern);
    
    Query<T> whereNotLike(String field, String pattern);
    
    Query<T> whereNotLikeLeft(String field, String pattern);
    
    Query<T> whereNotLikeRight(String field, String pattern);
    
    Query<T> whereBetween(String field, Object start, Object end);
    
    Query<T> whereNotBetween(String field, Object start, Object end);
    
    Query<T> eq(String field, Object value);
    
    Query<T> ne(String field, Object value);
    
    Query<T> gt(String field, Object value);
    
    Query<T> ge(String field, Object value);
    
    Query<T> lt(String field, Object value);
    
    Query<T> le(String field, Object value);
    
    Query<T> and();
    
    Query<T> or();
    
    Query<T> and(Consumer<Query<T>> consumer);
    
    Query<T> or(Consumer<Query<T>> consumer);
    
    Query<T> join(String table, String leftField, String rightField);
    
    Query<T> join(String table, String leftField, String operator, String rightField);
    
    Query<T> leftJoin(String table, String leftField, String rightField);
    
    Query<T> leftJoin(String table, String leftField, String operator, String rightField);
    
    Query<T> rightJoin(String table, String leftField, String rightField);
    
    Query<T> rightJoin(String table, String leftField, String operator, String rightField);
    
    Query<T> innerJoin(String table, String leftField, String rightField);
    
    Query<T> innerJoin(String table, String leftField, String operator, String rightField);
    
    Query<T> orderBy(String field);
    
    Query<T> orderBy(String field, boolean ascending);
    
    Query<T> orderByAsc(String field);
    
    Query<T> orderByDesc(String field);
    
    Query<T> limit(int limit);
    
    Query<T> offset(int offset);
    
    Query<T> page(int pageNum, int pageSize);
    
    Query<T> select(String... fields);
    
    Query<T> distinct();
    
    Query<T> groupBy(String... fields);
    
    Query<T> having(String field, String operator, Object value);
    
    Query<T> last(String sql);
    
    Query<T> include(String... relations);
    
    List<T> get();
    
    Optional<T> first();
    
    T firstOrNull();
    
    T getOne();
    
    T getOne(boolean throwEx);
    
    Optional<T> find(Object id);
    
    long count();
    
    boolean exists();
    
    Object max(String field);
    
    Object min(String field);
    
    Object sum(String field);
    
    Object avg(String field);
    
    Page<T> page(Page<T> page);
    
    int update(Map<String, Object> values);
    
    int delete();
    
    String toSql();
    
    Map<String, Object> getBindings();
    
    Query<T> enableSqlLog();
    
    Query<T> disableSqlLog();
    
    Query<T> timeout(int seconds);
    
    Query<T> noTimeout();
    
    Query<T> whereIn(String field, Query<?> subQuery);
    
    Query<T> whereNotIn(String field, Query<?> subQuery);
    
    Query<T> exists(Query<?> subQuery);
    
    Query<T> notExists(Query<?> subQuery);
}
