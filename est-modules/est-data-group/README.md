# EST Data Group 数据模块组 - 小白从入门到精通

## 目录
1. [什么是 EST Data Group？](#什么是-est-data-group)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Data Group？

### 用大白话理解

EST Data Group 就像是一个"数据工具箱"。想象一下你需要处理各种数据：

**传统方式**：每种数据库都要写不同的代码，还要自己处理事务、连接池... 很麻烦！

**EST Data Group 方式**：给你一套统一的数据访问工具，里面有：
- 💾 **数据访问抽象** - 支持 JDBC、内存、MongoDB、Redis 等多种存储
- 🔄 **工作流引擎** - 定义和执行业务流程

### 核心特点

- 🎯 **简单易用** - 统一的 API，不用关心底层存储
- ⚡ **高性能** - 优化的数据访问，支持连接池
- 🔧 **灵活扩展** - 可以自定义数据访问实现
- 🎨 **功能完整** - 支持多种数据库，还有工作流引擎

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-data</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-data-jdbc</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个数据应用

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.data.query.Query;
import ltd.idcu.est.collection.api.Seq;

public class FirstDataApp {
    public static void main(String[] args) {
        System.out.println("=== EST Data Group 第一个示例 ===\n");
        
        Repository<User> userRepo = JdbcRepository.create(User.class);
        
        User user = new User();
        user.setName("张三");
        user.setEmail("zhangsan@example.com");
        user.setAge(25);
        userRepo.save(user);
        System.out.println("用户已保存: " + user);
        
        User found = userRepo.findById(user.getId());
        System.out.println("找到用户: " + found);
        
        Seq<User> adults = userRepo.query()
            .where("age > ?", 18)
            .orderBy("name")
            .list();
        System.out.println("成年用户: " + adults.toList());
        
        userRepo.delete(user);
        System.out.println("用户已删除");
    }
}

class User {
    private Long id;
    private String name;
    private String email;
    private int age;
    
    public User() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
```

---

## 基础篇

### 1. est-data 数据访问抽象

#### Repository 基本操作

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.collection.api.Seq;

Repository<User> userRepo = JdbcRepository.create(User.class);

User user = new User();
user.setName("张三");
user.setEmail("zhangsan@example.com");
userRepo.save(user);

User found = userRepo.findById(user.getId());

Seq<User> all = userRepo.findAll();

userRepo.delete(user);

userRepo.deleteAll();
```

#### 查询数据

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.query.Query;
import ltd.idcu.est.collection.api.Seq;

Repository<User> userRepo = JdbcRepository.create(User.class);

Seq<User> users = userRepo.query()
    .where("age > ?", 18)
    .and("name LIKE ?", "张%")
    .orderBy("age", true)
    .limit(10)
    .list();

User first = userRepo.query()
    .where("email = ?", "zhangsan@example.com")
    .first()
    .orElse(null);

long count = userRepo.query()
    .where("age > ?", 18)
    .count();
```

### 2. 多种存储实现

#### 内存存储

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.memory.MemoryRepository;

Repository<User> userRepo = MemoryRepository.create(User.class);

User user = new User();
user.setName("张三");
userRepo.save(user);

System.out.println("用户已保存到内存");
```

#### JDBC 存储

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.data.jdbc.JdbcConfig;

JdbcConfig config = JdbcConfig.builder()
    .url("jdbc:mysql://localhost:3306/mydb")
    .username("root")
    .password("password")
    .build();

Repository<User> userRepo = JdbcRepository.create(User.class, config);
```

#### MongoDB 存储

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.mongodb.MongoRepository;
import ltd.idcu.est.data.mongodb.MongoConfig;

MongoConfig config = MongoConfig.builder()
    .connectionString("mongodb://localhost:27017")
    .database("mydb")
    .build();

Repository<User> userRepo = MongoRepository.create(User.class, config);
```

#### Redis 存储

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.redis.RedisRepository;
import ltd.idcu.est.data.redis.RedisConfig;

RedisConfig config = RedisConfig.builder()
    .host("localhost")
    .port(6379)
    .build();

Repository<User> userRepo = RedisRepository.create(User.class, config);
```

### 3. est-workflow 工作流引擎

#### 定义工作流

```java
import ltd.idcu.est.workflow.Workflow;
import ltd.idcu.est.workflow.WorkflowBuilder;
import ltd.idcu.est.workflow.Step;
import ltd.idcu.est.workflow.Condition;

Workflow orderWorkflow = WorkflowBuilder.create("order-process")
    .addStep("validate", ValidateOrderStep.class)
    .addStep("payment", ProcessPaymentStep.class)
        .when(Condition.success("validate"))
    .addStep("inventory", ReserveInventoryStep.class)
        .when(Condition.success("payment"))
    .addStep("shipping", ArrangeShippingStep.class)
        .when(Condition.success("inventory"))
    .addStep("notification", SendNotificationStep.class)
        .when(Condition.anySuccess())
    .build();
```

#### 执行工作流

```java
import ltd.idcu.est.workflow.WorkflowEngine;
import ltd.idcu.est.workflow.WorkflowInstance;
import ltd.idcu.est.workflow.WorkflowContext;

WorkflowEngine engine = WorkflowEngine.create();
engine.registerWorkflow(orderWorkflow);

WorkflowContext context = new WorkflowContext();
context.set("orderId", "ORDER001");
context.set("amount", 99.99);

WorkflowInstance instance = engine.start("order-process", context);

System.out.println("工作流状态: " + instance.getStatus());
System.out.println("当前步骤: " + instance.getCurrentStep());

while (instance.isRunning()) {
    Thread.sleep(1000);
    instance = engine.getInstance(instance.getId());
}

System.out.println("工作流结果: " + instance.getStatus());
```

---

## 进阶篇

### 1. 自定义 Repository

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.data.jdbc.JdbcRepository;
import ltd.idcu.est.data.query.Query;
import ltd.idcu.est.collection.api.Seq;

public interface UserRepository extends Repository<User> {
    Seq<User> findByAgeGreaterThan(int age);
    User findByEmail(String email);
    Seq<User> findByNameContaining(String name);
}

public class JdbcUserRepository extends JdbcRepository<User> implements UserRepository {
    
    public JdbcUserRepository() {
        super(User.class);
    }
    
    @Override
    public Seq<User> findByAgeGreaterThan(int age) {
        return query()
            .where("age > ?", age)
            .list();
    }
    
    @Override
    public User findByEmail(String email) {
        return query()
            .where("email = ?", email)
            .first()
            .orElse(null);
    }
    
    @Override
    public Seq<User> findByNameContaining(String name) {
        return query()
            .where("name LIKE ?", "%" + name + "%")
            .list();
    }
}

UserRepository userRepo = new JdbcUserRepository();
```

### 2. 事务管理

```java
import ltd.idcu.est.data.Transaction;
import ltd.idcu.est.data.TransactionManager;
import ltd.idcu.est.data.jdbc.JdbcTransactionManager;

TransactionManager txManager = JdbcTransactionManager.create(config);

try (Transaction tx = txManager.begin()) {
    userRepo.save(user1);
    userRepo.save(user2);
    orderRepo.save(order);
    tx.commit();
    System.out.println("事务已提交");
} catch (Exception e) {
    tx.rollback();
    System.out.println("事务已回滚");
    throw e;
}
```

### 3. 自定义工作流步骤

```java
import ltd.idcu.est.workflow.Step;
import ltd.idcu.est.workflow.StepContext;
import ltd.idcu.est.workflow.StepResult;

public class ValidateOrderStep implements Step {
    
    @Override
    public StepResult execute(StepContext context) {
        String orderId = context.get("orderId");
        Double amount = context.get("amount");
        
        if (amount == null || amount <= 0) {
            return StepResult.failure("Invalid amount");
        }
        
        System.out.println("订单验证通过: " + orderId);
        return StepResult.success();
    }
}

public class ProcessPaymentStep implements Step {
    
    @Override
    public StepResult execute(StepContext context) {
        String orderId = context.get("orderId");
        Double amount = context.get("amount");
        
        System.out.println("处理支付: " + orderId + ", 金额: " + amount);
        
        boolean success = paymentService.process(orderId, amount);
        
        if (success) {
            return StepResult.success();
        } else {
            return StepResult.failure("Payment failed");
        }
    }
}
```

---

## 最佳实践

### 1. Repository 命名规范

```java
// ✅ 推荐：清晰的方法名
public interface UserRepository extends Repository<User> {
    Seq<User> findByAgeGreaterThan(int age);
    User findByEmail(String email);
    Seq<User> findByNameContaining(String name);
}

// ❌ 不推荐：模糊的方法名
public interface BadUserRepository extends Repository<User> {
    Seq<User> getUsers(int age);
    User getUser(String email);
    Seq<User> search(String name);
}
```

### 2. 合理使用索引

```java
// ✅ 推荐：为常用查询字段创建索引
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_age ON users(age);

// 然后查询会更快
User user = userRepo.findByEmail("zhangsan@example.com");
```

### 3. 批量操作

```java
import ltd.idcu.est.data.Repository;
import ltd.idcu.est.collection.api.Seq;

Repository<User> userRepo = JdbcRepository.create(User.class);

// ✅ 推荐：批量保存
Seq<User> users = Seqs.of(user1, user2, user3);
userRepo.saveAll(users);

// ❌ 不推荐：逐个保存
userRepo.save(user1);
userRepo.save(user2);
userRepo.save(user3);
```

### 4. 工作流错误处理

```java
import ltd.idcu.est.workflow.WorkflowEngine;
import ltd.idcu.est.workflow.WorkflowInstance;

WorkflowEngine engine = WorkflowEngine.create();
WorkflowInstance instance = engine.start("order-process", context);

while (instance.isRunning()) {
    Thread.sleep(1000);
    instance = engine.getInstance(instance.getId());
    
    if (instance.hasFailed()) {
        System.out.println("工作流失败: " + instance.getError());
        engine.retry(instance.getId());
        break;
    }
}
```

---

## 模块结构

```
est-data-group/
├── est-data/         # 数据访问抽象（JDBC、Memory、MongoDB、Redis）
└── est-workflow/     # 工作流引擎
```

---

## 相关资源

- [est-data README](./est-data/README.md) - 数据访问详细文档
- [est-workflow README](./est-workflow/README.md) - 工作流详细文档
- [示例代码](../../est-examples/est-examples-basic/) - 基础示例
- [示例代码](../../est-examples/est-examples-advanced/) - 高级示例
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
