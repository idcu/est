package ltd.idcu.est.data.api;

import java.util.Map;

public interface LambdaUpdateWrapper<T> {

    <V> LambdaUpdateWrapper<T> set(SFunction<T, V> column, V value);

    LambdaUpdateWrapper<T> setSql(String sql);

    <V> LambdaUpdateWrapper<T> eq(SFunction<T, V> column, V value);

    <V> LambdaUpdateWrapper<T> ne(SFunction<T, V> column, V value);

    <V> LambdaUpdateWrapper<T> gt(SFunction<T, V> column, V value);

    <V> LambdaUpdateWrapper<T> ge(SFunction<T, V> column, V value);

    <V> LambdaUpdateWrapper<T> lt(SFunction<T, V> column, V value);

    <V> LambdaUpdateWrapper<T> le(SFunction<T, V> column, V value);

    LambdaUpdateWrapper<T> like(SFunction<T, ?> column, String value);

    LambdaUpdateWrapper<T> in(SFunction<T, ?> column, Iterable<?> values);

    LambdaUpdateWrapper<T> notIn(SFunction<T, ?> column, Iterable<?> values);

    LambdaUpdateWrapper<T> between(SFunction<T, ?> column, Object start, Object end);

    LambdaUpdateWrapper<T> isNull(SFunction<T, ?> column);

    LambdaUpdateWrapper<T> isNotNull(SFunction<T, ?> column);

    LambdaUpdateWrapper<T> and();

    LambdaUpdateWrapper<T> or();

    LambdaUpdateWrapper<T> last(String lastSql);

    int update();

    String toSql();

    Map<String, Object> getBindings();
}
