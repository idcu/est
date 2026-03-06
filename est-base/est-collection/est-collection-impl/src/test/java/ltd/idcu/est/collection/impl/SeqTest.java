package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Pair;
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.*;
import java.util.stream.Collectors;

public class SeqTest {

    @Test
    public void testCreation() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        Assertions.assertEquals(3, seq.size());
        Assertions.assertTrue(seq.contains("a"));
        Assertions.assertTrue(seq.contains("b"));
        Assertions.assertTrue(seq.contains("c"));

        Seq<String> empty = Seqs.empty();
        Assertions.assertTrue(empty.isEmpty());

        List<String> list = Arrays.asList("x", "y", "z");
        Seq<String> fromList = Seqs.from(list);
        Assertions.assertEquals(3, fromList.size());
    }

    @Test
    public void testMap() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> doubled = numbers.map(n -> n * 2);
        Assertions.assertEquals(Arrays.asList(2, 4, 6, 8, 10), doubled.toList());
    }

    @Test
    public void testPluck() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("a", 1),
            Pair.of("b", 2),
            Pair.of("c", 3)
        );
        Seq<Integer> values = pairs.pluck(Pair::getValue);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), values.toList());
    }

    @Test
    public void testMapIndexed() {
        Seq<String> letters = Seqs.of("a", "b", "c");
        Seq<String> indexed = letters.mapIndexed((i, s) -> i + ":" + s);
        Assertions.assertEquals(Arrays.asList("0:a", "1:b", "2:c"), indexed.toList());
    }

    @Test
    public void testFilter() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Seq<Integer> evens = numbers.filter(n -> n % 2 == 0);
        Assertions.assertEquals(Arrays.asList(2, 4, 6, 8, 10), evens.toList());
    }

    @Test
    public void testWhere() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> evens = numbers.where(n -> n % 2 == 0);
        Assertions.assertEquals(Arrays.asList(2, 4), evens.toList());
    }

    @Test
    public void testFilterNot() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> notEvens = numbers.filterNot(n -> n % 2 == 0);
        Assertions.assertEquals(Arrays.asList(1, 3, 5), notEvens.toList());
    }

    @Test
    public void testDistinct() {
        Seq<String> duplicates = Seqs.of("a", "b", "a", "c", "b");
        Seq<String> unique = duplicates.distinct();
        Assertions.assertEquals(Arrays.asList("a", "b", "c"), unique.toList());
    }

    @Test
    public void testDistinctBy() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("apple", 1),
            Pair.of("banana", 2),
            Pair.of("apple", 3)
        );
        Seq<Pair<String, Integer>> distinct = pairs.distinctBy(Pair::getKey);
        Assertions.assertEquals(2, distinct.size());
    }

    @Test
    public void testTake() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> first3 = numbers.take(3);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), first3.toList());

        Seq<Integer> take0 = numbers.take(0);
        Assertions.assertTrue(take0.isEmpty());

        Seq<Integer> take10 = numbers.take(10);
        Assertions.assertEquals(5, take10.size());
    }

    @Test
    public void testTakeWhile() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 1, 2);
        Seq<Integer> taken = numbers.takeWhile(n -> n < 5);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), taken.toList());
    }

    @Test
    public void testTakeLast() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> last2 = numbers.takeLast(2);
        Assertions.assertEquals(Arrays.asList(4, 5), last2.toList());
    }

    @Test
    public void testDrop() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> dropped = numbers.drop(2);
        Assertions.assertEquals(Arrays.asList(3, 4, 5), dropped.toList());
    }

    @Test
    public void testDropWhile() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> dropped = numbers.dropWhile(n -> n < 3);
        Assertions.assertEquals(Arrays.asList(3, 4, 5), dropped.toList());
    }

    @Test
    public void testDropLast() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> dropped = numbers.dropLast(2);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), dropped.toList());
    }

    @Test
    public void testSlice() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> sliced = numbers.slice(1, 4);
        Assertions.assertEquals(Arrays.asList(2, 3, 4), sliced.toList());
    }

    @Test
    public void testSorted() {
        Seq<Integer> numbers = Seqs.of(3, 1, 4, 1, 5);
        Seq<Integer> sorted = numbers.sorted();
        Assertions.assertEquals(Arrays.asList(1, 1, 3, 4, 5), sorted.toList());
    }

    @Test
    public void testSortedWithComparator() {
        Seq<String> words = Seqs.of("banana", "apple", "cherry");
        Seq<String> sorted = words.sorted(Comparator.reverseOrder());
        Assertions.assertEquals(Arrays.asList("cherry", "banana", "apple"), sorted.toList());
    }

    @Test
    public void testSortBy() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("banana", 2),
            Pair.of("apple", 1),
            Pair.of("cherry", 3)
        );
        Seq<Pair<String, Integer>> sorted = pairs.sortBy(Pair::getValue);
        Assertions.assertEquals("apple", sorted.first().orElse(null).getKey());
    }

    @Test
    public void testSortByDesc() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("banana", 2),
            Pair.of("apple", 1),
            Pair.of("cherry", 3)
        );
        Seq<Pair<String, Integer>> sorted = pairs.sortByDesc(Pair::getValue);
        Assertions.assertEquals("cherry", sorted.first().orElse(null).getKey());
    }

    @Test
    public void testReversed() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> reversed = numbers.reversed();
        Assertions.assertEquals(Arrays.asList(5, 4, 3, 2, 1), reversed.toList());
    }

    @Test
    public void testFirst() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertEquals(1, numbers.first().orElse(null));
    }

    @Test
    public void testFirstOrNull() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertEquals(1, numbers.firstOrNull());

        Seq<Integer> empty = Seqs.empty();
        Assertions.assertNull(empty.firstOrNull());
    }

    @Test
    public void testFirstWithPredicate() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(4, numbers.first(n -> n > 3).orElse(null));
    }

    @Test
    public void testLast() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertEquals(3, numbers.last().orElse(null));
    }

    @Test
    public void testLastOrNull() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertEquals(3, numbers.lastOrNull());

        Seq<Integer> empty = Seqs.empty();
        Assertions.assertNull(empty.lastOrNull());
    }

    @Test
    public void testElementAt() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(3, numbers.elementAt(2));
    }

    @Test
    public void testElementAtOrNull() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertEquals(2, numbers.elementAtOrNull(1));
        Assertions.assertNull(numbers.elementAtOrNull(10));
    }

    @Test
    public void testReduce() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(15, numbers.reduce(Integer::sum).orElse(null));
    }

    @Test
    public void testReduceWithInitial() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertEquals(16, numbers.reduce(10, Integer::sum));
    }

    @Test
    public void testFold() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertEquals(16, numbers.fold(10, Integer::sum));
    }

    @Test
    public void testGroupBy() {
        Seq<String> words = Seqs.of("apple", "banana", "apricot", "blueberry", "cherry");
        Map<String, Seq<String>> grouped = words.groupBy(s -> s.substring(0, 1));
        Assertions.assertEquals(3, grouped.size());
        Assertions.assertEquals(2, grouped.get("a").size());
    }

    @Test
    public void testAssociateBy() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("one", 1),
            Pair.of("two", 2)
        );
        Map<String, Integer> map = pairs.associateBy(Pair::getKey, Pair::getValue);
        Assertions.assertEquals(2, map.size());
        Assertions.assertEquals(1, map.get("one"));
    }

    @Test
    public void testKeyBy() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("one", 1),
            Pair.of("two", 2)
        );
        Map<String, Pair<String, Integer>> map = pairs.keyBy(Pair::getKey);
        Assertions.assertEquals(2, map.size());
        Assertions.assertEquals(1, map.get("one").getValue());
    }

    @Test
    public void testCountBy() {
        Seq<String> words = Seqs.of("a", "b", "a", "c", "a", "b");
        Map<String, Long> counts = words.countBy(s -> s);
        Assertions.assertEquals(3L, counts.get("a"));
        Assertions.assertEquals(2L, counts.get("b"));
        Assertions.assertEquals(1L, counts.get("c"));
    }

    @Test
    public void testPlus() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Seq<Integer> added = numbers.plus(4);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), added.toList());
    }

    @Test
    public void testPlusAll() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Seq<Integer> added = numbers.plusAll(Arrays.asList(4, 5, 6));
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), added.toList());
    }

    @Test
    public void testMinus() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 2);
        Seq<Integer> minus = numbers.minus(2);
        Assertions.assertEquals(Arrays.asList(1, 3), minus.toList());
    }

    @Test
    public void testMinusAll() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> minus = numbers.minusAll(Arrays.asList(2, 4));
        Assertions.assertEquals(Arrays.asList(1, 3, 5), minus.toList());
    }

    @Test
    public void testIntersect() {
        Seq<Integer> a = Seqs.of(1, 2, 3, 4);
        Seq<Integer> b = Seqs.of(3, 4, 5, 6);
        Seq<Integer> intersect = a.intersect(b);
        Assertions.assertEquals(Arrays.asList(3, 4), intersect.toList());
    }

    @Test
    public void testUnion() {
        Seq<Integer> a = Seqs.of(1, 2, 3);
        Seq<Integer> b = Seqs.of(3, 4, 5);
        Seq<Integer> union = a.union(b);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5), union.toList());
    }

    @Test
    public void testDiff() {
        Seq<Integer> a = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> b = Seqs.of(3, 4);
        Seq<Integer> diff = a.diff(b);
        Assertions.assertEquals(Arrays.asList(1, 2, 5), diff.toList());
    }

    @Test
    public void testToList() {
        Seq<String> seq = Seqs.of("a", "b", "c");
        List<String> list = seq.toList();
        Assertions.assertTrue(list instanceof ArrayList);
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void testToSet() {
        Seq<String> seq = Seqs.of("a", "b", "a");
        Set<String> set = seq.toSet();
        Assertions.assertTrue(set instanceof LinkedHashSet);
        Assertions.assertEquals(2, set.size());
    }

    @Test
    public void testJoinToString() {
        Seq<String> words = Seqs.of("Hello", "World", "!");
        Assertions.assertEquals("[Hello, World, !]", words.joinToString());
        Assertions.assertEquals("[Hello|World|!]", words.joinToString("|"));
        Assertions.assertEquals("<Hello; World; !>", words.joinToString("; ", "<", ">"));
        Assertions.assertEquals("HELLO WORLD !", words.joinToString(" ", "", "", String::toUpperCase));
    }

    @Test
    public void testImplode() {
        Seq<String> words = Seqs.of("Hello", "World", "!");
        Assertions.assertEquals("[Hello, World, !]", words.implode());
        Assertions.assertEquals("[Hello|World|!]", words.implode("|"));
    }

    @Test
    public void testAll() {
        Seq<Integer> numbers = Seqs.of(2, 4, 6, 8);
        Assertions.assertTrue(numbers.all(n -> n % 2 == 0));
        Assertions.assertFalse(numbers.all(n -> n > 5));
    }

    @Test
    public void testAny() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4);
        Assertions.assertTrue(numbers.any(n -> n > 3));
        Assertions.assertFalse(numbers.any(n -> n > 10));
    }

    @Test
    public void testNone() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Assertions.assertTrue(numbers.none(n -> n > 10));
        Assertions.assertFalse(numbers.none(n -> n > 2));
    }

    @Test
    public void testCount() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(5, numbers.count());
        Assertions.assertEquals(3, numbers.count(n -> n > 2));
    }

    @Test
    public void testMin() {
        Seq<Integer> numbers = Seqs.of(3, 1, 4, 1, 5);
        Assertions.assertEquals(1, numbers.min(Comparator.naturalOrder()).orElse(null));
    }

    @Test
    public void testMax() {
        Seq<Integer> numbers = Seqs.of(3, 1, 4, 1, 5);
        Assertions.assertEquals(5, numbers.max(Comparator.naturalOrder()).orElse(null));
    }

    @Test
    public void testMinBy() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("a", 3),
            Pair.of("b", 1),
            Pair.of("c", 4)
        );
        Assertions.assertEquals("b", pairs.minBy(Pair::getValue).orElse(null).getKey());
    }

    @Test
    public void testMaxBy() {
        Seq<Pair<String, Integer>> pairs = Seqs.of(
            Pair.of("a", 3),
            Pair.of("b", 1),
            Pair.of("c", 4)
        );
        Assertions.assertEquals("c", pairs.maxBy(Pair::getValue).orElse(null).getKey());
    }

    @Test
    public void testChunked() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7);
        Seq<List<Integer>> chunks = numbers.chunked(3);
        Assertions.assertEquals(3, chunks.size());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), chunks.first().orElse(null));
        Assertions.assertEquals(Arrays.asList(7), chunks.last().orElse(null));
    }

    @Test
    public void testChunk() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7);
        Seq<List<Integer>> chunks = numbers.chunk(3);
        Assertions.assertEquals(3, chunks.size());
    }

    @Test
    public void testWindowed() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<List<Integer>> windows = numbers.windowed(3);
        Assertions.assertEquals(3, windows.size());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), windows.first().orElse(null));
        Assertions.assertEquals(Arrays.asList(3, 4, 5), windows.last().orElse(null));
    }

    @Test
    public void testWindowedWithStep() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<List<Integer>> windows = numbers.windowed(2, 2);
        Assertions.assertEquals(2, windows.size());
        Assertions.assertEquals(Arrays.asList(1, 2), windows.first().orElse(null));
        Assertions.assertEquals(Arrays.asList(3, 4), windows.last().orElse(null));
    }

    @Test
    public void testWindowedWithPartial() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<List<Integer>> windows = numbers.windowed(3, 2, true);
        Assertions.assertEquals(3, windows.size());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), windows.first().orElse(null));
        Assertions.assertEquals(Arrays.asList(5), windows.last().orElse(null));
    }

    @Test
    public void testStream() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        long count = numbers.stream().filter(n -> n > 3).count();
        Assertions.assertEquals(2L, count);
    }

    @Test
    public void testParallelStream() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        int sum = numbers.parallelStream().mapToInt(Integer::intValue).sum();
        Assertions.assertEquals(15, sum);
    }

    @Test
    public void testCollect() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        List<Integer> collected = numbers.collect(Collectors.toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5), collected);
    }

    @Test
    public void testPeek() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        List<Integer> result = new ArrayList<>();
        numbers.peek(result::add);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testTap() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        List<Integer> result = new ArrayList<>();
        numbers.tap(result::add);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testForEach() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        List<Integer> result = new ArrayList<>();
        numbers.forEach(result::add);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testForEachIndexed() {
        Seq<String> letters = Seqs.of("a", "b", "c");
        List<String> result = new ArrayList<>();
        letters.forEachIndexed((i, s) -> result.add(i + ":" + s));
        Assertions.assertEquals(Arrays.asList("0:a", "1:b", "2:c"), result);
    }

    @Test
    public void testPartition() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6);
        Pair<Seq<Integer>, Seq<Integer>> partitioned = numbers.partition(n -> n % 2 == 0);
        Assertions.assertEquals(Arrays.asList(2, 4, 6), partitioned.getKey().toList());
        Assertions.assertEquals(Arrays.asList(1, 3, 5), partitioned.getValue().toList());
    }

    @Test
    public void testZip() {
        Seq<String> letters = Seqs.of("a", "b", "c");
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Seq<Pair<String, Integer>> zipped = letters.zip(numbers);
        Assertions.assertEquals(3, zipped.size());
        Assertions.assertEquals("a", zipped.first().orElse(null).getKey());
        Assertions.assertEquals(1, zipped.first().orElse(null).getValue());
    }

    @Test
    public void testZipWithIndex() {
        Seq<String> letters = Seqs.of("a", "b", "c");
        Seq<Pair<Integer, String>> indexed = letters.zipWithIndex();
        Assertions.assertEquals(3, indexed.size());
        Assertions.assertEquals(0, indexed.first().orElse(null).getKey());
        Assertions.assertEquals("a", indexed.first().orElse(null).getValue());
        Assertions.assertEquals(2, indexed.last().orElse(null).getKey());
        Assertions.assertEquals("c", indexed.last().orElse(null).getValue());
    }

    @Test
    public void testRandom() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Integer random = numbers.random().orElse(null);
        Assertions.assertTrue(random >= 1 && random <= 5);
    }

    @Test
    public void testSample() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Seq<Integer> sample = numbers.sample(3);
        Assertions.assertEquals(3, sample.size());
        
        Seq<Integer> sampleAll = numbers.sample(20);
        Assertions.assertEquals(10, sampleAll.size());
        
        Seq<Integer> sampleNone = numbers.sample(0);
        Assertions.assertTrue(sampleNone.isEmpty());
    }

    @Test
    public void testRotate() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> rotated = numbers.rotate(2);
        Assertions.assertEquals(Arrays.asList(4, 5, 1, 2, 3), rotated.toList());
        
        Seq<Integer> rotatedNegative = numbers.rotate(-2);
        Assertions.assertEquals(Arrays.asList(3, 4, 5, 1, 2), rotatedNegative.toList());
        
        Seq<Integer> rotatedZero = numbers.rotate(0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5), rotatedZero.toList());
    }

    @Test
    public void testPadStart() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Seq<Integer> padded = numbers.padStart(5, 0);
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 2, 3), padded.toList());
        
        Seq<Integer> paddedSame = numbers.padStart(3, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedSame.toList());
        
        Seq<Integer> paddedLess = numbers.padStart(2, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedLess.toList());
    }

    @Test
    public void testPadEnd() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3);
        Seq<Integer> padded = numbers.padEnd(5, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 0, 0), padded.toList());
        
        Seq<Integer> paddedSame = numbers.padEnd(3, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedSame.toList());
        
        Seq<Integer> paddedLess = numbers.padEnd(2, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedLess.toList());
    }

    @Test
    public void testShuffled() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Seq<Integer> shuffled = numbers.shuffled();
        Assertions.assertEquals(5, shuffled.size());
        Assertions.assertTrue(shuffled.contains(1));
        Assertions.assertTrue(shuffled.contains(2));
        Assertions.assertTrue(shuffled.contains(3));
        Assertions.assertTrue(shuffled.contains(4));
        Assertions.assertTrue(shuffled.contains(5));
    }

    @Test
    public void testShuffledWithRandom() {
        Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
        Random random = new Random(42);
        Seq<Integer> shuffled = numbers.shuffled(random);
        Assertions.assertEquals(5, shuffled.size());
    }

    @Test
    public void testRange() {
        Seq<Integer> range = Seqs.range(1, 5);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), range.toList());
    }

    @Test
    public void testRangeWithStep() {
        Seq<Integer> range = Seqs.range(1, 10, 2);
        Assertions.assertEquals(Arrays.asList(1, 3, 5, 7, 9), range.toList());
    }

    @Test
    public void testGenerate() {
        Seq<Double> randoms = Seqs.generate(5, Math::random);
        Assertions.assertEquals(5, randoms.size());
    }

    @Test
    public void testRepeat() {
        Seq<String> repeated = Seqs.repeat("hello", 5);
        Assertions.assertEquals(Arrays.asList("hello", "hello", "hello", "hello", "hello"), repeated.toList());
    }

    @Test
    public void testMutable() {
        Seq<Integer> seq = Seqs.of(1, 2, 3);
        ltd.idcu.est.collection.api.MutableSeq<Integer> mutable = seq.mutable();
        mutable.add(4);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), mutable.toList());
        
        Seq<Integer> immutable = mutable.immutable();
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), immutable.toList());
    }

    @Test
    public void testEqualsAndHashCode() {
        Seq<String> a = Seqs.of("a", "b", "c");
        Seq<String> b = Seqs.of("a", "b", "c");
        Seq<String> c = Seqs.of("x", "y", "z");

        Assertions.assertEquals(a, b);
        Assertions.assertNotEquals(a, c);
        Assertions.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        Seq<String> words = Seqs.of("Hello", "World");
        Assertions.assertEquals("[Hello, World]", words.toString());
    }
}
