package ltd.idcu.est.collection.api;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

public interface MutableSeq<T> extends Seq<T> {

    MutableSeq<T> add(T element);

    MutableSeq<T> addAll(Iterable<? extends T> elements);

    MutableSeq<T> remove(T element);

    MutableSeq<T> removeAll(Iterable<? extends T> elements);

    MutableSeq<T> removeIf(java.util.function.Predicate<? super T> predicate);

    MutableSeq<T> replaceAll(UnaryOperator<T> operator);

    MutableSeq<T> clear();

    MutableSeq<T> set(int index, T element);

    MutableSeq<T> insert(int index, T element);

    MutableSeq<T> removeAt(int index);

    MutableSeq<T> sort();

    MutableSeq<T> sort(Comparator<? super T> comparator);

    default <R extends Comparable<? super R>> MutableSeq<T> sortBy(
        java.util.function.Function<? super T, ? extends R> selector
    ) {
        return sort(Comparator.comparing(selector));
    }

    default <R extends Comparable<? super R>> MutableSeq<T> sortByDesc(
        java.util.function.Function<? super T, ? extends R> selector
    ) {
        return sort(Comparator.comparing(selector).reversed());
    }

    MutableSeq<T> reverse();

    MutableSeq<T> shuffle();

    MutableSeq<T> shuffle(Random random);

    Seq<T> immutable();
}
