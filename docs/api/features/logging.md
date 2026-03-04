# Logging 日志系统 API

日志系统提供控制台和文件日志实现，支持多种日志级别和格式化。

## 核心接口

```java
public interface Logger {
    String getName();
    void debug(String message);
    void debug(String message, Object... args);
    void debug(String message, Throwable throwable);
    void info(String message);
    void info(String message, Object... args);
    void info(String message, Throwable throwable);
    void warn(String message);
    void warn(String message, Object... args);
    void warn(String message, Throwable throwable);
    void error(String message);
    void error(String message, Object... args);
    void error(String message, Throwable throwable);
    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean isWarnEnabled();
    boolean isErrorEnabled();
}
```

## 日志级别

| 级别 | 说明 |
|------|------|
| DEBUG | 调试信息 |
| INFO | 一般信息 |
| WARN | 警告信息 |
| ERROR | 错误信息 |

## 控制台日志 (ConsoleLogger)

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

// 创建控制台日志
Logger logger = ConsoleLogs.create(MyClass.class);

// 使用日志
logger.debug("Debug message");
logger.info("User {} logged in", username);
logger.warn("Low disk space: {}%", 10);
logger.error("Failed to process", exception);
```

## 文件日志 (FileLogger)

```java
import ltd.idcu.est.features.logging.file.FileLogs;

// 创建文件日志
Logger fileLogger = FileLogs.create("./logs/app.log");

// 创建带滚动的文件日志
Logger rollingLogger = FileLogs.createRolling(
    "./logs/app", 
    "yyyy-MM-dd",  // 日期格式
    100,           // 保留天数
    10 * 1024 * 1024 // 单个文件最大10MB
);
```

## 日志格式配置

```java
// 自定义日志格式
Logger logger = ConsoleLogs.builder()
    .name("MyApp")
    .level(LogLevel.DEBUG)
    .format("%d{yyyy-MM-dd HH:mm:ss} [%p] %c - %m%n")
    .build();
```

## 日志工厂

```java
import ltd.idcu.est.features.logging.api.LogFactory;

// 使用日志工厂
Logger logger = LogFactory.getLogger(MyClass.class);
```
