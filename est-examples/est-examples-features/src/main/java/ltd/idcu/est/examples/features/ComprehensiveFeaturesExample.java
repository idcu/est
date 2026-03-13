package ltd.idcu.est.examples.features;

import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.api.LogLevel;
import ltd.idcu.est.logging.console.ConsoleLogs;
import ltd.idcu.est.logging.file.FileLogs;
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.api.CacheEntry;
import ltd.idcu.est.cache.memory.MemoryCache;
import ltd.idcu.est.cache.file.FileCache;
import ltd.idcu.est.event.api.Event;
import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventListener;
import ltd.idcu.est.event.local.LocalEventBus;
import ltd.idcu.est.event.async.AsyncEventBus;

import java.util.concurrent.TimeUnit;

public class ComprehensiveFeaturesExample {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST Features Module - Comprehensive Example");
        System.out.println("=".repeat(60));
        System.out.println("\nThis example will demonstrate various enterprise features of EST framework:");
        System.out.println("  - Logging system (multiple implementations)");
        System.out.println("  - Caching system (memory, file, etc.)");
        System.out.println("  - Event bus (synchronous, asynchronous)");
        System.out.println("  - More features...");
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("Part 1: Logging System");
        System.out.println("=".repeat(60));
        
        loggingExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 2: Caching System");
        System.out.println("=".repeat(60));
        
        cacheExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("Part 3: Event Bus");
        System.out.println("=".repeat(60));
        
        eventBusExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("[X] All feature examples completed!");
        System.out.println("=".repeat(60));
    }
    
    public static void run() {
        main(new String[]{});
    }
    
    private static void loggingExample() {
        System.out.println("\n--- Logging System Example ---");
        System.out.println("EST provides a flexible logging system with multiple implementations\n");
        
        System.out.println("[Method 1] Console Logging (Simplest)");
        System.out.println("Great for development and debugging, logs output directly to console\n");
        
        Logger consoleLogger = ConsoleLogs.getLogger(ComprehensiveFeaturesExample.class);
        
        System.out.println("1. Log output at different levels:");
        consoleLogger.trace("This is TRACE level log (most detailed)");
        consoleLogger.debug("This is DEBUG level log");
        consoleLogger.info("This is INFO level log (general info)");
        consoleLogger.warn("This is WARN level log (warning)");
        consoleLogger.error("This is ERROR level log (error)");
        
        System.out.println("\n2. Logs with parameters:");
        consoleLogger.info("User {} logged in, IP address: {}", "Zhang San", "192.168.1.100");
        
        System.out.println("\n3. Logs with exceptions:");
        try {
            int result = 10 / 0;
        } catch (Exception e) {
            consoleLogger.error("Error occurred during calculation", e);
        }
        
        System.out.println("\n[Method 2] File Logging");
        System.out.println("Great for production environments, logs written to files\n");
        
        Logger fileLogger = FileLogs.getLogger("application.log");
        fileLogger.info("This is a log written to file");
        fileLogger.warn("Warning: System resource usage is high");
        
        System.out.println("[X] File logs written to application.log");
        
        System.out.println("\n[Method 3] Setting Log Level");
        System.out.println("Can dynamically adjust log level to filter unnecessary logs\n");
        
        Logger levelLogger = ConsoleLogs.getLogger("LevelTest");
        ((ConsoleLogs) levelLogger).setLevel(LogLevel.WARN);
        
        System.out.println("   After setting log level to WARN:");
        System.out.println("   - DEBUG and INFO logs will not output");
        System.out.println("   - Only WARN and ERROR logs will output\n");
        
        levelLogger.debug("This will not show");
        levelLogger.info("This will not show either");
        levelLogger.warn("This warning will show");
        levelLogger.error("This error will show");
        
        System.out.println("\n[X] Logging system example completed\n");
    }
    
    private static void cacheExample() {
        System.out.println("\n--- Caching System Example ---");
        System.out.println("EST provides multiple cache implementations to improve application performance\n");
        
        System.out.println("[Method 1] Memory Cache (Fastest)");
        System.out.println("Data stored in memory, extremely fast read/write");
        System.out.println("Great for storing hot data, session info, etc.\n");
        
        Cache<String, String> memoryCache = new MemoryCache<>();
        
        System.out.println("1. Basic cache operations:");
        memoryCache.put("user:1", "Zhang San");
        memoryCache.put("user:2", "Li Si");
        memoryCache.put("user:3", "Wang Wu");
        
        System.out.println("   - user:1 = " + memoryCache.get("user:1"));
        System.out.println("   - user:2 = " + memoryCache.get("user:2"));
        
        System.out.println("\n2. Check if cache exists:");
        System.out.println("   - user:1 exists? " + memoryCache.containsKey("user:1"));
        System.out.println("   - user:99 exists? " + memoryCache.containsKey("user:99"));
        
        System.out.println("\n3. Remove cache:");
        memoryCache.remove("user:3");
        System.out.println("   - After removing user:3, cache size: " + memoryCache.size());
        
        System.out.println("\n4. Cache with expiration:");
        memoryCache.put("temp:data", "Temporary data", 5, TimeUnit.SECONDS);
        System.out.println("   - Set temporary data with 5 second expiration");
        System.out.println("   - Current value: " + memoryCache.get("temp:data"));
        
        System.out.println("\n[Method 2] File Cache (Persistent)");
        System.out.println("Data stored in files, persists after restart");
        System.out.println("Great for storing configurations, data that need persistence\n");
        
        Cache<String, String> fileCache = new FileCache<>("cache-data");
        
        System.out.println("1. Write to file cache:");
        fileCache.put("config:theme", "dark");
        fileCache.put("config:language", "en-US");
        fileCache.put("config:fontSize", "14px");
        
        System.out.println("   - Theme: " + fileCache.get("config:theme"));
        System.out.println("   - Language: " + fileCache.get("config:language"));
        
        System.out.println("\n2. Get all cache keys:");
        System.out.println("   - All cache keys: " + fileCache.keySet());
        
        System.out.println("\n3. Clear cache:");
        fileCache.clear();
        System.out.println("   - Cache size after clearing: " + fileCache.size());
        
        System.out.println("\n[Method 3] Cache Statistics and Advanced Features");
        System.out.println("Cache system provides rich statistics\n");
        
        Cache<String, Integer> statsCache = new MemoryCache<>();
        
        System.out.println("1. Simulate cache usage:");
        statsCache.put("product:1001", 2999);
        statsCache.put("product:1002", 3999);
        statsCache.put("product:1003", 4999);
        
        statsCache.get("product:1001");
        statsCache.get("product:1001");
        statsCache.get("product:1002");
        statsCache.get("product:9999");
        
        System.out.println("   - Cache size: " + statsCache.size());
        System.out.println("   - Is empty? " + statsCache.isEmpty());
        
        System.out.println("\n2. Using computeIfAbsent (lazy loading):");
        Integer expensiveData = statsCache.computeIfAbsent("expensive:data", key -> {
            System.out.println("   - Performing expensive calculation...");
            return 42;
        });
        System.out.println("   - Result: " + expensiveData);
        
        System.out.println("   - Getting again (returns directly from cache):");
        Integer cachedData = statsCache.computeIfAbsent("expensive:data", key -> {
            System.out.println("   - This will not execute");
            return 999;
        });
        System.out.println("   - Result: " + cachedData);
        
        System.out.println("\n[X] Caching system example completed\n");
    }
    
    private static void eventBusExample() {
        System.out.println("\n--- Event Bus Example ---");
        System.out.println("Event bus for decoupled communication between components\n");
        
        System.out.println("[Method 1] Synchronous Event Bus");
        System.out.println("Events processed synchronously on current thread, great for simple scenarios\n");
        
        EventBus localEventBus = new LocalEventBus();
        
        System.out.println("1. Define event listeners:");
        
        EventListener<UserRegisteredEvent> userListener = event -> {
            System.out.println("   [User Service] Received user registration event");
            System.out.println("     - Username: " + event.getUsername());
            System.out.println("     - Email: " + event.getEmail());
            System.out.println("     - Creating user account...");
        };
        
        EventListener<UserRegisteredEvent> emailListener = event -> {
            System.out.println("   [Email Service] Received user registration event");
            System.out.println("     - Sending welcome email to " + event.getEmail());
        };
        
        EventListener<UserRegisteredEvent> analyticsListener = event -> {
            System.out.println("   [Analytics Service] Received user registration event");
            System.out.println("     - Recording registration statistics...");
        };
        
        System.out.println("\n2. Register listeners:");
        localEventBus.subscribe(UserRegisteredEvent.class, userListener);
        localEventBus.subscribe(UserRegisteredEvent.class, emailListener);
        localEventBus.subscribe(UserRegisteredEvent.class, analyticsListener);
        
        System.out.println("\n3. Publish event:");
        UserRegisteredEvent event = new UserRegisteredEvent("Zhang San", "zhangsan@example.com");
        localEventBus.publish(event);
        
        System.out.println("\n[Method 2] Asynchronous Event Bus");
        System.out.println("Events processed asynchronously on background threads, not blocking main thread");
        System.out.println("Great for time-consuming operations like sending emails, calling external APIs, etc.\n");
        
        EventBus asyncEventBus = new AsyncEventBus();
        
        System.out.println("1. Register async listeners:");
        
        asyncEventBus.subscribe(OrderPlacedEvent.class, orderEvent -> {
            System.out.println("   [Inventory Service] Received order event (async)");
            System.out.println("     - Order ID: " + orderEvent.getOrderId());
            System.out.println("     - Deducting inventory... (simulating 1 second)");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("     - Inventory deduction completed");
        });
        
        asyncEventBus.subscribe(OrderPlacedEvent.class, orderEvent -> {
            System.out.println("   [Logistics Service] Received order event (async)");
            System.out.println("     - Order ID: " + orderEvent.getOrderId());
            System.out.println("     - Arranging shipping... (simulating 2 seconds)");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("     - Shipping arrangement completed");
        });
        
        System.out.println("\n2. Publish async event:");
        System.out.println("   - Main thread continues executing, not waiting for async processing to complete");
        asyncEventBus.publish(new OrderPlacedEvent("ORD-2024-00001"));
        
        System.out.println("   - Main thread continues executing other tasks...");
        
        System.out.println("\n3. Wait for async tasks to complete (for demo):");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n[Method 3] Unsubscribe");
        System.out.println("Can dynamically unsubscribe listeners\n");
        
        System.out.println("1. Subscribe a temporary listener:");
        EventListener<UserRegisteredEvent> tempListener = e -> {
            System.out.println("   [Temporary Listener] Received event");
        };
        localEventBus.subscribe(UserRegisteredEvent.class, tempListener);
        
        System.out.println("2. Publish event (temporary listener will receive):");
        localEventBus.publish(new UserRegisteredEvent("Li Si", "lisi@example.com"));
        
        System.out.println("\n3. Unsubscribe temporary listener:");
        localEventBus.unsubscribe(UserRegisteredEvent.class, tempListener);
        
        System.out.println("\n4. Publish event again (temporary listener will not receive):");
        localEventBus.publish(new UserRegisteredEvent("Wang Wu", "wangwu@example.com"));
        
        System.out.println("\n[X] Event bus example completed\n");
    }
}

class UserRegisteredEvent implements Event {
    private final String username;
    private final String email;
    
    public UserRegisteredEvent(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
}

class OrderPlacedEvent implements Event {
    private final String orderId;
    
    public OrderPlacedEvent(String orderId) {
        this.orderId = orderId;
    }
    
    public String getOrderId() {
        return orderId;
    }
}
