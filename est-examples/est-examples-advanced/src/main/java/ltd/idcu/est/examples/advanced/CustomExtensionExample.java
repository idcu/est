package ltd.idcu.est.examples.advanced;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheStrategy;
import ltd.idcu.est.features.cache.memory.MemoryCache;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogger;
import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginContext;
import ltd.idcu.est.plugin.impl.DefaultPluginManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomExtensionExample {

    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();
        logger.info("Custom extension example started");
        
        // 1. 自定义缓存策略
        testCustomCacheStrategy(logger);
        
        // 2. 自定义事件监听器
        testCustomEventListener(logger);
        
        // 3. 自定义插件
        testCustomPlugin(logger);
        
        logger.info("Custom extension example completed");
    }
    
    private static void testCustomCacheStrategy(Logger logger) {
        // 自定义缓存策略：FIFO (First-In-First-Out)
        CacheStrategy<String, String> fifoStrategy = new CacheStrategy<String, String>() {
            private final Map<String, String> cache = new LinkedHashMap<>();
            private final int maxSize = 10;
            
            @Override
            public void put(String key, String value) {
                if (cache.size() >= maxSize) {
                    // 移除最早添加的元素
                    String firstKey = cache.keySet().iterator().next();
                    cache.remove(firstKey);
                }
                cache.put(key, value);
            }
            
            @Override
            public String get(String key) {
                return cache.get(key);
            }
            
            @Override
            public void remove(String key) {
                cache.remove(key);
            }
            
            @Override
            public void clear() {
                cache.clear();
            }
            
            @Override
            public int size() {
                return cache.size();
            }
        };
        
        // 使用自定义缓存策略
        Cache<String, String> cache = new MemoryCache<>(fifoStrategy);
        
        // 测试缓存
        for (int i = 0; i < 15; i++) {
            cache.put("key" + i, "value" + i);
        }
        
        logger.info("Custom cache strategy test: size = " + ((MemoryCache<?, ?>) cache).size());
        logger.info("Cache contains key0: " + (cache.get("key0") != null));
        logger.info("Cache contains key10: " + (cache.get("key10") != null));
    }
    
    private static void testCustomEventListener(Logger logger) {
        // 自定义事件监听器
        EventListener<String> customListener = event -> {
            logger.info("Custom event listener: " + event);
            // 可以添加自定义逻辑
        };
        
        // 这里可以集成到事件总线中
        logger.info("Custom event listener registered");
    }
    
    private static void testCustomPlugin(Logger logger) {
        // 自定义插件
        Plugin customPlugin = new Plugin() {
            @Override
            public String getName() {
                return "custom-plugin";
            }
            
            @Override
            public String getVersion() {
                return "1.0.0";
            }
            
            @Override
            public void initialize(PluginContext context) {
                logger.info("Custom plugin initialized");
            }
            
            @Override
            public void start() {
                logger.info("Custom plugin started");
            }
            
            @Override
            public void stop() {
                logger.info("Custom plugin stopped");
            }
            
            @Override
            public void destroy() {
                logger.info("Custom plugin destroyed");
            }
        };
        
        // 注册插件
        DefaultPluginManager pluginManager = new DefaultPluginManager();
        pluginManager.register(customPlugin);
        
        // 启动插件
        pluginManager.start(customPlugin.getName());
        
        // 停止插件
        pluginManager.stop(customPlugin.getName());
    }

}