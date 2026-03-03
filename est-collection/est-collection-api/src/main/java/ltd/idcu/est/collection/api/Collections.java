package ltd.idcu.est.collection.api;

import java.util.Arrays;
import java.util.stream.Stream;

public final class Collections {

    private static volatile CollectionProvider provider;

    private Collections() {
    }

    public interface CollectionProvider {
        <T> Collection<T> fromIterable(Iterable<T> iterable);
        <T> Collection<T> fromStream(Stream<T> stream);
        <T> Collection<T> singleton(T element);
        Collection<String> fromJson(String json);
        Collection<Object> fromYaml(String yaml);
        Collection<Object> fromXml(String xml);
        Collection<Integer> range(int start, int end, int step);
        Collection<Long> range(long start, long end, long step);
        <T> Collection<T> generate(int count, java.util.function.Supplier<T> supplier);
        <T> Collection<T> repeat(T element, int times);
    }

    public static void setProvider(CollectionProvider provider) {
        Collections.provider = provider;
    }

    public static CollectionProvider getProvider() {
        return provider;
    }

    private static CollectionProvider requireProvider() {
        CollectionProvider p = provider;
        if (p == null) {
            throw new IllegalStateException("CollectionProvider not initialized. " +
                "Please add est-collection-impl dependency and call Collections.setProvider() " +
                "or use ltd.idcu.est.collection.impl.CollectionFactory directly.");
        }
        return p;
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> of(T... elements) {
        if (elements == null || elements.length == 0) {
            return empty();
        }
        return fromIterable(Arrays.asList(elements));
    }

    public static <T> Collection<T> fromIterable(Iterable<T> iterable) {
        return requireProvider().fromIterable(iterable);
    }

    public static <T> Collection<T> fromStream(Stream<T> stream) {
        return requireProvider().fromStream(stream);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> empty() {
        return (Collection<T>) EmptyCollection.INSTANCE;
    }

    public static <T> Collection<T> singleton(T element) {
        return requireProvider().singleton(element);
    }

    public static Collection<String> fromJson(String json) {
        return requireProvider().fromJson(json);
    }

    public static Collection<Object> fromYaml(String yaml) {
        return requireProvider().fromYaml(yaml);
    }

    public static Collection<Object> fromXml(String xml) {
        return requireProvider().fromXml(xml);
    }

    public static <T> Collection<T> fromArray(T[] array) {
        if (array == null || array.length == 0) {
            return empty();
        }
        return fromIterable(Arrays.asList(array));
    }

    public static Collection<Integer> fromArray(int[] array) {
        if (array == null || array.length == 0) {
            return empty();
        }
        return fromStream(Arrays.stream(array).boxed());
    }

    public static Collection<Long> fromArray(long[] array) {
        if (array == null || array.length == 0) {
            return empty();
        }
        return fromStream(Arrays.stream(array).boxed());
    }

    public static Collection<Double> fromArray(double[] array) {
        if (array == null || array.length == 0) {
            return empty();
        }
        return fromStream(Arrays.stream(array).boxed());
    }

    public static Collection<Integer> range(int start, int end) {
        return range(start, end, 1);
    }

    public static Collection<Integer> range(int start, int end, int step) {
        return requireProvider().range(start, end, step);
    }

    public static Collection<Long> range(long start, long end) {
        return range(start, end, 1L);
    }

    public static Collection<Long> range(long start, long end, long step) {
        return requireProvider().range(start, end, step);
    }

    public static <T> Collection<T> generate(int count, java.util.function.Supplier<T> supplier) {
        return requireProvider().generate(count, supplier);
    }

    public static <T> Collection<T> repeat(T element, int times) {
        return requireProvider().repeat(element, times);
    }

    private static final class EmptyCollection<T> implements Collection<T> {

        @SuppressWarnings("rawtypes")
        private static final EmptyCollection INSTANCE = new EmptyCollection();

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isNotEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object element) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> elements) {
            return elements.isEmpty();
        }

        @Override
        public <R> Collection<R> map(java.util.function.Function<? super T, ? extends R> mapper) {
            return empty();
        }

        @Override
        public <R> Collection<R> mapIndexed(java.util.function.BiFunction<Integer, ? super T, ? extends R> mapper) {
            return empty();
        }

        @Override
        public Collection<T> filter(java.util.function.Predicate<? super T> predicate) {
            return empty();
        }

        @Override
        public Collection<T> filterIndexed(java.util.function.BiPredicate<Integer, ? super T> predicate) {
            return empty();
        }

        @Override
        public Collection<T> filterNot(java.util.function.Predicate<? super T> predicate) {
            return empty();
        }

        @Override
        public <R> Collection<R> flatMap(java.util.function.Function<? super T, ? extends Iterable<? extends R>> mapper) {
            return empty();
        }

        @Override
        public Collection<T> distinct() {
            return empty();
        }

        @Override
        public Collection<T> distinctBy(java.util.function.Function<? super T, ?> selector) {
            return empty();
        }

        @Override
        public Collection<T> take(int n) {
            return empty();
        }

        @Override
        public Collection<T> takeWhile(java.util.function.Predicate<? super T> predicate) {
            return empty();
        }

        @Override
        public Collection<T> takeLast(int n) {
            return empty();
        }

        @Override
        public Collection<T> drop(int n) {
            return empty();
        }

        @Override
        public Collection<T> dropWhile(java.util.function.Predicate<? super T> predicate) {
            return empty();
        }

        @Override
        public Collection<T> dropLast(int n) {
            return empty();
        }

        @Override
        public Collection<T> slice(int fromIndex, int toIndex) {
            return empty();
        }

        @Override
        public Collection<T> sorted() {
            return empty();
        }

        @Override
        public Collection<T> sorted(java.util.Comparator<? super T> comparator) {
            return empty();
        }

        @Override
        public <R extends Comparable<? super R>> Collection<T> sortBy(java.util.function.Function<? super T, ? extends R> selector) {
            return empty();
        }

        @Override
        public <R extends Comparable<? super R>> Collection<T> sortByDescending(java.util.function.Function<? super T, ? extends R> selector) {
            return empty();
        }

        @Override
        public Collection<T> reversed() {
            return empty();
        }

        @Override
        public Collection<T> shuffled() {
            return empty();
        }

        @Override
        public Collection<T> shuffled(java.util.Random random) {
            return empty();
        }

        @Override
        public T first() {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T firstOrNull() {
            return null;
        }

        @Override
        public T first(java.util.function.Predicate<? super T> predicate) {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T firstOrNull(java.util.function.Predicate<? super T> predicate) {
            return null;
        }

        @Override
        public T last() {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T lastOrNull() {
            return null;
        }

        @Override
        public T last(java.util.function.Predicate<? super T> predicate) {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T lastOrNull(java.util.function.Predicate<? super T> predicate) {
            return null;
        }

        @Override
        public T single() {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T singleOrNull() {
            return null;
        }

        @Override
        public T single(java.util.function.Predicate<? super T> predicate) {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T singleOrNull(java.util.function.Predicate<? super T> predicate) {
            return null;
        }

        @Override
        public T elementAt(int index) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }

        @Override
        public T elementAtOrNull(int index) {
            return null;
        }

        @Override
        public T elementAtOrElse(int index, T defaultValue) {
            return defaultValue;
        }

        @Override
        public T reduce(java.util.function.BinaryOperator<T> operation) {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T reduceOrNull(java.util.function.BinaryOperator<T> operation) {
            return null;
        }

        @Override
        public <R> R reduce(R initial, java.util.function.BiFunction<R, ? super T, R> operation) {
            return initial;
        }

        @Override
        public <R> R fold(R initial, java.util.function.BiFunction<R, ? super T, R> operation) {
            return initial;
        }

        @Override
        public <K> java.util.Map<K, Collection<T>> groupBy(java.util.function.Function<? super T, ? extends K> selector) {
            return new java.util.HashMap<>();
        }

        @Override
        public <K, V> java.util.Map<K, V> associate(java.util.function.Function<? super T, ? extends Pair<K, V>> transform) {
            return new java.util.HashMap<>();
        }

        @Override
        public <K, V> java.util.Map<K, V> associateBy(java.util.function.Function<? super T, ? extends K> keySelector, java.util.function.Function<? super T, ? extends V> valueTransform) {
            return new java.util.HashMap<>();
        }

        @Override
        public <K> java.util.Map<K, T> associateBy(java.util.function.Function<? super T, ? extends K> keySelector) {
            return new java.util.HashMap<>();
        }

        @Override
        public <K, V> java.util.Map<K, V> associateWith(java.util.function.Function<? super K, ? extends V> valueTransform) {
            return new java.util.HashMap<>();
        }

        @Override
        public Collection<T> plus(T element) {
            return singleton(element);
        }

        @Override
        public Collection<T> plusAll(Iterable<? extends T> elements) {
            return fromIterable((Iterable<T>) elements);
        }

        @Override
        public Collection<T> minus(T element) {
            return empty();
        }

        @Override
        public Collection<T> minusAll(Iterable<? extends T> elements) {
            return empty();
        }

        @Override
        public Collection<T> intersect(Iterable<? extends T> other) {
            return empty();
        }

        @Override
        public Collection<T> union(Iterable<? extends T> other) {
            return fromIterable((Iterable<T>) other);
        }

        @Override
        public Collection<T> subtract(Iterable<? extends T> other) {
            return empty();
        }

        @Override
        public <K> java.util.Map<K, Integer> eachCount(java.util.function.Function<? super T, ? extends K> selector) {
            return new java.util.HashMap<>();
        }

        @Override
        public <K> java.util.Map<K, T> toMap(java.util.function.Function<? super T, ? extends K> keySelector) {
            return new java.util.HashMap<>();
        }

        @Override
        public <K, V> java.util.Map<K, V> toMap(java.util.function.Function<? super T, ? extends K> keySelector, java.util.function.Function<? super T, ? extends V> valueSelector) {
            return new java.util.HashMap<>();
        }

        @Override
        public java.util.List<T> toList() {
            return java.util.Collections.emptyList();
        }

        @Override
        public java.util.Set<T> toSet() {
            return java.util.Collections.emptySet();
        }

        @Override
        public <K, V> java.util.Map<K, V> toMap() {
            return new java.util.HashMap<>();
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <A> A[] toArray(A[] array) {
            return array;
        }

        @Override
        public String joinToString() {
            return "[]";
        }

        @Override
        public String joinToString(CharSequence separator) {
            return "[]";
        }

        @Override
        public String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
            return prefix.toString() + postfix.toString();
        }

        @Override
        public String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, java.util.function.Function<? super T, ? extends CharSequence> transform) {
            return prefix.toString() + postfix.toString();
        }

        @Override
        public String toJson() {
            return "[]";
        }

        @Override
        public String toYaml() {
            return "[]";
        }

        @Override
        public String toXml() {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><collection></collection>";
        }

        @Override
        public boolean all(java.util.function.Predicate<? super T> predicate) {
            return true;
        }

        @Override
        public boolean any() {
            return false;
        }

        @Override
        public boolean any(java.util.function.Predicate<? super T> predicate) {
            return false;
        }

        @Override
        public boolean none() {
            return true;
        }

        @Override
        public boolean none(java.util.function.Predicate<? super T> predicate) {
            return true;
        }

        @Override
        public int count() {
            return 0;
        }

        @Override
        public int count(java.util.function.Predicate<? super T> predicate) {
            return 0;
        }

        @Override
        public <R extends Comparable<? super R>> T minBy(java.util.function.Function<? super T, ? extends R> selector) {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public <R extends Comparable<? super R>> T maxBy(java.util.function.Function<? super T, ? extends R> selector) {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public <R extends Comparable<? super R>> T minByOrNull(java.util.function.Function<? super T, ? extends R> selector) {
            return null;
        }

        @Override
        public <R extends Comparable<? super R>> T maxByOrNull(java.util.function.Function<? super T, ? extends R> selector) {
            return null;
        }

        @Override
        public T min() {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T max() {
            throw new java.util.NoSuchElementException("Collection is empty");
        }

        @Override
        public T minOrNull() {
            return null;
        }

        @Override
        public T maxOrNull() {
            return null;
        }

        @Override
        public double sumByDouble(java.util.function.Function<? super T, Double> selector) {
            return 0.0;
        }

        @Override
        public int sumByInt(java.util.function.Function<? super T, Integer> selector) {
            return 0;
        }

        @Override
        public long sumByLong(java.util.function.Function<? super T, Long> selector) {
            return 0L;
        }

        @Override
        public double averageByDouble(java.util.function.Function<? super T, Double> selector) {
            return Double.NaN;
        }

        @Override
        public Collection<T> peek(java.util.function.Consumer<? super T> action) {
            return empty();
        }

        @Override
        public Collection<T> onEach(java.util.function.Consumer<? super T> action) {
            return empty();
        }

        @Override
        public Collection<java.util.List<T>> chunked(int size) {
            return empty();
        }

        @Override
        public Collection<java.util.List<T>> chunked(int size, boolean allowPartial) {
            return empty();
        }

        @Override
        public Collection<java.util.List<T>> windowed(int size) {
            return empty();
        }

        @Override
        public Collection<java.util.List<T>> windowed(int size, int step) {
            return empty();
        }

        @Override
        public Collection<java.util.List<T>> windowed(int size, int step, boolean partialWindows) {
            return empty();
        }

        @Override
        public Collection<T> flatten() {
            return empty();
        }

        @Override
        public <R> R collect(Collector<? super T, R> collector) {
            return collector.collect(empty());
        }

        @Override
        public void forEach(java.util.function.Consumer<? super T> action) {
        }

        @Override
        public void forEachIndexed(java.util.function.BiConsumer<Integer, ? super T> action) {
        }

        @Override
        public java.util.Iterator<T> iterator() {
            return java.util.Collections.<T>emptyList().iterator();
        }

        @Override
        public java.util.Spliterator<T> spliterator() {
            return java.util.Collections.<T>emptyList().spliterator();
        }

        @Override
        public Stream<T> stream() {
            return Stream.empty();
        }

        @Override
        public Stream<T> parallelStream() {
            return Stream.empty();
        }
    }
}
