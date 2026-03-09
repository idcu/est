package ltd.idcu.est.utils.common;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionOptimizerUtilsTest {

    @Test
    public void testOptimizedArrayList() {
        List<String> list = CollectionOptimizerUtils.optimizedArrayList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testOptimizedArrayListWithCapacity() {
        List<String> list = CollectionOptimizerUtils.optimizedArrayList(100);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testOptimizedHashMap() {
        Map<String, Integer> map = CollectionOptimizerUtils.optimizedHashMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testOptimizedHashMapWithCapacity() {
        Map<String, Integer> map = CollectionOptimizerUtils.optimizedHashMap(100);
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testOptimizedConcurrentHashMap() {
        Map<String, Integer> map = CollectionOptimizerUtils.optimizedConcurrentHashMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testOptimizedHashSet() {
        Set<String> set = CollectionOptimizerUtils.optimizedHashSet();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testSafeSubList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> subList = CollectionOptimizerUtils.safeSubList(list, 1, 4);
        assertEquals(Arrays.asList(2, 3, 4), subList);
    }

    @Test
    public void testSafeSubListOutOfBounds() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> subList = CollectionOptimizerUtils.safeSubList(list, 10, 20);
        assertTrue(subList.isEmpty());
    }

    @Test
    public void testSafeSubListNull() {
        List<Integer> subList = CollectionOptimizerUtils.safeSubList(null, 0, 10);
        assertTrue(subList.isEmpty());
    }

    @Test
    public void testDistinctBy() {
        List<String> list = Arrays.asList("apple", "banana", "apple", "cherry", "banana");
        List<String> result = CollectionOptimizerUtils.distinctBy(list, s -> s);
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testDistinctByEmpty() {
        List<String> result = CollectionOptimizerUtils.distinctBy(Collections.emptyList(), s -> s);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterAndMap() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> result = CollectionOptimizerUtils.filterAndMap(
            list,
            n -> n % 2 == 0,
            n -> n * 2
        );
        assertEquals(Arrays.asList(4, 8, 12), result);
    }

    @Test
    public void testBatchProcess() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> result = CollectionOptimizerUtils.batchProcess(
            list,
            3,
            batch -> batch.stream().map(n -> n * 2).toList()
        );
        assertEquals(Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18, 20), result);
    }

    @Test
    public void testQuickSort() {
        List<Integer> list = Arrays.asList(5, 2, 8, 1, 9, 3);
        List<Integer> sorted = CollectionOptimizerUtils.quickSort(list);
        assertEquals(Arrays.asList(1, 2, 3, 5, 8, 9), sorted);
    }

    @Test
    public void testQuickSortEmpty() {
        List<Integer> sorted = CollectionOptimizerUtils.quickSort(Collections.emptyList());
        assertTrue(sorted.isEmpty());
    }

    @Test
    public void testBinarySearch() {
        List<Integer> list = Arrays.asList(1, 3, 5, 7, 9);
        int index = CollectionOptimizerUtils.binarySearch(list, 5, Integer::compareTo);
        assertEquals(2, index);
    }

    @Test
    public void testBinarySearchNotFound() {
        List<Integer> list = Arrays.asList(1, 3, 5, 7, 9);
        int index = CollectionOptimizerUtils.binarySearch(list, 6, Integer::compareTo);
        assertTrue(index < 0);
    }

    @Test
    public void testMergeSortedLists() {
        List<Integer> list1 = Arrays.asList(1, 3, 5, 7);
        List<Integer> list2 = Arrays.asList(2, 4, 6, 8);
        List<Integer> merged = CollectionOptimizerUtils.mergeSortedLists(list1, list2, Integer::compareTo);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), merged);
    }

    @Test
    public void testDeduplicatePreserveOrder() {
        List<Integer> list = Arrays.asList(1, 2, 1, 3, 2, 4);
        List<Integer> deduplicated = CollectionOptimizerUtils.deduplicatePreserveOrder(list);
        assertEquals(Arrays.asList(1, 2, 3, 4), deduplicated);
    }

    @Test
    public void testCreateLRUCache() {
        Map<String, Integer> cache = CollectionOptimizerUtils.createLRUCache(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        cache.put("d", 4);
        assertFalse(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
    }

    @Test
    public void testReverseList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> reversed = CollectionOptimizerUtils.reverseList(list);
        assertEquals(Arrays.asList(5, 4, 3, 2, 1), reversed);
    }

    @Test
    public void testRotateList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> rotated = CollectionOptimizerUtils.rotateList(list, 2);
        assertEquals(Arrays.asList(4, 5, 1, 2, 3), rotated);
    }

    @Test
    public void testFindCommonElements() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7);
        List<Integer> common = CollectionOptimizerUtils.findCommonElements(list1, list2);
        assertEquals(Arrays.asList(3, 4, 5), common);
    }

    @Test
    public void testFrequencyMap() {
        List<String> list = Arrays.asList("a", "b", "a", "c", "b", "a");
        Map<String, Long> freqMap = CollectionOptimizerUtils.frequencyMap(list);
        assertEquals(3L, freqMap.get("a"));
        assertEquals(2L, freqMap.get("b"));
        assertEquals(1L, freqMap.get("c"));
    }

    @Test
    public void testTopNByFrequency() {
        List<String> list = Arrays.asList("a", "b", "a", "c", "b", "a", "d", "d", "d", "d");
        List<String> top2 = CollectionOptimizerUtils.topNByFrequency(list, 2);
        assertEquals(Arrays.asList("d", "a"), top2);
    }

    @Test
    public void testShuffleList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> shuffled = CollectionOptimizerUtils.shuffleList(list);
        assertNotNull(shuffled);
        assertEquals(5, shuffled.size());
        assertTrue(shuffled.containsAll(list));
    }

    @Test
    public void testIsSorted() {
        List<Integer> sortedList = Arrays.asList(1, 2, 3, 4, 5);
        assertTrue(CollectionOptimizerUtils.isSorted(sortedList, Integer::compareTo));
        
        List<Integer> unsortedList = Arrays.asList(3, 1, 4, 2, 5);
        assertFalse(CollectionOptimizerUtils.isSorted(unsortedList, Integer::compareTo));
    }

    @Test
    public void testRemoveDuplicatesInPlace() {
        List<Integer> list = Arrays.asList(1, 2, 1, 3, 2, 4);
        List<Integer> result = CollectionOptimizerUtils.removeDuplicatesInPlace(list);
        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }

    @Test
    public void testPartitionAndFlatten() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> result = CollectionOptimizerUtils.partitionAndFlatten(
            list,
            3,
            partition -> partition.stream().map(n -> n * 2).toList()
        );
        assertEquals(Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18, 20), result);
    }
}
