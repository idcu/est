# EST 框架测试指南

本指南介绍如何使用 EST 测试框架编写和运行测试，包括 EST Web 模块的测试结构和使用方法。

---

## 目录

1. [单元测试](#单元测试)
2. [断言示例](#断言示例)
3. [Mock 对象](#mock-对象)
4. [参数化测试](#参数化测试)
5. [集成测试](#集成测试)
6. [Web 测试](#web-测试)
7. [EST Web 模块测试](#est-web-模块测试)
8. [测试覆盖率](#测试覆盖率)
9. [测试最佳实践](#测试最佳实践)
10. [运行测试](#运行测试)
11. [持续集成](#持续集成)

---

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
        System.out.println("Starting tests...");
    }
    
    @AfterAll
    static void afterAll() {
        System.out.println("Tests completed.");
    }
    
    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl();
    }
    
    @AfterEach
    void afterEach() {
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
    @Disabled("暂时禁用此测试")
    void testDisabled() {
    }
}
```

---

## 断言示例

```java
@Test
void testAssertions() {
    Assertions.assertEquals(expected, actual);
    Assertions.assertNotEquals(unexpected, actual);
    Assertions.assertTrue(condition);
    Assertions.assertFalse(condition);
    Assertions.assertNull(object);
    Assertions.assertNotNull(object);
    Assertions.assertThrows(Exception.class, () -> {
    });
    Assertions.assertTimeout(Duration.ofSeconds(1), () -> {
    });
}
```

### 断言方法

| 断言 | 描述 |
|------|------|
| `Assertions.assertTrue(condition)` | 断言条件为真 |
| `Assertions.assertFalse(condition)` | 断言条件为假 |
| `Assertions.assertNull(object)` | 断言对象为空 |
| `Assertions.assertNotNull(object)` | 断言对象非空 |
| `Assertions.assertEquals(expected, actual)` | 断言值相等 |
| `Assertions.assertNotEquals(unexpected, actual)` | 断言值不相等 |
| `Assertions.assertSame(expected, actual)` | 断言对象相同 |
| `Assertions.assertNotSame(unexpected, actual)` | 断言对象不同 |
| `Assertions.assertArrayEquals(expected, actual)` | 断言数组相等 |
| `Assertions.assertThrows(expectedType, executable)` | 断言抛出异常 |
| `Assertions.assertDoesNotThrow(executable)` | 断言不抛出异常 |
| `Assertions.assertTimeout(duration, executable)` | 断言执行超时 |
| `Assertions.fail()` | 直接失败测试 |

### 测试注解

| 注解 | 描述 |
|------|------|
| `@Test` | 标记测试方法 |
| `@BeforeEach` | 每个测试前执行 |
| `@AfterEach` | 每个测试后执行 |
| `@BeforeAll` | 所有测试前执行 |
| `@AfterAll` | 所有测试后执行 |
| `@DisplayName` | 设置测试显示名 |
| `@Disabled` | 禁用测试 |

---

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
        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setName("Alice");
        
        Mock.when(userRepository.findById("1")).thenReturn(mockUser);
        
        User user = userService.getUserById("1");
        
        Assertions.assertEquals("Alice", user.getName());
        Mock.verify(userRepository).findById("1");
    }
}
```

---

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

---

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

---

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
        
        app.getRouter().get("/test", (req, res) -> {
            res.json(Map.of("status", "ok"));
        });
        
        app.run(0);
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

---

## EST Web 模块测试

### 测试文件结构

1. **`DefaultSessionManagerTest.java` - 会话管理器测试**
   - 位置：`est-web/est-web-impl/src/test/java/ltd/idcu/est/web/DefaultSessionManagerTest.java`
   - 测试内容：
     - 会话创建和获取
     - 会话销毁
     - 会话属性管理
     - 会话过期清理
     - 自动清理任务管理
     - 会话配置管理

2. **`DefaultRouterTest.java` - 路由器测试**
   - 位置：`est-web/est-web-impl/src/test/java/ltd/idcu/est/web/DefaultRouterTest.java`
   - 测试内容：
     - 路由添加和匹配
     - HTTP 方法支持（GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS）
     - RouteHandler 支持
     - 路径参数提取
     - 路由分组
     - 命名路由
     - 通配符路由

3. **`WebTestsRunner.java` - 测试运行器**
   - 位置：`est-web/est-web-impl/src/test/java/ltd/idcu/est/web/WebTestsRunner.java`
   - 用于运行整个测试包

### DefaultSessionManager 测试覆盖（24 个测试用例）

| 测试方法 | 描述 |
|----------|------|
| `testCreateSession` | 测试会话创建 |
| `testGetSession` | 测试会话获取 |
| `testGetSessionNonExistent` | 测试不存在的会话获取 |
| `testGetSessionCreateIfMissing` | 测试会话不存在时创建 |
| `testDestroySession` | 测试会话销毁（通过 ID） |
| `testDestroySessionBySession` | 测试会话销毁（通过对象） |
| `testHasSession` | 测试会话存在性检查 |
| `testGetAllSessions` | 测试获取所有会话 |
| `testGetActiveSessionCount` | 测试活跃会话计数 |
| `testSetAndGetMaxInactiveInterval` | 测试最大不活动间隔设置 |
| `testSetAndGetSessionTimeout` | 测试会话超时设置 |
| `testGenerateSessionId` | 测试会话 ID 生成 |
| `testIsValidSessionId` | 测试会话 ID 验证 |
| `testSessionAttributes` | 测试会话属性管理 |
| `testSessionAttributeOrDefault` | 测试会话属性默认值 |
| `testRemoveSessionAttribute` | 测试会话属性移除 |
| `testSessionInvalidate` | 测试会话失效 |
| `testCleanExpiredSessions` | 测试过期会话清理 |
| `testCleanupTaskStartAndStop` | 测试清理任务启动和停止 |
| `testCleanupTaskMultipleStart` | 测试多次启动清理任务 |
| `testGetSessionConfig` | 测试获取会话配置 |

### DefaultRouter 测试覆盖（25 个测试用例）

| 测试方法 | 描述 |
|----------|------|
| `testCreateRouter` | 测试路由器创建 |
| `testAddGetRoute` | 测试添加 GET 路由 |
| `testAddPostRoute` | 测试添加 POST 路由 |
| `testAddPutRoute` | 测试添加 PUT 路由 |
| `testAddDeleteRoute` | 测试添加 DELETE 路由 |
| `testAddPatchRoute` | 测试添加 PATCH 路由 |
| `testAddHeadRoute` | 测试添加 HEAD 路由 |
| `testAddOptionsRoute` | 测试添加 OPTIONS 路由 |
| `testRouteWithRouteHandler` | 测试使用 RouteHandler |
| `testRouteMatching` | 测试路由匹配 |
| `testPathVariables` | 测试路径变量提取 |
| `testRouteGroups` | 测试路由分组 |
| `testNamedRoutes` | 测试命名路由 |
| `testGetRoutes` | 测试获取所有路由 |
| `testGetRoutesByMethod` | 测试按方法获取路由 |
| `testGetRoutesByPath` | 测试按路径获取路由 |
| `testHasRoute` | 测试路由存在性检查 |
| `testRemoveRoute` | 测试路由移除 |
| `testClearRoutes` | 测试路由清空 |
| `testAddRouteDirectly` | 测试直接添加路由 |
| `testWildcardRoute` | 测试通配符路由 |
| `testRouteWithTrailingSlash` | 测试带尾部斜杠的路由 |

### 如何运行测试

#### 1. 编译项目

```bash
mvn clean compile
```

#### 2. 运行 Web 模块测试

##### 方式一：使用测试运行器类

```bash
cd est-web/est-web-impl
mvn exec:java -Dexec.mainClass="ltd.idcu.est.web.WebTestsRunner"
```

##### 方式二：使用 Tests 类 API

在代码中运行：

```java
import ltd.idcu.est.test.Tests;
import ltd.idcu.est.web.DefaultSessionManagerTest;
import ltd.idcu.est.web.DefaultRouterTest;

public class RunMyTests {
    public static void main(String[] args) {
        Tests.run(DefaultSessionManagerTest.class);
        Tests.run(DefaultSessionManagerTest.class, DefaultRouterTest.class);
        Tests.runPackage("ltd.idcu.est.web");
        boolean success = Tests.runAndExit(DefaultSessionManagerTest.class);
        System.exit(success ? 0 : 1);
    }
}
```

---

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

查看报告：在浏览器中打开 `target/site/jacoco/index.html`

---

## 测试最佳实践

### 1. 独立测试

- 每个测试应该独立运行
- 不依赖其他测试的执行顺序
- 每个测试准备自己的数据

```java
@Test
void testCreateUser() {
    User user = new User();
    user.setName("Test");
    User created = userService.createUser(user);
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
    User user = new User();
    user.setName("Alice");
    User created = userService.createUser(user);
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
}
```

---

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

---

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

---

## 新增功能测试示例

### 测试新增的自动会话清理功能

```java
@Test
public void testCleanupTaskStartAndStop() {
    DefaultSessionManager sessionManager = new DefaultSessionManager();
    
    Assertions.assertFalse(sessionManager.isCleanupStarted());
    
    sessionManager.startCleanupTask(100, TimeUnit.MILLISECONDS);
    Assertions.assertTrue(sessionManager.isCleanupStarted());
    
    sessionManager.stopCleanupTask();
    Assertions.assertFalse(sessionManager.isCleanupStarted());
}
```

### 测试新增的 RouteHandler 功能

```java
@Test
public void testRouteWithRouteHandler() {
    DefaultRouter router = new DefaultRouter();
    boolean[] handlerCalled = {false};
    
    RouteHandler handler = (req, res) -> {
        handlerCalled[0] = true;
    };
    
    router.get("/test", handler);
    
    Route route = router.match("/test", HttpMethod.GET);
    Assertions.assertNotNull(route);
    Assertions.assertNotNull(route.getRouteHandler());
}
```

---

## 总结

我们为 EST Web 模块添加了完整的测试套件，包括：

1. **49 个测试用例**覆盖：
   - 24 个 DefaultSessionManager 测试
   - 25 个 DefaultRouter 测试

2. **覆盖的功能**：
   - 会话管理的所有核心功能
   - 路由系统的所有核心功能
   - 新增的自动清理功能
   - 新增的 RouteHandler 功能

3. **测试质量保证**：
   - 独立、可重复的测试
   - 清晰的测试命名
   - 完整的功能覆盖
   - 使用项目自带的测试框架

这些测试将帮助确保 Web 模块的代码质量和稳定性！

