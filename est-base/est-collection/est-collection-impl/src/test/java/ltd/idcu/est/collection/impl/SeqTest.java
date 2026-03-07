package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeqTest {

    @Test
    public void testEmptySeq() {
        Seq<String> seq = Seqs.empty();
        Assertions.assertTrue(seq.isEmpty());
        Assertions.assertEquals(0, seq.size());
    }

    @Test
    public void testOfVarargs() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        Assertions.assertFalse(seq.isEmpty());
        Assertions.assertEquals(3, seq.size());
        Assertions.assertEquals("a", seq.get(0));
        Assertions.assertEquals("b", seq.get(1));
        Assertions.assertEquals("c", seq.get(2));
    }

    @Test
    public void testFromIterable() {
        List<String> list = Arrays.asList("x", "y", "z");
        Seq<String> seq = Seqs.from(list);
        Assertions.assertEquals(3, seq.size());
        Assertions.assertEquals("x", seq.get(0));
    }

    @Test
    public void testFromStream() {
        Stream<String> stream = Stream.of("1", "2", "3");
        Seq<String> seq = Seqs.from(stream);
        Assertions.assertEquals(3, seq.size());
        Assertions.assertEquals("1", seq.get(0));
    }

    @Test
    public void testRange() {
        Seq<Integer> seq = Seqs.range(1, 5);
        Assertions.assertEquals(4, seq.size());
        Assertions.assertEquals(1, seq.get(0));
        Assertions.assertEquals(4, seq.get(3));
    }

    @Test
    public void testRangeWithStep() {
        Seq<Integer> seq = Seqs.range(0, 10, 2);
        Assertions.assertEquals(5, seq.size());
        Assertions.assertEquals(0, seq.get(0));
        Assertions.assertEquals(8, seq.get(4));
    }

    @Test
    public void testRangeDescending() {
        Seq<Integer> seq = Seqs.range(5, 0, -1);
        Assertions.assertEquals(5, seq.size());
        Assertions.assertEquals(5, seq.get(0));
        Assertions.assertEquals(1, seq.get(4));
    }

    @Test
    public void testRangeEmpty() {
        Seq<Integer> seq = Seqs.range(5, 1);
        Assertions.assertTrue(seq.isEmpty());
    }

    @Test
    public void testRangeZeroStepThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Seqs.range(1, 10, 0);
        });
    }

    @Test
    public void testGenerate() {
        Seq<Integer> seq = Seqs.generate(5, () -> 42);
        Assertions.assertEquals(5, seq.size());
        seq.forEach(i -> Assertions.assertEquals(42, i));
    }

    @Test
    public void testGenerateZeroCount() {
        Seq<Integer> seq = Seqs.generate(0, () -> 1);
        Assertions.assertTrue(seq.isEmpty());
    }

    @Test
    public void testGenerateNegativeCount() {
        Seq<Integer> seq = Seqs.generate(-5, () -> 1);
        Assertions.assertTrue(seq.isEmpty());
    }

    @Test
    public void testRepeat() {
        Seq<String> seq = Seqs.repeat("hello", 3);
        Assertions.assertEquals(3, seq.size());
        seq.forEach(s -> Assertions.assertEquals("hello", s));
    }

    @Test
    public void testRepeatZeroTimes() {
        Seq<String> seq = Seqs.repeat("hello", 0);
        Assertions.assertTrue(seq.isEmpty());
    }

    @Test
    public void testRepeatNegativeTimes() {
        Seq<String> seq = Seqs.repeat("hello", -1);
        Assertions.assertTrue(seq.isEmpty());
    }

    @Test
    public void testMap() {
        Seq<Integer> seq = Seqs.of(1, 2, 3);
        Seq<Integer> result = seq.map(i -> i * 2);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(2, result.get(0));
        Assertions.assertEquals(4, result.get(1));
        Assertions.assertEquals(6, result.get(2));
    }

    @Test
    public void testFilter() {
        Seq<Integer> seq = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> result = seq.filter(i -> i % 2 == 0);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2, result.get(0));
        Assertions.assertEquals(4, result.get(1));
    }

    @Test
    public void testFirst() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        Optional<String> first = seq.first();
        Assertions.assertTrue(first.isPresent());
        Assertions.assertEquals("a", first.get());
    }

    @Test
    public void testFirstEmpty() {
        Seq<String> seq = Seqs.empty();
        Optional<String> first = seq.first();
        Assertions.assertFalse(first.isPresent());
    }

    @Test
    public void testLast() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        Optional<String> last = seq.last();
        Assertions.assertTrue(last.isPresent());
        Assertions.assertEquals("c", last.get());
    }

    @Test
    public void testLastEmpty() {
        Seq<String> seq = Seqs.empty();
        Optional<String> last = seq.last();
        Assertions.assertFalse(last.isPresent());
    }

    @Test
    public void testContains() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        Assertions.assertTrue(seq.contains("a"));
        Assertions.assertTrue(seq.contains("b"));
        Assertions.assertFalse(seq.contains("d"));
    }

    @Test
    public void testIndexOf() {
        Seq<String> seq = Seqs.of("a", "b", "c", "b");
        Assertions.assertEquals(0, seq.indexOf("a"));
        Assertions.assertEquals(1, seq.indexOf("b"));
        Assertions.assertEquals(-1, seq.indexOf("d"));
    }

    @Test
    public void testLastIndexOf() {
        Seq<String> seq = Seqs.of("a", "b", "c", "b");
        Assertions.assertEquals(3, seq.lastIndexOf("b"));
        Assertions.assertEquals(-1, seq.lastIndexOf("d"));
    }

    @Test
    public void testTake() {
        Seq<Integer> seq = Seqs.range(1, 6);
        Seq<Integer> result = seq.take(3);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(1, result.get(0));
        Assertions.assertEquals(3, result.get(2));
    }

    @Test
    public void testTakeMoreThanSize() {
        Seq<Integer> seq = Seqs.range(1, 4);
        Seq<Integer> result = seq.take(10);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void testDrop() {
        Seq<Integer> seq = Seqs.range(1, 6);
        Seq<Integer> result = seq.drop(2);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(3, result.get(0));
        Assertions.assertEquals(5, result.get(2));
    }

    @Test
    public void testDropMoreThanSize() {
        Seq<Integer> seq = Seqs.range(1, 4);
        Seq<Integer> result = seq.drop(10);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testReverse() {
        Seq<Integer> seq = Seqs.of(1, 2, 3);
        Seq<Integer> result = seq.reverse();
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(3, result.get(0));
        Assertions.assertEquals(1, result.get(2));
    }

    @Test
    public void testDistinct() {
        Seq<Integer> seq = Seqs.of(1, 2, 2, 3, 3, 3);
        Seq<Integer> result = seq.distinct();
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(1, result.get(0));
        Assertions.assertEquals(2, result.get(1));
        Assertions.assertEquals(3, result.get(2));
    }

    @Test
    public void testSorted() {
        Seq<Integer> seq = Seqs.of(3, 1, 4, 1, 5);
        Seq<Integer> result = seq.sorted();
        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals(1, result.get(0));
        Assertions.assertEquals(5, result.get(4));
    }

    @Test
    public void testForEach() {
        Seq<Integer> seq = Seqs.of(1, 2, 3);
        int[] sum = {0};
        seq.forEach(i -> sum[0] += i);
        Assertions.assertEquals(6, sum[0]);
    }

    @Test
    public void testReduce() {
        Seq<Integer> seq = Seqs.of(1, 2, 3, 4);
        Optional<Integer> result = seq.reduce((a, b) -> a + b);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(10, result.get());
    }

    @Test
    public void testReduceWithIdentity() {
        Seq<Integer> seq = Seqs.of(1, 2, 3, 4);
        Integer result = seq.reduce(0, (a, b) -> a + b);
        Assertions.assertEquals(10, result);
    }

    @Test
    public void testAnyMatch() {
        Seq<Integer> seq = Seqs.of(1, 2, 3, 4, 5);
        Assertions.assertTrue(seq.anyMatch(i -> i > 3));
        Assertions.assertFalse(seq.anyMatch(i -> i > 10));
    }

    @Test
    public void testAllMatch() {
        Seq<Integer> seq = Seqs.of(1, 2, 3, 4, 5);
        Assertions.assertTrue(seq.allMatch(i -> i > 0));
        Assertions.assertFalse(seq.allMatch(i -> i > 3));
    }

    @Test
    public void testNoneMatch() {
        Seq<Integer> seq = Seqs.of(1, 2, 3, 4, 5);
        Assertions.assertTrue(seq.noneMatch(i -> i > 10));
        Assertions.assertFalse(seq.noneMatch(i -> i > 3));
    }

    @Test
    public void testToList() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        List<String> list = seq.toList();
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals("a", list.get(0));
    }

    @Test
    public void testToArray() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        Object[] array = seq.toArray();
        Assertions.assertEquals(3, array.length);
        Assertions.assertEquals("a", array[0]);
    }

    @Test
    public void testToArrayWithType() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        String[] array = seq.toArray(String[]::new);
        Assertions.assertEquals(3, array.length);
        Assertions.assertEquals("a", array[0]);
    }

    @Test
    public void testStream() {
        Seq<Integer> seq = Seqs.of(1, 2, 3);
        Stream<Integer> stream = seq.stream();
        List<Integer> list = stream.collect(Collectors.toList());
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals(1, list.get(0));
    }

    @Test
    public void testJoin() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        String result = seq.join(",");
        Assertions.assertEquals("a,b,c", result);
    }

    @Test
    public void testJoinEmpty() {
        Seq<String> seq = Seqs.empty();
        String result = seq.join(",");
        Assertions.assertEquals("", result);
    }

    @Test
    public void testFromList() {
        List<String> list = Arrays.asList("x", "y", "z");
        Seq<String> seq = Seqs.fromList(list);
        Assertions.assertEquals(3, seq.size());
        Assertions.assertEquals("x", seq.get(0));
    }
}
