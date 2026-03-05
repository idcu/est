package ltd.idcu.est.collection.api;

import java.util.function.UnaryOperator;

public interface MutableCollection<T> extends Collection<T> {

    MutableCollection<T> addItem(T element);

    MutableCollection<T> addAllItems(Iterable<? extends T> elements);

    MutableCollection<T> removeItem(T element);

    MutableCollection<T> removeAllItems(Iterable<? extends T> elements);

    MutableCollection<T> removeItemsIf(java.util.function.Predicate<? super T> predicate);

    MutableCollection<T> replaceAllItems(UnaryOperator<T> operator);

    MutableCollection<T> clearItems();

    MutableCollection<T> setItem(int index, T element);

    MutableCollection<T> insertItem(int index, T element);

    MutableCollection<T> removeItemAt(int index);

    MutableCollection<T> sortItems();

    MutableCollection<T> sortItems(java.util.Comparator<? super T> comparator);

    <R extends Comparable<? super R>> MutableCollection<T> sortItemsBy(java.util.function.Function<? super T, ? extends R> selector);

    <R extends Comparable<? super R>> MutableCollection<T> sortItemsByDescending(java.util.function.Function<? super T, ? extends R> selector);

    MutableCollection<T> reverseItems();

    MutableCollection<T> shuffleItems();

    MutableCollection<T> shuffleItems(java.util.Random random);

    Collection<T> toImmutable();

    boolean isMutable();
}
