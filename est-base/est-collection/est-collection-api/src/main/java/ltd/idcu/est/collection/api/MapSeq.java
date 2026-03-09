package ltd.idcu.est.collection.api;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MapSeq<K, V> extends Iterable<Map.Entry<K, V>> {

    int size();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    boolean containsKey(K key);

    boolean containsValue(V value);

    Optional<V> get(K key);

    default V getOr(K key, V defaultValue) {
        return get(key).orElse(defaultValue);
    }

    default V getOrNull(K key) {
        return get(key).orElse(null);
    }

    Seq<K> keys();

    Seq<V> values();

    Seq<Pair<K, V>> entries();

    <NK, NV> MapSeq<NK, NV> mapEntries(BiFunction<? super K, ? super V, ? extends Pair<NK, NV>> mapper);

    <NK> MapSeq<NK, V> mapKeys(Function<? super K, ? extends NK> mapper);

    <NV> MapSeq<K, NV> mapValues(Function<? super V, ? extends NV> mapper);

    MapSeq<K, V> filter(BiPredicate<? super K, ? super V> predicate);

    default MapSeq<K, V> filterKeys(Predicate<? super K> predicate) {
        return filter((k, v) -> predicate.test(k));
    }

    default MapSeq<K, V> filterValues(Predicate<? super V> predicate) {
        return filter((k, v) -> predicate.test(v));
    }

    default MapSeq<K, V> filterNot(BiPredicate<? super K, ? super V> predicate) {
        return filter((k, v) -> !predicate.test(k, v));
    }

    MapSeq<K, V> plus(K key, V value);

    MapSeq<K, V> plusAll(Map<? extends K, ? extends V> map);

    MapSeq<K, V> minus(K key);

    MapSeq<K, V> minusAll(Iterable<? extends K> keys);

    Map<K, V> toMap();

    <M extends Map<K, V>> M toMap(Function<Map<K, V>, M> mapFactory);
}
