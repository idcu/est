package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultMutableSeq<T> implements MutableSeq<T> {

    private final List<T> items;

    public DefaultMutableSeq() {
        this.items = new ArrayList<>();
    }

    public DefaultMutableSeq(List<T> items) {
        this.items = new ArrayList<>(items);
    }

    @SafeVarargs
    public DefaultMutableSeq(T... elements) {
        this.items = new ArrayList<>(Arrays.asList(elements));
    }

    private <R> DefaultSeq<R> newSeq(List<R> items) {
        return DefaultSeq.from(items);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean contains(Object element) {
        return items.contains(element);
    }

    @Override
    public MutableSeq<T> add(T element) {
        items.add(element);
        return this;
    }

    @Override
    public MutableSeq<T> addAll(Iterable<? extends T> elements) {
        if (elements instanceof java.util.Collection) {
            items.addAll((java.util.Collection<? extends T>) elements);
        } else {
            for (T element : elements) {
                items.add(element);
            }
        }
        return this;
    }

    @Override
    public MutableSeq<T> remove(T element) {
        items.remove(element);
        return this;
    }

    @Override
    public MutableSeq<T> removeAll(Iterable<? extends T> elements) {
        if (elements instanceof java.util.Collection) {
            items.removeAll((java.util.Collection<?>) elements);
        } else {
            for (T element : elements) {
                items.remove(element);
            }
        }
        return this;
    }

    @Override
    public MutableSeq<T> removeIf(Predicate<? super T> predicate) {
        items.removeIf(predicate);
        return this;
    }

    @Override
    public MutableSeq<T> replaceAll(UnaryOperator<T> operator) {
        items.replaceAll(operator);
        return this;
    }

    @Override
    public MutableSeq<T> clear() {
        items.clear();
        return this;
    }

    @Override
    public MutableSeq<T> set(int index, T element) {
        items.set(index, element);
        return this;
    }

    @Override
    public MutableSeq<T> insert(int index, T element) {
        items.add(index, element);
        return this;
    }

    @Override
    public MutableSeq<T> removeAt(int index) {
        items.remove(index);
        return this;
    }

    @Override
    public MutableSeq<T> sort() {
        items.sort(null);
        return this;
    }

    @Override
    public MutableSeq<T> sort(Comparator<? super T> comparator) {
        items.sort(comparator);
        return this;
    }

    @Override
    public MutableSeq<T> reverse() {
        Collections.reverse(items);
        return this;
    }

    @Override
    public MutableSeq<T> shuffle() {
        Collections.shuffle(items);
        return this;
    }

    @Override
    public MutableSeq<T> shuffle(Random random) {
        Collections.shuffle(items, random);
        return this;
    }

    @Override
    public Seq<T> immutable() {
        return newSeq(new ArrayList<>(items));
    }

    @Override
    public <R> Seq<R> map(Function<? super T, ? extends R> mapper) {
        return newSeq(stream().map(mapper).collect(Collectors.toList()));
    }

    @Override
    public <R> Seq<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            result.add(mapper.apply(i, items.get(i)));
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> filter(Predicate<? super T> predicate) {
        return newSeq(stream().filter(predicate).collect(Collectors.toList()));
    }

    @Override
    public Seq<T> filterNot(Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }

    @Override
    public Seq<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (predicate.test(i, items.get(i))) {
                result.add(items.get(i));
            }
        }
        return newSeq(result);
    }

    @Override
    public <R> Seq<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : items) {
            Iterable<? extends R> iterable = mapper.apply(item);
            if (iterable != null) {
                for (R r : iterable) {
                    result.add(r);
                }
            }
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> distinct() {
        return newSeq(stream().distinct().collect(Collectors.toList()));
    }

    @Override
    public Seq<T> distinctBy(Function<? super T, ?> selector) {
        Set<Object> seen = new HashSet<>();
        List<T> result = new ArrayList<>();
        for (T item : items) {
            Object key = selector.apply(item);
            if (seen.add(key)) {
                result.add(item);
            }
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> take(int n) {
        if (n <= 0) {
            return DefaultSeq.empty();
        }
        int limit = Math.min(n, items.size());
        return newSeq(new ArrayList<>(items.subList(0, limit)));
    }

    @Override
    public Seq<T> takeWhile(Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (!predicate.test(item)) {
                break;
            }
            result.add(item);
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> takeLast(int n) {
        if (n <= 0) {
            return DefaultSeq.empty();
        }
        int size = items.size();
        int from = Math.max(0, size - n);
        return newSeq(new ArrayList<>(items.subList(from, size)));
    }

    @Override
    public Seq<T> drop(int n) {
        if (n >= items.size()) {
            return DefaultSeq.empty();
        }
        return newSeq(new ArrayList<>(items.subList(n, items.size())));
    }

    @Override
    public Seq<T> dropWhile(Predicate<? super T> predicate) {
        int index = 0;
        while (index < items.size() && predicate.test(items.get(index))) {
            index++;
        }
        return newSeq(new ArrayList<>(items.subList(index, items.size())));
    }

    @Override
    public Seq<T> dropLast(int n) {
        if (n >= items.size()) {
            return DefaultSeq.empty();
        }
        return newSeq(new ArrayList<>(items.subList(0, items.size() - n)));
    }

    @Override
    public Seq<T> slice(int fromIndex, int toIndex) {
        int from = Math.max(0, fromIndex);
        int to = Math.min(items.size(), toIndex);
        if (from >= to) {
            return DefaultSeq.empty();
        }
        return newSeq(new ArrayList<>(items.subList(from, to)));
    }

    @Override
    public Seq<T> sorted() {
        List<T> result = new ArrayList<>(items);
        result.sort(null);
        return newSeq(result);
    }

    @Override
    public Seq<T> sorted(Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(items);
        result.sort(comparator);
        return newSeq(result);
    }

    @Override
    public Seq<T> reversed() {
        List<T> result = new ArrayList<>(items);
        Collections.reverse(result);
        return newSeq(result);
    }

    @Override
    public Seq<T> shuffled() {
        List<T> result = new ArrayList<>(items);
        Collections.shuffle(result);
        return newSeq(result);
    }

    @Override
    public Seq<T> shuffled(Random random) {
        List<T> result = new ArrayList<>(items);
        Collections.shuffle(result, random);
        return newSeq(result);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> operation) {
        return stream().reduce(operation);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> operation) {
        return stream().reduce(identity, operation);
    }

    @Override
    public <R> R fold(R identity, BiFunction<R, ? super T, R> operation) {
        R result = identity;
        for (T item : items) {
            result = operation.apply(result, item);
        }
        return result;
    }

    @Override
    public <K> Map<K, Seq<T>> groupBy(Function<? super T, ? extends K> selector) {
        Map<K, List<T>> groups = stream()
            .collect(Collectors.groupingBy(selector));
        Map<K, Seq<T>> result = new LinkedHashMap<>();
        for (Map.Entry<K, List<T>> entry : groups.entrySet()) {
            result.put(entry.getKey(), newSeq(entry.getValue()));
        }
        return result;
    }

    @Override
    public <K, V> Map<K, V> associateBy(
        Function<? super T, ? extends K> keySelector,
        Function<? super T, ? extends V> valueSelector
    ) {
        return stream().collect(Collectors.toMap(
            keySelector,
            valueSelector,
            (v1, v2) -> v2,
            LinkedHashMap::new
        ));
    }

    @Override
    public <K> Map<K, Long> countBy(Function<? super T, ? extends K> selector) {
        return stream().collect(Collectors.groupingBy(
            selector,
            LinkedHashMap::new,
            Collectors.counting()
        ));
    }

    @Override
    public List<T> toList() {
        return new ArrayList<>(items);
    }

    @Override
    public Set<T> toSet() {
        return new LinkedHashSet<>(items);
    }

    @Override
    public Object[] toArray() {
        return items.toArray();
    }

    @Override
    public <A> A[] toArray(A[] array) {
        return items.toArray(array);
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
    public String joinToString(
        CharSequence separator,
        CharSequence prefix,
        CharSequence postfix,
        Function<? super T, ? extends CharSequence> transform
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        boolean first = true;
        for (T item : items) {
            if (!first) {
                sb.append(separator);
            }
            first = false;
            sb.append(transform.apply(item));
        }
        sb.append(postfix);
        return sb.toString();
    }

    @Override
    public boolean all(Predicate<? super T> predicate) {
        return stream().allMatch(predicate);
    }

    @Override
    public boolean any(Predicate<? super T> predicate) {
        return stream().anyMatch(predicate);
    }

    @Override
    public boolean none(Predicate<? super T> predicate) {
        return stream().noneMatch(predicate);
    }

    @Override
    public int count() {
        return items.size();
    }

    @Override
    public int count(Predicate<? super T> predicate) {
        return (int) stream().filter(predicate).count();
    }

    @Override
    public Optional<T> random() {
        if (items.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        return Optional.of(items.get(random.nextInt(items.size())));
    }

    @Override
    public Seq<T> sample(int n) {
        if (n <= 0) {
            return DefaultSeq.empty();
        }
        if (n >= items.size()) {
            return newSeq(new ArrayList<>(items));
        }
        List<T> result = new ArrayList<>(items);
        Collections.shuffle(result);
        return newSeq(result.subList(0, n));
    }

    @Override
    public Seq<T> plus(T element) {
        List<T> result = new ArrayList<>(items);
        result.add(element);
        return newSeq(result);
    }

    @Override
    public Seq<T> plusAll(Iterable<? extends T> elements) {
        List<T> result = new ArrayList<>(items);
        for (T element : elements) {
            result.add(element);
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> minus(T element) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (!Objects.equals(item, element)) {
                result.add(item);
            }
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> minusAll(Iterable<? extends T> elements) {
        Set<T> toRemove = new HashSet<>();
        for (T element : elements) {
            toRemove.add(element);
        }
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (!toRemove.contains(item)) {
                result.add(item);
            }
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> intersect(Iterable<? extends T> other) {
        Set<T> otherSet = new HashSet<>();
        for (T item : other) {
            otherSet.add(item);
        }
        List<T> result = new ArrayList<>();
        Set<T> seen = new HashSet<>();
        for (T item : items) {
            if (otherSet.contains(item) && seen.add(item)) {
                result.add(item);
            }
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> union(Iterable<? extends T> other) {
        Set<T> seen = new LinkedHashSet<>(items);
        for (T item : other) {
            seen.add(item);
        }
        return newSeq(new ArrayList<>(seen));
    }

    @Override
    public Seq<T> diff(Iterable<? extends T> other) {
        Set<T> toRemove = new HashSet<>();
        for (T item : other) {
            toRemove.add(item);
        }
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (!toRemove.contains(item)) {
                result.add(item);
            }
        }
        return newSeq(result);
    }

    @Override
    public Seq<List<T>> chunked(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        List<List<T>> result = new ArrayList<>();
        int total = items.size();
        for (int i = 0; i < total; i += size) {
            int end = Math.min(i + size, total);
            result.add(new ArrayList<>(items.subList(i, end)));
        }
        return newSeq(result);
    }

    @Override
    public Seq<List<T>> windowed(int size) {
        return windowed(size, 1);
    }

    @Override
    public Seq<List<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    @Override
    public Seq<List<T>> windowed(int size, int step, boolean partialWindows) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be positive");
        }
        List<List<T>> result = new ArrayList<>();
        int total = items.size();
        for (int i = 0; i < total; i += step) {
            int end = i + size;
            if (end <= total) {
                result.add(new ArrayList<>(items.subList(i, end)));
            } else if (partialWindows) {
                result.add(new ArrayList<>(items.subList(i, total)));
            }
        }
        return newSeq(result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Seq<R> flatten() {
        List<R> result = new ArrayList<>();
        for (T item : items) {
            if (item instanceof Iterable) {
                for (Object nested : (Iterable<?>) item) {
                    result.add((R) nested);
                }
            } else {
                result.add((R) item);
            }
        }
        return (Seq<R>) newSeq(result);
    }

    @Override
    public <R> R collect(Collector<? super T, ?, R> collector) {
        return stream().collect(collector);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        items.forEach(action);
    }

    @Override
    public void forEachIndexed(BiConsumer<Integer, ? super T> action) {
        for (int i = 0; i < items.size(); i++) {
            action.accept(i, items.get(i));
        }
    }

    @Override
    public Seq<T> peek(Consumer<? super T> action) {
        for (T item : items) {
            action.accept(item);
        }
        return this;
    }

    @Override
    public Stream<T> stream() {
        return items.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return items.parallelStream();
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return items.spliterator();
    }

    @Override
    public Pair<Seq<T>, Seq<T>> partition(Predicate<? super T> predicate) {
        List<T> first = new ArrayList<>();
        List<T> second = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                first.add(item);
            } else {
                second.add(item);
            }
        }
        return Pair.of(newSeq(first), newSeq(second));
    }

    @Override
    public <U> Seq<Pair<T, U>> zip(Seq<U> other) {
        List<Pair<T, U>> result = new ArrayList<>();
        Iterator<T> iter1 = items.iterator();
        Iterator<U> iter2 = other.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            result.add(Pair.of(iter1.next(), iter2.next()));
        }
        return newSeq(result);
    }

    @Override
    public Seq<Pair<Integer, T>> zipWithIndex() {
        List<Pair<Integer, T>> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            result.add(Pair.of(i, items.get(i)));
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> rotate(int distance) {
        if (items.isEmpty() || distance == 0) {
            return newSeq(new ArrayList<>(items));
        }
        int size = items.size();
        int effectiveDistance = distance % size;
        if (effectiveDistance < 0) {
            effectiveDistance += size;
        }
        List<T> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int index = (i - effectiveDistance + size) % size;
            result.add(items.get(index));
        }
        return newSeq(result);
    }

    @Override
    public Seq<T> padStart(int length, T padElement) {
        if (items.size() >= length) {
            return newSeq(new ArrayList<>(items));
        }
        List<T> result = new ArrayList<>();
        int padCount = length - items.size();
        for (int i = 0; i < padCount; i++) {
            result.add(padElement);
        }
        result.addAll(items);
        return newSeq(result);
    }

    @Override
    public Seq<T> padEnd(int length, T padElement) {
        if (items.size() >= length) {
            return newSeq(new ArrayList<>(items));
        }
        List<T> result = new ArrayList<>(items);
        int padCount = length - items.size();
        for (int i = 0; i < padCount; i++) {
            result.add(padElement);
        }
        return newSeq(result);
    }

    @Override
    public MutableSeq<T> mutable() {
        return this;
    }

    @Override
    public String toString() {
        return joinToString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultMutableSeq<?> that = (DefaultMutableSeq<?>) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
