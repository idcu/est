# 教程 02: 依赖注入容器

在本教程中，你将学习如何使用 EST 的依赖注入容器来管理你的组件！

---

## 🎯 学习目标

完成本教程后，你将学会：
- ✅ 什么是依赖注入（DI）
- ✅ 如何创建和使用容器
- ✅ 如何注册和获取服务
- ✅ 单例和非单例的区别
- ✅ 如何在实际项目中使用 DI

---

## 🤔 什么是依赖注入？

依赖注入（Dependency Injection，简称 DI）是一种设计模式，它的核心思想是：**不要自己创建依赖，让容器帮你管理！**

### 传统方式 vs DI 方式

**传统方式（不好）：**
```java
public class UserService {
    // 自己创建依赖
    private UserRepository repository = new UserRepository();
    private Logger logger = new Logger();
    
    public User getUser(String id) {
        logger.info("获取用户：" + id);
        return repository.findById(id);
    }
}
```

问题：
- ❌ 每次都要手动创建依赖
- ❌ 难以测试（无法替换为模拟对象）
- ❌ 代码耦合度高

**DI 方式（好）：**
```java
public class UserService {
    // 通过构造函数接收依赖
    private final UserRepository repository;
    private final Logger logger;
    
    public UserService(UserRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }
    
    public User getUser(String id) {
        logger.info("获取用户：" + id);
        return repository.findById(id);
    }
}
```

好处：
- ✅ 依赖从外部传入，更灵活
- ✅ 容易测试（可以传入模拟对象）
- ✅ 代码耦合度低

---

## 📦 创建第一个容器

让我们创建一个简单的依赖注入示例！

### 1. 定义服务接口和实现

首先，让我们定义一些服务：

```java
package com.example.di;

// 1. 用户服务接口
interface UserService {
    String getUserName(String id);
}

// 2. 用户服务实现
class UserServiceImpl implements UserService {
    @Override
    public String getUserName(String id) {
        return "用户" + id;
    }
}

// 3. 邮件服务接口
interface EmailService {
    void sendEmail(String to, String message);
}

// 4. 邮件服务实现
class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String message) {
        System.out.println("📧 发送邮件到：" + to);
        System.out.println("   内容：" + message);
    }
}
```

### 2. 使用容器管理服务

现在让我们创建一个容器，并用它来管理这些服务：

```java
package com.example.di;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class DiExample {
    public static void main(String[] args) {
        // ==========================================
        // 第一步：创建容器
        // ==========================================
        
        Container container = new DefaultContainer();
        
        // ==========================================
        // 第二步：注册服务
        // ==========================================
        
        // 注册用户服务（接口 -> 实现）
        container.register(UserService.class, UserServiceImpl.class);
        
        // 注册邮件服务
        container.register(EmailService.class, EmailServiceImpl.class);
        
        // ==========================================
        // 第三步：获取并使用服务
        // ==========================================
        
        // 获取用户服务
        UserService userService = container.get(UserService.class);
        String userName = userService.getUserName("123");
        System.out.println("👤 " + userName);
        
        // 获取邮件服务
        EmailService emailService = container.get(EmailService.class);
        emailService.sendEmail(userName + "@example.com", "欢迎使用！");
    }
}
```

### 3. 运行示例

运行这个程序，你会看到：

```
👤 用户123
📧 发送邮件到：用户123@example.com
   内容：欢迎使用！
```

---

## 🔄 单例 vs 非单例

容器支持两种注册方式：

### 1. 非单例（每次获取都创建新实例）

```java
// 注册为非单例
container.register(UserService.class, UserServiceImpl.class);

// 每次获取都是新实例
UserService service1 = container.get(UserService.class);
UserService service2 = container.get(UserService.class);

// service1 != service2（两个不同的对象）
System.out.println(service1 == service2); // 输出：false
```

### 2. 单例（每次获取都返回同一个实例）

```java
// 注册为单例
UserService singletonService = new UserServiceImpl();
container.registerSingleton(UserService.class, singletonService);

// 或者让容器创建单例
container.registerSingleton(UserService.class, UserServiceImpl.class);

// 每次获取都是同一个实例
UserService service1 = container.get(UserService.class);
UserService service2 = container.get(UserService.class);

// service1 == service2（同一个对象）
System.out.println(service1 == service2); // 输出：true
```

### 什么时候用单例？

✅ **用单例的情况：**
- 配置对象（只需要一个）
- 日志器（只需要一个）
- 数据库连接池（只需要一个）
- 缓存管理器（只需要一个）

❌ **不用单例的情况：**
- 用户会话（每个用户不同）
- 请求处理对象（每次请求不同）
- 临时数据对象（每次使用不同）

---

## 🏗️ 完整示例：分层架构

让我们看一个更完整的例子，模拟一个实际的 Web 应用架构！

### 1. 定义数据模型

```java
package com.example.di;

// 用户模型
class User {
    private String id;
    private String name;
    private String email;
    
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}
```

### 2. 定义 Repository（数据访问层）

```java
package com.example.di;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 用户仓库接口
interface UserRepository {
    User findById(String id);
    void save(User user);
    void delete(String id);
    boolean exists(String id);
}

// 内存用户仓库实现
class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    
    @Override
    public User findById(String id) {
        return users.get(id);
    }
    
    @Override
    public void save(User user) {
        users.put(user.getId(), user);
        System.out.println("💾 保存用户：" + user);
    }
    
    @Override
    public void delete(String id) {
        users.remove(id);
        System.out.println("🗑️ 删除用户：" + id);
    }
    
    @Override
    public boolean exists(String id) {
        return users.containsKey(id);
    }
}
```

### 3. 定义 Service（业务逻辑层）

```java
package com.example.di;

// 用户服务接口
interface UserService {
    User getUser(String id);
    void createUser(String id, String name, String email);
    void deleteUser(String id);
}

// 用户服务实现
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final EmailService emailService;
    
    // 通过构造函数注入依赖
    public UserServiceImpl(UserRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }
    
    @Override
    public User getUser(String id) {
        System.out.println("🔍 查找用户：" + id);
        return repository.findById(id);
    }
    
    @Override
    public void createUser(String id, String name, String email) {
        if (repository.exists(id)) {
            throw new RuntimeException("用户已存在：" + id);
        }
        
        User user = new User(id, name, email);
        repository.save(user);
        
        // 发送欢迎邮件
        emailService.sendEmail(email, "欢迎 " + name + "！", "您的账户已创建成功。");
    }
    
    @Override
    public void deleteUser(String id) {
        if (!repository.exists(id)) {
            throw new RuntimeException("用户不存在：" + id);
        }
        repository.delete(id);
    }
}
```

### 4. 组装并运行

```java
package com.example.di;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class CompleteDiExample {
    public static void main(String[] args) {
        System.out.println("🚀 开始依赖注入示例...\n");
        
        // ==========================================
        // 1. 创建容器
        // ==========================================
        Container container = new DefaultContainer();
        
        // ==========================================
        // 2. 注册服务（从底层到上层）
        // ==========================================
        
        // Repository 层（单例，因为数据需要共享）
        container.registerSingleton(UserRepository.class, InMemoryUserRepository.class);
        
        // Email 服务（单例）
        container.registerSingleton(EmailService.class, EmailServiceImpl.class);
        
        // Service 层（可以是单例，也可以不是）
        container.register(UserService.class, UserServiceImpl.class);
        
        // ==========================================
        // 3. 使用服务
        // ==========================================
        
        UserService userService = container.get(UserService.class);
        
        System.out.println("--- 创建用户 ---");
        userService.createUser("001", "张三", "zhangsan@example.com");
        userService.createUser("002", "李四", "lisi@example.com");
        
        System.out.println("\n--- 查询用户 ---");
        User user1 = userService.getUser("001");
        System.out.println("查到用户：" + user1);
        
        System.out.println("\n--- 删除用户 ---");
        userService.deleteUser("002");
        
        System.out.println("\n✅ 示例完成！");
    }
}
```

### 5. 运行结果

```
🚀 开始依赖注入示例...

--- 创建用户 ---
💾 保存用户：User{id='001', name='张三', email='zhangsan@example.com'}
📧 发送邮件到：zhangsan@example.com
   内容：欢迎 张三！
💾 保存用户：User{id='002', name='李四', email='lisi@example.com'}
📧 发送邮件到：lisi@example.com
   内容：欢迎 李四！

--- 查询用户 ---
🔍 查找用户：001
查到用户：User{id='001', name='张三', email='zhangsan@example.com'}

--- 删除用户 ---
🗑️ 删除用户：002

✅ 示例完成！
```

---

## 🔍 其他有用的功能

### 1. 检查服务是否已注册

```java
if (container.contains(UserService.class)) {
    System.out.println("用户服务已注册");
} else {
    System.out.println("用户服务未注册");
}
```

### 2. 安全获取服务（避免异常）

```java
// 使用 Optional 安全获取
java.util.Optional<UserService> serviceOpt = container.getIfPresent(UserService.class);

serviceOpt.ifPresent(service -> {
    // 服务存在，使用它
    System.out.println("服务可用：" + service);
});

// 或者提供默认值
UserService service = serviceOpt.orElseGet(() -> new UserServiceImpl());
```

### 3. 移除服务

```java
// 从容器中移除服务
container.unregister(UserService.class);
```

---

## ✅ 最佳实践

### 1. 优先使用构造函数注入

```java
// ✅ 好：构造函数注入
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

// ❌ 不好：字段注入（无法保证不变性）
public class UserService {
    private UserRepository repository; // 可能被修改
}
```

### 2. 接口优先

```java
// ✅ 好：面向接口编程
container.register(UserService.class, UserServiceImpl.class);
UserService service = container.get(UserService.class);

// ❌ 不好：直接使用实现
container.register(UserServiceImpl.class, UserServiceImpl.class);
UserServiceImpl service = container.get(UserServiceImpl.class);
```

### 3. 避免循环依赖

```java
// ❌ 不好：循环依赖
class A {
    public A(B b) { } // A 依赖 B
}
class B {
    public B(A a) { } // B 依赖 A
}

// ✅ 好：使用事件解耦
class A {
    public A(EventBus eventBus) {
        eventBus.publish(new AEvent());
    }
}
class B {
    public B(EventBus eventBus) {
        eventBus.subscribe(AEvent.class, this::handle);
    }
}
```

### 4. 使用单例管理共享资源

```java
// ✅ 好：数据库连接池、缓存等用单例
container.registerSingleton(DatabasePool.class, DatabasePool.class);
container.registerSingleton(CacheManager.class, CacheManager.class);
container.registerSingleton(Logger.class, Logger.class);

// ❌ 不好：每次创建新的连接池
container.register(DatabasePool.class, DatabasePool.class);
```

---

## 💡 小练习

试试完成以下练习：

1. **添加新服务**：创建一个 `ProductService` 和 `ProductRepository`，并注册到容器
2. **使用单例**：将某个服务改为单例，验证每次获取都是同一个实例
3. **分层架构**：创建一个完整的三层架构（Repository → Service → Controller）
4. **模拟测试**：创建一个 `MockUserRepository` 用于测试

---

## 🎓 下一步

恭喜你！你已经学会了使用依赖注入容器！

在下一个教程中，我们将学习 [配置管理](./03-configuration.md)。

---

**做得好！继续加油！** 🎉
