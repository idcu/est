# 代码组织最佳实践

本文档介绍如何组织你的 EST 项目代码，让代码就像整理你的书架一样，要有条理才好找！

## 为什么要好好组织代码？

想象一下你的房间：
- **乱糟糟的房间**：找东西要花半天，东西容易丢
- **整洁的房间**：东西都在固定位置，找起来很快

代码也是一样！好的代码组织可以：
- 让你更容易找到代码
- 让团队协作更顺畅
- 让代码更容易维护
- 让代码更容易测试

## 推荐的项目结构

这是一个标准的 EST 项目结构，就像一个文件柜，每层都有不同的文件夹：

```
src/main/java/
└── com/yourcompany/yourapp/          # 你的公司/项目名
    ├── Application.java              # 🚪 应用入口（大门）
    ├── config/                        # ⚙️ 配置类（设置）
    │   ├── AppConfig.java
    │   └── ModuleConfig.java
    ├── controller/                    # 🎮 控制器层（接收请求）
    │   ├── UserController.java
    │   └── ProductController.java
    ├── service/                       # 💼 服务层（业务逻辑）
    │   ├── UserService.java          # 接口（定义做什么）
    │   └── UserServiceImpl.java    # 实现（具体怎么做）
    ├── repository/                    # 📦 数据访问层（和数据库打交道）
    │   ├── UserRepository.java      # 接口
    │   └── JdbcUserRepository.java  # 实现
    ├── model/                         # 📋 数据模型（数据长什么样）
    │   ├── User.java
    │   └── Product.java
    ├── dto/                           # 📨 数据传输对象（前后端传数据）
    │   ├── CreateUserRequest.java
    │   └── UserResponse.java
    ├── middleware/                    # 🛡️ 中间件（门卫）
    │   └── AuthMiddleware.java
    ├── exception/                     # ⚠️ 异常类（出错时的处理）
    │   ├── BusinessException.java
    │   └── ResourceNotFoundException.java
    ├── util/                          # 🔧 工具类（通用工具）
    │   └── ValidationUtils.java
    └── module/                        # 🧩 自定义模块（扩展功能）
        └── CustomModule.java
```

## 分层架构（像工厂流水线！

代码就像工厂流水线，每个车间负责不同的工序：

```
Controller 层（接待员）→ 接收客户需求
     ↓
Service 层（车间主任）→ 安排生产
     ↓
Repository 层（仓库管理员）→ 存取货物
     ↓
Database / External Service（仓库）
```

### 各层职责说明

#### 1. **Controller 层（接待员）**

负责接待用户（HTTP 请求）：
- 接收请求
- 检查参数
- 告诉 Service 层去处理
- 把结果返回给用户

```java
import ltd.idcu.est.web.api.RestController;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class UserController implements RestController {
    
    private final UserService userService;
    private final Request request;
    private final Response response;
    
    public UserController(UserService userService, Request request, Response response) {
        this.userService = userService;
        this.request = request;
        this.response = response;
    }
    
    @Override
    public Request getRequest() {
        return request;
    }
    
    @Override
    public Response getResponse() {
        return response;
    }
    
    // 获取用户详情
    public String getById() {
        String userId = getPathVariable("id");
        User user = userService.getUserById(userId);
        
        if (user != null) {
            return success(user);
        } else {
            return notFound();
        }
    }
    
    // 创建用户
    public String create() {
        Map<String, Object> body = request.getJsonBody();
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setName((String) body.get("name"));
        createRequest.setEmail((String) body.get("email"));
        createRequest.setPassword((String) body.get("password"));
        
        try {
            User user = userService.createUser(createRequest);
            return created().json(user);
        } catch (BusinessException e) {
            return badRequest().json(Map.of("error", e.getMessage()));
        }
    }
}
```

#### 2. **Service 层（车间主任）

负责业务逻辑（核心业务）：
- 实现具体的业务规则
- 调用 Repository 层存取数据
- 处理事务
- 验证业务规则

```java
// 先定义接口（告诉大家要做什么）
public interface UserService {
    User getUserById(String id);
    User createUser(CreateUserRequest request);
    void deleteUser(String id);
    List<User> getAllUsers();
}

// 然后实现（具体怎么做）
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // 构造函数注入依赖（通过构造函数把需要的东西传进来）
    public UserServiceImpl(UserRepository userRepository, 
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public User createUser(CreateUserRequest request) {
        // 1. 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已经被使用了");
        }
        
        // 2. 创建用户对象
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // 3. 密码加密（安全第一！
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // 4. 保存到数据库
        return userRepository.save(user);
    }
    
    @Override
    public User getUserById(String id) {
        return userRepository.findById(id);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
```

#### 3. **Repository 层（仓库管理员）

负责和数据库打交道：
- 保存数据
- 查询数据
- 删除数据
- 更新数据

```java
// 先定义接口
public interface UserRepository {
    User findById(String id);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    void deleteById(String id);
    List<User> findAll();
    long count();
}

// 然后实现（比如用 JDBC）
public class JdbcUserRepository implements UserRepository {
    
    private final DataSource dataSource;
    
    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public User findById(String id) {
        // 用 JDBC 查询数据库
        String sql = "SELECT * FROM users WHERE id = ?";
        // ... 具体实现 ...
        return user;
    }
    
    @Override
    public User save(User user) {
        // 保存到数据库
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        // ... 具体实现 ...
        return user;
    }
    
    // ... 其他方法 ...
}
```

#### 4. **Model 层（数据模型）

定义数据长什么样：

```java
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter 和 Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```

#### 5. **DTO 层（数据传输对象）

定义前后端传数据用的对象：

```java
// 创建用户的请求
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    
    // Getter 和 Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

// 返回给前端的用户响应
public class UserResponse {
    private String id;
    private String name;
    private String email;
    
    // 不返回密码！安全第一！
    
    // Getter 和 Setter
    // ...
}
```

## 接口优先原则

什么是接口优先？

就像装修房子先画图纸（接口），再施工（实现）：

```java
// 好的做法：先定义接口
public interface UserService {
    User getUserById(String id);
    User createUser(CreateUserRequest request);
}

// 再提供实现
public class UserServiceImpl implements UserService {
    // 具体实现
}

// 在容器中注册
container.register(UserService.class, UserServiceImpl.class);
```

这样做的好处：
- 🧪 便于测试（可以轻松模拟）
- 🔄 支持多种实现（比如内存、数据库都可以）
- 🎯 降低耦合（改实现不影响接口）

## 依赖注入最佳实践

### 构造函数注入（推荐！）

就像你需要什么东西，通过构造函数传进来：

```java
public class UserService {
    // 用 final 标记，表示不可变
    private final UserRepository userRepository;
    private final Logger logger;
    
    // 通过构造函数注入
    public UserService(UserRepository userRepository, Logger logger) {
        this.userRepository = userRepository;
        this.logger = logger;
    }
    
    // 使用
    public User getUserById(String id) {
        logger.info("查询用户: " + id);
        return userRepository.findById(id);
    }
}
```

优点：
- ✅ 依赖不可变（final）
- ✅ 易于测试（可以传 Mock 对象
- ✅ 依赖关系一目了然

### 避免循环依赖

就像 A 等 B，B 等 A，谁都动不了！

```java
// ❌ 不好的做法 - 循环依赖
class A {
    private B b;
    public A(B b) { this.b = b; }
}
class B {
    private A a;
    public B(A a) { this.a = a; }
}

// ✅ 好的做法 - 用事件解耦
class A {
    private EventBus eventBus;
    public A(EventBus eventBus) { this.eventBus = eventBus; }
    
    public void doSomething() {
        eventBus.publish(new SomethingHappenedEvent());
    }
}
class B {
    private EventBus eventBus;
    public B(EventBus eventBus) { 
        this.eventBus = eventBus;
        eventBus.subscribe(SomethingHappenedEvent.class, event -> {
            // 处理事件
        });
    }
}
```

## 模块化（按功能划分，不是按层次）

就像图书馆按书的类型分架，不是按书的大小分：

```
// ✅ 好的做法：按功能分
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

就像给文件起名字，要有规律：

| 类型 | 命名规范 | 示例 |
|------|----------|------|
| 接口 | 名词 | `UserService` |
| 实现 | 接口名 + Impl | `UserServiceImpl` |
| 控制器 | 功能 + Controller | `UserController` |
| 服务 | 功能 + Service | `UserService` |
| 仓库 | 功能 + Repository | `UserRepository` |
| DTO | 动作 + 资源 + Request/Response | `CreateUserRequest` |
| 异常 | 描述 + Exception | `UserNotFoundException` |

## 使用 EST 框架工具类

EST 框架提供了丰富的工具类来减少重复代码，提高代码质量。在编写代码时，应该优先使用这些工具类。

### 为什么使用工具类？

- ✅ **减少重复代码**：避免在多个地方写相同的 null 检查、字符串处理等代码
- ✅ **提高可读性**：使用统一的工具类让代码更易理解
- ✅ **降低错误率**：经过充分测试的工具类比手写代码更可靠
- ✅ **便于维护**：如果需要修改某个通用逻辑，只需要改工具类一处

### 常用工具类

#### 1. **AssertUtils - 断言工具**

用于参数验证和前置条件检查：

```java
import ltd.idcu.est.utils.common.AssertUtils;

public void saveUser(User user) {
    // 检查参数不为 null
    AssertUtils.notNull(user, "User cannot be null");
    AssertUtils.notNull(user.getName(), "User name cannot be null");
    
    // 检查条件是否满足
    AssertUtils.isTrue(user.getAge() > 0, "Age must be positive");
    AssertUtils.isTrue(StringUtils.isNotBlank(user.getEmail()), "Email cannot be blank");
    
    // 保存用户...
}
```

#### 2. **ObjectUtils - 对象工具**

用于对象 null 检查和比较：

```java
import ltd.idcu.est.utils.common.ObjectUtils;

// null 检查
if (ObjectUtils.isNull(user)) {
    // 处理 null 情况
}

if (ObjectUtils.isNotNull(user)) {
    // 处理非 null 情况
}

// 安全的 equals 比较
if (ObjectUtils.equals(user1, user2)) {
    // 两个对象相等
}
```

#### 3. **StringUtils - 字符串工具**

用于字符串处理和检查：

```java
import ltd.idcu.est.utils.common.StringUtils;

// 字符串检查
boolean isEmpty = StringUtils.isEmpty(str);           // 检查是否为空
boolean isBlank = StringUtils.isBlank(str);           // 检查是否为空或仅空白字符
boolean isNotBlank = StringUtils.isNotBlank(str);     // 检查是否不为空且不只是空白字符

// 字符串操作
String trimmed = StringUtils.trim(str);                // 去除首尾空白
String uppercase = StringUtils.toUpperCase(str);       // 转大写
String lowercase = StringUtils.toLowerCase(str);       // 转小写

// 字符串拼接
String joined = StringUtils.join(list, ", ");         // 用分隔符拼接集合
```

#### 4. **其他常用工具类**

- **Collections2**：集合工具类
- **Dates**：日期时间工具类
- **Files**：文件 IO 工具类
- **Json**：JSON 格式化工具类
- **Xml**：XML 格式化工具类
- **Yaml**：YAML 格式化工具类

### 代码审查要点

在进行代码审查时，注意检查以下问题：

1. **是否使用了手动 null 检查而不是 AssertUtils**：
   ```java
   // ❌ 不推荐：手动 null 检查
   if (user == null) {
       throw new IllegalArgumentException("User cannot be null");
   }
   
   // ✅ 推荐：使用 AssertUtils
   AssertUtils.notNull(user, "User cannot be null");
   ```

2. **是否使用了手动字符串检查而不是 StringUtils**：
   ```java
   // ❌ 不推荐：手动字符串检查
   if (str == null || str.trim().isEmpty()) {
       // ...
   }
   
   // ✅ 推荐：使用 StringUtils
   if (StringUtils.isBlank(str)) {
       // ...
   }
   ```

3. **是否重复造轮子**：
   - 检查是否有现成的工具类方法可以使用
   - 例如：不要自己写字符串拼接、日期格式化等常用功能

### 添加依赖

要使用这些工具类，需要在 pom.xml 中添加相应的依赖：

```xml
<!-- 通用工具（包含 AssertUtils, ObjectUtils, StringUtils 等） -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-common</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>

<!-- IO 工具（文件操作等） -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-io</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>

<!-- JSON 格式化 -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-format-json</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### 总结

- 🔧 **优先使用 EST 工具类**，避免重复造轮子
- 📋 **使用 AssertUtils** 进行参数验证和前置条件检查
- 📝 **使用 StringUtils** 处理字符串操作
- 🎯 **使用 ObjectUtils** 进行对象 null 检查和比较
- 👀 **代码审查时**，重点检查是否正确使用了工具类

## 小练习

1. 按照这个结构创建一个简单的博客应用
2. 创建一个 Product（商品）模块，包含 Controller、Service、Repository、Model
3. 练习使用构造函数注入

## 总结

- 📁 使用分层架构组织代码
- 📄 接口优先，实现分离
- 🏗️ 使用构造函数注入
- ❌ 避免循环依赖
- 🧩 按功能划分模块
- 📝 遵循统一的命名规范
