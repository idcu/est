# EST Patterns 设计模式模块

提供常用设计模式的实现和工具类，包括创建型、结构型和行为型模式。

## 模块结构

```
est-patterns/
├── est-patterns-api/      # 模式接口定义
├── est-patterns-impl/     # 模式实现
└── pom.xml
```

## 设计模式分类

### 创建型模式

| 模式 | 说明 |
|------|------|
| Singleton | 单例模式 |
| Factory | 工厂模式（简单工厂、工厂方法、抽象工厂） |
| Builder | 建造者模式 |
| Prototype | 原型模式 |

### 结构型模式

| 模式 | 说明 |
|------|------|
| Adapter | 适配器模式 |
| Decorator | 装饰器模式 |
| Proxy | 代理模式 |
| Composite | 组合模式 |
| Facade | 外观模式 |
| Bridge | 桥接模式 |
| Flyweight | 享元模式 |

### 行为型模式

| 模式 | 说明 |
|------|------|
| Observer | 观察者模式 |
| Strategy | 策略模式 |
| Command | 命令模式 |
| Chain of Responsibility | 责任链模式 |
| State | 状态模式 |
| Mediator | 中介者模式 |
| Iterator | 迭代器模式 |
| Visitor | 访问者模式 |
| Template Method | 模板方法模式 |
| Memento | 备忘录模式 |

## 快速示例

### 单例模式

```java
import ltd.idcu.est.patterns.creational.singleton.Singleton;

// 懒汉式单例
public class MySingleton {
    private static final Singleton<MySingleton> INSTANCE = Singleton.lazy(MySingleton::new);
    
    public static MySingleton getInstance() {
        return INSTANCE.get();
    }
}
```

### 工厂模式

```java
import ltd.idcu.est.patterns.creational.factory.Factory;

Factory<Shape> shapeFactory = Factory.simple(type -> {
    return switch (type) {
        case "circle" -> new Circle();
        case "square" -> new Square();
        default -> throw new IllegalArgumentException();
    };
});

Shape circle = shapeFactory.create("circle");
```

### 观察者模式

```java
import ltd.idcu.est.patterns.behavioral.observer.Observable;
import ltd.idcu.est.patterns.behavioral.observer.Observer;

public class WeatherStation extends Observable<WeatherData> {
    public void setMeasurements(WeatherData data) {
        notifyObservers(data);
    }
}

public class Display implements Observer<WeatherData> {
    @Override
    public void update(WeatherData data) {
        System.out.println("Temperature: " + data.getTemperature());
    }
}
```

## 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-patterns-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 相关文档

- [API 文档](../docs/api/patterns/)
