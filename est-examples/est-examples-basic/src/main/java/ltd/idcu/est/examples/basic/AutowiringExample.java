package ltd.idcu.est.examples.basic;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.impl.DefaultContainer;

public class AutowiringExample {

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

    @Component
    public static class MixedInjectionDemo {
        
        @Inject
        private GreetingService greetingService;
        
        private Logger logger;
        
        @Inject
        public void setLogger(Logger logger) {
            this.logger = logger;
        }
        
        public void run() {
            String message = greetingService.greet("Mixed Injection");
            logger.log(message);
        }
    }

    public static void main(String[] args) {
        Container container = new DefaultContainer();

        System.out.println("=== Scanning components for autowiring ===");
        container.scan("ltd.idcu.est.examples.basic.AutowiringExample");

        System.out.println("\n=== Testing Field Injection ===");
        FieldInjectionDemo fieldDemo = container.get(FieldInjectionDemo.class);
        fieldDemo.run();

        System.out.println("\n=== Testing Method Injection ===");
        MethodInjectionDemo methodDemo = container.get(MethodInjectionDemo.class);
        methodDemo.run();

        System.out.println("\n=== Testing Mixed Injection ===");
        MixedInjectionDemo mixedDemo = container.get(MixedInjectionDemo.class);
        mixedDemo.run();

        container.close();
    }
}
