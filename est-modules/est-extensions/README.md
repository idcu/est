# EST Extensions 扩展模块 - 小白从入门到精通

## 目录
1. [什么是 EST Extensions？](#什么是-est-extensions)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Extensions？

### 用大白话理解

EST Extensions 就像是一个"工具箱扩展包"。想象一下你有基本的工具，但还需要一些特殊的功能：

**传统方式**：定时任务要自己写定时器，插件系统要自己设计，热重载要自己实现... 很麻烦！

**EST Extensions 方式**：给你一套扩展工具，里面有：
- ⏰ **定时调度** - 支持 Cron 表达式和固定时间间隔
- 🔌 **插件系统** - 动态加载和管理插件
- 🔥 **热重载** - 代码修改后自动重新加载

### 核心特点

- 🎯 **简单易用** - 几行代码就能启用定时任务、插件系统
- ⚡ **高性能** - 优化的定时调度，低开销的插件加载
- 🔧 **灵活扩展** - 可以自定义调度策略、插件加载器
- 🎨 **功能完整** - 调度、插件、热重载一应俱全

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-scheduler</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-plugin</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个定时任务

```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.CronExpression;
import ltd.idcu.est.scheduler.FixedRate;

public class FirstExtensionApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Extensions 第一个示例 ===\n");
        
        Scheduler scheduler = Scheduler.create();
        
        scheduler.schedule(new FixedRate(5000), () -> {
            System.out.println("每 5 秒执行一次: " + LocalDateTime.now());
        });
        
        scheduler.schedule(new CronExpression("0 * * * * ?"), () -> {
            System.out.println("每分钟执行一次: " + LocalDateTime.now());
        });
        
        System.out.println("定时任务已启动");
        Thread.sleep(60000);
        scheduler.shutdown();
    }
}
```

---

## 基础篇

### 1. est-scheduler 定时调度

#### Cron 表达式

```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.CronExpression;

Scheduler scheduler = Scheduler.create();

scheduler.schedule(new CronExpression("0 0 12 * * ?"), () -> {
    System.out.println("每天中午 12 点执行");
});

scheduler.schedule(new CronExpression("0 0 8-18 * * ?"), () -> {
    System.out.println("每天 8 点到 18 点，每小时执行");
});

scheduler.schedule(new CronExpression("0 0/5 * * * ?"), () -> {
    System.out.println("每 5 分钟执行");
});
```

#### 固定时间间隔

```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.FixedRate;
import ltd.idcu.est.scheduler.FixedDelay;

Scheduler scheduler = Scheduler.create();

scheduler.schedule(new FixedRate(1000), () -> {
    System.out.println("固定频率：每 1 秒执行一次（不等待上次完成）");
});

scheduler.schedule(new FixedDelay(2000), () -> {
    System.out.println("固定延迟：上次完成后 2 秒再执行");
});
```

#### 一次性任务

```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.OneTime;

Scheduler scheduler = Scheduler.create();

scheduler.schedule(new OneTime(5000), () -> {
    System.out.println("5 秒后执行一次");
});

scheduler.schedule(new OneTime(LocalDateTime.now().plusMinutes(1)), () -> {
    System.out.println("1 分钟后执行一次");
});
```

### 2. est-plugin 插件系统

#### 定义插件

```java
import ltd.idcu.est.plugin.Plugin;
import ltd.idcu.est.plugin.PluginContext;

public interface MyPlugin extends Plugin {
    void doSomething();
}

public class MyPluginImpl implements MyPlugin {
    
    @Override
    public String getName() {
        return "my-plugin";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public void initialize(PluginContext context) {
        System.out.println("插件初始化: " + getName());
    }
    
    @Override
    public void destroy() {
        System.out.println("插件销毁: " + getName());
    }
    
    @Override
    public void doSomething() {
        System.out.println("插件执行操作");
    }
}
```

#### 加载插件

```java
import ltd.idcu.est.plugin.PluginManager;
import ltd.idcu.est.plugin.PluginLoader;

PluginManager manager = PluginManager.create();

PluginLoader loader = manager.getPluginLoader();
loader.loadPlugin(Paths.get("./plugins/my-plugin.jar"));

MyPlugin plugin = manager.getPlugin("my-plugin", MyPlugin.class);
plugin.doSomething();

manager.unloadPlugin("my-plugin");
```

#### 插件生命周期

```java
import ltd.idcu.est.plugin.Plugin;
import ltd.idcu.est.plugin.PluginContext;

public class LifecyclePlugin implements Plugin {
    
    @Override
    public void initialize(PluginContext context) {
        System.out.println("插件初始化");
        context.setAttribute("key", "value");
    }
    
    @Override
    public void start() {
        System.out.println("插件启动");
    }
    
    @Override
    public void stop() {
        System.out.println("插件停止");
    }
    
    @Override
    public void destroy() {
        System.out.println("插件销毁");
    }
}
```

### 3. est-hotreload 热重载

#### 启用热重载

```java
import ltd.idcu.est.hotreload.HotReloader;
import ltd.idcu.est.hotreload.ReloadListener;

HotReloader reloader = HotReloader.create();

reloader.addListener(new ReloadListener() {
    @Override
    public void onReload(Class<?> clazz) {
        System.out.println("类已重新加载: " + clazz.getName());
    }
});

reloader.watch(Paths.get("./target/classes"));
reloader.start();
```

---

## 进阶篇

### 1. 自定义调度策略

```java
import ltd.idcu.est.scheduler.Scheduler;
import ltd.idcu.est.scheduler.SchedulingStrategy;
import ltd.idcu.est.scheduler.Task;

public class CustomSchedulingStrategy implements SchedulingStrategy {
    
    @Override
    public void schedule(Task task) {
        // 自定义调度逻辑
    }
    
    @Override
    public void cancel(String taskId) {
        // 取消任务
    }
}

Scheduler scheduler = Scheduler.create();
scheduler.setStrategy(new CustomSchedulingStrategy());
```

### 2. 插件依赖管理

```java
import ltd.idcu.est.plugin.Plugin;
import ltd.idcu.est.plugin.PluginDependency;
import ltd.idcu.est.plugin.DependencyResolver;

public class DependentPlugin implements Plugin {
    
    @Override
    public PluginDependency[] getDependencies() {
        return new PluginDependency[]{
            new PluginDependency("core-plugin", "1.0.0"),
            new PluginDependency("utils-plugin", "2.0.0")
        };
    }
}

PluginManager manager = PluginManager.create();
manager.setDependencyResolver(new DependencyResolver() {
    @Override
    public boolean resolve(PluginDependency dependency) {
        return manager.getPlugin(dependency.getName()) != null;
    }
});
```

### 3. 热重载配置

```java
import ltd.idcu.est.hotreload.HotReloader;
import ltd.idcu.est.hotreload.HotReloadConfig;

HotReloadConfig config = HotReloadConfig.builder()
    .watchInterval(1000) // 1 秒检查一次
    .includePackages("ltd.idcu.est.demo")
    .excludePackages("ltd.idcu.est.demo.excluded")
    .build();

HotReloader reloader = HotReloader.create(config);
reloader.watch(Paths.get("./src/main/java"));
reloader.start();
```

---

## 最佳实践

### 1. 定时任务异常处理

```java
// ✅ 推荐：捕获异常，记录日志
scheduler.schedule(new FixedRate(5000), () -> {
    try {
        doSomething();
    } catch (Exception e) {
        System.err.println("定时任务执行失败: " + e.getMessage());
    }
});

// ❌ 不推荐：不处理异常
scheduler.schedule(new FixedRate(5000), () -> {
    doSomething(); // 异常会导致任务停止
});
```

### 2. 插件版本管理

```java
// ✅ 推荐：明确版本要求
public class MyPlugin implements Plugin {
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public PluginDependency[] getDependencies() {
        return new PluginDependency[]{
            new PluginDependency("core-plugin", ">=1.0.0,<2.0.0")
        };
    }
}

// ❌ 不推荐：不指定版本
public class BadPlugin implements Plugin {
    @Override
    public String getVersion() {
        return "unknown";
    }
}
```

### 3. 热重载使用场景

```java
// ✅ 推荐：开发环境使用热重载
if (isDevelopment) {
    HotReloader reloader = HotReloader.create();
    reloader.watch(Paths.get("./src"));
    reloader.start();
}

// ❌ 不推荐：生产环境使用热重载
// 生产环境应该使用完整的部署流程
```

---

## 模块结构

```
est-extensions/
├── est-scheduler/    # 定时调度（Cron、Fixed）
├── est-plugin/       # 插件系统
└── est-hotreload/    # 热重载
```

---

## 相关资源

- [est-scheduler README](./est-scheduler/README.md) - 定时调度详细文档
- [est-plugin README](./est-plugin/README.md) - 插件系统详细文档
- [示例代码](../../est-examples/est-examples-advanced/) - 高级示例
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
