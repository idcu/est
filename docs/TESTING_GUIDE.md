# EST Web 模块测试指南

## 概述
---

本指南介绍了 EST Web 模块的测试结构和使用方法。

## 测试文件结构
---

### 新增测试文件：

1. **`DefaultSessionManagerTest.java** - 会话管理器测试**
   - 位置：`est-web/est-web-impl/src/test/java/ltd/idcu/est/web/DefaultSessionManagerTest.java
   - 测试内容：
     - 会话创建和获取
     - 会话销毁
     - 会话属性管理
     - 会话过期清理
     - 自动清理任务管理
     - 会话配置管理

2. **`DefaultRouterTest.java` - 路由器测试**
   - 位置：`est-web/est-web-impl/src/test/java/ltd/idcu/est/web/DefaultRouterTest.java
   - 测试内容：
     - 路由添加和匹配
     - HTTP 方法支持（GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS）
     - RouteHandler 支持
     - 路径参数提取
     - 路由分组
     - 命名路由
     - 通配符路由

3. **`WebTestsRunner.java` - 测试运行器**
   - 位置：`est-web/est-web-impl/src/test/java/ltd/idcu/est/web/WebTestsRunner.java
   - 用于运行整个测试包

## 测试覆盖范围
---

### DefaultSessionManager 测试覆盖（24 个测试用例

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

## 如何运行测试
---

### 1. 编译项目

首先确保项目能够成功编译：

```bash
mvn clean compile
```

### 2. 运行 Web 模块测试

#### 方式一：使用测试运行器类

```bash
cd est-web/est-web-impl
mvn exec:java -Dexec.mainClass="ltd.idcu.est.web.WebTestsRunner"
```

#### 方式二：使用 Tests 类 API

在代码中运行：

```java
import ltd.idcu.est.test.Tests;
import ltd.idcu.est.web.DefaultSessionManagerTest;
import ltd.idcu.est.web.DefaultRouterTest;

public class RunMyTests {
    public static void main(String[] args) {
        // 运行单个测试类
        Tests.run(DefaultSessionManagerTest.class);
        
        // 运行多个测试类
        Tests.run(DefaultSessionManagerTest.class, DefaultRouterTest.class);
        
        // 运行整个测试包
        Tests.runPackage("ltd.idcu.est.web");
        
        // 运行并退出（返回退出码）
        boolean success = Tests.runAndExit(DefaultSessionManagerTest.class);
        System.exit(success ? 0 : 1);
    }
}
```

### 3. 测试框架特性
---

EST 测试框架提供以下特性：

#### 断言方法

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

#### 测试注解

| 注解 | 描述 |
|------|------|
| `@Test` | 标记测试方法 |
| `@BeforeEach` | 每个测试前执行 |
| `@AfterEach` | 每个测试后执行 |
| `@BeforeAll` | 所有测试前执行 |
| `@AfterAll` | 所有测试后执行 |
| `@DisplayName` | 设置测试显示名 |
| `@Disabled` | 禁用测试 |

## 测试最佳实践
---

### 1. 独立测试
每个测试方法应该独立运行，不依赖其他测试的执行顺序。

### 2. 清晰的测试名称
使用描述性的测试方法名称，例如：
- `testCreateSession`
- `testSessionAttributes`
- `testCleanupTaskStartAndStop`

### 3. 测试数据隔离
每个测试应该设置和清理自己的测试数据。

### 4. 使用适当的断言
使用最具体的断言来验证预期行为。

## 新增功能测试示例
---

### 测试新增的自动会话清理功能：

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

### 测试新增的 RouteHandler 功能：

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

## 总结
---

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
