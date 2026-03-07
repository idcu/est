package ltd.idcu.est.features.data.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface LambdaQueryWrapper<T> extends Serializable {

    <V> LambdaQueryWrapper<T> eq(SFunction<T, V> column, V value);

    <V> LambdaQueryWrapper<T> ne(SFunction<T, V> column, V value);

    <V> LambdaQueryWrapper<T> gt(SFunction<T, V> column, V value);

    <V> LambdaQueryWrapper<T> ge(SFunction<T, V> column, V value);

    <V> LambdaQueryWrapper<T> lt(SFunction<T, V> column, V value);

    <V> LambdaQueryWrapper<T> le(SFunction<T, V> column, V value);

    LambdaQueryWrapper<T> like(SFunction<T, ?> column, String value);

    LambdaQueryWrapper<T> likeLeft(SFunction<T, ?> column, String value);

    LambdaQueryWrapper<T> likeRight(SFunction<T, ?> column, String value);

    LambdaQueryWrapper<T> notLike(SFunction<T, ?> column, String value);

    LambdaQueryWrapper<T> notLikeLeft(SFunction<T, ?> column, String value);

    LambdaQueryWrapper<T> notLikeRight(SFunction<T, ?> column, String value);

    LambdaQueryWrapper<T> in(SFunction<T, ?> column, Iterable<?> values);

    LambdaQueryWrapper<T> notIn(SFunction<T, ?> column, Iterable<?> values);

    LambdaQueryWrapper<T> between(SFunction<T, ?> column, Object start, Object end);

    LambdaQueryWrapper<T> notBetween(SFunction<T, ?> column, Object start, Object end);

    LambdaQueryWrapper<T> isNull(SFunction<T, ?> column);

    LambdaQueryWrapper<T> isNotNull(SFunction<T, ?> column);

    LambdaQueryWrapper<T> and();

    LambdaQueryWrapper<T> or();

    LambdaQueryWrapper<T> and(Consumer<LambdaQueryWrapper<T>> consumer);

    LambdaQueryWrapper<T> or(Consumer<LambdaQueryWrapper<T>> consumer);

    LambdaQueryWrapper<T> orderBy(SFunction<T, ?> column);

    LambdaQueryWrapper<T> orderBy(SFunction<T, ?> column, boolean ascending);

    LambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> column);

    LambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column);

    LambdaQueryWrapper<T> select(SFunction<T, ?>... columns);

    LambdaQueryWrapper<T> distinct();

    LambdaQueryWrapper<T> groupBy(SFunction<T, ?>... columns);

    LambdaQueryWrapper<T> last(String lastSql);
    
    LambdaQueryWrapper<T> include(SFunction<T, ?>... relations);
    
    LambdaQueryWrapper<T> join(String table, String leftField, String rightField);
    
    LambdaQueryWrapper<T> join(String table, String leftField, String operator, String rightField);
    
    LambdaQueryWrapper<T> leftJoin(String table, String leftField, String rightField);
    
    LambdaQueryWrapper<T> leftJoin(String table, String leftField, String operator, String rightField);
    
    LambdaQueryWrapper<T> rightJoin(String table, String leftField, String rightField);
    
    LambdaQueryWrapper<T> rightJoin(String table, String leftField, String operator, String rightField);
    
    LambdaQueryWrapper<T> innerJoin(String table, String leftField, String rightField);
    
    LambdaQueryWrapper<T> innerJoin(String table, String leftField, String operator, String rightField);
    
    LambdaQueryWrapper<T> limit(int limit);

    LambdaQueryWrapper<T> offset(int offset);

    LambdaQueryWrapper<T> page(int pageNum, int pageSize);

    List<T> get();

    T getOne();

    T getOne(boolean throwEx);

    long count();

    boolean exists();

    Object max(SFunction<T, ?> column);

    Object min(SFunction<T, ?> column);

    Object sum(SFunction<T, ?> column);

    Object avg(SFunction<T, ?> column);

    String toSql();
    
    Map<String, Object> getBindings();
    
    <V> LambdaQueryWrapper<T> in(SFunction<T, V> column, Query<?> subQuery);
    
    <V> LambdaQueryWrapper<T> notIn(SFunction<T, V> column, Query<?> subQuery);
    
    LambdaQueryWrapper<T> exists(Query<?> subQuery);
    
    LambdaQueryWrapper<T> notExists(Query<?> subQuery);
}
