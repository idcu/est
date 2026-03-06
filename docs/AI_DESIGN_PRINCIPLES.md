# EST 框架 AI 设计原则

> 专为 AI 编程优化的设计原则和最佳实践

## 📋 目录

- [核心理念](#核心理念)
- [API 设计原则](#api-设计原则)
- [代码风格原则](#代码风格原则)
- [架构设计原则](#架构设计原则)
- [文档原则](#文档原则)
- [示例原则](#示例原则)
- [测试原则](#测试原则)
- [AI 辅助开发工具](#ai-辅助开发工具)
- [最佳实践](#最佳实践)

---

## 🎯 核心理念

EST 框架的 AI 友好设计基于以下核心理念：

### 1. 零依赖原则
- **最小化外部依赖**：优先使用 Java 标准库
- **减少决策负担**：AI 不需要选择和管理依赖版本
- **避免版本冲突**：单一版本，无兼容性问题

### 2. 一致性原则
- **统一的 API 风格**：所有模块使用相同的命名约定
- **可预测的行为**：相同的模式产生相同的结果
- **标准化的实现**：提供默认实现，减少选择

### 3. 渐进式原则
- **零配置启动**：最小化初始配置，快速上手
- **按需引入**：模块化设计，按需添加功能
- **平滑升级**：API 保持向后兼容

### 4. 自描述原则
- **语义化命名**：类名、方法名、参数名清晰表达意图
- **完整的 Javadoc**：每个公共 API 都有详细说明
- **示例驱动**：每个功能都配有可运行的示例

---

## 🔧 API 设计原则

### 1. 工厂方法优先
**好的设计：**
```java
// 使用工厂方法
Cache<String, User> cache = Caches.newMemoryCache();
EventBus eventBus = EventBuses.newLocalEventBus();
Logger logger = Loggers.newConsoleLogger();
```

**避免的设计：**
```java
// 直接使用构造函数
Cache<String, User> cache = new MemoryCache<>();
```

### 2. 链式 API
**好的设计：**
```java
// 链式调用
config.set("app.name", "MyApp")
      .set("app.version", "1.0.0")
      .set("server.port", 8080);

validator.validateRequired(name, "name")
         .validateLength(name, "name", 3, 50)
         .validateEmail(email, "email")
         .throwIfInvalid();
```

### 3. 明确的返回类型
**好的设计：**
```java
// 使用 Optional 明确可能为空
Optional<User> user = userRepository.findById(id);

// 使用 List 明确是集合
List<User> users = userRepository.findAll();
```

**避免的设计：**
```java
// 返回 null，AI 可能忘记处理
User user = userRepository.findById(id);
```

### 4. 简单的方法签名
**好的设计：**
```java
// 参数少，类型明确
public void get(String id, Request req, Response res) {
    // 实现
}

public User create(String name, String email) {
    // 实现
}
```

**避免的设计：**
```java
// 参数过多，容易混淆
public void process(String id, String name, String email, 
                    String phone, String address, int age, 
                    boolean active, Date createdAt) {
    // 实现
}
```

### 5. 语义化的方法名
**好的设计：**
```java
// 方法名清晰表达意图
userRepository.findById(id);
userRepository.save(user);
userRepository.delete(id);
userRepository.existsById(id);
userRepository.count();
```

**避免的设计：**
```java
// 方法名模糊不清
userRepository.get(id);
userRepository.put(user);
userRepository.remove(id);
userRepository.has(id);
userRepository.size();
```

---

## 📝 代码风格原则

### 1. 清晰的命名约定

#### 类名
```java
// 使用大驼峰，后缀明确类型
public class UserController { }           // Controller
public class UserService { }              // Service
public class UserRepository { }           // Repository
public class User { }                     // Model
public class UserDTO { }                  // DTO
public class UserValidator { }            // Validator
public class UserCache { }                // Cache
public class UserEvent { }                // Event
public class UserListener { }             // Listener
```

#### 方法名
```java
// 使用小驼峰，动词开头
public User getUser(String id) { }
public User createUser(User user) { }
public User updateUser(String id, User user) { }
public void deleteUser(String id) { }
public List<User> listUsers() { }
```

#### 变量名
```java
// 使用小驼峰，语义明确
private final Map<String, User> users = new ConcurrentHashMap<>();
private int nextId = 1;
private static final int MAX_RETRIES = 3;
private static final long DEFAULT_TIMEOUT = 30000L;
```

#### 常量名
```java
// 全大写下划线分隔
public static final String DEFAULT_APP_NAME = "EST Application";
public static final int DEFAULT_PORT = 8080;
public static final long DEFAULT_TTL = 300000L;
public static final boolean DEFAULT_DEBUG = false;
```

### 2. 合理的代码组织

```java
// 顺序：常量 → 字段 → 构造函数 → 公共方法 → 私有方法
public class UserService {
    
    // 1. 常量
    private static final int MAX_RETRIES = 3;
    
    // 2. 字段
    private final UserRepository repository;
    private final Cache<String, User> cache;
    
    // 3. 构造函数
    public UserService(UserRepository repository, Cache<String, User> cache) {
        this.repository = repository;
        this.cache = cache;
    }
    
    // 4. 公共方法
    public User getUser(String id) {
        return cache.get(id).orElseGet(() -> {
            User user = repository.findById(id).orElse(null);
            if (user != null) {
                cache.put(id, user);
            }
            return user;
        });
    }
    
    public User createUser(User user) {
        User saved = repository.save(user);
        cache.put(saved.getId(), saved);
        return saved;
    }
    
    // 5. 私有方法
    private void validateUser(User user) {
        // 验证逻辑
    }
}
```

### 3. 适当的注释

```java
/**
 * 用户服务 - 处理用户相关的业务逻辑
 * 
 * 功能包括：
 * - 用户查询
 * - 用户创建
 * - 用户更新
 * - 用户删除
 */
public class UserService {
    
    /**
     * 根据 ID 获取用户
     * 
     * @param id 用户 ID
     * @return 用户对象，如果不存在返回 null
     */
    public User getUser(String id) {
        // 实现
    }
    
    /**
     * 创建新用户
     * 
     * @param user 用户信息
     * @return 创建后的用户
     * @throws IllegalArgumentException 如果用户信息无效
     */
    public User createUser(User user) {
        // 实现
    }
}
```

---

## 🏗️ 架构设计原则

### 1. 分层架构

```
my-app/
├── controller/          # Controller 层 - HTTP 请求处理
│   └── UserController.java
├── service/             # Service 层 - 业务逻辑
│   └── UserService.java
├── repository/          # Repository 层 - 数据访问
│   └── UserRepository.java
├── model/               # Model 层 - 数据模型
│   └── User.java
├── dto/                 # DTO 层 - 数据传输对象
│   └── UserDTO.java
├── validator/           # Validator - 数据验证
│   └── UserValidator.java
├── config/              # Config - 配置管理
│   └── AppConfig.java
└── Main.java            # 入口类
```

### 2. 接口与实现分离

```java
// 接口定义
public interface UserService {
    User getUser(String id);
    User createUser(User user);
    User updateUser(String id, User user);
    void deleteUser(String id);
    List<User> listUsers();
}

// 实现类
public class UserServiceImpl implements UserService {
    // 实现
}

// 使用依赖注入
container.register(UserService.class, UserServiceImpl.class);
UserService service = container.get(UserService.class);
```

### 3. 单一职责原则

**好的设计：**
```java
// 每个类只负责一件事
public class UserController {      // 只处理 HTTP 请求
public class UserService {         // 只处理业务逻辑
public class UserRepository {      // 只处理数据访问
public class UserValidator {       // 只处理数据验证
public class UserCache {           // 只处理缓存
```

**避免的设计：**
```java
// 一个类负责太多事情
public class UserManager {         // 既处理 HTTP，又处理业务，还处理数据访问
    public void handleRequest(Request req, Response res) { }
    public User createUser(User user) { }
    public void saveToDatabase(User user) { }
}
```

---

## 📚 文档原则

### 1. 完整的 Javadoc

每个公共类、接口、方法都应该有 Javadoc：

```java
/**
 * Web 应用程序接口
 * 
 * 提供 HTTP 服务器功能，支持路由定义、中间件、静态文件服务等。
 * 
 * 示例用法：
 * <pre>{@code
 * WebApplication app = Web.create("MyApp", "1.0.0");
 * app.get("/", (req, res) -> res.send("Hello"));
 * app.run(8080);
 * }</pre>
 */
public interface WebApplication {
    
    /**
     * 添加 GET 路由
     * 
     * @param path 路由路径
     * @param handler 路由处理器
     * @return 当前应用实例，用于链式调用
     */
    WebApplication get(String path, RouteHandler handler);
    
    /**
     * 启动服务器
     * 
     * @param port 监听端口
     */
    void run(int port);
}
```

### 2. 可运行的示例

每个功能都应该配有完整的可运行示例：

```java
/**
 * 缓存使用示例
 * 
 * 演示如何使用 EST 缓存模块。
 */
public class CacheExample {
    
    public static void main(String[] args) {
        // 创建缓存
        Cache<String, User> cache = Caches.newMemoryCache();
        
        // 存储数据
        cache.put("1", new User("1", "张三"));
        cache.put("2", new User("2", "李四"));
        
        // 读取数据
        Optional<User> user = cache.get("1");
        user.ifPresent(u -> System.out.println(u.getName()));
        
        // 获取缓存统计
        System.out.println("Hit rate: " + cache.getHitRate());
        
        // 清理资源
        cache.shutdown();
    }
}
```

### 3. 快速参考卡

提供简洁的快速参考卡，方便 AI 快速查找：

```markdown
## Web API 速查

| 功能 | 代码 |
|------|------|
| 创建应用 | `WebApplication app = Web.create("name", "version");` |
| GET 路由 | `app.get("/path", (req, res) -> { ... });` |
| 获取参数 | `String id = req.param("id");` |
| 返回 JSON | `res.json(data);` |
| 启动服务 | `app.run(8080);` |
```

---

## 💡 示例原则

### 1. 最小化示例

示例应该只包含必要的代码：

**好的示例：**
```java
// 简洁明了
public class HelloWorld {
    public static void main(String[] args) {
        WebApplication app = Web.create("Hello", "1.0.0");
        app.get("/", (req, res) -> res.send("Hello, World!"));
        app.run(8080);
    }
}
```

**避免的示例：**
```java
// 过于复杂，包含太多无关代码
public class HelloWorld {
    private static final Logger logger = Loggers.newConsoleLogger();
    private static final Config config = new DefaultConfig();
    
    public static void main(String[] args) {
        logger.info("Starting application...");
        
        WebApplication app = Web.create("Hello", "1.0.0");
        
        app.use(new LoggingMiddleware());
        app.use(new CorsMiddleware());
        
        app.get("/", (req, res) -> {
            logger.debug("Handling request...");
            String name = req.queryParam("name", "World");
            String message = String.format("Hello, %s!", name);
            res.send(message);
        });
        
        app.onStartup(() -> logger.info("Application started"));
        app.onShutdown(() -> logger.info("Application stopped"));
        
        app.run(config.getInt("port", 8080));
    }
}
```

### 2. 渐进式示例

提供从简单到复杂的示例：

```
examples/
├── 01-hello-world/          # 最简单的示例
├── 02-basic-web/            # 基础 Web 应用
├── 03-rest-api/             # REST API
├── 04-dependency-injection/ # 依赖注入
├── 05-caching/              # 缓存
├── 06-event-driven/         # 事件驱动
├── 07-full-stack/           # 完整应用
└── 08-microservices/        # 微服务
```

### 3. 可运行示例

所有示例都应该可以直接运行：

```bash
cd examples/01-hello-world
mvn clean install
mvn exec:java
```

---

## 🧪 测试原则

### 1. 测试命名

```java
// 使用 given-when-then 模式
@Test
void givenValidUser_whenCreateUser_thenUserIsSaved() { }

@Test
void givenNonExistentId_whenGetUser_thenReturnsEmpty() { }

@Test
void givenInvalidEmail_whenValidate_thenThrowsException() { }
```

### 2. 测试结构

```java
class UserServiceTest {
    
    private UserService service;
    private UserRepository repository;
    private Cache<String, User> cache;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        cache = Caches.newMemoryCache();
        service = new UserService(repository, cache);
    }
    
    @Test
    void shouldCreateUser() {
        // Given
        User user = new User("1", "张三", "zhangsan@example.com");
        
        // When
        User created = service.createUser(user);
        
        // Then
        assertThat(created.getId()).isEqualTo("1");
        assertThat(created.getName()).isEqualTo("张三");
        assertThat(repository.findById("1")).isPresent();
    }
    
    @AfterEach
    void tearDown() {
        cache.shutdown();
    }
}
```

---

## 🛠️ AI 辅助开发工具

### 1. AICodeAssistant

EST 提供了 AI 辅助开发工具：

```bash
# 列出可用模式
java AICodeAssistant list-patterns

# 生成提示词
java AICodeAssistant generate-prompt web-app

# 分析代码
java AICodeAssistant analyze-code MyClass.java

# 建议重构
java AICodeAssistant suggest-refactor MyClass.java
```

### 2. CodeSnippetGenerator

使用代码片段生成器快速创建代码：

```bash
# 列出可用模板
java -jar est-scaffold.jar snippet list

# 生成 Controller
java -jar est-scaffold.jar snippet controller UserController.java \
    package=com.example \
    className=UserController \
    model=User \
    resource=users

# 生成 Repository
java -jar est-scaffold.jar snippet repository UserRepository.java \
    package=com.example \
    className=UserRepository \
    model=User

# 生成 Service
java -jar est-scaffold.jar snippet service UserService.java \
    package=com.example \
    className=UserService \
    model=User

# 生成 Validator
java -jar est-scaffold.jar snippet validator UserValidator.java \
    package=com.example \
    className=UserValidator

# 生成 Cache
java -jar est-scaffold.jar snippet cache UserCache.java \
    package=com.example \
    className=UserCache

# 生成 Logger
java -jar est-scaffold.jar snippet logger AppLogger.java \
    package=com.example \
    className=AppLogger

# 生成 Scheduler
java -jar est-scaffold.jar snippet scheduler TaskScheduler.java \
    package=com.example \
    className=TaskScheduler

# 生成 Config
java -jar est-scaffold.jar snippet config AppConfig.java \
    package=com.example \
    className=AppConfig
```

---

## ✅ 最佳实践

### 1. AI 代码生成工作流

```
1. 理解需求
   └─ 明确功能需求和技术约束

2. 参考文档
   ├─ AI_CODER_GUIDE.md
   ├─ AI_PROMPTS.md
   ├─ PRACTICAL_EXAMPLES.md
   └─ QUICK_REFERENCE.md

3. 生成代码
   ├─ 使用 AICodeAssistant 生成提示词
   ├─ 使用 CodeSnippetGenerator 生成模板
   └─ 让 AI 生成完整代码

4. 审查代码
   ├─ 检查 API 使用是否正确
   ├─ 检查是否符合设计原则
   └─ 使用 AICodeAssistant analyze-code

5. 测试验证
   ├─ 运行单元测试
   ├─ 运行集成测试
   └─ 手动测试验证

6. 迭代优化
   ├─ 根据反馈调整
   ├─ 使用 AICodeAssistant suggest-refactor
   └─ 持续改进
```

### 2. 快速检查清单

在让 AI 生成代码前，检查以下内容：

- [ ] 需求是否明确？
- [ ] 技术约束是否清晰？
- [ ] 是否参考了 EST 文档？
- [ ] 是否有类似的示例可以参考？
- [ ] 代码结构是否符合分层架构？
- [ ] API 使用是否正确？
- [ ] 错误处理是否完善？
- [ ] 是否有测试覆盖？

### 3. 常见问题

**Q: AI 生成的代码不符合 EST 风格怎么办？**

A: 
1. 在提示词中明确要求遵循 EST 最佳实践
2. 提供 EST 示例代码作为参考
3. 使用 CodeSnippetGenerator 生成基础模板
4. 使用 AICodeAssistant analyze-code 检查

**Q: 如何让 AI 生成更好的代码？**

A:
1. 提供清晰、详细的需求
2. 给出输入输出示例
3. 明确技术约束
4. 参考相关文档和示例
5. 分步骤生成，逐步细化

**Q: 如何验证 AI 生成的代码？**

A:
1. 检查 API 使用是否正确
2. 运行编译检查
3. 运行单元测试
4. 手动测试关键功能
5. 使用 AICodeAssistant analyze-code 分析
6. 代码审查

---

## 📖 相关文档

- [AI_CODER_GUIDE.md](./AI_CODER_GUIDE.md) - AI 编码指南
- [AI_PROMPTS.md](./AI_PROMPTS.md) - AI 提示词模板
- [PRACTICAL_EXAMPLES.md](./PRACTICAL_EXAMPLES.md) - 实际使用示例
- [QUICK_REFERENCE.md](./QUICK_REFERENCE.md) - 快速参考
- [ARCHITECTURE.md](./ARCHITECTURE.md) - 架构设计

---

**AI 友好设计，让编码更简单！** 🚀
