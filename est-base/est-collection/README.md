# EST Collection 集合模块 - 小白从入门到精通

## 目录
1. [什么是 EST Collection？](#什么是-est-collection)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇：创建和操作序列](#基础篇创建和操作序列)
4. [进阶篇：常用操作详解](#进阶篇常用操作详解)
5. [高级篇：复杂场景实战](#高级篇复杂场景实战)
6. [性能与最佳实践](#性能与最佳实践)

---

## 什么是 EST Collection？

### 用大白话理解

想象一下，你有一堆数据需要处理，比如：
- 从100个用户里找出年龄大于18岁的
- 给这些用户按年龄排序
- 只取前10个
- 然后提取他们的邮箱地址

**传统的做法**：你需要写很多循环、临时变量，代码又长又难看。

**用 EST Collection**：就像流水线一样，一步接一步，代码简洁优雅！

```java
// 优雅的链式操作
List<String> emails = users
    .where(user -> user.getAge() > 18)    // 筛选成年人
    .sortBy(User::getAge)                   // 按年龄排序
    .take(10)                                // 取前10个
    .pluck(User::getEmail)                  // 提取邮箱
    .toList();                               // 转成 List
```

### 核心特点
- 🎯 **像说话一样写代码**：`where`（筛选）、`sortBy`（排序）、`take`（取前N个）
- ⚡ **基于 Java Stream**：性能优秀，Java 原生支持
- 🔒 **默认不可变**：安全，不会意外修改数据
- 🎨 **链式调用**：代码流畅，可读性强

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### 第二步：你的第一个程序

创建一个简单的 Java 文件：

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;

public class FirstExample {
    public static void main(String[] args) {
        // 1. 创建一个序列（序列就是一组数据）
        Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子", "葡萄", "西瓜");
        
        // 2. 做一些操作
        Seq<String> result = fruits
            .filter(fruit -> fruit.length() > 2)  // 筛选名字长度大于2的
            .sorted()                               // 排序
            .take(3);                               // 只取前3个
        
        // 3. 输出结果
        System.out.println("筛选结果：" + result.toList());
    }
}
```

运行一下，你会看到：
```
筛选结果：[橙子, 西瓜, 葡萄]
```

恭喜！你已经会用 EST Collection 了！🎉

---

## 基础篇：创建和操作序列

### 3.1 什么是序列（Seq）？

**序列**就是一组有序的数据，就像一排排队的人。

在 EST Collection 中，`Seq<T>` 是核心接口，`T` 表示序列里装的是什么类型的数据。

### 3.2 创建序列的 7 种方法

#### 方法1：直接创建（最常用）

```java
// 用 of() 方法，直接把数据放进去
Seq<String> seq1 = Seqs.of("a", "b", "c");
Seq<Integer> seq2 = Seqs.of(1, 2, 3, 4, 5);
```

#### 方法2：从 List 创建

```java
import java.util.Arrays;
import java.util.List;

List<String> list = Arrays.asList("x", "y", "z");
Seq<String> seq = Seqs.from(list);
```

#### 方法3：创建空序列

```java
Seq<String> emptySeq = Seqs.empty();
```

#### 方法4：创建数字范围

```java
// 从 1 到 10（不包含10）
Seq<Integer> range1 = Seqs.range(1, 10);      // [1, 2, 3, 4, 5, 6, 7, 8, 9]

// 从 1 到 10，步长为 2
Seq<Integer> range2 = Seqs.range(1, 10, 2);   // [1, 3, 5, 7, 9]
```

#### 方法5：生成序列

```java
import java.util.Random;

// 生成 5 个随机数
Seq<Double> randoms = Seqs.generate(5, Math::random);

// 生成 10 个 0-100 的随机整数
Random random = new Random();
Seq<Integer> randomInts = Seqs.generate(10, () -> random.nextInt(100));
```

#### 方法6：重复元素

```java
// 重复 "hello" 5 次
Seq<String> repeated = Seqs.repeat("hello", 5);  // ["hello", "hello", "hello", "hello", "hello"]
```

#### 方法7：从 Stream 创建

```java
import java.util.stream.Stream;

Stream<String> stream = Stream.of("a", "b", "c");
Seq<String> seq = Seqs.from(stream);
```

### 3.3 序列的基本信息

```java
Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子");

// 获取元素个数
int size = fruits.size();  // 3

// 判断是否为空
boolean isEmpty = fruits.isEmpty();  // false
boolean isNotEmpty = fruits.isNotEmpty();  // true

// 判断是否包含某个元素
boolean hasApple = fruits.contains("苹果");  // true
boolean hasGrape = fruits.contains("葡萄");  // false
```

### 3.4 获取序列中的元素

```java
Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子", "葡萄", "西瓜");

// 获取第一个元素
String first = fruits.firstOrNull();  // "苹果"
String firstOrDefault = fruits.firstOr("默认值");  // "苹果"

// 获取最后一个元素
String last = fruits.lastOrNull();  // "西瓜"

// 根据索引获取（索引从 0 开始）
String second = fruits.get(1);  // "香蕉"
String fifth = fruits.elementAt(4);  // "西瓜"

// 安全获取（索引越界时返回 null）
String outOfBounds = fruits.elementAtOrNull(10);  // null
```

---

## 进阶篇：常用操作详解

### 4.1 筛选操作：只留下你想要的

#### filter / where：按条件筛选

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 留下偶数
Seq<Integer> evenNumbers = numbers.filter(n -> n % 2 == 0);
// 或者用 where（效果一样，写法更直观）
Seq<Integer> evenNumbers2 = numbers.where(n -> n % 2 == 0);

// 留下大于 5 的数
Seq<Integer> greaterThan5 = numbers.where(n -> n > 5);

System.out.println(evenNumbers.toList());  // [2, 4, 6, 8, 10]
System.out.println(greaterThan5.toList());  // [6, 7, 8, 9, 10]
```

#### filterNot / whereNot：排除符合条件的

```java
// 排除偶数，留下奇数
Seq<Integer> oddNumbers = numbers.filterNot(n -> n % 2 == 0);
// 或者
Seq<Integer> oddNumbers2 = numbers.whereNot(n -> n % 2 == 0);

System.out.println(oddNumbers.toList());  // [1, 3, 5, 7, 9]
```

#### distinct：去重

```java
Seq<String> words = Seqs.of("a", "b", "a", "c", "b", "d");
Seq<String> uniqueWords = words.distinct();

System.out.println(uniqueWords.toList());  // [a, b, c, d]
```

#### distinctBy：按某个属性去重

```java
// 假设有一个 User 类
class User {
    private String name;
    private String email;
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
}

Seq<User> users = Seqs.of(
    new User("张三", "zhangsan@example.com"),
    new User("李四", "lisi@example.com"),
    new User("张三", "zhangsan@example.com"),  // 重复的邮箱
    new User("王五", "wangwu@example.com")
);

// 按邮箱去重
Seq<User> uniqueUsers = users.distinctBy(User::getEmail);
```

### 4.2 映射操作：把数据变成另一种样子

#### map / pluck：转换每个元素

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);

// 每个数乘以 2
Seq<Integer> doubled = numbers.map(n -> n * 2);
System.out.println(doubled.toList());  // [2, 4, 6, 8, 10]

// 数字转字符串
Seq<String> asStrings = numbers.map(n -> "数字：" + n);
System.out.println(asStrings.toList());  // [数字：1, 数字：2, 数字：3, 数字：4, 数字：5]

// pluck 是 map 的别名，用于提取对象属性（更直观）
Seq<User> users = Seqs.of(
    new User("张三", "zhangsan@example.com"),
    new User("李四", "lisi@example.com")
);
Seq<String> names = users.pluck(User::getName);
System.out.println(names.toList());  // [张三, 李四]
```

#### mapIndexed：带索引的映射

```java
Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子");

// 索引从 0 开始
Seq<String> indexed = fruits.mapIndexed((index, fruit) -> index + ": " + fruit);
System.out.println(indexed.toList());  // [0: 苹果, 1: 香蕉, 2: 橙子]
```

### 4.3 排序操作：让数据井然有序

#### sorted：自然排序

```java
Seq<Integer> numbers = Seqs.of(5, 2, 8, 1, 9);
Seq<Integer> sorted = numbers.sorted();
System.out.println(sorted.toList());  // [1, 2, 5, 8, 9]

Seq<String> words = Seqs.of("banana", "apple", "cherry");
Seq<String> sortedWords = words.sorted();
System.out.println(sortedWords.toList());  // [apple, banana, cherry]
```

#### sortBy：按某个属性排序

```java
Seq<User> users = Seqs.of(
    new User("张三", "zhangsan@example.com", 25),
    new User("李四", "lisi@example.com", 18),
    new User("王五", "wangwu@example.com", 30)
);

// 按年龄从小到大排序
Seq<User> sortedByAge = users.sortBy(User::getAge);

// 按姓名排序
Seq<User> sortedByName = users.sortBy(User::getName);
```

#### sortByDesc：按某个属性降序排序

```java
// 按年龄从大到小排序
Seq<User> sortedByAgeDesc = users.sortByDesc(User::getAge);
```

#### reversed：反转顺序

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
Seq<Integer> reversed = numbers.reversed();
System.out.println(reversed.toList());  // [5, 4, 3, 2, 1]
```

#### shuffled：随机打乱

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);
Seq<Integer> shuffled = numbers.shuffled();
System.out.println(shuffled.toList());  // 随机顺序，比如 [3, 1, 5, 2, 4]
```

### 4.4 截取操作：只取部分数据

#### take：取前 N 个

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 取前 3 个
Seq<Integer> first3 = numbers.take(3);
System.out.println(first3.toList());  // [1, 2, 3]
```

#### takeWhile：一直取，直到条件不满足

```java
// 取小于 5 的数
Seq<Integer> lessThan5 = numbers.takeWhile(n -> n < 5);
System.out.println(lessThan5.toList());  // [1, 2, 3, 4]
```

#### takeLast：取后 N 个

```java
// 取后 3 个
Seq<Integer> last3 = numbers.takeLast(3);
System.out.println(last3.toList());  // [8, 9, 10]
```

#### drop：跳过前 N 个

```java
// 跳过前 3 个
Seq<Integer> dropped3 = numbers.drop(3);
System.out.println(dropped3.toList());  // [4, 5, 6, 7, 8, 9, 10]
```

#### dropWhile：一直跳过，直到条件不满足

```java
// 跳过小于 5 的数
Seq<Integer> from5 = numbers.dropWhile(n -> n < 5);
System.out.println(from5.toList());  // [5, 6, 7, 8, 9, 10]
```

#### slice：切片（指定范围）

```java
// 从索引 2 到 5（不包含 5）
Seq<Integer> slice = numbers.slice(2, 5);
System.out.println(slice.toList());  // [3, 4, 5]
```

### 4.5 查找与匹配：判断数据是否符合要求

#### 查找元素

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 找第一个大于 5 的数
Integer firstGreaterThan5 = numbers.first(n -> n > 5).orElse(null);  // 6

// 找最后一个小于 5 的数
Integer lastLessThan5 = numbers.last(n -> n < 5).orElse(null);  // 4

// 随机取一个
Integer random = numbers.random().orElse(null);  // 随机一个数

// 随机取 3 个
Seq<Integer> sample = numbers.sample(3);  // 随机 3 个数
```

#### 匹配判断

```java
// 是否所有数都大于 0？
boolean allPositive = numbers.all(n -> n > 0);  // true

// 是否有大于 10 的数？
boolean anyGreaterThan10 = numbers.any(n -> n > 10);  // false

// 是否没有负数？
boolean noneNegative = numbers.none(n -> n < 0);  // true
```

#### 计数

```java
// 总共有多少个元素
int total = numbers.count();  // 10

// 有多少个偶数
int evenCount = numbers.count(n -> n % 2 == 0);  // 5
```

### 4.6 输出转换：把序列转成其他格式

#### 转成 List

```java
Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子");
List<String> list = fruits.toList();
```

#### 转成 Set

```java
Set<String> set = fruits.toSet();
```

#### 连接成字符串

```java
Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子");

// 直接连接
String joined1 = fruits.joinToString();  // "苹果香蕉橙子"

// 用逗号分隔
String joined2 = fruits.joinToString(", ");  // "苹果, 香蕉, 橙子"

// 带前缀和后缀
String joined3 = fruits.joinToString(", ", "[", "]");  // "[苹果, 香蕉, 橙子]"
```

#### 转成 Stream

```java
// 可以使用 Java 原生 Stream API
Stream<String> stream = fruits.stream();
Stream<String> parallelStream = fruits.parallelStream();
```

---

## 高级篇：复杂场景实战

### 5.1 聚合操作：把数据合并起来

#### reduce / fold：累积操作

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);

// 求和
Integer sum = numbers.reduce(0, (a, b) -> a + b);  // 15

// 或者用 fold（效果一样）
Integer sum2 = numbers.fold(0, (a, b) -> a + b);  // 15

// 求乘积
Integer product = numbers.reduce(1, (a, b) -> a * b);  // 120

// 找出最大值
Integer max = numbers.reduce(Integer::max).orElse(null);  // 5
```

#### groupBy：分组

```java
Seq<User> users = Seqs.of(
    new User("张三", "北京", 25),
    new User("李四", "上海", 18),
    new User("王五", "北京", 30),
    new User("赵六", "上海", 22),
    new User("钱七", "广州", 28)
);

// 按城市分组
Map<String, Seq<User>> byCity = users.groupBy(User::getCity);

// 查看结果
byCity.forEach((city, cityUsers) -> {
    System.out.println(city + ": " + cityUsers.pluck(User::getName).toList());
});
// 输出：
// 北京: [张三, 王五]
// 上海: [李四, 赵六]
// 广州: [钱七]
```

#### keyBy / associateBy：转成 Map

```java
// 按邮箱作为 key，User 对象作为 value
Map<String, User> byEmail = users.keyBy(User::getEmail);

// 按邮箱作为 key，姓名作为 value
Map<String, String> emailToName = users.associateBy(User::getEmail, User::getName);
```

#### countBy：按某个属性计数

```java
// 统计每个城市有多少人
Map<String, Long> countByCity = users.countBy(User::getCity);
System.out.println(countByCity);  // {北京=2, 上海=2, 广州=1}
```

#### minBy / maxBy：找最值

```java
// 找年龄最小的用户
User youngest = users.minBy(User::getAge).orElse(null);

// 找年龄最大的用户
User oldest = users.maxBy(User::getAge).orElse(null);
```

### 5.2 集合操作：多个序列之间的运算

```java
Seq<Integer> a = Seqs.of(1, 2, 3, 4);
Seq<Integer> b = Seqs.of(3, 4, 5, 6);

// 添加元素
Seq<Integer> plus = a.plus(5);  // [1, 2, 3, 4, 5]
Seq<Integer> plusAll = a.plusAll(b);  // [1, 2, 3, 4, 3, 4, 5, 6]

// 移除元素
Seq<Integer> minus = a.minus(2);  // [1, 3, 4]
Seq<Integer> minusAll = a.minusAll(b);  // [1, 2]

// 交集（两个序列都有的元素）
Seq<Integer> intersect = a.intersect(b);  // [3, 4]

// 并集（去重合并）
Seq<Integer> union = a.union(b);  // [1, 2, 3, 4, 5, 6]

// 差集（在 a 中但不在 b 中的元素）
Seq<Integer> diff = a.diff(b);  // [1, 2]
```

### 5.3 高级操作

#### partition：分区（分成两部分）

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 分成偶数和奇数两部分
Pair<Seq<Integer>, Seq<Integer>> partitioned = numbers.partition(n -> n % 2 == 0);

Seq<Integer> evens = partitioned.getFirst();  // 偶数
Seq<Integer> odds = partitioned.getSecond();  // 奇数
```

#### zip：拉链操作（两个序列合并）

```java
Seq<String> letters = Seqs.of("a", "b", "c");
Seq<Integer> numbers = Seqs.of(1, 2, 3);

// 合并成 Pair 序列
Seq<Pair<String, Integer>> zipped = letters.zip(numbers);

zipped.forEach(pair -> {
    System.out.println(pair.getFirst() + " -> " + pair.getSecond());
});
// 输出：
// a -> 1
// b -> 2
// c -> 3
```

#### zipWithIndex：带索引的拉链

```java
Seq<String> fruits = Seqs.of("苹果", "香蕉", "橙子");
Seq<Pair<Integer, String>> indexed = fruits.zipWithIndex();

indexed.forEach(pair -> {
    System.out.println(pair.getFirst() + ": " + pair.getSecond());
});
// 输出：
// 0: 苹果
// 1: 香蕉
// 2: 橙子
```

#### chunked / chunk：分块

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 每 3 个分成一块
Seq<List<Integer>> chunks = numbers.chunked(3);

chunks.forEach(chunk -> {
    System.out.println(chunk);
});
// 输出：
// [1, 2, 3]
// [4, 5, 6]
// [7, 8, 9]
// [10]
```

#### windowed：滑动窗口

```java
Seq<Integer> numbers = Seqs.of(1, 2, 3, 4, 5);

// 窗口大小为 3，步长为 1
Seq<List<Integer>> windows = numbers.windowed(3);

windows.forEach(window -> {
    System.out.println(window);
});
// 输出：
// [1, 2, 3]
// [2, 3, 4]
// [3, 4, 5]
```

### 5.4 完整实战案例

#### 案例1：电商数据处理

假设我们有一个电商订单数据，需要做以下处理：
1. 筛选出已支付的订单
2. 筛选出金额大于 100 元的订单
3. 按金额从大到小排序
4. 只取前 10 个订单
5. 提取订单号和金额
6. 计算总金额

```java
class Order {
    private String orderId;
    private double amount;
    private boolean paid;
    private String userId;
    
    public Order(String orderId, double amount, boolean paid, String userId) {
        this.orderId = orderId;
        this.amount = amount;
        this.paid = paid;
        this.userId = userId;
    }
    
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return paid; }
    public String getUserId() { return userId; }
}

Seq<Order> orders = Seqs.of(
    new Order("ORDER001", 150.0, true, "USER001"),
    new Order("ORDER002", 80.0, true, "USER002"),
    new Order("ORDER003", 200.0, false, "USER003"),
    new Order("ORDER004", 300.0, true, "USER004"),
    new Order("ORDER005", 50.0, true, "USER005"),
    new Order("ORDER006", 180.0, true, "USER006")
);

// 链式处理
Seq<Pair<String, Double>> result = orders
    .where(Order::isPaid)                    // 已支付
    .where(order -> order.getAmount() > 100) // 金额 > 100
    .sortByDesc(Order::getAmount)             // 按金额降序
    .take(10)                                  // 取前10个
    .map(order -> new Pair<>(order.getOrderId(), order.getAmount()));  // 提取订单号和金额

// 计算总金额
double totalAmount = orders
    .where(Order::isPaid)
    .where(order -> order.getAmount() > 100)
    .fold(0.0, (sum, order) -> sum + order.getAmount());

System.out.println("符合条件的订单：");
result.forEach(pair -> {
    System.out.println(pair.getFirst() + ": ¥" + pair.getSecond());
});
System.out.println("总金额：¥" + totalAmount);
```

---

## 性能与最佳实践

### 6.1 性能特点

- ✅ **基于 Java Stream API**：充分利用 JVM 的优化，性能优秀
- ✅ **懒加载**：中间操作不会立即执行，直到终端操作才会计算
- ✅ **不可变优先**：默认不可变，避免副作用，线程安全

### 6.2 最佳实践

#### 1. 优先使用不可变 Seq

```java
// 推荐：使用不可变 Seq
Seq<String> seq = Seqs.of("a", "b", "c");

// 不推荐：除非确实需要修改
MutableSeq<String> mutable = seq.mutable();
```

#### 2. 灵活切换

```java
Seq<User> users = ...;

// 需要修改时，获取可变版本
MutableSeq<User> mutable = users.mutable();
mutable.add(newUser);

// 修改完后，可以转回不可变
Seq<User> immutable = mutable.immutable();
```

#### 3. 混合使用 Stream API

```java
Seq<String> seq = Seqs.of("a", "b", "c");

// 可以随时转成 Stream 使用 Java 原生 API
List<String> result = seq.stream()
    .filter(s -> s.length() > 0)
    .collect(Collectors.toList());
```

#### 4. 使用直观的别名

```java
// 推荐：使用 where、pluck、sortBy 等更直观的别名
Seq<String> result = users
    .where(User::isActive)
    .sortBy(User::getName)
    .pluck(User::getEmail);

// 也可以用 filter、map、sorted
Seq<String> result2 = users
    .filter(User::isActive)
    .sorted(Comparator.comparing(User::getName))
    .map(User::getEmail);
```

### 6.3 常见问题

#### Q: 和 Java Stream 有什么区别？

A: EST Collection 基于 Java Stream，但提供了更友好的 API：
- 更直观的方法名（where、pluck、sortBy）
- 默认不可变，更安全
- 更丰富的操作（chunked、windowed、zip 等）
- 可以随时和 Stream 互相转换

#### Q: 性能怎么样？

A: 由于基于 Java Stream，性能和 Stream 相当。在大多数场景下，性能都足够好。

#### Q: 线程安全吗？

A: 默认的 Seq 是不可变的，所以是线程安全的。MutableSeq 不是线程安全的，如果在多线程环境下使用，需要自己处理同步。

---

## 相关资源

- [API 文档](../../docs/api/collection/)
- [示例代码](../../est-examples/est-examples-basic/)
- [完整测试用例](./est-collection-impl/src/test/java/)

---

## 总结

恭喜你！你已经掌握了 EST Collection 的核心用法！

- 📚 **基础篇**：创建序列、基本操作
- 🎯 **进阶篇**：筛选、映射、排序、截取
- 🚀 **高级篇**：聚合、集合运算、实战案例
- 💡 **最佳实践**：性能优化、使用技巧

现在，去用 EST Collection 让你的代码更优雅吧！🎉

