# EST 框架性能调优指南

本文档提供 EST 框架应用的性能调优最佳实践，帮助你构建高性能的 EST 应用。

---

## 目录

1. [性能基准测试](#性能基准测试)
2. [容器优化](#容器优化)
3. [Web 性能优化](#web-性能优化)
4. [数据访问优化](#数据访问优化)
5. [缓存优化](#缓存优化)
6. [内存优化](#内存优化)
7. [并发优化](#并发优化)
8. [JVM 调优](#jvm-调优)
9. [生产环境配置](#生产环境配置)
10. [性能监控](#性能监控)
11. [性能调优检查清单](#性能调优检查清单)

---

## 性能基准测试

### 运行基准测试

EST 框架内置了 JMH 基准测试套件：

```bash
cd est-foundation/est-test/est-test-benchmark
mvn clean package
java -jar target/est-benchmarks.jar
```

### 基准测试结果解读

当前 EST 1.3.0 的性能基准：

| Benchmark | Score | Unit | 说明 |
|-----------|-------|------|------|
| ContainerBenchmark.container_get | 0.0078 | μs/op | 容器获取极快 |
| ContainerBenchmark.container_contains | 0.0019 | μs/op | 容器包含检查 |
| CacheBenchmark.cache_get | 55,059 | ops/ms | 缓存读取性能 |
| CacheBenchmark.cache_put | 17,294 | ops/ms | 缓存写入性能 |
| CollectionBenchmark.estCollection_size | 1,506,554 | ops/ms | 集合大小查询 |

### 自定义基准测试

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

## 容器优化

### DefaultContainer 性能优化

#### 减少反射调用

```java
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

#### Bean 实例缓存

```java
private final Map<String, Object> instances = new ConcurrentHashMap<>();

public <T> T get(Class<T> type) {
    String key = type.getName();
    T instance = (T) instances.get(key);
    if (instance != null) {
        return instance;
    }
    return (T) instances.computeIfAbsent(key, k -> createInstance(type));
}
```

#### 延迟初始化

```java
public <T> void registerSupplier(Class<T> type, Supplier<T> supplier) {
    registrations.put(type.getName(), 
        new Registration(supplier, Scope.SINGLETON));
}

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

### 依赖注入优化

#### 优先使用构造函数注入

```java
@Service
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

#### 减少循环依赖检查

```java
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

### 组件扫描优化

#### 限制扫描范围

```java
ComponentScanner scanner = new ComponentScanner();
scanner.scan("com.mycompany.myapp");
```

#### 显式注册替代扫描

```java
Container container = new DefaultContainer();
container.register(UserService.class, UserServiceImpl.class);
container.register(ProductService.class, ProductServiceImpl.class);
```

---

## Web 性能优化

### 路由优化

#### 路由缓存

```java
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

#### 路由顺序优化

```java
app.get("/api/users", userHandler);
app.get("/api/products", productHandler);
app.get("/api/:param", genericHandler);
```

#### 使用路由分组

```java
app.group("/api", () -> {
    app.get("/users", userHandler);
    app.get("/products", productHandler);
});
```

### 中间件优化

#### 中间件优先级

```java
app.use(new SecurityMiddleware(), 10);
app.use(new LoggingMiddleware(), 50);
app.use(new CompressionMiddleware(), 100);
```

#### 条件应用中间件

```java
public class CompressionMiddleware implements Middleware {
    @Override
    public boolean shouldApply(Request request) {
        String path = request.getPath();
        return !path.startsWith("/api/stream") && 
               !path.startsWith("/static/large");
    }
}
```

### 响应优化

#### 启用 Gzip 压缩

```java
app.use(new CompressionMiddleware(CompressionConfig.builder()
    .minSize(1024)
    .mimeTypes(List.of(
        "text/html",
        "text/css",
        "application/javascript",
        "application/json"
    ))
    .build()));
```

#### 静态资源缓存

```java
app.staticFiles("/static", "./public", StaticFileConfig.builder()
    .cacheMaxAge(86400)
    .etagEnabled(true)
    .lastModifiedEnabled(true)
    .build());
```

#### 响应头优化

```java
app.get("/api/data", (req, res) -> {
    res.header("Cache-Control", "public, max-age=3600");
    res.header("Vary", "Accept-Encoding");
    res.json(data);
});
```

### 会话优化

#### 会话存储优化

```java
SessionManager sessionManager = DefaultSessionManager.builder()
    .memoryCacheSize(10000)
    .sessionStore(redisSessionStore)
    .sessionTimeout(1800)
    .build();
```

#### 减少会话数据

```java
session.setAttribute("userId", userId);
```

---

## 数据访问优化

### 连接池配置

```java
DataSource dataSource = JdbcDataSource.builder()
    .url("jdbc:postgresql://localhost/mydb")
    .username("user")
    .password("pass")
    .maxConnections(20)
    .minIdleConnections(5)
    .connectionTimeout(30000)
    .idleTimeout(600000)
    .maxLifetime(1800000)
    .build();
```

### 批量操作

```java
repository.saveAll(users);

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

### 查询优化

#### 使用索引

```java
@Entity
class User {
    @Id
    private Long id;
    
    @Indexed
    private String email;
    
    @Indexed
    private String status;
}
```

#### 避免 N+1 查询

```java
List<Order> orders = orderRepository.findAll();
Set<Long> userIds = orders.stream()
    .map(Order::getUserId)
    .collect(Collectors.toSet());
List<User> users = userRepository.findByIdIn(userIds);
Map<Long, User> userMap = users.stream()
    .collect(Collectors.toMap(User::getId, u -> u));
orders.forEach(order -> order.setUser(userMap.get(order.getUserId())));
```

#### 分页查询

```java
public Page<User> findUsers(int page, int size) {
    String sql = "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?";
    List<User> users = query(sql, User.class, size, (page - 1) * size);
    
    String countSql = "SELECT COUNT(*) FROM users";
    long total = queryForObject(countSql, Long.class);
    
    return new Page<>(users, page, size, total);
}
```

### NoSQL 优化

#### MongoDB 查询优化

```java
List<Document> users = collection.find(Filters.eq("status", "active"))
    .projection(Projections.include("name", "email"))
    .into(new ArrayList<>());

collection.createIndex(Indexes.ascending("email"));
collection.createIndex(Indexes.compoundIndex(
    Indexes.ascending("status"),
    Indexes.descending("createdAt")
));
```

---

## 缓存优化

### 缓存策略选择

| 场景 | 推荐实现 | 配置建议 |
|------|----------|----------|
| 小数据量，高频访问 | MemoryCache | maxSize=1000-10000 |
| 需要持久化 | FileCache | expireAfterWrite=3600 |
| 分布式环境 | RedisCache | ttl=3600-7200 |
| 热点数据 | LRU MemoryCache | maxSize=热点数据量 |

### 多层缓存

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

### 缓存穿透防护

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

### 缓存预热

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

## 内存优化

### 对象池化

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

### 及时释放资源

```java
try (InputStream in = new FileInputStream("file.txt")) {
    byte[] data = in.readAllBytes();
} catch (IOException e) {
}

try (Connection conn = dataSource.getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
    while (rs.next()) {
    }
}
```

### 避免内存泄漏

```java
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

Cache<String, Object> cache = MemoryCache.<String, Object>builder()
    .maxSize(1000)
    .expireAfterWrite(3600)
    .build();
```

### 字符串优化

```java
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();

String uniqueString = string.intern();
```

---

## 并发优化

### 虚拟线程

```java
app.get("/api/heavy-task", (req, res) -> {
    CompletableFuture.runAsync(() -> {
        heavyComputation();
    }, Thread.ofVirtual().executor());
    
    res.json(Map.of("status", "processing"));
});

ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
executor.submit(() -> {
});
```

### 并发集合

```java
Map<String, User> users = new ConcurrentHashMap<>();
List<User> users = new CopyOnWriteArrayList<>();
Queue<Task> tasks = new ConcurrentLinkedQueue<>();
```

### 原子操作

```java
private final AtomicInteger counter = new AtomicInteger(0);

public int increment() {
    return counter.incrementAndGet();
}

private final AtomicReference<User> currentUser = new AtomicReference<>();

public boolean updateUser(User newUser) {
    return currentUser.compareAndSet(currentUser.get(), newUser);
}
```

### 锁优化

```java
private final ReentrantLock lock = new ReentrantLock();

public void criticalSection() {
    lock.lock();
    try {
    } finally {
        lock.unlock();
    }
}

private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

public void read() {
    rwLock.readLock().lock();
    try {
    } finally {
        rwLock.readLock().unlock();
    }
}

public void write() {
    rwLock.writeLock().lock();
    try {
    } finally {
        rwLock.writeLock().unlock();
    }
}
```

---

## JVM 调优

### 堆内存配置

```bash
java -Xms4g -Xmx4g \
     -XX:MaxMetaspaceSize=512m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar myapp.jar
```

### GC 优化

| JVM 参数 | 说明 | 推荐值 |
|----------|------|---------|
| -Xms | 初始堆大小 | 与 -Xmx 相同 |
| -Xmx | 最大堆大小 | 根据应用调整 |
| -XX:MaxMetaspaceSize | 元空间大小 | 256m-512m |
| -XX:+UseG1GC | G1 垃圾收集器 | 推荐 |
| -XX:MaxGCPauseMillis | 最大 GC 暂停时间 | 200 |
| -XX:+UseStringDeduplication | 字符串去重 | 推荐 |

### JIT 编译优化

```bash
-XX:+TieredCompilation
-XX:CompileThreshold=10000
-XX:+UseOnStackReplacement
```

### 性能监控

```bash
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9010
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
-Xlog:gc*:file=gc.log:time,uptime:filecount=10,filesize=100m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/path/to/dumps
```

---

## 生产环境配置

### 应用配置

```yaml
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

### 系统配置

```bash
net.core.somaxconn = 1024
net.core.netdev_max_backlog = 5000
net.ipv4.tcp_max_syn_backlog = 2048
net.ipv4.tcp_tw_reuse = 1
net.ipv4.ip_local_port_range = 1024 65535

est soft nofile 65536
est hard nofile 65536
est soft nproc 4096
est hard nproc 4096
```

### 健康检查

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

## 性能监控

### 使用 Monitor 模块

```java
JvmMonitor jvmMonitor = JvmMonitor.getInstance();
Metrics jvmMetrics = jvmMonitor.getMetrics();
System.out.println("Heap used: " + jvmMetrics.get("memory.heap.used"));

SystemMonitor systemMonitor = SystemMonitor.getInstance();
Metrics systemMetrics = systemMonitor.getMetrics();
System.out.println("CPU load: " + systemMetrics.get("cpu.load"));
```

### 健康检查

```java
HealthCheck databaseCheck = new HealthCheck() {
    @Override
    public HealthCheckResult check() {
        try {
            database.ping();
            return HealthCheckResult.healthy("Database is ok");
        } catch (Exception e) {
            return HealthCheckResult.unhealthy("Database error: " + e.getMessage());
        }
    }
};
```

---

## 性能调优检查清单

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

