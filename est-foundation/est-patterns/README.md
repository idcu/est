# EST Patterns - 设计模式模块

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

EST Patterns 是 EST 框架的设计模式模块，提供了常用设计模式的开箱即用实现，帮助你编写更优雅、更可维护的代码。

---

## 📚 目录

- [快速入门](#快速入门)
- [基础篇：创建型模式](#基础篇创建型模式)
- [基础篇：结构型模式](#基础篇结构型模式)
- [进阶篇：行为型模式](#进阶篇行为型模式)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是设计模式？

想象你在做菜，每次做红烧肉都用同样的步骤：切肉、焯水、炒糖色、炖煮... 这个固定的"菜谱"就是一种模式。

**设计模式**就是编程中的"菜谱"，是前人总结出来的解决特定问题的最佳实践。

### 5分钟上手

让我们从最常用的**单例模式**开始：

```java
import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

public class FirstExample {
    public static void main(String[] args) {
        // 创建单例 - 确保只有一个实例
        Singleton<DatabaseConnection> singleton = 
            DefaultSingleton.of(DatabaseConnection::new);
        
        // 获取实例（多次获取都是同一个）
        DatabaseConnection conn1 = singleton.getInstance();
        DatabaseConnection conn2 = singleton.getInstance();
        
        System.out.println("是否是同一个实例: " + (conn1 == conn2));
    }
}

class DatabaseConnection {
    public DatabaseConnection() {
        System.out.println("数据库连接已创建");
    }
}
```

运行结果：
```
数据库连接已创建
是否是同一个实例: true
```

恭喜！你已经学会使用第一个设计模式了！ 🎉

---

## 🔰 基础篇：创建型模式

### 生活类比

创建型模式就像"生产车间"，专门负责"生产"对象。

### 1. 单例模式 (Singleton)

**场景**：班级里只有一个班长，大家都找他汇报工作。

```java
import ltd.idcu.est.patterns.api.creational.Singleton;
import ltd.idcu.est.patterns.impl.creational.DefaultSingleton;

public class SingletonExample {
    public static void main(String[] args) {
        // 创建班长单例
        Singleton<ClassMonitor> monitorSingleton = 
            DefaultSingleton.of(ClassMonitor::new);
        
        // 多个同学都找同一个班长
        ClassMonitor monitor1 = monitorSingleton.getInstance();
        ClassMonitor monitor2 = monitorSingleton.getInstance();
        
        monitor1.report("小明");
        monitor2.report("小红");
        
        System.out.println("是同一个班长: " + (monitor1 == monitor2));
    }
}

class ClassMonitor {
    public void report(String studentName) {
        System.out.println(studentName + " 向班长汇报工作");
    }
}
```

**特点**：
- 只有一个实例
- 全局访问点
- 线程安全

---

### 2. 工厂模式 (Factory)

**场景**：去餐厅吃饭，你告诉服务员"来一份宫保鸡丁"，后厨就给你做好了，你不用关心怎么做的。

```java
import ltd.idcu.est.patterns.api.creational.Factory;
import ltd.idcu.est.patterns.impl.creational.DefaultFactory;

public class FactoryExample {
    public static void main(String[] args) {
        // 创建菜品工厂
        Factory<Food> noodlesFactory = DefaultFactory.of("noodles", Noodles::new);
        Factory<Food> riceFactory = DefaultFactory.of("rice", Rice::new);
        
        // 点餐 - 工厂帮你创建
        Food noodles = noodlesFactory.create();
        Food rice = riceFactory.create();
        
        noodles.eat();
        rice.eat();
    }
}

interface Food {
    void eat();
}

class Noodles implements Food {
    @Override
    public void eat() {
        System.out.println("吃面条");
    }
}

class Rice implements Food {
    @Override
    public void eat() {
        System.out.println("吃米饭");
    }
}
```

**特点**：
- 不用自己 new 对象
- 容易扩展新产品
- 封装创建逻辑

---

### 3. 建造者模式 (Builder)

**场景**：组装电脑，你可以选择不同的 CPU、内存、硬盘... 自由组合。

```java
import ltd.idcu.est.patterns.api.creational.Builder;
import ltd.idcu.est.patterns.impl.creational.AbstractBuilder;

public class BuilderExample {
    public static void main(String[] args) {
        // 自由组装电脑
        Computer computer = new Computer.Builder()
            .cpu("Intel i7")
            .memory("16GB")
            .storage("512GB SSD")
            .build();
        
        System.out.println(computer);
    }
}

class Computer {
    private final String cpu;
    private final String memory;
    private final String storage;
    
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.memory = builder.memory;
        this.storage = builder.storage;
    }
    
    @Override
    public String toString() {
        return "电脑配置: CPU=" + cpu + ", 内存=" + memory + ", 硬盘=" + storage;
    }
    
    public static class Builder extends AbstractBuilder<Computer> {
        private String cpu;
        private String memory;
        private String storage;
        
        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }
        
        public Builder memory(String memory) {
            this.memory = memory;
            return this;
        }
        
        public Builder storage(String storage) {
            this.storage = storage;
            return this;
        }
        
        @Override
        protected Computer doBuild() {
            return new Computer(this);
        }
    }
}
```

**特点**：
- 链式调用
- 参数可选
-  immutable 对象

---

## 🔰 基础篇：结构型模式

### 生活类比

结构型模式就像"装修师傅"，帮你把东西组合在一起。

### 1. 装饰器模式 (Decorator)

**场景**：买咖啡，可以加糖、加奶、加珍珠... 想加什么就加什么。

```java
import ltd.idcu.est.patterns.api.structural.Decorator;
import ltd.idcu.est.patterns.impl.structural.AbstractDecorator;

public class DecoratorExample {
    public static void main(String[] args) {
        // 基础咖啡
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " 价格: " + coffee.getCost());
        
        // 加糖
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " 价格: " + coffee.getCost());
        
        // 再加奶
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " 价格: " + coffee.getCost());
    }
}

interface Coffee {
    String getDescription();
    double getCost();
}

class SimpleCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "简单咖啡";
    }
    
    @Override
    public double getCost() {
        return 10.0;
    }
}

class MilkDecorator extends AbstractDecorator<Coffee> implements Coffee {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + 牛奶";
    }
    
    @Override
    public double getCost() {
        return decorated.getCost() + 2.0;
    }
}

class SugarDecorator extends AbstractDecorator<Coffee> implements Coffee {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }
    
    @Override
    public String getDescription() {
        return decorated.getDescription() + " + 糖";
    }
    
    @Override
    public double getCost() {
        return decorated.getCost() + 1.0;
    }
}
```

**特点**：
- 动态添加功能
- 不修改原代码
- 可组合多个装饰

---

## 📈 进阶篇：行为型模式

### 生活类比

行为型模式就像"沟通协调员"，帮对象之间更好地沟通。

### 1. 观察者模式 (Observer)

**场景**：微信群聊，群主发消息，所有人都能收到。

```java
import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.patterns.api.behavioral.Subject;
import ltd.idcu.est.patterns.impl.behavioral.DefaultSubject;

public class ObserverExample {
    public static void main(String[] args) {
        // 创建群聊
        WeChatGroup group = new WeChatGroup();
        
        // 加入成员
        group.addMember(new Member("小明"));
        group.addMember(new Member("小红"));
        group.addMember(new Member("小刚"));
        
        // 群主发消息
        group.sendMessage("今晚8点开会！");
    }
}

class WeChatGroup {
    private final Subject<String> subject = new DefaultSubject<>();
    
    public void addMember(Observer<String> member) {
        subject.attach(member);
    }
    
    public void sendMessage(String message) {
        subject.notifyObservers(message);
    }
}

class Member implements Observer<String> {
    private final String name;
    
    public Member(String name) {
        this.name = name;
    }
    
    @Override
    public String getId() {
        return name;
    }
    
    @Override
    public void update(String message) {
        System.out.println(name + " 收到消息: " + message);
    }
}
```

**特点**：
- 一对多依赖
- 松耦合
- 自动通知

---

### 2. 策略模式 (Strategy)

**场景**：出行方式，可以选择步行、骑车、开车... 想怎么去就怎么去。

```java
import ltd.idcu.est.patterns.api.behavioral.Strategy;
import ltd.idcu.est.patterns.api.behavioral.StrategyContext;
import ltd.idcu.est.patterns.impl.behavioral.DefaultStrategy;
import ltd.idcu.est.patterns.impl.behavioral.DefaultStrategyContext;

public class StrategyExample {
    public static void main(String[] args) {
        // 定义出行策略
        Strategy<String, String> walk = DefaultStrategy.of("walk", 
            place -> "步行去" + place + "，需要30分钟");
        Strategy<String, String> bike = DefaultStrategy.of("bike", 
            place -> "骑车去" + place + "，需要15分钟");
        Strategy<String, String> car = DefaultStrategy.of("car", 
            place -> "开车去" + place + "，需要5分钟");
        
        // 使用策略上下文
        StrategyContext<String, String> context = new DefaultStrategyContext<>();
        context.registerStrategy("walk", walk);
        context.registerStrategy("bike", bike);
        context.registerStrategy("car", car);
        
        // 根据情况选择策略
        System.out.println(context.execute("walk", "公司"));
        System.out.println(context.execute("bike", "超市"));
        System.out.println(context.execute("car", "机场"));
    }
}
```

**特点**：
- 算法可互换
- 易于扩展
- 避免多重判断

---

## ✨ 最佳实践

### 1. 不要为了用模式而用模式

```java
// ❌ 过度设计 - 简单功能用了复杂模式
// 简单的事情简单做

// ✅ 简单直接
if (type.equals("A")) {
    doA();
} else {
    doB();
}
```

### 2. 组合使用多个模式

```java
// 单例 + 工厂 + 观察者
// 很多场景需要组合使用多个模式
```

### 3. 理解模式的意图

每个模式都有特定的解决问题，不要只记形式：
- Singleton：确保唯一实例
- Factory：封装对象创建
- Observer：一对多通知

---

## 📦 模块集成

### 与 est-collection 集成

```java
import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.collection.impl.Seqs;

public class CollectionIntegration {
    public static void main(String[] args) {
        // 用 Collection 管理观察者
        Seqs.of(new Member("小明"), new Member("小红"))
            .forEach(member -> System.out.println("加入: " + member.getId()));
    }
}
```

---

## 📚 更多内容

- [EST 项目主页](https://github.com/idcu/est)
- [EST Core](../est-core/README.md)
- [EST Collection](../est-collection/README.md)

---

**祝你使用愉快！** 🎉
