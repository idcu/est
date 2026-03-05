package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.console.ConsoleLogs;
import ltd.idcu.est.features.logging.file.FileLogs;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheEntry;
import ltd.idcu.est.features.cache.memory.MemoryCache;
import ltd.idcu.est.features.cache.file.FileCache;
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.async.AsyncEventBus;

import java.util.concurrent.TimeUnit;

public class ComprehensiveFeaturesExample {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("EST 功能模块 - 全面示例");
        System.out.println("=".repeat(60));
        System.out.println("\n本示例将展示 EST 框架的各种企业级功能：");
        System.out.println("  - 日志系统（多种实现）");
        System.out.println("  - 缓存系统（内存、文件等）");
        System.out.println("  - 事件总线（同步、异步）");
        System.out.println("  - 更多功能...");
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("第一部分：日志系统");
        System.out.println("=".repeat(60));
        
        loggingExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("第二部分：缓存系统");
        System.out.println("=".repeat(60));
        
        cacheExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("第三部分：事件总线");
        System.out.println("=".repeat(60));
        
        eventBusExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(60));
        System.out.println("✓ 所有功能示例运行完成！");
        System.out.println("=".repeat(60));
    }
    
    public static void run() {
        main(new String[]{});
    }
    
    private static void loggingExample() {
        System.out.println("\n--- 日志系统示例 ---");
        System.out.println("EST 提供灵活的日志系统，支持多种实现方式\n");
        
        System.out.println("【方式一】控制台日志（最简单）");
        System.out.println("适合开发调试，日志直接输出到控制台\n");
        
        Logger consoleLogger = ConsoleLogs.getLogger(ComprehensiveFeaturesExample.class);
        
        System.out.println("1. 不同级别的日志输出：");
        consoleLogger.trace("这是 TRACE 级别的日志（最详细）");
        consoleLogger.debug("这是 DEBUG 级别的日志");
        consoleLogger.info("这是 INFO 级别的日志（一般信息）");
        consoleLogger.warn("这是 WARN 级别的日志（警告）");
        consoleLogger.error("这是 ERROR 级别的日志（错误）");
        
        System.out.println("\n2. 带参数的日志：");
        consoleLogger.info("用户 {} 登录了系统，IP 地址是 {}", "张三", "192.168.1.100");
        
        System.out.println("\n3. 带异常的日志：");
        try {
            int result = 10 / 0;
        } catch (Exception e) {
            consoleLogger.error("计算时发生错误", e);
        }
        
        System.out.println("\n【方式二】文件日志");
        System.out.println("适合生产环境，日志写入文件\n");
        
        Logger fileLogger = FileLogs.getLogger("application.log");
        fileLogger.info("这是一条写入文件的日志");
        fileLogger.warn("警告：系统资源使用率较高");
        
        System.out.println("✓ 文件日志已写入 application.log");
        
        System.out.println("\n【方式三】设置日志级别");
        System.out.println("可以动态调整日志级别，过滤不需要的日志\n");
        
        Logger levelLogger = ConsoleLogs.getLogger("LevelTest");
        ((ConsoleLogs) levelLogger).setLevel(LogLevel.WARN);
        
        System.out.println("   设置日志级别为 WARN 后：");
        System.out.println("   - DEBUG 和 INFO 日志不会输出");
        System.out.println("   - 只有 WARN 和 ERROR 日志会输出\n");
        
        levelLogger.debug("这条不会显示");
        levelLogger.info("这条也不会显示");
        levelLogger.warn("这条警告会显示");
        levelLogger.error("这条错误会显示");
        
        System.out.println("\n✓ 日志系统示例完成\n");
    }
    
    private static void cacheExample() {
        System.out.println("\n--- 缓存系统示例 ---");
        System.out.println("EST 提供多种缓存实现，提升应用性能\n");
        
        System.out.println("【方式一】内存缓存（最快）");
        System.out.println("数据存储在内存中，读写速度极快");
        System.out.println("适合存储热点数据、会话信息等\n");
        
        Cache<String, String> memoryCache = new MemoryCache<>();
        
        System.out.println("1. 基本的缓存操作：");
        memoryCache.put("user:1", "张三");
        memoryCache.put("user:2", "李四");
        memoryCache.put("user:3", "王五");
        
        System.out.println("   - user:1 = " + memoryCache.get("user:1"));
        System.out.println("   - user:2 = " + memoryCache.get("user:2"));
        
        System.out.println("\n2. 检查缓存是否存在：");
        System.out.println("   - user:1 存在吗？ " + memoryCache.containsKey("user:1"));
        System.out.println("   - user:99 存在吗？ " + memoryCache.containsKey("user:99"));
        
        System.out.println("\n3. 删除缓存：");
        memoryCache.remove("user:3");
        System.out.println("   - 删除 user:3 后，缓存大小：" + memoryCache.size());
        
        System.out.println("\n4. 带过期时间的缓存：");
        memoryCache.put("temp:data", "临时数据", 5, TimeUnit.SECONDS);
        System.out.println("   - 设置了 5 秒过期的临时数据");
        System.out.println("   - 当前值：" + memoryCache.get("temp:data"));
        
        System.out.println("\n【方式二】文件缓存（持久化）");
        System.out.println("数据存储在文件中，重启后依然存在");
        System.out.println("适合存储需要持久化的配置、数据等\n");
        
        Cache<String, String> fileCache = new FileCache<>("cache-data");
        
        System.out.println("1. 写入文件缓存：");
        fileCache.put("config:theme", "dark");
        fileCache.put("config:language", "zh-CN");
        fileCache.put("config:fontSize", "14px");
        
        System.out.println("   - 主题：" + fileCache.get("config:theme"));
        System.out.println("   - 语言：" + fileCache.get("config:language"));
        
        System.out.println("\n2. 获取所有缓存键：");
        System.out.println("   - 所有缓存键：" + fileCache.keySet());
        
        System.out.println("\n3. 清空缓存：");
        fileCache.clear();
        System.out.println("   - 清空后缓存大小：" + fileCache.size());
        
        System.out.println("\n【方式三】缓存统计和高级功能");
        System.out.println("缓存系统提供丰富的统计信息\n");
        
        Cache<String, Integer> statsCache = new MemoryCache<>();
        
        System.out.println("1. 模拟缓存使用：");
        statsCache.put("product:1001", 2999);
        statsCache.put("product:1002", 3999);
        statsCache.put("product:1003", 4999);
        
        statsCache.get("product:1001");
        statsCache.get("product:1001");
        statsCache.get("product:1002");
        statsCache.get("product:9999");
        
        System.out.println("   - 缓存大小：" + statsCache.size());
        System.out.println("   - 是否为空：" + statsCache.isEmpty());
        
        System.out.println("\n2. 使用 computeIfAbsent（懒加载）：");
        Integer expensiveData = statsCache.computeIfAbsent("expensive:data", key -> {
            System.out.println("   - 执行昂贵的计算...");
            return 42;
        });
        System.out.println("   - 结果：" + expensiveData);
        
        System.out.println("   - 再次获取（直接从缓存返回）：");
        Integer cachedData = statsCache.computeIfAbsent("expensive:data", key -> {
            System.out.println("   - 不会执行到这里");
            return 999;
        });
        System.out.println("   - 结果：" + cachedData);
        
        System.out.println("\n✓ 缓存系统示例完成\n");
    }
    
    private static void eventBusExample() {
        System.out.println("\n--- 事件总线示例 ---");
        System.out.println("事件总线用于组件间的解耦通信\n");
        
        System.out.println("【方式一】同步事件总线");
        System.out.println("事件在当前线程同步处理，适合简单场景\n");
        
        EventBus localEventBus = new LocalEventBus();
        
        System.out.println("1. 定义事件监听器：");
        
        EventListener<UserRegisteredEvent> userListener = event -> {
            System.out.println("   [用户服务] 收到用户注册事件：");
            System.out.println("     - 用户名：" + event.getUsername());
            System.out.println("     - 邮箱：" + event.getEmail());
            System.out.println("     - 正在创建用户账户...");
        };
        
        EventListener<UserRegisteredEvent> emailListener = event -> {
            System.out.println("   [邮件服务] 收到用户注册事件：");
            System.out.println("     - 正在发送欢迎邮件到 " + event.getEmail());
        };
        
        EventListener<UserRegisteredEvent> analyticsListener = event -> {
            System.out.println("   [分析服务] 收到用户注册事件：");
            System.out.println("     - 正在记录注册统计...");
        };
        
        System.out.println("\n2. 注册监听器：");
        localEventBus.subscribe(UserRegisteredEvent.class, userListener);
        localEventBus.subscribe(UserRegisteredEvent.class, emailListener);
        localEventBus.subscribe(UserRegisteredEvent.class, analyticsListener);
        
        System.out.println("\n3. 发布事件：");
        UserRegisteredEvent event = new UserRegisteredEvent("张三", "zhangsan@example.com");
        localEventBus.publish(event);
        
        System.out.println("\n【方式二】异步事件总线");
        System.out.println("事件在后台线程异步处理，不阻塞主线程");
        System.out.println("适合耗时操作，如发送邮件、调用外部 API 等\n");
        
        EventBus asyncEventBus = new AsyncEventBus();
        
        System.out.println("1. 注册异步监听器：");
        
        asyncEventBus.subscribe(OrderPlacedEvent.class, orderEvent -> {
            System.out.println("   [库存服务] 收到订单事件（异步）：");
            System.out.println("     - 订单号：" + orderEvent.getOrderId());
            System.out.println("     - 正在扣减库存...（模拟耗时 1 秒）");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("     - 库存扣减完成！");
        });
        
        asyncEventBus.subscribe(OrderPlacedEvent.class, orderEvent -> {
            System.out.println("   [物流服务] 收到订单事件（异步）：");
            System.out.println("     - 订单号：" + orderEvent.getOrderId());
            System.out.println("     - 正在安排发货...（模拟耗时 2 秒）");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("     - 发货安排完成！");
        });
        
        System.out.println("\n2. 发布异步事件：");
        System.out.println("   - 主线程继续执行，不等待异步处理完成");
        asyncEventBus.publish(new OrderPlacedEvent("ORD-2024-00001"));
        
        System.out.println("   - 主线程继续执行其他任务...");
        
        System.out.println("\n3. 等待异步任务完成（演示用）：");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n【方式三】取消订阅");
        System.out.println("可以动态取消监听器的订阅\n");
        
        System.out.println("1. 订阅一个临时监听器：");
        EventListener<UserRegisteredEvent> tempListener = e -> {
            System.out.println("   [临时监听器] 收到事件");
        };
        localEventBus.subscribe(UserRegisteredEvent.class, tempListener);
        
        System.out.println("2. 发布事件（临时监听器会收到）：");
        localEventBus.publish(new UserRegisteredEvent("李四", "lisi@example.com"));
        
        System.out.println("\n3. 取消临时监听器的订阅：");
        localEventBus.unsubscribe(UserRegisteredEvent.class, tempListener);
        
        System.out.println("\n4. 再次发布事件（临时监听器不会收到）：");
        localEventBus.publish(new UserRegisteredEvent("王五", "wangwu@example.com"));
        
        System.out.println("\n✓ 事件总线示例完成\n");
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
