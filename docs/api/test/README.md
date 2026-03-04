# Test 测试模块 API

测试模块提供断言工具和测试运行器支持。

## 核心接口

### Assertions

```java
public final class Assertions {
    
    // 布尔断言
    public static void assertTrue(boolean condition);
    public static void assertTrue(boolean condition, String message);
    public static void assertFalse(boolean condition);
    public static void assertFalse(boolean condition, String message);
    
    // 对象断言
    public static void assertNull(Object object);
    public static void assertNull(Object object, String message);
    public static void assertNotNull(Object object);
    public static void assertNotNull(Object object, String message);
    
    // 相等断言
    public static void assertEquals(Object expected, Object actual);
    public static void assertEquals(Object expected, Object actual, String message);
    public static void assertNotEquals(Object unexpected, Object actual);
    public static void assertNotEquals(Object unexpected, Object actual, String message);
    
    // 数组断言
    public static void assertArrayEquals(Object[] expected, Object[] actual);
    public static void assertArrayEquals(Object[] expected, Object[] actual, String message);
    
    // 相同断言
    public static void assertSame(Object expected, Object actual);
    public static void assertSame(Object expected, Object actual, String message);
    public static void assertNotSame(Object unexpected, Object actual);
    public static void assertNotSame(Object unexpected, Object actual, String message);
    
    // 异常断言
    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable);
    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable, String message);
    public static void assertDoesNotThrow(Executable executable);
    public static void assertDoesNotThrow(Executable executable, String message);
    
    // 超时断言
    public static void assertTimeout(Duration timeout, Executable executable);
    public static void assertTimeout(Duration timeout, ThrowingSupplier<?> supplier);
    public static void assertTimeoutPreemptively(Duration timeout, Executable executable);
    
    // 失败
    public static void fail();
    public static void fail(String message);
    public static void fail(Throwable cause);
    public static void fail(String message, Throwable cause);
}
```

## 使用断言

```java
import static ltd.idcu.est.test.api.Assertions.*;

// 布尔断言
assertTrue(1 > 0);
assertFalse(1 < 0, "1 should not be less than 0");

// 对象断言
assertNotNull(object);
assertNull(nullValue);

// 相等断言
assertEquals(4, 2 + 2);
assertNotEquals(5, 2 + 2);

// 字符串断言
assertEquals("hello", "hello");
assertTrue("hello".startsWith("he"));

// 数组断言
assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3});
```

## 异常断言

```java
// 断言抛出异常
IllegalArgumentException exception = assertThrows(
    IllegalArgumentException.class,
    () -> {
        throw new IllegalArgumentException("Invalid argument");
    }
);
assertEquals("Invalid argument", exception.getMessage());

// 断言不抛出异常
assertDoesNotThrow(() -> {
    // 安全的操作
    int result = 1 + 1;
});
```

## 超时断言

```java
import java.time.Duration;

// 断言在超时内完成
assertTimeout(Duration.ofSeconds(1), () -> {
    // 快速操作
    Thread.sleep(100);
});

// 断言超时（抢占式）
assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
    Thread.sleep(50);
});
```

## 测试注解

```java
import ltd.idcu.est.test.api.Test;
import ltd.idcu.est.test.api.BeforeEach;
import ltd.idcu.est.test.api.AfterEach;
import ltd.idcu.est.test.api.BeforeAll;
import ltd.idcu.est.test.api.AfterAll;

public class MyTest {
    
    @BeforeAll
    static void beforeAll() {
        // 所有测试前执行一次
    }
    
    @AfterAll
    static void afterAll() {
        // 所有测试后执行一次
    }
    
    @BeforeEach
    void beforeEach() {
        // 每个测试前执行
    }
    
    @AfterEach
    void afterEach() {
        // 每个测试后执行
    }
    
    @Test
    void testSomething() {
        // 测试方法
        assertEquals(2, 1 + 1);
    }
    
    @Test
    @Disabled("Not implemented yet")
    void testDisabled() {
        // 被禁用的测试
    }
}
```

## 测试运行器

```java
import ltd.idcu.est.test.api.Tests;

// 运行单个测试类
TestResult result = Tests.run(MyTest.class);
System.out.println("Passed: " + result.getPassedCount());
System.out.println("Failed: " + result.getFailedCount());

// 运行多个测试类
TestSuiteResult suiteResult = Tests.runAll(
    MyTest.class,
    AnotherTest.class
);

// 运行包下所有测试
TestSuiteResult packageResult = Tests.runPackage("com.example.tests");
```

## 测试结果

```java
TestResult result = Tests.run(MyTest.class);

// 统计
int total = result.getTotalCount();
int passed = result.getPassedCount();
int failed = result.getFailedCount();
int skipped = result.getSkippedCount();

// 详细信息
List<TestFailure> failures = result.getFailures();
for (TestFailure failure : failures) {
    System.out.println("Test: " + failure.getTestName());
    System.out.println("Error: " + failure.getMessage());
    failure.getException().printStackTrace();
}
```

## 假设

```java
import static ltd.idcu.est.test.api.Assumptions.*;

@Test
void testOnWindows() {
    assumeTrue(System.getProperty("os.name").startsWith("Windows"));
    // 仅在Windows上运行
}

@Test
void testWithEnvironment() {
    assumeNotNull(System.getenv("API_KEY"), "API_KEY not set");
    // 需要API_KEY环境变量
}
```
