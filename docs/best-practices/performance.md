# 性能优化最佳实践

本文档介绍如何优化 EST 应用的性能。

## 缓存策略

### 1. 合理使用缓存

```java
@Service
public class ProductService {
    private final Cache<String, Product> productCache;
    private final ProductRepository productRepository;
    
    public ProductService(Cache<String, Product> productCache,
                          ProductRepository productRepository) {
        this.productCache = productCache;
        this.productRepository = productRepository;
    }
    
    public Product getProduct(String id) {
        // 先从缓存获取
        Product cached = productCache.get(id);
        if (cached != null) {
            return cached;
        }
        
        // 缓存未命中，从数据库加载
        Product product = productRepository.findById(id);
        
        // 写入缓存，设置过期时间
        if (product != null) {
            productCache.put(id, product, 3600); // 1小时过期
        }
        
        return product;
    }
    
    public void updateProduct(Product product) {
        productRepository.update(product);
        // 更新后清除缓存
        productCache.remove(product.getId());
    }
}
```

### 2. 缓存预热

```java
public class CacheWarmer implements Module {
    private final Cache<String, Product> productCache;
    private final ProductRepository productRepository;
    
    @Override
    public void initialize(EstApplication app) {
        // 应用启动时预热热门数据
        List<Product> hotProducts = productRepository.findHotProducts();
        for (Product product : hotProducts) {
            productCache.put(product.getId(), product, 7200);
        }
        System.out.println("Cache warmed with " + hotProducts.size() + " products");
    }
}
```

### 3. 选择合适的缓存实现

| 场景 | 推荐实现 |
|------|----------|
| 小数据量，快速访问 | MemoryCache |
| 需要持久化 | FileCache |
| 分布式环境 | RedisCache |
| 需要淘汰策略 | LRU MemoryCache |

## 数据库优化

### 1. 使用连接池

```java
// 配置连接池
ConnectionPool pool = ConnectionPool.builder()
    .maxConnections(20)
    .minIdleConnections(5)
    .connectionTimeout(30000)
    .build();

// 使用连接池
JdbcData data = JdbcData.create(pool);
```

### 2. 批量操作

```java
// 不好的做法 - 逐条插入
for (User user : users) {
    repository.save(user);
}

// 好的做法 - 批量插入
repository.saveAll(users);
```

### 3. 索引优化

确保常用查询字段有索引：

```sql
-- 为 email 字段添加索引
CREATE INDEX idx_user_email ON users(email);

-- 为组合查询添加索引
CREATE INDEX idx_user_status_created ON users(status, created_at);
```

## Web 性能优化

### 1. 启用 Gzip 压缩

```java
// 添加压缩中间件
app.use(new CompressionMiddleware());
```

### 2. 静态资源缓存

```java
// 配置静态文件缓存
app.staticFiles("/static", "./public", StaticFileConfig.builder()
    .cacheMaxAge(86400)  // 1天
    .etagEnabled(true)
    .build());
```

### 3. 异步处理

```java
app.getRouter().get("/api/heavy-task", (req, res) -> {
    // 使用虚拟线程处理耗时操作
    CompletableFuture.runAsync(() -> {
        // 耗时操作
        heavyComputation();
    }, Thread.ofVirtual().executor());
    
    res.json(Map.of("status", "processing"));
});
```

## 内存优化

### 1. 及时释放资源

```java
// 使用 try-with-resources
try (InputStream in = file.openInputStream()) {
    // 处理流
} catch (IOException e) {
    // 处理异常
}
```

### 2. 避免内存泄漏

```java
// 不好的做法 - 静态集合持有引用
public class Cache {
    private static final Map<String, Object> CACHE = new HashMap<>();
    
    public static void put(String key, Object value) {
        CACHE.put(key, value); // 永不会被回收
    }
}

// 好的做法 - 使用弱引用或设置过期
public class Cache {
    private final Cache<String, Object> cache;
    
    public Cache() {
        this.cache = MemoryCaches.create(CacheConfig.builder()
            .maxSize(1000)
            .expireAfterWrite(3600)
            .build());
    }
}
```

## 性能监控

### 1. 使用 Monitor 模块

```java
// JVM 监控
JvmMonitor jvmMonitor = JvmMonitor.getInstance();
Metrics jvmMetrics = jvmMonitor.getMetrics();
System.out.println("Heap used: " + jvmMetrics.get("memory.heap.used"));

// 系统监控
SystemMonitor systemMonitor = SystemMonitor.getInstance();
Metrics systemMetrics = systemMonitor.getMetrics();
System.out.println("CPU load: " + systemMetrics.get("cpu.load"));
```

### 2. 健康检查

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

## 性能检查清单

- [ ] 热点数据已缓存
- [ ] 数据库使用连接池
- [ ] 批量操作代替单条操作
- [ ] 静态资源启用缓存
- [ ] 资源使用 try-with-resources
- [ ] 缓存有过期策略
- [ ] 启用性能监控
- [ ] 定期健康检查

## 总结

性能优化是一个持续的过程：
1. 先测量，再优化
2. 优先优化热点路径
3. 使用缓存减少重复计算
4. 批量操作减少 I/O
5. 监控性能指标
