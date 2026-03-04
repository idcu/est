# 安全性最佳实践

本文档介绍如何确保 EST 应用的安全性。

## 输入验证

### 1. 验证所有用户输入

```java
public class UserController {
    public void handle(Request request, Response response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // 验证输入
        if (!ValidationUtils.isValidEmail(email)) {
            response.status(400).json(Map.of(
                "error", "Invalid email format"
            ));
            return;
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            response.status(400).json(Map.of(
                "error", "Password must be at least 8 characters"
            ));
            return;
        }
        
        // 处理业务逻辑
    }
}

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        // 检查是否包含字母、数字和特殊字符
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        return hasLetter && hasDigit;
    }
}
```

### 2. 防止 SQL 注入

```java
// 不好的做法 - 字符串拼接
public User findByEmail(String email) {
    String sql = "SELECT * FROM users WHERE email = '" + email + "'";
    // 危险！如果 email 是 " ' OR '1'='1 "
}

// 好的做法 - 使用参数化查询
public User findByEmail(String email) {
    String sql = "SELECT * FROM users WHERE email = ?";
    return jdbcQuery.queryOne(sql, User.class, email);
}
```

### 3. 防止 XSS 攻击

```java
// 对用户输入进行转义
public class XssUtils {
    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
}

// 在输出时使用
app.getRouter().get("/profile", (req, res) -> {
    String name = req.getParameter("name");
    String safeName = XssUtils.escapeHtml(name);
    res.html("<h1>Hello, " + safeName + "</h1>");
});
```

## 密码安全

### 1. 使用强密码哈希

```java
// 使用 BCrypt 密码加密
PasswordEncoder encoder = new BCryptPasswordEncoder();

// 注册时加密密码
String hashedPassword = encoder.encode(rawPassword);
user.setPassword(hashedPassword);

// 登录时验证
boolean matches = encoder.matches(rawPassword, hashedPassword);
```

### 2. 密码策略

```java
public class PasswordPolicy {
    public static void validate(String password) {
        if (password == null || password.length() < 12) {
            throw new SecurityException("Password must be at least 12 characters");
        }
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> "!@#$%^&*".indexOf(c) >= 0);
        
        if (!hasUpper || !hasLower || !hasDigit || !hasSpecial) {
            throw new SecurityException(
                "Password must contain uppercase, lowercase, digit, and special character"
            );
        }
    }
}
```

## 认证与授权

### 1. 使用 JWT 认证

```java
// 配置 JWT
JwtSecurityConfig config = JwtSecurityConfig.builder()
    .secretKey("your-256-bit-secret-key-here")
    .tokenExpiration(3600)  // 1小时
    .refreshTokenExpiration(86400 * 7)  // 7天
    .build();

TokenProvider tokenProvider = JwtSecurity.createTokenProvider(config);

// 登录时生成 token
if (authenticate(username, password)) {
    String token = tokenProvider.generateToken(Map.of(
        "sub", userId,
        "username", username,
        "roles", user.getRoles()
    ));
    res.json(Map.of("token", token));
}

// 验证 token
String authHeader = request.getHeader("Authorization");
if (authHeader != null && authHeader.startsWith("Bearer ")) {
    String token = authHeader.substring(7);
    try {
        Token decoded = tokenProvider.validateToken(token);
        String userId = decoded.getSubject();
    } catch (TokenException e) {
        res.status(401).json(Map.of("error", "Invalid token"));
    }
}
```

### 2. 实现授权中间件

```java
public class AuthMiddleware implements Middleware {
    private final TokenProvider tokenProvider;
    
    @Override
    public boolean before(Request request, Response response) {
        // 跳过公开路由
        String path = request.getPath();
        if (path.equals("/login") || path.equals("/register")) {
            return true;
        }
        
        // 验证 token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.status(401).json(Map.of("error", "Unauthorized"));
            return false;
        }
        
        try {
            String token = authHeader.substring(7);
            Token decoded = tokenProvider.validateToken(token);
            request.setAttribute("userId", decoded.getSubject());
            request.setAttribute("roles", decoded.getClaim("roles"));
            return true;
        } catch (TokenException e) {
            response.status(401).json(Map.of("error", "Invalid token"));
            return false;
        }
    }
    
    @Override
    public String getName() { return "auth"; }
    
    @Override
    public int getPriority() { return 100; }
}

// 使用中间件
app.use(new AuthMiddleware(tokenProvider));
```

### 3. 基于角色的访问控制

```java
public class AuthorizationUtils {
    public static boolean hasRole(Request request, String role) {
        List<String> roles = (List<String>) request.getAttribute("roles");
        return roles != null && roles.contains(role);
    }
    
    public static void requireRole(Request request, Response response, String role) {
        if (!hasRole(request, role)) {
            response.status(403).json(Map.of("error", "Forbidden"));
            throw new ForbiddenException();
        }
    }
}

// 在路由中使用
app.getRouter().get("/admin", (req, res) -> {
    AuthorizationUtils.requireRole(req, res, "ADMIN");
    // 管理员逻辑
});
```

## Web 安全头

```java
// 配置安全头
app.use((req, res) -> {
    res.header("X-Content-Type-Options", "nosniff");
    res.header("X-Frame-Options", "DENY");
    res.header("X-XSS-Protection", "1; mode=block");
    res.header("Content-Security-Policy", "default-src 'self'");
    res.header("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
    return true;
});
```

## HTTPS 配置

```java
// 生产环境启用 HTTPS
WebApplication app = DefaultWebApplication.create();
app.run(8443, HttpsConfig.builder()
    .keyStorePath("/path/to/keystore.jks")
    .keyStorePassword("password")
    .keyPassword("password")
    .build());
```

## 安全检查清单

- [ ] 所有用户输入已验证
- [ ] 使用参数化查询防止 SQL 注入
- [ ] 输出转义防止 XSS
- [ ] 密码使用强哈希（BCrypt）
- [ ] 使用 JWT 进行认证
- [ ] 实现授权中间件
- [ ] 设置安全头
- [ ] 生产环境使用 HTTPS
- [ ] 敏感数据不记录日志
- [ ] 配置 CORS 策略

## 总结

安全性是一个持续的过程：
1. 永远不信任用户输入
2. 使用成熟的安全库
3. 保持依赖更新
4. 定期进行安全审计
5. 记录安全事件
