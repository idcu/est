# est-web-session - 小白从入门到精通

## 目录
- [什么是 est-web-session](#什么是-est-web-session)
- [快速入门：5分钟上手](#快速入门5分钟上手)
- [基础篇：核心功能](#基础篇核心功能)
- [进阶篇：高级用法](#进阶篇高级用法)
- [最佳实践](#最佳实践)
- [模块结构](#模块结构)
- [相关资源](#相关资源)

---

## 什么是 est-web-session

### 用大白话理解
est-web-session 就像网站给每个用户发的"会员卡"。当你第一次访问网站时，系统给你一张卡（Session），上面记着你的登录状态、购物车商品等信息。下次再来，刷一下卡，网站就知道是你了。

### 核心特点
- **多存储后端**：支持内存、Redis、数据库等多种存储方式
- **自动管理**：自动处理 Session 的创建、过期、销毁
- **分布式支持**：集群环境下 Session 共享
- **安全加固**：防劫持、防篡改机制

---

## 快速入门：5分钟上手

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-web-session</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 配置 Session
```yaml
est:
  web:
    session:
      timeout: 3600  # Session 过期时间（秒）
      store-type: memory  # 存储类型：memory/redis/jdbc
```

### 3. 使用 Session
```java
@Controller
public class UserController {
    
    @Get("/login")
    public void login(String username, String password, Session session) {
        if (validate(username, password)) {
            session.setAttribute("userId", getUserId(username));
            session.setAttribute("username", username);
        }
    }
    
    @Get("/profile")
    public String profile(Session session) {
        String username = session.getAttribute("username");
        return "欢迎回来，" + username;
    }
    
    @Get("/logout")
    public void logout(Session session) {
        session.invalidate();
    }
}
```

---

## 基础篇：核心功能

### 1. Session 基本操作

#### 创建和获取 Session
```java
// 获取 Session，如果不存在则创建
Session session = request.getSession(true);

// 仅获取已存在的 Session
Session session = request.getSession(false);
```

#### 存取属性
```java
// 存储属性
session.setAttribute("user", user);
session.setAttribute("cart", cart);

// 获取属性
User user = session.getAttribute("user");
Cart cart = session.getAttribute("cart");

// 删除属性
session.removeAttribute("cart");

// 清空所有属性
session.clearAttributes();
```

#### Session 生命周期
```java
// 获取 Session ID
String sessionId = session.getId();

// 获取创建时间
long createTime = session.getCreationTime();

// 获取最后访问时间
long lastAccessTime = session.getLastAccessedTime();

// 设置最大不活动时间（秒）
session.setMaxInactiveInterval(1800);

// 销毁 Session
session.invalidate();
```

### 2. 多种存储后端

#### 内存存储（开发环境）
```yaml
est:
  web:
    session:
      store-type: memory
```

#### Redis 存储（生产环境）
```yaml
est:
  web:
    session:
      store-type: redis
      redis:
        namespace: est:session
        key-prefix: sess:
```

#### 数据库存储
```yaml
est:
  web:
    session:
      store-type: jdbc
      table-name: est_sessions
```

### 3. Session 监听器

#### 监听 Session 创建和销毁
```java
@WebListener
public class MySessionListener implements SessionListener {
    
    @Override
    public void sessionCreated(SessionEvent event) {
        System.out.println("Session 创建: " + event.getSession().getId());
    }
    
    @Override
    public void sessionDestroyed(SessionEvent event) {
        System.out.println("Session 销毁: " + event.getSession().getId());
    }
}
```

#### 监听属性变化
```java
@WebListener
public class MyAttributeListener implements SessionAttributeListener {
    
    @Override
    public void attributeAdded(SessionAttributeEvent event) {
        System.out.println("属性添加: " + event.getName());
    }
    
    @Override
    public void attributeRemoved(SessionAttributeEvent event) {
        System.out.println("属性删除: " + event.getName());
    }
    
    @Override
    public void attributeReplaced(SessionAttributeEvent event) {
        System.out.println("属性替换: " + event.getName());
    }
}
```

---

## 进阶篇：高级用法

### 1. 分布式 Session 共享

#### 配置 Redis 会话存储
```java
@Configuration
public class SessionConfig {
    
    @Bean
    public SessionStore sessionStore(RedisClient redisClient) {
        return new RedisSessionStore(redisClient)
            .setNamespace("est:session")
            .setKeyPrefix("sess:");
    }
}
```

#### 会话粘性配置（可选）
```yaml
est:
  web:
    session:
      sticky: true  # 启用会话粘性
      cookie:
        name: SESSIONID
        http-only: true
        secure: false
        same-site: LAX
```

### 2. Session 安全加固

#### 配置安全选项
```yaml
est:
  web:
    session:
      cookie:
        http-only: true      # 防止 XSS 读取 Cookie
        secure: true         # 仅 HTTPS 传输
        same-site: STRICT    # 防止 CSRF
      regeneration:
        enabled: true        # 登录后重新生成 Session ID
        on-login: true
      fixation:
        prevention: true     # 防止会话固定攻击
```

#### 自定义 Session ID 生成器
```java
public class SecureSessionIdGenerator implements SessionIdGenerator {
    
    @Override
    public String generate() {
        return SecureRandomUtil.generateSecureId(32);
    }
}
```

### 3. Session 持久化和恢复

#### 手动持久化
```java
public class SessionBackupService {
    
    @Inject
    private SessionStore sessionStore;
    
    public void backupSession(String sessionId) {
        Session session = sessionStore.load(sessionId);
        if (session != null) {
            byte[] data = serialize(session);
            saveToBackup(sessionId, data);
        }
    }
    
    public Session restoreSession(String sessionId) {
        byte[] data = loadFromBackup(sessionId);
        if (data != null) {
            Session session = deserialize(data);
            sessionStore.save(session);
            return session;
        }
        return null;
    }
}
```

### 4. 会话集群监控

#### 会话统计
```java
@Service
public class SessionMonitor {
    
    @Inject
    private SessionRegistry sessionRegistry;
    
    public SessionStats getStats() {
        return SessionStats.builder()
            .activeSessions(sessionRegistry.getActiveSessionCount())
            .totalSessions(sessionRegistry.getTotalSessionCount())
            .expiredSessions(sessionRegistry.getExpiredSessionCount())
            .build();
    }
    
    public List<SessionInfo> listActiveSessions() {
        return sessionRegistry.getActiveSessions().stream()
            .map(this::toSessionInfo)
            .collect(Collectors.toList());
    }
}
```

---

## 最佳实践

### ✅ 推荐做法

| 场景 | 推荐做法 | 说明 |
|------|---------|------|
| 存储选择 | 生产用 Redis，开发用内存 | Redis 性能好、支持分布式 |
| 过期时间 | 30分钟-2小时 | 平衡安全和用户体验 |
| 敏感数据 | 不要存密码，只存用户 ID | Session 不是安全存储 |
| 登录处理 | 登录后重新生成 Session ID | 防止会话固定攻击 |
| Cookie 设置 | HttpOnly + Secure + SameSite | 多层安全防护 |

### ❌ 不推荐做法

```java
// ❌ 不要在 Session 中存大量数据
session.setAttribute("bigData", hugeObject);  // 内存爆炸

// ❌ 不要存敏感信息
session.setAttribute("password", "123456");  // 安全风险

// ❌ 不要依赖 Session 做持久化
session.setAttribute("order", order);  // 重启就丢失

// ✅ 应该用数据库存
orderRepository.save(order);
session.setAttribute("orderId", order.getId());
```

---

## 模块结构

```
est-web-session/
├── est-web-session-api/          # API 接口定义
│   ├── src/main/java/
│   │   └── ltd/idcu/est/web/session/
│   │       ├── Session.java              # Session 接口
│   │       ├── SessionStore.java         # Session 存储接口
│   │       ├── SessionListener.java      # Session 监听器
│   │       └── SessionConfig.java        # 配置接口
│   └── pom.xml
├── est-web-session-impl/         # 实现模块
│   ├── src/main/java/
│   │   └── ltd/idcu/est/web/session/
│   │       ├── MemorySessionStore.java   # 内存存储
│   │       ├── RedisSessionStore.java    # Redis 存储
│   │       ├── JdbcSessionStore.java     # 数据库存储
│   │       └── DefaultSession.java       # 默认实现
│   └── pom.xml
├── README.md
└── pom.xml
```

---

## 相关资源

- [父模块文档](../README.md)
- [Web 路由模块](../est-web-router/README.md)
- [Web 中间件模块](../est-web-middleware/README.md)
- [API 网关](../est-gateway/README.md)
- [EST 快速入门](../../docs/getting-started/README.md)
- [示例代码](../../est-examples/est-examples-web/)
