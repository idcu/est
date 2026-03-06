# EST 框架性能调优指南

本文档提供 EST 框架应用的性能调优最佳实践，帮助你构建高性能的 EST 应用。

---

## 目录

1. [性能基准测试](#1-性能基准测试)
2. [容器优化](#2-容器优化)
3. [Web 性能优化](#3-web-性能优化)
4. [数据访问优化](#4-数据访问优化)
5. [缓存优化](#5-缓存优化)
6. [内存优化](#6-内存优化)
7. [并发优化](#7-并发优化)
8. [JVM 调优](#8-jvm-调优)
9. [生产环境配置](#9-生产环境配置)

---

## 1. 性能基准测试

### 1.1 运行基准测试

EST 框架内置了 JMH 基准测试套件：

```bash
cd est-foundation/est-test/est-test-benchmark
mvn clean package
java -jar target/est-benchmarks.jar
```

### 1.2 基准测试结果解读

当前 EST 1.3.0 的性能基准：

| Benchmark | Score | Unit | 说明 |
|-----------|-------|------|------|
| ContainerBenchmark.container_get | 0.0078 | μs/op | 容器获取极快 |
| ContainerBenchmark.container_contains | 0.0019 | μs/op | 容器包含检查 |
| CacheBenchmark.cache_get | 55,059 | ops/ms | 缓存读取性能 |
| CacheBenchmark.cache_put | 17,294 | ops/ms | 缓存写入性能 |
| CollectionBenchmark.estCollection_size | 1,506,554 | ops/ms | 集合大小查询 |

### 1.3 自定义基准测试

创建你的基准测试：

```java
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class MyBenchmark {
    
    private Container container;
    
    @Setup
    public void setup() {
        container = new DefaultContainer();
        container.register(MyService.class, MyServiceImpl.class);
    }
    
    @Benchmark
    public void containerGet() {
        container.get(MyService.class);
    }
}
```

---

## 2. 容器优化

### 2.1 DefaultContainer 性能优化

#### 2.1.1 减少反射调用

```java
// 优化前 - 每次都反射创建
public <T> T get(Class<T> type) {
    return createInstance(type);
}

// 优化后 - 缓存构造函数
public class DefaultContainer implements Container {
    private final Map<Class<?>, Constructor<?>> constructorCache = new ConcurrentHashMap<>();
    
    private <T> Constructor<T> getConstructor(Class<T> type) {
        return (Constructor<T>) constructorCache.computeIfAbsent(type, 
            t -> {
                try {
                    return t.getDeclaredConstructor();
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
```

#### 2.1.2 Bean 实例缓存

```java
// 使用 ConcurrentHashMap 确保线程安全
private final Map<String, Object> instances = new ConcurrentHashMap<>();

// 单例模式优化
public <T> T get(Class<T> type) {
    String key = type.getName();
    T instance = (T) instances.get(key);
    if (instance != null) {
        return instance;
    }
    return (T) instances.computeIfAbsent(key, k -> createInstance(type));
}
```

#### 2.1.3 延迟初始化

```java
// 对于不常用的 Bean，使用 Supplier 延迟创建
public <T> void registerSupplier(Class<T> type, Supplier<T> supplier) {
    registrations.put(type.getName(), 
        new Registration(supplier, Scope.SINGLETON));
}

// 只在第一次获取时创建
public <T> T get(Class<T> type) {
    String key = type.getName();
    T instance = (T) instances.get(key);
    if (instance == null) {
        Registration reg = registrations.get(key);
        if (reg != null) {
            instance = (T) reg.supplier.get();
            instances.put(key, instance);
        }
    }
    return instance;
}
```

### 2.2 依赖注入优化

#### 2.2.1 优先使用构造函数注入

```java
// 推荐 - 构造函数注入
@Service
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

// 避免 - 字段注入（反射性能较差）
@Service
public class UserService {
    @Inject
    private UserRepository repository;
}
```

#### 2.2.2 减少循环依赖检查

```java
// 在生产环境可以禁用循环依赖检查（如果确定没有循环依赖）
public class DefaultContainer implements Container {
    private final boolean checkCircularDependencies = false;
    
    private <T> T createInstance(Class<T> type) {
        if (checkCircularDependencies) {
            checkForCircularDependency(type);
        }
        return doCreateInstance(type);
    }
}
```

### 2.3 组件扫描优化

#### 2.3.1 限制扫描范围

```java
// 优化前 - 扫描整个 classpath
ComponentScanner scanner = new ComponentScanner();
scanner.scan("");

// 优化后 - 只扫描需要的包
ComponentScanner scanner = new ComponentScanner();
scanner.scan("com.mycompany.myapp");
```

#### 2.3.2 显式注册替代扫描

```java
// 优化前 - 组件扫描
@ComponentScan("com.mycompany")
public class AppConfig {}

// 优化后 - 显式注册
Container container = new DefaultContainer();
container.register(UserService.class, UserServiceImpl.class);
container.register(ProductService.class, ProductServiceImpl.class);
```

---

## 3. Web 性能优化

### 3.1 路由优化

#### 3.1.1 路由缓存

```java
// DefaultRouter 已经内置了路由缓存
public class DefaultRouter implements Router {
    private final Map<HttpMethod, Map<String, Route>> routeCache;
    
    public Route match(String path, HttpMethod method) {
        Map<String, Route> methodCache = routeCache.get(method);
        if (methodCache != null) {
            Route cached = methodCache.get(path);
            if (cached != null) {
                return cached;
            }
        }
        return doMatch(path, method);
    }
}
```

#### 3.1.2 路由顺序优化

```java
// 将高频路由放在前面
app.get("/api/users", userHandler);
app.get("/api/products", productHandler);
app.get("/api/:param", genericHandler);

// 避免过深的路径嵌套
// 不推荐
app.get("/api/v1/users/:userId/orders/:orderId/items/:itemId", handler);

// 推荐
app.get("/api/v1/orders/:orderId/items/:itemId", handler);
```

#### 3.1.3 使用路由分组

```java
// 路由分组可以优化匹配
app.group("/api", () -> {
    app.get("/users", userHandler);
    app.get("/products", productHandler);
});
```

### 3.2 中间件优化

#### 3.2.1 中间件优先级

```java
// 将高频检查的中间件放在前面
app.use(new SecurityMiddleware(), 10);  // 高优先级
app.use(new LoggingMiddleware(), 50);    // 中优先级
app.use(new CompressionMiddleware(), 100); // 低优先级
```

#### 3.2.2 条件应用中间件

```java
// 只对需要的路径应用中间件
public class CompressionMiddleware implements Middleware {
    @Override
    public boolean shouldApply(Request request) {
        String path = request.getPath();
        return !path.startsWith("/api/stream") && 
               !path.startsWith("/static/large");
    }
}
```

### 3.3 响应优化

#### 3.3.1 启用 Gzip 压缩

```java
app.use(new CompressionMiddleware(CompressionConfig.builder()
    .minSize(1024)           // 只压缩大于 1KB 的响应
    .mimeTypes(List.of(
        "text/html",
        "text/css",
        "application/javascript",
        "application/json"
    ))
    .build()));
```

#### 3.3.2 静态资源缓存

```java
app.staticFiles("/static", "./public", StaticFileConfig.builder()
    .cacheMaxAge(86400)              // 缓存 1 天
    .etagEnabled(true)                // 启用 ETag
    .lastModifiedEnabled(true)        // 启用 Last-Modified
    .build());
```

#### 3.3.3 响应头优化

```java
app.get("/api/data", (req, res) -> {
    res.header("Cache-Control", "public, max-age=3600");
    res.header("Vary", "Accept-Encoding");
    res.json(data);
});
```

### 3.4 会话优化

#### 3.4.1 会话存储优化

```java
// 使用内存缓存 + Redis 持久化
SessionManager sessionManager = DefaultSessionManager.builder()
    .memoryCacheSize(10000)          // 内存缓存 10000 个会话
    .sessionStore(redisSessionStore)  // Redis 持久化
    .sessionTimeout(1800)             // 30 分钟超时
    .build();
```

#### 3.4.2 减少会话数据

```java
// 避免在会话中存储大量数据
// 不推荐
session.setAttribute("user", userObject);
session.setAttribute("cart", cartObject);
session.setAttribute("history", historyList);

// 推荐
session.setAttribute("userId", userId);
// 其他数据从数据库或缓存获取
```

---

## 4. 数据访问优化

### 4.1 连接池配置

```java
DataSource dataSource = JdbcDataSource.builder()
    .url("jdbc:postgresql://localhost/mydb")
    .username("user")
    .password("pass")
    .maxConnections(20)           // 最大连接数
    .minIdleConnections(5)         // 最小空闲连接
    .connectionTimeout(30000)      // 连接超时 30 秒
    .idleTimeout(600000)          // 空闲超时 10 分钟
    .maxLifetime(1800000)         // 连接最大生命周期 30 分钟
    .build();
```

### 4.2 批量操作

```java
// 优化前 - 逐条操作
for (User user : users) {
    repository.save(user);
}

// 优化后 - 批量操作
repository.saveAll(users);

// 自定义批量操作
public void batchInsert(List<User> users) {
    String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        for (User user : users) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.addBatch();
        }
        stmt.executeBatch();
    }
}
```

### 4.3 查询优化

#### 4.3.1 使用索引

```java
// 确保查询字段有索引
@Entity
class User {
    @Id
    private Long id;
    
    @Indexed  // 添加索引
    private String email;
    
    @Indexed
    private String status;
}
```

#### 4.3.2 避免 N+1 查询

```java
// 优化前 - N+1 查询
List<Order> orders = orderRepository.findAll();
for (Order order : orders) {
    User user = userRepository.findById(order.getUserId());
    order.setUser(user);
}

// 优化后 - 批量加载
List<Order> orders = orderRepository.findAll();
Set<Long> userIds = orders.stream()
    .map(Order::getUserId)
    .collect(Collectors.toSet());
List<User> users = userRepository.findByIdIn(userIds);
Map<Long, User> userMap = users.stream()
    .collect(Collectors.toMap(User::getId, u -> u));
orders.forEach(order -> order.setUser(userMap.get(order.getUserId())));
```

#### 4.3.3 分页查询

```java
// 使用分页避免加载大量数据
public Page<User> findUsers(int page, int size) {
    String sql = "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?";
    List<User> users = query(sql, User.class, size, (page - 1) * size);
    
    String countSql = "SELECT COUNT(*) FROM users";
    long total = queryForObject(countSql, Long.class);
    
    return new Page<>(users, page, size, total);
}
```

### 4.4 NoSQL 优化

#### 4.4.1 MongoDB 查询优化

```java
// 使用投影只查询需要的字段
List<Document> users = collection.find(Filters.eq("status", "active"))
    .projection(Projections.include("name", "email"))
    .into(new ArrayList<>());

// 使用索引
collection.createIndex(Indexes.ascending("email"));
collection.createIndex(Indexes.compoundIndex(
    Indexes.ascending("status"),
    Indexes.descending("createdAt")
));
```

---

## 5. 缓存优化

### 5.1 缓存策略选择

| 场景 | 推荐实现 | 配置建议 |
|------|----------|----------|
| 小数据量，高频访问 | MemoryCache | maxSize=1000-10000 |
| 需要持久化 | FileCache | expireAfterWrite=3600 |
| 分布式环境 | RedisCache | ttl=3600-7200 |
| 热点数据 | LRU MemoryCache | maxSize=热点数据量 |

### 5.2 多层缓存

```java
public class MultiLevelCache<K, V> {
    private final Cache<K, V> localCache;
    private final Cache<K, V> remoteCache;
    
    public MultiLevelCache(Cache<K, V> localCache, Cache<K, V> remoteCache) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
    }
    
    public V get(K key) {
        V value = localCache.get(key);
        if (value != null) {
            return value;
        }
        
        value = remoteCache.get(key);
        if (value != null) {
            localCache.put(key, value);
            return value;
        }
        
        return null;
    }
    
    public void put(K key, V value) {
        remoteCache.put(key, value);
        localCache.put(key, value);
    }
    
    public void remove(K key) {
        localCache.remove(key);
        remoteCache.remove(key);
    }
}
```

### 5.3 缓存穿透防护

```java
public class ProtectedCache<K, V> {
    private final Cache<K, V> cache;
    private final V nullValue;
    private final ConcurrentHashMap<K, Lock> locks = new ConcurrentHashMap<>();
    
    public ProtectedCache(Cache<K, V> cache, V nullValue) {
        this.cache = cache;
        this.nullValue = nullValue;
    }
    
    public V get(K key, Supplier<V> loader) {
        V value = cache.get(key);
        
        if (value == nullValue) {
            return null;
        }
        
        if (value != null) {
            return value;
        }
        
        Lock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
            value = cache.get(key);
            if (value != null) {
                return value;
            }
            
            value = loader.get();
            
            if (value == null) {
                cache.put(key, nullValue);
            } else {
                cache.put(key, value);
            }
            
            return value;
        } finally {
            lock.unlock();
            locks.remove(key);
        }
    }
}
```

### 5.4 缓存预热

```java
public class CacheWarmer implements Module {
    private final Cache<String, Product> productCache;
    private final ProductRepository productRepository;
    
    @Override
    public void initialize(EstApplication app) {
        warmUpHotProducts();
        warmUpCategories();
    }
    
    private void warmUpHotProducts() {
        List<Product> hotProducts = productRepository.findHotProducts();
        for (Product product : hotProducts) {
            productCache.put(product.getId(), product, 7200);
        }
        System.out.println("Warmed up " + hotProducts.size() + " hot products");
    }
    
    private void warmUpCategories() {
        List<Category> categories = productRepository.findAllCategories();
        for (Category category : categories) {
            productCache.put("category:" + category.getId(), category, 86400);
        }
    }
}
```

---

## 6. 内存优化

### 6.1 对象池化

```java
public class ByteArrayPool {
    private final Queue<byte[]> pool;
    private final int bufferSize;
    private final int maxSize;
    
    public ByteArrayPool(int bufferSize, int maxSize) {
        this.bufferSize = bufferSize;
        this.maxSize = maxSize;
        this.pool = new ConcurrentLinkedQueue<>();
    }
    
    public byte[] acquire() {
        byte[] buffer = pool.poll();
        return buffer != null ? buffer : new byte[bufferSize];
    }
    
    public void release(byte[] buffer) {
        if (buffer != null && buffer.length == bufferSize && pool.size() < maxSize) {
            Arrays.fill(buffer, (byte) 0);
            pool.offer(buffer);
        }
    }
}
```

### 6.2 及时释放资源

```java
// 使用 try-with-resources
try (InputStream in = new FileInputStream("file.txt")) {
    byte[] data = in.readAllBytes();
} catch (IOException e) {
    // 处理异常
}

// 数据库连接
try (Connection conn = dataSource.getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
    while (rs.next()) {
        // 处理结果
    }
}
```

### 6.3 避免内存泄漏

```java
// 不好的做法 - 静态集合持有引用
public class CacheManager {
    private static final Map<String, Object> CACHE = new HashMap<>();
    
    public static void put(String key, Object value) {
        CACHE.put(key, value);  // 永不会被回收
    }
}

// 好的做法 - 使用弱引用
public class CacheManager {
    private final Map<String, WeakReference<Object>> cache = new HashMap<>();
    
    public void put(String key, Object value) {
        cache.put(key, new WeakReference<>(value));
    }
    
    public Object get(String key) {
        WeakReference<Object> ref = cache.get(key);
        return ref != null ? ref.get() : null;
    }
}

// 更好的做法 - 使用 EST 的 Cache
Cache<String, Object> cache = MemoryCache.<String, Object>builder()
    .maxSize(1000)
    .expireAfterWrite(3600)
    .build();
```

### 6.4 字符串优化

```java
// 避免字符串拼接
// 不推荐
String result = "";
for (String s : list) {
    result += s;
}

// 推荐
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();

// 使用 intern() 减少重复字符串
String uniqueString = string.intern();
```

---

## 7. 并发优化

### 7.1 虚拟线程

```java
// 使用虚拟线程处理并发请求
app.get("/api/heavy-task", (req, res) -> {
    CompletableFuture.runAsync(() -> {
        // 耗时操作
        heavyComputation();
    }, Thread.ofVirtual().executor());
    
    res.json(Map.of("status", "processing"));
});

// 虚拟线程池
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
executor.submit(() -> {
    // 任务
});
```

### 7.2 并发集合

```java
// 使用 ConcurrentHashMap 替代 HashMap
Map<String, User> users = new ConcurrentHashMap<>();

// 使用 CopyOnWriteArrayList 替代 ArrayList（读多写少场景）
List<User> users = new CopyOnWriteArrayList<>();

// 使用 ConcurrentLinkedQueue
Queue<Task> tasks = new ConcurrentLinkedQueue<>();
```

### 7.3 原子操作

```java
// 使用 AtomicInteger
private final AtomicInteger counter = new AtomicInteger(0);

public int increment() {
    return counter.incrementAndGet();
}

// 使用 AtomicReference
private final AtomicReference<User> currentUser = new AtomicReference<>();

public boolean updateUser(User newUser) {
    return currentUser.compareAndSet(currentUser.get(), newUser);
}
```

### 7.4 锁优化

```java
// 使用 ReentrantLock
private final ReentrantLock lock = new ReentrantLock();

public void criticalSection() {
    lock.lock();
    try {
        // 临界区
    } finally {
        lock.unlock();
    }
}

// 使用读写锁（读多写少）
private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

public void read() {
    rwLock.readLock().lock();
    try {
        // 读操作
    } finally {
        rwLock.readLock().unlock();
    }
}

public void write() {
    rwLock.writeLock().lock();
    try {
        // 写操作
    } finally {
        rwLock.writeLock().unlock();
    }
}
```

---

## 8. JVM 调优

### 8.1 堆内存配置

```bash
# 生产环境推荐配置
java -Xms4g -Xmx4g \
     -XX:MaxMetaspaceSize=512m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar myapp.jar
```

### 8.2 GC 优化

| JVM 参数 | 说明 | 推荐值 |
|----------|------|---------|
| -Xms | 初始堆大小 | 与 -Xmx 相同 |
| -Xmx | 最大堆大小 | 根据应用调整 |
| -XX:MaxMetaspaceSize | 元空间大小 | 256m-512m |
| -XX:+UseG1GC | G1 垃圾收集器 | 推荐 |
| -XX:MaxGCPauseMillis | 最大 GC 暂停时间 | 200 |
| -XX:+UseStringDeduplication | 字符串去重 | 推荐 |

### 8.3 JIT 编译优化

```bash
# 启用分层编译
-XX:+TieredCompilation

# 增加编译阈值
-XX:CompileThreshold=10000

# 启用 OSR（栈上替换）
-XX:+UseOnStackReplacement
```

### 8.4 性能监控

```bash
# 启用 JMX 监控
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9010
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false

# 启用 GC 日志
-Xlog:gc*:file=gc.log:time,uptime:filecount=10,filesize=100m

# 启用 Heap Dump（OOM 时）
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/path/to/dumps
```

---

## 9. 生产环境配置

### 9.1 应用配置

```yaml
# est-config.yml
server:
  port: 8080
  host: 0.0.0.0
  backlog: 1024
  maxThreads: 200
  minThreads: 10

database:
  maxConnections: 20
  minIdleConnections: 5
  connectionTimeout: 30000

cache:
  maxSize: 10000
  expireAfterWrite: 3600

logging:
  level: INFO
  file: /var/log/est/app.log
  maxSize: 100MB
  maxFiles: 10
```

### 9.2 系统配置

```bash
# Linux 系统优化
# /etc/sysctl.conf
net.core.somaxconn = 1024
net.core.netdev_max_backlog = 5000
net.ipv4.tcp_max_syn_backlog = 2048
net.ipv4.tcp_tw_reuse = 1
net.ipv4.ip_local_port_range = 1024 65535

# 文件描述符限制
# /etc/security/limits.conf
est soft nofile 65536
est hard nofile 65536
est soft nproc 4096
est hard nproc 4096
```

### 9.3 健康检查

```java
app.get("/health", (req, res) -> {
    HealthCheckResult dbCheck = checkDatabase();
    HealthCheckResult cacheCheck = checkCache();
    
    boolean healthy = dbCheck.isHealthy() && cacheCheck.isHealthy();
    
    res.status(healthy ? 200 : 503)
       .json(Map.of(
           "status", healthy ? "UP" : "DOWN",
           "database", dbCheck,
           "cache", cacheCheck,
           "timestamp", System.currentTimeMillis()
       ));
});
```

---

## 10. 性能调优检查清单

- [ ] 运行基准测试建立性能基线
- [ ] 优化容器配置，减少反射调用
- [ ] 启用路由缓存
- [ ] 配置合适的中间件优先级
- [ ] 启用 Gzip 压缩
- [ ] 配置静态资源缓存
- [ ] 使用数据库连接池
- [ ] 实现批量操作
- [ ] 添加数据库索引
- [ ] 配置多层缓存
- [ ] 实现缓存预热
- [ ] 防护缓存穿透/击穿
- [ ] 使用虚拟线程
- [ ] 使用并发集合
- [ ] 配置 JVM 参数
- [ ] 启用 GC 日志
- [ ] 配置系统文件描述符
- [ ] 添加健康检查端点
- [ ] 设置监控告警

---

## 总结

性能调优是一个持续的过程，遵循以下原则：

1. **先测量，再优化** - 使用基准测试和监控数据指导优化
2. **优先优化热点路径** - 关注性能瓶颈所在
3. **使用缓存** - 减少重复计算和 I/O
4. **批量操作** - 减少网络往返和 I/O 次数
5. **并发优化** - 充分利用多核和虚拟线程
6. **持续监控** - 建立性能基线，及时发现问题

记住：过早优化是万恶之源。先让功能正常工作，再进行优化！
