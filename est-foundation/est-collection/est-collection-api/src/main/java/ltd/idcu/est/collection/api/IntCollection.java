package ltd.idcu.est.collection.api;

import java.util.Random;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public interface IntCollection extends PrimitiveCollection<Integer> {

    int get(int index);

    int first();

    int firstOrNull();

    int first(IntPredicate predicate);

    int firstOrNull(IntPredicate predicate);

    int last();

    int lastOrNull();

    int last(IntPredicate predicate);

    int lastOrNull(IntPredicate predicate);

    int single();

    int singleOrNull();

    int single(IntPredicate predicate);

    int singleOrNull(IntPredicate predicate);

    int elementAt(int index);

    int elementAtOrNull(int index);

    int elementAtOrElse(int index, int defaultValue);

    int min();

    int max();

    int minOrNull();

    int maxOrNull();

    int sum();

    double average();

    int reduce(IntBinaryOperator operation);

    int reduceOrNull(IntBinaryOperator operation);

    int reduce(int initial, IntBinaryOperator operation);

    boolean all(IntPredicate predicate);

    boolean any();

    boolean any(IntPredicate predicate);

    boolean none();

    boolean none(IntPredicate predicate);

    int count();

    int count(IntPredicate predicate);

    int random();

    IntCollection filter(IntPredicate predicate);

    IntCollection filterNot(IntPredicate predicate);

    IntCollection map(IntUnaryOperator mapper);

    <R> Collection<R> mapToObj(IntFunction<? extends R> mapper);

    IntCollection distinct();

    IntCollection take(int n);

    IntCollection takeWhile(IntPredicate predicate);

    IntCollection takeLast(int n);

    IntCollection drop(int n);

    IntCollection dropWhile(IntPredicate predicate);

    IntCollection dropLast(int n);

    IntCollection slice(int fromIndex, int toIndex);

    IntCollection sorted();

    IntCollection reversed();

    IntCollection shuffled();

    IntCollection shuffled(Random random);

    IntCollection shuffle();

    IntCollection shuffle(Random random);

    IntCollection plus(int element);

    IntCollection plusAll(int[] elements);

    IntCollection minus(int element);

    IntCollection minusAll(int[] elements);

    IntCollection intersect(int[] other);

    IntCollection union(int[] other);

    IntCollection subtract(int[] other);

    IntCollection rotate(int distance);

    IntCollection padStart(int length, int padElement);

    IntCollection padEnd(int length, int padElement);

    int[] toIntArray();

    void forEach(IntConsumer action);

    IntCollection lazy();

    IntCollection eager();

    MutableIntCollection mutable();

    IntCollection immutable();
}
