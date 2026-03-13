package ltd.idcu.est.examples.advanced;

import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.api.annotation.Component;
import ltd.idcu.est.core.container.api.annotation.Inject;
import ltd.idcu.est.core.lifecycle.api.InitializingBean;
import ltd.idcu.est.patterns.api.strategy.Strategy;
import ltd.idcu.est.patterns.api.strategy.StrategyContext;

import java.util.ArrayList;
import java.util.List;

public class NewArchitectureExample {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("EST 2.0 New Architecture Features Example");
        System.out.println("=".repeat(80));
        System.out.println();
        
        demonstrateCoreModules();
        demonstrateModularArchitecture();
        demonstratePatternIntegration();
        demonstrateLayeredArchitecture();
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("EST 2.0 New Architecture Features Example Complete");
        System.out.println("=".repeat(80));
    }
    
    private static void demonstrateCoreModules() {
        System.out.println("--- 1. Core Module Split Example ---");
        System.out.println();
        
        System.out.println("EST 2.0 splits core functionality into independent modules:");
        System.out.println("  - est-core-container: Dependency injection container");
        System.out.println("  - est-core-config: Configuration management");
        System.out.println("  - est-core-lifecycle: Lifecycle management");
        System.out.println("  - est-core-module: Module management");
        System.out.println("  - est-core-aop: AOP support");
        System.out.println("  - est-core-tx: Transaction management");
        System.out.println();
        
        System.out.println("Advantages:");
        System.out.println("  - Clearer responsibility separation");
        System.out.println("  - Better testability");
        System.out.println("  - More flexible dependency management");
        System.out.println("  - Easier extension and maintenance");
        System.out.println();
    }
    
    private static void demonstrateModularArchitecture() {
        System.out.println("--- 2. Modular Architecture Example ---");
        System.out.println();
        
        System.out.println("EST 2.0 uses a clear layered architecture:");
        System.out.println("  est-base (Foundation)");
        System.out.println("    ├── est-utils (Utilities)");
        System.out.println("    ├── est-collection (Collections)");
        System.out.println("    ├── est-patterns (Design Patterns)");
        System.out.println("    └── est-test (Testing)");
        System.out.println();
        System.out.println("  est-core (Core)");
        System.out.println("    ├── est-core-container");
        System.out.println("    ├── est-core-config");
        System.out.println("    ├── est-core-lifecycle");
        System.out.println("    ├── est-core-module");
        System.out.println("    ├── est-core-aop");
        System.out.println("    └── est-core-tx");
        System.out.println();
        System.out.println("  est-modules (Features)");
        System.out.println("    ├── est-cache, est-logging, est-data, est-security");
        System.out.println("    ├── est-messaging, est-monitor, est-scheduler");
        System.out.println("    ├── est-event, est-circuitbreaker, est-discovery");
        System.out.println("    ├── est-config, est-performance, est-hotreload");
        System.out.println("    ├── est-ai, est-plugin, est-gateway");
        System.out.println();
        System.out.println("  est-app (Applications)");
        System.out.println("    ├── est-web");
        System.out.println("    ├── est-microservice");
        System.out.println("    └── est-console");
        System.out.println();
        System.out.println("  est-tools (Tools)");
        System.out.println("    ├── est-cli");
        System.out.println("    ├── est-codegen");
        System.out.println("    ├── est-migration");
        System.out.println("    └── est-scaffold");
        System.out.println();
    }
    
    private static void demonstratePatternIntegration() {
        System.out.println("--- 3. Design Pattern Integration Example ---");
        System.out.println();
        
        System.out.println("EST 2.0 includes design patterns as part of the foundation layer:");
        System.out.println("  - Strategy Pattern");
        System.out.println("  - Observer Pattern");
        System.out.println("  - Factory Pattern");
        System.out.println("  - Builder Pattern");
        System.out.println("  - Singleton Pattern");
        System.out.println();
        
        System.out.println("Advantages:");
        System.out.println("  - Provides standardized pattern implementations");
        System.out.println("  - Reduces repetitive code");
        System.out.println("  - Improves code quality and maintainability");
        System.out.println();
    }
    
    private static void demonstrateLayeredArchitecture() {
        System.out.println("--- 4. Layered Architecture Advantages ---");
        System.out.println();
        
        System.out.println("1. Clear responsibility separation:");
        System.out.println("   - Each layer has clear responsibilities");
        System.out.println("   - Layers communicate through clear interfaces");
        System.out.println();
        
        System.out.println("2. Better extensibility:");
        System.out.println("   - New features can be added to corresponding layers");
        System.out.println("   - Does not affect code in other layers");
        System.out.println();
        
        System.out.println("3. Easier testing:");
        System.out.println("   - Each layer can be tested independently");
        System.out.println("   - Easier to isolate tests using Mock objects");
        System.out.println();
        
        System.out.println("4. More flexible dependency management:");
        System.out.println("   - Can selectively use needed modules");
        System.out.println("   - Reduces unnecessary dependencies");
        System.out.println();
    }
    
    @Component
    public static class OrderService implements InitializingBean {
        
        @Inject
        private PaymentStrategy paymentStrategy;
        
        @Override
        public void afterPropertiesSet() {
            System.out.println("OrderService initialized");
        }
        
        public void processOrder(double amount) {
            paymentStrategy.pay(amount);
        }
    }
    
    public interface PaymentStrategy extends Strategy {
        void pay(double amount);
    }
    
    @Component
    public static class CreditCardPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Paying with credit card: " + amount);
        }
        
        @Override
        public String getStrategyName() {
            return "credit-card";
        }
    }
    
    @Component
    public static class AlipayPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Paying with Alipay: " + amount);
        }
        
        @Override
        public String getStrategyName() {
            return "alipay";
        }
    }
    
    @Component
    public static class WechatPayment implements PaymentStrategy {
        @Override
        public void pay(double amount) {
            System.out.println("Paying with WeChat: " + amount);
        }
        
        @Override
        public String getStrategyName() {
            return "wechat";
        }
    }
}
