# EST 框架常见问题（FAQ）

本文档收集了使用 EST 框架时最常见的问题及其解答。

---

## 目录

1. [入门问题](#1-入门问题)
2. [安装与配置](#2-安装与配置)
3. [容器与依赖注入](#3-容器与依赖注入)
4. [Web 开发](#4-web-开发)
5. [数据访问](#5-数据访问)
6. [性能问题](#6-性能问题)
7. [调试与问题排查](#7-调试与问题排查)
8. [与其他框架对比](#8-与其他框架对比)
9. [贡献与社区](#9-贡献与社区)

---

## 1. 入门问题

### Q1.1: EST 框架是什么？

EST 是一个**零依赖**的现代 Java 框架，采用递进式模块结构设计。它的特点包括：

- **零依赖**：完全使用 Java 标准库，无需任何第三方依赖
- **简单易用**：API 设计简洁直观
- **功能完整**：包含 Web 开发、缓存、日志、数据库访问等企业级功能
- **AI 友好**：专为 AI 代码生成场景优化

### Q1.2: EST 和 Spring Boot 有什么区别？

| 特性 | EST | Spring Boot |
|------|-----|-------------|
| 依赖 | 零依赖 | 重度依赖 |
| 启动速度 | ~0.5s | ~3-5s |
| 内存占用 | ~30MB | ~200-500MB |
| 学习曲线 | 平缓 | 陡峭 |
| 生态 | 新兴 | 超大规模 |

**选择建议**：
- 选择 EST：如果你需要零依赖、学习框架原理、AI 代码生成场景
- 选择 Spring Boot：如果你需要成熟生态、企业级应用、大量第三方集成

### Q1.3: EST 支持哪些 JDK 版本？

EST 框架要求 **JDK 21 或更高版本**。这是因为：

- 使用了 Java 21 的虚拟线程特性
- 利用了现代 Java 的新特性
- 保持代码库的现代性

### Q1.4: 我是初学者，应该从哪里开始？

建议按以下顺序学习：

1. **阅读快速开始指南**：`docs/guides/getting-started.md`
2. **运行基础示例**：查看 `est-examples/est-examples-basic/`
3. **尝试 Web 开发**：查看 `est-examples/est-examples-web/`
4. **阅读教程**：`docs/tutorials/` 目录下的教程
5. **参考 API 文档**：`docs/api/` 目录

---

## 2. 安装与配置

### Q2.1: 如何安装 EST 框架？

EST 框架使用 Maven 构建，安装步骤：

```bash
# 1. 克隆仓库
git clone https://github.com/idcu/est.git
cd est

# 2. 构建项目
mvn clean install

# 3. 跳过测试（更快）
mvn clean install -DskipTests
```

### Q2.2: 如何在我的项目中使用 EST？

在你的 `pom.xml` 中添加 EST 依赖：

```xml
<dependencies>
    <!-- 核心模块 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-api</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    
    <!-- Web 模块（如果需要） -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-api</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

或者使用脚手架生成项目：

```bash
# 使用 est-scaffold 生成项目
java -jar est-scaffold.jar create --type web --name myapp
```

### Q2.3: 如何配置 EST 应用？

EST 支持多种配置方式：

**方式 1: 编程式配置**
```java
WebApplication app = Web.create("My App", "1.0.0")
    .config("server.port", 8080)
    .config("database.url", "jdbc:postgresql://localhost/mydb");
```

**方式 2: YAML 配置文件**
```yaml
# est-config.yml
server:
  port: 8080
  host: 0.0.0.0

database:
  url: jdbc:postgresql://localhost/mydb
  username: user
  password: pass
```

**方式 3: 环境变量**
```bash
export EST_SERVER_PORT=8080
export EST_DATABASE_URL=jdbc:postgresql://localhost/mydb
```

### Q2.4: 如何修改日志级别？

使用 EST 的日志模块：

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LoggerFactory;
import ltd.idcu.est.features.logging.api.LogLevel;

Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.setLevel(LogLevel.DEBUG);

logger.debug("Debug message");
logger.info("Info message");
logger.warn("Warning message");
logger.error("Error message");
```

或者在配置文件中设置：
```yaml
logging:
  level: DEBUG
  file: /path/to/app.log
```

---

## 3. 容器与依赖注入

### Q3.1: 如何注册 Bean？

EST 提供多种注册 Bean 的方式：

**方式 1: 直接注册**
```java
Container container = new DefaultContainer();
container.register(UserService.class, UserServiceImpl.class);
```

**方式 2: 注册单例**
```java
UserService service = new UserServiceImpl();
container.registerSingleton(UserService.class, service);
```

**方式 3: 使用 Supplier**
```java
container.registerSupplier(UserService.class, () -> {
    UserService service = new UserServiceImpl();
    service.setSomeConfig(config);
    return service;
});
```

**方式 4: 使用注解 + 组件扫描**
```java
@Component
public class UserServiceImpl implements UserService {
    // ...
}

// 扫描并注册
ComponentScanner scanner = new ComponentScanner();
scanner.scan("com.mycompany.services");
```

### Q3.2: 如何注入依赖？

**方式 1: 构造函数注入（推荐）**
```java
@Service
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

**方式 2: 字段注入**
```java
@Service
public class UserService {
    @Inject
    private UserRepository repository;
}
```

**方式 3: 方法注入**
```java
@Service
public class UserService {
    private UserRepository repository;
    
    @Inject
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
}
```

### Q3.3: 如何处理循环依赖？

EST 容器会自动检测循环依赖并抛出异常：

```java
// 这种情况会导致循环依赖异常
container.register(A.class, AImpl.class);  // A 依赖 B
container.register(B.class, BImpl.class);  // B 依赖 A

// 解决方法 1: 重构代码，消除循环依赖
// 解决方法 2: 使用 @Lazy 延迟加载
// 解决方法 3: 使用 Provider 接口
```

### Q3.4: 如何使用不同的作用域？

EST 支持多种作用域：

```java
// 单例（默认）
container.register(UserService.class, UserServiceImpl.class, Scope.SINGLETON);

// 原型（每次获取创建新实例）
container.register(UserService.class, UserServiceImpl.class, Scope.PROTOTYPE);

// 自定义作用域
container.register(UserService.class, UserServiceImpl.class, 
    new CustomScope());
```

### Q3.5: 如何使用 Qualifier 区分多个实现？

```java
// 注册多个实现
container.register(UserRepository.class, JdbcUserRepository.class, "jdbc");
container.register(UserRepository.class, MongoUserRepository.class, "mongo");

// 使用 Qualifier 注入
public class UserService {
    @Inject
    @Qualifier("jdbc")
    private UserRepository repository;
}

// 或者直接获取
UserRepository jdbcRepo = container.get(UserRepository.class, "jdbc");
UserRepository mongoRepo = container.get(UserRepository.class, "mongo");
```

---

## 4. Web 开发

### Q4.1: 如何创建一个简单的 Web 应用？

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class HelloWorld {
    public static void main(String[] args) {
        WebApplication app = Web.create("Hello", "1.0.0");
        
        app.get("/", (req, res) -> res.send("Hello World!"));
        
        app.run(8080);
    }
}
```

### Q4.2: 如何定义路由？

EST 支持多种路由定义方式：

```java
// 基本路由
app.get("/users", handler);
app.post("/users", handler);
app.put("/users/:id", handler);
app.delete("/users/:id", handler);

// 路径参数
app.get("/users/:id", (req, res) -> {
    String id = req.param("id");
    res.send("User: " + id);
});

// 查询参数
app.get("/search", (req, res) -> {
    String query = req.param("q", "default");
    int page = Integer.parseInt(req.param("page", "1"));
    res.send("Query: " + query + ", Page: " + page);
});

// 路由分组
app.group("/api", () -> {
    app.get("/users", userHandler);
    app.get("/products", productHandler);
});

// 路由命名
app.get("/users/:id", (req, res) -> {
    // ...
}).name("user-detail");
```

### Q4.3: 如何处理 JSON 请求和响应？

```java
// 发送 JSON 响应
app.get("/api/users", (req, res) -> {
    List<User> users = userService.findAll();
    res.json(users);
});

// 接收 JSON 请求体
app.post("/api/users", (req, res) -> {
    User user = req.body(User.class);
    User saved = userService.save(user);
    res.status(201).json(saved);
});

// 自定义 JSON 配置
app.get("/api/data", (req, res) -> {
    res.json(data, JsonConfig.builder()
        .prettyPrint(true)
        .includeNulls(false)
        .build());
});
```

### Q4.4: 如何使用中间件？

```java
// 创建中间件
public class LoggingMiddleware implements Middleware {
    @Override
    public boolean before(Request req, Response res) {
        System.out.println("Request: " + req.getMethod() + " " + req.getPath());
        return true;
    }
    
    @Override
    public void after(Request req, Response res) {
        System.out.println("Response: " + res.getStatus());
    }
    
    @Override
    public String getName() { return "logging"; }
    
    @Override
    public int getPriority() { return 50; }
}

// 使用中间件
app.use(new LoggingMiddleware());

// 带优先级
app.use(new SecurityMiddleware(), 10);  // 高优先级
app.use(new LoggingMiddleware(), 50);    // 中优先级

// 条件应用
public class CompressionMiddleware implements Middleware {
    @Override
    public boolean shouldApply(Request req) {
        String path = req.getPath();
        return !path.startsWith("/api/stream");
    }
    // ...
}
```

### Q4.5: 如何提供静态文件？

```java
// 简单的静态文件服务
app.staticFiles("/static", "./public");

// 带配置
app.staticFiles("/static", "./public", StaticFileConfig.builder()
    .cacheMaxAge(86400)              // 缓存 1 天
    .etagEnabled(true)                // 启用 ETag
    .lastModifiedEnabled(true)        // 启用 Last-Modified
    .indexFiles(List.of("index.html")) // 默认索引文件
    .build());
```

### Q4.6: 如何处理文件上传？

```java
app.post("/upload", (req, res) -> {
    MultipartFile file = req.file("avatar");
    if (file == null) {
        res.status(400).send("No file uploaded");
        return;
    }
    
    // 获取文件信息
    String filename = file.getOriginalFilename();
    long size = file.getSize();
    String contentType = file.getContentType();
    
    // 保存文件
    file.transferTo(new File("./uploads/" + filename));
    
    res.json(Map.of(
        "filename", filename,
        "size", size,
        "contentType", contentType
    ));
});

// 多文件上传
app.post("/uploads", (req, res) -> {
    List<MultipartFile> files = req.files("documents");
    for (MultipartFile file : files) {
        // 处理每个文件
    }
    res.send("Uploaded " + files.size() + " files");
});
```

### Q4.7: 如何处理会话？

```java
app.get("/login", (req, res) -> {
    Session session = req.session(true);  // 创建会话（如果不存在）
    session.setAttribute("userId", "user-123");
    session.setAttribute("username", "john");
    res.send("Logged in");
});

app.get("/profile", (req, res) -> {
    Session session = req.session(false);  // 获取会话（不创建）
    if (session == null) {
        res.status(401).send("Not logged in");
        return;
    }
    
    String userId = (String) session.getAttribute("userId");
    res.send("Profile: " + userId);
});

app.post("/logout", (req, res) -> {
    Session session = req.session(false);
    if (session != null) {
        session.invalidate();
    }
    res.send("Logged out");
});
```

### Q4.8: 如何使用模板引擎？

```java
// 渲染模板
app.get("/", (req, res) -> {
    res.render("index", Map.of(
        "title", "My Page",
        "message", "Hello World",
        "items", List.of("Item 1", "Item 2", "Item 3")
    ));
});

// 模板示例 (index.html)
/*
<!DOCTYPE html>
<html>
<head>
    <title>{{title}}</title>
</head>
<body>
    <h1>{{message}}</h1>
    <ul>
        {{#each items}}
            <li>{{this}}</li>
        {{/each}}
    </ul>
</body>
</html>
*/
```

---

## 5. 数据访问

### Q5.1: 如何使用 JDBC 访问数据库？

```java
// 创建数据源
DataSource dataSource = JdbcDataSource.builder()
    .url("jdbc:postgresql://localhost/mydb")
    .username("user")
    .password("pass")
    .maxConnections(20)
    .build();

// 创建 Repository
ProductRepository repository = new JdbcProductRepository(dataSource);

// 使用
Product product = new Product();
product.setName("Laptop");
product.setPrice(999.99);
repository.save(product);

Optional<Product> found = repository.findById(product.getId());
List<Product> all = repository.findAll();
```

### Q5.2: 如何定义实体？

```java
@Entity("products")
public class Product {
    
    @Id
    private Long id;
    
    @Column("name")
    private String name;
    
    @Column("price")
    private Double price;
    
    @Column("stock")
    private Integer stock;
    
    @Column("created_at")
    private Date createdAt;
    
    @Transient  // 不持久化
    private String tempField;
    
    // getters and setters
}
```

### Q5.3: 如何执行自定义查询？

```java
public class JdbcProductRepository extends JdbcRepository<Product, Long> 
    implements ProductRepository {
    
    public JdbcProductRepository(DataSource dataSource) {
        super(dataSource, Product.class);
    }
    
    @Override
    public List<Product> findByPriceGreaterThan(Double price) {
        String sql = "SELECT * FROM products WHERE price > ?";
        return query(sql, Product.class, price);
    }
    
    @Override
    public Optional<Product> findByName(String name) {
        String sql = "SELECT * FROM products WHERE name = ? LIMIT 1";
        List<Product> results = query(sql, Product.class, name);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public int updateStock(Long productId, int quantity) {
        String sql = "UPDATE products SET stock = stock + ? WHERE id = ?";
        return update(sql, quantity, productId);
    }
}
```

### Q5.4: 如何使用事务？

```java
public class OrderService {
    private final DataSource dataSource;
    
    public OrderService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void createOrder(Order order, List<OrderItem> items) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // 保存订单
                saveOrder(conn, order);
                
                // 保存订单项
                for (OrderItem item : items) {
                    saveOrderItem(conn, item);
                    // 更新库存
                    updateStock(conn, item.getProductId(), -item.getQuantity());
                }
                
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
```

### Q5.5: 如何使用 MongoDB？

```java
// 创建 MongoDB 客户端
MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
MongoDatabase database = mongoClient.getDatabase("est_db");

// 创建 Repository
OrderRepository repository = new MongoOrderRepository(database);

// 使用
Order order = new Order();
order.setOrderNumber("ORD-001");
order.setCustomerId("CUST-123");
repository.save(order);

Optional<Order> found = repository.findByOrderNumber("ORD-001");
List<Order> customerOrders = repository.findByCustomerId("CUST-123");
```

---

## 6. 性能问题

### Q6.1: EST 应用启动慢怎么办？

**优化建议**：

1. **减少组件扫描范围**
```java
// 不推荐
scanner.scan("");

// 推荐
scanner.scan("com.mycompany.myapp");
```

2. **使用显式注册替代扫描**
```java
// 推荐：显式注册需要的 Bean
container.register(UserService.class, UserServiceImpl.class);
container.register(ProductService.class, ProductServiceImpl.class);
```

3. **延迟初始化非关键 Bean**
```java
container.registerSupplier(NonCriticalService.class, 
    () -> new NonCriticalServiceImpl(), Scope.SINGLETON);
```

4. **参考性能调优指南**：`docs/PERFORMANCE_TUNING_GUIDE.md`

### Q6.2: 如何提升 Web 应用性能？

**优化建议**：

1. **启用路由缓存**（默认已启用）
2. **使用 Gzip 压缩**
```java
app.use(new CompressionMiddleware());
```

3. **配置静态资源缓存**
```java
app.staticFiles("/static", "./public", StaticFileConfig.builder()
    .cacheMaxAge(86400)
    .etagEnabled(true)
    .build());
```

4. **优化中间件顺序**
```java
app.use(new SecurityMiddleware(), 10);   // 先检查安全
app.use(new LoggingMiddleware(), 50);     // 再记录日志
app.use(new CompressionMiddleware(), 100); // 最后压缩
```

5. **使用虚拟线程处理耗时请求**
```java
app.get("/api/heavy-task", (req, res) -> {
    CompletableFuture.runAsync(() -> {
        heavyComputation();
    }, Thread.ofVirtual().executor());
    res.json(Map.of("status", "processing"));
});
```

### Q6.3: 如何优化数据库查询性能？

**优化建议**：

1. **使用连接池**
```java
DataSource dataSource = JdbcDataSource.builder()
    .maxConnections(20)
    .minIdleConnections(5)
    .build();
```

2. **使用批量操作**
```java
// 不推荐
for (User user : users) {
    repository.save(user);
}

// 推荐
repository.saveAll(users);
```

3. **添加索引**
```java
@Entity
class User {
    @Id
    private Long id;
    
    @Indexed  // 添加索引
    private String email;
}
```

4. **避免 N+1 查询**
```java
// 推荐：批量加载
Set<Long> userIds = orders.stream()
    .map(Order::getUserId)
    .collect(Collectors.toSet());
List<User> users = userRepository.findByIdIn(userIds);
```

5. **使用分页**
```java
Page<User> page = repository.findUsers(1, 20);
```

### Q6.4: 如何优化缓存性能？

**优化建议**：

1. **使用多层缓存**
```java
Cache<String, Product> localCache = MemoryCache.builder()
    .maxSize(1000)
    .expireAfterWrite(300)
    .build();

Cache<String, Product> remoteCache = RedisCache.builder()
    .host("localhost")
    .port(6379)
    .expireAfterWrite(3600)
    .build();
```

2. **实现缓存预热**
```java
public class CacheWarmer {
    public void warmUp() {
        List<Product> hotProducts = repository.findHotProducts();
        for (Product product : hotProducts) {
            cache.put(product.getId(), product, 7200);
        }
    }
}
```

3. **防护缓存穿透**
```java
// 使用布隆过滤器或空值缓存
if (product == null) {
    cache.put(productId, NULL_PRODUCT);
}
```

4. **随机化 TTL 防止缓存雪崩**
```java
int randomTtl = 300 + (int) (Math.random() * 100);
cache.put(key, value, randomTtl);
```

---

## 7. 调试与问题排查

### Q7.1: 如何启用调试日志？

```java
// 设置日志级别
Logger logger = LoggerFactory.getLogger("ltd.idcu.est");
logger.setLevel(LogLevel.DEBUG);

// 或者在配置中
logging:
  level: DEBUG
  loggers:
    "ltd.idcu.est": DEBUG
    "ltd.idcu.est.web": TRACE
```

### Q7.2: 如何排查依赖注入问题？

**常见问题及解决方案**：

1. **"No registration found for type"**
```java
// 原因：没有注册该类型
// 解决：确保已经注册
container.register(MyService.class, MyServiceImpl.class);
```

2. **循环依赖异常**
```java
// 原因：A 依赖 B，B 又依赖 A
// 解决：重构代码，使用 Provider 或 @Lazy
```

3. **注入的 Bean 为 null**
```java
// 原因：字段注入时对象不是由容器创建的
// 解决：使用构造函数注入，或确保对象由容器管理
```

### Q7.3: 如何排查 Web 请求问题？

**调试步骤**：

1. **添加日志中间件**
```java
app.use(new LoggingMiddleware());
```

2. **检查路由是否匹配**
```java
// 在处理器中添加日志
app.get("/api/users", (req, res) -> {
    System.out.println("Request received: " + req.getPath());
    System.out.println("Headers: " + req.getHeaders());
    // ...
});
```

3. **检查中间件是否阻止了请求**
```java
// 检查中间件的 before 方法返回值
public boolean before(Request req, Response res) {
    // 如果返回 false，请求会被阻止
    return true;
}
```

### Q7.4: 如何排查性能问题？

**排查步骤**：

1. **运行基准测试**
```bash
cd est-foundation/est-test/est-test-benchmark
mvn clean package
java -jar target/est-benchmarks.jar
```

2. **启用 JVM 监控**
```bash
java -Dcom.sun.management.jmxremote \
     -Xlog:gc*:file=gc.log \
     -jar myapp.jar
```

3. **添加性能监控中间件**
```java
app.use(new PerformanceMonitorMiddleware());
```

4. **使用 Profiler**
- VisualVM
- JProfiler
- YourKit

### Q7.5: 遇到问题在哪里寻求帮助？

1. **查看文档**：`docs/` 目录
2. **查看示例**：`est-examples/` 目录
3. **GitHub Issues**：https://github.com/idcu/est/issues
4. **常见问题**：本文档（FAQ.md）

---

## 8. 与其他框架对比

### Q8.1: EST vs Solon - 如何选择？

| 维度 | EST | Solon |
|------|-----|-------|
| 零依赖 | ✅ 完全零依赖 | ❌ 少量依赖 |
| Java 版本 | Java 21+ | Java 8+ |
| 学习曲线 | 极平缓 | 平缓 |
| 生态成熟度 | 新兴 | 活跃 |
| 微服务支持 | 基础 | 完善 |

**选择建议**：
- 选择 EST：零依赖需求、学习框架、AI 代码生成
- 选择 Solon：企业级应用、微服务、Java 8 兼容

### Q8.2: EST vs Spring Boot - 如何选择？

| 维度 | EST | Spring Boot |
|------|-----|-------------|
| 依赖 | 零依赖 | 重度依赖 |
| 启动速度 | ~0.5s | ~3-5s |
| 内存占用 | ~30MB | ~200-500MB |
| 生态 | 新兴 | 超大规模 |
| 学习曲线 | 极平缓 | 陡峭 |

**选择建议**：
- 选择 EST：学习框架原理、快速原型、资源受限环境
- 选择 Spring Boot：企业级应用、成熟生态、团队已有经验

### Q8.3: 可以从 Spring Boot 迁移到 EST 吗？

可以，但需要考虑：

1. **依赖管理**：EST 是零依赖，需要替换 Spring 的依赖
2. **注解差异**：EST 的注解与 Spring 类似但不完全相同
3. **生态差异**：EST 的生态不如 Spring 完善
4. **迁移策略**：建议先迁移小型模块，逐步推进

---

## 9. 贡献与社区

### Q9.1: 如何为 EST 贡献代码？

请参考 `docs/CONTRIBUTING.md`，基本步骤：

1. Fork 仓库
2. 创建分支
3. 编写代码
4. 运行测试
5. 提交 Pull Request

### Q9.2: 可以报告 Bug 或提出功能建议吗？

非常欢迎！请在 GitHub Issues 中报告：

- **Bug 报告**：请包含复现步骤、期望行为、实际行为
- **功能建议**：请说明功能用途、使用场景、设计思路

### Q9.3: 如何编写 EST 插件？

参考 `est-extensions/est-plugin/` 模块：

```java
public class MyPlugin implements Plugin {
    @Override
    public String getName() { return "my-plugin"; }
    
    @Override
    public String getVersion() { return "1.0.0"; }
    
    @Override
    public void install(Container container) {
        container.register(MyService.class, MyServiceImpl.class);
    }
}
```

详细文档请参考 `docs/CONTRIBUTING.md`。

---

## 10. 其他问题

### Q10.1: EST 可以用于生产环境吗？

EST 目前处于 **1.3.0-SNAPSHOT** 版本，正在进行稳定化优化。

**建议**：
- ✅ 可以用于学习、原型开发、内部工具
- ⚠️ 生产环境使用请谨慎评估
- 👀 关注项目的稳定化进展

### Q10.2: EST 的未来发展计划是什么？

短期计划（3-6个月）：
- 完善测试覆盖率到 80%+
- 持续性能优化
- 完善文档
- 代码质量提升

中期计划（6-12个月）：
- GraalVM 原生镜像支持
- 微服务组件完善
- 更多数据库支持
- 社区建设

长期计划（1-2年）：
- 强化 AI 友好设计
- 建立教育品牌
- 积累生产案例

### Q10.3: 如何获取最新版本？

1. **GitHub Releases**：https://github.com/idcu/est/releases
2. **Maven 仓库**：配置 Maven 依赖使用最新版本
3. **Git 主分支**：`git clone https://github.com/idcu/est.git`

### Q10.4: 有商业支持吗？

目前 EST 是开源项目，没有官方商业支持。

如果你需要：
- 定制开发
- 技术咨询
- 培训服务

可以通过 GitHub Issues 或邮件联系项目维护者。

---

## 附录

### 更多资源

- **EST GitHub**: https://github.com/idcu/est
- **文档中心**: `docs/README.md`
- **贡献指南**: `docs/CONTRIBUTING.md`
- **性能调优指南**: `docs/PERFORMANCE_TUNING_GUIDE.md`
- **实际使用示例**: `docs/PRACTICAL_EXAMPLES.md`
- **框架对比**: `docs/FRAMEWORK_COMPARISON.md`

---

*本文档最后更新：2026-03-06*
*如有问题或建议，欢迎在 GitHub Issues 中反馈！*
