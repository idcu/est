# EST Collection 集成指南

本文档介绍如何将 EST Collection 与 ORM（数据持久化）、JSON/YAML（数据序列化）等模块无缝集成使用。

## 目录
1. [EST Collection 与 ORM 集成](#est-collection-与-orm-集成)
2. [EST Collection 与 JSON 集成](#est-collection-与-json-集成)
3. [EST Collection 与 YAML 集成](#est-collection-与-yaml-集成)
4. [完整实战案例](#完整实战案例)

---

## EST Collection 与 ORM 集成

### 概述

EST Collection 可以与 `est-features-data` 模块中的 Repository 完美配合，实现：
- 从数据库查询数据后直接使用链式操作
- 对查询结果进行灵活的过滤、排序、分组等处理
- 将处理后的数据批量保存回数据库

### 基本使用

#### 1. 从 Repository 获取数据并转换为 Seq

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.features.data.api.Repository;

public class UserService {
    
    private Repository<User, Long> userRepository;
    
    public Seq<User> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return Seqs.from(userList);
    }
    
    public Seq<User> getActiveUsers() {
        return getAllUsers()
            .where(User::isActive);
    }
}
```

#### 2. 复杂查询后的数据处理

```java
public class OrderService {
    
    private Repository<Order, Long> orderRepository;
    
    public Map<String, Double> getTotalAmountByCity() {
        Seq<Order> orders = Seqs.from(orderRepository.findAll());
        
        return orders
            .where(Order::isPaid)
            .where(order -> order.getAmount() > 100)
            .groupBy(Order::getCity)
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue()
                    .fold(0.0, (sum, order) -> sum + order.getAmount())
            ));
    }
    
    public Seq<Order> getTopOrdersByUser(Long userId, int limit) {
        return Seqs.from(orderRepository.findAll())
            .where(order -> userId.equals(order.getUserId()))
            .sortByDesc(Order::getAmount)
            .take(limit);
    }
}
```

#### 3. 批量保存处理后的数据

```java
public class DataSyncService {
    
    private Repository<User, Long> userRepository;
    
    public void syncAndProcessUsers() {
        Seq<User> processedUsers = Seqs.from(userRepository.findAll())
            .where(user -> user.getAge() >= 18)
            .map(user -> {
                user.setActive(true);
                return user;
            });
        
        userRepository.saveAll(processedUsers.toList());
    }
}
```

### 完整示例

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.features.data.api.Repository;
import java.util.Map;

@Entity
class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private int age;
    private String city;
    private boolean active;
    
    public User() {}
    public User(String name, String email, int age, String city, boolean active) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.city = city;
        this.active = active;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

public class UserServiceIntegrationExample {
    
    private Repository<User, Long> userRepository;
    
    public UserServiceIntegrationExample(Repository<User, Long> userRepository) {
        this.userRepository = userRepository;
    }
    
    public void demonstrateIntegration() {
        initSampleData();
        
        Seq<User> allUsers = Seqs.from(userRepository.findAll());
        System.out.println("所有用户：" + allUsers.pluck(User::getName).toList());
        
        Seq<User> activeAdults = allUsers
            .where(User::isActive)
            .where(user -> user.getAge() >= 18);
        System.out.println("活跃成年人：" + activeAdults.pluck(User::getName).toList());
        
        Map<String, Seq<User>> usersByCity = allUsers.groupBy(User::getCity);
        System.out.println("按城市分组：");
        usersByCity.forEach((city, users) -> {
            System.out.println("  " + city + ": " + users.pluck(User::getName).toList());
        });
        
        User oldest = allUsers.maxBy(User::getAge).orElse(null);
        System.out.println("年龄最大的用户：" + (oldest != null ? oldest.getName() : "无"));
        
        Seq<User> updatedUsers = allUsers
            .map(user -> {
                if (user.getAge() < 18) {
                    user.setActive(false);
                }
                return user;
            });
        userRepository.saveAll(updatedUsers.toList());
        System.out.println("已批量更新用户状态");
    }
    
    private void initSampleData() {
        userRepository.saveAll(Arrays.asList(
            new User("张三", "zhangsan@example.com", 25, "北京", true),
            new User("李四", "lisi@example.com", 17, "上海", true),
            new User("王五", "wangwu@example.com", 30, "北京", false),
            new User("赵六", "zhaoliu@example.com", 22, "上海", true),
            new User("钱七", "qianqi@example.com", 28, "广州", true)
        ));
    }
}
```

---

## EST Collection 与 JSON 集成

### 概述

EST Collection 可以与 `JsonUtils` 配合，实现：
- 将 Seq 序列化为 JSON 数组
- 从 JSON 数组反序列化为 Seq
- 处理复杂的 JSON 数据结构

### 基本使用

#### 1. 将 Seq 序列化为 JSON

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.utils.format.json.JsonUtils;

public class JsonIntegrationExample {
    
    public static void main(String[] args) {
        Seq<User> users = Seqs.of(
            new User("张三", "zhangsan@example.com", 25),
            new User("李四", "lisi@example.com", 30),
            new User("王五", "wangwu@example.com", 28)
        );
        
        String json = JsonUtils.toJson(users.toList());
        System.out.println("JSON: " + json);
        
        String prettyJson = JsonUtils.toPrettyJson(users.toList());
        System.out.println("美化后的 JSON:\n" + prettyJson);
    }
}
```

#### 2. 从 JSON 反序列化为 Seq

```java
public class JsonToSeqExample {
    
    public static void main(String[] args) {
        String json = "[{\"name\":\"张三\",\"email\":\"zhangsan@example.com\",\"age\":25}," +
                      "{\"name\":\"李四\",\"email\":\"lisi@example.com\",\"age\":30}]";
        
        List<User> userList = JsonUtils.parseArray(json, User.class);
        Seq<User> users = Seqs.from(userList);
        
        Seq<String> names = users
            .where(user -> user.getAge() > 25)
            .pluck(User::getName);
        
        System.out.println("年龄大于25的用户：" + names.toList());
    }
}
```

#### 3. 处理复杂 JSON 数据

```java
public class ComplexJsonExample {
    
    public static void main(String[] args) {
        String complexJson = "{\n" +
            "  \"data\": {\n" +
            "    \"users\": [\n" +
            "      {\"name\":\"张三\",\"age\":25,\"city\":\"北京\"},\n" +
            "      {\"name\":\"李四\",\"age\":30,\"city\":\"上海\"},\n" +
            "      {\"name\":\"王五\",\"age\":28,\"city\":\"北京\"}\n" +
            "    ],\n" +
            "    \"total\": 3\n" +
            "  }\n" +
            "}";
        
        Map<String, Object> root = JsonUtils.parseObject(complexJson);
        Map<String, Object> data = JsonUtils.getMap(root, "data");
        List<Object> usersRaw = JsonUtils.getList(data, "users");
        
        Seq<Map<String, Object>> users = Seqs.from(usersRaw)
            .map(obj -> (Map<String, Object>) obj);
        
        Seq<Map<String, Object>> beijingUsers = users
            .where(user -> "北京".equals(JsonUtils.getString(user, "city")));
        
        System.out.println("北京的用户：" + beijingUsers.toList());
    }
}
```

---

## EST Collection 与 YAML 集成

### 概述

EST Collection 可以与 `YamlUtils` 配合，实现：
- 将 Seq 序列化为 YAML
- 从 YAML 反序列化为 Seq
- 处理配置文件等 YAML 数据

### 基本使用

#### 1. 将 Seq 序列化为 YAML

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.utils.format.yaml.YamlUtils;

public class YamlIntegrationExample {
    
    public static void main(String[] args) {
        Seq<User> users = Seqs.of(
            new User("张三", "zhangsan@example.com", 25, "北京"),
            new User("李四", "lisi@example.com", 30, "上海"),
            new User("王五", "wangwu@example.com", 28, "广州")
        );
        
        String yaml = YamlUtils.toYaml(users.toList());
        System.out.println("YAML:\n" + yaml);
    }
}
```

#### 2. 从 YAML 反序列化为 Seq

```java
public class YamlToSeqExample {
    
    public static void main(String[] args) {
        String yaml = "- name: 张三\n" +
                     "  email: zhangsan@example.com\n" +
                     "  age: 25\n" +
                     "  city: 北京\n" +
                     "- name: 李四\n" +
                     "  email: lisi@example.com\n" +
                     "  age: 30\n" +
                     "  city: 上海";
        
        List<Map<String, Object>> userList = YamlUtils.parseDocuments(yaml).get(0);
        Seq<Map<String, Object>> users = Seqs.from(userList)
            .map(obj -> (Map<String, Object>) obj);
        
        Seq<String> names = users
            .map(user -> YamlUtils.getString(user, "name"));
        
        System.out.println("用户姓名：" + names.toList());
    }
}
```

#### 3. 处理配置文件

```java
public class ConfigYamlExample {
    
    public static void main(String[] args) {
        String configYaml = "app:\n" +
                           "  name: MyApp\n" +
                           "  version: 1.0.0\n" +
                           "  features:\n" +
                           "    - name: cache\n" +
                           "      enabled: true\n" +
                           "    - name: logging\n" +
                           "      enabled: true\n" +
                           "    - name: metrics\n" +
                           "      enabled: false";
        
        Map<String, Object> config = YamlUtils.parse(configYaml);
        Map<String, Object> appConfig = YamlUtils.getMap(config, "app");
        List<Object> featuresRaw = YamlUtils.getList(appConfig, "features");
        
        Seq<Map<String, Object>> features = Seqs.from(featuresRaw)
            .map(obj -> (Map<String, Object>) obj);
        
        Seq<String> enabledFeatures = features
            .where(feature -> YamlUtils.getBoolean(feature, "enabled", false))
            .map(feature -> YamlUtils.getString(feature, "name"));
        
        System.out.println("启用的功能：" + enabledFeatures.toList());
    }
}
```

---

## 完整实战案例

### 电商数据处理全流程

这个案例展示了从数据库查询 → 数据处理 → JSON/YAML 序列化的完整流程。

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.features.data.api.Repository;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.yaml.YamlUtils;

@Entity
class Product {
    @Id
    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;
    private boolean active;
    
    public Product() {}
    public Product(String name, double price, String category, int stock, boolean active) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.active = active;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    public boolean isActive() { return active; }
}

public class CompleteEcommerceExample {
    
    private Repository<Product, Long> productRepository;
    
    public CompleteEcommerceExample(Repository<Product, Long> productRepository) {
        this.productRepository = productRepository;
    }
    
    public void runCompleteWorkflow() {
        System.out.println("=== 电商数据处理完整流程 ===\n");
        
        initSampleProducts();
        
        Seq<Product> products = Seqs.from(productRepository.findAll());
        System.out.println("1. 从数据库获取了 " + products.count() + " 个产品\n");
        
        System.out.println("2. 开始数据处理...");
        
        Seq<Product> availableProducts = products
            .where(Product::isActive)
            .where(p -> p.getStock() > 0);
        System.out.println("   - 可用产品数：" + availableProducts.count());
        
        Map<String, Seq<Product>> productsByCategory = availableProducts
            .groupBy(Product::getCategory);
        System.out.println("   - 产品类别数：" + productsByCategory.size());
        
        System.out.println("\n3. 各类别最贵产品：");
        productsByCategory.forEach((category, categoryProducts) -> {
            Product mostExpensive = categoryProducts
                .maxBy(Product::getPrice)
                .orElse(null);
            if (mostExpensive != null) {
                System.out.printf("   - %s: %s (¥%.2f)%n", 
                    category, mostExpensive.getName(), mostExpensive.getPrice());
            }
        });
        
        System.out.println("\n4. 导出为 JSON...");
        List<Product> productList = availableProducts
            .sortBy(Product::getPrice)
            .toList();
        
        String jsonOutput = JsonUtils.toPrettyJson(productList);
        System.out.println("   JSON 导出成功！");
        System.out.println("   JSON 预览:\n" + jsonOutput.substring(0, Math.min(200, jsonOutput.length())) + "...");
        
        System.out.println("\n5. 导出为 YAML...");
        String yamlOutput = YamlUtils.toYaml(productList);
        System.out.println("   YAML 导出成功！");
        System.out.println("   YAML 预览:\n" + yamlOutput.substring(0, Math.min(200, yamlOutput.length())) + "...");
        
        System.out.println("\n=== 流程完成！===");
    }
    
    private void initSampleProducts() {
        productRepository.saveAll(Arrays.asList(
            new Product("iPhone 15", 7999.0, "手机", 50, true),
            new Product("MacBook Pro", 14999.0, "电脑", 30, true),
            new Product("AirPods Pro", 1999.0, "配件", 100, true),
            new Product("iPad Air", 4799.0, "平板", 0, true),
            new Product("Apple Watch", 2999.0, "手表", 40, true),
            new Product("Magic Keyboard", 999.0, "配件", 80, false)
        ));
    }
}
```

## 总结

EST Collection 与 ORM、JSON/YAML 的集成非常简单且强大：

- **ORM 集成**：Repository 返回的 List 可以直接通过 `Seqs.from()` 转换为 Seq
- **JSON/YAML 集成**：Seq 可以通过 `toList()` 转换为 List，然后使用 JsonUtils/YamlUtils 序列化
- **链式处理**：可以在数据查询和序列化之间插入任意的 Collection 操作

这种组合让数据处理变得流畅、优雅且易于维护！

