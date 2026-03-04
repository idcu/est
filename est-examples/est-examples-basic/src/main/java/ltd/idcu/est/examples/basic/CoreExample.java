package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.core.impl.DefaultConfig;
import ltd.idcu.est.core.impl.AbstractModule;

public class CoreExample {
    public static void run() {
        System.out.println("=== Core Example ===");
        
        // 依赖注入示例
        containerExample();
        
        // 增强依赖注入示例
        EnhancedContainerExample.run();
        
        // 配置管理示例
        configExample();
        
        // 模块管理示例
        moduleExample();
    }
    
    private static void containerExample() {
        System.out.println("\n1. Dependency Injection Container Example:");
        Container container = new DefaultContainer();
        
        // 注册服务 - 使用 registerSingleton
        container.registerSingleton(String.class, "Hello EST!");
        container.registerSingleton(Integer.class, 42);
        
        // 获取服务
        String message = container.get(String.class);
        Integer number = container.get(Integer.class);
        
        System.out.println("Message: " + message);
        System.out.println("Number: " + number);
    }
    
    private static void configExample() {
        System.out.println("\n2. Configuration Management Example:");
        Config config = new DefaultConfig();
        
        // 设置配置
        config.set("app.name", "EST Example");
        config.set("app.version", "1.0.0");
        config.set("app.debug", true);
        
        // 获取配置
        String appName = config.getString("app.name");
        String appVersion = config.getString("app.version");
        boolean debug = config.getBoolean("app.debug");
        
        System.out.println("App Name: " + appName);
        System.out.println("App Version: " + appVersion);
        System.out.println("Debug Mode: " + debug);
    }
    
    private static void moduleExample() {
        System.out.println("\n3. Module Management Example:");
        Module module = new AbstractModule("example-module", "1.0.0") {
            @Override
            protected void doInitialize() {
                System.out.println("Module initialized");
            }

            @Override
            protected void doStart() {
                System.out.println("Module started");
            }

            @Override
            protected void doStop() {
                System.out.println("Module stopped");
            }
        };
        
        // 初始化模块
        module.initialize();
        
        // 启动模块
        module.start();
        
        // 停止模块
        module.stop();
        
        System.out.println("Module lifecycle completed");
    }
}
