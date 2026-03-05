# EST Plugin 插件模块

提供插件系统支持，包括插件接口、插件加载器和插件管理器。

## 模块结构

```
est-plugin/
├── est-plugin-api/      # 插件接口定义
├── est-plugin-impl/     # 插件实现
└── pom.xml
```

## 主要功能

### 插件接口

```java
import ltd.idcu.est.plugin.api.Plugin;

public interface Plugin {
    String getName();
    String getVersion();
    void onLoad(PluginContext context);
    void onEnable();
    void onDisable();
    void onUnload();
}
```

### 创建插件

```java
public class MyPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "MyPlugin";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public void onLoad(PluginContext context) {
        System.out.println("Plugin loaded");
    }
    
    @Override
    public void onEnable() {
        System.out.println("Plugin enabled");
    }
    
    @Override
    public void onDisable() {
        System.out.println("Plugin disabled");
    }
    
    @Override
    public void onUnload() {
        System.out.println("Plugin unloaded");
    }
}
```

### 插件管理器

```java
import ltd.idcu.est.plugin.api.PluginManager;
import ltd.idcu.est.plugin.impl.DefaultPluginManager;

// 创建插件管理器
PluginManager manager = new DefaultPluginManager();

// 加载插件
manager.loadPlugin(new MyPlugin());

// 从目录加载
manager.loadFromDirectory("./plugins");

// 从JAR加载
manager.loadFromJar("./plugins/my-plugin.jar");

// 启用所有插件
manager.enableAll();

// 获取插件
Optional<Plugin> plugin = manager.getPlugin("MyPlugin");

// 列出所有插件
List<Plugin> plugins = manager.getPlugins();
```

### 插件依赖

```java
@PluginDependency(plugin = "AnotherPlugin", version = ">=1.0.0")
public class MyPlugin implements Plugin {
    // ...
}
```

## 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-plugin-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```
