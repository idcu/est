# Configuration API

`Configuration` 接口提供了灵活的配置管理功能，支持多种数据类型和配置源。

## 接口定义

```java
package ltd.idcu.est.core.api;

public interface Configuration {
    void set(String key, Object value);
    <T> T get(String key);
    <T> T get(String key, T defaultValue);
    
    String getString(String key);
    String getString(String key, String defaultValue);
    
    int getInt(String key);
    int getInt(String key, int defaultValue);
    
    long getLong(String key);
    long getLong(String key, long defaultValue);
    
    double getDouble(String key);
    double getDouble(String key, double defaultValue);
    
    boolean getBoolean(String key);
    boolean getBoolean(String key, boolean defaultValue);
    
    <T> List<T> getList(String key);
    <T> List<T> getList(String key, List<T> defaultValue);
    
    <K, V> Map<K, V> getMap(String key);
    <K, V> Map<K, V> getMap(String key, Map<K, V> defaultValue);
    
    boolean contains(String key);
    void remove(String key);
    void clear();
    
    void load(String resourcePath);
    void load(Path filePath);
    
    Map<String, Object> getAll();
}
```

## 方法详解

### set

设置配置值。

**参数：**
- `key` - 配置键
- `value` - 配置值

**示例：**
```java
config.set("app.name", "My App");
config.set("server.port", 8080);
config.set("debug", true);
```

---

### get

获取配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 配置值或默认值

**示例：**
```java
String appName = config.get("app.name");
String version = config.get("app.version", "1.0.0");
```

---

### getString

获取字符串配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 字符串值或默认值

**示例：**
```java
String appName = config.getString("app.name");
String description = config.getString("app.description", "No description");
```

---

### getInt

获取整数配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 整数值或默认值

**示例：**
```java
int port = config.getInt("server.port");
int maxConnections = config.getInt("db.maxConnections", 20);
```

---

### getLong

获取长整数配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 长整数值或默认值

**示例：**
```java
long timeout = config.getLong("connection.timeout");
long maxFileSize = config.getLong("upload.maxFileSize", 10485760L);
```

---

### getDouble

获取浮点数配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 浮点数值或默认值

**示例：**
```java
double threshold = config.getDouble("threshold");
double taxRate = config.getDouble("tax.rate", 0.08);
```

---

### getBoolean

获取布尔值配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 布尔值或默认值

**示例：**
```java
boolean debug = config.getBoolean("debug");
boolean cacheEnabled = config.getBoolean("cache.enabled", false);
```

---

### getList

获取列表配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 列表值或默认值

**示例：**
```java
List<String> features = config.getList("app.features");
List<String> defaultFeatures = Arrays.asList("core", "web");
List<String> features = config.getList("app.features", defaultFeatures);
```

---

### getMap

获取映射配置值。

**参数：**
- `key` - 配置键
- `defaultValue` - 默认值（可选）

**返回：**
- 映射值或默认值

**示例：**
```java
Map<String, Object> database = config.getMap("db");
Map<String, Object> defaultDb = Map.of("url", "jdbc:h2:mem:test");
Map<String, Object> database = config.getMap("db", defaultDb);
```

---

### contains

检查配置键是否存在。

**参数：**
- `key` - 配置键

**返回：**
- 键是否存在

**示例：**
```java
if (config.contains("app.name")) {
    // 配置存在
}
```

---

### remove

删除配置键。

**参数：**
- `key` - 配置键

**示例：**
```java
config.remove("temporary.key");
```

---

### clear

清空所有配置。

**示例：**
```java
config.clear();
```

---

### load

从资源或文件加载配置。

**参数：**
- `resourcePath` - 类路径资源路径
- `filePath` - 文件系统路径

**支持格式：**
- `.properties` - Properties 文件
- `.yml` / `.yaml` - YAML 文件
- `.json` - JSON 文件

**示例：**
```java
// 从类路径加载
config.load("application.properties");
config.load("config/application.yml");

// 从文件系统加载
config.load(Paths.get("/etc/myapp/config.properties"));
```

---

### getAll

获取所有配置。

**返回：**
- 包含所有配置的 Map

**示例：**
```java
Map<String, Object> allConfig = config.getAll();
for (Map.Entry<String, Object> entry : allConfig.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

## DefaultConfiguration 实现

`DefaultConfiguration` 是 `Configuration` 接口的默认实现。

**特性：**
- 线程安全
- 支持嵌套键（`app.name`）
- 支持类型转换
- 支持多种配置格式

**使用示例：**
```java
Configuration config = new DefaultConfiguration();

// 设置嵌套配置
config.set("app.name", "My App");
config.set("app.version", "1.0.0");
config.set("server.port", 8080);

// 获取嵌套配置
String appName = config.getString("app.name");
int port = config.getInt("server.port");
```

## 配置文件示例

### application.properties

```properties
app.name=My Application
app.version=1.0.0
app.description=A sample application

server.port=8080
server.host=0.0.0.0
server.threadPoolSize=20

db.url=jdbc:mysql://localhost:3306/mydb
db.username=root
db.password=secret
db.maxConnections=20

cache.enabled=true
cache.maxSize=1000
cache.expireAfterWrite=3600

logging.level=INFO
logging.file=logs/app.log
```

### application.yml

```yaml
app:
  name: My Application
  version: 1.0.0
  description: A sample application

server:
  port: 8080
  host: 0.0.0.0
  threadPoolSize: 20

db:
  url: jdbc:mysql://localhost:3306/mydb
  username: root
  password: secret
  maxConnections: 20

cache:
  enabled: true
  maxSize: 1000
  expireAfterWrite: 3600

logging:
  level: INFO
  file: logs/app.log
```

## 最佳实践

1. **使用配置文件**
   - 不要硬编码配置值
   - 使用外部配置文件

2. **提供默认值**
   - 使用带默认值的 getter 方法
   - 确保应用在缺少配置时也能运行

3. **验证配置**
   - 启动时验证必需配置
   - 检查配置值的有效性

4. **环境特定配置**
   - 使用不同的配置文件
   - 如 `application-dev.properties`, `application-prod.properties`
