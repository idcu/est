package ltd.idcu.est.examples.basic;

import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.api.creational.Factory;
import ltd.idcu.est.patterns.api.creational.Builder;
import ltd.idcu.est.patterns.api.structural.Adapter;

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
        
        // 使用 Singleton.of() 创建单例
        Singleton<String> singleton = Singleton.of(() -> "Singleton Instance");
        
        String instance1 = singleton.getInstance();
        String instance2 = singleton.getInstance();
        
        System.out.println("Instance 1: " + instance1);
        System.out.println("Instance 2: " + instance2);
        System.out.println("Are they the same instance? " + (instance1 == instance2));
    }
    
    private static void factoryExample() {
        System.out.println("\n2. Factory Pattern Example:");
        
        // 创建工厂实例
        Factory<String> factory = new Factory<>() {
            @Override
            public String create() {
                return "Product from Factory";
            }

            @Override
            public String getType() {
                return "StringFactory";
            }
        };
        
        String product = factory.create();
        String type = factory.getType();
        
        System.out.println("Product: " + product);
        System.out.println("Factory type: " + type);
    }
    
    private static void builderExample() {
        System.out.println("\n3. Builder Pattern Example:");
        
        // 创建建造者实例
        StringBuilder builder = new StringBuilder();
        Builder<String> stringBuilder = new Builder<>() {
            @Override
            public Builder<String> reset() {
                builder.setLength(0);
                return this;
            }

            @Override
            public String build() {
                return builder.toString();
            }
        };
        
        builder.append("Hello").append(" ").append("Builder");
        String result = stringBuilder.build();
        
        System.out.println("Built string: " + result);
    }
    
    private static void adapterExample() {
        System.out.println("\n4. Adapter Pattern Example:");
        
        // 创建适配器实例
        Adapter<String, Integer> stringToIntAdapter = new Adapter<>() {
            @Override
            public Integer adapt(String source) {
                return Integer.parseInt(source);
            }

            @Override
            public Class<String> getSourceType() {
                return String.class;
            }

            @Override
            public Class<Integer> getTargetType() {
                return Integer.class;
            }
        };
        
        String numberStr = "12345";
        Integer number = stringToIntAdapter.adapt(numberStr);
        
        System.out.println("Source: " + numberStr + " (" + stringToIntAdapter.getSourceType().getSimpleName() + ")");
        System.out.println("Target: " + number + " (" + stringToIntAdapter.getTargetType().getSimpleName() + ")");
    }
}
