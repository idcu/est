# EST 最佳实践

欢迎来到 EST 框架最佳实践文档！这里汇集了使用 EST 框架开发的最佳实践和经验总结。

## 目录

- [代码风格](#代码风格)
- [项目结构](#项目结构)
- [性能优化](#性能优化)
- [安全实践](#安全实践)
- [测试策略](#测试策略)
- [部署指南](#部署指南)

---

## 代码风格

### 命名规范

- **类名**: 使用大驼峰命名法，如 `UserService`、`ProductController`
- **方法名**: 使用小驼峰命名法，如 `getUserById`、`createOrder`
- **变量名**: 使用小驼峰命名法，如 `userName`、`orderList`
- **常量名**: 使用全大写下划线分隔，如 `MAX_SIZE`、`DEFAULT_TIMEOUT`
- **包名**: 使用全小写，如 `com.example.service`、`ltd.idcu.est.web`

### 代码格式

- **缩进**: 使用 4 个空格
- **行宽**: 建议不超过 120 字符
- **空行**: 逻辑块之间使用空行分隔
- **括号**: 左括号不换行

### 示例

```java
package com.example.service;

import ltd.idcu.est.core.container.Container;

@Component
public class UserService {
    
    private static final int MAX_RETRY_COUNT = 3;
    
    private final UserRepository userRepository;
    
    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id);
    }
}
```

---

## 项目结构

### 推荐的项目结构

```
my-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── Application.java
│   │   │           ├── controller/
│   │   │           ├── service/
│   │   │           ├── repository/
│   │   │           ├── entity/
│   │   │           ├── dto/
│   │   │           ├── config/
│   │   │           └── util/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── logback.xml
│   │       └── static/
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── service/
└── pom.xml
```

### 分层原则

- **Controller 层**: 处理 HTTP 请求和响应
- **Service 层**: 业务逻辑
- **Repository 层**: 数据访问
- **Entity 层**: 数据模型
- **DTO 层**: 数据传输对象

---

## 性能优化

### 缓存策略

```java
@Component
public class ProductService {
    
    private final Cache<Long, Product> productCache;
    
    @Inject
    public ProductService(CacheManager cacheManager) {
        this.productCache = cacheManager.getCache("products");
    }
    
    public Product getProductById(Long id) {
        Product product = productCache.get(id);
        if (product == null) {
            product = productRepository.findById(id);
            if (product != null) {
                productCache.put(id, product, 1, TimeUnit.HOURS);
            }
        }
        return product;
    }
}
```

### 连接池配置

```yaml
database:
  pool:
    maxSize: 20
    minIdle: 5
    maxWait: 30000
```

### 异步处理

```java
@Component
public class EmailService {
    
    @Async
    public void sendWelcomeEmail(User user) {
        // 发送邮件
    }
}
```

---

## 安全实践

### 输入验证

```java
public User createUser(UserDto userDto) {
    if (userDto.getEmail() == null || !userDto.getEmail().matches(EMAIL_PATTERN)) {
        throw new ValidationException("Invalid email");
    }
    if (userDto.getPassword() == null || userDto.getPassword().length() < 8) {
        throw new ValidationException("Password must be at least 8 characters");
    }
    // ...
}
```

### SQL 注入防护

使用参数化查询：

```java
public User findByEmail(String email) {
    String sql = "SELECT * FROM users WHERE email = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{email}, userRowMapper);
}
```

### 密码加密

```java
@Component
public class PasswordService {
    
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
```

### JWT 认证

```java
@Component
public class JwtService {
    
    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(user.getId().toString())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SECRET_KEY)
            .compact();
    }
}
```

---

## 测试策略

### 单元测试

```java
@Test
public void testGetUserById() {
    User user = new User();
    user.setId(1L);
    user.setName("Test");
    
    when(userRepository.findById(1L)).thenReturn(user);
    
    User result = userService.getUserById(1L);
    
    assertEquals("Test", result.getName());
}
```

### 集成测试

```java
@IntegrationTest
public class UserControllerTest {
    
    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Test");
        userDto.setEmail("test@example.com");
        
        HttpResponse response = httpClient.post("/api/users", userDto);
        
        assertEquals(201, response.getStatusCode());
    }
}
```

### 测试覆盖率目标

- 单元测试覆盖率: ≥ 80%
- 核心业务逻辑: ≥ 95%

---

## 部署指南

### Docker 部署

```dockerfile
FROM eclipse-temurin:21-jre
COPY target/my-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes 部署

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-app
        image: my-app:1.0.0
        ports:
        - containerPort: 8080
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
```

### 健康检查

```java
@Component
public class HealthCheckController {
    
    @Get("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "timestamp", Instant.now().toString()
        );
    }
}
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
