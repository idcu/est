# 瀹瑰櫒 API

## Container 鎺ュ彛

渚濊禆娉ㄥ叆瀹瑰櫒鎺ュ彛銆?
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

### 鏂规硶璇存槑

#### get(Class&lt;T&gt; type)
鏍规嵁绫诲瀷鑾峰彇缁勪欢銆?
**鍙傛暟锛?*
- `type` - 缁勪欢绫诲瀷

**杩斿洖鍊硷細** 缁勪欢瀹炰緥

**绀轰緥锛?*
```java
UserService userService = container.get(UserService.class);
```

---

#### get(Class&lt;T&gt; type, String name)
鏍规嵁绫诲瀷鍜屽悕绉拌幏鍙栫粍浠躲€?
**鍙傛暟锛?*
- `type` - 缁勪欢绫诲瀷
- `name` - 缁勪欢鍚嶇О

**杩斿洖鍊硷細** 缁勪欢瀹炰緥

**绀轰緥锛?*
```java
UserService userService = container.get(UserService.class, "userService");
```

---

#### getAll(Class&lt;T&gt; type)
鑾峰彇鎸囧畾绫诲瀷鐨勬墍鏈夌粍浠躲€?
**鍙傛暟锛?*
- `type` - 缁勪欢绫诲瀷

**杩斿洖鍊硷細** 缁勪欢瀹炰緥鍒楄〃

**绀轰緥锛?*
```java
List<Plugin> plugins = container.getAll(Plugin.class);
```

---

#### register(Class&lt;?&gt; type)
娉ㄥ唽缁勪欢绫诲瀷銆?
**鍙傛暟锛?*
- `type` - 缁勪欢绫诲瀷

**绀轰緥锛?*
```java
container.register(UserService.class);
```

---

#### register(Class&lt;?&gt; type, String name)
娉ㄥ唽缁勪欢绫诲瀷骞舵寚瀹氬悕绉般€?
**鍙傛暟锛?*
- `type` - 缁勪欢绫诲瀷
- `name` - 缁勪欢鍚嶇О

**绀轰緥锛?*
```java
container.register(UserService.class, "userService");
```

---

#### register(Object instance)
娉ㄥ唽缁勪欢瀹炰緥銆?
**鍙傛暟锛?*
- `instance` - 缁勪欢瀹炰緥

**绀轰緥锛?*
```java
UserService service = new UserService();
container.register(service);
```

---

#### register(Object instance, String name)
娉ㄥ唽缁勪欢瀹炰緥骞舵寚瀹氬悕绉般€?
**鍙傛暟锛?*
- `instance` - 缁勪欢瀹炰緥
- `name` - 缁勪欢鍚嶇О

**绀轰緥锛?*
```java
UserService service = new UserService();
container.register(service, "userService");
```

---

#### contains(Class&lt;?&gt; type)
妫€鏌ュ鍣ㄤ腑鏄惁鍖呭惈鎸囧畾绫诲瀷鐨勭粍浠躲€?
**鍙傛暟锛?*
- `type` - 缁勪欢绫诲瀷

**杩斿洖鍊硷細** 濡傛灉鍖呭惈杩斿洖 true锛屽惁鍒欒繑鍥?false

**绀轰緥锛?*
```java
if (container.contains(UserService.class)) {
    // ...
}
```

---

#### contains(String name)
妫€鏌ュ鍣ㄤ腑鏄惁鍖呭惈鎸囧畾鍚嶇О鐨勭粍浠躲€?
**鍙傛暟锛?*
- `name` - 缁勪欢鍚嶇О

**杩斿洖鍊硷細** 濡傛灉鍖呭惈杩斿洖 true锛屽惁鍒欒繑鍥?false

**绀轰緥锛?*
```java
if (container.contains("userService")) {
    // ...
}
```

---

## DefaultContainer 瀹炵幇

榛樿鐨勫鍣ㄥ疄鐜扮被銆?
### 鏋勯€犲嚱鏁?
```java
public DefaultContainer()
```

**绀轰緥锛?*
```java
Container container = new DefaultContainer();
```

---

## 娉ㄨВ

### @Component
鏍囪绫讳负缁勪欢銆?
```java
@Component
public class UserService {
    // ...
}
```

### @Inject
鏍囪鏋勯€犲嚱鏁版垨瀛楁杩涜渚濊禆娉ㄥ叆銆?
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
鎸囧畾缁勪欢鍚嶇О銆?
```java
@Component
@Named("userService")
public class UserService {
    // ...
}
```

### @Singleton
鏍囪缁勪欢涓哄崟渚嬨€?
```java
@Component
@Singleton
public class UserService {
    // ...
}
```

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
