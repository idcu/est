# Data 数据访问 API

数据访问模块提供统一的Repository接口，支持JDBC、内存、Redis和MongoDB等多种存储。

## 核心接口

### Repository 接口

```java
public interface Repository<T, ID> {
    T save(T entity);
    List<T> saveAll(Iterable<T> entities);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    List<T> findAllById(Iterable<ID> ids);
    long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAllById(Iterable<? extends ID> ids);
    void deleteAll(Iterable<? extends T> entities);
    void deleteAll();
}
```

## ORM 注解

```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    @Column(name = "email")
    private String email;
    
    @CreatedAt
    private LocalDateTime createdAt;
    
    @UpdatedAt
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
    
    @Transient
    private String transientField;
    
    // getters and setters
}
```

## 内存存储 (MemoryRepository)

```java
import ltd.idcu.est.features.data.memory.MemoryRepositories;

// 创建内存Repository
Repository<User, Long> userRepo = MemoryRepositories.create(User.class, Long.class);

// CRUD操作
User user = new User();
user.setUsername("john");
user = userRepo.save(user);

Optional<User> found = userRepo.findById(user.getId());
List<User> all = userRepo.findAll();
userRepo.delete(user);
```

## JDBC 存储 (JdbcRepository)

```java
import ltd.idcu.est.features.data.jdbc.JdbcRepositories;
import ltd.idcu.est.features.data.jdbc.JdbcConfig;

// 配置JDBC
JdbcConfig config = JdbcConfig.builder()
    .url("jdbc:mysql://localhost:3306/mydb")
    .username("root")
    .password("password")
    .poolSize(10)
    .build();

// 创建JDBC Repository
Repository<User, Long> jdbcRepo = JdbcRepositories.create(config, User.class, Long.class);

// 事务支持
jdbcRepo.executeInTransaction(() -> {
    userRepo.save(user1);
    userRepo.save(user2);
});
```

## Redis 存储 (RedisRepository)

```java
import ltd.idcu.est.features.data.redis.RedisRepositories;

Repository<User, String> redisRepo = RedisRepositories.create(
    "localhost", 6379, User.class, String.class
);
```

## MongoDB 存储 (MongoRepository)

```java
import ltd.idcu.est.features.data.mongodb.MongoRepositories;

Repository<User, String> mongoRepo = MongoRepositories.create(
    "mongodb://localhost:27017", "mydb", User.class, String.class
);
```

## 查询 DSL

```java
// 简单查询
List<User> users = userRepo.query()
    .where("username").eq("john")
    .and("age").gt(18)
    .orderBy("createdAt", Direction.DESC)
    .limit(10)
    .list();

// 分页
Page<User> page = userRepo.query()
    .where("status").eq("active")
    .page(0, 20);
```
