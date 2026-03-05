package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.api.Collections;
import ltd.idcu.est.collection.api.LazyCollection;
import ltd.idcu.est.collection.api.Pair;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.AfterAll;
import ltd.idcu.est.test.annotation.BeforeAll;
import ltd.idcu.est.test.annotation.Test;

import java.util.*;

public class CollectionTest {

    @BeforeAll
    static void beforeAll() {
        CollectionInitializer.init();
    }

    @Test
    public void testCreation() {
        Collection<String> coll = Collections.of("a", "b", "c");
        Assertions.assertEquals(3, coll.size());
        Assertions.assertTrue(coll.contains("a"));
        Assertions.assertTrue(coll.contains("b"));
        Assertions.assertTrue(coll.contains("c"));

        Collection<String> empty = Collections.empty();
        Assertions.assertTrue(empty.isEmpty());

        List<String> list = Arrays.asList("x", "y", "z");
        Collection<String> fromList = Collections.fromIterable(list);
        Assertions.assertEquals(3, fromList.size());
    }

    @Test
    public void testMap() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> doubled = numbers.map(n -> n * 2);
        Assertions.assertEquals(Arrays.asList(2, 4, 6, 8, 10), doubled.toList());
    }

    @Test
    public void testMapIndexed() {
        Collection<String> letters = Collections.of("a", "b", "c");
        Collection<String> indexed = letters.mapIndexed((i, s) -> i + ":" + s);
        Assertions.assertEquals(Arrays.asList("0:a", "1:b", "2:c"), indexed.toList());
    }

    @Test
    public void testFilter() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collection<Integer> evens = numbers.filter(n -> n % 2 == 0);
        Assertions.assertEquals(Arrays.asList(2, 4, 6, 8, 10), evens.toList());
    }

    @Test
    public void testFilterNot() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> notEvens = numbers.filterNot(n -> n % 2 == 0);
        Assertions.assertEquals(Arrays.asList(1, 3, 5), notEvens.toList());
    }

    @Test
    public void testDistinct() {
        Collection<String> duplicates = Collections.of("a", "b", "a", "c", "b");
        Collection<String> unique = duplicates.distinct();
        Assertions.assertEquals(Arrays.asList("a", "b", "c"), unique.toList());
    }

    @Test
    public void testDistinctBy() {
        Collection<Pair<String, Integer>> pairs = Collections.of(
            Pair.of("apple", 1),
            Pair.of("banana", 2),
            Pair.of("apple", 3)
        );
        Collection<Pair<String, Integer>> distinct = pairs.distinctBy(Pair::getKey);
        Assertions.assertEquals(2, distinct.size());
    }

    @Test
    public void testTake() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> first3 = numbers.take(3);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), first3.toList());

        Collection<Integer> take0 = numbers.take(0);
        Assertions.assertTrue(take0.isEmpty());

        Collection<Integer> take10 = numbers.take(10);
        Assertions.assertEquals(5, take10.size());
    }

    @Test
    public void testTakeWhile() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 1, 2);
        Collection<Integer> taken = numbers.takeWhile(n -> n < 5);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), taken.toList());
    }

    @Test
    public void testTakeLast() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> last2 = numbers.takeLast(2);
        Assertions.assertEquals(Arrays.asList(4, 5), last2.toList());
    }

    @Test
    public void testDrop() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> dropped = numbers.drop(2);
        Assertions.assertEquals(Arrays.asList(3, 4, 5), dropped.toList());
    }

    @Test
    public void testDropWhile() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> dropped = numbers.dropWhile(n -> n < 3);
        Assertions.assertEquals(Arrays.asList(3, 4, 5), dropped.toList());
    }

    @Test
    public void testDropLast() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> dropped = numbers.dropLast(2);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), dropped.toList());
    }

    @Test
    public void testSlice() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> sliced = numbers.slice(1, 4);
        Assertions.assertEquals(Arrays.asList(2, 3, 4), sliced.toList());
    }

    @Test
    public void testSorted() {
        Collection<Integer> numbers = Collections.of(3, 1, 4, 1, 5);
        Collection<Integer> sorted = numbers.sorted();
        Assertions.assertEquals(Arrays.asList(1, 1, 3, 4, 5), sorted.toList());
    }

    @Test
    public void testSortedWithComparator() {
        Collection<String> words = Collections.of("banana", "apple", "cherry");
        Collection<String> sorted = words.sorted(Comparator.reverseOrder());
        Assertions.assertEquals(Arrays.asList("cherry", "banana", "apple"), sorted.toList());
    }

    @Test
    public void testSortBy() {
        Collection<Pair<String, Integer>> pairs = Collections.of(
            Pair.of("banana", 2),
            Pair.of("apple", 1),
            Pair.of("cherry", 3)
        );
        Collection<Pair<String, Integer>> sorted = pairs.sortBy(Pair::getValue);
        Assertions.assertEquals("apple", sorted.first().getKey());
    }

    @Test
    public void testReversed() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> reversed = numbers.reversed();
        Assertions.assertEquals(Arrays.asList(5, 4, 3, 2, 1), reversed.toList());
    }

    @Test
    public void testFirst() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(1, numbers.first());
    }

    @Test
    public void testFirstOrNull() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(1, numbers.firstOrNull());

        Collection<Integer> empty = Collections.empty();
        Assertions.assertNull(empty.firstOrNull());
    }

    @Test
    public void testFirstWithPredicate() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(4, numbers.first(n -> n > 3));
    }

    @Test
    public void testLast() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(3, numbers.last());
    }

    @Test
    public void testLastOrNull() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(3, numbers.lastOrNull());

        Collection<Integer> empty = Collections.empty();
        Assertions.assertNull(empty.lastOrNull());
    }

    @Test
    public void testSingle() {
        Collection<Integer> single = Collections.of(42);
        Assertions.assertEquals(42, single.single());
    }

    @Test
    public void testSingleOrNull() {
        Collection<Integer> single = Collections.of(42);
        Assertions.assertEquals(42, single.singleOrNull());

        Collection<Integer> multiple = Collections.of(1, 2);
        Assertions.assertNull(multiple.singleOrNull());

        Collection<Integer> empty = Collections.empty();
        Assertions.assertNull(empty.singleOrNull());
    }

    @Test
    public void testElementAt() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(3, numbers.elementAt(2));
    }

    @Test
    public void testElementAtOrNull() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(2, numbers.elementAtOrNull(1));
        Assertions.assertNull(numbers.elementAtOrNull(10));
    }

    @Test
    public void testElementAtOrElse() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(2, numbers.elementAtOrElse(1, 99));
        Assertions.assertEquals(99, numbers.elementAtOrElse(10, 99));
    }

    @Test
    public void testReduce() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(15, numbers.reduce(Integer::sum));
    }

    @Test
    public void testReduceOrNull() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(6, numbers.reduceOrNull(Integer::sum));

        Collection<Integer> empty = Collections.empty();
        Assertions.assertNull(empty.reduceOrNull(Integer::sum));
    }

    @Test
    public void testReduceWithInitial() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(16, numbers.reduce(10, Integer::sum));
    }

    @Test
    public void testFold() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertEquals(16, numbers.fold(10, Integer::sum));
    }

    @Test
    public void testGroupBy() {
        Collection<String> words = Collections.of("apple", "banana", "apricot", "blueberry", "cherry");
        Map<String, Collection<String>> grouped = words.groupBy(s -> s.substring(0, 1));
        Assertions.assertEquals(3, grouped.size());
        Assertions.assertEquals(2, grouped.get("a").size());
    }

    @Test
    public void testAssociateBy() {
        Collection<Pair<String, Integer>> pairs = Collections.of(
            Pair.of("one", 1),
            Pair.of("two", 2)
        );
        Map<String, Integer> map = pairs.associateBy(Pair::getKey, Pair::getValue);
        Assertions.assertEquals(2, map.size());
        Assertions.assertEquals(1, map.get("one"));
    }

    @Test
    public void testPlus() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Collection<Integer> added = numbers.plus(4);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), added.toList());
    }

    @Test
    public void testPlusAll() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Collection<Integer> added = numbers.plusAll(Arrays.asList(4, 5, 6));
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), added.toList());
    }

    @Test
    public void testMinus() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 2);
        Collection<Integer> minus = numbers.minus(2);
        Assertions.assertEquals(Arrays.asList(1, 3), minus.toList());
    }

    @Test
    public void testMinusAll() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> minus = numbers.minusAll(Arrays.asList(2, 4));
        Assertions.assertEquals(Arrays.asList(1, 3, 5), minus.toList());
    }

    @Test
    public void testIntersect() {
        Collection<Integer> a = Collections.of(1, 2, 3, 4);
        Collection<Integer> b = Collections.of(3, 4, 5, 6);
        Collection<Integer> intersect = a.intersect(b);
        Assertions.assertEquals(Arrays.asList(3, 4), intersect.toList());
    }

    @Test
    public void testUnion() {
        Collection<Integer> a = Collections.of(1, 2, 3);
        Collection<Integer> b = Collections.of(3, 4, 5);
        Collection<Integer> union = a.union(b);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5), union.toList());
    }

    @Test
    public void testSubtract() {
        Collection<Integer> a = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> b = Collections.of(3, 4);
        Collection<Integer> subtract = a.subtract(b);
        Assertions.assertEquals(Arrays.asList(1, 2, 5), subtract.toList());
    }

    @Test
    public void testEachCount() {
        Collection<String> words = Collections.of("a", "b", "a", "c", "a", "b");
        Map<String, Integer> counts = words.eachCount(s -> s);
        Assertions.assertEquals(3, counts.get("a"));
        Assertions.assertEquals(2, counts.get("b"));
        Assertions.assertEquals(1, counts.get("c"));
    }

    @Test
    public void testToList() {
        Collection<String> coll = Collections.of("a", "b", "c");
        List<String> list = coll.toList();
        Assertions.assertTrue(list instanceof ArrayList);
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void testToSet() {
        Collection<String> coll = Collections.of("a", "b", "a");
        Set<String> set = coll.toSet();
        Assertions.assertTrue(set instanceof LinkedHashSet);
        Assertions.assertEquals(2, set.size());
    }

    @Test
    public void testJoinToString() {
        Collection<String> words = Collections.of("Hello", "World", "!");
        Assertions.assertEquals("[Hello, World, !]", words.joinToString());
        Assertions.assertEquals("Hello|World|!", words.joinToString("|"));
        Assertions.assertEquals("<Hello; World; !>", words.joinToString("; ", "<", ">"));
        Assertions.assertEquals("HELLO WORLD !", words.joinToString(" ", "", "", String::toUpperCase));
    }

    @Test
    public void testAll() {
        Collection<Integer> numbers = Collections.of(2, 4, 6, 8);
        Assertions.assertTrue(numbers.all(n -> n % 2 == 0));
        Assertions.assertFalse(numbers.all(n -> n > 5));
    }

    @Test
    public void testAny() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4);
        Assertions.assertTrue(numbers.any());
        Assertions.assertTrue(numbers.any(n -> n > 3));
        Assertions.assertFalse(numbers.any(n -> n > 10));

        Collection<Integer> empty = Collections.empty();
        Assertions.assertFalse(empty.any());
    }

    @Test
    public void testNone() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Assertions.assertFalse(numbers.none());
        Assertions.assertTrue(numbers.none(n -> n > 10));
        Assertions.assertFalse(numbers.none(n -> n > 2));

        Collection<Integer> empty = Collections.empty();
        Assertions.assertTrue(empty.none());
    }

    @Test
    public void testCount() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(5, numbers.count());
        Assertions.assertEquals(3, numbers.count(n -> n > 2));
    }

    @Test
    public void testMin() {
        Collection<Integer> numbers = Collections.of(3, 1, 4, 1, 5);
        Assertions.assertEquals(1, numbers.min());
    }

    @Test
    public void testMax() {
        Collection<Integer> numbers = Collections.of(3, 1, 4, 1, 5);
        Assertions.assertEquals(5, numbers.max());
    }

    @Test
    public void testMinBy() {
        Collection<Pair<String, Integer>> pairs = Collections.of(
            Pair.of("a", 3),
            Pair.of("b", 1),
            Pair.of("c", 4)
        );
        Assertions.assertEquals("b", pairs.minBy(Pair::getValue).getKey());
    }

    @Test
    public void testMaxBy() {
        Collection<Pair<String, Integer>> pairs = Collections.of(
            Pair.of("a", 3),
            Pair.of("b", 1),
            Pair.of("c", 4)
        );
        Assertions.assertEquals("c", pairs.maxBy(Pair::getValue).getKey());
    }

    @Test
    public void testSumByInt() {
        Collection<Pair<String, Integer>> pairs = Collections.of(
            Pair.of("a", 1),
            Pair.of("b", 2),
            Pair.of("c", 3)
        );
        Assertions.assertEquals(6, pairs.sumByInt(Pair::getValue));
    }

    @Test
    public void testSumByLong() {
        Collection<Pair<String, Long>> pairs = Collections.of(
            Pair.of("a", 1L),
            Pair.of("b", 2L),
            Pair.of("c", 3L)
        );
        Assertions.assertEquals(6L, pairs.sumByLong(Pair::getValue));
    }

    @Test
    public void testSumByDouble() {
        Collection<Pair<String, Double>> pairs = Collections.of(
            Pair.of("a", 1.5),
            Pair.of("b", 2.5),
            Pair.of("c", 3.0)
        );
        Assertions.assertEqualsWithDelta(7.0, pairs.sumByDouble(Pair::getValue), 0.001);
    }

    @Test
    public void testAverageByDouble() {
        Collection<Pair<String, Double>> pairs = Collections.of(
            Pair.of("a", 2.0),
            Pair.of("b", 4.0),
            Pair.of("c", 6.0)
        );
        Assertions.assertEqualsWithDelta(4.0, pairs.averageByDouble(Pair::getValue), 0.001);
    }

    @Test
    public void testChunked() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7);
        Collection<List<Integer>> chunks = numbers.chunked(3);
        Assertions.assertEquals(3, chunks.size());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), chunks.first());
        Assertions.assertEquals(Arrays.asList(7), chunks.last());
    }

    @Test
    public void testChunkedWithoutPartial() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7);
        Collection<List<Integer>> chunks = numbers.chunked(3, false);
        Assertions.assertEquals(2, chunks.size());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), chunks.first());
        Assertions.assertEquals(Arrays.asList(4, 5, 6), chunks.last());
    }

    @Test
    public void testWindowed() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<List<Integer>> windows = numbers.windowed(3);
        Assertions.assertEquals(3, windows.size());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), windows.first());
        Assertions.assertEquals(Arrays.asList(3, 4, 5), windows.last());
    }

    @Test
    public void testWindowedWithStep() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<List<Integer>> windows = numbers.windowed(2, 2);
        Assertions.assertEquals(2, windows.size());
        Assertions.assertEquals(Arrays.asList(1, 2), windows.first());
        Assertions.assertEquals(Arrays.asList(3, 4), windows.last());
    }

    @Test
    public void testWindowedWithPartial() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<List<Integer>> windows = numbers.windowed(3, 2, true);
        Assertions.assertEquals(3, windows.size());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), windows.first());
        Assertions.assertEquals(Arrays.asList(5), windows.last());
    }

    @Test
    public void testFlatten() {
        Collection<Integer> flattened = Collections.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5), flattened.toList());
    }

    @Test
    public void testStream() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        long count = numbers.stream().filter(n -> n > 3).count();
        Assertions.assertEquals(2, count);
    }

    @Test
    public void testParallelStream() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        int sum = numbers.parallelStream().mapToInt(Integer::intValue).sum();
        Assertions.assertEquals(15, sum);
    }

    @Test
    public void testEqualsAndHashCode() {
        Collection<String> a = Collections.of("a", "b", "c");
        Collection<String> b = Collections.of("a", "b", "c");
        Collection<String> c = Collections.of("x", "y", "z");

        Assertions.assertEquals(a, b);
        Assertions.assertNotEquals(a, c);
        Assertions.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        Collection<String> words = Collections.of("Hello", "World");
        Assertions.assertEquals("[Hello, World]", words.toString());
    }

    @Test
    public void testPartition() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6);
        Pair<Collection<Integer>, Collection<Integer>> partitioned = numbers.partition(n -> n % 2 == 0);
        Assertions.assertEquals(Arrays.asList(2, 4, 6), partitioned.getKey().toList());
        Assertions.assertEquals(Arrays.asList(1, 3, 5), partitioned.getValue().toList());
    }

    @Test
    public void testZip() {
        Collection<String> letters = Collections.of("a", "b", "c");
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Collection<Pair<String, Integer>> zipped = letters.zip(numbers);
        Assertions.assertEquals(3, zipped.size());
        Assertions.assertEquals("a", zipped.first().getKey());
        Assertions.assertEquals(1, zipped.first().getValue());
    }

    @Test
    public void testZipWithIndex() {
        Collection<String> letters = Collections.of("a", "b", "c");
        Collection<Pair<Integer, String>> indexed = letters.zipWithIndex();
        Assertions.assertEquals(3, indexed.size());
        Assertions.assertEquals(0, indexed.first().getKey());
        Assertions.assertEquals("a", indexed.first().getValue());
        Assertions.assertEquals(2, indexed.last().getKey());
        Assertions.assertEquals("c", indexed.last().getValue());
    }

    @Test
    public void testRandom() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Integer random = numbers.random();
        Assertions.assertTrue(random >= 1 && random <= 5);
    }

    @Test
    public void testSample() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collection<Integer> sample = numbers.sample(3);
        Assertions.assertEquals(3, sample.size());
        
        Collection<Integer> sampleAll = numbers.sample(20);
        Assertions.assertEquals(10, sampleAll.size());
        
        Collection<Integer> sampleNone = numbers.sample(0);
        Assertions.assertTrue(sampleNone.isEmpty());
    }

    @Test
    public void testRotate() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> rotated = numbers.rotate(2);
        Assertions.assertEquals(Arrays.asList(4, 5, 1, 2, 3), rotated.toList());
        
        Collection<Integer> rotatedNegative = numbers.rotate(-2);
        Assertions.assertEquals(Arrays.asList(3, 4, 5, 1, 2), rotatedNegative.toList());
        
        Collection<Integer> rotatedZero = numbers.rotate(0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5), rotatedZero.toList());
    }

    @Test
    public void testPadStart() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Collection<Integer> padded = numbers.padStart(5, 0);
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 2, 3), padded.toList());
        
        Collection<Integer> paddedSame = numbers.padStart(3, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedSame.toList());
        
        Collection<Integer> paddedLess = numbers.padStart(2, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedLess.toList());
    }

    @Test
    public void testPadEnd() {
        Collection<Integer> numbers = Collections.of(1, 2, 3);
        Collection<Integer> padded = numbers.padEnd(5, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 0, 0), padded.toList());
        
        Collection<Integer> paddedSame = numbers.padEnd(3, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedSame.toList());
        
        Collection<Integer> paddedLess = numbers.padEnd(2, 0);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), paddedLess.toList());
    }

    @Test
    public void testShuffle() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Collection<Integer> shuffled = numbers.shuffle();
        Assertions.assertEquals(5, shuffled.size());
        Assertions.assertTrue(shuffled.contains(1));
        Assertions.assertTrue(shuffled.contains(2));
        Assertions.assertTrue(shuffled.contains(3));
        Assertions.assertTrue(shuffled.contains(4));
        Assertions.assertTrue(shuffled.contains(5));
    }

    @Test
    public void testShuffleWithRandom() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);
        Random random = new Random(42);
        Collection<Integer> shuffled = numbers.shuffle(random);
        Assertions.assertEquals(5, shuffled.size());
    }

    @Test
    public void testLazyCollection() {
        Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        LazyCollection<Integer> lazy = numbers.lazy();
        Assertions.assertTrue(lazy.isLazy());
        
        Collection<Integer> result = lazy
            .filter(n -> n % 2 == 0)
            .map(n -> n * 2)
            .sorted();
        
        Assertions.assertTrue(result instanceof LazyCollection);
        
        Collection<Integer> eager = result.eager();
        Assertions.assertEquals(Arrays.asList(4, 8, 12, 16, 20), eager.toList());
    }

    @Test
    public void testLazyCollectionCreation() {
        LazyCollection<Integer> lazy = LazyCollection.of(1, 2, 3);
        Assertions.assertTrue(lazy.isLazy());
        Assertions.assertEquals(3, lazy.size());
        
        List<Integer> list = Arrays.asList(4, 5, 6);
        LazyCollection<Integer> lazyFromList = LazyCollection.from(list);
        Assertions.assertTrue(lazyFromList.isLazy());
        Assertions.assertEquals(3, lazyFromList.size());
    }

    @Test
    public void testLazyChainOperations() {
        LazyCollection<Integer> lazy = LazyCollection.of(1, 2, 3, 4, 5);
        
        Collection<Integer> result = lazy
            .filter(n -> n > 2)
            .map(n -> n * 10)
            .take(2);
        
        Assertions.assertEquals(Arrays.asList(30, 40), result.toList());
    }

    @Test
    public void testLazyCollectionEqualsAndHashCode() {
        LazyCollection<Integer> lazy1 = LazyCollection.of(1, 2, 3);
        LazyCollection<Integer> lazy2 = LazyCollection.of(1, 2, 3);
        LazyCollection<Integer> lazy3 = LazyCollection.of(4, 5, 6);
        
        Assertions.assertEquals(lazy1, lazy2);
        Assertions.assertNotEquals(lazy1, lazy3);
        Assertions.assertEquals(lazy1.hashCode(), lazy2.hashCode());
    }
}
