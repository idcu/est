package ltd.idcu.est.examples.advanced;

import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.impl.DefaultCollection;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCache;
import ltd.idcu.est.features.cache.memory.LruCacheStrategy;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.async.AsyncEventBus;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogger;

public class PerformanceOptimizationExample {

    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();
        logger.info("Performance optimization example started");
        
        // 1. 缓存优化
        testCacheOptimization(logger);
        
        // 2. 集合操作优化
        testCollectionOptimization(logger);
        
        // 3. 事件处理优化
        testEventOptimization(logger);
        
        logger.info("Performance optimization example completed");
    }
    
    private static void testCacheOptimization(Logger logger) {
        // 使用LRU策略的缓存
        Cache<String, String> cache = new MemoryCache<>(new LruCacheStrategy<>(1000));
        
        long startTime = System.currentTimeMillis();
        
        // 模拟大量缓存操作
        for (int i = 0; i < 10000; i++) {
            cache.put("key" + i, "value" + i);
        }
        
        for (int i = 0; i < 10000; i++) {
            cache.get("key" + i);
        }
        
        long endTime = System.currentTimeMillis();
        logger.info("Cache optimization test: " + (endTime - startTime) + "ms");
    }
    
    private static void testCollectionOptimization(Logger logger) {
        Collection<String> collection = new DefaultCollection<>();
        
        // 添加大量元素
        for (int i = 0; i < 10000; i++) {
            collection.add("item" + i);
        }
        
        long startTime = System.currentTimeMillis();
        
        // 测试链式操作性能
        long count = collection
            .filter(item -> item.contains("5"))
            .map(item -> item.toUpperCase())
            .count();
        
        long endTime = System.currentTimeMillis();
        logger.info("Collection optimization test: " + (endTime - startTime) + "ms, count: " + count);
    }
    
    private static void testEventOptimization(Logger logger) {
        // 使用异步事件总线
        EventBus eventBus = new AsyncEventBus();
        
        // 订阅事件
        eventBus.subscribe(String.class, event -> {
            // 模拟事件处理
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        long startTime = System.currentTimeMillis();
        
        // 发布大量事件
        for (int i = 0; i < 1000; i++) {
            eventBus.publish("Event " + i);
        }
        
        long endTime = System.currentTimeMillis();
        logger.info("Event optimization test: " + (endTime - startTime) + "ms");
        
        // 关闭事件总线
        eventBus.close();
    }

}