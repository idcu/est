# EST Security Group 安全模块组 - 小白从入门到精通

## 目录
1. [什么是 EST Security Group？](#什么是-est-security-group)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Security Group？

### 用大白话理解

EST Security Group 就像是一个"安全保镖"。想象一下你有一个重要的网站，需要保护用户的安全：

**传统方式**：你需要自己写认证、权限控制、审计日志... 很复杂，容易出错！

**EST Security Group 方式**：给你一套完整的安全工具箱，里面有：
- 🔐 **身份认证** - 支持 Basic、JWT、OAuth2、API Key、Policy 等多种认证方式
- 👮 **权限控制** - RBAC 基于角色的访问控制
- 📝 **审计日志** - 记录谁在什么时候做了什么操作

### 核心特点

- 🎯 **简单易用** - 几行代码就能启用安全认证
- ⚡ **多种认证方式** - 支持 Basic、JWT、OAuth2、API Key、Policy
- 🔧 **灵活扩展** - 可以自定义认证逻辑和权限策略
- 🎨 **功能完整** - 认证、授权、审计一应俱全

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-security</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-rbac</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个安全应用

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.BasicAuthenticator;
import ltd.idcu.est.security.user.InMemoryUserStore;
import ltd.idcu.est.security.user.User;
import ltd.idcu.est.security.user.UserBuilder;

public class FirstSecurityApp {
    public static void main(String[] args) {
        System.out.println("=== EST Security Group 第一个示例 ===\n");
        
        Security security = Security.create();
        
        InMemoryUserStore userStore = new InMemoryUserStore();
        User admin = UserBuilder.create()
            .username("admin")
            .password("admin123")
            .role("ADMIN")
            .build();
        User user = UserBuilder.create()
            .username("user")
            .password("user123")
            .role("USER")
            .build();
        userStore.addUser(admin);
        userStore.addUser(user);
        
        security.setAuthenticator(new BasicAuthenticator(userStore));
        security.requireRole("/admin/**", "ADMIN");
        security.requireRole("/user/**", "USER", "ADMIN");
        
        System.out.println("安全配置已完成！");
        System.out.println("admin/admin123 可以访问 /admin 和 /user");
        System.out.println("user/user123 只能访问 /user");
    }
}
```

---

## 基础篇

### 1. est-security 身份认证

#### Basic 认证

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.BasicAuthenticator;
import ltd.idcu.est.security.user.InMemoryUserStore;

Security security = Security.create();

InMemoryUserStore userStore = new InMemoryUserStore();
userStore.addUser(UserBuilder.create()
    .username("admin")
    .password("admin123")
    .build());

security.setAuthenticator(new BasicAuthenticator(userStore));
```

#### JWT 认证

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.JwtAuthenticator;
import ltd.idcu.est.security.jwt.JwtConfig;

Security security = Security.create();

JwtConfig config = JwtConfig.builder()
    .secretKey("my-secret-key-at-least-256-bits-long")
    .expiration(3600) // 1小时
    .build();

security.setAuthenticator(new JwtAuthenticator(config));
```

#### API Key 认证

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.ApiKeyAuthenticator;
import ltd.idcu.est.security.apikey.InMemoryApiKeyStore;

Security security = Security.create();

InMemoryApiKeyStore apiKeyStore = new InMemoryApiKeyStore();
apiKeyStore.addApiKey("my-api-key-123", "service-1");

security.setAuthenticator(new ApiKeyAuthenticator(apiKeyStore));
```

#### OAuth2 认证

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.OAuth2Authenticator;
import ltd.idcu.est.security.oauth2.OAuth2Config;

Security security = Security.create();

OAuth2Config config = OAuth2Config.builder()
    .clientId("my-client-id")
    .clientSecret("my-client-secret")
    .authorizationUri("https://auth.example.com/authorize")
    .tokenUri("https://auth.example.com/token")
    .userInfoUri("https://auth.example.com/userinfo")
    .build();

security.setAuthenticator(new OAuth2Authenticator(config));
```

### 2. est-rbac 基于角色的访问控制

#### 定义角色和权限

```java
import ltd.idcu.est.rbac.Rbac;
import ltd.idcu.est.rbac.Role;
import ltd.idcu.est.rbac.Permission;

Rbac rbac = Rbac.create();

Role adminRole = Role.create("ADMIN")
    .addPermission(Permission.create("user:read"))
    .addPermission(Permission.create("user:write"))
    .addPermission(Permission.create("user:delete"))
    .addPermission(Permission.create("order:read"))
    .addPermission(Permission.create("order:write"))
    .addPermission(Permission.create("order:delete"));

Role userRole = Role.create("USER")
    .addPermission(Permission.create("user:read"))
    .addPermission(Permission.create("order:read"));

rbac.addRole(adminRole);
rbac.addRole(userRole);
```

#### 检查权限

```java
boolean canReadUser = rbac.hasPermission("ADMIN", "user:read"); // true
boolean canDeleteUser = rbac.hasPermission("USER", "user:delete"); // false

boolean canAccessAdminPath = rbac.hasRole("ADMIN", "/admin/**");
```

#### 保护路由

```java
import ltd.idcu.est.web.router.WebRouter;
import ltd.idcu.est.security.Security;
import ltd.idcu.est.rbac.Rbac;

WebRouter router = WebRouter.create();
Security security = Security.create();
Rbac rbac = Rbac.create();

router.use(security.middleware());
router.use(rbac.middleware());

router.get("/admin/users", (req, res) -> {
    res.json(userService.findAll());
}).requireRole("ADMIN");

router.get("/user/profile", (req, res) -> {
    res.json(req.user());
}).requireRole("USER", "ADMIN");
```

### 3. est-audit 审计日志

#### 记录操作

```java
import ltd.idcu.est.audit.Audit;
import ltd.idcu.est.audit.AuditLog;
import ltd.idcu.est.audit.AuditLogger;

Audit audit = Audit.create();
AuditLogger logger = audit.getLogger();

logger.log(AuditLog.builder()
    .userId("123")
    .username("admin")
    .action("LOGIN")
    .resource("auth")
    .details("用户登录系统")
    .ipAddress("192.168.1.100")
    .build());

logger.log(AuditLog.builder()
    .userId("123")
    .username("admin")
    .action("CREATE")
    .resource("user")
    .resourceId("456")
    .details("创建用户: 张三")
    .ipAddress("192.168.1.100")
    .build());
```

#### 查询审计日志

```java
import ltd.idcu.est.audit.Audit;
import ltd.idcu.est.audit.AuditLog;
import ltd.idcu.est.audit.AuditQuery;
import ltd.idcu.est.collection.api.Seq;

Audit audit = Audit.create();
AuditQuery query = audit.createQuery();

Seq<AuditLog> logs = query
    .userId("123")
    .action("CREATE")
    .startTime(LocalDateTime.now().minusDays(7))
    .endTime(LocalDateTime.now())
    .find();

logs.forEach(log -> {
    System.out.println(log.getTimestamp() + " " + log.getUsername() + " " + log.getAction());
});
```

---

## 进阶篇

### 1. 自定义用户存储

```java
import ltd.idcu.est.security.user.UserStore;
import ltd.idcu.est.security.user.User;

public class DatabaseUserStore implements UserStore {
    private final JdbcTemplate jdbcTemplate;
    
    public DatabaseUserStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE username = ?",
            new Object[]{username},
            (rs, rowNum) -> UserBuilder.create()
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .role(rs.getString("role"))
                .build()
        );
    }
    
    @Override
    public boolean validatePassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}

Security security = Security.create();
security.setAuthenticator(new BasicAuthenticator(new DatabaseUserStore(jdbcTemplate)));
```

### 2. 自定义权限策略

```java
import ltd.idcu.est.rbac.Policy;
import ltd.idcu.est.rbac.RbacContext;

public class CustomPolicy implements Policy {
    @Override
    public boolean check(RbacContext context) {
        String role = context.getRole();
        String resource = context.getResource();
        String action = context.getAction();
        
        if ("ADMIN".equals(role)) {
            return true;
        }
        
        if ("USER".equals(role)) {
            if ("user".equals(resource)) {
                return "read".equals(action);
            }
            if ("order".equals(resource)) {
                return "read".equals(action);
            }
        }
        
        return false;
    }
}

Rbac rbac = Rbac.create();
rbac.addPolicy(new CustomPolicy());
```

### 3. 审计日志存储

```java
import ltd.idcu.est.audit.AuditStore;
import ltd.idcu.est.audit.AuditLog;

public class DatabaseAuditStore implements AuditStore {
    private final JdbcTemplate jdbcTemplate;
    
    public DatabaseAuditStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void save(AuditLog log) {
        jdbcTemplate.update(
            "INSERT INTO audit_logs (user_id, username, action, resource, resource_id, details, ip_address, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            log.getUserId(),
            log.getUsername(),
            log.getAction(),
            log.getResource(),
            log.getResourceId(),
            log.getDetails(),
            log.getIpAddress(),
            log.getTimestamp()
        );
    }
    
    @Override
    public Seq<AuditLog> query(AuditQuery query) {
        // 实现查询逻辑
        return Seqs.empty();
    }
}

Audit audit = Audit.create();
audit.setStore(new DatabaseAuditStore(jdbcTemplate));
```

---

## 最佳实践

### 1. 密码安全

```java
import org.mindrot.jbcrypt.BCrypt;

// ✅ 推荐：使用 BCrypt 等强加密算法
String hashedPassword = BCrypt.hashpw("userpassword", BCrypt.gensalt());

// ❌ 不推荐：明文存储或弱加密
String badPassword = "userpassword";
```

### 2. 合理使用角色和权限

```java
// ✅ 推荐：最小权限原则
Role userRole = Role.create("USER")
    .addPermission(Permission.create("user:read"))
    .addPermission(Permission.create("order:read"));

// ❌ 不推荐：给普通用户管理员权限
Role badRole = Role.create("USER")
    .addPermission(Permission.create("*"));
```

### 3. 审计日志的重要字段

```java
// ✅ 推荐：记录完整的审计信息
logger.log(AuditLog.builder()
    .userId("123")
    .username("admin")
    .action("DELETE")
    .resource("user")
    .resourceId("456")
    .details("删除用户: 张三")
    .ipAddress("192.168.1.100")
    .userAgent("Mozilla/5.0...")
    .build());

// ❌ 不推荐：信息不全
logger.log(AuditLog.builder()
    .action("DELETE")
    .build());
```

### 4. JWT 安全配置

```java
// ✅ 推荐：使用强密钥和合理的过期时间
JwtConfig config = JwtConfig.builder()
    .secretKey("my-very-long-secret-key-at-least-256-bits-for-hs256")
    .expiration(3600) // 1小时
    .issuer("my-application")
    .audience("my-users")
    .build();

// ❌ 不推荐：弱密钥或过长的过期时间
JwtConfig badConfig = JwtConfig.builder()
    .secretKey("short")
    .expiration(86400 * 365) // 1年，太长了
    .build();
```

---

## 模块结构

```
est-security-group/
├── est-security/     # 身份认证（Basic、JWT、OAuth2、API Key、Policy）
├── est-rbac/         # 基于角色的访问控制
└── est-audit/        # 审计日志
```

---

## 相关资源

- [est-security README](./est-security/README.md) - 安全认证详细文档
- [示例代码](../../est-examples/est-examples-advanced/) - 高级示例
- [EST Web Group](../est-web-group/README.md) - Web 模块
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
