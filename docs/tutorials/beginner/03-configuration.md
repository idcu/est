# 教程 03: 配置管理

在本教程中，你将学习如何使用 EST 的配置管理功能！

---

## 🎯 学习目标

完成本教程后，你将学会：
- ✅ 如何设置和获取配置
- ✅ 支持的数据类型
- ✅ 如何在 Web 应用中使用配置
- ✅ 配置的最佳实践

---

## ⚙️ 基础用法

### 从 Web 应用获取配置

在 EST Web 应用中，配置对象可以直接从 `WebApplication` 获取：

```java
package com.example.config;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.core.api.Config;

public class BasicConfigExample {
    public static void main(String[] args) {
        // 创建 Web 应用
        WebApplication app = Web.create("配置示例", "1.0.0");
        Config config = app.getConfig();
        
        // ==========================================
        // 1. 设置配置
        // ==========================================
        
        config.set("app.name", "我的应用");
        config.set("app.version", "1.0.0");
        config.set("server.port", 8080);
        config.set("debug", true);
        config.set("timeout", 30000L);
        config.set("maxConnections", 100);
        config.set("ratio", 0.75);
        
        // ==========================================
        // 2. 获取配置
        // ==========================================
        
        // 获取字符串
        String appName = config.getString("app.name");
        String appVersion = config.getString("app.version", "0.0.1"); // 默认值
        
        // 获取整数
        int port = config.getInt("server.port");
        int portWithDefault = config.getInt("server.port", 80);
        
        // 获取布尔值
        boolean debug = config.getBoolean("debug");
        boolean debugWithDefault = config.getBoolean("debug", false);
        
        // 获取长整型
        long timeout = config.getLong("timeout");
        long timeoutWithDefault = config.getLong("timeout", 30000L);
        
        // 获取双精度浮点数
        double ratio = config.getDouble("ratio");
        double ratioWithDefault = config.getDouble("ratio", 0.5);
        
        // 获取整数
        int maxConn = config.getInt("maxConnections", 50);
        
        // ==========================================
        // 3. 使用配置
        // ==========================================
        
        System.out.println("应用名称：" + appName);
        System.out.println("版本：" + appVersion);
        System.out.println("端口：" + port);
        System.out.println("调试模式：" + (debug ? "开启" : "关闭"));
        System.out.println("超时时间：" + timeout + "ms");
        System.out.println("最大连接数：" + maxConn);
        System.out.println("比率：" + ratio);
        
        // 在路由中使用配置
        app.get("/api/config", (req, res) -> {
            res.json(java.util.Map.of(
                "appName", config.getString("app.name"),
                "version", config.getString("app.version"),
                "debug", config.getBoolean("debug")
            ));
        });
        
        app.onStartup(() -> {
            System.out.println("\n🚀 " + appName + " 启动成功！");
            System.out.println("📍 访问地址：http://localhost:" + port);
        });
        
        app.run(port);
    }
}
```

### 运行结果

```
应用名称：我的应用
版本：1.0.0
端口：8080
调试模式：开启
超时时间：30000ms
最大连接数：100
比率：0.75

🚀 我的应用 启动成功！
📍 访问地址：http://localhost:8080
```

---

## 📊 支持的数据类型

EST 的配置系统支持多种数据类型：

| 数据类型 | 设置方法 | 获取方法 | 示例 |
|---------|----------|----------|------|
| String | `config.set("key", "value")` | `config.getString("key")` | `"hello"` |
| int | `config.set("key", 123)` | `config.getInt("key")` | `123` |
| long | `config.set("key", 123456789L)` | `config.getLong("key")` | `123456789L` |
| double | `config.set("key", 3.14)` | `config.getDouble("key")` | `3.14` |
| boolean | `config.set("key", true)` | `config.getBoolean("key")` | `true` / `false` |
| Object | `config.set("key", obj)` | `config.get("key")` | 任意对象 |

### 完整示例

```java
package com.example.config;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.core.api.Config;

public class AllTypesExample {
    public static void main(String[] args) {
        WebApplication app = Web.create("类型示例", "1.0.0");
        Config config = app.getConfig();
        
        // 设置各种类型的配置
        config.set("string.key", "Hello, EST!");
        config.set("int.key", 42);
        config.set("long.key", 1234567890123L);
        config.set("double.key", 3.1415926);
        config.set("boolean.key", true);
        
        // 获取并打印
        System.out.println("字符串: " + config.getString("string.key"));
        System.out.println("整数: " + config.getInt("int.key"));
        System.out.println("长整型: " + config.getLong("long.key"));
        System.out.println("浮点数: " + config.getDouble("double.key"));
        System.out.println("布尔值: " + config.getBoolean("boolean.key"));
        
        // 使用默认值（不存在的键）
        System.out.println("\n不存在的键（带默认值）:");
        System.out.println("字符串: " + config.getString("nonexistent", "默认值"));
        System.out.println("整数: " + config.getInt("nonexistent", 100));
        System.out.println("布尔值: " + config.getBoolean("nonexistent", false));
    }
}
```

---

## 🎛️ 在 Web 应用中使用配置

让我们看一个更实际的例子，在 Web 应用中使用配置：

### 示例：可配置的 Web 应用

```java
package com.example.config;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.core.api.Config;

public class ConfigurableWebApp {
    public static void main(String[] args) {
        // 创建应用
        WebApplication app = Web.create("可配置应用", "1.0.0");
        Config config = app.getConfig();
        
        // ==========================================
        // 1. 配置应用
        // ==========================================
        
        // 应用配置
        config.set("app.name", "我的在线商店");
        config.set("app.description", "一个简单的在线商店");
        config.set("app.currency", "CNY");
        
        // 服务器配置
        config.set("server.port", 8080);
        config.set("server.host", "0.0.0.0");
        
        // 业务配置
        config.set("shop.taxRate", 0.13);
        config.set("shop.maxCartItems", 100);
        config.set("shop.enableDiscounts", true);
        
        // ==========================================
        // 2. 从配置读取
        // ==========================================
        
        int port = config.getInt("server.port", 8080);
        double taxRate = config.getDouble("shop.taxRate", 0.10);
        int maxCartItems = config.getInt("shop.maxCartItems", 50);
        boolean enableDiscounts = config.getBoolean("shop.enableDiscounts", false);
        String currency = config.getString("app.currency", "CNY");
        
        // ==========================================
        // 3. 创建路由
        // ==========================================
        
        // 首页 - 显示应用信息
        app.get("/", (req, res) -> {
            res.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>%s</title>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 40px; }
                        h1 { color: #2c3e50; }
                        .config { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-top: 20px; }
                        .config-item { margin: 10px 0; }
                    </style>
                </head>
                <body>
                    <h1>🎉 %s</h1>
                    <p>%s</p>
                    
                    <div class="config">
                        <h3>⚙️ 当前配置</h3>
                        <div class="config-item">💰 货币：%s</div>
                        <div class="config-item">📊 税率：%.0f%%</div>
                        <div class="config-item">🛒 最大购物车：%d 件</div>
                        <div class="config-item">🎁 折扣功能：%s</div>
                    </div>
                </body>
                </html>
            """.formatted(
                config.getString("app.name"),
                config.getString("app.name"),
                config.getString("app.description"),
                currency,
                taxRate * 100,
                maxCartItems,
                enableDiscounts ? "已启用" : "未启用"
            ));
        });
        
        // API - 计算价格（使用税率配置）
        app.get("/api/calculate-price", (req, res) -> {
            double price = Double.parseDouble(req.queryParam("price", "0"));
            
            double tax = price * taxRate;
            double total = price + tax;
            
            // 如果启用折扣，满100减10
            if (enableDiscounts && total >= 100) {
                total -= 10;
            }
            
            res.json(java.util.Map.of(
                "originalPrice", price,
                "tax", String.format("%.2f", tax),
                "discount", enableDiscounts && total + 10 >= 100 ? 10 : 0,
                "totalPrice", String.format("%.2f", total),
                "currency", currency
            ));
        });
        
        // API - 获取所有配置
        app.get("/api/config", (req, res) -> {
            res.json(java.util.Map.of(
                "app", java.util.Map.of(
                    "name", config.getString("app.name"),
                    "description", config.getString("app.description"),
                    "currency", currency
                ),
                "shop", java.util.Map.of(
                    "taxRate", taxRate,
                    "maxCartItems", maxCartItems,
                    "enableDiscounts", enableDiscounts
                )
            ));
        });
        
        // ==========================================
        // 4. 启动应用
        // ==========================================
        
        app.onStartup(() -> {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("  🚀 " + config.getString("app.name") + " 已启动！");
            System.out.println("  📍 访问地址：http://localhost:" + port);
            System.out.println("  🧮 测试价格计算：http://localhost:" + port + "/api/calculate-price?price=100");
            System.out.println("=".repeat(50) + "\n");
        });
        
        app.run(port);
    }
}
```

### 测试这个应用

运行应用后，你可以：

1. **访问首页**：http://localhost:8080
   - 会显示应用信息和当前配置

2. **测试价格计算**：http://localhost:8080/api/calculate-price?price=100
   - 会计算包含税费和折扣的总价

3. **查看所有配置**：http://localhost:8080/api/config
   - 会返回 JSON 格式的所有配置

---

## 🔍 检查配置是否存在

你可以检查某个配置键是否存在：

```java
// 检查配置是否存在
if (config.contains("app.name")) {
    System.out.println("app.name 配置存在");
} else {
    System.out.println("app.name 配置不存在");
}
```

---

## 🗑️ 移除配置

你可以移除不需要的配置：

```java
// 设置配置
config.set("temp.key", "临时值");
System.out.println("设置后：" + config.getString("temp.key"));

// 移除配置
config.remove("temp.key");
System.out.println("移除后：" + config.getString("temp.key", "已移除"));
```

---

## 📦 获取所有配置键

你可以获取所有已设置的配置键：

```java
import java.util.Set;

Set<String> keys = config.keys();

System.out.println("所有配置键：");
for (String key : keys) {
    System.out.println("- " + key);
}
```

---

## ✅ 最佳实践

### 1. 使用带默认值的 getter

```java
// ✅ 好：总是提供默认值
int port = config.getInt("server.port", 8080);
boolean debug = config.getBoolean("debug", false);
String currency = config.getString("app.currency", "CNY");

// ❌ 不好：不提供默认值（可能抛出异常）
int port = config.getInt("server.port");
```

### 2. 使用配置常量

```java
// ✅ 好：定义配置键的常量
public class AppConfigKeys {
    public static final String APP_NAME = "app.name";
    public static final String SERVER_PORT = "server.port";
    public static final String DEBUG_MODE = "debug";
}

// 使用常量
config.set(AppConfigKeys.APP_NAME, "我的应用");
String appName = config.getString(AppConfigKeys.APP_NAME, "默认应用");
```

### 3. 分组相关配置

```java
// ✅ 好：用点号分组
config.set("app.name", "我的应用");
config.set("app.version", "1.0.0");
config.set("server.port", 8080);
config.set("server.host", "0.0.0.0");
config.set("database.url", "jdbc:mysql://...");
config.set("database.username", "root");

// ❌ 不好：没有分组
config.set("name", "我的应用");
config.set("version", "1.0.0");
config.set("port", 8080);
```

### 4. 验证配置值

```java
// ✅ 好：验证配置值的有效性
int port = config.getInt("server.port", 8080);

if (port < 1 || port > 65535) {
    throw new IllegalArgumentException("端口必须在 1-65535 之间：" + port);
}

boolean debug = config.getBoolean("debug", false);
```

### 5. 在启动时打印配置

```java
// ✅ 好：启动时打印重要配置
app.onStartup(() -> {
    System.out.println("=== 应用配置 ===");
    System.out.println("应用名称：" + config.getString("app.name"));
    System.out.println("版本：" + config.getString("app.version"));
    System.out.println("端口：" + config.getInt("server.port"));
    System.out.println("调试模式：" + (config.getBoolean("debug") ? "开启" : "关闭"));
    System.out.println("==============");
});
```

---

## 💡 小练习

试试完成以下练习：

1. **添加更多配置**：添加 `app.theme`、`shop.discountThreshold` 等配置
2. **创建配置页面**：创建一个网页，显示所有配置并允许修改
3. **配置验证**：添加代码验证所有配置值的有效性
4. **多环境配置**：创建一个简单的多环境配置系统（开发、测试、生产）

---

## 🎓 下一步

恭喜你！你已经学会了使用配置管理！

你现在已经完成了所有的入门教程！接下来，你可以：

- 学习 [Web 开发教程](../web/01-basic-web-app.md) - 深入学习 Web 开发
- 查阅 [API 参考](../api/README.md) - 了解更多 API
- 探索 [示例代码](../../est-examples/) - 查看更多示例
- 阅读 [最佳实践](../best-practices/README.md) - 提升代码质量

---

**入门教程完成！你太棒了！** 🎉🎊
