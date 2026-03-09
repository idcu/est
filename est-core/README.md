# EST Core 核心模块 - 小白从入门到精通

## 目录
1. [什么是 EST Core？](#什么是-est-core)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [核心组件](#核心组件)
4. [进阶指南](#进阶指南)
5. [高级指南](#高级指南)
6. [与 est-collection 集成](#与-est-collection-集成)
7. [最佳实践](#最佳实践)

---

## 什么是 EST Core？

### 用大白话理解

EST Core 就像是一个「能力管家」。想象一下你经营一家公司，有很多不同的部门：市场部、技术部、财务部... 每个部门都需要用到其他部门的工作。

**传统方式**：每个部门需要什么都自己去创建，导致重复工作，关系混乱。

**EST Core 方式**：你把所有部门都告诉管家，他会：
- 记住每个部门的位置和职责
- 当一个部门需要另一个部门帮助时，管家会自动安排
- 确保每个人拿到的都是正确的、需要的东西

### 核心特点

- 📦 **简单易用** - 几行代码就能创建和管理组件
- 🔧 **高性能** - 三级 Map 实现，快速查找组件
- 🔄 **类型安全** - 编译时就能发现类型错误
- 🚀 **可扩展** - 支持多种注入方式、作用域、生命周期管理

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-api</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-impl</artifactId>
        <version>2.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个程序

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class FirstExample {
    public static void main(String[] args) {
        // 1. 创建一个「管家」（容器）
        Container container = new DefaultContainer();
        
        // 2. 告诉管家有什么东西
        container.register(MyService.class, MyServiceImpl.class);
        
        // 3. 从管家里获取需要的东西
        MyService service = container.get(MyService.class);
        
        // 4. 使用它！
        System.out.println(service.hello("小白"));
    }
}

interface MyService {
    String hello(String name);
}

class MyServiceImpl implements MyService {
    @Override
    public String hello(String name) {
        return "你好，" + name + "！欢迎使用 EST Core！";
    }
}
```

运行这个程序，你会看到：
```
你好，小白！欢迎使用 EST Core！
```

喜欢你！你已经成功使用 EST Core 了！

---

## 核心组件

### 1. 什么是依赖注入？

#### 不使用依赖注入的情况

```java
class OrderService {
    private UserService userService;
    private ProductService productService;
    
    public OrderService() {
        // 自己创建依赖 - 麻烦！
        this.userService = new UserServiceImpl();
        this.productService = new ProductServiceImpl();
    }
}
```

#### 使用依赖注入的情况

```java
class OrderService {
    private UserService userService;
    private ProductService productService;
    
    // 由容器提供依赖 - 清爽！
    public OrderService(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
}
```

### 2. 注册组件的基本方式

#### 方法一：注册实现类

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class BasicRegistration {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 注册：接口 -> 实现类
        container.register(UserService.class, UserServiceImpl.class);
        
        // 获取组件
        UserService userService = container.get(UserService.class);
        System.out.println("用户服务：" + userService);
    }
}

interface UserService {
    String getUserInfo();
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserInfo() {
        return "用户信息：传奇";
    }
}
```

#### 方法二：注册单例（已经创建好的对象）

```java
// 创建一个配置对象
Config myConfig = new Config("app", "1.0");

// 直接注册这个对象
container.registerSingleton(Config.class, myConfig);

// 获取的就是你之前注册的那个对象
Config config = container.get(Config.class);
System.out.println(config); // 输出：Config{name='app', version='1.0'}
```

### 3. 获取组件

```java
// 方法一：直接获取（如果不存在会报错）
UserService service = container.get(UserService.class);

// 方法二：安全获取（如果不存在返回null）
Optional<UserService> optional = container.getIfPresent(UserService.class);
if (optional.isPresent()) {
    UserService service = optional.get();
    System.out.println("找到了服务！");
} else {
    System.out.println("服务不存在");
}

// 方法三：检查是否存在
if (container.contains(UserService.class)) {
    System.out.println("服务已注册");
}
```

---

## 进阶指南

### 1. 构造函数注入

EST Core 会自动识别构造函数，并注入需要的依赖。

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ConstructorInjection {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 注册所有需要的组件
        container.register(UserService.class, UserServiceImpl.class);
        container.register(ProductService.class, ProductServiceImpl.class);
        container.register(OrderService.class, OrderServiceImpl.class);
        
        // 获取订单服务 - 它会自动获取用户服务和产品服务！
        OrderService orderService = container.get(OrderService.class);
        System.out.println(orderService.createOrder("张三", "iPhone 15"));
    }
}

interface UserService {
    String getUserName();
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserName() {
        return "张三";
    }
}

interface ProductService {
    String getProductName();
}

class ProductServiceImpl implements ProductService {
    @Override
    public String getProductName() {
        return "iPhone 15";
    }
}

interface OrderService {
    String createOrder(String user, String product);
}

class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ProductService productService;
    
    // 构造函数注入 - EST Core 会自动注入！
    public OrderServiceImpl(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    
    @Override
    public String createOrder(String user, String product) {
        return "订单创建成功，用户：" + userService.getUserName() + 
               "，产品：" + productService.getProductName();
    }
}
```

### 2. @Inject 注解 - 字段注入和方法注入

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.impl.DefaultContainer;

public class AnnotationInjection {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        container.register(UserService.class, UserServiceImpl.class);
        container.register(EmailService.class, EmailServiceImpl.class);
        container.register(NotificationService.class, NotificationServiceImpl.class);
        
        NotificationService notificationService = container.get(NotificationService.class);
        notificationService.sendNotification("你有新消息！");
    }
}

class NotificationServiceImpl implements NotificationService {
    @Inject
    private UserService userService;
    
    private EmailService emailService;
    
    @Inject
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @Override
    public void sendNotification(String message) {
        String userName = userService.getUserName();
        emailService.sendEmail(userName, message);
    }
}
```

### 3. @Component 注解 - 自动注册

使用注解后，你可以让 EST Core 自动扫描和注册组件：

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ComponentScan {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 自动扫描指定包下的所有 @Component、@Service、@Repository
        container.scan("com.example.myapp");
        
        // 直接使用！
        MyService service = container.get(MyService.class);
        System.out.println(service.doSomething());
    }
}

@Service
class MyServiceImpl implements MyService {
    @Override
    public String doSomething() {
        return "自动扫描成功！";
    }
}
```

### 4. @Qualifier - 多个实现类时的选择

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.impl.DefaultContainer;

public class QualifierExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 注册两个实现
        container.register(PaymentService.class, WeChatPayment.class, "wechat");
        container.register(PaymentService.class, AliPayment.class, "alipay");
        
        // 通过限定符获取
        PaymentService wechat = container.get(PaymentService.class, "wechat");
        PaymentService alipay = container.get(PaymentService.class, "alipay");
        
        System.out.println(wechat.pay(100));
        System.out.println(alipay.pay(100));
    }
}

interface PaymentService {
    String pay(double amount);
}

class WeChatPayment implements PaymentService {
    @Override
    public String pay(double amount) {
        return "微信支付：" + amount + " 元";
    }
}

class AliPayment implements PaymentService {
    @Override
    public String pay(double amount) {
        return "支付宝支付：" + amount + " 元";
    }
}
```

---

## 高级指南

### 1. 作用域（Scope）- 控制对象的生命周期

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ScopeExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 单例模式（默认）：每次获取都是同一个对象
        container.register(SingletonService.class, SingletonServiceImpl.class, Scope.SINGLETON);
        SingletonService s1 = container.get(SingletonService.class);
        SingletonService s2 = container.get(SingletonService.class);
        System.out.println("单例是否相同：" + (s1 == s2)); // true
        
        // 原型模式：每次获取都是新对象
        container.register(PrototypeService.class, PrototypeServiceImpl.class, Scope.PROTOTYPE);
        PrototypeService p1 = container.get(PrototypeService.class);
        PrototypeService p2 = container.get(PrototypeService.class);
        System.out.println("原型是否相同：" + (p1 == p2)); // false
    }
}
```

### 2. 生命周期管理

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.lifecycle.PreDestroy;
import ltd.idcu.est.core.impl.DefaultContainer;

public class LifecycleExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        container.register(DatabaseService.class, DatabaseServiceImpl.class);
        
        // 获取时会调用 @PostConstruct
        DatabaseService db = container.get(DatabaseService.class);
        
        // 使用服务
        db.query("SELECT * FROM users");
        
        // 关闭容器时会调用 @PreDestroy
        container.close();
    }
}

interface DatabaseService {
    void query(String sql);
}

class DatabaseServiceImpl implements DatabaseService {
    
    @PostConstruct
    public void init() {
        System.out.println("数据库连接已建立");
    }
    
    @Override
    public void query(String sql) {
        System.out.println("执行 SQL：" + sql);
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("数据库连接已关闭");
    }
}
```

### 3. 配置管理

```java
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.DefaultContainer;

import java.util.HashMap;
import java.util.Map;

public class ConfigExample {
    public static void main(String[] args) {
        // 创建配置
        Map<String, Object> props = new HashMap<>();
        props.put("app.name", "我的应用");
        props.put("app.version", "1.0.0");
        props.put("server.port", 8080);
        props.put("debug", true);
        
        Config config = new DefaultConfig(props);
        
        // 使用配置
        String appName = config.getString("app.name");
        int port = config.getInt("server.port", 8080);
        boolean debug = config.getBoolean("debug", false);
        
        System.out.println("应用名称：" + appName);
        System.out.println("端口：" + port);
        System.out.println("调试模式：" + debug);
        
        // 也可以把配置放到容器里
        Container container = new DefaultContainer(config);
        container.registerSingleton(Config.class, config);
        
        Config containerConfig = container.get(Config.class);
        System.out.println("从容器获取的应用名称：" + containerConfig.getString("app.name"));
    }
}
```

---

## 与 est-collection 集成

EST Core 和 est-collection 可以完美结合使用！

### 场景：用户管理系统

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.impl.DefaultContainer;

public class CollectionIntegration {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        container.scan("com.example");
        
        UserController controller = container.get(UserController.class);
        
        System.out.println("=== 所有成年用户 ===");
        controller.printAdultUsers();
        
        System.out.println("\n=== 按年龄排序 ===");
        controller.printUsersSortedByAge();
        
        System.out.println("\n=== 平均年龄 ===");
        controller.printAverageAge();
    }
}

class User {
    private String name;
    private int age;
    private String city;
    
    public User(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    
    @Override
    public String toString() {
        return name + " (" + age + "岁) " + city;
    }
}

@Service
class UserRepository {
    public Seq<User> findAll() {
        return Seqs.of(
            new User("张三", 25, "北京"),
            new User("李四", 17, "上海"),
            new User("王五", 30, "广州"),
            new User("赵六", 16, "深圳"),
            new User("钱七", 28, "北京")
        );
    }
}

@Service
class UserService {
    @Inject
    private UserRepository userRepository;
    
    public Seq<User> getAdultUsers() {
        return userRepository.findAll()
            .where(user -> user.getAge() >= 18);
    }
    
    public Seq<User> getUsersSortedByAge() {
        return userRepository.findAll()
            .sortBy(User::getAge);
    }
    
    public double getAverageAge() {
        return userRepository.findAll()
            .mapToInt(User::getAge)
            .average()
            .orElse(0);
    }
}

@Component
class UserController {
    @Inject
    private UserService userService;
    
    public void printAdultUsers() {
        userService.getAdultUsers().forEach(System.out::println);
    }
    
    public void printUsersSortedByAge() {
        userService.getUsersSortedByAge().forEach(System.out::println);
    }
    
    public void printAverageAge() {
        System.out.println("平均年龄：" + userService.getAverageAge() + " 岁");
    }
}
```

运行输出：
```
=== 所有成年用户 ===
张三 (25岁) 北京
王五 (30岁) 广州
钱七 (28岁) 北京

=== 按年龄排序 ===
赵六 (16岁) 深圳
李四 (17岁) 上海
张三 (25岁) 北京
钱七 (28岁) 北京
王五 (30岁) 广州

=== 平均年龄 ===
平均年龄：23.2 岁
```

---

## 最佳实践

### 1. 优先使用构造函数注入

```java
// ✅ 推荐：构造函数注入
class GoodService {
    private final Dependency dependency;
    
    public GoodService(Dependency dependency) {
        this.dependency = dependency;
    }
}

// ❌ 不推荐：字段注入
class BadService {
    @Inject
    private Dependency dependency;
}
```

### 2. 使用接口编程

```java
// ✅ 推荐：依赖接口
interface UserRepository {
    User findById(String id);
}

class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

// ❌ 不推荐：依赖具体实现
class BadUserService {
    private final JdbcUserRepository repository;
    
    public BadUserService(JdbcUserRepository repository) {
        this.repository = repository;
    }
}
```

### 3. 合理使用单例

```java
// ✅ 适合单例：无状态的服务
@Service
class StatelessService {
    public String process(String input) {
        return input.toUpperCase();
    }
}

// ❌ 不适合单例：有状态的对象
class StatefulObject {
    private int counter;
    
    public void increment() {
        counter++;
    }
}
```

### 4. 模块化注册

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.core.impl.AbstractModule;
import ltd.idcu.est.core.impl.DefaultContainer;

public class ModuleExample {
    public static void main(String[] args) {
        Container container = new DefaultContainer();
        
        // 按模块组织
        container.install(new UserModule());
        container.install(new OrderModule());
        container.install(new PaymentModule());
    }
}

class UserModule extends AbstractModule {
    @Override
    protected void configure() {
        register(UserService.class, UserServiceImpl.class);
        register(UserRepository.class, UserRepositoryImpl.class);
    }
}
```

## 常见问题

### Q: 循环依赖怎么办？

A: EST Core 会检测循环依赖并抛出异常。解决方案：
1. 重构代码，消除循环依赖
2. 使用懒加载
3. 使用 setter 注入替换构造函数注入

### Q: 性能如何？

A: EST Core 使用三级 Map 实现，性能非常好，完全可以满足生产环境需求。

### Q: 和 Spring 有什么区别？

A: EST Core 更轻量、更简单，专注于核心的 DI 功能，没有 Spring 那么复杂。

## 下一步

- 学习 [est-collection](../est-base/est-collection/README.md) 进行数据处理
- 查看 [EST Modules](../est-modules/README.md) 进行功能扩展
- 阅读 [API 文档](../docs/api/core/) 了解更多细节
