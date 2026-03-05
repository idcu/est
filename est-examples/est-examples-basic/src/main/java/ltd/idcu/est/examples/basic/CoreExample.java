package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.api.lifecycle.InitializingBean;
import ltd.idcu.est.core.api.lifecycle.DisposableBean;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.lifecycle.PreDestroy;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.AbstractModule;

public class CoreExample {
    public static void run() {
        System.out.println("=== EST 框架核心功能示例 ===");
        System.out.println("本示例将展示 EST 框架的核心功能，包括依赖注入、配置管理和模块系统");
        System.out.println("适合初学者理解框架的基本原理\n");
        
        System.out.println("=".repeat(60));
        System.out.println("第一部分：依赖注入容器");
        System.out.println("=".repeat(60));
        
        System.out.println("\n【方式一】最基础的容器使用 - 手动注册和获取");
        basicContainerExample();
        
        System.out.println("\n【方式二】接口与实现分离 - 更符合工程实践");
        interfaceImplementationExample();
        
        System.out.println("\n【方式三】不同作用域的 Bean - Singleton 和 Prototype");
        scopeExample();
        
        System.out.println("\n【方式四】Supplier 注册 - 灵活的实例创建");
        supplierExample();
        
        System.out.println("\n【方式五】生命周期管理 - Bean 的初始化和销毁");
        lifecycleExample();
        
        System.out.println("\n=".repeat(60));
        System.out.println("第二部分：配置管理");
        System.out.println("=".repeat(60));
        
        configExample();
        
        System.out.println("\n=".repeat(60));
        System.out.println("第三部分：模块系统");
        System.out.println("=".repeat(60));
        
        moduleExample();
        
        System.out.println("\n=".repeat(60));
        System.out.println("核心示例运行完成！");
        System.out.println("接下来请查看其他示例文件，了解更多功能");
        System.out.println("=".repeat(60));
    }
    
    private static void basicContainerExample() {
        System.out.println("\n--- 基础容器使用 ---");
        System.out.println("这是最简单的使用方式，适合快速入门");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. 注册单例对象（Singleton）");
        System.out.println("   - Singleton 意味着整个应用中只有一个实例");
        container.registerSingleton(String.class, "你好，EST！");
        container.registerSingleton(Integer.class, 42);
        
        System.out.println("\n2. 从容器中获取对象");
        String message = container.get(String.class);
        Integer number = container.get(Integer.class);
        
        System.out.println("   - 获取到的字符串: " + message);
        System.out.println("   - 获取到的数字: " + number);
        
        System.out.println("\n✓ 基础容器示例完成\n");
    }
    
    private static void interfaceImplementationExample() {
        System.out.println("\n--- 接口与实现分离 ---");
        System.out.println("在实际项目中，我们通常定义接口，然后编写实现类");
        System.out.println("这样做的好处是：解耦、便于测试、易于替换实现\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. 定义服务接口（这里是 UserService）");
        System.out.println("2. 编写实现类（UserServiceImpl）");
        System.out.println("3. 在容器中注册：接口 -> 实现类");
        
        container.register(UserService.class, UserServiceImpl.class);
        
        System.out.println("\n4. 从容器获取服务（通过接口类型获取）");
        UserService userService = container.get(UserService.class);
        
        System.out.println("5. 使用服务");
        String userName = userService.getUserName("1001");
        System.out.println("   - 用户 ID 1001 的名字: " + userName);
        
        System.out.println("\n✓ 接口与实现示例完成\n");
    }
    
    private static void scopeExample() {
        System.out.println("\n--- Bean 作用域示例 ---");
        System.out.println("EST 支持两种主要作用域：");
        System.out.println("  - SINGLETON（单例）：每次获取都是同一个实例");
        System.out.println("  - PROTOTYPE（原型）：每次获取都创建新实例\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. 注册单例作用域的服务（默认就是 SINGLETON）");
        container.register(CounterService.class, CounterService.class, Scope.SINGLETON, "singleton-counter");
        
        System.out.println("2. 注册原型作用域的服务");
        container.register(CounterService.class, CounterService.class, Scope.PROTOTYPE, "prototype-counter");
        
        System.out.println("\n3. 测试单例作用域");
        CounterService singleton1 = container.get(CounterService.class, "singleton-counter");
        CounterService singleton2 = container.get(CounterService.class, "singleton-counter");
        singleton1.increment();
        singleton2.increment();
        System.out.println("   - singleton1 计数: " + singleton1.getCount());
        System.out.println("   - singleton2 计数: " + singleton2.getCount());
        System.out.println("   - 两者相同，因为是同一个实例");
        
        System.out.println("\n4. 测试原型作用域");
        CounterService prototype1 = container.get(CounterService.class, "prototype-counter");
        CounterService prototype2 = container.get(CounterService.class, "prototype-counter");
        prototype1.increment();
        prototype2.increment();
        System.out.println("   - prototype1 计数: " + prototype1.getCount());
        System.out.println("   - prototype2 计数: " + prototype2.getCount());
        System.out.println("   - 两者不同，因为是不同的实例");
        
        System.out.println("\n✓ 作用域示例完成\n");
    }
    
    private static void supplierExample() {
        System.out.println("\n--- Supplier 注册示例 ---");
        System.out.println("使用 Supplier 可以灵活控制实例的创建过程");
        System.out.println("适合需要复杂初始化逻辑的场景\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. 使用 Supplier 注册服务");
        System.out.println("   - 可以在 Supplier 中添加任意初始化逻辑");
        container.registerSupplier(ConfiguredService.class, () -> {
            ConfiguredService service = new ConfiguredService();
            service.setDatabaseUrl("jdbc:mysql://localhost:3306/mydb");
            service.setMaxConnections(10);
            return service;
        });
        
        System.out.println("\n2. 获取并使用服务");
        ConfiguredService service = container.get(ConfiguredService.class);
        System.out.println("   - 数据库 URL: " + service.getDatabaseUrl());
        System.out.println("   - 最大连接数: " + service.getMaxConnections());
        
        System.out.println("\n✓ Supplier 示例完成\n");
    }
    
    private static void lifecycleExample() {
        System.out.println("\n--- 生命周期管理示例 ---");
        System.out.println("EST 提供了多种方式管理 Bean 的生命周期：");
        System.out.println("  1. 实现 InitializingBean 和 DisposableBean 接口");
        System.out.println("  2. 使用 @PostConstruct 和 @PreDestroy 注解\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. 注册带有生命周期回调的 Bean");
        container.register(LifecycleService.class, LifecycleService.class);
        
        System.out.println("\n2. 获取 Bean（会自动触发初始化）");
        LifecycleService service = container.get(LifecycleService.class);
        service.doWork();
        
        System.out.println("\n3. 关闭容器（会自动触发销毁）");
        container.close();
        
        System.out.println("\n✓ 生命周期示例完成\n");
    }
    
    private static void configExample() {
        System.out.println("\n--- 配置管理示例 ---");
        System.out.println("Config 用于管理应用的配置信息");
        System.out.println("支持多种数据类型：String、int、boolean 等\n");
        
        Config config = new DefaultConfig();
        
        System.out.println("1. 设置配置项");
        config.set("app.name", "我的 EST 应用");
        config.set("app.version", "1.0.0");
        config.set("app.debug", true);
        config.set("server.port", 8080);
        config.set("database.maxPoolSize", 20);
        
        System.out.println("\n2. 获取配置项");
        String appName = config.getString("app.name");
        String appVersion = config.getString("app.version");
        boolean debugMode = config.getBoolean("app.debug", false);
        int port = config.getInt("server.port", 80);
        int maxPoolSize = config.getInt("database.maxPoolSize", 10);
        String defaultValue = config.getString("non.existent.key", "这是默认值");
        
        System.out.println("   - 应用名称: " + appName);
        System.out.println("   - 应用版本: " + appVersion);
        System.out.println("   - 调试模式: " + debugMode);
        System.out.println("   - 服务器端口: " + port);
        System.out.println("   - 数据库连接池大小: " + maxPoolSize);
        System.out.println("   - 不存在的配置（使用默认值）: " + defaultValue);
        
        System.out.println("\n✓ 配置管理示例完成\n");
    }
    
    private static void moduleExample() {
        System.out.println("\n--- 模块系统示例 ---");
        System.out.println("Module 用于组织大型应用的功能模块");
        System.out.println("每个模块都有自己的生命周期：初始化 -> 启动 -> 停止\n");
        
        System.out.println("1. 创建一个自定义模块");
        Module module = new AbstractModule("示例模块", "1.0.0") {
            @Override
            protected void doInitialize() {
                System.out.println("   [模块生命周期] 正在初始化...");
                System.out.println("   - 加载配置文件");
                System.out.println("   - 建立数据库连接");
            }

            @Override
            protected void doStart() {
                System.out.println("   [模块生命周期] 正在启动...");
                System.out.println("   - 启动后台任务");
                System.out.println("   - 开始监听事件");
            }

            @Override
            protected void doStop() {
                System.out.println("   [模块生命周期] 正在停止...");
                System.out.println("   - 停止后台任务");
                System.out.println("   - 关闭数据库连接");
            }
        };
        
        System.out.println("\n2. 执行模块生命周期");
        System.out.println("\n   步骤 1: 初始化模块");
        module.initialize();
        
        System.out.println("\n   步骤 2: 启动模块");
        module.start();
        
        System.out.println("\n   步骤 3: 停止模块");
        module.stop();
        
        System.out.println("\n✓ 模块系统示例完成\n");
    }
}

interface UserService {
    String getUserName(String id);
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserName(String id) {
        return "用户" + id;
    }
}

class CounterService {
    private int count = 0;
    
    public void increment() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
}

class ConfiguredService {
    private String databaseUrl;
    private int maxConnections;
    
    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }
    
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }
    
    public String getDatabaseUrl() {
        return databaseUrl;
    }
    
    public int getMaxConnections() {
        return maxConnections;
    }
}

class LifecycleService implements InitializingBean, DisposableBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("   [生命周期] InitializingBean.afterPropertiesSet() 被调用");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("   [生命周期] @PostConstruct 注解的方法被调用");
    }
    
    public void doWork() {
        System.out.println("   [生命周期] 执行业务逻辑...");
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("   [生命周期] @PreDestroy 注解的方法被调用");
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println("   [生命周期] DisposableBean.destroy() 被调用");
    }
}
