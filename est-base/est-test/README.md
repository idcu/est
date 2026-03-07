# EST Test 测试模块

提供断言工具、测试注解和测试运行器支持。

## 模块结构

```
est-test/
├── est-test-api/      # 测试接口定义
├── est-test-impl/     # 测试实现
└── pom.xml
```

## 主要功能

### Assertions 断言

```java
import static ltd.idcu.est.test.api.Assertions.*;

// 布尔断言
assertTrue(condition);
assertFalse(condition);

// 对象断言
assertNotNull(object);
assertNull(object);

// 相等断言
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);

// 异常断言
assertThrows(IllegalArgumentException.class, () -> {
    throw new IllegalArgumentException();
});

// 超时断言
assertTimeout(Duration.ofSeconds(1), () -> {
    // 快速操作
});
```

### 测试注解

```java
import ltd.idcu.est.test.api.Test;
import ltd.idcu.est.test.api.BeforeEach;
import ltd.idcu.est.test.api.AfterEach;
import ltd.idcu.est.test.api.BeforeAll;
import ltd.idcu.est.test.api.AfterAll;

public class MyTest {
    
    @BeforeAll
    static void beforeAll() { }
    
    @AfterAll
    static void afterAll() { }
    
    @BeforeEach
    void beforeEach() { }
    
    @AfterEach
    void afterEach() { }
    
    @Test
    void testSomething() {
        assertEquals(2, 1 + 1);
    }
}
```

### 测试运行器

```java
import ltd.idcu.est.test.api.Tests;

// 运行单个测试类
TestResult result = Tests.run(MyTest.class);
System.out.println("Passed: " + result.getPassedCount());

// 运行多个测试类
TestSuiteResult suiteResult = Tests.runAll(
    MyTest.class,
    AnotherTest.class
);
```

## 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-test-api</artifactId>
    <version>2.0.0</version>
</dependency>
```

## 相关文档

- [API 文档](../docs/api/test/)
