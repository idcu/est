# EST Collection 集合模块

提供类似 Laravel Collection 的链式数据处理能力。

## 模块结构

```
est-collection/
├── est-collection-api/      # 集合接口定义
├── est-collection-impl/     # 集合实现
└── pom.xml
```

## 快速开始

```java
import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.api.Collections;

Collection<User> users = Collections.of(user1, user2, user3);

// 链式操作
List<String> result = users
    .filter(user -> user.getAge() > 18)
    .map(User::getName)
    .sorted()
    .take(10)
    .toList();
```

## 主要功能

### 创建集合

```java
// 从数组创建
Collection<String> coll = Collections.of("a", "b", "c");

// 从List创建
List<String> list = Arrays.asList("x", "y", "z");
Collection<String> fromList = Collections.fromIterable(list);

// 空集合
Collection<String> empty = Collections.empty();

// 单元素集合
Collection<String> single = Collections.singleton("hello");
```

### 原生类型特化 (IntCollection)

```java
// 创建 int 集合
IntCollection numbers = Collections.intOf(1, 2, 3, 4, 5);

// 从数组创建
int[] array = {10, 20, 30};
IntCollection fromArray = Collections.intFromArray(array);

// 创建范围
IntCollection range = Collections.intRange(1, 10);

// 操作示例
int sum = numbers.sum();
double avg = numbers.average();
int max = numbers.max();
int min = numbers.min();

// 链式操作
IntCollection result = numbers
    .filter(n -> n % 2 == 0)
    .map(n -> n * 2)
    .sorted();
```

### 延迟求值

```java
// 转换为延迟集合
Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

LazyCollection<Integer> lazy = numbers.lazy();

// 链式操作不会立即执行
Collection<Integer> result = lazy
    .filter(n -> n % 2 == 0)
    .map(n -> n * 2)
    .sorted();

// 转换为立即求值集合时才会执行
Collection<Integer> eager = result.eager();

// 直接创建延迟集合
LazyCollection<Integer> lazyColl = LazyCollection.of(1, 2, 3);
```

### 可变/不可变集合

```java
// 默认是不可变的
Collection<String> coll = Collections.of("a", "b", "c");

// 转换为可变集合
MutableCollection<String> mutable = coll.mutable();
mutable.addItem("d");
mutable.removeItem("a");
mutable.sortItems();

// 转换回不可变集合
Collection<String> immutable = mutable.immutable();

// 直接创建可变集合
MutableCollection<String> mutableColl = Collections.mutableOf("x", "y", "z");

// 直接创建不可变集合
Collection<String> immutableColl = Collections.immutableOf("x", "y", "z");
```

### 过滤与映射

```java
Collection<User> users = Collections.of(user1, user2, user3);

// 过滤
Collection<User> adults = users.filter(user -> user.getAge() > 18);
Collection<User> nonAdults = users.filterNot(user -> user.getAge() > 18);

// 映射
Collection<String> names = users.map(User::getName);
Collection<String> indexed = users.mapIndexed((i, user) -> i + ":" + user.getName());

// 去重
Collection<String> unique = names.distinct();
Collection<User> distinctUsers = users.distinctBy(User::getEmail);

// 排序
Collection<User> sorted = users.sorted(Comparator.comparing(User::getName));
Collection<User> sortedBy = users.sortBy(User::getAge);
Collection<User> reversed = users.reversed();
```

### 聚合操作

```java
Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);

// 数学运算
int sum = numbers.reduce(Integer::sum);
int sumWithInitial = numbers.reduce(10, Integer::sum);
int fold = numbers.fold(10, Integer::sum);

// 分组
Map<String, Collection<User>> byCity = users.groupBy(User::getCity);

// 多重分组
Map<Pair<String, Integer>, Collection<User>> multipleGroup = users.groupByMultiple(
    user -> Pair.of(user.getCity(), user.getAge() / 10)
);

// 关联
Map<String, User> byEmail = users.associateBy(User::getEmail);
Map<String, String> emailToName = users.associateBy(User::getEmail, User::getName);

// 计数
Map<String, Integer> counts = users.eachCount(User::getCity);
```

### 查找与匹配

```java
// 查找第一个
User first = users.first();
User firstAdult = users.first(user -> user.getAge() > 18);
User firstOrNull = users.firstOrNull();
User firstAdultOrNull = users.firstOrNull(user -> user.getAge() > 18);

// 查找最后一个
User last = users.last();
User lastAdult = users.last(user -> user.getAge() > 18);
User lastOrNull = users.lastOrNull();

// 查找单个
User single = users.single();
User singleAdmin = users.single(user -> "ADMIN".equals(user.getRole()));
User singleOrNull = users.singleOrNull();

// 按索引查找
User third = users.elementAt(2);
User thirdOrNull = users.elementAtOrNull(2);
User thirdOrElse = users.elementAtOrElse(2, defaultUser);

// 匹配
boolean allAdult = users.all(user -> user.getAge() > 18);
boolean anyAdult = users.any(user -> user.getAge() > 18);
boolean noneChild = users.none(user -> user.getAge() < 18);

// 计数
int total = users.count();
int adultCount = users.count(user -> user.getAge() > 18);

// 最值
int min = numbers.min();
int max = numbers.max();
User youngest = users.minBy(User::getAge);
User oldest = users.maxBy(User::getAge);

// 随机
User randomUser = users.random();
Collection<User> sample = users.sample(3);
```

### 切片与截取

```java
Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 截取前 N 个
Collection<Integer> first3 = numbers.take(3);
Collection<Integer> takeWhile = numbers.takeWhile(n -> n < 5);
Collection<Integer> last2 = numbers.takeLast(2);

// 跳过前 N 个
Collection<Integer> drop2 = numbers.drop(2);
Collection<Integer> dropWhile = numbers.dropWhile(n -> n < 5);
Collection<Integer> dropLast2 = numbers.dropLast(2);

// 切片
Collection<Integer> slice = numbers.slice(2, 5);
```

### 集合操作

```java
Collection<Integer> a = Collections.of(1, 2, 3, 4);
Collection<Integer> b = Collections.of(3, 4, 5, 6);

// 添加元素
Collection<Integer> plus = a.plus(5);
Collection<Integer> plusAll = a.plusAll(b);

// 移除元素
Collection<Integer> minus = a.minus(2);
Collection<Integer> minusAll = a.minusAll(b);

// 交集
Collection<Integer> intersect = a.intersect(b);

// 并集
Collection<Integer> union = a.union(b);

// 差集
Collection<Integer> subtract = a.subtract(b);
```

### 其他便捷操作

```java
Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);

// 分区
Pair<Collection<Integer>, Collection<Integer>> partitioned = 
    numbers.partition(n -> n % 2 == 0);

// 拉链
Collection<String> letters = Collections.of("a", "b", "c");
Collection<Pair<String, Integer>> zipped = letters.zip(numbers);
Collection<Pair<Integer, String>> indexed = letters.zipWithIndex();

// 旋转
Collection<Integer> rotated = numbers.rotate(2);

// 填充
Collection<Integer> padStart = numbers.padStart(10, 0);
Collection<Integer> padEnd = numbers.padEnd(10, 0);

// 洗牌
Collection<Integer> shuffled = numbers.shuffle();
Collection<Integer> shuffledWithRandom = numbers.shuffle(new Random(42));

// 分块
Collection<List<Integer>> chunks = numbers.chunked(3);

// 滑动窗口
Collection<List<Integer>> windows = numbers.windowed(3);
```

### 输出转换

```java
Collection<String> coll = Collections.of("a", "b", "c");

// 转换为 List
List<String> list = coll.toList();

// 转换为 Set
Set<String> set = coll.toSet();

// 转换为 Map
Map<String, User> map = coll.associateBy(User::getEmail);

// 连接字符串
String joined = coll.joinToString();
String joinedWithSeparator = coll.joinToString("|");
String formatted = coll.joinToString(", ", "<", ">");

// JSON 输出
String json = coll.toJson();
String prettyJson = coll.toPrettyJson();

// 反序列化
Collection<User> fromJson = Collections.fromJson(jsonString, User.class);
Collection<User> fromYaml = Collections.fromYaml(yamlString, User.class);
Collection<User> fromXml = Collections.fromXml(xmlString, User.class);
```

## 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-collection-api</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

## 相关文档

- [API 文档](../docs/api/collection/)
