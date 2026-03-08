# est-core-container - 小白从入门到精通

## 目录
- [什么是 est-core-container](#什么是-est-core-container)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-core-container

### 用大白话理解
est-core-container 就像"对象管家"，帮你创建和管理所有对象，自动组装对象之间的依赖关系，不用你手动 new 对象。

### 核心特点
- **依赖注入**：构造器、字段、Setter 注入
- **生命周期管理**：Bean 的创建、初始化、销毁
- **作用域支持**：单例、原型、请求、会话作用域
- **AOP 集成**：与 AOP 模块无缝集成

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-container</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 定义 Bean
```java
@Service
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    public User findById(Long id) {
        return userRepository.findById(id);
    }
}

@Repository
public class UserRepository {
    public User findById(Long id) {
        return new User(id);
    }
}
```

### 3. 启动容器
```java
public class Application {
    public static void main(String[] args) {
        Container container = EstContainer.run(Application.class);
        
        UserService userService = container.getBean(UserService.class);
        User user = userService.findById(1L);
    }
}
```

---

## 核心功能

### 依赖注入方式
```java
// 构造器注入（推荐）
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    
    @Inject
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}

// 字段注入
@Service
public class ProductService {
    @Inject
    private ProductRepository productRepository;
}

// Setter 注入
@Service
public class CartService {
    private CartRepository cartRepository;
    
    @Inject
    public void setCartRepository(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}
```

### 生命周期回调
```java
@Service
public class MyService {
    
    @PostConstruct
    public void init() {
        System.out.println("Bean 初始化");
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println("Bean 销毁");
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [AOP 模块](../est-core-aop/README.md)
