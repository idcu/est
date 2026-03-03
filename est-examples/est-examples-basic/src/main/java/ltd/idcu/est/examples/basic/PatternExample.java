package ltd.idcu.est.examples.basic;

import ltd.idcu.est.patterns.api.Singleton;
import ltd.idcu.est.patterns.api.Factory;
import ltd.idcu.est.patterns.api.Builder;
import ltd.idcu.est.patterns.api.Adapter;
import ltd.idcu.est.patterns.impl.DefaultSingleton;
import ltd.idcu.est.patterns.impl.DefaultFactory;
import ltd.idcu.est.patterns.impl.AbstractBuilder;
import ltd.idcu.est.patterns.impl.DefaultAdapter;

public class PatternExample {
    public static void run() {
        System.out.println("\n=== Design Pattern Example ===");
        
        // 单例模式示例
        singletonExample();
        
        // 工厂模式示例
        factoryExample();
        
        // 建造者模式示例
        builderExample();
        
        // 适配器模式示例
        adapterExample();
    }
    
    private static void singletonExample() {
        System.out.println("\n1. Singleton Pattern Example:");
        Singleton singleton1 = DefaultSingleton.getInstance();
        Singleton singleton2 = DefaultSingleton.getInstance();
        
        System.out.println("Singleton 1 hashcode: " + singleton1.hashCode());
        System.out.println("Singleton 2 hashcode: " + singleton2.hashCode());
        System.out.println("Are they the same instance? " + (singleton1 == singleton2));
    }
    
    private static void factoryExample() {
        System.out.println("\n2. Factory Pattern Example:");
        Factory factory = new DefaultFactory();
        
        // 创建不同类型的对象
        Object product1 = factory.create("type1");
        Object product2 = factory.create("type2");
        
        System.out.println("Product 1: " + product1);
        System.out.println("Product 2: " + product2);
    }
    
    private static void builderExample() {
        System.out.println("\n3. Builder Pattern Example:");
        Builder builder = new AbstractBuilder();
        
        // 构建复杂对象
        Object complexObject = builder.build();
        
        System.out.println("Complex object built: " + complexObject);
    }
    
    private static void adapterExample() {
        System.out.println("\n4. Adapter Pattern Example:");
        Adapter adapter = new DefaultAdapter();
        
        // 适配不同接口
        Object adaptedObject = adapter.adapt(new Object());
        
        System.out.println("Adapted object: " + adaptedObject);
    }
}