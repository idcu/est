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
        System.out.println("EST 2.0 新架构特性示�?);
        System.out.println("=".repeat(80));
        System.out.println();
        
        demonstrateCoreModules();
        demonstrateModularArchitecture();
        demonstratePatternIntegration();
        demonstrateLayeredArchitecture();
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("EST 2.0 新架构特性示例完�?);
        System.out.println("=".repeat(80));
    }
    
    private static void demonstrateCoreModules() {
        System.out.println("--- 1. 核心模块拆分示例 ---");
        System.out.println();
        
        System.out.println("EST 2.0 将核心功能拆分为独立模块�?);
        System.out.println("  - est-core-container: 依赖注入容器");
        System.out.println("  - est-core-config: 配置管理");
        System.out.println("  - est-core-lifecycle: 生命周期管理");
        System.out.println("  - est-core-module: 模块管理");
        System.out.println("  - est-core-aop: AOP支持");
        System.out.println("  - est-core-tx: 事务管理");
        System.out.println();
        
        System.out.println("优势�?);
        System.out.println("  - 更清晰的职责分离");
        System.out.println("  - 更好的可测试�?);
        System.out.println("  - 更灵活的依赖管理");
        System.out.println("  - 更容易的扩展和维�?);
        System.out.println();
    }
    
    private static void demonstrateModularArchitecture() {
        System.out.println("--- 2. 模块化架构示�?---");
        System.out.println();
        
        System.out.println("EST 2.0 采用清晰的层级架构：");
        System.out.println("  est-base (基础�?");
        System.out.println("    ├── est-utils (工具)");
        System.out.println("    ├── est-collection (集合)");
        System.out.println("    ├── est-patterns (设计模式)");
        System.out.println("    └── est-test (测试)");
        System.out.println();
        System.out.println("  est-core (核心�?");
        System.out.println("    ├── est-core-container");
        System.out.println("    ├── est-core-config");
        System.out.println("    ├── est-core-lifecycle");
        System.out.println("    ├── est-core-module");
        System.out.println("    ├── est-core-aop");
        System.out.println("    └── est-core-tx");
        System.out.println();
        System.out.println("  est-modules (功能�?");
        System.out.println("    ├── est-cache, est-logging, est-data, est-security");
        System.out.println("    ├── est-messaging, est-monitor, est-scheduler");
        System.out.println("    ├── est-event, est-circuitbreaker, est-discovery");
        System.out.println("    ├── est-config, est-performance, est-hotreload");
        System.out.println("    ├── est-ai, est-plugin, est-gateway");
        System.out.println();
        System.out.println("  est-app (应用�?");
        System.out.println("    ├── est-web");
        System.out.println("    ├── est-microservice");
        System.out.println("    └── est-console");
        System.out.println();
        System.out.println("  est-tools (工具�?");
        System.out.println("    ├── est-cli");
        System.out.println("    ├── est-codegen");
        System.out.println("    ├── est-migration");
        System.out.println("    └── est-scaffold");
        System.out.println();
    }
    
    private static void demonstratePatternIntegration() {
        System.out.println("--- 3. 设计模式集成示例 ---");
        System.out.println();
        
        System.out.println("EST 2.0 将设计模式作为基础层的一部分�?);
        System.out.println("  - 策略模式 (Strategy Pattern)");
        System.out.println("  - 观察者模�?(Observer Pattern)");
        System.out.println("  - 工厂模式 (Factory Pattern)");
        System.out.println("  - 建造者模�?(Builder Pattern)");
        System.out.println("  - 单例模式 (Singleton Pattern)");
        System.out.println();
        
        System.out.println("优势�?);
        System.out.println("  - 提供标准化的模式实现");
        System.out.println("  - 减少重复代码");
        System.out.println("  - 提高代码质量和可维护�?);
        System.out.println();
    }
    
    private static void demonstrateLayeredArchitecture() {
        System.out.println("--- 4. 分层架构优势 ---");
        System.out.println();
        
        System.out.println("1. 清晰的职责分离：");
        System.out.println("   - 每一层都有明确的职责");
        System.out.println("   - 层与层之间通过清晰的接口交�?);
        System.out.println();
        
        System.out.println("2. 更好的可扩展性：");
        System.out.println("   - 新增功能可以添加到对应层�?);
        System.out.println("   - 不会影响其他层级的代�?);
        System.out.println();
        
        System.out.println("3. 更易于测试：");
        System.out.println("   - 每一层都可以独立测试");
        System.out.println("   - 使用Mock对象更容易隔离测�?);
        System.out.println();
        
        System.out.println("4. 更灵活的依赖管理�?);
        System.out.println("   - 可以选择性地使用需要的模块");
        System.out.println("   - 减少不必要的依赖");
        System.out.println();
    }
    
    @Component
    public static class OrderService implements InitializingBean {
        
        @Inject
        private PaymentStrategy paymentStrategy;
        
        @Override
        public void afterPropertiesSet() {
            System.out.println("OrderService 初始化完�?);
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
            System.out.println("使用信用卡支�? " + amount);
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
            System.out.println("使用支付宝支�? " + amount);
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
            System.out.println("使用微信支付: " + amount);
        }
        
        @Override
        public String getStrategyName() {
            return "wechat";
        }
    }
}
