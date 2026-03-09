package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.MapSeq;
import ltd.idcu.est.collection.api.Pair;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapSeqsTest {

    @Test
    public void testEmpty() {
        MapSeq<String, Integer> mapSeq = MapSeqs.empty();
        Assertions.assertTrue(mapSeq.isEmpty());
        Assertions.assertEquals(0, mapSeq.size());
    }

    @Test
    public void testOf() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1);
        Assertions.assertFalse(mapSeq.isEmpty());
        Assertions.assertEquals(1, mapSeq.size());
        Assertions.assertTrue(mapSeq.containsKey("one"));
        Assertions.assertEquals(1, mapSeq.getOrNull("one"));

        MapSeq<String, Integer> mapSeq2 = MapSeqs.of("one", 1, "two", 2);
        Assertions.assertEquals(2, mapSeq2.size());

        MapSeq<String, Integer> mapSeq3 = MapSeqs.of("one", 1, "two", 2, "three", 3);
        Assertions.assertEquals(3, mapSeq3.size());
    }

    @Test
    public void testFrom() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        MapSeq<String, Integer> mapSeq = MapSeqs.from(map);
        Assertions.assertEquals(3, mapSeq.size());
        Assertions.assertTrue(mapSeq.containsKey("a"));
        Assertions.assertTrue(mapSeq.containsKey("b"));
        Assertions.assertTrue(mapSeq.containsKey("c"));
    }

    @Test
    public void testKeys() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        var keys = mapSeq.keys();
        Assertions.assertEquals(2, keys.size());
        Assertions.assertTrue(keys.contains("one"));
        Assertions.assertTrue(keys.contains("two"));
    }

    @Test
    public void testValues() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        var values = mapSeq.values();
        Assertions.assertEquals(2, values.size());
        Assertions.assertTrue(values.contains(1));
        Assertions.assertTrue(values.contains(2));
    }

    @Test
    public void testEntries() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        var entries = mapSeq.entries();
        Assertions.assertEquals(2, entries.size());
    }

    @Test
    public void testMapKeys() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        MapSeq<String, Integer> result = mapSeq.mapKeys(String::toUpperCase);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.containsKey("ONE"));
        Assertions.assertTrue(result.containsKey("TWO"));
    }

    @Test
    public void testMapValues() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        MapSeq<String, Integer> result = mapSeq.mapValues(v -> v * 2);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2, result.getOrNull("one"));
        Assertions.assertEquals(4, result.getOrNull("two"));
    }

    @Test
    public void testMapEntries() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        MapSeq<String, Integer> result = mapSeq.mapEntries((k, v) -> 
            Pair.of(k.toUpperCase(), v * 2)
        );
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2, result.getOrNull("ONE"));
        Assertions.assertEquals(4, result.getOrNull("TWO"));
    }

    @Test
    public void testFilter() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2, "three", 3);
        MapSeq<String, Integer> result = mapSeq.filter((k, v) -> v > 1);
        Assertions.assertEquals(2, result.size());
        Assertions.assertFalse(result.containsKey("one"));
        Assertions.assertTrue(result.containsKey("two"));
        Assertions.assertTrue(result.containsKey("three"));
    }

    @Test
    public void testFilterKeys() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2, "three", 3);
        MapSeq<String, Integer> result = mapSeq.filterKeys(k -> k.startsWith("t"));
        Assertions.assertEquals(2, result.size());
        Assertions.assertFalse(result.containsKey("one"));
        Assertions.assertTrue(result.containsKey("two"));
        Assertions.assertTrue(result.containsKey("three"));
    }

    @Test
    public void testFilterValues() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2, "three", 3);
        MapSeq<String, Integer> result = mapSeq.filterValues(v -> v % 2 == 0);
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.containsKey("two"));
    }

    @Test
    public void testPlus() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1);
        MapSeq<String, Integer> result = mapSeq.plus("two", 2);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.containsKey("one"));
        Assertions.assertTrue(result.containsKey("two"));
    }

    @Test
    public void testPlusAll() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1);
        Map<String, Integer> other = new HashMap<>();
        other.put("two", 2);
        other.put("three", 3);
        MapSeq<String, Integer> result = mapSeq.plusAll(other);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void testMinus() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        MapSeq<String, Integer> result = mapSeq.minus("one");
        Assertions.assertEquals(1, result.size());
        Assertions.assertFalse(result.containsKey("one"));
        Assertions.assertTrue(result.containsKey("two"));
    }

    @Test
    public void testToMap() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        Map<String, Integer> map = mapSeq.toMap();
        Assertions.assertEquals(2, map.size());
        Assertions.assertTrue(map instanceof HashMap);
    }

    @Test
    public void testToMapWithFactory() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1, "two", 2);
        TreeMap<String, Integer> treeMap = mapSeq.toMap(TreeMap::new);
        Assertions.assertEquals(2, treeMap.size());
        Assertions.assertTrue(treeMap instanceof TreeMap);
    }

    @Test
    public void testImmutability() {
        MapSeq<String, Integer> mapSeq = MapSeqs.of("one", 1);
        Map<String, Integer> originalMap = mapSeq.toMap();
        
        MapSeq<String, Integer> modified = mapSeq.plus("two", 2);
        
        Assertions.assertEquals(1, mapSeq.size());
        Assertions.assertEquals(2, modified.size());
        Assertions.assertNotSame(mapSeq, modified);
    }
}
