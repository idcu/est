# EST 框架贡献指南

感谢您对 EST 框架的关注和支持！我们欢迎并鼓励社区贡献。本指南将帮助您了解如何为 EST 框架贡献代码、文档和想法。

---

## 目录

1. [贡献方式](#1-贡献方式)
2. [开发环境设置](#2-开发环境设置)
3. [代码规范](#3-代码规范)
4. [提交 Pull Request](#4-提交-pull-request)
5. [测试指南](#5-测试指南)
6. [文档贡献](#6-文档贡献)
7. [插件开发](#7-插件开发)
8. [代码审查](#8-代码审查)
9. [问题报告](#9-问题报告)
10. [行为准则](#10-行为准则)
11. [社区参与](#11-社区参与)

---

## 1. 贡献方式

您可以通过多种方式为 EST 框架贡献：

### 1.1 报告问题

- **Bug 报告**：发现框架的问题或错误
- **功能建议**：提出新功能或改进建议
- **文档问题**：指出文档中的错误或不清楚的地方

### 1.2 提交代码

- **Bug 修复**：修复已发现的问题
- **新功能**：实现新的功能特性
- **性能优化**：改进框架性能
- **代码重构**：改进代码结构和可读性

### 1.3 改进文档

- **完善 API 文档**：补充 Javadoc 注释
- **编写教程**：创建新的教程和指南
- **翻译文档**：将文档翻译成其他语言
- **修正错误**：修复文档中的错误

### 1.4 分享经验

- **写博客文章**：分享使用 EST 的经验
- **创建示例项目**：提供实际使用示例
- **回答问题**：帮助社区中的其他用户
- **演讲分享**：在社区活动中分享 EST

---

## 2. 开发环境设置

### 2.1 环境要求

- **JDK**: 21 或更高版本
- **Maven**: 3.6 或更高版本
- **Git**: 最新版本
- **IDE**: IntelliJ IDEA / Eclipse / VS Code（可选）

### 2.2 验证环境

```bash
# 检查 Java 版本
java -version

# 检查 Maven 版本
mvn -version

# 检查 Git 版本
git --version
```

### 2.3 克隆仓库

```bash
# Fork 仓库（在 GitHub 上操作）
# 然后克隆您的 fork
git clone https://github.com/[your-username]/est.git
cd est

# 添加主仓库为上游
git remote add upstream https://github.com/idcu/est.git
```

### 2.4 构建项目

```bash
# 完整构建（包含测试）
mvn clean install

# 跳过测试（更快）
mvn clean install -DskipTests

# 只编译不运行测试
mvn clean compile

# 运行特定模块的测试
cd est-core/est-core-impl
mvn test
```

### 2.5 导入 IDE

**IntelliJ IDEA**:
1. File → Open → 选择 `pom.xml`
2. 等待 Maven 依赖下载完成
3. 确保项目 SDK 设置为 JDK 21

**Eclipse**:
1. File → Import → Existing Maven Projects
2. 选择项目根目录
3. 点击 Finish

---

## 3. 代码规范

### 3.1 编码风格

EST 遵循以下编码规范：

#### 3.1.1 命名约定

| 元素 | 规范 | 示例 |
|------|------|------|
| 类名 | 大驼峰（PascalCase） | `UserService`, `DefaultContainer` |
| 接口名 | 大驼峰，通常以 `I` 开头或使用名词 | `Container`, `Router` |
| 方法名 | 小驼峰（camelCase） | `getUser()`, `createContainer()` |
| 变量名 | 小驼峰（camelCase） | `userId`, `currentUser` |
| 常量 | 全大写下划线分隔 | `MAX_SIZE`, `DEFAULT_TIMEOUT` |
| 包名 | 全小写点分隔 | `ltd.idcu.est.core`, `ltd.idcu.est.web` |

#### 3.1.2 格式规范

- **缩进**: 4 个空格（不要使用 Tab）
- **行宽**: 最大 120 字符
- **空行**: 方法之间空一行，逻辑块之间空一行
- **大括号**: K&R 风格（左大括号不换行）

```java
// 正确
public class MyClass {
    public void myMethod() {
        if (condition) {
            doSomething();
        } else {
            doOtherThing();
        }
    }
}
```

#### 3.1.3 导入规范

- 按分组排序：java → javax → 第三方 → EST
- 每个分组内按字母顺序
- 避免使用通配符导入（`*`）

```java
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
```

### 3.2 设计原则

#### 3.2.1 零依赖原则

- **核心模块**：完全使用 Java 标准库
- **功能模块**：可选项依赖，但保持接口独立
- **扩展模块**：可以有第三方依赖，但可选引入

#### 3.2.2 接口与实现分离

```java
// 接口定义在 api 模块
public interface Container {
    <T> T get(Class<T> type);
}

// 实现在 impl 模块
public class DefaultContainer implements Container {
    @Override
    public <T> T get(Class<T> type) {
        // 实现
    }
}
```

#### 3.2.3 Builder 模式

对于复杂配置对象，使用 Builder 模式：

```java
public class CacheConfig {
    private final int maxSize;
    private final long expireAfterWrite;
    private final boolean evictOnOverflow;
    
    private CacheConfig(Builder builder) {
        this.maxSize = builder.maxSize;
        this.expireAfterWrite = builder.expireAfterWrite;
        this.evictOnOverflow = builder.evictOnOverflow;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private int maxSize = 1000;
        private long expireAfterWrite = 3600;
        private boolean evictOnOverflow = true;
        
        public Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
        
        public Builder expireAfterWrite(long seconds) {
            this.expireAfterWrite = seconds;
            return this;
        }
        
        public Builder evictOnOverflow(boolean evict) {
            this.evictOnOverflow = evict;
            return this;
        }
        
        public CacheConfig build() {
            return new CacheConfig(this);
        }
    }
}

// 使用
CacheConfig config = CacheConfig.builder()
    .maxSize(5000)
    .expireAfterWrite(7200)
    .build();
```

### 3.3 注释规范

#### 3.3.1 Javadoc 注释

所有公共 API 必须有 Javadoc 注释：

```java
/**
 * 依赖注入容器接口，用于管理 Bean 的生命周期和依赖关系。
 *
 * <p>EST 容器支持多种注册方式：
 * <ul>
 *   <li>直接注册：{@link #register(Class, Class)}</li>
 *   <li>单例注册：{@link #registerSingleton(Class, Object)}</li>
 *   <li>Supplier 注册：{@link #registerSupplier(Class, Supplier)}</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>{@code
 * Container container = new DefaultContainer();
 * container.register(UserService.class, UserServiceImpl.class);
 * UserService service = container.get(UserService.class);
 * }</pre>
 *
 * @author EST Team
 * @since 1.0.0
 */
public interface Container {
    
    /**
     * 获取指定类型的 Bean 实例。
     *
     * <p>如果 Bean 尚未创建，会自动创建并注入依赖。
     *
     * @param type Bean 的类型
     * @param <T> Bean 的泛型类型
     * @return Bean 实例
     * @throws NoSuchBeanException 如果没有注册该类型的 Bean
     * @throws CircularDependencyException 如果检测到循环依赖
     */
    <T> T get(Class<T> type);
    
    /**
     * 注册 Bean 类型及其实现类。
     *
     * @param type Bean 的接口类型
     * @param implementation Bean 的实现类
     * @param <T> Bean 的泛型类型
     * @throws IllegalArgumentException 如果 type 或 implementation 为 null
     */
    <T> void register(Class<T> type, Class<? extends T> implementation);
}
```

#### 3.3.2 行内注释

对于复杂的逻辑，添加行内注释说明：

```java
// 使用双重检查锁定实现线程安全的单例
public T get(Class<T> type) {
    String key = type.getName();
    T instance = (T) instances.get(key);
    if (instance == null) {
        synchronized (this) {
            instance = (T) instances.get(key);
            if (instance == null) {
                instance = createInstance(type);
                instances.put(key, instance);
            }
        }
    }
    return instance;
}
```

### 3.4 提交规范

#### 3.4.1 提交消息格式

提交消息应遵循以下格式：

```
[模块名] 简短描述

详细描述（可选）
```

**示例**：

```
[est-core] 修复依赖注入容器的线程安全问题

- 使用 ConcurrentHashMap 替代 HashMap
- 添加双重检查锁定
- 增加线程安全测试用例

Closes #123
```

#### 3.4.2 提交类型

| 类型 | 描述 |
|------|------|
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `docs` | 文档更新 |
| `style` | 代码格式调整 |
| `refactor` | 代码重构 |
| `perf` | 性能优化 |
| `test` | 测试相关 |
| `chore` | 构建/工具相关 |

---

## 4. 提交 Pull Request

### 4.1 准备工作

在提交 PR 之前，请确保：

- [ ] 您已经从 upstream 获取最新代码
- [ ] 代码可以正常编译
- [ ] 所有测试通过
- [ ] 代码符合编码规范
- [ ] 有相应的测试覆盖
- [ ] 更新了相关文档

### 4.2 工作流程

```bash
# 1. 同步上游代码
git fetch upstream
git checkout main
git merge upstream/main

# 2. 创建功能分支
git checkout -b feature/your-feature-name

# 3. 编写代码
# ... 修改文件 ...

# 4. 运行测试
mvn test

# 5. 提交代码
git add .
git commit -m "[est-core] 添加新功能"

# 6. 推送到您的 fork
git push origin feature/your-feature-name

# 7. 在 GitHub 上创建 Pull Request
```

### 4.3 PR 描述模板

创建 PR 时，请使用以下模板：

```markdown
## 描述

简要描述您的更改：

- 解决了什么问题？
- 添加了什么功能？
- 有什么改进？

## 关联 Issue

Closes #123
Related to #456

## 测试

- [ ] 单元测试已添加/更新
- [ ] 集成测试已添加/更新
- [ ] 所有测试通过
- [ ] 手动测试已完成

## 变更类型

- [ ] Bug 修复
- [ ] 新功能
- [ ] 性能优化
- [ ] 代码重构
- [ ] 文档更新
- [ ] 其他（请描述）

## 检查清单

- [ ] 代码符合编码规范
- [ ] 已运行 Checkstyle 检查
- [ ] 已运行 PMD 检查
- [ ] 文档已更新
- [ ] 提交消息格式正确
```

### 4.4 小技巧

- **一个 PR 一个功能**：不要在一个 PR 中包含多个不相关的更改
- **保持 PR 小**：小的 PR 更容易审查和合并
- **及时响应**：积极响应审查意见
- **写好描述**：清晰的描述有助于审查者理解

---

## 5. 测试指南

### 5.1 测试框架

EST 使用 JUnit 5 进行测试：

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

class DefaultContainerTest {
    
    private Container container;
    
    @BeforeEach
    void setUp() {
        container = new DefaultContainer();
    }
    
    @AfterEach
    void tearDown() {
        // 清理
    }
    
    @Test
    void shouldRegisterAndGetBean() {
        container.register(UserService.class, UserServiceImpl.class);
        
        UserService service = container.get(UserService.class);
        
        assertNotNull(service);
        assertTrue(service instanceof UserServiceImpl);
    }
    
    @Test
    void shouldThrowExceptionWhenBeanNotFound() {
        assertThrows(NoSuchBeanException.class, () -> {
            container.get(NonExistentService.class);
        });
    }
}
```

### 5.2 测试分类

#### 5.2.1 单元测试

测试单个类或方法的独立功能：

```java
@Test
void shouldCalculateTotalPrice() {
    Order order = new Order();
    order.addItem(new OrderItem("Product 1", 2, 10.0));
    order.addItem(new OrderItem("Product 2", 1, 20.0));
    
    double total = order.calculateTotal();
    
    assertEquals(40.0, total, 0.001);
}
```

#### 5.2.2 集成测试

测试多个组件的协作：

```java
@Test
void shouldCompleteOrderProcessing() {
    OrderService orderService = container.get(OrderService.class);
    InventoryService inventoryService = container.get(InventoryService.class);
    PaymentService paymentService = container.get(PaymentService.class);
    
    Order order = createTestOrder();
    OrderResult result = orderService.processOrder(order);
    
    assertTrue(result.isSuccess());
    assertTrue(inventoryService.isReserved(order.getItems()));
    assertTrue(paymentService.isProcessed(order.getId()));
}
```

#### 5.2.3 性能基准测试

使用 JMH 进行性能测试：

```java
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class ContainerBenchmark {
    
    private Container container;
    
    @Setup
    public void setup() {
        container = new DefaultContainer();
        container.register(MyService.class, MyServiceImpl.class);
    }
    
    @Benchmark
    public void containerGet() {
        container.get(MyService.class);
    }
}
```

### 5.3 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定模块的测试
cd est-core/est-core-impl
mvn test

# 运行特定测试类
mvn test -Dtest=DefaultContainerTest

# 运行特定测试方法
mvn test -Dtest=DefaultContainerTest#shouldRegisterAndGetBean

# 运行基准测试
cd est-foundation/est-test/est-test-benchmark
mvn clean package
java -jar target/est-benchmarks.jar
```

### 5.4 测试覆盖率

EST 的目标是测试覆盖率达到 80%+。查看覆盖率：

```bash
# 生成覆盖率报告
mvn clean test jacoco:report

# 查看报告
open est-core/est-core-impl/target/site/jacoco/index.html
```

---

## 6. 文档贡献

### 6.1 文档类型

EST 的文档包括：

- **API 文档**：Javadoc 注释生成
- **用户指南**：`docs/guides/` 目录
- **教程**：`docs/tutorials/` 目录
- **最佳实践**：`docs/best-practices/` 目录
- **API 参考**：`docs/api/` 目录

### 6.2 撰写教程

教程结构建议：

```markdown
# 教程标题

## 概述

简要介绍本教程的内容和目标。

## 前置条件

- 需要的知识
- 需要的软件
- 需要的配置

## 步骤 1：xxx

详细描述第一步...

## 步骤 2：xxx

详细描述第二步...

## 完整代码

提供完整的可运行代码示例。

## 总结

总结本教程的要点。

## 下一步

推荐进一步学习的资源。
```

### 6.3 文档格式规范

- 使用 Markdown 格式
- 代码块指定语言：`java`, `bash`, `yaml` 等
- 标题层级清晰
- 使用列表和表格提高可读性
- 提供可运行的示例代码

### 6.4 更新 API 文档

确保所有公共 API 都有完整的 Javadoc 注释：

```java
/**
 * 创建一个新的 Web 应用实例。
 *
 * @param name 应用名称
 * @param version 应用版本
 * @return Web 应用实例
 * @throws NullPointerException 如果 name 或 version 为 null
 * @since 1.0.0
 */
public static WebApplication create(String name, String version) {
    // 实现
}
```

---

## 7. 插件开发

### 7.1 插件结构

EST 插件的标准结构：

```
est-plugin-myfeature/
├── est-plugin-myfeature-api/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── ltd/idcu/est/plugin/myfeature/
│   │               └── api/
│   │                   └── MyFeature.java
│   └── pom.xml
├── est-plugin-myfeature-impl/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── ltd/idcu/est/plugin/myfeature/
│   │               └── impl/
│   │                   └── DefaultMyFeature.java
│   └── pom.xml
└── pom.xml
```

### 7.2 插件接口

```java
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.plugin.api.Plugin;

public class MyFeaturePlugin implements Plugin {
    
    @Override
    public String getName() {
        return "my-feature";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String getDescription() {
        return "My awesome feature plugin";
    }
    
    @Override
    public void install(Container container) {
        container.register(MyFeature.class, DefaultMyFeature.class);
    }
    
    @Override
    public void uninstall(Container container) {
        // 清理资源
    }
}
```

### 7.3 插件配置

```java
@Configuration
public class MyFeatureConfig {
    
    private final String endpoint;
    private final int timeout;
    private final boolean enabled;
    
    private MyFeatureConfig(Builder builder) {
        this.endpoint = builder.endpoint;
        this.timeout = builder.timeout;
        this.enabled = builder.enabled;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String endpoint = "http://localhost:8080";
        private int timeout = 5000;
        private boolean enabled = true;
        
        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }
        
        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
        
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        public MyFeatureConfig build() {
            return new MyFeatureConfig(this);
        }
    }
}
```

---

## 8. 代码审查

### 8.1 审查流程

所有 Pull Request 都会经过以下审查流程：

1. **自动检查**：CI/CD 自动运行测试和代码检查
2. **初步审查**：维护者进行初步审查
3. **详细审查**：深入检查代码质量和设计
4. **反馈修改**：提出修改意见
5. **合并**：审查通过后合并

### 8.2 审查重点

审查者会关注以下方面：

#### 8.2.1 功能与质量
- **功能正确性**：代码是否正确实现了功能
- **代码质量**：代码是否清晰、可维护
- **性能影响**：是否有性能问题
- **测试覆盖**：是否有足够的测试
- **文档更新**：文档是否同步更新
- **向后兼容**：是否破坏现有 API

#### 8.2.2 工具类使用（重要！）

在代码审查中，**必须检查是否正确使用了 EST 框架提供的工具类**：

##### 必须检查的要点：

1. **null 检查是否使用了 AssertUtils/ObjectUtils**
   ```java
   // ❌ 不推荐：手动 null 检查
   if (user == null) {
       throw new IllegalArgumentException("User cannot be null");
   }
   
   // ✅ 推荐：使用 AssertUtils
   AssertUtils.notNull(user, "User cannot be null");
   
   // 条件判断使用 ObjectUtils
   if (ObjectUtils.isNull(user)) {
       // 处理 null
   }
   ```

2. **字符串检查是否使用了 StringUtils**
   ```java
   // ❌ 不推荐：手动字符串检查
   if (str == null || str.trim().isEmpty()) {
       // ...
   }
   
   // ✅ 推荐：使用 StringUtils
   if (StringUtils.isBlank(str)) {
       // ...
   }
   ```

3. **字符串操作是否使用了 StringUtils**
   - 字符串拼接、分割、修剪、大小写转换等
   - 检查是否有重复的字符串处理逻辑

4. **对象比较是否使用了 ObjectUtils**
   ```java
   // ❌ 不推荐：手动 equals 比较（可能有 NPE）
   if (user1.equals(user2)) {
       // ...
   }
   
   // ✅ 推荐：使用 ObjectUtils
   if (ObjectUtils.equals(user1, user2)) {
       // ...
   }
   ```

5. **集合/数组操作是否使用了相应的工具类**
   - 检查是否有重复的集合操作代码

6. **不要重复造轮子**
   - 在编写通用逻辑前，先检查 est-utils 模块是否已有现成的工具方法
   - 常见的操作（null 检查、字符串处理、日期处理等）几乎都有现成的工具类

##### 工具类快速参考：

| 场景 | 推荐使用 | 说明 |
|------|----------|------|
| 参数验证、前置条件检查 | `AssertUtils` | 抛异常的验证 |
| 对象 null 检查 | `ObjectUtils` | 条件判断用 |
| 字符串检查、操作 | `StringUtils` | 字符串相关 |
| 日期时间操作 | `DateUtils` | 日期相关 |
| 数组操作 | `ArrayUtils` | 数组相关 |
| 数字操作 | `NumberUtils` | 数字相关 |
| 类操作 | `ClassUtils` | 反射相关 |

##### 审查否决项：

如果发现以下情况，PR 可能会被要求修改：
- 手动写了 3 处以上相同的 null 检查代码
- 手写了可以用 StringUtils 替代的字符串处理
- 重复实现了工具类中已有的功能
- 没有合理的理由而不使用现有工具类

##### 依赖配置：

确保模块已添加 est-utils 依赖：
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-utils-common</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

### 8.3 接受审查意见

- **保持开放**：接受建设性批评
- **及时响应**：尽快回复审查意见
- **讨论理解**：如有疑问，主动讨论
- **持续改进**：将审查意见视为学习机会

---

## 9. 问题报告

### 9.1 Bug 报告

报告 Bug 时请包含：

- **清晰的标题**：简要描述问题
- **复现步骤**：详细的步骤说明
- **期望行为**：您期望的正确结果
- **实际行为**：实际发生的情况
- **环境信息**：EST 版本、JDK 版本、操作系统
- **示例代码**：可复现的最小代码示例
- **堆栈跟踪**：如果有异常，提供完整堆栈

**Bug 报告模板**：

```markdown
## 描述

简要描述问题...

## 复现步骤

1. 步骤 1
2. 步骤 2
3. 步骤 3

## 期望行为

描述您期望的正确结果...

## 实际行为

描述实际发生的情况...

## 环境信息

- EST 版本：1.3.0-SNAPSHOT
- JDK 版本：21.0.1
- 操作系统：Windows 11 / macOS / Linux

## 示例代码

```java
// 可复现问题的最小代码示例
```

## 堆栈跟踪

```
如果有异常，请粘贴完整的堆栈跟踪...
```
```

### 9.2 功能建议

提出功能建议时请说明：

- **功能名称**：新功能的名称
- **问题背景**：解决什么问题
- **解决方案**：您建议的实现方式
- **使用场景**：什么情况下会用到
- **替代方案**：您考虑过的其他方案
- **优先级**：您认为的重要程度

---

## 10. 行为准则

### 10.1 我们的承诺

为了营造开放和友好的环境，我们承诺：

- 尊重不同的观点和经验
- 优雅地接受建设性批评
- 关注对社区最有利的事情
- 对其他社区成员表示同理心

### 10.2 不可接受的行为

- 使用性化的语言或图像
- 恶意评论或人身攻击
- 公开或私下骚扰
- 未经许可发布他人的私人信息
- 其他不专业或不恰当的行为

### 10.3 执行

如果遇到不可接受的行为，请通过以下方式联系项目维护者：

- GitHub Issues（私密报告）
- 邮件：idcu@qq.com

---

## 11. 社区参与

### 11.1 获取帮助

- **GitHub Issues**：报告问题和寻求帮助
- **文档**：首先查看 `docs/` 目录
- **示例**：查看 `est-examples/` 目录
- **FAQ**：查看 `docs/FAQ.md`

### 11.2 分享经验

- **写博客**：分享您的使用经验
- **录制视频**：创建教程视频
- **在社区活动演讲**：分享 EST 相关话题
- **回答问题**：帮助其他用户

### 11.3 推荐 EST

- 在 GitHub 上加星
- 向朋友和同事推荐
- 在社交媒体分享
- 在您的项目中使用 EST

---

## 12. 致谢

感谢所有为 EST 框架做出贡献的人！

- 代码贡献者
- 问题报告者
- 文档改进者
- 社区参与者

---

## 联系我们

如果您有任何问题或建议，可以通过以下方式联系我们：

- **GitHub Issues**：https://github.com/idcu/est/issues
- **GitHub Discussions**：https://github.com/idcu/est/discussions
- **邮箱**：idcu@qq.com

---

再次感谢您的贡献！🎉
