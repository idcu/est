# 代码组织最佳实践

本文档介绍如何组织你的 EST 项目代码。

## 推荐的项目结构

```
src/main/java/
└── com/yourcompany/yourapp/
    ├── Application.java              # 应用入口
    ├── config/                        # 配置类
    │   ├── AppConfig.java
    │   └── ModuleConfig.java
    ├── controller/                    # 控制器层（Web）
    │   ├── UserController.java
    │   └── ProductController.java
    ├── service/                       # 服务层（业务逻辑）
    │   ├── UserService.java
    │   └── UserServiceImpl.java
    ├── repository/                    # 数据访问层
    │   ├── UserRepository.java
    │   └── JdbcUserRepository.java
    ├── model/                         # 数据模型
    │   ├── User.java
    │   └── Product.java
    ├── dto/                           # 数据传输对象
    │   ├── CreateUserRequest.java
    │   └── UserResponse.java
    ├── middleware/                    # 中间件
    │   └── AuthMiddleware.java
    ├── exception/                     # 异常类
    │   ├── BusinessException.java
    │   └── ResourceNotFoundException.java
    ├── util/                          # 工具类
    │   └── ValidationUtils.java
    └── module/                        # 自定义模块
        └── CustomModule.java
```

## 分层架构原则

### 1. 分层说明

```
Controller 层（Web 接口）
     ↓
Service 层（业务逻辑）
     ↓
Repository 层（数据访问）
     ↓
Database / External Service
```

### 2. 各层职责

**Controller 层**
- 处理 HTTP 请求
- 参数验证
- 调用 Service 层
- 构造响应

```java
// 示例：UserController
public class UserController implements Controller {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void handle(Request request, Response response) {
        String userId = request.getPathVariable("id");
        User user = userService.getUserById(userId);
        response.json(user);
    }
}
```

**Service 层**
- 实现业务逻辑
- 事务管理
- 调用 Repository 层

```java
// 示例：UserService
public interface UserService {
    User getUserById(String id);
    User createUser(CreateUserRequest request);
    void deleteUser(String id);
}

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserServiceImpl(UserRepository userRepository, 
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        return userRepository.save(user);
    }
}
```

**Repository 层**
- 数据访问
- 查询构建
- 结果映射

```java
// 示例：UserRepository
public interface UserRepository {
    User findById(String id);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    void deleteById(String id);
}
```

## 接口优先原则

始终先定义接口，再提供实现：

```java
// 好的做法
public interface UserService { ... }
public class UserServiceImpl implements UserService { ... }

// 在容器中注册
container.register(UserService.class, UserServiceImpl.class);
```

这样做的好处：
- 便于 Mock 测试
- 支持多种实现
- 降低耦合

## 依赖注入最佳实践

### 构造函数注入（推荐）

```java
public class UserService {
    private final UserRepository userRepository;
    private final Logger logger;
    
    public UserService(UserRepository userRepository, Logger logger) {
        this.userRepository = userRepository;
        this.logger = logger;
    }
}
```

优点：
- 依赖不可变（final）
- 易于测试
- 依赖关系清晰

### 避免循环依赖

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

// 好的做法 - 引入中间层或事件
class A {
    private EventBus eventBus;
    public A(EventBus eventBus) { this.eventBus = eventBus; }
}
class B {
    private EventBus eventBus;
    public B(EventBus eventBus) { this.eventBus = eventBus; }
}
```

## 模块化

按功能划分模块，而不是按层次：

```
// 好的做法
com.yourapp.user/
    ├── UserController.java
    ├── UserService.java
    ├── UserRepository.java
    └── User.java

com.yourapp.product/
    ├── ProductController.java
    ├── ProductService.java
    ├── ProductRepository.java
    └── Product.java
```

## 命名规范

| 类型 | 命名规范 | 示例 |
|------|----------|------|
| 接口 | 名词 | `UserService` |
| 实现 | 接口名 + Impl | `UserServiceImpl` |
| 控制器 | 功能 + Controller | `UserController` |
| 服务 | 功能 + Service | `UserService` |
| 仓库 | 功能 + Repository | `UserRepository` |
| DTO | 动作 + 资源 + Request/Response | `CreateUserRequest` |
| 异常 | 描述 + Exception | `UserNotFoundException` |

## 总结

- 使用分层架构组织代码
- 接口优先，实现分离
- 使用构造函数注入
- 避免循环依赖
- 按功能划分模块
- 遵循统一的命名规范
