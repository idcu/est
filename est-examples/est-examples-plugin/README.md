# EST 插件系统示例

本示例展示了如何使用 EST Framework 的插件系统。

## 功能特性

- **插件生命周期管理**: 加载、初始化、启动、停止、卸载
- **插件依赖管理**: 支持插件之间的依赖关系
- **事件监听**: 监听插件的各种生命周期事件
- **插件统计**: 获取插件系统的运行统计信息
- **属性管理**: 插件之间可以共享属性

## 项目结构

```
est-examples-plugin/
├── src/main/java/ltd/idcu/est/examples/plugin/
│   ├── HelloPlugin.java          # 基础 Hello 插件
│   ├── GreetingPlugin.java       # 依赖 HelloPlugin 的问候插件
│   ├── LoggingPlugin.java        # 日志记录插件
│   └── PluginSystemExample.java  # 插件系统主示例
├── pom.xml
└── README.md
```

## 快速开始

### 运行示例

```bash
cd est-examples-plugin
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.examples.plugin.PluginSystemExample"
```

### 或在 IDE 中运行

直接运行 `PluginSystemExample.java` 的 `main` 方法。

## 示例说明

### 1. HelloPlugin

最简单的插件示例，展示了插件的基本结构和生命周期方法。

```java
public class HelloPlugin extends AbstractPlugin {
    public HelloPlugin() {
        super(PluginInfo.builder()
                .name("hello-plugin")
                .version("1.0.0")
                .description("一个简单的 Hello 插件")
                .author("EST Team")
                .build());
    }

    @Override
    public void onLoad() {
        System.out.println("[HelloPlugin] 插件已加载");
    }

    public String sayHello() {
        return "Hello from HelloPlugin!";
    }
}
```

### 2. LoggingPlugin

展示了插件的属性管理和状态管理功能。

```java
public class LoggingPlugin extends AbstractPlugin {
    @Override
    public void onLoad() {
        setAttribute("loadTime", System.currentTimeMillis());
    }

    public void log(String message) {
        // 记录日志
    }
}
```

### 3. GreetingPlugin

展示了插件之间的依赖关系。

```java
public class GreetingPlugin extends AbstractPlugin {
    public GreetingPlugin() {
        super(PluginInfo.builder()
                .name("greeting-plugin")
                .dependencies("hello-plugin")  // 依赖 HelloPlugin
                .build());
    }
}
```

## 插件系统 API

### PluginManager

核心插件管理器接口，提供以下功能：

```java
PluginManager pluginManager = new DefaultPluginManager();

// 从类加载插件
Plugin plugin = pluginManager.loadPluginFromClass(MyPlugin.class);

// 从路径加载插件
Plugin plugin = pluginManager.loadPlugin("/path/to/plugin.jar");

// 启动插件
pluginManager.startPlugin("plugin-name");

// 停止插件
pluginManager.stopPlugin("plugin-name");

// 卸载插件
pluginManager.unloadPlugin("plugin-name");

// 获取所有插件
List<Plugin> plugins = pluginManager.getPlugins();

// 获取统计信息
PluginStats stats = pluginManager.getStats();
```

### PluginListener

插件事件监听器：

```java
pluginManager.addListener(new PluginListener() {
    @Override
    public void onPluginLoaded(Plugin plugin) {
        System.out.println("插件已加载: " + plugin.getName());
    }

    @Override
    public void onPluginStarted(Plugin plugin) {
        System.out.println("插件已启动: " + plugin.getName());
    }

    // 其他生命周期方法...
});
```

### Plugin 生命周期

插件有以下状态：

- `LOADED` - 已加载
- `INITIALIZED` - 已初始化
- `STARTING` - 启动中
- `RUNNING` - 运行中
- `STOPPING` - 停止中
- `STOPPED` - 已停止
- `ERROR` - 错误状态

## 创建自己的插件

1. 继承 `AbstractPlugin` 类
2. 在构造函数中提供 `PluginInfo`
3. 重写需要的生命周期方法
4. 实现自己的业务逻辑

```java
public class MyPlugin extends AbstractPlugin {
    public MyPlugin() {
        super(PluginInfo.builder()
                .name("my-plugin")
                .version("1.0.0")
                .description("我的插件")
                .author("My Name")
                .mainClass(MyPlugin.class.getName())
                .category("工具")
                .tags("my", "plugin")
                .minFrameworkVersion("2.3.0")
                .build());
    }

    @Override
    public void onEnable() {
        System.out.println("我的插件已启用!");
    }

    public void doSomething() {
        System.out.println("做一些事情...");
    }
}
```

## 更多信息

- 查看 EST Framework 文档了解更多插件系统功能
- 参考 `est-tools/est-code-cli` 中的插件实现
