# EST Logging - 日志系统

## 📚 目录

- [快速入门](#快速入门)
- [基础篇](#基础篇)
- [进阶篇](#进阶篇)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是日志系统？

想象一下，你在开飞机。飞行过程中，飞机上有一个"黑盒子"，它会记录下所有重要的信息：
- 什么时候起飞的？
- 飞行高度是多少？
- 引擎运转正常吗？
- 有没有遇到什么问题？

如果飞机出了问题，我们可以查看"黑盒子"的记录，找出问题出在哪里。

**日志系统**就是程序的"黑盒子"，它可以记录：
- 程序什么时候启动的？
- 用户做了什么操作？
- 数据处理得怎么样？
- 有没有报错？

当程序出问题时，我们可以查看日志，找出问题所在！

### 第一个例子

让我们用 3 分钟写一个简单的日志程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-logging-api</artifactId>
    <version>1.3.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-logging-console</artifactId>
    <version>1.3.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LoggingFirstExample {
    public static void main(String[] args) {
        // 1. 获取一个日志记录器（就像拿到黑盒子）
        Logger logger = ConsoleLogs.getLogger("MyFirstLogger");
        
        // 2. 记录不同级别的日志
        logger.trace("这是一条 TRACE 级别的日志 - 非常详细的信息");
        logger.debug("这是一条 DEBUG 级别的日志 - 调试信息");
        logger.info("这是一条 INFO 级别的日志 - 一般信息");
        logger.warn("这是一条 WARN 级别的日志 - 警告信息");
        logger.error("这是一条 ERROR 级别的日志 - 错误信息");
        
        System.out.println("\n✅ 日志记录完成！");
    }
}
```

运行这个程序，你会看到类似这样的输出：

```
[2024-01-15 10:30:00] [INFO] MyFirstLogger - 这是一条 INFO 级别的日志 - 一般信息
[2024-01-15 10:30:00] [WARN] MyFirstLogger - 这是一条 WARN 级别的日志 - 警告信息
[2024-01-15 10:30:00] [ERROR] MyFirstLogger - 这是一条 ERROR 级别的日志 - 错误信息

✅ 日志记录完成！
```

🎉 恭喜你！你已经学会了使用日志系统！

---

## 📖 基础篇

### 1. 核心概念

在深入学习之前，让我们先理解几个核心概念：

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **日志级别** | 日志的重要程度，从低到高：TRACE &lt; DEBUG &lt; INFO &lt; WARN &lt; ERROR | 天气警报：普通通知 &lt; 注意 &lt; 提醒 &lt; 警告 &lt; 紧急 |
| **日志记录器** | 记录日志的工具 | 黑盒子 |
| **Appender** | 日志输出的目的地（控制台、文件等） | 打印机，可以打印到纸上或屏幕上 |
| **Formatter** | 日志的格式 | 排版样式 |

### 2. 日志级别

EST Logging 提供了 5 个日志级别，让我们逐一了解：

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogLevelExample {
    public static void main(String[] args) {
        // 创建一个 DEBUG 级别的日志记录器
        Logger logger = ConsoleLogs.getLogger("LevelExample", LogLevel.DEBUG);
        
        System.out.println("=== 日志级别示例 ===\n");
        
        // TRACE - 最详细的信息，通常用于追踪代码执行流程
        logger.trace("进入了方法 A");
        logger.trace("变量 x 的值是: 100");
        
        // DEBUG - 调试信息，用于开发阶段
        logger.debug("正在处理用户请求");
        logger.debug("查询数据库，返回了 50 条记录");
        
        // INFO - 一般信息，记录程序的正常运行状态
        logger.info("程序启动成功");
        logger.info("用户张三登录了系统");
        
        // WARN - 警告信息，表示可能有问题，但不影响程序运行
        logger.warn("内存使用率超过 80%");
        logger.warn("接口响应时间超过 1 秒");
        
        // ERROR - 错误信息，表示出现了问题
        logger.error("数据库连接失败");
        logger.error("无法读取配置文件");
    }
}
```

**日志级别的使用场景：**

| 级别 | 使用场景 | 示例 |
|------|----------|------|
| **TRACE** | 非常详细的流程追踪 | "进入方法 calculateTotal()"、"循环第 5 次" |
| **DEBUG** | 开发调试信息 | "查询结果: [User(id=1, name='张三')]" |
| **INFO** | 重要的业务事件 | "用户登录成功"、"订单创建完成" |
| **WARN** | 潜在问题，但程序继续运行 | "内存使用率达到 85%"、"重试第 2 次" |
| **ERROR** | 错误，需要关注 | "数据库连接失败"、"支付处理异常" |

### 3. 创建日志记录器

EST Logging 提供了多种创建日志记录器的方式：

```java
import ltd.idcu.est.features.logging.api.LogConfig;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class CreateLoggerExample {
    public static void main(String[] args) {
        // 方式一：通过名称创建
        Logger logger1 = ConsoleLogs.getLogger("MyApplication");
        
        // 方式二：通过类创建（推荐）
        Logger logger2 = ConsoleLogs.getLogger(CreateLoggerExample.class);
        
        // 方式三：指定日志级别
        Logger logger3 = ConsoleLogs.getLogger("DebugLogger", LogLevel.DEBUG);
        
        // 方式四：使用 DEBUG 级别的快捷方法
        Logger logger4 = ConsoleLogs.debugLogger("DebugOnly");
        
        // 方式五：使用 TRACE 级别的快捷方法
        Logger logger5 = ConsoleLogs.traceLogger("TraceEverything");
        
        // 方式六：使用自定义配置
        LogConfig config = LogConfig.defaultConfig()
                .setLevel(LogLevel.INFO);
        Logger logger6 = ConsoleLogs.getLogger("CustomConfig", config);
        
        // 方式七：使用 Builder 模式
        Logger logger7 = ConsoleLogs.builder()
                .name("BuilderLogger")
                .level(LogLevel.DEBUG)
                .build();
        
        System.out.println("✅ 7 种日志记录器创建方式都成功了！");
    }
}
```

### 4. 记录日志

#### 4.1 基本日志记录

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class BasicLoggingExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger(BasicLoggingExample.class);
        
        // 简单的消息
        logger.info("程序启动");
        
        // 使用占位符（推荐）
        String username = "张三";
        int age = 25;
        logger.info("用户 {} 登录，年龄 {}", username, age);
        
        // 多个占位符
        logger.info("订单 {} 购买了 {} 件商品，总价 {} 元", 
                    "ORD-001", 3, 299.99);
    }
}
```

#### 4.2 记录异常

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class ExceptionLoggingExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger(ExceptionLoggingExample.class);
        
        try {
            // 可能会出错的代码
            int result = 10 / 0;
        } catch (Exception e) {
            // 记录异常
            logger.error("计算出错了", e);
        }
        
        try {
            String text = null;
            text.length();
        } catch (Exception e) {
            // 带消息的异常记录
            logger.error("处理文本时出错: 文本为 null", e);
        }
    }
}
```

#### 4.3 检查日志级别是否启用

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LevelCheckExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger("LevelCheck", LogLevel.INFO);
        
        // 检查级别是否启用
        System.out.println("TRACE 启用: " + logger.isTraceEnabled());  // false
        System.out.println("DEBUG 启用: " + logger.isDebugEnabled());  // false
        System.out.println("INFO 启用: " + logger.isInfoEnabled());    // true
        System.out.println("WARN 启用: " + logger.isWarnEnabled());    // true
        System.out.println("ERROR 启用: " + logger.isErrorEnabled());  // true
        
        // 对于复杂的日志消息，先检查级别可以提高性能
        if (logger.isDebugEnabled()) {
            String expensiveInfo = calculateExpensiveInfo();
            logger.debug("调试信息: {}", expensiveInfo);
        }
    }
    
    private static String calculateExpensiveInfo() {
        // 模拟耗时操作
        return "一些需要计算的信息";
    }
}
```

### 5. 控制台日志

控制台日志是最简单的日志输出方式：

```java
import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.time.format.DateTimeFormatter;

public class ConsoleLoggingExample {
    public static void main(String[] args) {
        // 1. 普通控制台日志
        Logger logger1 = ConsoleLogs.getLogger("NormalLogger");
        logger1.info("这是普通的控制台日志");
        
        // 2. 带颜色的控制台日志
        LogFormatter coloredFormatter = ConsoleLogs.coloredFormatter();
        Logger logger2 = ConsoleLogs.builder()
                .name("ColoredLogger")
                .formatter(coloredFormatter)
                .build();
        logger2.info("这是带颜色的控制台日志");
        
        // 3. 自定义时间格式
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LogFormatter customFormatter = ConsoleLogs.formatter(customFormat, true);
        Logger logger3 = ConsoleLogs.builder()
                .name("CustomFormatLogger")
                .formatter(customFormatter)
                .build();
        logger3.info("这是自定义时间格式的日志");
    }
}
```

---

## 🔧 进阶篇

### 1. 文件日志

除了控制台，我们还可以把日志输出到文件：

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.file.FileLogs;

import java.nio.file.Paths;

public class FileLoggingExample {
    public static void main(String[] args) {
        // 创建文件日志记录器
        Logger fileLogger = FileLogs.builder()
                .name("FileLogger")
                .filePath(Paths.get("app.log"))
                .build();
        
        // 记录日志到文件
        fileLogger.info("这条日志会写入到 app.log 文件");
        fileLogger.warn("这是一条警告");
        fileLogger.error("这是一条错误");
        
        System.out.println("✅ 日志已写入文件 app.log");
    }
}
```

### 2. 日志配置

我们可以通过 LogConfig 来配置日志系统：

```java
import ltd.idcu.est.features.logging.api.LogConfig;
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogConfigExample {
    public static void main(String[] args) {
        // 创建自定义配置
        LogConfig config = LogConfig.defaultConfig()
                .setLevel(LogLevel.DEBUG)  // 设置日志级别
                .setIncludeTimestamp(true)   // 包含时间戳
                .setIncludeLoggerName(true); // 包含日志记录器名称
        
        // 使用配置创建日志记录器
        Logger logger = ConsoleLogs.getLogger("ConfiguredLogger", config);
        
        logger.trace("TRACE 信息");
        logger.debug("DEBUG 信息");
        logger.info("INFO 信息");
        
        System.out.println("✅ 使用自定义配置的日志记录器创建成功！");
    }
}
```

### 3. 自定义日志格式

你可以创建自己的日志格式：

```java
import ltd.idcu.est.features.logging.api.LogFormatter;
import ltd.idcu.est.features.logging.api.LogRecord;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

// 自定义日志格式器
class MyLogFormatter implements LogFormatter {
    @Override
    public String format(LogRecord record) {
        return String.format("[%s] [%s] %s", 
                record.getLevel(), 
                record.getLoggerName(), 
                record.getMessage());
    }
}

public class CustomFormatterExample {
    public static void main(String[] args) {
        // 使用自定义格式器
        Logger logger = ConsoleLogs.builder()
                .name("CustomFormatLogger")
                .formatter(new MyLogFormatter())
                .build();
        
        logger.info("这是使用自定义格式的日志");
        logger.warn("这是警告信息");
    }
}
```

### 4. 日志统计

EST Logging 提供了日志统计功能：

```java
import ltd.idcu.est.features.logging.api.LogStats;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogStatsExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger("StatsLogger");
        
        // 记录一些日志
        for (int i = 0; i < 10; i++) {
            logger.info("这是第 {} 条 INFO 日志", i);
        }
        logger.warn("这是一条 WARN 日志");
        logger.error("这是一条 ERROR 日志");
        
        // 获取统计信息
        if (logger instanceof ltd.idcu.est.features.logging.api.AbstractLogger) {
            LogStats stats = ((ltd.idcu.est.features.logging.api.AbstractLogger) logger).getStats();
            
            System.out.println("=== 日志统计信息 ===");
            System.out.println("总日志数: " + stats.getTotalCount());
            System.out.println("TRACE 数: " + stats.getTraceCount());
            System.out.println("DEBUG 数: " + stats.getDebugCount());
            System.out.println("INFO 数: " + stats.getInfoCount());
            System.out.println("WARN 数: " + stats.getWarnCount());
            System.out.println("ERROR 数: " + stats.getErrorCount());
        }
    }
}
```

### 5. 与 EST Collection 集成

日志系统可以和 EST Collection 完美结合：

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.List;

public class LoggingCollectionIntegrationExample {
    public static void main(String[] args) {
        Logger logger = ConsoleLogs.getLogger(LoggingCollectionIntegrationExample.class);
        
        List<String> users = List.of("张三", "李四", "王五", "赵六");
        
        logger.info("开始处理 {} 个用户", users.size());
        
        // 使用 Collection 批量处理并记录日志
        Seqs.of(users)
                .filter(user -> {
                    logger.debug("检查用户: {}", user);
                    return !user.equals("王五");
                })
                .forEach(user -> {
                    logger.info("处理用户: {}", user);
                });
        
        logger.info("用户处理完成！");
    }
}
```

---

## 💡 最佳实践

### 1. 日志记录器命名规范

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class UserService {
    // ✅ 推荐：使用类名作为日志记录器名称
    private static final Logger logger = ConsoleLogs.getLogger(UserService.class);
    
    public void registerUser(String username) {
        logger.info("用户注册: {}", username);
        // ...
    }
}
```

### 2. 日志消息规范

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class LogMessageBestPractice {
    private static final Logger logger = ConsoleLogs.getLogger(LogMessageBestPractice.class);
    
    public void processOrder(String orderId) {
        // ✅ 好的日志消息：清晰、描述性强
        logger.info("开始处理订单: {}", orderId);
        
        // ❌ 不好的日志消息：信息太少
        logger.info("处理中...");
        
        // ✅ 使用占位符，不要用字符串拼接
        String user = "张三";
        int amount = 100;
        logger.info("用户 {} 消费了 {} 元", user, amount);  // 推荐
        
        // ❌ 不要这样做（性能差）
        logger.info("用户 " + user + " 消费了 " + amount + " 元");  // 不推荐
    }
}
```

### 3. 异常处理规范

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class ExceptionHandlingBestPractice {
    private static final Logger logger = ConsoleLogs.getLogger(ExceptionHandlingBestPractice.class);
    
    public void doSomething() {
        try {
            // 可能出错的代码
        } catch (Exception e) {
            // ✅ 好的做法：记录异常
            logger.error("操作失败", e);
        }
        
        try {
            // 可能出错的代码
        } catch (Exception e) {
            // ❌ 不好的做法：吞掉异常
        }
        
        try {
            // 可能出错的代码
        } catch (Exception e) {
            // ❌ 不好的做法：只记录消息，不记录异常
            logger.error("操作失败");
        }
    }
}
```

### 4. 性能优化建议

```java
import ltd.idcu.est.features.logging.api.LogLevel;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class PerformanceBestPractice {
    private static final Logger logger = ConsoleLogs.getLogger(PerformanceBestPractice.class);
    
    public void processData() {
        // ✅ 对于复杂的日志消息，先检查级别
        if (logger.isDebugEnabled()) {
            String expensiveData = computeExpensiveData();
            logger.debug("处理数据: {}", expensiveData);
        }
        
        // ❌ 不要这样做（即使 DEBUG 级别禁用，也会执行 computeExpensiveData）
        logger.debug("处理数据: {}", computeExpensiveData());
    }
    
    private String computeExpensiveData() {
        // 模拟耗时操作
        return "一些需要计算的数据";
    }
}
```

### 5. 完整示例：用户服务

让我们用日志系统构建一个用户服务：

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.HashMap;
import java.util.Map;

class User {
    private String id;
    private String username;
    private String email;
    
    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

public class UserServiceWithLogging {
    private static final Logger logger = ConsoleLogs.getLogger(UserServiceWithLogging.class);
    private Map<String, User> users = new HashMap<>();
    
    public User registerUser(String id, String username, String email) {
        logger.info("开始注册用户: username={}, email={}", username, email);
        
        try {
            if (users.containsKey(id)) {
                logger.warn("用户已存在: id={}", id);
                throw new IllegalArgumentException("用户已存在");
            }
            
            User user = new User(id, username, email);
            users.put(id, user);
            
            logger.info("用户注册成功: id={}, username={}", id, username);
            logger.debug("当前用户数: {}", users.size());
            
            return user;
        } catch (Exception e) {
            logger.error("用户注册失败: username={}", username, e);
            throw e;
        }
    }
    
    public User getUser(String id) {
        logger.debug("查询用户: id={}", id);
        
        User user = users.get(id);
        if (user == null) {
            logger.warn("用户不存在: id={}", id);
        } else {
            logger.info("查询到用户: id={}, username={}", id, user.getUsername());
        }
        
        return user;
    }
    
    public static void main(String[] args) {
        logger.info("=== 用户服务示例 ===");
        
        UserServiceWithLogging service = new UserServiceWithLogging();
        
        // 注册用户
        service.registerUser("1", "张三", "zhangsan@example.com");
        service.registerUser("2", "李四", "lisi@example.com");
        
        // 查询用户
        service.getUser("1");
        service.getUser("3");  // 不存在的用户
        
        logger.info("=== 示例执行完成 ===");
    }
}
```

---

## 🎯 总结

恭喜你！你已经完整学习了 EST Logging 日志系统！

让我们回顾一下重点：

1. **核心概念**：日志级别、日志记录器、Appender、Formatter
2. **日志级别**：TRACE &lt; DEBUG &lt; INFO &lt; WARN &lt; ERROR
3. **基本操作**：创建日志记录器、记录日志、记录异常
4. **高级功能**：文件日志、自定义配置、日志统计
5. **最佳实践**：命名规范、消息规范、异常处理、性能优化

日志是程序的"黑盒子"，好好利用它，你可以更快地发现和解决问题！

下一章，我们将学习 EST Monitor 监控系统，不见不散！🎉
