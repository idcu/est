# 教程 02: 依赖注入容器

在本教程中，你将学习如何使用 EST 的依赖注入容器。

## 什么是依赖注入？

依赖注入（Dependency Injection，简称 DI）是一种设计模式，用于实现控制反转（IoC）。它的核心思想是：**不要自己创建依赖，让容器帮你管理。**

## 创建服务接口和实现

首先，让我们定义一些服务：

```java
package com.example.di;

// 服务接口
public interface UserService {
    User getUserById(String id);
    void createUser(User user);
}

// 服务实现
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Logger logger;
    
    // 通过构造函数注入依赖
    public UserServiceImpl(UserRepository userRepository, Logger logger) {
        this.userRepository = userRepository;
        this.logger = logger;
    }
    
    @Override
    public User getUserById(String id) {
        logger.info("Getting user by id: " + id);
        return userRepository.findById(id);
    }
    
    @Override
    public void createUser(User user) {
        logger.info("Creating user: " + user.getName());
        userRepository.save(user);
    }
}

// 仓库接口
public interface UserRepository {
    User findById(String id);
    void save(User user);
}

// 仓库实现
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();
    
    @Override
    public User findById(String id) {
        return users.get(id);
    }
    
    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}

// 用户模型
public class User {
    private String id;
    private String name;
    private String email;
    
    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

## 注册服务到容器

现在让我们使用容器来管理这些服务：

```java
package com.example.di;

import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

public class DIContainerExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Container container = app.getContainer();
        
        // 1. 注册单例实例
        Logger logger = ConsoleLogs.create(DIContainerExample.class);
        container.registerSingleton(Logger.class, logger);
        
        // 2. 注册接口与实现
        container.register(UserRepository.class, InMemoryUserRepository.class);
        container.register(UserService.class, UserServiceImpl.class);
        
        // 3. 获取服务
        UserService userService = container.get(UserService.class);
        
        // 使用服务
        User user = new User();
        user.setId("1");
        user.setName("Alice");
        user.setEmail("alice@example.com");
        
        userService.createUser(user);
        
        User retrieved = userService.getUserById("1");
        System.out.println("Retrieved user: " + retrieved.getName());
    }
}
```

## 注册方式详解

### 1. register - 注册类型与实现

```java
// 简单注册
container.register(Service.class, ServiceImpl.class);

// 每次获取都会创建新实例
Service instance1 = container.get(Service.class);
Service instance2 = container.get(Service.class);
// instance1 != instance2
```

### 2. registerSingleton - 注册单例

```java
// 注册已创建的实例
Service service = new ServiceImpl();
container.registerSingleton(Service.class, service);

// 每次获取都返回同一实例
Service instance1 = container.get(Service.class);
Service instance2 = container.get(Service.class);
// instance1 == instance2
```

### 3. registerSupplier - 注册供应者

```java
// 使用供应者延迟创建
container.registerSupplier(Service.class, () -> {
    Service service = new ServiceImpl();
    service.init(); // 自定义初始化
    return service;
});
```

## 循环依赖处理

避免循环依赖是良好的设计实践。如果遇到循环依赖，考虑：

```java
// 不好的做法 - 循环依赖
class A {
    private B b;
    public A(B b) { this.b = b; }
}
class B {
    private A a;
    public B(A a) { this.a = a; }
}

// 好的做法 - 使用事件总线解耦
class A {
    private EventBus eventBus;
    public A(EventBus eventBus) { this.eventBus = eventBus; }
    
    public void doSomething() {
        eventBus.publish(new AEvent());
    }
}
class B {
    public B(EventBus eventBus) {
        eventBus.subscribe(AEvent.class, event -> handleAEvent());
    }
    
    private void handleAEvent() {
        // 处理 A 的事件
    }
}
```

## 使用 getIfPresent 安全获取

```java
Optional<Service> serviceOpt = container.getIfPresent(Service.class);
serviceOpt.ifPresent(service -> {
    // 服务存在，使用它
    service.doSomething();
});

// 或提供默认值
Service service = serviceOpt.orElse(new DefaultService());
```

## 检查服务是否注册

```java
if (container.contains(Service.class)) {
    Service service = container.get(Service.class);
    // 使用服务
} else {
    // 服务未注册，处理这种情况
}
```

## 实际示例：分层架构

让我们看一个完整的分层架构示例：

```java
package com.example.di;

public class CompleteDIExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Container container = app.getContainer();
        
        // 从底层开始注册
        Logger logger = ConsoleLogs.create(CompleteDILayeredExample.class);
        container.registerSingleton(Logger.class, logger);
        
        // Repository 层
        container.register(UserRepository.class, InMemoryUserRepository.class);
        container.register(ProductRepository.class, InMemoryProductRepository.class);
        
        // Service 层
        container.register(UserService.class, UserServiceImpl.class);
        container.register(ProductService.class, ProductServiceImpl.class);
        
        // 获取并使用
        UserService userService = container.get(UserService.class);
        ProductService productService = container.get(ProductService.class);
        
        // 创建用户
        User user = new User();
        user.setId("1");
        user.setName("Bob");
        userService.createUser(user);
        
        // 创建产品
        Product product = new Product();
        product.setId("100");
        product.setName("Laptop");
        productService.createProduct(product);
        
        System.out.println("Application initialized with DI!");
    }
}
```

## 最佳实践

1. **优先使用构造函数注入**
   - 依赖关系清晰
   - 易于测试
   - 依赖不可变

2. **接口优先**
   - 定义接口，再提供实现
   - 便于 Mock 测试
   - 支持多种实现

3. **避免循环依赖**
   - 使用事件总线解耦
   - 引入中间层
   - 重新设计模块边界

4. **使用单例管理共享资源**
   - 数据库连接池
   - 缓存实例
   - 日志器

## 下一步

在下一个教程中，我们将学习 [配置管理](./03-configuration.md)。
