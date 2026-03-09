package ltd.idcu.est.web;

import ltd.idcu.est.utils.common.CollectionOptimizerUtils;
import ltd.idcu.est.utils.common.PerformanceUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PerformanceOptimizationExample {

    public static void main(String[] args) {
        System.out.println("=== EST Framework 2.3.0 性能优化工具使用示例 ===\n");

        example1_TimingMeasurement();
        System.out.println();
        
        example2_MemoryMonitoring();
        System.out.println();
        
        example3_OptimizedCollections();
        System.out.println();
        
        example4_SortingAndSearching();
        System.out.println();
        
        example5_BatchProcessing();
        System.out.println();
        
        example6_LRUCache();
        System.out.println();
        
        example7_OptimizationTips();
    }

    private static void example1_TimingMeasurement() {
        System.out.println("1. 时间测量示例");
        System.out.println("-------------------");

        String taskName = "data-processing";
        PerformanceUtils.startTiming(taskName);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long durationNs = PerformanceUtils.stopTiming(taskName);
        long durationMs = TimeUnit.NANOSECONDS.toMillis(durationNs);

        System.out.println("任务执行时间: " + durationMs + " ms");
        System.out.println("纳秒级精度: " + durationNs + " ns");

        for (int i = 0; i < 5; i++) {
            PerformanceUtils.startTiming(taskName);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            PerformanceUtils.stopTiming(taskName);
        }

        long averageMs = PerformanceUtils.getAverageTiming(taskName, TimeUnit.MILLISECONDS);
        System.out.println("平均执行时间: " + averageMs + " ms");

        var stats = PerformanceUtils.getTimingStats();
        System.out.println("计时统计: " + stats);
        PerformanceUtils.clearTimings();
    }

    private static void example2_MemoryMonitoring() {
        System.out.println("2. 内存监控示例");
        System.out.println("-------------------");

        PerformanceUtils.MemorySnapshot snapshot = PerformanceUtils.getMemorySnapshot();
        System.out.println("堆内存初始化: " + formatBytes(snapshot.getHeapInit()));
        System.out.println("堆内存已使用: " + formatBytes(snapshot.getHeapUsed()));
        System.out.println("堆内存已提交: " + formatBytes(snapshot.getHeapCommitted()));
        System.out.println("堆内存最大值: " + formatBytes(snapshot.getHeapMax()));
        System.out.println("堆内存使用率: " + String.format("%.2f%%", snapshot.getHeapUsedPercent()));
        System.out.println("非堆内存已使用: " + formatBytes(snapshot.getNonHeapUsed()));

        double usagePercent = PerformanceUtils.getHeapMemoryUsagePercent();
        System.out.println("当前堆内存使用率: " + String.format("%.2f%%", usagePercent));
    }

    private static void example3_OptimizedCollections() {
        System.out.println("3. 优化集合示例");
        System.out.println("-------------------");

        List<String> list = CollectionOptimizerUtils.optimizedArrayList(1000);
        for (int i = 0; i < 1000; i++) {
            list.add("item-" + i);
        }
        System.out.println("优化ArrayList创建成功，大小: " + list.size());

        Map<String, Integer> map = CollectionOptimizerUtils.optimizedHashMap(500);
        for (int i = 0; i < 500; i++) {
            map.put("key-" + i, i);
        }
        System.out.println("优化HashMap创建成功，大小: " + map.size());

        Set<String> set = CollectionOptimizerUtils.optimizedHashSet(200);
        for (int i = 0; i < 200; i++) {
            set.add("element-" + i);
        }
        System.out.println("优化HashSet创建成功，大小: " + set.size());

        List<String> originalList = Arrays.asList("a", "b", "c", "a", "d", "b", "e");
        List<String> distinctList = CollectionOptimizerUtils.deduplicatePreserveOrder(originalList);
        System.out.println("去重前: " + originalList);
        System.out.println("去重后: " + distinctList);
    }

    private static void example4_SortingAndSearching() {
        System.out.println("4. 排序和搜索示例");
        System.out.println("-------------------");

        List<Integer> unsortedList = Arrays.asList(5, 2, 8, 1, 9, 3, 7, 4, 6);
        System.out.println("未排序列表: " + unsortedList);

        List<Integer> sortedList = CollectionOptimizerUtils.quickSort(unsortedList);
        System.out.println("快速排序后: " + sortedList);

        int target = 7;
        int index = CollectionOptimizerUtils.binarySearch(sortedList, target, Integer::compareTo);
        System.out.println("二分查找 " + target + " 的索引: " + index);

        List<Integer> list1 = Arrays.asList(1, 3, 5, 7, 9);
        List<Integer> list2 = Arrays.asList(2, 4, 6, 8, 10);
        List<Integer> mergedList = CollectionOptimizerUtils.mergeSortedLists(list1, list2, Integer::compareTo);
        System.out.println("合并有序列表: " + list1 + " + " + list2 + " = " + mergedList);
    }

    private static void example5_BatchProcessing() {
        System.out.println("5. 批处理示例");
        System.out.println("-------------------");

        List<Integer> largeList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            largeList.add(i);
        }
        System.out.println("处理大型列表，大小: " + largeList.size());

        List<Integer> result = CollectionOptimizerUtils.batchProcess(
            largeList,
            10,
            batch -> {
                System.out.println("  处理批次: " + batch);
                return batch.stream().map(n -> n * 2).toList();
            }
        );
        System.out.println("批处理结果（前10个）: " + result.subList(0, Math.min(10, result.size())));
    }

    private static void example6_LRUCache() {
        System.out.println("6. LRU缓存示例");
        System.out.println("-------------------");

        Map<String, String> cache = CollectionOptimizerUtils.createLRUCache(3);
        cache.put("user1", "Alice");
        cache.put("user2", "Bob");
        cache.put("user3", "Charlie");
        System.out.println("缓存内容（容量3）: " + cache);

        cache.put("user4", "David");
        System.out.println("添加user4后（LRU淘汰user1）: " + cache);

        cache.get("user2");
        cache.put("user5", "Eve");
        System.out.println("访问user2并添加user5后（LRU淘汰user3）: " + cache);
    }

    private static void example7_OptimizationTips() {
        System.out.println("7. 优化建议示例");
        System.out.println("-------------------");

        var memoryTips = PerformanceUtils.getMemoryOptimizationTips();
        System.out.println("内存优化建议:");
        for (var tip : memoryTips) {
            System.out.println("  [" + tip.getSeverity() + "] " + tip.getTitle() + ": " + tip.getDescription());
        }

        System.out.println();

        var startupTips = PerformanceUtils.getStartupOptimizationTips();
        System.out.println("启动优化建议:");
        for (var tip : startupTips) {
            System.out.println("  [" + tip.getSeverity() + "] " + tip.getTitle() + ": " + tip.getDescription());
        }
    }

    private static String formatBytes(long bytes) {
        if (bytes >= 1024 * 1024 * 1024) {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        } else if (bytes >= 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return bytes + " B";
        }
    }
}
