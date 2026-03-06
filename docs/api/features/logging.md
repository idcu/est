# Logging 日志系统 API

日志系统提供控制台和文件日志实现，支持多种日志级别和格式化。

## 核心概念

| 概念 | 说明 |
|------|------|
| **Logger** | 日志记录器接口，用于记录日志 |
| **LogLevel** | 日志级别（TRACE、DEBUG、INFO、WARN、ERROR） |
| **LogAppender** | 日志输出器，决定日志输出到哪里 |
| **LogFormatter** | 日志格式化器，决定日志的格式 |
| **LogConfig** | 日志配置 |
| **LoggerFactory** | 日志工厂 |

---

## 日志级别

| 级别 | 说明 | 使用场景 |
|------|------|----------|
| **TRACE** | 最详细的日志信息 | 详细的调试信息，追踪代码执行流程 |
| **DEBUG** | 调试信息 | 开发调试时使用 |
| **INFO** | 一般信息 | 记录应用程序的关键运行信息 |
| **WARN** | 警告信息 | 记录可能出现问题的情况 |
| **ERROR** | 错误信息 | 记录错误和异常 |

---

## 控制台日志 (ConsoleLogger)

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

// 方式一：通过类获取日志
Logger logger = ConsoleLogs.getLogger(MyClass.class);

// 方式二：通过名称获取日志
Logger logger = ConsoleLogs.getLogger("my-app");

// 方式三：指定日志级别
Logger debugLogger = ConsoleLogs.getLogger("my-app", LogLevel.DEBUG);
Logger traceLogger = ConsoleLogs.traceLogger("my-app");

// 使用日志
logger.trace("Trace message");
logger.debug("Debug message");
logger.info("Info message");
logger.warn("Warn message");
logger.error("Error message");

// 使用格式化参数
logger.info("User {} logged in from {}", "张三", "192.168.1.1");
logger.debug("Processing order: {}, amount: {}", "ORD-001", 99.99);

// 记录异常
try {
    // 一些可能出错的代码
} catch (Exception e) {
    logger.error("Failed to process request", e);
}

// 检查日志级别是否启用
if (logger.isDebugEnabled()) {
    logger.debug("This message will only appear if debug is enabled");
}
```

---

## 使用 Builder 创建日志

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

// 使用 Builder 创建日志
Logger logger = ConsoleLogs.builder()
    .name("my-application")
    .level(LogLevel.DEBUG)
    .formatter(ConsoleLogs.coloredFormatter())
    .output(System.out)
    .build();

logger.info("Application started");
```

---

## 日志格式化

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;
import ltd.idcu.est.features.logging.console.ConsoleLogFormatter;

import java.time.format.DateTimeFormatter;

// 使用彩色格式化器（默认）
Logger coloredLogger = ConsoleLogs.builder()
    .name("colored-logger")
    .formatter(ConsoleLogs.coloredFormatter())
    .build();

// 使用纯文本格式化器
Logger plainLogger = ConsoleLogs.builder()
    .name("plain-logger")
    .formatter(ConsoleLogs.plainFormatter())
    .build();

// 自定义日期格式和颜色
DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
ConsoleLogFormatter formatter = ConsoleLogs.formatter(customFormatter, true);
Logger customLogger = ConsoleLogs.builder()
    .name("custom-logger")
    .formatter(formatter)
    .build();
```

---

## 设置默认日志工厂

```java
import ltd.idcu.est.features.logging.api.LogConfig;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LoggerFactory;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

// 设置控制台日志为默认
ConsoleLogs.setAsDefault();

// 现在可以通过 LoggerFactory 获取日志
Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.info("Using default logger");

// 或者设置带配置的默认日志
LogConfig config = LogConfig.defaultConfig()
    .setLevel(LogLevel.DEBUG);
ConsoleLogs.setAsDefault(config);

Logger debugLogger = LoggerFactory.getLogger("debug-app");
debugLogger.debug("Debug message");
```

---

## 完整示例

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LoggingExample {
    private static final Logger logger = ConsoleLogs.getLogger(LoggingExample.class);
    
    public static void main(String[] args) {
        System.out.println("=== EST Logging Example ===\n");
        
        // 1. 基本日志记录
        logger.trace("This is a TRACE message");
        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message");
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
        
        System.out.println();
        
        // 2. 带参数的日志
        String username = "张三";
        int userId = 1001;
        logger.info("User {} (ID: {}) logged in", username, userId);
        
        System.out.println();
        
        // 3. 不同级别的条件日志
        if (logger.isDebugEnabled()) {
            logger.debug("This debug message is shown because debug is enabled");
        }
        
        if (logger.isTraceEnabled()) {
            logger.trace("This trace message may or may not be shown");
        }
        
        System.out.println();
        
        // 4. 记录异常
        try {
            throw new RuntimeException("Something went wrong!");
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
        
        System.out.println();
        
        // 5. 使用不同的日志级别创建日志
        Logger debugLogger = ConsoleLogs.getLogger("debug-only", LogLevel.DEBUG);
        debugLogger.debug("Debug-only logger: This message will show");
        debugLogger.trace("Debug-only logger: This message won't show (TRACE < DEBUG)");
        
        System.out.println();
        
        // 6. 使用彩色格式化器
        Logger coloredLogger = ConsoleLogs.builder()
            .name("colored-logger")
            .level(LogLevel.INFO)
            .formatter(ConsoleLogs.coloredFormatter())
            .build();
        coloredLogger.info("This message has colors!");
        coloredLogger.warn("Warning!");
        coloredLogger.error("Error!");
        
        System.out.println("\n=== Example Complete ===");
    }
}
```

---

## 最佳实践

1. **选择合适的日志级别**：
   - 使用 TRACE 记录最详细的调试信息
   - 使用 DEBUG 记录开发调试信息
   - 使用 INFO 记录关键业务流程
   - 使用 WARN 记录潜在问题
   - 使用 ERROR 记录错误和异常

2. **使用参数化日志**：
   ```java
   // ✅ 推荐：使用参数化
   logger.info("User {} logged in", username);
   
   // ❌ 不推荐：字符串拼接
   logger.info("User " + username + " logged in");
   ```

3. **检查日志级别**：
   ```java
   // 在记录复杂日志前检查级别
   if (logger.isDebugEnabled()) {
       logger.debug("Complex data: " + computeComplexData());
   }
   ```

4. **合理命名日志**：
   - 使用类名作为日志名称
   - 或使用有意义的模块名称

5. **记录异常时包含堆栈信息**：
   ```java
   try {
       // ...
   } catch (Exception e) {
       logger.error("Failed to process", e); // 包含堆栈
   }
   ```
