package ltd.idcu.est.collection.api;

import java.util.*;
import java.util.function.*;

public interface Collection<T> extends Iterable<T> {

    int size();

    boolean isEmpty();

    boolean isNotEmpty();

    boolean contains(Object element);

    boolean containsAll(Collection<?> elements);

    <R> Collection<R> map(Function<? super T, ? extends R> mapper);

    <R> Collection<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper);

    Collection<T> filter(Predicate<? super T> predicate);

    Collection<T> filterIndexed(BiPredicate<Integer, ? super T> predicate);

    Collection<T> filterNot(Predicate<? super T> predicate);

    <R> Collection<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper);

    Collection<T> distinct();

    Collection<T> distinctBy(Function<? super T, ?> selector);

    Collection<T> take(int n);

    Collection<T> takeWhile(Predicate<? super T> predicate);

    Collection<T> takeLast(int n);

    Collection<T> drop(int n);

    Collection<T> dropWhile(Predicate<? super T> predicate);

    Collection<T> dropLast(int n);

    Collection<T> slice(int fromIndex, int toIndex);

    Collection<T> sorted();

    Collection<T> sorted(Comparator<? super T> comparator);

    <R extends Comparable<? super R>> Collection<T> sortBy(Function<? super T, ? extends R> selector);

    <R extends Comparable<? super R>> Collection<T> sortByDescending(Function<? super T, ? extends R> selector);

    Collection<T> reversed();

    Collection<T> shuffled();

    Collection<T> shuffled(Random random);

    T first();

    T firstOrNull();

    T first(Predicate<? super T> predicate);

    T firstOrNull(Predicate<? super T> predicate);

    T last();

    T lastOrNull();

    T last(Predicate<? super T> predicate);

    T lastOrNull(Predicate<? super T> predicate);

    T single();

    T singleOrNull();

    T single(Predicate<? super T> predicate);

    T singleOrNull(Predicate<? super T> predicate);

    T elementAt(int index);

    T elementAtOrNull(int index);

    T elementAtOrElse(int index, T defaultValue);

    T reduce(BinaryOperator<T> operation);

    T reduceOrNull(BinaryOperator<T> operation);

    <R> R reduce(R initial, BiFunction<R, ? super T, R> operation);

    <R> R fold(R initial, BiFunction<R, ? super T, R> operation);

    <K> Map<K, Collection<T>> groupBy(Function<? super T, ? extends K> selector);

    <K, V> Map<K, V> associate(Function<? super T, ? extends Pair<K, V>> transform);

    <K, V> Map<K, V> associateBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform);

    <K> Map<K, T> associateBy(Function<? super T, ? extends K> keySelector);

    <K, V> Map<K, V> associateWith(Function<? super K, ? extends V> valueTransform);

    Collection<T> plus(T element);

    Collection<T> plusAll(Iterable<? extends T> elements);

    Collection<T> minus(T element);

    Collection<T> minusAll(Iterable<? extends T> elements);

    Collection<T> intersect(Iterable<? extends T> other);

    Collection<T> union(Iterable<? extends T> other);

    Collection<T> subtract(Iterable<? extends T> other);

    <K> Map<K, Integer> eachCount(Function<? super T, ? extends K> selector);

    <K> Map<K, T> toMap(Function<? super T, ? extends K> keySelector);

    <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector);

    List<T> toList();

    Set<T> toSet();

    <K, V> Map<K, V> toMap();

    Object[] toArray();

    <A> A[] toArray(A[] array);

    String joinToString();

    String joinToString(CharSequence separator);

    String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix);

    String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, Function<? super T, ? extends CharSequence> transform);

    String toJson();

    String toYaml();

    String toXml();

    boolean all(Predicate<? super T> predicate);

    boolean any();

    boolean any(Predicate<? super T> predicate);

    boolean none();

    boolean none(Predicate<? super T> predicate);

    int count();

    int count(Predicate<? super T> predicate);

    <R extends Comparable<? super R>> T minBy(Function<? super T, ? extends R> selector);

    <R extends Comparable<? super R>> T maxBy(Function<? super T, ? extends R> selector);

    <R extends Comparable<? super R>> T minByOrNull(Function<? super T, ? extends R> selector);

    <R extends Comparable<? super R>> T maxByOrNull(Function<? super T, ? extends R> selector);

    T min();

    T max();

    T minOrNull();

    T maxOrNull();

    double sumByDouble(Function<? super T, Double> selector);

    int sumByInt(Function<? super T, Integer> selector);

    long sumByLong(Function<? super T, Long> selector);

    double averageByDouble(Function<? super T, Double> selector);

    Collection<T> peek(Consumer<? super T> action);

    Collection<T> onEach(Consumer<? super T> action);

    Collection<List<T>> chunked(int size);

    Collection<List<T>> chunked(int size, boolean allowPartial);

    Collection<List<T>> windowed(int size);

    Collection<List<T>> windowed(int size, int step);

    Collection<List<T>> windowed(int size, int step, boolean partialWindows);

    Collection<T> flatten();

    <R> R collect(Collector<? super T, R> collector);

    void forEach(Consumer<? super T> action);

    void forEachIndexed(BiConsumer<Integer, ? super T> action);

    Iterator<T> iterator();

    Spliterator<T> spliterator();

    Stream<T> stream();

    Stream<T> parallelStream();

    static <T> Collection<T> empty() {
        return Collections.emptyList();
    }
}
