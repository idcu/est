package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.impl.DefaultContainer;

public class SimpleAutowiringTest {

    public interface GreetingService {
        String greet(String name);
    }

    @Service
    public static class DefaultGreetingService implements GreetingService {
        @Override
        public String greet(String name) {
            return "Hello, " + name + "!";
        }
    }

    public interface Logger {
        void log(String message);
    }

    @Component
    public static class ConsoleLogger implements Logger {
        @Override
        public void log(String message) {
            System.out.println("[LOG] " + message);
        }
    }

    @Component
    public static class FieldInjectionDemo {
        
        @Inject
        private GreetingService greetingService;
        
        @Inject
        private Logger logger;
        
        public void run() {
            String message = greetingService.greet("Field Injection");
            logger.log(message);
        }
    }

    @Component
    public static class MethodInjectionDemo {
        
        private GreetingService greetingService;
        private Logger logger;
        
        @Inject
        public void setServices(GreetingService greetingService, Logger logger) {
            this.greetingService = greetingService;
            this.logger = logger;
        }
        
        public void run() {
            String message = greetingService.greet("Method Injection");
            logger.log(message);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 开始测试自动装配机制 ===\n");
        
        Container container = new DefaultContainer();

        System.out.println("=== 注册组件 ===");
        container.register(GreetingService.class, DefaultGreetingService.class);
        container.register(Logger.class, ConsoleLogger.class);
        container.register(FieldInjectionDemo.class, FieldInjectionDemo.class);
        container.register(MethodInjectionDemo.class, MethodInjectionDemo.class);

        System.out.println("\n=== 测试字段注入 ===");
        FieldInjectionDemo fieldDemo = container.get(FieldInjectionDemo.class);
        fieldDemo.run();

        System.out.println("\n=== 测试方法注入 ===");
        MethodInjectionDemo methodDemo = container.get(MethodInjectionDemo.class);
        methodDemo.run();

        System.out.println("\n=== 自动装配机制测试完成 ===");
        
        container.close();
    }
}
