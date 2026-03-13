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
        System.out.println("=== EST Framework Core Feature Example ===");
        System.out.println("This example demonstrates EST Framework core features including DI, config, and modules");
        System.out.println("Good for beginners to understand the framework's basic principles\n");
        
        System.out.println("=".repeat(60));
        System.out.println("Part 1: Dependency Injection Container");
        System.out.println("=".repeat(60));
        
        System.out.println("\n[Method 1] Basic container usage - manual registration and retrieval");
        basicContainerExample();
        
        System.out.println("\n[Method 2] Interface and implementation separation - more engineering practice");
        interfaceImplementationExample();
        
        System.out.println("\n[Method 3] Different scope beans - Singleton and Prototype");
        scopeExample();
        
        System.out.println("\n[Method 4] Supplier registration - flexible instance creation");
        supplierExample();
        
        System.out.println("\n[Method 5] Lifecycle management - Bean initialization and destruction");
        lifecycleExample();
        
        System.out.println("\n=".repeat(60));
        System.out.println("Part 2: Configuration Management");
        System.out.println("=".repeat(60));
        
        configExample();
        
        System.out.println("\n=".repeat(60));
        System.out.println("Part 3: Module System");
        System.out.println("=".repeat(60));
        
        moduleExample();
        
        System.out.println("\n=".repeat(60));
        System.out.println("Core example completed!");
        System.out.println("Please check other example files for more features");
        System.out.println("=".repeat(60));
    }
    
    private static void basicContainerExample() {
        System.out.println("\n--- Basic Container Usage ---");
        System.out.println("This is the simplest way, good for quick start\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. Register singleton objects");
        System.out.println("   - Singleton means only one instance in the entire application");
        container.registerSingleton(String.class, "Hello EST!");
        container.registerSingleton(Integer.class, 42);
        
        System.out.println("\n2. Get objects from container");
        String message = container.get(String.class);
        Integer number = container.get(Integer.class);
        
        System.out.println("   - Retrieved string: " + message);
        System.out.println("   - Retrieved number: " + number);
        
        System.out.println("\n[OK] Basic container example completed\n");
    }
    
    private static void interfaceImplementationExample() {
        System.out.println("\n--- Interface and Implementation Separation ---");
        System.out.println("In real projects, we usually define interfaces and implement classes");
        System.out.println("Benefits: decoupling, easy testing, easy implementation replacement\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. Define service interface (here is UserService)");
        System.out.println("2. Write implementation class (UserServiceImpl)");
        System.out.println("3. Register in container: interface -> implementation");
        
        container.register(UserService.class, UserServiceImpl.class);
        
        System.out.println("\n4. Get service from container (via interface type)");
        UserService userService = container.get(UserService.class);
        
        System.out.println("5. Use service");
        String userName = userService.getUserName("1001");
        System.out.println("   - User ID 1001's name: " + userName);
        
        System.out.println("\n[OK] Interface and implementation example completed\n");
    }
    
    private static void scopeExample() {
        System.out.println("\n--- Bean Scope Example ---");
        System.out.println("EST supports two main scopes:");
        System.out.println("  - SINGLETON: same instance every time");
        System.out.println("  - PROTOTYPE: new instance every time\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. Register singleton scope service (default is SINGLETON)");
        container.register(CounterService.class, CounterService.class, Scope.SINGLETON, "singleton-counter");
        
        System.out.println("2. Register prototype scope service");
        container.register(CounterService.class, CounterService.class, Scope.PROTOTYPE, "prototype-counter");
        
        System.out.println("\n3. Test singleton scope");
        CounterService singleton1 = container.get(CounterService.class, "singleton-counter");
        CounterService singleton2 = container.get(CounterService.class, "singleton-counter");
        singleton1.increment();
        singleton2.increment();
        System.out.println("   - singleton1 count: " + singleton1.getCount());
        System.out.println("   - singleton2 count: " + singleton2.getCount());
        System.out.println("   - Both are same, because it's the same instance");
        
        System.out.println("\n4. Test prototype scope");
        CounterService prototype1 = container.get(CounterService.class, "prototype-counter");
        CounterService prototype2 = container.get(CounterService.class, "prototype-counter");
        prototype1.increment();
        prototype2.increment();
        System.out.println("   - prototype1 count: " + prototype1.getCount());
        System.out.println("   - prototype2 count: " + prototype2.getCount());
        System.out.println("   - Both are different, because they are different instances");
        
        System.out.println("\n[OK] Scope example completed\n");
    }
    
    private static void supplierExample() {
        System.out.println("\n--- Supplier Registration Example ---");
        System.out.println("Using Supplier can flexibly control instance creation");
        System.out.println("Good for scenarios requiring complex initialization logic\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. Register service using Supplier");
        System.out.println("   - Can add any initialization logic in Supplier");
        container.registerSupplier(ConfiguredService.class, () -> {
            ConfiguredService service = new ConfiguredService();
            service.setDatabaseUrl("jdbc:mysql://localhost:3306/mydb");
            service.setMaxConnections(10);
            return service;
        });
        
        System.out.println("\n2. Get and use service");
        ConfiguredService service = container.get(ConfiguredService.class);
        System.out.println("   - Database URL: " + service.getDatabaseUrl());
        System.out.println("   - Max connections: " + service.getMaxConnections());
        
        System.out.println("\n[OK] Supplier example completed\n");
    }
    
    private static void lifecycleExample() {
        System.out.println("\n--- Lifecycle Management Example ---");
        System.out.println("EST provides multiple ways to manage Bean lifecycle:");
        System.out.println("  1. Implement InitializingBean and DisposableBean interfaces");
        System.out.println("  2. Use @PostConstruct and @PreDestroy annotations\n");
        
        Container container = new DefaultContainer();
        
        System.out.println("1. Register Bean with lifecycle callbacks");
        container.register(LifecycleService.class, LifecycleService.class);
        
        System.out.println("\n2. Get Bean (automatically triggers initialization)");
        LifecycleService service = container.get(LifecycleService.class);
        service.doWork();
        
        System.out.println("\n3. Close container (automatically triggers destruction)");
        container.close();
        
        System.out.println("\n[OK] Lifecycle example completed\n");
    }
    
    private static void configExample() {
        System.out.println("\n--- Configuration Management Example ---");
        System.out.println("Config is used to manage application configuration");
        System.out.println("Supports multiple data types: String, int, boolean, etc.\n");
        
        Config config = new DefaultConfig();
        
        System.out.println("1. Set configuration values");
        config.set("app.name", "My EST App");
        config.set("app.version", "1.0.0");
        config.set("app.debug", true);
        config.set("server.port", 8080);
        config.set("database.maxPoolSize", 20);
        
        System.out.println("\n2. Get configuration values");
        String appName = config.getString("app.name");
        String appVersion = config.getString("app.version");
        boolean debugMode = config.getBoolean("app.debug", false);
        int port = config.getInt("server.port", 80);
        int maxPoolSize = config.getInt("database.maxPoolSize", 10);
        String defaultValue = config.getString("non.existent.key", "This is default value");
        
        System.out.println("   - Application name: " + appName);
        System.out.println("   - Application version: " + appVersion);
        System.out.println("   - Debug mode: " + debugMode);
        System.out.println("   - Server port: " + port);
        System.out.println("   - Database pool size: " + maxPoolSize);
        System.out.println("   - Non-existent config (using default): " + defaultValue);
        
        System.out.println("\n[OK] Configuration management example completed\n");
    }
    
    private static void moduleExample() {
        System.out.println("\n--- Module System Example ---");
        System.out.println("Module is used to organize large application feature modules");
        System.out.println("Each module has its own lifecycle: initialize -> start -> stop\n");
        
        System.out.println("1. Create a custom module");
        Module module = new AbstractModule("Example Module", "1.0.0") {
            @Override
            protected void doInitialize() {
                System.out.println("   [Module Lifecycle] Initializing...");
                System.out.println("   - Loading config files");
                System.out.println("   - Establishing database connection");
            }

            @Override
            protected void doStart() {
                System.out.println("   [Module Lifecycle] Starting...");
                System.out.println("   - Starting background tasks");
                System.out.println("   - Starting to listen to events");
            }

            @Override
            protected void doStop() {
                System.out.println("   [Module Lifecycle] Stopping...");
                System.out.println("   - Stopping background tasks");
                System.out.println("   - Closing database connection");
            }
        };
        
        System.out.println("\n2. Execute module lifecycle");
        System.out.println("\n   Step 1: Initialize module");
        module.initialize();
        
        System.out.println("\n   Step 2: Start module");
        module.start();
        
        System.out.println("\n   Step 3: Stop module");
        module.stop();
        
        System.out.println("\n[OK] Module system example completed\n");
    }
}

interface UserService {
    String getUserName(String id);
}

class UserServiceImpl implements UserService {
    @Override
    public String getUserName(String id) {
        return "User" + id;
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
        System.out.println("   [Lifecycle] InitializingBean.afterPropertiesSet() called");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("   [Lifecycle] @PostConstruct annotated method called");
    }
    
    public void doWork() {
        System.out.println("   [Lifecycle] Executing business logic...");
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("   [Lifecycle] @PreDestroy annotated method called");
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println("   [Lifecycle] DisposableBean.destroy() called");
    }
}
