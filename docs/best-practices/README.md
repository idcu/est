# EST 鏈€浣冲疄璺?
娆㈣繋鏉ュ埌 EST 妗嗘灦鏈€浣冲疄璺垫枃妗ｏ紒杩欓噷姹囬泦浜嗕娇鐢?EST 妗嗘灦寮€鍙戠殑鏈€浣冲疄璺靛拰缁忛獙鎬荤粨銆?
## 鐩綍

- [浠ｇ爜椋庢牸](#浠ｇ爜椋庢牸)
- [椤圭洰缁撴瀯](#椤圭洰缁撴瀯)
- [鎬ц兘浼樺寲](#鎬ц兘浼樺寲)
- [瀹夊叏瀹炶返](#瀹夊叏瀹炶返)
- [娴嬭瘯绛栫暐](#娴嬭瘯绛栫暐)
- [閮ㄧ讲鎸囧崡](#閮ㄧ讲鎸囧崡)

---

## 浠ｇ爜椋庢牸

### 鍛藉悕瑙勮寖

- **绫诲悕**: 浣跨敤澶ч┘宄板懡鍚嶆硶锛屽 `UserService`銆乣ProductController`
- **鏂规硶鍚?*: 浣跨敤灏忛┘宄板懡鍚嶆硶锛屽 `getUserById`銆乣createOrder`
- **鍙橀噺鍚?*: 浣跨敤灏忛┘宄板懡鍚嶆硶锛屽 `userName`銆乣orderList`
- **甯搁噺鍚?*: 浣跨敤鍏ㄥぇ鍐欎笅鍒掔嚎鍒嗛殧锛屽 `MAX_SIZE`銆乣DEFAULT_TIMEOUT`
- **鍖呭悕**: 浣跨敤鍏ㄥ皬鍐欙紝濡?`com.example.service`銆乣ltd.idcu.est.web`

### 浠ｇ爜鏍煎紡

- **缂╄繘**: 浣跨敤 4 涓┖鏍?- **琛屽**: 寤鸿涓嶈秴杩?120 瀛楃
- **绌鸿**: 閫昏緫鍧椾箣闂翠娇鐢ㄧ┖琛屽垎闅?- **鎷彿**: 宸︽嫭鍙蜂笉鎹㈣

### 绀轰緥

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

## 椤圭洰缁撴瀯

### 鎺ㄨ崘鐨勯」鐩粨鏋?
```
my-project/
鈹溾攢鈹€ src/
鈹?  鈹溾攢鈹€ main/
鈹?  鈹?  鈹溾攢鈹€ java/
鈹?  鈹?  鈹?  鈹斺攢鈹€ com/
鈹?  鈹?  鈹?      鈹斺攢鈹€ example/
鈹?  鈹?  鈹?          鈹溾攢鈹€ Application.java
鈹?  鈹?  鈹?          鈹溾攢鈹€ controller/
鈹?  鈹?  鈹?          鈹溾攢鈹€ service/
鈹?  鈹?  鈹?          鈹溾攢鈹€ repository/
鈹?  鈹?  鈹?          鈹溾攢鈹€ entity/
鈹?  鈹?  鈹?          鈹溾攢鈹€ dto/
鈹?  鈹?  鈹?          鈹溾攢鈹€ config/
鈹?  鈹?  鈹?          鈹斺攢鈹€ util/
鈹?  鈹?  鈹斺攢鈹€ resources/
鈹?  鈹?      鈹溾攢鈹€ application.yml
鈹?  鈹?      鈹溾攢鈹€ logback.xml
鈹?  鈹?      鈹斺攢鈹€ static/
鈹?  鈹斺攢鈹€ test/
鈹?      鈹斺攢鈹€ java/
鈹?          鈹斺攢鈹€ com/
鈹?              鈹斺攢鈹€ example/
鈹?                  鈹斺攢鈹€ service/
鈹斺攢鈹€ pom.xml
```

### 鍒嗗眰鍘熷垯

- **Controller 灞?*: 澶勭悊 HTTP 璇锋眰鍜屽搷搴?- **Service 灞?*: 涓氬姟閫昏緫
- **Repository 灞?*: 鏁版嵁璁块棶
- **Entity 灞?*: 鏁版嵁妯″瀷
- **DTO 灞?*: 鏁版嵁浼犺緭瀵硅薄

---

## 鎬ц兘浼樺寲

### 缂撳瓨绛栫暐

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

### 杩炴帴姹犻厤缃?
```yaml
database:
  pool:
    maxSize: 20
    minIdle: 5
    maxWait: 30000
```

### 寮傛澶勭悊

```java
@Component
public class EmailService {
    
    @Async
    public void sendWelcomeEmail(User user) {
        // 鍙戦€侀偖浠?    }
}
```

---

## 瀹夊叏瀹炶返

### 杈撳叆楠岃瘉

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

### SQL 娉ㄥ叆闃叉姢

浣跨敤鍙傛暟鍖栨煡璇細

```java
public User findByEmail(String email) {
    String sql = "SELECT * FROM users WHERE email = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{email}, userRowMapper);
}
```

### 瀵嗙爜鍔犲瘑

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

### JWT 璁よ瘉

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

## 娴嬭瘯绛栫暐

### 鍗曞厓娴嬭瘯

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

### 闆嗘垚娴嬭瘯

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

### 娴嬭瘯瑕嗙洊鐜囩洰鏍?
- 鍗曞厓娴嬭瘯瑕嗙洊鐜? 鈮?80%
- 鏍稿績涓氬姟閫昏緫: 鈮?95%

---

## 閮ㄧ讲鎸囧崡

### Docker 閮ㄧ讲

```dockerfile
FROM eclipse-temurin:21-jre
COPY target/my-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes 閮ㄧ讲

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

### 鍋ュ悍妫€鏌?
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

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
