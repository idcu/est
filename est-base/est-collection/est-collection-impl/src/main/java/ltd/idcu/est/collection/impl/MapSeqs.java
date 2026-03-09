package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.MapSeq;

import java.util.Map;

public final class MapSeqs {

    private MapSeqs() {
    }

    public static <K, V> MapSeq<K, V> empty() {
        return DefaultMapSeq.empty();
    }

    public static <K, V> MapSeq<K, V> of(K key, V value) {
        return DefaultMapSeq.of(key, value);
    }

    public static <K, V> MapSeq<K, V> of(K key1, V value1, K key2, V value2) {
        return DefaultMapSeq.of(key1, value1, key2, value2);
    }

    public static <K, V> MapSeq<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        return DefaultMapSeq.of(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> MapSeq<K, V> from(Map<? extends K, ? extends V> map) {
        return DefaultMapSeq.from(map);
    }
}
