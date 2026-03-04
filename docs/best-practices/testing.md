# 测试最佳实践

本指南介绍如何使用 EST 测试框架编写和运行测试。

## 单元测试

### 基本测试结构

```java
package com.example.test;

import ltd.idcu.est.test.api.Test;
import ltd.idcu.est.test.api.BeforeEach;
import ltd.idcu.est.test.api.AfterEach;
import ltd.idcu.est.test.api.BeforeAll;
import ltd.idcu.est.test.api.AfterAll;
import ltd.idcu.est.test.api.Assertions;
import ltd.idcu.est.test.api.DisplayName;
import ltd.idcu.est.test.api.Disabled;

public class UserServiceTest {
    
    private UserService userService;
    
    @BeforeAll
    static void beforeAll() {
        // 所有测试前执行一次
        System.out.println("Starting tests...");
    }
    
    @AfterAll
    static void afterAll() {
        // 所有测试后执行一次
        System.out.println("Tests completed.");
    }
    
    @BeforeEach
    void beforeEach() {
        // 每个测试前执行
        userService = new UserServiceImpl();
    }
    
    @AfterEach
    void afterEach() {
        // 每个测试后执行
        userService = null;
    }
    
    @Test
    @DisplayName("创建用户成功")
    void testCreateUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        
        User created = userService.createUser(user);
        
        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals("Alice", created.getName());
    }
    
    @Test
    @Test
    @Disabled("暂时禁用此测试")
    void testDisabled() {
        // 此测试不会运行
    }
}
```

### 断言示例

```java
@Test
void testAssertions() {
    // 相等断言
    Assertions.assertEquals(expected, actual);
    Assertions.assertNotEquals(unexpected, actual);
    
    // 布尔断言
    Assertions.assertTrue(condition);
    Assertions.assertFalse(condition);
    
    // 空值断言
    Assertions.assertNull(object);
    Assertions.assertNotNull(object);
    
    // 异常断言
    Assertions.assertThrows(Exception.class, () -> {
        // 应该抛出异常的代码
    });
    
    // 超时断言
    Assertions.assertTimeout(Duration.ofSeconds(1), () -> {
        // 应该在 1 秒内完成
    });
}
```

## Mock 对象

### 使用 Mock

```java
package com.example.test;

import ltd.idcu.est.test.api.Mock;
import ltd.idcu.est.test.api.Mocked;

public class UserServiceMockTest {
    
    @Mock
    private UserRepository userRepository;
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }
    
    @Test
    void testWithMock() {
        // 设置 Mock 行为
        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setName("Alice");
        
        Mock.when(userRepository.findById("1")).thenReturn(mockUser);
        
        // 执行测试
        User user = userService.getUserById("1");
        
        // 验证结果
        Assertions.assertEquals("Alice", user.getName());
        
        // 验证 Mock 调用
        Mock.verify(userRepository).findById("1");
    }
}
```

## 参数化测试

```java
import ltd.idcu.est.test.api.ParameterizedTest;
import ltd.idcu.est.test.api.ValueSource;
import ltd.idcu.est.test.api.CsvSource;
import ltd.idcu.est.test.api.MethodSource;

public class ParameterizedTests {
    
    @ParameterizedTest
    @ValueSource(strings = {"Alice", "Bob", "Charlie"})
    void testWithValueSource(String name) {
        User user = new User();
        user.setName(name);
        Assertions.assertNotNull(user.getName());
    }
    
    @ParameterizedTest
    @CsvSource({
        "Alice, alice@example.com, 30",
        "Bob, bob@example.com, 25",
        "Charlie, charlie@example.com, 35"
    })
    void testWithCsvSource(String name, String email, int age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        
        Assertions.assertEquals(name, user.getName());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(age, user.getAge());
    }
    
    static Stream<String> nameProvider() {
        return Stream.of("Alice", "Bob", "Charlie");
    }
    
    @ParameterizedTest
    @MethodSource("nameProvider")
    void testWithMethodSource(String name) {
        Assertions.assertNotNull(name);
    }
}
```

## 集成测试

```java
package com.example.test;

import ltd.idcu.est.core.DefaultEstApplication;
import ltd.idcu.est.core.api.EstApplication;

public class IntegrationTest {
    
    private EstApplication app;
    
    @BeforeEach
    void setUp() {
        app = DefaultEstApplication.create();
        app.getConfiguration().set("test.mode", true);
        app.run();
    }
    
    @AfterEach
    void tearDown() {
        app.shutdown();
    }
    
    @Test
    void testFullApplication() {
        UserService userService = app.getContainer().get(UserService.class);
        
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        
        User created = userService.createUser(user);
        
        Assertions.assertNotNull(created.getId());
    }
}
```

## Web 测试

```java
package com.example.test;

import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.test.TestHttpClient;

public class WebTest {
    
    private WebApplication app;
    private TestHttpClient client;
    
    @BeforeEach
    void setUp() {
        app = DefaultWebApplication.create();
        
        // 配置测试路由
        app.getRouter().get("/test", (req, res) -> {
            res.json(Map.of("status", "ok"));
        });
        
        app.run(0); // 使用随机端口
        int port = app.getPort();
        client = new TestHttpClient("localhost", port);
    }
    
    @AfterEach
    void tearDown() {
        app.shutdown();
    }
    
    @Test
    void testWebEndpoint() {
        TestHttpResponse response = client.get("/test");
        
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("application/json", response.getContentType());
    }
    
    @Test
    void testPostEndpoint() {
        Map<String, Object> body = Map.of("name", "Test");
        TestHttpResponse response = client.post("/api/users", body);
        
        Assertions.assertEquals(201, response.getStatusCode());
    }
}
```

## 测试覆盖率

### 使用 JaCoCo

在 `pom.xml` 中添加：

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

运行测试并生成报告：

```bash
mvn clean test jacoco:report
```

查看报告：

在浏览器中打开 `target/site/jacoco/index.html`

## 测试最佳实践

### 1. 测试独立

- 每个测试应该独立运行
- 不依赖其他测试的执行顺序
- 每个测试准备自己的数据

```java
@Test
void testCreateUser() {
    // 准备测试数据
    User user = new User();
    user.setName("Test");
    
    // 执行
    User created = userService.createUser(user);
    
    // 断言
    Assertions.assertNotNull(created);
}
```

### 2. 测试命名

使用描述性的测试名称：

```java
@Test
@DisplayName("创建用户时应生成有效用户")
void shouldCreateUser_WhenGivenValidUser() {
}

@Test
@DisplayName("当用户名为空时应抛出异常")
void shouldThrowException_WhenNameIsNull() {
}
```

### 3. Given-When-Then 模式

```java
@Test
void testUserCreation() {
    // Given - 准备
    User user = new User();
    user.setName("Alice");
    
    // When - 执行
    User created = userService.createUser(user);
    
    // Then - 断言
    Assertions.assertNotNull(created.getId());
}
```

### 4. 测试边界情况

```java
@Test
void testWithEmptyName() {
    User user = new User();
    user.setName("");
    
    Assertions.assertThrows(ValidationException.class, () -> {
        userService.createUser(user);
    });
}

@Test
void testWithLongName() {
    String longName = "a".repeat(1000);
    User user = new User();
    user.setName(longName);
    
    Assertions.assertThrows(ValidationException.class, () -> {
        userService.createUser(user);
    });
}
```

### 5. 使用测试数据工厂

```java
public class TestDataFactory {
    
    public static User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Test User");
        user.setEmail("test" + System.currentTimeMillis() + "@example.com");
        return user;
    }
    
    public static User createUserWithName(String name) {
        User user = createTestUser();
        user.setName(name);
        return user;
    }
}

// 使用
@Test
void testWithFactory() {
    User user = TestDataFactory.createTestUser();
    // ...
}
```

## 运行测试

### 运行所有测试

```bash
mvn test
```

### 运行特定测试类

```bash
mvn test -Dtest=UserServiceTest
```

### 运行特定测试方法

```bash
mvn test -Dtest=UserServiceTest#testCreateUser
```

### 跳过测试

```bash
mvn install -DskipTests
```

## 持续集成

### GitHub Actions 示例

```yaml
name: CI/CD Pipeline

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Build and test
      run: mvn clean test
```

### GitLab CI 示例

```yaml
stages:
  - test
  - build

test:
  stage: test
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn test
  artifacts:
    reports:
      junit: target/surefire-reports/TEST-*.xml

build:
  stage: build
  image: maven:3.9-eclipse-temurin-21
  script:
    - mvn package
```
