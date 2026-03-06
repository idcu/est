# EST 框架实际使用示例

本文档提供 EST 框架在实际项目中的使用示例，涵盖常见场景和最佳实践。

---

## 目录

1. [Web应用示例](#1-web应用示例)
2. [RESTful API示例](#2-restful-api示例)
3. [数据访问示例](#3-数据访问示例)
4. [缓存应用示例](#4-缓存应用示例)
5. [事件驱动示例](#5-事件驱动示例)
6. [安全认证示例](#6-安全认证示例)
7. [任务调度示例](#7-任务调度示例)
8. [消息队列示例](#8-消息队列示例)
9. [监控与日志示例](#9-监控与日志示例)
10. [AI集成示例](#10-ai集成示例)

---

## 1. Web应用示例

### 1.1 简单博客系统

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlogApplication {
    
    private static final Map<String, BlogPost> posts = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        WebApplication app = Web.create("My Blog", "1.0.0");
        
        app.get("/", (req, res) -> res.render("index", Map.of("posts", posts.values())));
        
        app.get("/posts/new", (req, res) -> res.render("new-post"));
        
        app.post("/posts", (req, res) -> {
            String title = req.param("title");
            String content = req.param("content");
            String id = UUID.randomUUID().toString();
            posts.put(id, new BlogPost(id, title, content, new Date()));
            res.redirect("/posts/" + id);
        });
        
        app.get("/posts/:id", (req, res) -> {
            String id = req.param("id");
            BlogPost post = posts.get(id);
            if (post != null) {
                res.render("post", Map.of("post", post));
            } else {
                res.status(404).send("Post not found");
            }
        });
        
        app.run(8080);
    }
    
    static class BlogPost {
        String id;
        String title;
        String content;
        Date createdAt;
        
        BlogPost(String id, String title, String content, Date createdAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.createdAt = createdAt;
        }
    }
}
```

### 1.2 待办事项应用

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TodoApplication {
    
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        TodoRepository repository = new InMemoryTodoRepository();
        TodoService service = new TodoService(repository);
        container.registerSingleton(TodoService.class, service);
        
        WebApplication app = Web.create("Todo App", "1.0.0", container);
        
        app.get("/", (req, res) -> {
            List<Todo> todos = service.getAllTodos();
            res.render("index", Map.of("todos", todos));
        });
        
        app.post("/todos", (req, res) -> {
            String title = req.param("title");
            String description = req.param("description");
            service.createTodo(title, description);
            res.redirect("/");
        });
        
        app.post("/todos/:id/complete", (req, res) -> {
            String id = req.param("id");
            service.completeTodo(id);
            res.redirect("/");
        });
        
        app.delete("/todos/:id", (req, res) -> {
            String id = req.param("id");
            service.deleteTodo(id);
            res.status(204).send("");
        });
        
        app.run(8080);
    }
}

class Todo {
    String id;
    String title;
    String description;
    boolean completed;
    Date createdAt;
}

interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(String id);
    List<Todo> findAll();
    void deleteById(String id);
}

class InMemoryTodoRepository implements TodoRepository {
    private final Map<String, Todo> todos = new ConcurrentHashMap<>();
    
    @Override
    public Todo save(Todo todo) {
        if (todo.id == null) {
            todo.id = UUID.randomUUID().toString();
            todo.createdAt = new Date();
        }
        todos.put(todo.id, todo);
        return todo;
    }
    
    @Override
    public Optional<Todo> findById(String id) {
        return Optional.ofNullable(todos.get(id));
    }
    
    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }
    
    @Override
    public void deleteById(String id) {
        todos.remove(id);
    }
}

class TodoService {
    private final TodoRepository repository;
    
    TodoService(TodoRepository repository) {
        this.repository = repository;
    }
    
    public Todo createTodo(String title, String description) {
        Todo todo = new Todo();
        todo.title = title;
        todo.description = description;
        todo.completed = false;
        return repository.save(todo);
    }
    
    public void completeTodo(String id) {
        repository.findById(id).ifPresent(todo -> {
            todo.completed = true;
            repository.save(todo);
        });
    }
    
    public void deleteTodo(String id) {
        repository.deleteById(id);
    }
    
    public List<Todo> getAllTodos() {
        return repository.findAll();
    }
}
```

---

## 2. RESTful API示例

### 2.1 用户管理API

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserApiApplication {
    
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        WebApplication app = Web.create("User API", "1.0.0");
        
        app.get("/api/users", (req, res) -> {
            int page = Integer.parseInt(req.param("page", "1"));
            int size = Integer.parseInt(req.param("size", "10"));
            List<User> userList = new ArrayList<>(users.values());
            int start = (page - 1) * size;
            int end = Math.min(start + size, userList.size());
            res.json(Map.of(
                "data", userList.subList(start, end),
                "page", page,
                "size", size,
                "total", userList.size()
            ));
        });
        
        app.get("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            User user = users.get(id);
            if (user != null) {
                res.json(user);
            } else {
                res.status(404).json(Map.of("error", "User not found"));
            }
        });
        
        app.post("/api/users", (req, res) -> {
            User user = req.body(User.class);
            if (user.email == null || user.name == null) {
                res.status(400).json(Map.of("error", "Name and email are required"));
                return;
            }
            user.id = UUID.randomUUID().toString();
            user.createdAt = new Date();
            users.put(user.id, user);
            res.status(201).json(user);
        });
        
        app.put("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            User existingUser = users.get(id);
            if (existingUser == null) {
                res.status(404).json(Map.of("error", "User not found"));
                return;
            }
            User update = req.body(User.class);
            if (update.name != null) existingUser.name = update.name;
            if (update.email != null) existingUser.email = update.email;
            existingUser.updatedAt = new Date();
            res.json(existingUser);
        });
        
        app.delete("/api/users/:id", (req, res) -> {
            String id = req.param("id");
            if (users.remove(id) != null) {
                res.status(204).send("");
            } else {
                res.status(404).json(Map.of("error", "User not found"));
            }
        });
        
        app.run(8080);
    }
    
    static class User {
        String id;
        String name;
        String email;
        Date createdAt;
        Date updatedAt;
    }
}
```

### 2.2 文件上传API

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

public class FileUploadApplication {
    
    private static final String UPLOAD_DIR = "./uploads";
    
    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        
        WebApplication app = Web.create("File Upload API", "1.0.0");
        
        app.post("/api/upload", (req, res) -> {
            MultipartFile file = req.file("file");
            if (file == null) {
                res.status(400).json(Map.of("error", "No file provided"));
                return;
            }
            
            String originalFilename = file.getOriginalFilename();
            String extension = getExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + "." + extension;
            Path filePath = Paths.get(UPLOAD_DIR, newFilename);
            
            file.transferTo(filePath.toFile());
            
            res.json(Map.of(
                "filename", newFilename,
                "originalFilename", originalFilename,
                "size", file.getSize(),
                "contentType", file.getContentType(),
                "url", "/uploads/" + newFilename
            ));
        });
        
        app.get("/uploads/:filename", (req, res) -> {
            String filename = req.param("filename");
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            if (Files.exists(filePath)) {
                res.sendFile(filePath.toFile());
            } else {
                res.status(404).json(Map.of("error", "File not found"));
            }
        });
        
        app.delete("/uploads/:filename", (req, res) -> {
            String filename = req.param("filename");
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            try {
                Files.deleteIfExists(filePath);
                res.status(204).send("");
            } catch (IOException e) {
                res.status(500).json(Map.of("error", "Failed to delete file"));
            }
        });
        
        app.run(8080);
    }
    
    private static String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1) : "";
    }
}
```

---

## 3. 数据访问示例

### 3.1 JDBC数据访问

```java
import ltd.idcu.est.features.data.api.Repository;
import ltd.idcu.est.features.data.api.Entity;
import ltd.idcu.est.features.data.api.Id;
import ltd.idcu.est.features.data.api.Column;
import ltd.idcu.est.features.data.jdbc.JdbcRepository;
import ltd.idcu.est.features.data.jdbc.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class JdbcDataExample {
    
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        ProductRepository repository = new JdbcProductRepository(dataSource);
        
        initDatabase(dataSource);
        
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(999.99);
        product.setStock(50);
        repository.save(product);
        
        Optional<Product> found = repository.findById(product.getId());
        found.ifPresent(p -> System.out.println("Found: " + p.getName()));
        
        List<Product> all = repository.findAll();
        System.out.println("Total products: " + all.size());
        
        List<Product> expensive = repository.findByPriceGreaterThan(500.0);
        System.out.println("Expensive products: " + expensive.size());
    }
    
    private static DataSource createDataSource() {
        return JdbcDataSource.builder()
            .url("jdbc:h2:mem:testdb")
            .username("sa")
            .password("")
            .build();
    }
    
    private static void initDatabase(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE products (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,
                    stock INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

@Entity("products")
class Product {
    @Id
    private Long id;
    
    @Column("name")
    private String name;
    
    @Column("price")
    private Double price;
    
    @Column("stock")
    private Integer stock;
    
    @Column("created_at")
    private java.util.Date createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public java.util.Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.util.Date createdAt) { this.createdAt = createdAt; }
}

interface ProductRepository extends Repository<Product, Long> {
    List<Product> findByPriceGreaterThan(Double price);
    List<Product> findByNameContaining(String name);
    Optional<Product> findByName(String name);
}

class JdbcProductRepository extends JdbcRepository<Product, Long> implements ProductRepository {
    
    public JdbcProductRepository(DataSource dataSource) {
        super(dataSource, Product.class);
    }
    
    @Override
    public List<Product> findByPriceGreaterThan(Double price) {
        String sql = "SELECT * FROM products WHERE price > ?";
        return query(sql, Product.class, price);
    }
    
    @Override
    public List<Product> findByNameContaining(String name) {
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        return query(sql, Product.class, "%" + name + "%");
    }
    
    @Override
    public Optional<Product> findByName(String name) {
        String sql = "SELECT * FROM products WHERE name = ? LIMIT 1";
        List<Product> results = query(sql, Product.class, name);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
```

### 3.2 MongoDB数据访问

```java
import ltd.idcu.est.features.data.api.Repository;
import ltd.idcu.est.features.data.mongodb.MongoRepository;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.List;
import java.util.Optional;

public class MongoDataExample {
    
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("est_db");
        
        OrderRepository repository = new MongoOrderRepository(database);
        
        Order order = new Order();
        order.setOrderNumber("ORD-001");
        order.setCustomerId("CUST-123");
        order.setItems(List.of(
            new OrderItem("Product A", 2, 29.99),
            new OrderItem("Product B", 1, 49.99)
        ));
        order.setTotal(109.97);
        repository.save(order);
        
        Optional<Order> found = repository.findByOrderNumber("ORD-001");
        found.ifPresent(o -> System.out.println("Found order: " + o.getOrderNumber()));
        
        List<Order> customerOrders = repository.findByCustomerId("CUST-123");
        System.out.println("Customer orders: " + customerOrders.size());
    }
}

class Order {
    private String id;
    private String orderNumber;
    private String customerId;
    private List<OrderItem> items;
    private Double total;
    private String status;
    private java.util.Date createdAt;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.util.Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.util.Date createdAt) { this.createdAt = createdAt; }
}

class OrderItem {
    private String productName;
    private Integer quantity;
    private Double price;
    
    public OrderItem() {}
    public OrderItem(String productName, Integer quantity, Double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}

interface OrderRepository extends Repository<Order, String> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByCustomerId(String customerId);
    List<Order> findByStatus(String status);
}

class MongoOrderRepository extends MongoRepository<Order, String> implements OrderRepository {
    
    public MongoOrderRepository(MongoDatabase database) {
        super(database, Order.class, "orders");
    }
    
    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return findOne("orderNumber", orderNumber);
    }
    
    @Override
    public List<Order> findByCustomerId(String customerId) {
        return findMany("customerId", customerId);
    }
    
    @Override
    public List<Order> findByStatus(String status) {
        return findMany("status", status);
    }
}
```

---

## 4. 缓存应用示例

### 4.1 多层缓存策略

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCache;
import ltd.idcu.est.features.cache.redis.RedisCache;

import java.util.Optional;

public class MultiLevelCacheExample {
    
    public static void main(String[] args) {
        Cache<String, Product> localCache = MemoryCache.<String, Product>builder()
            .maxSize(1000)
            .expireAfterWrite(300)
            .build();
        
        Cache<String, Product> remoteCache = RedisCache.<String, Product>builder()
            .host("localhost")
            .port(6379)
            .expireAfterWrite(3600)
            .build();
        
        ProductService service = new ProductService(localCache, remoteCache);
        
        Product product = service.getProduct("P001");
        System.out.println("Product: " + product.getName());
        
        Product cachedProduct = service.getProduct("P001");
        System.out.println("Cached Product: " + cachedProduct.getName());
    }
}

class ProductService {
    private final Cache<String, Product> localCache;
    private final Cache<String, Product> remoteCache;
    private final ProductRepository repository;
    
    public ProductService(Cache<String, Product> localCache,
                         Cache<String, Product> remoteCache) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
        this.repository = new ProductRepository();
    }
    
    public Product getProduct(String productId) {
        Product product = localCache.get(productId);
        if (product != null) {
            System.out.println("Cache hit (local)");
            return product;
        }
        
        product = remoteCache.get(productId);
        if (product != null) {
            System.out.println("Cache hit (remote)");
            localCache.put(productId, product);
            return product;
        }
        
        System.out.println("Cache miss");
        product = repository.findById(productId);
        if (product != null) {
            remoteCache.put(productId, product);
            localCache.put(productId, product);
        }
        return product;
    }
    
    public void updateProduct(Product product) {
        repository.save(product);
        localCache.remove(product.getId());
        remoteCache.remove(product.getId());
    }
    
    public void evictAll() {
        localCache.clear();
        remoteCache.clear();
    }
}

class ProductRepository {
    public Product findById(String id) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Product product = new Product();
        product.setId(id);
        product.setName("Product " + id);
        product.setPrice(99.99);
        return product;
    }
    
    public void save(Product product) {
        System.out.println("Saving product: " + product.getId());
    }
}

class Product {
    private String id;
    private String name;
    private Double price;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
```

### 4.2 缓存穿透与击穿防护

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CacheProtectionExample {
    
    public static void main(String[] args) {
        Cache<String, Product> cache = MemoryCache.<String, Product>builder()
            .maxSize(1000)
            .expireAfterWrite(300)
            .build();
        
        ProductService service = new ProductService(cache);
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            new Thread(() -> {
                Product product = service.getProductWithProtection("P001");
                System.out.println("Thread " + index + ": " + product.getName());
            }).start();
        }
    }
}

class ProductService {
    private final Cache<String, Product> cache;
    private final ProductRepository repository;
    private final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();
    private static final Product NULL_PRODUCT = new Product();
    
    public ProductService(Cache<String, Product> cache) {
        this.cache = cache;
        this.repository = new ProductRepository();
    }
    
    public Product getProductWithProtection(String productId) {
        Product product = cache.get(productId);
        
        if (product == NULL_PRODUCT) {
            return null;
        }
        
        if (product != null) {
            return product;
        }
        
        Lock lock = locks.computeIfAbsent(productId, k -> new ReentrantLock());
        lock.lock();
        try {
            product = cache.get(productId);
            if (product != null) {
                return product;
            }
            
            product = repository.findById(productId);
            
            if (product == null) {
                cache.put(productId, NULL_PRODUCT);
            } else {
                cache.put(productId, product);
            }
            
            return product;
        } finally {
            lock.unlock();
            locks.remove(productId);
        }
    }
    
    public Product getProductWithRandomTtl(String productId) {
        Product product = cache.get(productId);
        if (product != null) {
            return product;
        }
        
        product = repository.findById(productId);
        if (product != null) {
            int randomTtl = 300 + (int) (Math.random() * 100);
            cache.put(productId, product, randomTtl);
        }
        return product;
    }
}
```

---

## 5. 事件驱动示例

### 5.1 用户注册事件流

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEventBus;
import ltd.idcu.est.features.event.async.AsyncEventBus;

import java.util.Date;

public class EventDrivenExample {
    
    public static void main(String[] args) throws InterruptedException {
        EventBus eventBus = new AsyncEventBus();
        
        eventBus.register(new UserRegistrationListener());
        eventBus.register(new WelcomeEmailListener());
        eventBus.register(new LoyaltyPointsListener());
        eventBus.register(new AnalyticsListener());
        
        UserService userService = new UserService(eventBus);
        
        User user = new User();
        user.setId("U001");
        user.setEmail("user@example.com");
        user.setName("John Doe");
        
        userService.registerUser(user);
        
        Thread.sleep(1000);
    }
}

class UserRegisteredEvent implements Event {
    private final User user;
    private final Date occurredAt;
    
    public UserRegisteredEvent(User user) {
        this.user = user;
        this.occurredAt = new Date();
    }
    
    public User getUser() { return user; }
    public Date getOccurredAt() { return occurredAt; }
}

class UserRegistrationListener implements EventListener<UserRegisteredEvent> {
    @Override
    public void onEvent(UserRegisteredEvent event) {
        System.out.println("[Registration] User registered: " + event.getUser().getEmail());
    }
}

class WelcomeEmailListener implements EventListener<UserRegisteredEvent> {
    @Override
    public void onEvent(UserRegisteredEvent event) {
        System.out.println("[Email] Sending welcome email to: " + event.getUser().getEmail());
        sendEmail(event.getUser());
    }
    
    private void sendEmail(User user) {
        try {
            Thread.sleep(100);
            System.out.println("[Email] Welcome email sent to: " + user.getEmail());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class LoyaltyPointsListener implements EventListener<UserRegisteredEvent> {
    @Override
    public void onEvent(UserRegisteredEvent event) {
        System.out.println("[Loyalty] Awarding welcome points to: " + event.getUser().getId());
        awardPoints(event.getUser(), 100);
    }
    
    private void awardPoints(User user, int points) {
        try {
            Thread.sleep(50);
            System.out.println("[Loyalty] " + points + " points awarded to: " + user.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class AnalyticsListener implements EventListener<UserRegisteredEvent> {
    @Override
    public void onEvent(UserRegisteredEvent event) {
        System.out.println("[Analytics] Tracking registration event for: " + event.getUser().getId());
        trackEvent(event);
    }
    
    private void trackEvent(UserRegisteredEvent event) {
        try {
            Thread.sleep(30);
            System.out.println("[Analytics] Event tracked: UserRegistered");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class UserService {
    private final EventBus eventBus;
    
    public UserService(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
    public void registerUser(User user) {
        System.out.println("[Service] Creating user: " + user.getEmail());
        user.setCreatedAt(new Date());
        eventBus.publish(new UserRegisteredEvent(user));
    }
}

class User {
    private String id;
    private String email;
    private String name;
    private Date createdAt;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
```

### 5.2 订单处理事件链

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.async.AsyncEventBus;

import java.util.ArrayList;
import java.util.List;

public class OrderProcessingExample {
    
    public static void main(String[] args) throws InterruptedException {
        EventBus eventBus = new AsyncEventBus();
        
        InventoryManager inventoryManager = new InventoryManager();
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        ShippingService shippingService = new ShippingService();
        NotificationService notificationService = new NotificationService();
        
        eventBus.register(new OrderCreatedHandler(inventoryManager, paymentProcessor));
        eventBus.register(new InventoryReservedHandler(paymentProcessor));
        eventBus.register(new PaymentProcessedHandler(shippingService));
        eventBus.register(new OrderShippedHandler(notificationService));
        
        OrderService orderService = new OrderService(eventBus);
        
        Order order = new Order();
        order.setId("ORD-001");
        order.setItems(List.of(
            new OrderItem("P001", 2),
            new OrderItem("P002", 1)
        ));
        order.setTotal(299.97);
        
        orderService.createOrder(order);
        
        Thread.sleep(2000);
    }
}

class OrderCreatedEvent implements Event {
    private final Order order;
    public OrderCreatedEvent(Order order) { this.order = order; }
    public Order getOrder() { return order; }
}

class InventoryReservedEvent implements Event {
    private final Order order;
    private final boolean success;
    public InventoryReservedEvent(Order order, boolean success) {
        this.order = order;
        this.success = success;
    }
    public Order getOrder() { return order; }
    public boolean isSuccess() { return success; }
}

class PaymentProcessedEvent implements Event {
    private final Order order;
    private final boolean success;
    public PaymentProcessedEvent(Order order, boolean success) {
        this.order = order;
        this.success = success;
    }
    public Order getOrder() { return order; }
    public boolean isSuccess() { return success; }
}

class OrderShippedEvent implements Event {
    private final Order order;
    private final String trackingNumber;
    public OrderShippedEvent(Order order, String trackingNumber) {
        this.order = order;
        this.trackingNumber = trackingNumber;
    }
    public Order getOrder() { return order; }
    public String getTrackingNumber() { return trackingNumber; }
}

class OrderCreatedHandler implements EventListener<OrderCreatedEvent> {
    private final InventoryManager inventoryManager;
    private final PaymentProcessor paymentProcessor;
    
    public OrderCreatedHandler(InventoryManager inventoryManager,
                                PaymentProcessor paymentProcessor) {
        this.inventoryManager = inventoryManager;
        this.paymentProcessor = paymentProcessor;
    }
    
    @Override
    public void onEvent(OrderCreatedEvent event) {
        System.out.println("[Order] Processing order: " + event.getOrder().getId());
        boolean reserved = inventoryManager.reserveInventory(event.getOrder());
        eventBus().publish(new InventoryReservedEvent(event.getOrder(), reserved));
    }
}

class InventoryReservedHandler implements EventListener<InventoryReservedEvent> {
    private final PaymentProcessor paymentProcessor;
    
    public InventoryReservedHandler(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
    
    @Override
    public void onEvent(InventoryReservedEvent event) {
        if (event.isSuccess()) {
            System.out.println("[Inventory] Inventory reserved, processing payment");
            boolean paid = paymentProcessor.processPayment(event.getOrder());
            eventBus().publish(new PaymentProcessedEvent(event.getOrder(), paid));
        } else {
            System.out.println("[Inventory] Failed to reserve inventory");
        }
    }
}

class PaymentProcessedHandler implements EventListener<PaymentProcessedEvent> {
    private final ShippingService shippingService;
    
    public PaymentProcessedHandler(ShippingService shippingService) {
        this.shippingService = shippingService;
    }
    
    @Override
    public void onEvent(PaymentProcessedEvent event) {
        if (event.isSuccess()) {
            System.out.println("[Payment] Payment processed, preparing shipment");
            String trackingNumber = shippingService.shipOrder(event.getOrder());
            eventBus().publish(new OrderShippedEvent(event.getOrder(), trackingNumber));
        } else {
            System.out.println("[Payment] Payment failed");
        }
    }
}

class OrderShippedHandler implements EventListener<OrderShippedEvent> {
    private final NotificationService notificationService;
    
    public OrderShippedHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @Override
    public void onEvent(OrderShippedEvent event) {
        System.out.println("[Shipping] Order shipped: " + event.getTrackingNumber());
        notificationService.sendShippingNotification(event.getOrder(), event.getTrackingNumber());
    }
}

class InventoryManager {
    public boolean reserveInventory(Order order) {
        try {
            Thread.sleep(200);
            System.out.println("[Inventory] Reserved inventory for: " + order.getId());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}

class PaymentProcessor {
    public boolean processPayment(Order order) {
        try {
            Thread.sleep(300);
            System.out.println("[Payment] Processed payment: $" + order.getTotal());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}

class ShippingService {
    public String shipOrder(Order order) {
        try {
            Thread.sleep(200);
            String trackingNumber = "TRK-" + System.currentTimeMillis();
            System.out.println("[Shipping] Generated tracking: " + trackingNumber);
            return trackingNumber;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}

class NotificationService {
    public void sendShippingNotification(Order order, String trackingNumber) {
        try {
            Thread.sleep(100);
            System.out.println("[Notification] Sent shipping notification with tracking: " + trackingNumber);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class OrderService {
    private final EventBus eventBus;
    
    public OrderService(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
    public void createOrder(Order order) {
        eventBus.publish(new OrderCreatedEvent(order));
    }
}

class Order {
    private String id;
    private List<OrderItem> items;
    private Double total;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}

class OrderItem {
    private String productId;
    private Integer quantity;
    
    public OrderItem() {}
    public OrderItem(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
```

---

## 6. 安全认证示例

### 6.1 JWT认证

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.features.security.jwt.JwtAuthentication;
import ltd.idcu.est.features.security.jwt.JwtToken;
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.UserPrincipal;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JwtAuthExample {
    
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    private static final JwtAuthentication jwtAuth = JwtAuthentication.builder()
        .secretKey("my-secret-key-123456")
        .expiration(3600)
        .build();
    
    public static void main(String[] args) {
        users.put("admin", new User("admin", "admin123", "ADMIN"));
        users.put("user", new User("user", "user123", "USER"));
        
        WebApplication app = Web.create("JWT Auth Example", "1.0.0");
        
        app.post("/api/auth/login", (req, res) -> {
            LoginRequest login = req.body(LoginRequest.class);
            User user = users.get(login.username);
            
            if (user == null || !user.password.equals(login.password)) {
                res.status(401).json(Map.of("error", "Invalid credentials"));
                return;
            }
            
            JwtToken token = jwtAuth.generateToken(new UserPrincipal() {
                @Override
                public String getId() { return user.username; }
                @Override
                public String getName() { return user.username; }
                @Override
                public java.util.Set<String> getRoles() {
                    return java.util.Set.of(user.role);
                }
            });
            
            res.json(Map.of(
                "accessToken", token.getAccessToken(),
                "expiresIn", token.getExpiresIn(),
                "tokenType", "Bearer"
            ));
        });
        
        app.get("/api/user/profile", (req, res) -> {
            String authHeader = req.header("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401).json(Map.of("error", "Missing or invalid authorization header"));
                return;
            }
            
            String token = authHeader.substring(7);
            try {
                Authentication auth = jwtAuth.authenticate(token);
                UserPrincipal principal = auth.getPrincipal();
                
                res.json(Map.of(
                    "username", principal.getName(),
                    "roles", principal.getRoles(),
                    "authenticated", auth.isAuthenticated()
                ));
            } catch (Exception e) {
                res.status(401).json(Map.of("error", "Invalid token"));
            }
        });
        
        app.get("/api/admin/dashboard", (req, res) -> {
            String authHeader = req.header("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401).json(Map.of("error", "Unauthorized"));
                return;
            }
            
            String token = authHeader.substring(7);
            try {
                Authentication auth = jwtAuth.authenticate(token);
                if (!auth.getPrincipal().getRoles().contains("ADMIN")) {
                    res.status(403).json(Map.of("error", "Forbidden"));
                    return;
                }
                
                res.json(Map.of(
                    "message", "Welcome to admin dashboard",
                    "stats", Map.of(
                        "users", users.size(),
                        "activeSessions", 42
                    )
                ));
            } catch (Exception e) {
                res.status(401).json(Map.of("error", "Invalid token"));
            }
        });
        
        app.run(8080);
    }
    
    static class User {
        String username;
        String password;
        String role;
        
        User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
    
    static class LoginRequest {
        String username;
        String password;
    }
}
```

### 6.2 基于角色的访问控制

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.features.security.api.Authorization;
import ltd.idcu.est.features.security.api.Permission;
import ltd.idcu.est.features.security.api.Role;

import java.util.*;

public class RbacExample {
    
    public static void main(String[] args) {
        Role adminRole = new Role("ADMIN", Set.of(
            new Permission("user.create"),
            new Permission("user.read"),
            new Permission("user.update"),
            new Permission("user.delete"),
            new Permission("product.create"),
            new Permission("product.read"),
            new Permission("product.update"),
            new Permission("product.delete")
        ));
        
        Role userRole = new Role("USER", Set.of(
            new Permission("user.read"),
            new Permission("user.update"),
            new Permission("product.read")
        ));
        
        Map<String, Role> roles = Map.of("ADMIN", adminRole, "USER", userRole);
        
        AuthorizationService authService = new AuthorizationService(roles);
        
        WebApplication app = Web.create("RBAC Example", "1.0.0");
        
        app.use(new AuthMiddleware(authService));
        
        app.get("/api/products", (req, res) -> {
            if (!req.attribute("hasPermission", "product.read")) {
                res.status(403).json(Map.of("error", "Forbidden"));
                return;
            }
            res.json(List.of(
                Map.of("id", 1, "name", "Product 1"),
                Map.of("id", 2, "name", "Product 2")
            ));
        });
        
        app.post("/api/products", (req, res) -> {
            if (!req.attribute("hasPermission", "product.create")) {
                res.status(403).json(Map.of("error", "Forbidden"));
                return;
            }
            res.status(201).json(Map.of("message", "Product created"));
        });
        
        app.get("/api/users/:id", (req, res) -> {
            if (!req.attribute("hasPermission", "user.read")) {
                res.status(403).json(Map.of("error", "Forbidden"));
                return;
            }
            res.json(Map.of("id", req.param("id"), "name", "User " + req.param("id")));
        });
        
        app.delete("/api/users/:id", (req, res) -> {
            if (!req.attribute("hasPermission", "user.delete")) {
                res.status(403).json(Map.of("error", "Forbidden"));
                return;
            }
            res.status(204).send("");
        });
        
        app.run(8080);
    }
}

class AuthorizationService {
    private final Map<String, Role> roles;
    
    public AuthorizationService(Map<String, Role> roles) {
        this.roles = roles;
    }
    
    public boolean hasPermission(String roleName, String permissionName) {
        Role role = roles.get(roleName);
        if (role == null) return false;
        return role.getPermissions().stream()
            .anyMatch(p -> p.getName().equals(permissionName));
    }
    
    public boolean hasAnyPermission(String roleName, Set<String> permissionNames) {
        return permissionNames.stream()
            .anyMatch(p -> hasPermission(roleName, p));
    }
    
    public boolean hasAllPermissions(String roleName, Set<String> permissionNames) {
        return permissionNames.stream()
            .allMatch(p -> hasPermission(roleName, p));
    }
}

class AuthMiddleware implements Middleware {
    private final AuthorizationService authService;
    
    public AuthMiddleware(AuthorizationService authService) {
        this.authService = authService;
    }
    
    @Override
    public boolean before(Request req, Response res) {
        String role = req.header("X-Role", "USER");
        req.attribute("role", role);
        
        req.attribute("hasPermission", (String perm) -> 
            authService.hasPermission(role, perm));
        
        return true;
    }
    
    @Override
    public String getName() { return "auth"; }
    
    @Override
    public int getPriority() { return 100; }
}
```

---

## 7. 任务调度示例

### 7.1 Cron表达式调度

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.cron.CronScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedScheduler;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SchedulerExample {
    
    public static void main(String[] args) throws InterruptedException {
        Scheduler cronScheduler = new CronScheduler();
        Scheduler fixedScheduler = new FixedScheduler();
        
        cronScheduler.schedule("daily-report", "0 0 9 * * ?", () -> {
            System.out.println("[Cron] Generating daily report at: " + new Date());
            generateDailyReport();
        });
        
        cronScheduler.schedule("hourly-cleanup", "0 0 * * * ?", () -> {
            System.out.println("[Cron] Running hourly cleanup at: " + new Date());
            cleanupTempFiles();
        });
        
        fixedScheduler.scheduleAtFixedRate("heartbeat", () -> {
            System.out.println("[Fixed] Heartbeat at: " + new Date());
        }, 0, 10, TimeUnit.SECONDS);
        
        fixedScheduler.scheduleWithFixedDelay("data-sync", () -> {
            System.out.println("[Fixed] Starting data sync at: " + new Date());
            syncData();
            System.out.println("[Fixed] Data sync completed at: " + new Date());
        }, 5, 30, TimeUnit.SECONDS);
        
        System.out.println("Schedulers started. Press Ctrl+C to stop.");
        
        Thread.sleep(60000);
        
        cronScheduler.cancel("daily-report");
        cronScheduler.cancel("hourly-cleanup");
        fixedScheduler.cancel("heartbeat");
        fixedScheduler.cancel("data-sync");
        
        cronScheduler.shutdown();
        fixedScheduler.shutdown();
    }
    
    private static void generateDailyReport() {
        try {
            Thread.sleep(1000);
            System.out.println("[Report] Daily report generated successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static void cleanupTempFiles() {
        try {
            Thread.sleep(500);
            System.out.println("[Cleanup] Temporary files cleaned up");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static void syncData() {
        try {
            Thread.sleep(2000);
            System.out.println("[Sync] Data synchronized with remote server");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### 7.2 分布式任务调度

```java
import ltd.idcu.est.features.scheduler.api.Scheduler;
import ltd.idcu.est.features.scheduler.cron.CronScheduler;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.redis.RedisCache;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DistributedSchedulerExample {
    
    public static void main(String[] args) throws InterruptedException {
        Cache<String, String> lockCache = RedisCache.<String, String>builder()
            .host("localhost")
            .port(6379)
            .build();
        
        DistributedScheduler scheduler = new DistributedScheduler(lockCache, "server-1");
        
        scheduler.schedule("distributed-task", "0 */5 * * * ?", () -> {
            System.out.println("[Distributed] Attempting to run task at: " + new Date());
            if (scheduler.tryAcquireLock("distributed-task", 60)) {
                try {
                    System.out.println("[Distributed] Lock acquired, running task");
                    runDistributedTask();
                } finally {
                    scheduler.releaseLock("distributed-task");
                }
            } else {
                System.out.println("[Distributed] Could not acquire lock, skipping");
            }
        });
        
        System.out.println("Distributed scheduler started");
        Thread.sleep(300000);
        
        scheduler.shutdown();
    }
    
    private static void runDistributedTask() {
        try {
            Thread.sleep(5000);
            System.out.println("[Distributed] Task completed successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class DistributedScheduler {
    private final Scheduler scheduler;
    private final Cache<String, String> lockCache;
    private final String serverId;
    
    public DistributedScheduler(Cache<String, String> lockCache, String serverId) {
        this.scheduler = new CronScheduler();
        this.lockCache = lockCache;
        this.serverId = serverId;
    }
    
    public void schedule(String taskId, String cron, Runnable task) {
        scheduler.schedule(taskId, cron, task);
    }
    
    public boolean tryAcquireLock(String taskId, int ttlSeconds) {
        String lockKey = "lock:" + taskId;
        String currentOwner = lockCache.get(lockKey);
        if (currentOwner != null && !currentOwner.equals(serverId)) {
            return false;
        }
        lockCache.put(lockKey, serverId, ttlSeconds);
        return serverId.equals(lockCache.get(lockKey));
    }
    
    public void releaseLock(String taskId) {
        String lockKey = "lock:" + taskId;
        String owner = lockCache.get(lockKey);
        if (serverId.equals(owner)) {
            lockCache.remove(lockKey);
        }
    }
    
    public void shutdown() {
        scheduler.shutdown();
    }
}
```

---

## 8. 消息队列示例

### 8.1 Kafka消息生产者与消费者

```java
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.kafka.KafkaMessageProducer;
import ltd.idcu.est.features.messaging.kafka.KafkaMessageConsumer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KafkaMessagingExample {
    
    private static final String TOPIC = "order-events";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    
    public static void main(String[] args) throws InterruptedException {
        MessageProducer producer = KafkaMessageProducer.builder()
            .bootstrapServers(BOOTSTRAP_SERVERS)
            .clientId("order-producer")
            .build();
        
        MessageConsumer consumer = KafkaMessageConsumer.builder()
            .bootstrapServers(BOOTSTRAP_SERVERS)
            .groupId("order-consumer-group")
            .topic(TOPIC)
            .build();
        
        consumer.subscribe(message -> {
            System.out.println("[Consumer] Received message: " + message.getBody());
            processOrderEvent(message);
        });
        
        OrderEventProducer orderEventProducer = new OrderEventProducer(producer);
        
        for (int i = 1; i <= 5; i++) {
            OrderEvent event = new OrderEvent();
            event.setOrderId("ORD-" + i);
            event.setEventType("CREATED");
            event.setTimestamp(new Date());
            event.setData(Map.of("customerId", "CUST-" + i, "total", i * 100.0));
            
            orderEventProducer.publishOrderEvent(event);
            Thread.sleep(1000);
        }
        
        Thread.sleep(5000);
        
        producer.close();
        consumer.close();
    }
    
    private static void processOrderEvent(Message<OrderEvent> message) {
        OrderEvent event = message.getBody();
        System.out.println("[Processor] Processing order: " + event.getOrderId() + 
                          ", Type: " + event.getEventType());
    }
}

class OrderEventProducer {
    private final MessageProducer producer;
    
    public OrderEventProducer(MessageProducer producer) {
        this.producer = producer;
    }
    
    public void publishOrderEvent(OrderEvent event) {
        Message<OrderEvent> message = Message.<OrderEvent>builder()
            .topic("order-events")
            .key(event.getOrderId())
            .body(event)
            .header("event-type", event.getEventType())
            .header("timestamp", event.getTimestamp().toString())
            .build();
        
        producer.send(message);
        System.out.println("[Producer] Sent order event: " + event.getOrderId());
    }
}

class OrderEvent {
    private String orderId;
    private String eventType;
    private Date timestamp;
    private Map<String, Object> data;
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}
```

### 8.2 消息重试与死信队列

```java
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.local.LocalMessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessageConsumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageRetryExample {
    
    private static final String MAIN_TOPIC = "main-topic";
    private static final String RETRY_TOPIC = "retry-topic";
    private static final String DLQ_TOPIC = "dlq-topic";
    private static final int MAX_RETRIES = 3;
    
    public static void main(String[] args) throws InterruptedException {
        MessageProducer producer = new LocalMessageProducer();
        MessageConsumer mainConsumer = new LocalMessageConsumer(MAIN_TOPIC);
        MessageConsumer retryConsumer = new LocalMessageConsumer(RETRY_TOPIC);
        MessageConsumer dlqConsumer = new LocalMessageConsumer(DLQ_TOPIC);
        
        ConcurrentHashMap<String, AtomicInteger> retryCounts = new ConcurrentHashMap<>();
        
        mainConsumer.subscribe(message -> {
            try {
                processMessage(message);
            } catch (Exception e) {
                handleProcessingError(producer, message, retryCounts);
            }
        });
        
        retryConsumer.subscribe(message -> {
            try {
                processMessage(message);
            } catch (Exception e) {
                handleProcessingError(producer, message, retryCounts);
            }
        });
        
        dlqConsumer.subscribe(message -> {
            System.out.println("[DLQ] Message in dead letter queue: " + message.getKey());
            saveToDlq(message);
        });
        
        for (int i = 1; i <= 10; i++) {
            Message<String> message = Message.<String>builder()
                .topic(MAIN_TOPIC)
                .key("msg-" + i)
                .body("Message content " + i)
                .header("shouldFail", i % 3 == 0 ? "true" : "false")
                .build();
            
            producer.send(message);
            Thread.sleep(500);
        }
        
        Thread.sleep(5000);
        
        producer.close();
        mainConsumer.close();
        retryConsumer.close();
        dlqConsumer.close();
    }
    
    private static void processMessage(Message<String> message) {
        String shouldFail = message.getHeader("shouldFail", "false");
        if ("true".equals(shouldFail)) {
            throw new RuntimeException("Simulated processing failure");
        }
        System.out.println("[Processor] Successfully processed: " + message.getKey());
    }
    
    private static void handleProcessingError(MessageProducer producer,
                                              Message<?> message,
                                              ConcurrentHashMap<String, AtomicInteger> retryCounts) {
        String key = message.getKey();
        AtomicInteger count = retryCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
        int currentRetry = count.incrementAndGet();
        
        if (currentRetry <= MAX_RETRIES) {
            System.out.println("[Retry] Retrying message " + key + 
                              " (attempt " + currentRetry + "/" + MAX_RETRIES + ")");
            
            Message<?> retryMessage = Message.builder()
                .topic(RETRY_TOPIC)
                .key(key)
                .body(message.getBody())
                .headers(message.getHeaders())
                .header("retry-count", String.valueOf(currentRetry))
                .build();
            
            producer.send(retryMessage);
        } else {
            System.out.println("[DLQ] Max retries reached for " + key + ", sending to DLQ");
            
            Message<?> dlqMessage = Message.builder()
                .topic(DLQ_TOPIC)
                .key(key)
                .body(message.getBody())
                .headers(message.getHeaders())
                .header("original-topic", MAIN_TOPIC)
                .header("final-retry-count", String.valueOf(currentRetry))
                .build();
            
            producer.send(dlqMessage);
            retryCounts.remove(key);
        }
    }
    
    private static void saveToDlq(Message<?> message) {
        System.out.println("[DLQ] Saved message: " + message.getKey());
    }
}
```

---

## 9. 监控与日志示例

### 9.1 应用性能监控

```java
import ltd.idcu.est.features.monitor.api.Metrics;
import ltd.idcu.est.features.monitor.api.Monitor;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;
import ltd.idcu.est.features.monitor.system.SystemMonitor;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LoggerFactory;
import ltd.idcu.est.features.logging.console.ConsoleLogger;

import java.util.Timer;
import java.util.TimerTask;

public class MonitoringExample {
    
    private static final Logger logger = LoggerFactory.getLogger(MonitoringExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        Monitor jvmMonitor = JvmMonitor.getInstance();
        Monitor systemMonitor = SystemMonitor.getInstance();
        
        MetricsCollector collector = new MetricsCollector(jvmMonitor, systemMonitor);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                collector.collectAndLog();
            }
        }, 0, 10000);
        
        logger.info("Application started, monitoring active");
        
        simulateWorkload();
        
        Thread.sleep(60000);
        
        timer.cancel();
        logger.info("Monitoring stopped");
    }
    
    private static void simulateWorkload() {
        new Thread(() -> {
            while (true) {
                try {
                    byte[] data = new byte[1024 * 1024];
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}

class MetricsCollector {
    private final Monitor jvmMonitor;
    private final Monitor systemMonitor;
    private final Logger logger = LoggerFactory.getLogger(MetricsCollector.class);
    
    public MetricsCollector(Monitor jvmMonitor, Monitor systemMonitor) {
        this.jvmMonitor = jvmMonitor;
        this.systemMonitor = systemMonitor;
    }
    
    public void collectAndLog() {
        Metrics jvmMetrics = jvmMonitor.getMetrics();
        Metrics systemMetrics = systemMonitor.getMetrics();
        
        logger.info("=== JVM Metrics ===");
        logger.info("Heap Used: {} MB", formatBytes(jvmMetrics.getLong("memory.heap.used", 0L)));
        logger.info("Heap Max: {} MB", formatBytes(jvmMetrics.getLong("memory.heap.max", 0L)));
        logger.info("Threads: {}", jvmMetrics.getInteger("threads.active", 0));
        logger.info("GC Count: {}", jvmMetrics.getLong("gc.count", 0L));
        
        logger.info("=== System Metrics ===");
        logger.info("CPU Load: {}%", systemMetrics.getDouble("cpu.load", 0.0) * 100);
        logger.info("Memory Free: {} MB", formatBytes(systemMetrics.getLong("memory.free", 0L)));
        logger.info("Memory Total: {} MB", formatBytes(systemMetrics.getLong("memory.total", 0L)));
        
        checkThresholds(jvmMetrics, systemMetrics);
    }
    
    private void checkThresholds(Metrics jvmMetrics, Metrics systemMetrics) {
        double cpuLoad = systemMetrics.getDouble("cpu.load", 0.0);
        if (cpuLoad > 0.8) {
            logger.warn("High CPU load detected: {}%", cpuLoad * 100);
        }
        
        long heapUsed = jvmMetrics.getLong("memory.heap.used", 0L);
        long heapMax = jvmMetrics.getLong("memory.heap.max", 0L);
        if (heapMax > 0) {
            double heapUsage = (double) heapUsed / heapMax;
            if (heapUsage > 0.9) {
                logger.error("Critical heap usage: {}%", heapUsage * 100);
            } else if (heapUsage > 0.7) {
                logger.warn("High heap usage: {}%", heapUsage * 100);
            }
        }
    }
    
    private String formatBytes(long bytes) {
        return String.format("%.2f", bytes / (1024.0 * 1024.0));
    }
}
```

### 9.2 结构化日志

```java
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.api.LoggerFactory;
import ltd.idcu.est.features.logging.file.FileLogger;

import java.util.Map;

public class StructuredLoggingExample {
    
    private static final Logger logger = LoggerFactory.getLogger(StructuredLoggingExample.class);
    
    public static void main(String[] args) {
        logger.info("Application starting", Map.of(
            "version", "1.0.0",
            "environment", "production",
            "timestamp", System.currentTimeMillis()
        ));
        
        String userId = "user-123";
        String orderId = "order-456";
        
        try {
            logger.debug("Processing request", Map.of(
                "userId", userId,
                "orderId", orderId,
                "action", "ORDER_PROCESSING"
            ));
            
            validateOrder(orderId);
            
            logger.info("Order validated successfully", Map.of(
                "userId", userId,
                "orderId", orderId,
                "durationMs", 42
            ));
            
            processPayment(userId, orderId, 99.99);
            
            logger.info("Order completed", Map.of(
                "userId", userId,
                "orderId", orderId,
                "amount", 99.99,
                "status", "SUCCESS"
            ));
            
        } catch (ValidationException e) {
            logger.warn("Order validation failed", Map.of(
                "userId", userId,
                "orderId", orderId,
                "error", e.getMessage(),
                "errorCode", "VALIDATION_ERROR"
            ), e);
            
        } catch (PaymentException e) {
            logger.error("Payment processing failed", Map.of(
                "userId", userId,
                "orderId", orderId,
                "amount", 99.99,
                "error", e.getMessage(),
                "errorCode", "PAYMENT