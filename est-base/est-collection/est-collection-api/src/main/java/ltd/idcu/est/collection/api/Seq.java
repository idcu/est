package ltd.idcu.est.collection.api;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Seq<T> extends Iterable<T> {

    int size();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    boolean contains(Object element);

    default Optional<T> first() {
        return stream().findFirst();
    }

    default T firstOr(T defaultValue) {
        return first().orElse(defaultValue);
    }

    default T firstOrNull() {
        return first().orElse(null);
    }

    default Optional<T> first(Predicate<? super T> predicate) {
        return stream().filter(predicate).findFirst();
    }

    default T firstOr(Predicate<? super T> predicate, T defaultValue) {
        return first(predicate).orElse(defaultValue);
    }

    default T firstOrNull(Predicate<? super T> predicate) {
        return first(predicate).orElse(null);
    }

    default Optional<T> last() {
        List<T> list = toList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(list.size() - 1));
    }

    default T lastOr(T defaultValue) {
        return last().orElse(defaultValue);
    }

    default T lastOrNull() {
        return last().orElse(null);
    }

    default T get(int index) {
        return elementAt(index);
    }

    default T elementAt(int index) {
        return toList().get(index);
    }

    default Optional<T> elementAtOr(int index, T defaultValue) {
        List<T> list = toList();
        return index >= 0 && index < list.size() 
            ? Optional.of(list.get(index)) 
            : Optional.of(defaultValue);
    }

    default T elementAtOrNull(int index) {
        List<T> list = toList();
        return index >= 0 && index < list.size() ? list.get(index) : null;
    }

    <R> Seq<R> map(Function<? super T, ? extends R> mapper);

    default <R> Seq<R> pluck(Function<? super T, ? extends R> mapper) {
        return map(mapper);
    }

    <R> Seq<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper);

    Seq<T> filter(Predicate<? super T> predicate);

    default Seq<T> where(Predicate<? super T> predicate) {
        return filter(predicate);
    }

    default Seq<T> whereNot(Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }

    Seq<T> filterNot(Predicate<? super T> predicate);

    Seq<T> filterIndexed(BiPredicate<Integer, ? super T> predicate);

    <R> Seq<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper);

    Seq<T> distinct();

    Seq<T> distinctBy(Function<? super T, ?> selector);

    Seq<T> take(int n);

    Seq<T> takeWhile(Predicate<? super T> predicate);

    Seq<T> takeLast(int n);

    Seq<T> drop(int n);

    Seq<T> dropWhile(Predicate<? super T> predicate);

    Seq<T> dropLast(int n);

    Seq<T> slice(int fromIndex, int toIndex);

    Seq<T> sorted();

    Seq<T> sorted(Comparator<? super T> comparator);

    default <R extends Comparable<? super R>> Seq<T> sortBy(Function<? super T, ? extends R> selector) {
        return sorted(Comparator.comparing(selector));
    }

    default <R extends Comparable<? super R>> Seq<T> sortByDesc(Function<? super T, ? extends R> selector) {
        return sorted(Comparator.comparing(selector).reversed());
    }

    Seq<T> reversed();

    Seq<T> shuffled();

    Seq<T> shuffled(Random random);

    Optional<T> reduce(BinaryOperator<T> operation);

    T reduce(T identity, BinaryOperator<T> operation);

    <R> R fold(R identity, BiFunction<R, ? super T, R> operation);

    <K> Map<K, Seq<T>> groupBy(Function<? super T, ? extends K> selector);

    <K, V> Map<K, V> associateBy(
        Function<? super T, ? extends K> keySelector,
        Function<? super T, ? extends V> valueSelector
    );

    default <K> Map<K, T> keyBy(Function<? super T, ? extends K> keySelector) {
        return associateBy(keySelector, Function.identity());
    }

    default <K> Map<K, T> associateBy(Function<? super T, ? extends K> keySelector) {
        return keyBy(keySelector);
    }

    <K> Map<K, Long> countBy(Function<? super T, ? extends K> selector);

    List<T> toList();

    Set<T> toSet();

    default <K, V> Map<K, V> toMap(
        Function<? super T, ? extends K> keyMapper,
        Function<? super T, ? extends V> valueMapper
    ) {
        return stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    default Map<T, Long> countEach() {
        return stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    Object[] toArray();

    <A> A[] toArray(A[] array);

    <A> A[] toArray(java.util.function.IntFunction<A[]> generator);

    String joinToString();

    String joinToString(CharSequence separator);

    String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix);

    String joinToString(
        CharSequence separator,
        CharSequence prefix,
        CharSequence postfix,
        Function<? super T, ? extends CharSequence> transform
    );

    default String implode() {
        return joinToString();
    }

    default String implode(CharSequence separator) {
        return joinToString(separator);
    }

    boolean all(Predicate<? super T> predicate);

    boolean any(Predicate<? super T> predicate);

    boolean none(Predicate<? super T> predicate);

    int count();

    int count(Predicate<? super T> predicate);

    default Optional<T> min(Comparator<? super T> comparator) {
        return stream().min(comparator);
    }

    default Optional<T> max(Comparator<? super T> comparator) {
        return stream().max(comparator);
    }

    default <R extends Comparable<? super R>> Optional<T> minBy(Function<? super T, ? extends R> selector) {
        return stream().min(Comparator.comparing(selector));
    }

    default <R extends Comparable<? super R>> Optional<T> maxBy(Function<? super T, ? extends R> selector) {
        return stream().max(Comparator.comparing(selector));
    }

    Optional<T> random();

    Seq<T> sample(int n);

    Seq<T> plus(T element);

    Seq<T> plusAll(Iterable<? extends T> elements);

    Seq<T> minus(T element);

    Seq<T> minusAll(Iterable<? extends T> elements);

    Seq<T> intersect(Iterable<? extends T> other);

    Seq<T> union(Iterable<? extends T> other);

    Seq<T> diff(Iterable<? extends T> other);

    default Seq<T> except(Iterable<? extends T> other) {
        return diff(other);
    }

    Seq<List<T>> chunked(int size);

    default Seq<List<T>> chunk(int size) {
        return chunked(size);
    }

    Seq<List<T>> windowed(int size);

    Seq<List<T>> windowed(int size, int step);

    Seq<List<T>> windowed(int size, int step, boolean partialWindows);

    <R> Seq<R> flatten();

    <R> R collect(Collector<? super T, ?, R> collector);

    void forEach(Consumer<? super T> action);

    void forEachIndexed(BiConsumer<Integer, ? super T> action);

    Seq<T> peek(Consumer<? super T> action);

    default Seq<T> tap(Consumer<? super T> action) {
        return peek(action);
    }

    Stream<T> stream();

    Stream<T> parallelStream();

    Iterator<T> iterator();

    Spliterator<T> spliterator();

    Pair<Seq<T>, Seq<T>> partition(Predicate<? super T> predicate);

    <U> Seq<Pair<T, U>> zip(Seq<U> other);

    Seq<Pair<Integer, T>> zipWithIndex();

    Seq<T> rotate(int distance);

    Seq<T> padStart(int length, T padElement);

    Seq<T> padEnd(int length, T padElement);

    int indexOf(Object element);

    int lastIndexOf(Object element);

    default Seq<T> reverse() {
        return reversed();
    }

    default boolean anyMatch(Predicate<? super T> predicate) {
        return any(predicate);
    }

    default boolean allMatch(Predicate<? super T> predicate) {
        return all(predicate);
    }

    default boolean noneMatch(Predicate<? super T> predicate) {
        return none(predicate);
    }

    default String join(CharSequence separator) {
        return joinToString(separator, "", "");
    }

    default String join() {
        return joinToString("", "", "");
    }

    MutableSeq<T> mutable();

    Seq<T> immutable();
}
