package ltd.idcu.est.utils.common;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CollectionOptimizerUtils {

    private CollectionOptimizerUtils() {
    }

    public static <T> List<T> optimizedArrayList(int initialCapacity) {
        return new ArrayList<>(Math.max(initialCapacity, 16));
    }

    public static <T> List<T> optimizedArrayList() {
        return new ArrayList<>(16);
    }

    public static <K, V> Map<K, V> optimizedHashMap(int initialCapacity) {
        return new HashMap<>(calculateInitialCapacity(initialCapacity), 0.75f);
    }

    public static <K, V> Map<K, V> optimizedHashMap() {
        return new HashMap<>(16, 0.75f);
    }

    public static <K, V> Map<K, V> optimizedConcurrentHashMap(int initialCapacity) {
        return new ConcurrentHashMap<>(calculateInitialCapacity(initialCapacity), 0.75f, 16);
    }

    public static <K, V> Map<K, V> optimizedConcurrentHashMap() {
        return new ConcurrentHashMap<>(16, 0.75f, 16);
    }

    public static <T> Set<T> optimizedHashSet(int initialCapacity) {
        return new HashSet<>(calculateInitialCapacity(initialCapacity), 0.75f);
    }

    public static <T> Set<T> optimizedHashSet() {
        return new HashSet<>(16, 0.75f);
    }

    private static int calculateInitialCapacity(int expectedSize) {
        if (expectedSize <= 0) {
            return 16;
        }
        return (int) Math.ceil(expectedSize / 0.75);
    }

    public static <T> List<T> safeSubList(List<T> list, int fromIndex, int toIndex) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        int size = list.size();
        int from = Math.max(0, fromIndex);
        int to = Math.min(size, toIndex);
        if (from >= to) {
            return Collections.emptyList();
        }
        return new ArrayList<>(list.subList(from, to));
    }

    public static <T> List<T> distinctBy(List<T> list, Function<? super T, ?> keyExtractor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return list.stream()
            .filter(item -> seen.add(keyExtractor.apply(item)))
            .collect(Collectors.toList());
    }

    public static <T> List<T> filterAndMap(List<T> list, Predicate<? super T> filter, Function<? super T, ? extends T> mapper) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = optimizedArrayList(list.size());
        for (T item : list) {
            if (filter.test(item)) {
                result.add(mapper.apply(item));
            }
        }
        return result;
    }

    public static <T> List<T> batchProcess(List<T> list, int batchSize, Function<List<T>, List<T>> processor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        if (batchSize <= 0) {
            batchSize = 1000;
        }
        List<T> result = optimizedArrayList(list.size());
        int total = list.size();
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<T> batch = list.subList(i, end);
            result.addAll(processor.apply(batch));
        }
        return result;
    }

    public static <T extends Comparable<T>> List<T> quickSort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list == null ? Collections.emptyList() : new ArrayList<>(list);
        }
        List<T> result = new ArrayList<>(list);
        quickSort(result, 0, result.size() - 1);
        return result;
    }

    private static <T extends Comparable<T>> void quickSort(List<T> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private static <T extends Comparable<T>> int partition(List<T> list, int low, int high) {
        T pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static <T> int binarySearch(List<T> list, T key, Comparator<? super T> comparator) {
        if (list == null || list.isEmpty()) {
            return -1;
        }
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = list.get(mid);
            int cmp = comparator.compare(midVal, key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    public static <T> List<T> mergeSortedLists(List<T> list1, List<T> list2, Comparator<? super T> comparator) {
        if (list1 == null || list1.isEmpty()) {
            return list2 == null ? Collections.emptyList() : new ArrayList<>(list2);
        }
        if (list2 == null || list2.isEmpty()) {
            return new ArrayList<>(list1);
        }
        List<T> result = optimizedArrayList(list1.size() + list2.size());
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            T item1 = list1.get(i);
            T item2 = list2.get(j);
            if (comparator.compare(item1, item2) <= 0) {
                result.add(item1);
                i++;
            } else {
                result.add(item2);
                j++;
            }
        }
        while (i < list1.size()) {
            result.add(list1.get(i++));
        }
        while (j < list2.size()) {
            result.add(list2.get(j++));
        }
        return result;
    }

    public static <T> List<T> deduplicatePreserveOrder(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        Set<T> seen = new LinkedHashSet<>(list);
        return new ArrayList<>(seen);
    }

    public static <K, V> Map<K, V> createLRUCache(int maxSize) {
        return new LinkedHashMap<K, V>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    public static <T> List<T> reverseList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(list);
        Collections.reverse(result);
        return result;
    }

    public static <T> List<T> rotateList(List<T> list, int distance) {
        if (list == null || list.isEmpty() || distance == 0) {
            return list == null ? Collections.emptyList() : new ArrayList<>(list);
        }
        int size = list.size();
        int effectiveDistance = distance % size;
        if (effectiveDistance < 0) {
            effectiveDistance += size;
        }
        List<T> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int index = (i - effectiveDistance + size) % size;
            result.add(list.get(index));
        }
        return result;
    }

    public static <T> List<T> findCommonElements(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return Collections.emptyList();
        }
        Set<T> set1 = new HashSet<>(list1);
        List<T> result = optimizedArrayList();
        for (T item : list2) {
            if (set1.contains(item)) {
                result.add(item);
            }
        }
        return deduplicatePreserveOrder(result);
    }

    public static <T> Map<T, Long> frequencyMap(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<T, Long> result = optimizedHashMap(list.size());
        for (T item : list) {
            result.merge(item, 1L, Long::sum);
        }
        return result;
    }

    public static <T> List<T> topNByFrequency(List<T> list, int n) {
        if (list == null || list.isEmpty() || n <= 0) {
            return Collections.emptyList();
        }
        Map<T, Long> freqMap = frequencyMap(list);
        return freqMap.entrySet().stream()
            .sorted(Map.Entry.<T, Long>comparingByValue().reversed())
            .limit(n)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public static <T> List<T> shuffleList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(list);
        Collections.shuffle(result);
        return result;
    }

    public static <T> List<T> shuffleList(List<T> list, Random random) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(list);
        Collections.shuffle(result, random);
        return result;
    }

    public static <T> boolean isSorted(List<T> list, Comparator<? super T> comparator) {
        if (list == null || list.size() <= 1) {
            return true;
        }
        for (int i = 1; i < list.size(); i++) {
            if (comparator.compare(list.get(i - 1), list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public static <T> List<T> removeDuplicatesInPlace(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        Set<T> seen = new HashSet<>();
        List<T> result = optimizedArrayList(list.size());
        for (T item : list) {
            if (seen.add(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T> List<T> partitionAndFlatten(List<T> list, int partitionSize, Function<List<T>, List<T>> partitionProcessor) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        if (partitionSize <= 0) {
            partitionSize = 100;
        }
        List<T> result = optimizedArrayList(list.size());
        int total = list.size();
        for (int i = 0; i < total; i += partitionSize) {
            int end = Math.min(i + partitionSize, total);
            List<T> partition = list.subList(i, end);
            result.addAll(partitionProcessor.apply(partition));
        }
        return result;
    }
}
