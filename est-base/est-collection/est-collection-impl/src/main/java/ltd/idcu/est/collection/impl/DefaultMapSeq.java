package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.MapSeq;
import ltd.idcu.est.collection.api.Pair;
import ltd.idcu.est.collection.api.Seq;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultMapSeq<K, V> implements MapSeq<K, V> {

    private final Map<K, V> map;

    private DefaultMapSeq(Map<K, V> map) {
        this.map = Collections.unmodifiableMap(new HashMap<>(map));
    }

    public static <K, V> DefaultMapSeq<K, V> empty() {
        return new DefaultMapSeq<>(Collections.emptyMap());
    }

    public static <K, V> DefaultMapSeq<K, V> of(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return new DefaultMapSeq<>(map);
    }

    public static <K, V> DefaultMapSeq<K, V> of(K key1, V value1, K key2, V value2) {
        Map<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return new DefaultMapSeq<>(map);
    }

    public static <K, V> DefaultMapSeq<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        Map<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        return new DefaultMapSeq<>(map);
    }

    public static <K, V> DefaultMapSeq<K, V> from(Map<? extends K, ? extends V> map) {
        return new DefaultMapSeq<>(new HashMap<>(map));
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public Seq<K> keys() {
        return Seqs.from(map.keySet());
    }

    @Override
    public Seq<V> values() {
        return Seqs.from(map.values());
    }

    @Override
    public Seq<Pair<K, V>> entries() {
        return Seqs.from(map.entrySet().stream()
            .map(e -> Pair.of(e.getKey(), e.getValue()))
            .collect(Collectors.toList()));
    }

    @Override
    public <NK, NV> MapSeq<NK, NV> mapEntries(BiFunction<? super K, ? super V, ? extends Pair<NK, NV>> mapper) {
        Map<NK, NV> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Pair<NK, NV> newEntry = mapper.apply(entry.getKey(), entry.getValue());
            newMap.put(newEntry.getKey(), newEntry.getValue());
        }
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public <NK> MapSeq<NK, V> mapKeys(Function<? super K, ? extends NK> mapper) {
        Map<NK, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            newMap.put(mapper.apply(entry.getKey()), entry.getValue());
        }
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public <NV> MapSeq<K, NV> mapValues(Function<? super V, ? extends NV> mapper) {
        Map<K, NV> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            newMap.put(entry.getKey(), mapper.apply(entry.getValue()));
        }
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public MapSeq<K, V> filter(BiPredicate<? super K, ? super V> predicate) {
        Map<K, V> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.test(entry.getKey(), entry.getValue())) {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public MapSeq<K, V> filterKeys(Predicate<? super K> predicate) {
        return filter((k, v) -> predicate.test(k));
    }

    @Override
    public MapSeq<K, V> filterValues(Predicate<? super V> predicate) {
        return filter((k, v) -> predicate.test(v));
    }

    @Override
    public MapSeq<K, V> filterNot(BiPredicate<? super K, ? super V> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public MapSeq<K, V> plus(K key, V value) {
        Map<K, V> newMap = new HashMap<>(map);
        newMap.put(key, value);
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public MapSeq<K, V> plusAll(Map<? extends K, ? extends V> other) {
        Map<K, V> newMap = new HashMap<>(map);
        newMap.putAll(other);
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public MapSeq<K, V> minus(K key) {
        Map<K, V> newMap = new HashMap<>(map);
        newMap.remove(key);
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public MapSeq<K, V> minusAll(Iterable<? extends K> keys) {
        Map<K, V> newMap = new HashMap<>(map);
        for (K key : keys) {
            newMap.remove(key);
        }
        return new DefaultMapSeq<>(newMap);
    }

    @Override
    public Map<K, V> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public <M extends Map<K, V>> M toMap(Function<Map<K, V>, M> mapFactory) {
        return mapFactory.apply(new HashMap<>(map));
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultMapSeq<?, ?> that = (DefaultMapSeq<?, ?>) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
