# est-core-tx - 小白从入门到精通

## 目录
- [什么是 est-core-tx](#什么是-est-core-tx)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-core-tx

### 用大白话理解
est-core-tx 就像"事务管家"，帮你管理数据库事务，要么全部成功，要么全部失败，不会出现一半成功一半失败的情况。

### 核心特点
- **声明式事务**：注解驱动的事务管理
- **事务传播**：支持多种事务传播行为
- **事务隔离**：支持多种隔离级别
- **回滚控制**：灵活的回滚规则

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-tx</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 使用事务
```java
@Service
public class OrderService {
    
    @Inject
    private OrderRepository orderRepository;
    
    @Inject
    private InventoryService inventoryService;
    
    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
        inventoryService.deductStock(order.getProductId(), order.getQuantity());
    }
}
```

---

## 核心功能

### 事务配置
```java
@Transactional(
    propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED,
    timeout = 30,
    readOnly = false,
    rollbackFor = Exception.class
)
public void complexOperation() {
}
```

### 编程式事务
```java
@Service
public class ManualTransactionService {
    
    @Inject
    private TransactionTemplate transactionTemplate;
    
    public void doInTransaction() {
        transactionTemplate.execute(status -> {
            try {
                orderRepository.save(order);
                inventoryService.deduct(productId, quantity);
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [依赖注入容器](../est-core-container/README.md)
