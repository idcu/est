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
import ltd.idcu.est.collection.impl.Collections;

Collection<User> users = Collections.of(user1, user2, user3);

// 链式操作
List<String> result = users
    .filter(user -> user.getAge() > 18)
    .map(User::getName)
    .sorted()
    .limit(10)
    .toList();
```

## 主要功能

### 创建集合

```java
// 从数组创建
Collection<String> coll = Collections.of("a", "b", "c");

// 从List创建
List<String> list = Arrays.asList("x", "y", "z");
Collection<String> fromList = Collections.from(list);

// 从Set创建
Set<Integer> set = Set.of(1, 2, 3);
Collection<Integer> fromSet = Collections.from(set);

// 创建范围
Collection<Integer> range = Collections.range(1, 10);
```

### 过滤与映射

```java
Collection<User> users = Collections.of(user1, user2, user3);

// 过滤
Collection<User> adults = users.filter(user -> user.getAge() > 18);

// 映射
Collection<String> names = users.map(User::getName);

// 去重
Collection<String> unique = names.distinct();

// 排序
Collection<User> sorted = users.sorted(Comparator.comparing(User::getName));
```

### 聚合操作

```java
Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);

// 数学运算
int sum = numbers.sum();
Optional<Integer> max = numbers.max();
double avg = numbers.average().orElse(0);

// 统计
IntSummaryStatistics stats = numbers.summaryStatistics();

// 分组
Map<String, List<User>> byCity = users.groupBy(User::getCity);
```

### 查找与匹配

```java
// 查找
Optional<User> admin = users
    .filter(user -> "ADMIN".equals(user.getRole()))
    .firstOrNull();

// 匹配
boolean anyAdult = users.anyMatch(user -> user.getAge() > 18);
boolean allActive = users.allMatch(User::isActive);
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
