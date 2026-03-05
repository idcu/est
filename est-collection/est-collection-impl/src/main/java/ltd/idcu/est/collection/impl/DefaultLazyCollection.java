package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.*;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.yaml.YamlUtils;
import ltd.idcu.est.utils.format.xml.XmlUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DefaultLazyCollection<T> implements LazyCollection<T> {

    private interface Operation<T, R> {
        Stream<R> apply(Stream<T> stream);
    }

    private final Iterable<T> source;
    private final List<Operation<?, ?>> operations;
    private ltd.idcu.est.collection.api.Collection<T> cached;

    @SuppressWarnings("unchecked")
    public DefaultLazyCollection(Iterable<T> source) {
        this.source = source;
        this.operations = new ArrayList<>();
        this.cached = null;
    }

    private DefaultLazyCollection(Iterable<T> source, List<Operation<?, ?>> operations) {
        this.source = source;
        this.operations = operations;
        this.cached = null;
    }

    @SuppressWarnings("unchecked")
    private ltd.idcu.est.collection.api.Collection<T> evaluate() {
        if (cached != null) {
            return cached;
        }
        Stream<Object> stream = StreamSupport.stream(source.spliterator(), false).map(obj -> (Object) obj);
        for (Operation<?, ?> op : operations) {
            stream = ((Operation<Object, Object>) op).apply(stream);
        }
        List<T> result = new ArrayList<>();
        stream.forEach(item -> result.add((T) item));
        cached = new DefaultCollection<>(result, true);
        return cached;
    }

    @SuppressWarnings("unchecked")
    private <R> LazyCollection<R> addOperation(Operation<T, R> op) {
        List<Operation<?, ?>> newOps = new ArrayList<>(operations);
        newOps.add(op);
        return (LazyCollection<R>) new DefaultLazyCollection<>(source, newOps);
    }

    @Override
    public LazyCollection<T> lazy() {
        return this;
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> eager() {
        return evaluate();
    }

    @Override
    public boolean isLazy() {
        return true;
    }

    @Override
    public int size() {
        return evaluate().size();
    }

    @Override
    public boolean isEmpty() {
        return evaluate().isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        return evaluate().isNotEmpty();
    }

    @Override
    public boolean contains(Object element) {
        return evaluate().contains(element);
    }

    @Override
    public boolean containsAll(ltd.idcu.est.collection.api.Collection<?> elements) {
        return evaluate().containsAll(elements);
    }

    @Override
    public <R> ltd.idcu.est.collection.api.Collection<R> map(Function<? super T, ? extends R> mapper) {
        return addOperation(stream -> stream.map(mapper));
    }

    @Override
    public <R> ltd.idcu.est.collection.api.Collection<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return addOperation(stream -> {
            Iterator<? extends T> iterator = stream.iterator();
            List<R> result = new ArrayList<>();
            int index = 0;
            while (iterator.hasNext()) {
                result.add(mapper.apply(index++, iterator.next()));
            }
            return result.stream();
        });
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> filter(Predicate<? super T> predicate) {
        return addOperation(stream -> stream.filter(predicate));
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        return addOperation(stream -> {
            Iterator<? extends T> iterator = stream.iterator();
            List<T> result = new ArrayList<>();
            int index = 0;
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (predicate.test(index++, item)) {
                    result.add(item);
                }
            }
            return result.stream();
        });
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> filterNot(Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public <R> ltd.idcu.est.collection.api.Collection<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return addOperation(stream -> stream.flatMap(t -> {
            Iterable<? extends R> iterable = mapper.apply(t);
            return iterable != null ? StreamSupport.stream(iterable.spliterator(), false) : Stream.empty();
        }));
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> distinct() {
        return addOperation(Stream::distinct);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> distinctBy(Function<? super T, ?> selector) {
        return addOperation(stream -> {
            Set<Object> seen = new HashSet<>();
            List<T> result = new ArrayList<>();
            Iterator<? extends T> iterator = stream.iterator();
            while (iterator.hasNext()) {
                T item = iterator.next();
                Object key = selector.apply(item);
                if (seen.add(key)) {
                    result.add(item);
                }
            }
            return result.stream();
        });
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> take(int n) {
        return addOperation(stream -> stream.limit(n));
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> takeWhile(Predicate<? super T> predicate) {
        return addOperation(stream -> {
            List<T> result = new ArrayList<>();
            Iterator<? extends T> iterator = stream.iterator();
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (!predicate.test(item)) {
                    break;
                }
                result.add(item);
            }
            return result.stream();
        });
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> takeLast(int n) {
        return evaluate().takeLast(n);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> drop(int n) {
        return addOperation(stream -> stream.skip(n));
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> dropWhile(Predicate<? super T> predicate) {
        return evaluate().dropWhile(predicate);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> dropLast(int n) {
        return evaluate().dropLast(n);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> slice(int fromIndex, int toIndex) {
        return evaluate().slice(fromIndex, toIndex);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> sorted() {
        return addOperation(Stream::sorted);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> sorted(Comparator<? super T> comparator) {
        return addOperation(stream -> stream.sorted(comparator));
    }

    @Override
    public <R extends Comparable<? super R>> ltd.idcu.est.collection.api.Collection<T> sortBy(Function<? super T, ? extends R> selector) {
        return addOperation(stream -> stream.sorted(Comparator.comparing(selector)));
    }

    @Override
    public <R extends Comparable<? super R>> ltd.idcu.est.collection.api.Collection<T> sortByDescending(Function<? super T, ? extends R> selector) {
        return addOperation(stream -> stream.sorted(Comparator.comparing(selector).reversed()));
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> reversed() {
        return evaluate().reversed();
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> shuffled() {
        return evaluate().shuffled();
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> shuffled(Random random) {
        return evaluate().shuffled(random);
    }

    @Override
    public T first() {
        return evaluate().first();
    }

    @Override
    public T firstOrNull() {
        return evaluate().firstOrNull();
    }

    @Override
    public T first(Predicate<? super T> predicate) {
        return evaluate().first(predicate);
    }

    @Override
    public T firstOrNull(Predicate<? super T> predicate) {
        return evaluate().firstOrNull(predicate);
    }

    @Override
    public T last() {
        return evaluate().last();
    }

    @Override
    public T lastOrNull() {
        return evaluate().lastOrNull();
    }

    @Override
    public T last(Predicate<? super T> predicate) {
        return evaluate().last(predicate);
    }

    @Override
    public T lastOrNull(Predicate<? super T> predicate) {
        return evaluate().lastOrNull(predicate);
    }

    @Override
    public T single() {
        return evaluate().single();
    }

    @Override
    public T singleOrNull() {
        return evaluate().singleOrNull();
    }

    @Override
    public T single(Predicate<? super T> predicate) {
        return evaluate().single(predicate);
    }

    @Override
    public T singleOrNull(Predicate<? super T> predicate) {
        return evaluate().singleOrNull(predicate);
    }

    @Override
    public T elementAt(int index) {
        return evaluate().elementAt(index);
    }

    @Override
    public T elementAtOrNull(int index) {
        return evaluate().elementAtOrNull(index);
    }

    @Override
    public T elementAtOrElse(int index, T defaultValue) {
        return evaluate().elementAtOrElse(index, defaultValue);
    }

    @Override
    public T reduce(BinaryOperator<T> operation) {
        return evaluate().reduce(operation);
    }

    @Override
    public T reduceOrNull(BinaryOperator<T> operation) {
        return evaluate().reduceOrNull(operation);
    }

    @Override
    public <R> R reduce(R initial, BiFunction<R, ? super T, R> operation) {
        return evaluate().reduce(initial, operation);
    }

    @Override
    public <R> R fold(R initial, BiFunction<R, ? super T, R> operation) {
        return evaluate().fold(initial, operation);
    }

    @Override
    public <K> Map<K, ltd.idcu.est.collection.api.Collection<T>> groupBy(Function<? super T, ? extends K> selector) {
        return evaluate().groupBy(selector);
    }

    @Override
    public <K1, K2> Map<K1, Map<K2, ltd.idcu.est.collection.api.Collection<T>>> groupByMultiple(
            Function<? super T, ? extends K1> keySelector1,
            Function<? super T, ? extends K2> keySelector2) {
        return evaluate().groupByMultiple(keySelector1, keySelector2);
    }

    @Override
    public <K, A, R> Map<K, R> aggregateBy(
            Function<? super T, ? extends K> keySelector,
            Supplier<A> initialSupplier,
            BiFunction<A, ? super T, A> accumulator) {
        return evaluate().aggregateBy(keySelector, initialSupplier, accumulator);
    }

    @Override
    public <K, A, R> Map<K, R> aggregateBy(
            Function<? super T, ? extends K> keySelector,
            Supplier<A> initialSupplier,
            BiFunction<A, ? super T, A> accumulator,
            Function<A, R> finisher) {
        return evaluate().aggregateBy(keySelector, initialSupplier, accumulator, finisher);
    }

    @Override
    public <K, V> Map<K, V> associate(Function<? super T, ? extends Pair<K, V>> transform) {
        return evaluate().associate(transform);
    }

    @Override
    public <K, V> Map<K, V> associateBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform) {
        return evaluate().associateBy(keySelector, valueTransform);
    }

    @Override
    public <K> Map<K, T> associateBy(Function<? super T, ? extends K> keySelector) {
        return evaluate().associateBy(keySelector);
    }

    @Override
    public <K, V> Map<K, V> associateWith(Function<? super K, ? extends V> valueTransform) {
        return evaluate().associateWith(valueTransform);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> plus(T element) {
        return evaluate().plus(element);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> plusAll(Iterable<? extends T> elements) {
        return evaluate().plusAll(elements);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> minus(T element) {
        return evaluate().minus(element);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> minusAll(Iterable<? extends T> elements) {
        return evaluate().minusAll(elements);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> intersect(Iterable<? extends T> other) {
        return evaluate().intersect(other);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> union(Iterable<? extends T> other) {
        return evaluate().union(other);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> subtract(Iterable<? extends T> other) {
        return evaluate().subtract(other);
    }

    @Override
    public <K> Map<K, Integer> eachCount(Function<? super T, ? extends K> selector) {
        return evaluate().eachCount(selector);
    }

    @Override
    public <K> Map<K, T> toMap(Function<? super T, ? extends K> keySelector) {
        return evaluate().toMap(keySelector);
    }

    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return evaluate().toMap(keySelector, valueSelector);
    }

    @Override
    public List<T> toList() {
        return evaluate().toList();
    }

    @Override
    public Set<T> toSet() {
        return evaluate().toSet();
    }

    @Override
    public <K, V> Map<K, V> toMap() {
        return evaluate().toMap();
    }

    @Override
    public Object[] toArray() {
        return evaluate().toArray();
    }

    @Override
    public <A> A[] toArray(A[] array) {
        return evaluate().toArray(array);
    }

    @Override
    public String joinToString() {
        return evaluate().joinToString();
    }

    @Override
    public String joinToString(CharSequence separator) {
        return evaluate().joinToString(separator);
    }

    @Override
    public String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return evaluate().joinToString(separator, prefix, postfix);
    }

    @Override
    public String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, Function<? super T, ? extends CharSequence> transform) {
        return evaluate().joinToString(separator, prefix, postfix, transform);
    }

    @Override
    public String toJson() {
        return evaluate().toJson();
    }

    @Override
    public String toPrettyJson() {
        return evaluate().toPrettyJson();
    }

    @Override
    public String toPrettyJson(int indent) {
        return evaluate().toPrettyJson(indent);
    }

    @Override
    public String toYaml() {
        return evaluate().toYaml();
    }

    @Override
    public String toXml() {
        return evaluate().toXml();
    }

    @Override
    public boolean all(Predicate<? super T> predicate) {
        return evaluate().all(predicate);
    }

    @Override
    public boolean any() {
        return evaluate().any();
    }

    @Override
    public boolean any(Predicate<? super T> predicate) {
        return evaluate().any(predicate);
    }

    @Override
    public boolean none() {
        return evaluate().none();
    }

    @Override
    public boolean none(Predicate<? super T> predicate) {
        return evaluate().none(predicate);
    }

    @Override
    public int count() {
        return evaluate().count();
    }

    @Override
    public int count(Predicate<? super T> predicate) {
        return evaluate().count(predicate);
    }

    @Override
    public <R extends Comparable<? super R>> T minBy(Function<? super T, ? extends R> selector) {
        return evaluate().minBy(selector);
    }

    @Override
    public <R extends Comparable<? super R>> T maxBy(Function<? super T, ? extends R> selector) {
        return evaluate().maxBy(selector);
    }

    @Override
    public <R extends Comparable<? super R>> T minByOrNull(Function<? super T, ? extends R> selector) {
        return evaluate().minByOrNull(selector);
    }

    @Override
    public <R extends Comparable<? super R>> T maxByOrNull(Function<? super T, ? extends R> selector) {
        return evaluate().maxByOrNull(selector);
    }

    @Override
    public T min() {
        return evaluate().min();
    }

    @Override
    public T max() {
        return evaluate().max();
    }

    @Override
    public T minOrNull() {
        return evaluate().minOrNull();
    }

    @Override
    public T maxOrNull() {
        return evaluate().maxOrNull();
    }

    @Override
    public double sumByDouble(Function<? super T, Double> selector) {
        return evaluate().sumByDouble(selector);
    }

    @Override
    public int sumByInt(Function<? super T, Integer> selector) {
        return evaluate().sumByInt(selector);
    }

    @Override
    public long sumByLong(Function<? super T, Long> selector) {
        return evaluate().sumByLong(selector);
    }

    @Override
    public double averageByDouble(Function<? super T, Double> selector) {
        return evaluate().averageByDouble(selector);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> peek(Consumer<? super T> action) {
        return evaluate().peek(action);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> onEach(Consumer<? super T> action) {
        return evaluate().onEach(action);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<List<T>> chunked(int size) {
        return evaluate().chunked(size);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<List<T>> chunked(int size, boolean allowPartial) {
        return evaluate().chunked(size, allowPartial);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<List<T>> windowed(int size) {
        return evaluate().windowed(size);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<List<T>> windowed(int size, int step) {
        return evaluate().windowed(size, step);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<List<T>> windowed(int size, int step, boolean partialWindows) {
        return evaluate().windowed(size, step, partialWindows);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> flatten() {
        return evaluate().flatten();
    }

    @Override
    public <R> R collect(Collector<? super T, R> collector) {
        return evaluate().collect(collector);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        evaluate().forEach(action);
    }

    @Override
    public void forEachIndexed(BiConsumer<Integer, ? super T> action) {
        evaluate().forEachIndexed(action);
    }

    @Override
    public Iterator<T> iterator() {
        return evaluate().iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return evaluate().spliterator();
    }

    @Override
    public Stream<T> stream() {
        return evaluate().stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return evaluate().parallelStream();
    }

    @Override
    public Pair<ltd.idcu.est.collection.api.Collection<T>, ltd.idcu.est.collection.api.Collection<T>> partition(Predicate<? super T> predicate) {
        return evaluate().partition(predicate);
    }

    @Override
    public <U> ltd.idcu.est.collection.api.Collection<Pair<T, U>> zip(ltd.idcu.est.collection.api.Collection<U> other) {
        return evaluate().zip(other);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<Pair<Integer, T>> zipWithIndex() {
        return evaluate().zipWithIndex();
    }

    @Override
    public T random() {
        return evaluate().random();
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> sample(int n) {
        return evaluate().sample(n);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> rotate(int distance) {
        return evaluate().rotate(distance);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> padStart(int length, T padElement) {
        return evaluate().padStart(length, padElement);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> padEnd(int length, T padElement) {
        return evaluate().padEnd(length, padElement);
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> shuffle() {
        return evaluate().shuffle();
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> shuffle(Random random) {
        return evaluate().shuffle(random);
    }

    @Override
    public MutableCollection<T> mutable() {
        return evaluate().mutable();
    }

    @Override
    public ltd.idcu.est.collection.api.Collection<T> immutable() {
        return evaluate().immutable();
    }

    @Override
    public String toString() {
        return evaluate().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultLazyCollection<?> that = (DefaultLazyCollection<?>) o;
        return Objects.equals(evaluate(), that.evaluate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaluate());
    }
}
