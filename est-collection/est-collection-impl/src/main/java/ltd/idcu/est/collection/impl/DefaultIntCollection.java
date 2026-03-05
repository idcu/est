package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.IntCollection;
import ltd.idcu.est.collection.api.MutableIntCollection;
import ltd.idcu.est.collection.api.Collection;

import java.util.*;
import java.util.function.*;

public class DefaultIntCollection implements MutableIntCollection {

    private final int[] items;
    private final boolean mutable;

    public DefaultIntCollection() {
        this.items = new int[0];
        this.mutable = true;
    }

    public DefaultIntCollection(boolean mutable) {
        this.items = new int[0];
        this.mutable = mutable;
    }

    public DefaultIntCollection(int[] array) {
        this(array, true);
    }

    public DefaultIntCollection(int[] array, boolean mutable) {
        this.items = Arrays.copyOf(array, array.length);
        this.mutable = mutable;
    }

    private DefaultIntCollection(int[] items, boolean internal, boolean mutable) {
        this.items = items;
        this.mutable = mutable;
    }

    private void ensureMutable() {
        if (!mutable) {
            throw new UnsupportedOperationException("This collection is immutable");
        }
    }

    @Override
    public MutableIntCollection addItem(int element) {
        ensureMutable();
        int[] newItems = Arrays.copyOf(items, items.length + 1);
        newItems[items.length] = element;
        return new DefaultIntCollection(newItems, true, mutable);
    }

    @Override
    public MutableIntCollection addAllItems(int[] elements) {
        ensureMutable();
        int[] newItems = Arrays.copyOf(items, items.length + elements.length);
        System.arraycopy(elements, 0, newItems, items.length, elements.length);
        return new DefaultIntCollection(newItems, true, mutable);
    }

    @Override
    public MutableIntCollection removeItem(int element) {
        ensureMutable();
        List<Integer> list = new ArrayList<>();
        for (int item : items) {
            if (item != element) {
                list.add(item);
            }
        }
        int[] newItems = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newItems[i] = list.get(i);
        }
        return new DefaultIntCollection(newItems, true, mutable);
    }

    @Override
    public MutableIntCollection removeAllItems(int[] elements) {
        ensureMutable();
        Set<Integer> set = new HashSet<>();
        for (int e : elements) {
            set.add(e);
        }
        List<Integer> list = new ArrayList<>();
        for (int item : items) {
            if (!set.contains(item)) {
                list.add(item);
            }
        }
        int[] newItems = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newItems[i] = list.get(i);
        }
        return new DefaultIntCollection(newItems, true, mutable);
    }

    @Override
    public MutableIntCollection clear() {
        ensureMutable();
        return new DefaultIntCollection(new int[0], true, mutable);
    }

    @Override
    public MutableIntCollection sortItems() {
        ensureMutable();
        int[] sorted = Arrays.copyOf(items, items.length);
        Arrays.sort(sorted);
        return new DefaultIntCollection(sorted, true, mutable);
    }

    @Override
    public MutableIntCollection shuffleItems() {
        ensureMutable();
        int[] shuffled = Arrays.copyOf(items, items.length);
        Random rnd = new Random();
        for (int i = shuffled.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int temp = shuffled[index];
            shuffled[index] = shuffled[i];
            shuffled[i] = temp;
        }
        return new DefaultIntCollection(shuffled, true, mutable);
    }

    @Override
    public MutableIntCollection shuffleItems(Random random) {
        ensureMutable();
        int[] shuffled = Arrays.copyOf(items, items.length);
        for (int i = shuffled.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = shuffled[index];
            shuffled[index] = shuffled[i];
            shuffled[i] = temp;
        }
        return new DefaultIntCollection(shuffled, true, mutable);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public boolean isEmpty() {
        return items.length == 0;
    }

    @Override
    public boolean isNotEmpty() {
        return items.length > 0;
    }

    @Override
    public boolean contains(Object element) {
        if (element instanceof Integer) {
            int e = (Integer) element;
            for (int item : items) {
                if (item == e) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= items.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + items.length);
        }
        return items[index];
    }

    @Override
    public int first() {
        if (items.length == 0) {
            throw new NoSuchElementException("Collection is empty");
        }
        return items[0];
    }

    @Override
    public int firstOrNull() {
        return items.length == 0 ? 0 : items[0];
    }

    @Override
    public int first(IntPredicate predicate) {
        for (int item : items) {
            if (predicate.test(item)) {
                return item;
            }
        }
        throw new NoSuchElementException("No element matching predicate found");
    }

    @Override
    public int firstOrNull(IntPredicate predicate) {
        for (int item : items) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return 0;
    }

    @Override
    public int last() {
        if (items.length == 0) {
            throw new NoSuchElementException("Collection is empty");
        }
        return items[items.length - 1];
    }

    @Override
    public int lastOrNull() {
        return items.length == 0 ? 0 : items[items.length - 1];
    }

    @Override
    public int last(IntPredicate predicate) {
        for (int i = items.length - 1; i >= 0; i--) {
            if (predicate.test(items[i])) {
                return items[i];
            }
        }
        throw new NoSuchElementException("No element matching predicate found");
    }

    @Override
    public int lastOrNull(IntPredicate predicate) {
        for (int i = items.length - 1; i >= 0; i--) {
            if (predicate.test(items[i])) {
                return items[i];
            }
        }
        return 0;
    }

    @Override
    public int single() {
        if (items.length != 1) {
            throw new IllegalStateException("Collection must contain exactly one element");
        }
        return items[0];
    }

    @Override
    public int singleOrNull() {
        return items.length == 1 ? items[0] : 0;
    }

    @Override
    public int single(IntPredicate predicate) {
        int count = 0;
        int result = 0;
        for (int item : items) {
            if (predicate.test(item)) {
                count++;
                result = item;
            }
        }
        if (count != 1) {
            throw new IllegalStateException("Collection must contain exactly one element matching predicate");
        }
        return result;
    }

    @Override
    public int singleOrNull(IntPredicate predicate) {
        int count = 0;
        int result = 0;
        for (int item : items) {
            if (predicate.test(item)) {
                count++;
                result = item;
            }
        }
        return count == 1 ? result : 0;
    }

    @Override
    public int elementAt(int index) {
        return get(index);
    }

    @Override
    public int elementAtOrNull(int index) {
        if (index < 0 || index >= items.length) {
            return 0;
        }
        return items[index];
    }

    @Override
    public int elementAtOrElse(int index, int defaultValue) {
        if (index < 0 || index >= items.length) {
            return defaultValue;
        }
        return items[index];
    }

    @Override
    public int min() {
        if (items.length == 0) {
            throw new NoSuchElementException("Collection is empty");
        }
        int min = items[0];
        for (int i = 1; i < items.length; i++) {
            if (items[i] < min) {
                min = items[i];
            }
        }
        return min;
    }

    @Override
    public int max() {
        if (items.length == 0) {
            throw new NoSuchElementException("Collection is empty");
        }
        int max = items[0];
        for (int i = 1; i < items.length; i++) {
            if (items[i] > max) {
                max = items[i];
            }
        }
        return max;
    }

    @Override
    public int minOrNull() {
        return items.length == 0 ? 0 : min();
    }

    @Override
    public int maxOrNull() {
        return items.length == 0 ? 0 : max();
    }

    @Override
    public int sum() {
        int sum = 0;
        for (int item : items) {
            sum += item;
        }
        return sum;
    }

    @Override
    public double average() {
        if (items.length == 0) {
            return Double.NaN;
        }
        return (double) sum() / items.length;
    }

    @Override
    public int reduce(IntBinaryOperator operation) {
        if (items.length == 0) {
            throw new NoSuchElementException("Collection is empty");
        }
        int result = items[0];
        for (int i = 1; i < items.length; i++) {
            result = operation.applyAsInt(result, items[i]);
        }
        return result;
    }

    @Override
    public int reduceOrNull(IntBinaryOperator operation) {
        return items.length == 0 ? 0 : reduce(operation);
    }

    @Override
    public int reduce(int initial, IntBinaryOperator operation) {
        int result = initial;
        for (int item : items) {
            result = operation.applyAsInt(result, item);
        }
        return result;
    }

    @Override
    public boolean all(IntPredicate predicate) {
        for (int item : items) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean any() {
        return items.length > 0;
    }

    @Override
    public boolean any(IntPredicate predicate) {
        for (int item : items) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean none() {
        return items.length == 0;
    }

    @Override
    public boolean none(IntPredicate predicate) {
        for (int item : items) {
            if (predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int count() {
        return items.length;
    }

    @Override
    public int count(IntPredicate predicate) {
        int count = 0;
        for (int item : items) {
            if (predicate.test(item)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int random() {
        if (items.length == 0) {
            throw new NoSuchElementException("Collection is empty");
        }
        Random random = new Random();
        return items[random.nextInt(items.length)];
    }

    @Override
    public IntCollection filter(IntPredicate predicate) {
        List<Integer> list = new ArrayList<>();
        for (int item : items) {
            if (predicate.test(item)) {
                list.add(item);
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection filterNot(IntPredicate predicate) {
        return filter(predicate.negate());
    }

    @Override
    public IntCollection map(IntUnaryOperator mapper) {
        int[] result = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = mapper.applyAsInt(items[i]);
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public <R> Collection<R> mapToObj(IntFunction<? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (int item : items) {
            result.add(mapper.apply(item));
        }
        return new DefaultCollection<>(result, mutable);
    }

    @Override
    public IntCollection distinct() {
        Set<Integer> set = new LinkedHashSet<>();
        for (int item : items) {
            set.add(item);
        }
        int[] result = new int[set.size()];
        int i = 0;
        for (int item : set) {
            result[i++] = item;
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection take(int n) {
        if (n <= 0) {
            return new DefaultIntCollection(mutable);
        }
        if (n >= items.length) {
            return new DefaultIntCollection(items, mutable);
        }
        int[] result = Arrays.copyOf(items, n);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection takeWhile(IntPredicate predicate) {
        List<Integer> list = new ArrayList<>();
        for (int item : items) {
            if (!predicate.test(item)) {
                break;
            }
            list.add(item);
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection takeLast(int n) {
        if (n <= 0) {
            return new DefaultIntCollection(mutable);
        }
        if (n >= items.length) {
            return new DefaultIntCollection(items, mutable);
        }
        int[] result = Arrays.copyOfRange(items, items.length - n, items.length);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection drop(int n) {
        if (n <= 0) {
            return new DefaultIntCollection(items, mutable);
        }
        if (n >= items.length) {
            return new DefaultIntCollection(mutable);
        }
        int[] result = Arrays.copyOfRange(items, n, items.length);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection dropWhile(IntPredicate predicate) {
        int start = 0;
        while (start < items.length && predicate.test(items[start])) {
            start++;
        }
        if (start == 0) {
            return new DefaultIntCollection(items, mutable);
        }
        if (start >= items.length) {
            return new DefaultIntCollection(mutable);
        }
        int[] result = Arrays.copyOfRange(items, start, items.length);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection dropLast(int n) {
        if (n <= 0) {
            return new DefaultIntCollection(items, mutable);
        }
        if (n >= items.length) {
            return new DefaultIntCollection(mutable);
        }
        int[] result = Arrays.copyOf(items, items.length - n);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection slice(int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > items.length) {
            toIndex = items.length;
        }
        if (fromIndex >= toIndex) {
            return new DefaultIntCollection(mutable);
        }
        int[] result = Arrays.copyOfRange(items, fromIndex, toIndex);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection sorted() {
        int[] sorted = Arrays.copyOf(items, items.length);
        Arrays.sort(sorted);
        return new DefaultIntCollection(sorted, mutable);
    }

    @Override
    public IntCollection reversed() {
        int[] reversed = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            reversed[i] = items[items.length - 1 - i];
        }
        return new DefaultIntCollection(reversed, mutable);
    }

    @Override
    public IntCollection shuffled() {
        return shuffleItems();
    }

    @Override
    public IntCollection shuffled(Random random) {
        return shuffleItems(random);
    }

    @Override
    public IntCollection shuffle() {
        return shuffleItems();
    }

    @Override
    public IntCollection shuffle(Random random) {
        return shuffleItems(random);
    }

    @Override
    public IntCollection plus(int element) {
        int[] result = Arrays.copyOf(items, items.length + 1);
        result[items.length] = element;
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection plusAll(int[] elements) {
        int[] result = Arrays.copyOf(items, items.length + elements.length);
        System.arraycopy(elements, 0, result, items.length, elements.length);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection minus(int element) {
        List<Integer> list = new ArrayList<>();
        boolean removed = false;
        for (int item : items) {
            if (!removed && item == element) {
                removed = true;
            } else {
                list.add(item);
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection minusAll(int[] elements) {
        Set<Integer> set = new HashSet<>();
        for (int e : elements) {
            set.add(e);
        }
        List<Integer> list = new ArrayList<>();
        for (int item : items) {
            if (!set.contains(item)) {
                list.add(item);
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection intersect(int[] other) {
        Set<Integer> otherSet = new HashSet<>();
        for (int e : other) {
            otherSet.add(e);
        }
        List<Integer> list = new ArrayList<>();
        for (int item : items) {
            if (otherSet.contains(item)) {
                list.add(item);
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection union(int[] other) {
        Set<Integer> set = new LinkedHashSet<>();
        for (int item : items) {
            set.add(item);
        }
        for (int e : other) {
            set.add(e);
        }
        int[] result = new int[set.size()];
        int i = 0;
        for (int item : set) {
            result[i++] = item;
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection subtract(int[] other) {
        Set<Integer> otherSet = new HashSet<>();
        for (int e : other) {
            otherSet.add(e);
        }
        List<Integer> list = new ArrayList<>();
        for (int item : items) {
            if (!otherSet.contains(item)) {
                list.add(item);
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection rotate(int distance) {
        if (items.length == 0 || distance == 0) {
            return new DefaultIntCollection(items, mutable);
        }
        int size = items.length;
        int effectiveDistance = distance % size;
        if (effectiveDistance < 0) {
            effectiveDistance += size;
        }
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            int index = (i - effectiveDistance + size) % size;
            result[i] = items[index];
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection padStart(int length, int padElement) {
        if (items.length >= length) {
            return new DefaultIntCollection(items, mutable);
        }
        int padCount = length - items.length;
        int[] result = new int[length];
        for (int i = 0; i < padCount; i++) {
            result[i] = padElement;
        }
        System.arraycopy(items, 0, result, padCount, items.length);
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public IntCollection padEnd(int length, int padElement) {
        if (items.length >= length) {
            return new DefaultIntCollection(items, mutable);
        }
        int[] result = Arrays.copyOf(items, length);
        for (int i = items.length; i < length; i++) {
            result[i] = padElement;
        }
        return new DefaultIntCollection(result, mutable);
    }

    @Override
    public int[] toIntArray() {
        return Arrays.copyOf(items, items.length);
    }

    @Override
    public void forEach(IntConsumer action) {
        for (int item : items) {
            action.accept(item);
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < items.length;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return items[index++];
            }
        };
    }

    @Override
    public IntCollection lazy() {
        return this;
    }

    @Override
    public IntCollection eager() {
        return this;
    }

    @Override
    public MutableIntCollection mutable() {
        if (mutable) {
            return this;
        }
        return new DefaultIntCollection(items, true);
    }

    @Override
    public IntCollection immutable() {
        return toImmutable();
    }

    public IntCollection toImmutable() {
        if (!mutable) {
            return this;
        }
        return new DefaultIntCollection(items, false);
    }

    @Override
    public String toString() {
        if (items.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        sb.append(items[0]);
        for (int i = 1; i < items.length; i++) {
            sb.append(", ").append(items[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultIntCollection that = (DefaultIntCollection) o;
        return Arrays.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(items);
    }
}
