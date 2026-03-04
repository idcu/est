# Security 安全认证 API

安全认证模块提供多种认证方式和授权机制，支持Basic、JWT、API Key、OAuth2等。

## 认证架构

```
Authentication → Authorization → Policy
```

## 用户与角色

```java
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.api.Role;
import ltd.idcu.est.features.security.api.Permission;

// 创建用户
User user = User.builder()
    .id("1")
    .username("admin")
    .password(BCrypt.hash("password"))
    .email("admin@example.com")
    .roles(List.of("ADMIN", "USER"))
    .permissions(List.of("user:create", "user:delete"))
    .build();

// 创建角色
Role adminRole = Role.builder()
    .name("ADMIN")
    .permissions(List.of("*"))
    .build();
```

## 基础认证 (BasicAuthentication)

```java
import ltd.idcu.est.features.security.basic.BasicSecurity;

// 创建基础认证
BasicSecurity security = BasicSecurity.create();

// 添加用户
security.addUser(user);

// 认证
Optional<User> authenticated = security.authenticate("admin", "password");

// 验证密码
boolean valid = BCrypt.verify("password", hashedPassword);
```

## JWT 认证 (JwtAuthentication)

```java
import ltd.idcu.est.features.security.jwt.JwtSecurity;
import ltd.idcu.est.features.security.jwt.JwtConfig;

// 配置JWT
JwtConfig config = JwtConfig.builder()
    .secretKey("your-secret-key-at-least-256-bits")
    .expirationMinutes(60)
    .issuer("est-framework")
    .build();

// 创建JWT认证
JwtSecurity jwtSecurity = JwtSecurity.create(config);

// 生成Token
String token = jwtSecurity.generateToken(user);

// 验证Token
Optional<User> verified = jwtSecurity.verifyToken(token);

// 刷新Token
String refreshed = jwtSecurity.refreshToken(token);
```

## API Key 认证 (ApiKeyAuthentication)

```java
import ltd.idcu.est.features.security.apikey.ApiKeySecurity;

// 创建API Key认证
ApiKeySecurity apiKeySecurity = ApiKeySecurity.create();

// 生成API Key
String apiKey = apiKeySecurity.generateKey("my-service");

// 验证API Key
Optional<String> service = apiKeySecurity.verifyKey(apiKey);
```

## OAuth2 认证 (OAuth2Authentication)

```java
import ltd.idcu.est.features.security.oauth2.OAuth2Security;
import ltd.idcu.est.features.security.oauth2.OAuth2Config;

// 配置OAuth2
OAuth2Config config = OAuth2Config.builder()
    .clientId("client-id")
    .clientSecret("client-secret")
    .redirectUri("http://localhost:8080/callback")
    .authorizationUri("https://provider.com/oauth/authorize")
    .tokenUri("https://provider.com/oauth/token")
    .userInfoUri("https://provider.com/api/user")
    .build();

// 创建OAuth2认证
OAuth2Security oauth2 = OAuth2Security.create(config);

// 获取授权URL
String authUrl = oauth2.getAuthorizationUrl();

// 交换Token
OAuth2Token token = oauth2.exchangeCode(code);

// 获取用户信息
User user = oauth2.getUserInfo(token);
```

## 策略引擎 (PolicyEngine)

基于属性的访问控制（ABAC）。

```java
import ltd.idcu.est.features.security.policy.PolicyEngine;
import ltd.idcu.est.features.security.policy.Policy;

// 创建策略引擎
PolicyEngine policyEngine = PolicyEngine.create();

// 定义策略
Policy policy = Policy.builder()
    .name("allow-admin-edit")
    .effect(Effect.ALLOW)
    .subject("role", "ADMIN")
    .action("edit")
    .resource("type", "document")
    .build();

// 添加策略
policyEngine.addPolicy(policy);

// 评估访问
boolean allowed = policyEngine.evaluate(
    subject,    // 用户
    "edit",     // 动作
    resource    // 资源
);
```

## Web 集成

```java
import ltd.idcu.est.web.WebApplication;
import ltd.idcu.est.features.security.web.SecurityMiddleware;

// 添加安全中间件
app.middleware(new SecurityMiddleware(security));

// 路由保护
app.get("/admin", (req, res) -> {
    User user = req.getAttribute("user");
    res.send("Welcome, " + user.getUsername());
}).middleware("auth");
```
