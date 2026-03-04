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
import java.util.stream.StreamSupport;

public class DefaultCollection<T> implements Collection<T> {

    private final List<T> items;

    public DefaultCollection() {
        this.items = new ArrayList<>();
    }

    public DefaultCollection(Iterable<? extends T> iterable) {
        this.items = new ArrayList<>();
        if (iterable != null) {
            for (T item : iterable) {
                this.items.add(item);
            }
        }
    }

    public DefaultCollection(Collection<? extends T> collection) {
        this.items = new ArrayList<>();
        if (collection != null) {
            for (T item : collection) {
                this.items.add(item);
            }
        }
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
    public boolean isNotEmpty() {
        return !items.isEmpty();
    }

    @Override
    public boolean contains(Object element) {
        return items.contains(element);
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        List<Object> list = new ArrayList<>();
        for (Object item : elements) {
            list.add(item);
        }
        return items.containsAll(list);
    }

    @Override
    public <R> Collection<R> map(Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : items) {
            result.add(mapper.apply(item));
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public <R> Collection<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            result.add(mapper.apply(i, items.get(i)));
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> filter(Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (predicate.test(i, items.get(i))) {
                result.add(items.get(i));
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> filterNot(Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (!predicate.test(item)) {
                result.add(item);
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public <R> Collection<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : items) {
            Iterable<? extends R> iterable = mapper.apply(item);
            if (iterable != null) {
                for (R r : iterable) {
                    result.add(r);
                }
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> distinct() {
        Set<T> seen = new LinkedHashSet<>();
        for (T item : items) {
            seen.add(item);
        }
        return new DefaultCollection<>(new ArrayList<>(seen));
    }

    @Override
    public Collection<T> distinctBy(Function<? super T, ?> selector) {
        Set<Object> seen = new HashSet<>();
        List<T> result = new ArrayList<>();
        for (T item : items) {
            Object key = selector.apply(item);
            if (seen.add(key)) {
                result.add(item);
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> take(int n) {
        if (n <= 0) {
            return new DefaultCollection<>();
        }
        int count = Math.min(n, items.size());
        return new DefaultCollection<>(new ArrayList<>(items.subList(0, count)));
    }

    @Override
    public Collection<T> takeWhile(Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (!predicate.test(item)) {
                break;
            }
            result.add(item);
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> takeLast(int n) {
        if (n <= 0) {
            return new DefaultCollection<>();
        }
        int size = items.size();
        int fromIndex = Math.max(0, size - n);
        return new DefaultCollection<>(new ArrayList<>(items.subList(fromIndex, size)));
    }

    @Override
    public Collection<T> drop(int n) {
        if (n >= items.size()) {
            return new DefaultCollection<>();
        }
        return new DefaultCollection<>(new ArrayList<>(items.subList(n, items.size())));
    }

    @Override
    public Collection<T> dropWhile(Predicate<? super T> predicate) {
        int index = 0;
        while (index < items.size() && predicate.test(items.get(index))) {
            index++;
        }
        return new DefaultCollection<>(new ArrayList<>(items.subList(index, items.size())));
    }

    @Override
    public Collection<T> dropLast(int n) {
        if (n >= items.size()) {
            return new DefaultCollection<>();
        }
        return new DefaultCollection<>(new ArrayList<>(items.subList(0, items.size() - n)));
    }

    @Override
    public Collection<T> slice(int fromIndex, int toIndex) {
        int from = Math.max(0, fromIndex);
        int to = Math.min(items.size(), toIndex);
        if (from >= to) {
            return new DefaultCollection<>();
        }
        return new DefaultCollection<>(new ArrayList<>(items.subList(from, to)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> sorted() {
        List<T> result = new ArrayList<>(items);
        result.sort((a, b) -> {
            if (a instanceof Comparable && b instanceof Comparable) {
                return ((Comparable<Object>) a).compareTo(b);
            }
            throw new ClassCastException("Elements must implement Comparable");
        });
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> sorted(Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(items);
        result.sort(comparator);
        return new DefaultCollection<>(result);
    }

    @Override
    public <R extends Comparable<? super R>> Collection<T> sortBy(Function<? super T, ? extends R> selector) {
        List<T> result = new ArrayList<>(items);
        result.sort(Comparator.comparing(selector));
        return new DefaultCollection<>(result);
    }

    @Override
    public <R extends Comparable<? super R>> Collection<T> sortByDescending(Function<? super T, ? extends R> selector) {
        List<T> result = new ArrayList<>(items);
        result.sort(Comparator.comparing(selector).reversed());
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> reversed() {
        List<T> result = new ArrayList<>(items);
        Collections.reverse(result);
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> shuffled() {
        List<T> result = new ArrayList<>(items);
        Collections.shuffle(result);
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> shuffled(Random random) {
        List<T> result = new ArrayList<>(items);
        Collections.shuffle(result, random);
        return new DefaultCollection<>(result);
    }

    @Override
    public T first() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        return items.get(0);
    }

    @Override
    public T firstOrNull() {
        return items.isEmpty() ? null : items.get(0);
    }

    @Override
    public T first(Predicate<? super T> predicate) {
        for (T item : items) {
            if (predicate.test(item)) {
                return item;
            }
        }
        throw new NoSuchElementException("No element matching predicate found");
    }

    @Override
    public T firstOrNull(Predicate<? super T> predicate) {
        for (T item : items) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public T last() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        return items.get(items.size() - 1);
    }

    @Override
    public T lastOrNull() {
        return items.isEmpty() ? null : items.get(items.size() - 1);
    }

    @Override
    public T last(Predicate<? super T> predicate) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (predicate.test(items.get(i))) {
                return items.get(i);
            }
        }
        throw new NoSuchElementException("No element matching predicate found");
    }

    @Override
    public T lastOrNull(Predicate<? super T> predicate) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (predicate.test(items.get(i))) {
                return items.get(i);
            }
        }
        return null;
    }

    @Override
    public T single() {
        int size = items.size();
        if (size == 0) {
            throw new NoSuchElementException("Collection is empty");
        }
        if (size > 1) {
            throw new IllegalArgumentException("Collection has more than one element");
        }
        return items.get(0);
    }

    @Override
    public T singleOrNull() {
        return items.size() == 1 ? items.get(0) : null;
    }

    @Override
    public T single(Predicate<? super T> predicate) {
        T result = null;
        boolean found = false;
        for (T item : items) {
            if (predicate.test(item)) {
                if (found) {
                    throw new IllegalArgumentException("More than one element matches predicate");
                }
                found = true;
                result = item;
            }
        }
        if (!found) {
            throw new NoSuchElementException("No element matching predicate found");
        }
        return result;
    }

    @Override
    public T singleOrNull(Predicate<? super T> predicate) {
        T result = null;
        for (T item : items) {
            if (predicate.test(item)) {
                if (result != null) {
                    return null;
                }
                result = item;
            }
        }
        return result;
    }

    @Override
    public T elementAt(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + items.size());
        }
        return items.get(index);
    }

    @Override
    public T elementAtOrNull(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    @Override
    public T elementAtOrElse(int index, T defaultValue) {
        if (index < 0 || index >= items.size()) {
            return defaultValue;
        }
        return items.get(index);
    }

    @Override
    public T reduce(BinaryOperator<T> operation) {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        T result = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            result = operation.apply(result, items.get(i));
        }
        return result;
    }

    @Override
    public T reduceOrNull(BinaryOperator<T> operation) {
        if (items.isEmpty()) {
            return null;
        }
        T result = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            result = operation.apply(result, items.get(i));
        }
        return result;
    }

    @Override
    public <R> R reduce(R initial, BiFunction<R, ? super T, R> operation) {
        R result = initial;
        for (T item : items) {
            result = operation.apply(result, item);
        }
        return result;
    }

    @Override
    public <R> R fold(R initial, BiFunction<R, ? super T, R> operation) {
        R result = initial;
        for (T item : items) {
            result = operation.apply(result, item);
        }
        return result;
    }

    @Override
    public <K> Map<K, Collection<T>> groupBy(Function<? super T, ? extends K> selector) {
        Map<K, List<T>> map = new LinkedHashMap<>();
        for (T item : items) {
            K key = selector.apply(item);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        Map<K, Collection<T>> result = new LinkedHashMap<>();
        for (Map.Entry<K, List<T>> entry : map.entrySet()) {
            result.put(entry.getKey(), new DefaultCollection<>(entry.getValue()));
        }
        return result;
    }

    @Override
    public <K, V> Map<K, V> associate(Function<? super T, ? extends Pair<K, V>> transform) {
        Map<K, V> result = new LinkedHashMap<>();
        for (T item : items) {
            Pair<K, V> pair = transform.apply(item);
            if (pair != null) {
                result.put(pair.getKey(), pair.getValue());
            }
        }
        return result;
    }

    @Override
    public <K, V> Map<K, V> associateBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform) {
        Map<K, V> result = new LinkedHashMap<>();
        for (T item : items) {
            K key = keySelector.apply(item);
            V value = valueTransform.apply(item);
            result.put(key, value);
        }
        return result;
    }

    @Override
    public <K> Map<K, T> associateBy(Function<? super T, ? extends K> keySelector) {
        Map<K, T> result = new LinkedHashMap<>();
        for (T item : items) {
            result.put(keySelector.apply(item), item);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> associateWith(Function<? super K, ? extends V> valueTransform) {
        Map<K, V> result = new LinkedHashMap<>();
        for (T item : items) {
            K key = (K) item;
            result.put(key, valueTransform.apply(key));
        }
        return result;
    }

    @Override
    public Collection<T> plus(T element) {
        List<T> result = new ArrayList<>(items);
        result.add(element);
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> plusAll(Iterable<? extends T> elements) {
        List<T> result = new ArrayList<>(items);
        for (T element : elements) {
            result.add(element);
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> minus(T element) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (!Objects.equals(item, element)) {
                result.add(item);
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> minusAll(Iterable<? extends T> elements) {
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
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> intersect(Iterable<? extends T> other) {
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
        return new DefaultCollection<>(result);
    }

    @Override
    public Collection<T> union(Iterable<? extends T> other) {
        Set<T> seen = new LinkedHashSet<>();
        for (T item : items) {
            seen.add(item);
        }
        for (T item : other) {
            seen.add(item);
        }
        return new DefaultCollection<>(new ArrayList<>(seen));
    }

    @Override
    public Collection<T> subtract(Iterable<? extends T> other) {
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
        return new DefaultCollection<>(result);
    }

    @Override
    public <K> Map<K, Integer> eachCount(Function<? super T, ? extends K> selector) {
        Map<K, Integer> result = new LinkedHashMap<>();
        for (T item : items) {
            K key = selector.apply(item);
            result.merge(key, 1, Integer::sum);
        }
        return result;
    }

    @Override
    public <K> Map<K, T> toMap(Function<? super T, ? extends K> keySelector) {
        Map<K, T> result = new LinkedHashMap<>();
        for (T item : items) {
            result.put(keySelector.apply(item), item);
        }
        return result;
    }

    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        Map<K, V> result = new LinkedHashMap<>();
        for (T item : items) {
            result.put(keySelector.apply(item), valueSelector.apply(item));
        }
        return result;
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
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> toMap() {
        Map<K, V> result = new LinkedHashMap<>();
        for (T item : items) {
            if (item instanceof Pair) {
                Pair<K, V> pair = (Pair<K, V>) item;
                result.put(pair.getKey(), pair.getValue());
            } else if (item instanceof Map.Entry) {
                Map.Entry<K, V> entry = (Map.Entry<K, V>) item;
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
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
    public String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, Function<? super T, ? extends CharSequence> transform) {
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
    public String toJson() {
        return JsonUtils.toJson(items);
    }

    @Override
    public String toYaml() {
        return YamlUtils.toYaml(items);
    }

    @Override
    public String toXml() {
        XmlUtils.XmlNode root = new XmlUtils.XmlNode("collection");
        for (T item : items) {
            XmlUtils.XmlNode itemNode = new XmlUtils.XmlNode("item");
            if (item != null) {
                itemNode.setTextContent(String.valueOf(item));
            }
            root.addChild(itemNode);
        }
        return XmlUtils.toXml(root);
    }

    @Override
    public boolean all(Predicate<? super T> predicate) {
        for (T item : items) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean any() {
        return !items.isEmpty();
    }

    @Override
    public boolean any(Predicate<? super T> predicate) {
        for (T item : items) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean none() {
        return items.isEmpty();
    }

    @Override
    public boolean none(Predicate<? super T> predicate) {
        for (T item : items) {
            if (predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int count() {
        return items.size();
    }

    @Override
    public int count(Predicate<? super T> predicate) {
        int count = 0;
        for (T item : items) {
            if (predicate.test(item)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public <R extends Comparable<? super R>> T minBy(Function<? super T, ? extends R> selector) {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        T min = items.get(0);
        R minValue = selector.apply(min);
        for (int i = 1; i < items.size(); i++) {
            T item = items.get(i);
            R value = selector.apply(item);
            if (value.compareTo(minValue) < 0) {
                min = item;
                minValue = value;
            }
        }
        return min;
    }

    @Override
    public <R extends Comparable<? super R>> T maxBy(Function<? super T, ? extends R> selector) {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        T max = items.get(0);
        R maxValue = selector.apply(max);
        for (int i = 1; i < items.size(); i++) {
            T item = items.get(i);
            R value = selector.apply(item);
            if (value.compareTo(maxValue) > 0) {
                max = item;
                maxValue = value;
            }
        }
        return max;
    }

    @Override
    public <R extends Comparable<? super R>> T minByOrNull(Function<? super T, ? extends R> selector) {
        if (items.isEmpty()) {
            return null;
        }
        return minBy(selector);
    }

    @Override
    public <R extends Comparable<? super R>> T maxByOrNull(Function<? super T, ? extends R> selector) {
        if (items.isEmpty()) {
            return null;
        }
        return maxBy(selector);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T min() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        T min = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T item = items.get(i);
            if (item instanceof Comparable && min instanceof Comparable) {
                if (((Comparable<Object>) min).compareTo(item) > 0) {
                    min = item;
                }
            } else {
                throw new ClassCastException("Elements must implement Comparable");
            }
        }
        return min;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T max() {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        T max = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T item = items.get(i);
            if (item instanceof Comparable && max instanceof Comparable) {
                if (((Comparable<Object>) max).compareTo(item) < 0) {
                    max = item;
                }
            } else {
                throw new ClassCastException("Elements must implement Comparable");
            }
        }
        return max;
    }

    @Override
    public T minOrNull() {
        if (items.isEmpty()) {
            return null;
        }
        return min();
    }

    @Override
    public T maxOrNull() {
        if (items.isEmpty()) {
            return null;
        }
        return max();
    }

    @Override
    public double sumByDouble(Function<? super T, Double> selector) {
        double sum = 0.0;
        for (T item : items) {
            Double value = selector.apply(item);
            if (value != null) {
                sum += value;
            }
        }
        return sum;
    }

    @Override
    public int sumByInt(Function<? super T, Integer> selector) {
        int sum = 0;
        for (T item : items) {
            Integer value = selector.apply(item);
            if (value != null) {
                sum += value;
            }
        }
        return sum;
    }

    @Override
    public long sumByLong(Function<? super T, Long> selector) {
        long sum = 0L;
        for (T item : items) {
            Long value = selector.apply(item);
            if (value != null) {
                sum += value;
            }
        }
        return sum;
    }

    @Override
    public double averageByDouble(Function<? super T, Double> selector) {
        if (items.isEmpty()) {
            return Double.NaN;
        }
        return sumByDouble(selector) / items.size();
    }

    @Override
    public Collection<T> peek(Consumer<? super T> action) {
        for (T item : items) {
            action.accept(item);
        }
        return new DefaultCollection<>(this);
    }

    @Override
    public Collection<T> onEach(Consumer<? super T> action) {
        for (T item : items) {
            action.accept(item);
        }
        return new DefaultCollection<>(this);
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
        int total = items.size();
        for (int i = 0; i < total; i += size) {
            int end = Math.min(i + size, total);
            if (!allowPartial && end - i < size) {
                break;
            }
            result.add(new ArrayList<>(items.subList(i, end)));
        }
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
        int total = items.size();
        for (int i = 0; i < total; i += step) {
            int end = i + size;
            if (end <= total) {
                result.add(new ArrayList<>(items.subList(i, end)));
            } else if (partialWindows) {
                result.add(new ArrayList<>(items.subList(i, total)));
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> flatten() {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (item instanceof Iterable) {
                for (Object nested : (Iterable<?>) item) {
                    result.add((T) nested);
                }
            } else {
                result.add(item);
            }
        }
        return new DefaultCollection<>(result);
    }

    @Override
    public <R> R collect(Collector<? super T, R> collector) {
        return collector.collect(this);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T item : items) {
            action.accept(item);
        }
    }

    @Override
    public void forEachIndexed(BiConsumer<Integer, ? super T> action) {
        for (int i = 0; i < items.size(); i++) {
            action.accept(i, items.get(i));
        }
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
    public Stream<T> stream() {
        return items.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return items.parallelStream();
    }

    @Override
    public String toString() {
        return joinToString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCollection<?> that = (DefaultCollection<?>) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
