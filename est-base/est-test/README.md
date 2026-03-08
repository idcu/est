# EST Test 娴嬭瘯妯″潡

鎻愪緵鏂█宸ュ叿銆佹祴璇曟敞瑙ｅ拰娴嬭瘯杩愯鍣ㄦ敮鎸併€?

## 妯″潡缁撴瀯

```
est-test/
鈹溾攢鈹€ est-test-api/      # 娴嬭瘯鎺ュ彛瀹氫箟
鈹溾攢鈹€ est-test-impl/     # 娴嬭瘯瀹炵幇
鈹斺攢鈹€ pom.xml
```

## 涓昏鍔熻兘

### Assertions 鏂█

```java
import static ltd.idcu.est.test.api.Assertions.*;

// 甯冨皵鏂█
assertTrue(condition);
assertFalse(condition);

// 瀵硅薄鏂█
assertNotNull(object);
assertNull(object);

// 鐩哥瓑鏂█
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);

// 寮傚父鏂█
assertThrows(IllegalArgumentException.class, () -> {
    throw new IllegalArgumentException();
});

// 瓒呮椂鏂█
assertTimeout(Duration.ofSeconds(1), () -> {
    // 蹇€熸搷浣?
});
```

### 娴嬭瘯娉ㄨВ

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

### 娴嬭瘯杩愯鍣?

```java
import ltd.idcu.est.test.api.Tests;

// 杩愯鍗曚釜娴嬭瘯绫?
TestResult result = Tests.run(MyTest.class);
System.out.println("Passed: " + result.getPassedCount());

// 杩愯澶氫釜娴嬭瘯绫?
TestSuiteResult suiteResult = Tests.runAll(
    MyTest.class,
    AnotherTest.class
);
```

## 渚濊禆

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-test-api</artifactId>
    <version>2.1.0</version>
</dependency>
```

## 鐩稿叧鏂囨。

- [API 鏂囨。](../docs/api/test/)
