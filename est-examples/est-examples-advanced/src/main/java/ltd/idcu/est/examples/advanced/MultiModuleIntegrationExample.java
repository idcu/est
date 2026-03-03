package ltd.idcu.est.examples.advanced;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.DefaultModule;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCache;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogger;

public class MultiModuleIntegrationExample {

    public static void main(String[] args) {
        // 初始化容器
        Container container = new DefaultContainer();
        
        // 初始化配置
        Config config = new DefaultConfig();
        container.register(Config.class, config);
        
        // 初始化模块
        Module module = new DefaultModule("multi-module-example");
        container.register(Module.class, module);
        
        // 集成缓存模块
        Cache<String, String> cache = new MemoryCache<>();
        container.register(Cache.class, cache);
        
        // 集成事件模块
        EventBus eventBus = new LocalEventBus();
        container.register(EventBus.class, eventBus);
        
        // 集成日志模块
        Logger logger = new ConsoleLogger();
        container.register(Logger.class, logger);
        
        // 使用集成的模块
        logger.info("Multi-module integration example started");
        
        // 测试缓存
        cache.put("key1", "value1");
        String value = cache.get("key1");
        logger.info("Cache test: " + value);
        
        // 测试事件
        eventBus.subscribe(String.class, event -> {
            logger.info("Received event: " + event);
        });
        eventBus.publish("Test event");
        
        logger.info("Multi-module integration example completed");
    }

}