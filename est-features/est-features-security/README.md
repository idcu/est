# EST Security - 安全系统

## 📚 目录

- [快速入门](#快速入门)
- [基础篇](#基础篇)
- [进阶篇](#进阶篇)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是安全系统？

想象一下，你在管理一个高档小区：
- 门口有保安，检查进出人员的身份（认证）
- 不同的人有不同的权限，比如业主可以进所有楼，访客只能进指定楼（授权）
- 进出都有记录，方便追查（审计）

**安全系统**就是程序的"保安系统"，它提供：
- 用户认证（验证你是谁）
- 权限管理（你能做什么）
- 多种认证方式（Basic、JWT、OAuth2、API Key）

保护你的程序安全！

### 第一个例子

让我们用 3 分钟写一个简单的安全程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-security-api</artifactId>
    <version>1.3.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-security-basic</artifactId>
    <version>1.3.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.basic.BasicSecurity;
import ltd.idcu.est.features.security.basic.DefaultUser;
import ltd.idcu.est.features.security.basic.SecurityFactory;

public class SecurityFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 安全系统示例 ===\n");
        
        // 创建安全上下文
        BasicSecurity security = SecurityFactory.createBasicSecurity();
        
        // 添加用户
        User user = new DefaultUser("1", "张三", "zhangsan", "password123");
        security.getUserDetailsService().addUser(user);
        
        // 尝试认证
        try {
            Authentication auth = security.authenticate("zhangsan", "password123");
            System.out.println("认证成功！用户: " + auth.getName());
            System.out.println("是否已认证: " + auth.isAuthenticated());
        } catch (Exception e) {
            System.out.println("认证失败: " + e.getMessage());
        }
        
        System.out.println("\n✅ 安全示例完成！");
    }
}
```

运行这个程序，你会看到用户认证成功！

🎉 恭喜你！你已经学会了使用安全系统！

---

## 📖 基础篇

### 1. 核心概念

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **认证（Authentication）** | 验证用户身份 | 检查身份证 |
| **授权（Authorization）** | 验证用户权限 | 检查门禁卡 |
| **用户（User）** | 使用系统的人 | 小区居民 |
| **角色（Role）** | 用户的身份 | 业主、访客、保安 |
| **权限（Permission）** | 能做的操作 | 进大门、进车库 |

### 2. 基础认证

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.basic.BasicSecurity;
import ltd.idcu.est.features.security.basic.DefaultPermission;
import ltd.idcu.est.features.security.basic.DefaultRole;
import ltd.idcu.est.features.security.basic.DefaultUser;
import ltd.idcu.est.features.security.basic.SecurityFactory;

public class BasicAuthExample {
    public static void main(String[] args) {
        BasicSecurity security = SecurityFactory.createBasicSecurity();
        
        // 创建权限
        DefaultPermission readPerm = new DefaultPermission("read", "读取权限");
        DefaultPermission writePerm = new DefaultPermission("write", "写入权限");
        
        // 创建角色
        DefaultRole userRole = new DefaultRole("user", "普通用户");
        userRole.addPermission(readPerm);
        
        DefaultRole adminRole = new DefaultRole("admin", "管理员");
        adminRole.addPermission(readPerm);
        adminRole.addPermission(writePerm);
        
        // 创建用户
        User user = new DefaultUser("1", "张三", "zhangsan", "password123");
        ((DefaultUser) user).addRole(userRole);
        
        User admin = new DefaultUser("2", "李四", "admin", "admin123");
        ((DefaultUser) admin).addRole(adminRole);
        
        // 添加用户
        security.getUserDetailsService().addUser(user);
        security.getUserDetailsService().addUser(admin);
        
        // 认证和授权检查
        try {
            Authentication auth = security.authenticate("zhangsan", "password123");
            System.out.println("张三认证成功");
            System.out.println("有读取权限: " + security.getAuthorization().hasPermission(auth, "read"));
            System.out.println("有写入权限: " + security.getAuthorization().hasPermission(auth, "write"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 3. JWT 认证

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.Token;
import ltd.idcu.est.features.security.jwt.JwtSecurity;
import ltd.idcu.est.features.security.jwt.JwtSecurityContext;

public class JwtAuthExample {
    public static void main(String[] args) {
        JwtSecurity security = JwtSecurityContext.create();
        
        // 生成 JWT Token
        Token token = security.getTokenProvider().generateToken("user123");
        System.out.println("生成的 Token: " + token.getValue());
        
        // 验证 Token
        try {
            Authentication auth = security.authenticate(token.getValue());
            System.out.println("Token 验证成功，用户: " + auth.getName());
        } catch (Exception e) {
            System.out.println("Token 验证失败: " + e.getMessage());
        }
    }
}
```

---

## 🔧 进阶篇

### 1. API Key 认证

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.apikey.ApiKeySecurity;
import ltd.idcu.est.features.security.apikey.DefaultApiKeyAuthentication;

public class ApiKeyAuthExample {
    public static void main(String[] args) {
        ApiKeySecurity security = new ApiKeySecurity();
        
        // 添加 API Key
        String apiKey = "my-secret-api-key-12345";
        security.addApiKey(apiKey, "service-a");
        
        // 验证 API Key
        try {
            Authentication auth = security.authenticate(apiKey);
            System.out.println("API Key 验证成功: " + auth.getName());
        } catch (Exception e) {
            System.out.println("API Key 验证失败: " + e.getMessage());
        }
    }
}
```

### 2. 与 EST Collection 集成

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.basic.DefaultUser;
import ltd.idcu.est.features.security.basic.SecurityFactory;

import java.util.List;

public class SecurityCollectionIntegrationExample {
    public static void main(String[] args) {
        var security = SecurityFactory.createBasicSecurity();
        
        List<User> users = List.of(
                new DefaultUser("1", "张三", "zhangsan", "pass1"),
                new DefaultUser("2", "李四", "lisi", "pass2"),
                new DefaultUser("3", "王五", "wangwu", "pass3")
        );
        
        // 使用 Collection 批量添加用户
        Seqs.of(users)
                .forEach(user -> security.getUserDetailsService().addUser(user));
        
        System.out.println("已添加用户数: " + users.size());
    }
}
```

---

## 💡 最佳实践

### 1. 密码加密

```java
import ltd.idcu.est.features.security.api.PasswordEncoder;
import ltd.idcu.est.features.security.basic.BCryptPasswordEncoder;

public class PasswordEncodingExample {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 加密密码
        String rawPassword = "mysecretpassword";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后: " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("密码匹配: " + matches);
    }
}
```

---

## 🎯 总结

安全系统就像程序的"保安系统"，保护你的程序免受未授权访问！

下一章，我们将学习 EST Messaging 消息系统！🎉
