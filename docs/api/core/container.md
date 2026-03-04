# Container API

`Container` 是 EST 框架的依赖注入容器，用于管理组件的注册和获取。

## 接口定义

```java
package ltd.idcu.est.core.api;

public interface Container {
    <T> void register(Class<T> type, Class<? extends T> implementation);
    <T> void registerSingleton(Class<T> type, T instance);
    <T> void registerSupplier(Class<T> type, Supplier<T> supplier);
    
    <T> T get(Class<T> type);
    <T> Optional<T> getIfPresent(Class<T> type);
    <T> boolean contains(Class<T> type);
}
```

## 方法详解

### register

注册类型与实现类。

**参数：**
- `type` - 类型接口或基类
- `implementation` - 实现类

**示例：**
```java
container.register(UserService.class, UserServiceImpl.class);
```

---

### registerSingleton

注册单例实例。

**参数：**
- `type` - 类型
- `instance` - 单例实例

**示例：**
```java
container.registerSingleton(Config.class, new Config());
```

---

### registerSupplier

注册供应者函数，用于延迟创建实例。

**参数：**
- `type` - 类型
- `supplier` - 供应者函数

**示例：**
```java
container.registerSupplier(Database.class, () -> {
    Database db = new Database();
    db.connect();
    return db;
});
```

---

### get

获取类型的实例，不存在则创建。

**参数：**
- `type` - 要获取的类型

**返回：**
- 类型实例

**示例：**
```java
UserService service = container.get(UserService.class);
```

---

### getIfPresent

安全获取，返回 Optional。

**参数：**
- `type` - 要获取的类型

**返回：**
- Optional 包装的实例

**示例：**
```java
Optional<UserService> service = container.getIfPresent(UserService.class);
service.ifPresent(s -> s.doSomething());
```

---

### contains

检查类型是否已注册。

**参数：**
- `type` - 要检查的类型

**返回：**
- 是否已注册

**示例：**
```java
if (container.contains(UserService.class)) {
    // ...
}
```

## DefaultContainer 实现

`DefaultContainer` 是 `Container` 接口的默认实现，使用三层 Map 结构：

```java
public class DefaultContainer implements Container {
    private final Map<Class<?>, Supplier<?>> registrations;
    private final Map<Class<?>, Object> instances;
    private final Map<Class<?>, Object> cachedInstances;
}
```

**特性：**
- 线程安全（使用 ConcurrentHashMap）
- 支持单例和非单例
- 支持延迟初始化
