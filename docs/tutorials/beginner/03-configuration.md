# 教程 03: 配置管理

在本教程中，你将学习如何使用 EST 的配置管理功能。

## Configuration 接口

`Configuration` 接口提供了灵活的配置管理功能。

## 基础用法

```java
package com.example.config;

import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.Configuration;
import ltd.idcu.est.core.api.EstApplication;

public class BasicConfigExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Configuration config = app.getConfiguration();
        
        // 设置配置
        config.set("app.name", "My App");
        config.set("app.version", "1.0.0");
        config.set("server.port", 8080);
        config.set("debug", true);
        
        // 获取配置
        String appName = config.getString("app.name");
        String version = config.getString("app.version", "0.0.1");
        int port = config.getInt("server.port", 80);
        boolean debug = config.getBoolean("debug", false);
        
        System.out.println("App Name: " + appName);
        System.out.println("Version: " + version);
        System.out.println("Port: " + port);
        System.out.println("Debug: " + debug);
    }
}
```

## 支持的数据类型

```java
Configuration config = app.getConfiguration();

// 字符串
config.set("string.key", "value");
String str = config.getString("string.key");

// 整数
config.set("int.key", 123);
int intVal = config.getInt("int.key");
int intWithDefault = config.getInt("nonexistent", 456);

// 长整数
config.set("long.key", 123456789L);
long longVal = config.getLong("long.key");

// 浮点数
config.set("double.key", 3.14);
double doubleVal = config.getDouble("double.key");

// 布尔值
config.set("boolean.key", true);
boolean boolVal = config.getBoolean("boolean.key");

// List
List<String> list = Arrays.asList("a", "b", "c");
config.set("list.key", list);
List<String> retrievedList = config.getList("list.key");

// Map
Map<String, Object> map = new HashMap<>();
map.put("key1", "value1");
map.put("key2", 42);
config.set("map.key", map);
Map<String, Object> retrievedMap = config.getMap("map.key");
```

## 从 properties 文件加载

创建 `src/main/resources/application.properties`：

```properties
# 应用配置
app.name=My EST Application
app.version=1.0.0
app.description=A sample application

# 服务器配置
server.port=8080
server.host=0.0.0.0
server.threadPoolSize=20

# 数据库配置
db.url=jdbc:mysql://localhost:3306/mydb
db.username=root
db.password=secret
db.maxConnections=20

# 缓存配置
cache.enabled=true
cache.maxSize=1000
cache.expireAfterWrite=3600

# 日志配置
logging.level=INFO
logging.file=logs/app.log
logging.maxFileSize=10MB
```

现在加载配置文件：

```java
package com.example.config;

import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.Configuration;
import ltd.idcu.est.core.api.EstApplication;

public class PropertiesFileExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Configuration config = app.getConfiguration();
        
        // 从类路径加载 properties 文件
        config.load("application.properties");
        
        // 或者从文件系统加载
        // config.load(Paths.get("/path/to/config.properties"));
        
        // 使用配置
        String appName = config.getString("app.name");
        int port = config.getInt("server.port");
        boolean cacheEnabled = config.getBoolean("cache.enabled");
        
        System.out.println("App Name: " + appName);
        System.out.println("Port: " + port);
        System.out.println("Cache Enabled: " + cacheEnabled);
    }
}
```

## 从 YAML 文件加载

创建 `src/main/resources/application.yml`：

```yaml
app:
  name: My EST Application
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
  maxFileSize: 10MB
```

加载 YAML 配置：

```java
package com.example.config;

public class YamlFileExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Configuration config = app.getConfiguration();
        
        // 从类路径加载 YAML 文件
        config.load("application.yml");
        
        // 使用配置（扁平化键）
        String appName = config.getString("app.name");
        int port = config.getInt("server.port");
        
        System.out.println("App Name: " + appName);
        System.out.println("Port: " + port);
    }
}
```

## 配置合并和覆盖

```java
package com.example.config;

public class ConfigMergeExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Configuration config = app.getConfiguration();
        
        // 1. 先加载默认配置
        config.load("application-defaults.properties");
        
        // 2. 再加载环境特定配置（会覆盖默认值）
        String env = System.getProperty("env", "dev");
        config.load("application-" + env + ".properties");
        
        // 3. 最后可以用系统属性覆盖
        String port = System.getProperty("server.port");
        if (port != null) {
            config.set("server.port", Integer.parseInt(port));
        }
        
        // 使用最终配置
        System.out.println("Server Port: " + config.getInt("server.port"));
    }
}
```

## 配置验证

```java
package com.example.config;

public class ConfigValidationExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Configuration config = app.getConfiguration();
        
        config.load("application.properties");
        
        // 验证必需的配置
        validateConfig(config);
        
        // 继续应用启动
        System.out.println("Configuration validated successfully!");
    }
    
    private static void validateConfig(Configuration config) {
        List<String> errors = new ArrayList<>();
        
        // 检查必需配置
        if (!config.contains("app.name")) {
            errors.add("app.name is required");
        }
        
        // 验证端口范围
        int port = config.getInt("server.port", 8080);
        if (port < 1 || port > 65535) {
            errors.add("server.port must be between 1 and 65535");
        }
        
        // 验证日志级别
        String logLevel = config.getString("logging.level", "INFO");
        Set<String> validLevels = Set.of("DEBUG", "INFO", "WARN", "ERROR");
        if (!validLevels.contains(logLevel.toUpperCase())) {
            errors.add("logging.level must be one of: " + validLevels);
        }
        
        if (!errors.isEmpty()) {
            throw new IllegalStateException("Configuration errors:\n" + 
                String.join("\n", errors));
        }
    }
}
```

## 在应用中使用配置

```java
package com.example.config;

public class AppConfigExample {
    public static void main(String[] args) {
        EstApplication app = DefaultEstApplication.create();
        Configuration config = app.getConfiguration();
        
        config.load("application.properties");
        
        // 创建服务器配置
        int port = config.getInt("server.port", 8080);
        String host = config.getString("server.host", "0.0.0.0");
        
        // 创建缓存配置
        boolean cacheEnabled = config.getBoolean("cache.enabled", false);
        int cacheMaxSize = config.getInt("cache.maxSize", 1000);
        int cacheExpire = config.getInt("cache.expireAfterWrite", 3600);
        
        // 创建数据库配置
        String dbUrl = config.getString("db.url");
        String dbUsername = config.getString("db.username");
        String dbPassword = config.getString("db.password");
        
        // 使用配置创建组件
        if (cacheEnabled) {
            Cache<String, Object> cache = MemoryCaches.create(
                CacheConfig.builder()
                    .maxSize(cacheMaxSize)
                    .expireAfterWrite(cacheExpire)
                    .build()
            );
            app.getContainer().registerSingleton(Cache.class, cache);
        }
        
        // 启动应用
        app.run();
        System.out.println("Application started on " + host + ":" + port);
    }
}
```

## 最佳实践

1. **使用配置文件**
   - 不要硬编码配置值
   - 支持多环境配置（dev, test, prod）

2. **提供默认值**
   - 使用带默认值的 getter 方法
   - 确保应用在缺少配置时也能运行

3. **验证配置**
   - 启动时验证必需配置
   - 检查配置值的有效性

4. **配置优先级**
   - 默认配置 < 环境配置 < 系统属性 < 命令行参数

## 下一步

在下一个教程中，我们将学习 [模块系统](./04-modules.md)。
