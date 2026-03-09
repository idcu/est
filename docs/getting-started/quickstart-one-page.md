# EST 框架 - 一页纸快速上手指南
=================================

> 🚀 **5 分钟从 0 到能跑！**

---

## 第一步：环境准备 (1 分钟)

**必需：**
- JDK 21+
- Maven 3.6+

**检查环境：**
```bash
java -version
mvn -version
```

---

## 第二步：3 分钟跑一个 Web 应用 (2 分钟)

### 2.1 创建 pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>est-first-app</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-web</artifactId>
            <version>2.3.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

### 2.2 写代码 (HelloWorld.java)
```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        WebApplication app = Web.create("我的第一个 EST 应用", "1.0.0");
        
        app.get("/", (req, res) -> res.send("Hello, EST World! 🎉"));
        app.get("/api/user/:name", (req, res) -> 
            res.json(Map.of("name", req.getPathParam("name"))));
        
        app.run(8080);
    }
}
```

### 2.3 运行它
```bash
mvn compile exec:java -Dexec.mainClass="HelloWorld"
```

访问：http://localhost:8080

---

## 第三步：加缓存 (30 秒)

```java
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.MemoryCache;

Cache<String, String> cache = new MemoryCache<>();
cache.put("key", "我是缓存的值");
System.out.println(cache.get("key"));
```

---

## 第四步：用 AI (30 秒)

```java
import ltd.idcu.est.llm.LlmClient;
import ltd.idcu.est.llm.openai.OpenAiLlmClient;
import ltd.idcu.est.llm.ChatRequest;
import ltd.idcu.est.llm.ChatMessage;

LlmClient client = OpenAiLlmClient.builder()
    .apiKey("你的-API-Key")
    .model("gpt-4")
    .build();

ChatRequest request = ChatRequest.builder()
    .addMessage(ChatMessage.user("你好，请介绍一下 EST 框架"))
    .build();

System.out.println(client.chat(request).getContent());
```

---

## 常见场景速查
==============

### 场景 1：我要做一个 REST API
```java
app.get("/api/users", (req, res) -> res.json(userService.findAll()));
app.post("/api/users", (req, res) -> res.json(userService.create(req.getBodyAs(User.class))));
app.put("/api/users/:id", (req, res) -> res.json(userService.update(req.getPathParam("id"), req.getBodyAs(User.class))));
app.delete("/api/users/:id", (req, res) -> res.status(204));
```

### 场景 2：我要用数据库
```java
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.jdbc.JdbcData;

Repository<User, Long> repo = JdbcData.newRepository(dataSource, User.class);
User user = repo.findById(1L).orElse(null);
repo.save(newUser);
```

### 场景 3：我要用消息队列
```java
import ltd.idcu.est.messaging.MessageProducer;
import ltd.idcu.est.messaging.kafka.KafkaProducer;

MessageProducer producer = KafkaProducer.create("localhost:9092");
producer.send("orders", orderMessage);
```

### 场景 4：我要加定时任务
```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.Schedulers;

Scheduler scheduler = Schedulers.create();
scheduler.scheduleAtFixedRate(() -> System.out.println("每小时执行一次"), 1, TimeUnit.HOURS);
scheduler.scheduleCron(() -> System.out.println("每天 9 点执行"), "0 0 9 * * ?");
```

### 场景 5：我要加安全认证
```java
import ltd.idcu.est.security.jwt.JwtService;

JwtService jwtService = JwtService.create();
String token = jwtService.generateToken(user);
User authenticated = jwtService.validateToken(token);
```

---

## 性能优化开关
=============

### 开启性能模式 (减少 30% 开销)
```java
WebApplication app = Web.create("我的应用", "1.0.0")
    .enablePerformanceMode(true);  // 开启性能模式
```

### 使用轻量级模块
```xml
<!-- 只引入你真正需要的 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-web-lite</artifactId>
    <version>2.3.0-SNAPSHOT</version>
</dependency>
```

---

## 下一步
====

- 👉 详细文档：[README.md](README.md)
- 👉 更多示例：[est-examples/](est-examples/)
- 👉 AI 开发：[est-ai-suite/](est-modules/est-ai-suite/)
- 👉 管理后台：[est-admin/](est-app/est-admin/)

---

**恭喜！你已经是 EST 开发者了！** 🎊

---
文档版本: 1.0
最后更新: 2026-03-09
