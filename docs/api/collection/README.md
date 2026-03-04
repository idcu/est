# Collection 集合模块 API

集合模块提供类似 Laravel Collection 的链式数据处理能力。

## 核心接口

```java
public interface Collection<T> extends Iterable<T> {
    int size();
    boolean isEmpty();
    boolean contains(Object item);
    boolean containsAll(Collection<?> items);
    T get(int index);
    T first();
    T last();
    Optional<T> firstOrNull();
    Optional<T> lastOrNull();
    
    // 链式操作
    Collection<T> filter(Predicate<? super T> predicate);
    <R> Collection<R> map(Function<? super T, ? extends R> mapper);
    <R> Collection<R> flatMap(Function<? super T, ? extends Collection<? extends R>> mapper);
    Collection<T> distinct();
    Collection<T> sorted();
    Collection<T> sorted(Comparator<? super T> comparator);
    Collection<T> reverse();
    Collection<T> limit(int n);
    Collection<T> skip(int n);
    
    // 聚合操作
    <R> R reduce(R identity, BiFunction<R, ? super T, R> accumulator);
    boolean anyMatch(Predicate<? super T> predicate);
    boolean allMatch(Predicate<? super T> predicate);
    boolean noneMatch(Predicate<? super T> predicate);
    Optional<T> max(Comparator<? super T> comparator);
    Optional<T> min(Comparator<? super T> comparator);
    long count();
    long count(Predicate<? super T> predicate);
    
    // 转换
    List<T> toList();
    Set<T> toSet();
    <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, 
                            Function<? super T, ? extends V> valueMapper);
    <K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> classifier);
    
    // 副作用
    void forEach(Consumer<? super T> action);
    Collection<T> peek(Consumer<? super T> action);
    
    // 修改
    Collection<T> add(T item);
    Collection<T> addAll(Collection<? extends T> items);
    Collection<T> remove(T item);
    Collection<T> removeIf(Predicate<? super T> predicate);
    Collection<T> clear();
}
```

## 创建集合

```java
import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.impl.Collections;

// 从数组创建
Collection<String> collection = Collections.of("a", "b", "c");

// 从List创建
List<String> list = Arrays.asList("x", "y", "z");
Collection<String> fromList = Collections.from(list);

// 从Set创建
Set<Integer> set = Set.of(1, 2, 3);
Collection<Integer> fromSet = Collections.from(set);

// 创建空集合
Collection<String> empty = Collections.empty();

// 创建范围
Collection<Integer> range = Collections.range(1, 10);
Collection<Integer> rangeClosed = Collections.rangeClosed(1, 10);
```

## 链式操作示例

```java
Collection<User> users = Collections.of(user1, user2, user3, user4, user5);

// 过滤、映射、排序
List<String> result = users
    .filter(user -> user.getAge() > 18)
    .map(User::getName)
    .sorted()
    .limit(10)
    .toList();

// 分组
Map<String, List<User>> byCity = users
    .groupBy(User::getCity);

// 聚合
double avgAge = users
    .mapToInt(User::getAge)
    .average()
    .orElse(0);

// 查找
Optional<User> admin = users
    .filter(user -> "ADMIN".equals(user.getRole()))
    .firstOrNull();

// 统计
long activeCount = users
    .filter(User::isActive)
    .count();
```

## 数值操作

```java
Collection<Integer> numbers = Collections.of(1, 2, 3, 4, 5);

// 数学运算
int sum = numbers.sum();
Optional<Integer> max = numbers.max();
Optional<Integer> min = numbers.min();
double avg = numbers.average().orElse(0);

// 统计
IntSummaryStatistics stats = numbers.summaryStatistics();
System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
System.out.println("Avg: " + stats.getAverage());
System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
```

## 字符串操作

```java
Collection<String> words = Collections.of("Hello", "World", "EST");

// 连接
String joined = words.join(", "); // "Hello, World, EST"
String withPrefix = words.join(", ", "[", "]"); // "[Hello, World, EST]"

// 转换大小写
Collection<String> upper = words.map(String::toUpperCase);
Collection<String> lower = words.map(String::toLowerCase);
```

## 批量操作

```java
Collection<Integer> items = Collections.empty();

// 批量添加
items.addAll(Collections.of(1, 2, 3))
     .addAll(Collections.of(4, 5, 6));

// 批量删除
items.removeIf(n -> n % 2 == 0); // 删除偶数
```

## 不可变集合

```java
// 创建不可变集合
Collection<String> immutable = Collections.of("a", "b", "c").toImmutable();

// 尝试修改会抛出异常
try {
    immutable.add("d");
} catch (UnsupportedOperationException e) {
    System.out.println("Cannot modify immutable collection");
}
```
