# 调试最佳实践

本指南介绍如何调试 EST 应用。

## 日志调试

### 配置日志级别

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LoggingExample {
    
    private static final Logger logger = ConsoleLogs.create(LoggingExample.class);
    
    public void process() {
        logger.debug("开始处理");
        logger.info("处理中...");
        logger.warn("警告信息");
        logger.error("错误信息");
    }
}
```

### 日志级别

- **DEBUG**：详细的调试信息
- **INFO**：一般信息
- **WARN**：警告信息
- **ERROR**：错误信息

### 结构化日志

```java
logger.info("User {} created, email: {}, age: {}", 
    user.getId(), user.getEmail(), user.getAge());
```

## IDE 调试

### IntelliJ IDEA 断点调试

1. 在代码行号左侧点击设置断点
2. 右键点击 Debug 按钮
3. 使用调试工具栏：
   - Step Over (F8)
   - Step Into (F7)
   - Step Out (Shift+F8)
   - Resume Program (F9)

### 条件断点

```java
// 只在特定条件下暂停
for (int i = 0; i < 100; i++) {
    // 设置条件断点：i == 50
    process(i);
}
```

### 观察表达式

在调试器中添加观察表达式：

```
user.getName()
user.getEmail()
list.size()
```

## 远程调试

### 启用远程调试

启动应用时添加 JVM 参数：

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar yourapp.jar
```

在 IDE 中连接远程调试：

- Host: localhost
- Port: 5005

## 性能分析

### 使用 JVM 监控

```java
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;
import ltd.idcu.est.features.monitor.api.Metrics;

public class PerformanceMonitor {
    
    public void logMetrics() {
        JvmMonitor monitor = JvmMonitor.getInstance();
        Metrics metrics = monitor.getMetrics();
        
        System.out.println("堆内存使用: " + metrics.get("memory.heap.used"));
        System.out.println("线程数: " + metrics.get("threads.count"));
        System.out.println("GC 次数: " + metrics.get("gc.count"));
    }
}
```

### 使用 VisualVM

1. 下载并启动 VisualVM
2. 连接到运行中的应用
3. 查看：
   - 内存使用
   - 线程
   - CPU 使用率
   - 堆转储

## 常见问题排查

### 内存泄漏

```java
// 检查可能的内存泄漏
public class MemoryLeakExample {
    
    private static final List<Object> CACHE = new ArrayList<>();
    
    // 不好的做法
    public void addToCache(Object obj) {
        CACHE.add(obj); // 永远不会清理
    }
    
    // 好的做法
    private final Cache<String, Object> cache = MemoryCaches.create(
        CacheConfig.builder()
            .maxSize(1000)
            .expireAfterWrite(3600)
            .build()
    );
    
    public void addToCache(String key, Object obj) {
        cache.put(key, obj); // 自动过期
    }
}
```

### 线程问题

```java
// 检查线程安全
public class ThreadSafetyExample {
    
    // 不好的做法
    private List<User> users = new ArrayList<>();
    
    // 好的做法
    private List<User> users = Collections.synchronizedList(new ArrayList<>());
    
    // 或者
    private List<User> users = new CopyOnWriteArrayList<>();
}
```

## 调试工具

### 1. 堆转储

```bash
# 获取堆转储
jmap -dump:live,format=b,file=heapdump.hprof <pid>
```

### 2. 线程转储

```bash
# 获取线程转储
jstack <pid>
```

### 3. JVM 统计信息

```bash
# 查看 JVM 统计
jstat -gcutil <pid>
```

## 生产环境调试

### 健康检查端点

```java
app.getRouter().get("/debug/health", (req, res) -> {
    Map<String, Object> health = new HashMap<>();
    health.put("status", "healthy");
    health.put("jvm", JvmMonitor.getInstance().getMetrics().getAll());
    health.put("system", SystemMonitor.getInstance().getMetrics().getAll());
    res.json(health);
});
```

### 日志级别动态调整

```java
app.getRouter().post("/debug/log-level", (req, res) -> {
    String level = req.getParameter("level");
    LogLevel logLevel = LogLevel.valueOf(level.toUpperCase());
    logger.setLevel(logLevel);
    res.json(Map.of("status", "ok", "level", level));
});
```

## 调试检查清单

- [ ] 启用详细日志
- [ ] 检查配置值
- [ ] 验证输入数据
- [ ] 检查异常堆栈
- [ ] 监控内存使用
- [ ] 检查线程状态
- [ ] 验证外部服务连接
- [ ] 使用断点调试
- [ ] 查看性能指标
