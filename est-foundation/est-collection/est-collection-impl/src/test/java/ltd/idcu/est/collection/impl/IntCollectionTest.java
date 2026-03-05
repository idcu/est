package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Collections;
import ltd.idcu.est.collection.api.IntCollection;
import ltd.idcu.est.collection.api.MutableIntCollection;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.BeforeAll;
import ltd.idcu.est.test.annotation.Test;

import java.util.Random;

public class IntCollectionTest {

    @BeforeAll
    static void beforeAll() {
        CollectionInitializer.init();
    }

    @Test
    public void testCreation() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(3, coll.size());
        Assertions.assertTrue(coll.contains(1));
        Assertions.assertTrue(coll.contains(2));
        Assertions.assertTrue(coll.contains(3));

        IntCollection empty = Collections.intOf();
        Assertions.assertTrue(empty.isEmpty());

        int[] array = {4, 5, 6};
        IntCollection fromArray = Collections.intFromArray(array);
        Assertions.assertEquals(3, fromArray.size());
    }

    @Test
    public void testRange() {
        IntCollection range = Collections.intRange(1, 5);
        Assertions.assertEquals(5, range.size());
        Assertions.assertEquals(1, range.first());
        Assertions.assertEquals(5, range.last());
        Assertions.assertEquals(new int[]{1, 2, 3, 4, 5}, range.toIntArray());
    }

    @Test
    public void testGet() {
        IntCollection coll = Collections.intOf(10, 20, 30);
        Assertions.assertEquals(10, coll.get(0));
        Assertions.assertEquals(20, coll.get(1));
        Assertions.assertEquals(30, coll.get(2));
    }

    @Test
    public void testFirst() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(1, coll.first());
    }

    @Test
    public void testFirstOrNull() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(1, coll.firstOrNull());

        IntCollection empty = Collections.intOf();
        Assertions.assertEquals(0, empty.firstOrNull());
    }

    @Test
    public void testFirstWithPredicate() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Assertions.assertEquals(4, coll.first(n -> n > 3));
    }

    @Test
    public void testLast() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(3, coll.last());
    }

    @Test
    public void testLastOrNull() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(3, coll.lastOrNull());

        IntCollection empty = Collections.intOf();
        Assertions.assertEquals(0, empty.lastOrNull());
    }

    @Test
    public void testLastWithPredicate() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Assertions.assertEquals(5, coll.last(n -> n > 3));
    }

    @Test
    public void testSingle() {
        IntCollection single = Collections.intOf(42);
        Assertions.assertEquals(42, single.single());
    }

    @Test
    public void testSingleOrNull() {
        IntCollection single = Collections.intOf(42);
        Assertions.assertEquals(42, single.singleOrNull());

        IntCollection multiple = Collections.intOf(1, 2);
        Assertions.assertEquals(0, multiple.singleOrNull());

        IntCollection empty = Collections.intOf();
        Assertions.assertEquals(0, empty.singleOrNull());
    }

    @Test
    public void testElementAt() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Assertions.assertEquals(3, coll.elementAt(2));
    }

    @Test
    public void testElementAtOrNull() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(2, coll.elementAtOrNull(1));
        Assertions.assertEquals(0, coll.elementAtOrNull(10));
    }

    @Test
    public void testElementAtOrElse() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(2, coll.elementAtOrElse(1, 99));
        Assertions.assertEquals(99, coll.elementAtOrElse(10, 99));
    }

    @Test
    public void testMin() {
        IntCollection coll = Collections.intOf(3, 1, 4, 1, 5);
        Assertions.assertEquals(1, coll.min());
    }

    @Test
    public void testMax() {
        IntCollection coll = Collections.intOf(3, 1, 4, 1, 5);
        Assertions.assertEquals(5, coll.max());
    }

    @Test
    public void testMinOrNull() {
        IntCollection coll = Collections.intOf(3, 1, 4, 1, 5);
        Assertions.assertEquals(1, coll.minOrNull());

        IntCollection empty = Collections.intOf();
        Assertions.assertEquals(0, empty.minOrNull());
    }

    @Test
    public void testMaxOrNull() {
        IntCollection coll = Collections.intOf(3, 1, 4, 1, 5);
        Assertions.assertEquals(5, coll.maxOrNull());

        IntCollection empty = Collections.intOf();
        Assertions.assertEquals(0, empty.maxOrNull());
    }

    @Test
    public void testSum() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Assertions.assertEquals(15, coll.sum());
    }

    @Test
    public void testAverage() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Assertions.assertEqualsWithDelta(3.0, coll.average(), 0.001);
    }

    @Test
    public void testReduce() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Assertions.assertEquals(15, coll.reduce(Integer::sum));
    }

    @Test
    public void testReduceOrNull() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(6, coll.reduceOrNull(Integer::sum));

        IntCollection empty = Collections.intOf();
        Assertions.assertEquals(0, empty.reduceOrNull(Integer::sum));
    }

    @Test
    public void testReduceWithInitial() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals(16, coll.reduce(10, Integer::sum));
    }

    @Test
    public void testAll() {
        IntCollection coll = Collections.intOf(2, 4, 6, 8);
        Assertions.assertTrue(coll.all(n -> n % 2 == 0));
        Assertions.assertFalse(coll.all(n -> n > 5));
    }

    @Test
    public void testAny() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4);
        Assertions.assertTrue(coll.any());
        Assertions.assertTrue(coll.any(n -> n > 3));
        Assertions.assertFalse(coll.any(n -> n > 10));

        IntCollection empty = Collections.intOf();
        Assertions.assertFalse(empty.any());
    }

    @Test
    public void testNone() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertFalse(coll.none());
        Assertions.assertTrue(coll.none(n -> n > 10));
        Assertions.assertFalse(coll.none(n -> n > 2));

        IntCollection empty = Collections.intOf();
        Assertions.assertTrue(empty.none());
    }

    @Test
    public void testCount() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Assertions.assertEquals(5, coll.count());
        Assertions.assertEquals(3, coll.count(n -> n > 2));
    }

    @Test
    public void testFilter() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        IntCollection evens = coll.filter(n -> n % 2 == 0);
        Assertions.assertEquals(new int[]{2, 4, 6, 8, 10}, evens.toIntArray());
    }

    @Test
    public void testFilterNot() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection notEvens = coll.filterNot(n -> n % 2 == 0);
        Assertions.assertEquals(new int[]{1, 3, 5}, notEvens.toIntArray());
    }

    @Test
    public void testMap() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection doubled = coll.map(n -> n * 2);
        Assertions.assertEquals(new int[]{2, 4, 6, 8, 10}, doubled.toIntArray());
    }

    @Test
    public void testMapToObj() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        var mapped = coll.mapToObj(n -> "Number: " + n);
        Assertions.assertEquals(3, mapped.size());
        Assertions.assertEquals("Number: 1", mapped.first());
    }

    @Test
    public void testDistinct() {
        IntCollection coll = Collections.intOf(1, 2, 1, 3, 2);
        IntCollection unique = coll.distinct();
        Assertions.assertEquals(new int[]{1, 2, 3}, unique.toIntArray());
    }

    @Test
    public void testTake() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection first3 = coll.take(3);
        Assertions.assertEquals(new int[]{1, 2, 3}, first3.toIntArray());

        IntCollection take0 = coll.take(0);
        Assertions.assertTrue(take0.isEmpty());

        IntCollection take10 = coll.take(10);
        Assertions.assertEquals(5, take10.size());
    }

    @Test
    public void testTakeWhile() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5, 1, 2);
        IntCollection taken = coll.takeWhile(n -> n < 5);
        Assertions.assertEquals(new int[]{1, 2, 3, 4}, taken.toIntArray());
    }

    @Test
    public void testTakeLast() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection last2 = coll.takeLast(2);
        Assertions.assertEquals(new int[]{4, 5}, last2.toIntArray());
    }

    @Test
    public void testDrop() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection dropped = coll.drop(2);
        Assertions.assertEquals(new int[]{3, 4, 5}, dropped.toIntArray());
    }

    @Test
    public void testDropWhile() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection dropped = coll.dropWhile(n -> n < 3);
        Assertions.assertEquals(new int[]{3, 4, 5}, dropped.toIntArray());
    }

    @Test
    public void testDropLast() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection dropped = coll.dropLast(2);
        Assertions.assertEquals(new int[]{1, 2, 3}, dropped.toIntArray());
    }

    @Test
    public void testSlice() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection sliced = coll.slice(1, 4);
        Assertions.assertEquals(new int[]{2, 3, 4}, sliced.toIntArray());
    }

    @Test
    public void testSorted() {
        IntCollection coll = Collections.intOf(3, 1, 4, 1, 5);
        IntCollection sorted = coll.sorted();
        Assertions.assertEquals(new int[]{1, 1, 3, 4, 5}, sorted.toIntArray());
    }

    @Test
    public void testReversed() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection reversed = coll.reversed();
        Assertions.assertEquals(new int[]{5, 4, 3, 2, 1}, reversed.toIntArray());
    }

    @Test
    public void testShuffled() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection shuffled = coll.shuffled();
        Assertions.assertEquals(5, shuffled.size());
        Assertions.assertTrue(shuffled.contains(1));
        Assertions.assertTrue(shuffled.contains(2));
        Assertions.assertTrue(shuffled.contains(3));
        Assertions.assertTrue(shuffled.contains(4));
        Assertions.assertTrue(shuffled.contains(5));
    }

    @Test
    public void testShuffledWithRandom() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        Random random = new Random(42);
        IntCollection shuffled = coll.shuffled(random);
        Assertions.assertEquals(5, shuffled.size());
    }

    @Test
    public void testPlus() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        IntCollection added = coll.plus(4);
        Assertions.assertEquals(new int[]{1, 2, 3, 4}, added.toIntArray());
    }

    @Test
    public void testPlusAll() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        IntCollection added = coll.plusAll(new int[]{4, 5, 6});
        Assertions.assertEquals(new int[]{1, 2, 3, 4, 5, 6}, added.toIntArray());
    }

    @Test
    public void testMinus() {
        IntCollection coll = Collections.intOf(1, 2, 3, 2);
        IntCollection minus = coll.minus(2);
        Assertions.assertEquals(new int[]{1, 3}, minus.toIntArray());
    }

    @Test
    public void testMinusAll() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection minus = coll.minusAll(new int[]{2, 4});
        Assertions.assertEquals(new int[]{1, 3, 5}, minus.toIntArray());
    }

    @Test
    public void testIntersect() {
        IntCollection a = Collections.intOf(1, 2, 3, 4);
        IntCollection intersect = a.intersect(new int[]{3, 4, 5, 6});
        Assertions.assertEquals(new int[]{3, 4}, intersect.toIntArray());
    }

    @Test
    public void testUnion() {
        IntCollection a = Collections.intOf(1, 2, 3);
        IntCollection union = a.union(new int[]{3, 4, 5});
        Assertions.assertEquals(new int[]{1, 2, 3, 4, 5}, union.toIntArray());
    }

    @Test
    public void testSubtract() {
        IntCollection a = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection subtract = a.subtract(new int[]{3, 4});
        Assertions.assertEquals(new int[]{1, 2, 5}, subtract.toIntArray());
    }

    @Test
    public void testRotate() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        IntCollection rotated = coll.rotate(2);
        Assertions.assertEquals(new int[]{4, 5, 1, 2, 3}, rotated.toIntArray());

        IntCollection rotatedNegative = coll.rotate(-2);
        Assertions.assertEquals(new int[]{3, 4, 5, 1, 2}, rotatedNegative.toIntArray());

        IntCollection rotatedZero = coll.rotate(0);
        Assertions.assertEquals(new int[]{1, 2, 3, 4, 5}, rotatedZero.toIntArray());
    }

    @Test
    public void testPadStart() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        IntCollection padded = coll.padStart(5, 0);
        Assertions.assertEquals(new int[]{0, 0, 1, 2, 3}, padded.toIntArray());

        IntCollection paddedSame = coll.padStart(3, 0);
        Assertions.assertEquals(new int[]{1, 2, 3}, paddedSame.toIntArray());

        IntCollection paddedLess = coll.padStart(2, 0);
        Assertions.assertEquals(new int[]{1, 2, 3}, paddedLess.toIntArray());
    }

    @Test
    public void testPadEnd() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        IntCollection padded = coll.padEnd(5, 0);
        Assertions.assertEquals(new int[]{1, 2, 3, 0, 0}, padded.toIntArray());

        IntCollection paddedSame = coll.padEnd(3, 0);
        Assertions.assertEquals(new int[]{1, 2, 3}, paddedSame.toIntArray());

        IntCollection paddedLess = coll.padEnd(2, 0);
        Assertions.assertEquals(new int[]{1, 2, 3}, paddedLess.toIntArray());
    }

    @Test
    public void testRandom() {
        IntCollection coll = Collections.intOf(1, 2, 3, 4, 5);
        int random = coll.random();
        Assertions.assertTrue(random >= 1 && random <= 5);
    }

    @Test
    public void testForEach() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        int[] sum = {0};
        java.util.function.IntConsumer action = n -> sum[0] += n;
        coll.forEach(action);
        Assertions.assertEquals(6, sum[0]);
    }

    @Test
    public void testMutable() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        MutableIntCollection mutable = coll.mutable();
        
        mutable.addItem(4);
        Assertions.assertEquals(4, mutable.size());
        Assertions.assertEquals(4, mutable.last());
    }

    @Test
    public void testImmutable() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        IntCollection immutable = coll.immutable();
        Assertions.assertFalse(immutable instanceof MutableIntCollection);
    }

    @Test
    public void testEqualsAndHashCode() {
        IntCollection a = Collections.intOf(1, 2, 3);
        IntCollection b = Collections.intOf(1, 2, 3);
        IntCollection c = Collections.intOf(4, 5, 6);

        Assertions.assertEquals(a, b);
        Assertions.assertNotEquals(a, c);
        Assertions.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        IntCollection coll = Collections.intOf(1, 2, 3);
        Assertions.assertEquals("[1, 2, 3]", coll.toString());
    }
}