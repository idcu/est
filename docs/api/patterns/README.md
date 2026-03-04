# Patterns 设计模式模块 API

设计模式模块提供常用设计模式的实现和工具。

## 创建型模式

### 单例模式 (Singleton)

```java
import ltd.idcu.est.patterns.creational.singleton.Singleton;

// 懒汉式单例
public class MySingleton {
    private static final Singleton<MySingleton> INSTANCE = Singleton.lazy(MySingleton::new);
    
    public static MySingleton getInstance() {
        return INSTANCE.get();
    }
}

// 饿汉式单例
public class EagerSingleton {
    private static final Singleton<EagerSingleton> INSTANCE = Singleton.eager(new EagerSingleton());
    
    public static EagerSingleton getInstance() {
        return INSTANCE.get();
    }
}

// 枚举单例
public enum EnumSingleton {
    INSTANCE;
    
    public void doSomething() {
        // ...
    }
}
```

### 工厂模式 (Factory)

```java
import ltd.idcu.est.patterns.creational.factory.Factory;

// 简单工厂
Factory<Shape> shapeFactory = Factory.simple(type -> {
    return switch (type) {
        case "circle" -> new Circle();
        case "square" -> new Square();
        default -> throw new IllegalArgumentException("Unknown shape");
    };
});

Shape circle = shapeFactory.create("circle");

// 工厂方法
public interface ShapeFactory {
    Shape create();
}

public class CircleFactory implements ShapeFactory {
    @Override
    public Shape create() {
        return new Circle();
    }
}

// 抽象工厂
public interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

public class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() { return new WindowsButton(); }
    
    @Override
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}
```

### 建造者模式 (Builder)

```java
import ltd.idcu.est.patterns.creational.builder.Builder;

// 使用建造者
User user = User.builder()
    .name("John")
    .email("john@example.com")
    .age(30)
    .build();

// 自定义建造者
public class User {
    private final String name;
    private final String email;
    private final int age;
    
    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String email;
        private int age;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
}
```

### 原型模式 (Prototype)

```java
import ltd.idcu.est.patterns.creational.prototype.Prototype;

// 可克隆对象
public class Document implements Prototype<Document> {
    private String title;
    private String content;
    
    @Override
    public Document clone() {
        Document copy = new Document();
        copy.title = this.title;
        copy.content = this.content;
        return copy;
    }
}

// 使用原型
Document original = new Document();
Document copy = original.clone();
```

## 结构型模式

### 适配器模式 (Adapter)

```java
import ltd.idcu.est.patterns.structural.adapter.Adapter;

// 目标接口
public interface Target {
    void request();
}

// 被适配者
public class Adaptee {
    public void specificRequest() {
        System.out.println("Adaptee's specific request");
    }
}

// 适配器
public class Adapter implements Target {
    private final Adaptee adaptee;
    
    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }
    
    @Override
    public void request() {
        adaptee.specificRequest();
    }
}
```

### 装饰器模式 (Decorator)

```java
import ltd.idcu.est.patterns.structural.decorator.Decorator;

// 组件接口
public interface Coffee {
    double cost();
    String description();
}

// 具体组件
public class SimpleCoffee implements Coffee {
    @Override
    public double cost() { return 1.0; }
    
    @Override
    public String description() { return "Simple coffee"; }
}

// 装饰器
public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;
    
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

// 具体装饰器
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }
    
    @Override
    public double cost() { return coffee.cost() + 0.5; }
    
    @Override
    public String description() { return coffee.description() + ", milk"; }
}

// 使用
Coffee coffee = new MilkDecorator(new SimpleCoffee());
```

### 代理模式 (Proxy)

```java
import ltd.idcu.est.patterns.structural.proxy.Proxy;

// 主题接口
public interface Image {
    void display();
}

// 真实主题
public class RealImage implements Image {
    private final String filename;
    
    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }
    
    private void loadFromDisk() {
        System.out.println("Loading " + filename);
    }
    
    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

// 代理
public class ImageProxy implements Image {
    private final String filename;
    private RealImage realImage;
    
    public ImageProxy(String filename) {
        this.filename = filename;
    }
    
    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}
```

## 行为型模式

### 观察者模式 (Observer)

```java
import ltd.idcu.est.patterns.behavioral.observer.Observable;
import ltd.idcu.est.patterns.behavioral.observer.Observer;

// 主题
public class WeatherStation extends Observable<WeatherData> {
    private WeatherData data;
    
    public void setMeasurements(WeatherData data) {
        this.data = data;
        notifyObservers(data);
    }
}

// 观察者
public class Display implements Observer<WeatherData> {
    @Override
    public void update(WeatherData data) {
        System.out.println("Temperature: " + data.getTemperature());
    }
}

// 使用
WeatherStation station = new WeatherStation();
Display display = new Display();
station.addObserver(display);
station.setMeasurements(new WeatherData(25, 60));
```

### 策略模式 (Strategy)

```java
import ltd.idcu.est.patterns.behavioral.strategy.Strategy;

// 策略接口
public interface SortStrategy {
    void sort(int[] array);
}

// 具体策略
public class BubbleSort implements SortStrategy {
    @Override
    public void sort(int[] array) {
        // 冒泡排序实现
    }
}

public class QuickSort implements SortStrategy {
    @Override
    public void sort(int[] array) {
        // 快速排序实现
    }
}

// 上下文
public class Sorter {
    private SortStrategy strategy;
    
    public Sorter(SortStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void sort(int[] array) {
        strategy.sort(array);
    }
}
```

### 命令模式 (Command)

```java
import ltd.idcu.est.patterns.behavioral.command.Command;

// 命令接口
public interface Command {
    void execute();
    void undo();
}

// 具体命令
public class LightOnCommand implements Command {
    private final Light light;
    
    public LightOnCommand(Light light) {
        this.light = light;
    }
    
    @Override
    public void execute() {
        light.on();
    }
    
    @Override
    public void undo() {
        light.off();
    }
}

// 调用者
public class RemoteControl {
    private Command command;
    
    public void setCommand(Command command) {
        this.command = command;
    }
    
    public void pressButton() {
        command.execute();
    }
}
```
