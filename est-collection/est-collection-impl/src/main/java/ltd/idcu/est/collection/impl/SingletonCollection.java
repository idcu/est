package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.api.Collector;
import ltd.idcu.est.collection.api.Pair;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.yaml.YamlUtils;
import ltd.idcu.est.utils.format.xml.XmlUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

final class SingletonCollection<T> implements Collection<T> {

    private final T element;

    SingletonCollection(T element) {
        this.element = element;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isNotEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object element) {
        return Objects.equals(this.element, element);
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        for (Object item : elements) {
            if (!Objects.equals(this.element, item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <R> Collection<R> map(Function<? super T, ? extends R> mapper) {
        return new SingletonCollection<>(mapper.apply(element));
    }

    @Override
    public <R> Collection<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        return new SingletonCollection<>(mapper.apply(0, element));
    }

    @Override
    public Collection<T> filter(Predicate<? super T> predicate) {
        return predicate.test(element) ? this : new DefaultCollection<>();
    }

    @Override
    public Collection<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        return predicate.test(0, element) ? this : new DefaultCollection<>();
    }

    @Override
    public Collection<T> filterNot(Predicate<? super T> predicate) {
        return predicate.test(element) ? new DefaultCollection<>() : this;
    }

    @Override
    public <R> Collection<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        Iterable<? extends R> iterable = mapper.apply(element);
        if (iterable == null) {
            return new DefaultCollection<>();
        }
        return new DefaultCollection<>(iterable);
    }

    @Override
    public Collection<T> distinct() {
        return this;
    }

    @Override
    public Collection<T> distinctBy(Function<? super T, ?> selector) {
        return this;
    }

    @Override
    public Collection<T> take(int n) {
        return n > 0 ? this : new DefaultCollection<>();
    }

    @Override
    public Collection<T> takeWhile(Predicate<? super T> predicate) {
        return predicate.test(element) ? this : new DefaultCollection<>();
    }

    @Override
    public Collection<T> takeLast(int n) {
        return n > 0 ? this : new DefaultCollection<>();
    }

    @Override
    public Collection<T> drop(int n) {
        return n <= 0 ? this : new DefaultCollection<>();
    }

    @Override
    public Collection<T> dropWhile(Predicate<? super T> predicate) {
        return predicate.test(element) ? new DefaultCollection<>() : this;
    }

    @Override
    public Collection<T> dropLast(int n) {
        return n <= 0 ? this : new DefaultCollection<>();
    }

    @Override
    public Collection<T> slice(int fromIndex, int toIndex) {
        if (fromIndex <= 0 && toIndex >= 1) {
            return this;
        }
        return new DefaultCollection<>();
    }

    @Override
    public Collection<T> sorted() {
        return this;
    }

    @Override
    public Collection<T> sorted(Comparator<? super T> comparator) {
        return this;
    }

    @Override
    public <R extends Comparable<? super R>> Collection<T> sortBy(Function<? super T, ? extends R> selector) {
        return this;
    }

    @Override
    public <R extends Comparable<? super R>> Collection<T> sortByDescending(Function<? super T, ? extends R> selector) {
        return this;
    }

    @Override
    public Collection<T> reversed() {
        return this;
    }

    @Override
    public Collection<T> shuffled() {
        return this;
    }

    @Override
    public Collection<T> shuffled(Random random) {
        return this;
    }

    @Override
    public T first() {
        return element;
    }

    @Override
    public T firstOrNull() {
        return element;
    }

    @Override
    public T first(Predicate<? super T> predicate) {
        if (predicate.test(element)) {
            return element;
        }
        throw new NoSuchElementException("No element matching predicate found");
    }

    @Override
    public T firstOrNull(Predicate<? super T> predicate) {
        return predicate.test(element) ? element : null;
    }

    @Override
    public T last() {
        return element;
    }

    @Override
    public T lastOrNull() {
        return element;
    }

    @Override
    public T last(Predicate<? super T> predicate) {
        if (predicate.test(element)) {
            return element;
        }
        throw new NoSuchElementException("No element matching predicate found");
    }

    @Override
    public T lastOrNull(Predicate<? super T> predicate) {
        return predicate.test(element) ? element : null;
    }

    @Override
    public T single() {
        return element;
    }

    @Override
    public T singleOrNull() {
        return element;
    }

    @Override
    public T single(Predicate<? super T> predicate) {
        if (predicate.test(element)) {
            return element;
        }
        throw new NoSuchElementException("No element matching predicate found");
    }

    @Override
    public T singleOrNull(Predicate<? super T> predicate) {
        return predicate.test(element) ? element : null;
    }

    @Override
    public T elementAt(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 1");
        }
        return element;
    }

    @Override
    public T elementAtOrNull(int index) {
        return index == 0 ? element : null;
    }

    @Override
    public T elementAtOrElse(int index, T defaultValue) {
        return index == 0 ? element : defaultValue;
    }

    @Override
    public T reduce(BinaryOperator<T> operation) {
        return element;
    }

    @Override
    public T reduceOrNull(BinaryOperator<T> operation) {
        return element;
    }

    @Override
    public <R> R reduce(R initial, BiFunction<R, ? super T, R> operation) {
        return operation.apply(initial, element);
    }

    @Override
    public <R> R fold(R initial, BiFunction<R, ? super T, R> operation) {
        return operation.apply(initial, element);
    }

    @Override
    public <K> Map<K, Collection<T>> groupBy(Function<? super T, ? extends K> selector) {
        K key = selector.apply(element);
        Map<K, Collection<T>> result = new LinkedHashMap<>();
        result.put(key, this);
        return result;
    }

    @Override
    public <K, V> Map<K, V> associate(Function<? super T, ? extends Pair<K, V>> transform) {
        Pair<K, V> pair = transform.apply(element);
        Map<K, V> result = new LinkedHashMap<>();
        if (pair != null) {
            result.put(pair.getKey(), pair.getValue());
        }
        return result;
    }

    @Override
    public <K, V> Map<K, V> associateBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform) {
        Map<K, V> result = new LinkedHashMap<>();
        result.put(keySelector.apply(element), valueTransform.apply(element));
        return result;
    }

    @Override
    public <K> Map<K, T> associateBy(Function<? super T, ? extends K> keySelector) {
        Map<K, T> result = new LinkedHashMap<>();
        result.put(keySelector.apply(element), element);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> associateWith(Function<? super K, ? extends V> valueTransform) {
        Map<K, V> result = new LinkedHashMap<>();
        K key = (K) element;
        result.put(key, valueTransform.apply(key));
        return result;
    }

    @Override
    public Collection<T> plus(T element) {
        List<T> list = new ArrayList<>();
        list.add(this.element);
        list.add(element);
        return new DefaultCollection<>(list);
    }

    @Override
    public Collection<T> plusAll(Iterable<? extends T> elements) {
        List<T> list = new ArrayList<>();
        list.add(element);
        for (T item : elements) {
            list.add(item);
        }
        return new DefaultCollection<>(list);
    }

    @Override
    public Collection<T> minus(T element) {
        return Objects.equals(this.element, element) ? new DefaultCollection<>() : this;
    }

    @Override
    public Collection<T> minusAll(Iterable<? extends T> elements) {
        for (T item : elements) {
            if (Objects.equals(this.element, item)) {
                return new DefaultCollection<>();
            }
        }
        return this;
    }

    @Override
    public Collection<T> intersect(Iterable<? extends T> other) {
        for (T item : other) {
            if (Objects.equals(this.element, item)) {
                return this;
            }
        }
        return new DefaultCollection<>();
    }

    @Override
    public Collection<T> union(Iterable<? extends T> other) {
        Set<T> set = new LinkedHashSet<>();
        set.add(element);
        for (T item : other) {
            set.add(item);
        }
        return new DefaultCollection<>(new ArrayList<>(set));
    }

    @Override
    public Collection<T> subtract(Iterable<? extends T> other) {
        for (T item : other) {
            if (Objects.equals(this.element, item)) {
                return new DefaultCollection<>();
            }
        }
        return this;
    }

    @Override
    public <K> Map<K, Integer> eachCount(Function<? super T, ? extends K> selector) {
        K key = selector.apply(element);
        Map<K, Integer> result = new LinkedHashMap<>();
        result.put(key, 1);
        return result;
    }

    @Override
    public <K> Map<K, T> toMap(Function<? super T, ? extends K> keySelector) {
        Map<K, T> result = new LinkedHashMap<>();
        result.put(keySelector.apply(element), element);
        return result;
    }

    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        Map<K, V> result = new LinkedHashMap<>();
        result.put(keySelector.apply(element), valueSelector.apply(element));
        return result;
    }

    @Override
    public List<T> toList() {
        return Collections.singletonList(element);
    }

    @Override
    public Set<T> toSet() {
        return Collections.singleton(element);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> toMap() {
        if (element instanceof Pair) {
            Pair<K, V> pair = (Pair<K, V>) element;
            Map<K, V> result = new LinkedHashMap<>();
            result.put(pair.getKey(), pair.getValue());
            return result;
        } else if (element instanceof Map.Entry) {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) element;
            Map<K, V> result = new LinkedHashMap<>();
            result.put(entry.getKey(), entry.getValue());
            return result;
        }
        return new LinkedHashMap<>();
    }

    @Override
    public Object[] toArray() {
        return new Object[]{element};
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> A[] toArray(A[] array) {
        if (array.length < 1) {
            array = (A[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), 1);
        }
        array[0] = (A) element;
        if (array.length > 1) {
            array[1] = null;
        }
        return array;
    }

    @Override
    public String joinToString() {
        return joinToString(", ");
    }

    @Override
    public String joinToString(CharSequence separator) {
        return joinToString(separator, "[", "]");
    }

    @Override
    public String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinToString(separator, prefix, postfix, Object::toString);
    }

    @Override
    public String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, Function<? super T, ? extends CharSequence> transform) {
        return prefix.toString() + transform.apply(element) + postfix;
    }

    @Override
    public String toJson() {
        return JsonUtils.toJson(Collections.singletonList(element));
    }

    @Override
    public String toYaml() {
        return YamlUtils.toYaml(Collections.singletonList(element));
    }

    @Override
    public String toXml() {
        XmlUtils.XmlNode root = new XmlUtils.XmlNode("collection");
        XmlUtils.XmlNode itemNode = new XmlUtils.XmlNode("item");
        if (element != null) {
            itemNode.setTextContent(String.valueOf(element));
        }
        root.addChild(itemNode);
        return XmlUtils.toXml(root);
    }

    @Override
    public boolean all(Predicate<? super T> predicate) {
        return predicate.test(element);
    }

    @Override
    public boolean any() {
        return true;
    }

    @Override
    public boolean any(Predicate<? super T> predicate) {
        return predicate.test(element);
    }

    @Override
    public boolean none() {
        return false;
    }

    @Override
    public boolean none(Predicate<? super T> predicate) {
        return !predicate.test(element);
    }

    @Override
    public int count() {
        return 1;
    }

    @Override
    public int count(Predicate<? super T> predicate) {
        return predicate.test(element) ? 1 : 0;
    }

    @Override
    public <R extends Comparable<? super R>> T minBy(Function<? super T, ? extends R> selector) {
        return element;
    }

    @Override
    public <R extends Comparable<? super R>> T maxBy(Function<? super T, ? extends R> selector) {
        return element;
    }

    @Override
    public <R extends Comparable<? super R>> T minByOrNull(Function<? super T, ? extends R> selector) {
        return element;
    }

    @Override
    public <R extends Comparable<? super R>> T maxByOrNull(Function<? super T, ? extends R> selector) {
        return element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T min() {
        return element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T max() {
        return element;
    }

    @Override
    public T minOrNull() {
        return element;
    }

    @Override
    public T maxOrNull() {
        return element;
    }

    @Override
    public double sumByDouble(Function<? super T, Double> selector) {
        Double value = selector.apply(element);
        return value != null ? value : 0.0;
    }

    @Override
    public int sumByInt(Function<? super T, Integer> selector) {
        Integer value = selector.apply(element);
        return value != null ? value : 0;
    }

    @Override
    public long sumByLong(Function<? super T, Long> selector) {
        Long value = selector.apply(element);
        return value != null ? value : 0L;
    }

    @Override
    public double averageByDouble(Function<? super T, Double> selector) {
        Double value = selector.apply(element);
        return value != null ? value : Double.NaN;
    }

    @Override
    public Collection<T> peek(Consumer<? super T> action) {
        action.accept(element);
        return new SingletonCollection<>(element);
    }

    @Override
    public Collection<T> onEach(Consumer<? super T> action) {
        action.accept(element);
        return new SingletonCollection<>(element);
    }

    @Override
    public Collection<List<T>> chunked(int size) {
        return chunked(size, true);
    }

    @Override
    public Collection<List<T>> chunked(int size, boolean allowPartial) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        List<List<T>> result = new ArrayList<>();
        result.add(Collections.singletonList(element));
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<List<T>> windowed(int size) {
        return windowed(size, 1);
    }

    @Override
    public Collection<List<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    @Override
    public Collection<List<T>> windowed(int size, int step, boolean partialWindows) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be positive");
        }
        List<List<T>> result = new ArrayList<>();
        if (size == 1) {
            result.add(Collections.singletonList(element));
        } else if (partialWindows) {
            result.add(Collections.singletonList(element));
        }
        return new DefaultCollection<>(result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> flatten() {
        if (element instanceof Iterable) {
            List<T> result = new ArrayList<>();
            for (Object nested : (Iterable<?>) element) {
                result.add((T) nested);
            }
            return new DefaultCollection<>(result);
        }
        return this;
    }

    @Override
    public <R> R collect(Collector<? super T, R> collector) {
        return collector.collect(this);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        action.accept(element);
    }

    @Override
    public void forEachIndexed(BiConsumer<Integer, ? super T> action) {
        action.accept(0, element);
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.singleton(element).iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return Collections.singleton(element).spliterator();
    }

    @Override
    public Stream<T> stream() {
        return Stream.of(element);
    }

    @Override
    public Stream<T> parallelStream() {
        return Stream.of(element);
    }

    @Override
    public String toString() {
        return "[" + element + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingletonCollection<?> that = (SingletonCollection<?>) o;
        return Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element);
    }
}
