# 容器 API

## Container 接口

依赖注入容器接口。

```java
public interface Container {
    <T> T get(Class<T> type);
    <T> T get(Class<T> type, String name);
    <T> List<T> getAll(Class<T> type);
    void register(Class<?> type);
    void register(Class<?> type, String name);
    void register(Object instance);
    void register(Object instance, String name);
    boolean contains(Class<?> type);
    boolean contains(String name);
}
```

### 方法说明

#### get(Class&lt;T&gt; type)
根据类型获取组件。

**参数：**
- `type` - 组件类型

**返回值：** 组件实例

**示例：**
```java
UserService userService = container.get(UserService.class);
```

---

#### get(Class&lt;T&gt; type, String name)
根据类型和名称获取组件。

**参数：**
- `type` - 组件类型
- `name` - 组件名称

**返回值：** 组件实例

**示例：**
```java
UserService userService = container.get(UserService.class, "userService");
```

---

#### getAll(Class&lt;T&gt; type)
获取指定类型的所有组件。

**参数：**
- `type` - 组件类型

**返回值：** 组件实例列表

**示例：**
```java
List<Plugin> plugins = container.getAll(Plugin.class);
```

---

#### register(Class&lt;?&gt; type)
注册组件类型。

**参数：**
- `type` - 组件类型

**示例：**
```java
container.register(UserService.class);
```

---

#### register(Class&lt;?&gt; type, String name)
注册组件类型并指定名称。

**参数：**
- `type` - 组件类型
- `name` - 组件名称

**示例：**
```java
container.register(UserService.class, "userService");
```

---

#### register(Object instance)
注册组件实例。

**参数：**
- `instance` - 组件实例

**示例：**
```java
UserService service = new UserService();
container.register(service);
```

---

#### register(Object instance, String name)
注册组件实例并指定名称。

**参数：**
- `instance` - 组件实例
- `name` - 组件名称

**示例：**
```java
UserService service = new UserService();
container.register(service, "userService");
```

---

#### contains(Class&lt;?&gt; type)
检查容器中是否包含指定类型的组件。

**参数：**
- `type` - 组件类型

**返回值：** 如果包含返回 true，否则返回 false

**示例：**
```java
if (container.contains(UserService.class)) {
    // ...
}
```

---

#### contains(String name)
检查容器中是否包含指定名称的组件。

**参数：**
- `name` - 组件名称

**返回值：** 如果包含返回 true，否则返回 false

**示例：**
```java
if (container.contains("userService")) {
    // ...
}
```

---

## DefaultContainer 实现

默认的容器实现类。

### 构造函数

```java
public DefaultContainer()
```

**示例：**
```java
Container container = new DefaultContainer();
```

---

## 注解

### @Component
标记类为组件。

```java
@Component
public class UserService {
    // ...
}
```

### @Inject
标记构造函数或字段进行依赖注入。

```java
@Component
public class UserController {
    private final UserService userService;
    
    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

### @Named
指定组件名称。

```java
@Component
@Named("userService")
public class UserService {
    // ...
}
```

### @Singleton
标记组件为单例。

```java
@Component
@Singleton
public class UserService {
    // ...
}
```

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
