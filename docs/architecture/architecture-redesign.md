# EST 项目架构分析与重新设计(2.0 版本)

## 一、现有架构分析

### 1.1 当前模块结构

```
est2.0/
├── est-core/              # 核心层
│   ├── est-core-api/
│   └── est-core-impl/
├── est-foundation/        # 基础层
│   ├── est-patterns/
│   ├── est-utils/
│   ├── est-test/
│   └── est-collection/
├── est-features/          # 功能层
│   ├── est-features-cache/
│   ├── est-features-event/
│   ├── est-features-logging/
│   ├── est-features-data/
│   ├── est-features-security/
│   ├── est-features-messaging/
│   ├── est-features-monitor/
│   ├── est-features-scheduler/
│   ├── est-features-event/
│   ├── est-features-circuitbreaker/
│   ├── est-features-config/
│   ├── est-features-discovery/
│   ├── est-features-performance/
│   ├── est-features-hotreload/
│   └── est-features-ai/
├── est-extensions/        # 扩展层
│   ├── est-plugin/
│   └── est-web/
├── est-tools/             # 工具层
│   ├── est-db-generator/
│   ├── est-ide-support/
│   ├── est-migration-tool/
│   └── est-scaffold/
├── est-microservices/     # 微服务层
│   ├── est-microservices-api/
│   └── est-microservices-gateway/  # API 网关
└── est-examples/          # 示例层
```

### 1.2 现有架构的详细缺陷分析

#### 缺陷 1：模块命名不一致且冗余
- **问题描述**：
  - `est-features-*` 前缀重复，模块名冗长
  - 不同层级命名风格不统一（`est-core` vs `est-foundation` vs `est-features`）
  - 缺乏统一的命名规范文档
- **影响**：
  - 新开发者难以快速理解模块定位
  - 代码补全和查找效率低
  - 容易产生命名冲突
- **整改方向**：
  - 统一采用 `est-[领域]` 的简洁命名
  - 制定详细的命名规范并强制执行

#### 缺陷 2：层级边界模糊，职责不清
- **问题描述**：
  - `est-web` 在 `extensions` 层，但实际上是核心应用框架，不是"扩展"
  - `config` 在 `features` 层，但配置属于基础设施能力
  - `est-plugin` 在 `extensions` 层，但插件系统是核心功能模块
  - `est-microservices` 独立存在，与其他模块关系不明确
- **影响**：
  - 模块职责划分不明确
  - 依赖关系容易混乱
  - 维护成本高
- **整改方向**：
  - 重新定义清晰的层级边界
  - 每个层级有明确的职责定位
  - 将模块归类到合适的层级

#### 缺陷 3：核心层职责过重，未细分
- **问题描述**：
  - `est-core` 包含了 DI 容器、配置、生命周期、模块管理、AOP、事务等所有核心能力
  - 所有核心接口混在一个 `est-core-api` 模块中
  - 无法按需引入核心能力（想只用 DI 容器却要引入整个 core）
- **影响**：
  - 模块耦合度高
  - 依赖体积大
  - 不利于独立测试和演进
- **整改方向**：
  - 将 `est-core` 拆分为多个职责单一的子模块
  - 每个核心能力可以独立使用

#### 缺陷 4：foundation 层职责混乱
- **问题描述**：
  - `est-foundation` 包含了 utils（工具）、collection（集合）、patterns（设计模式）、test（测试框架）
  - 这些模块性质差异很大，放在同一层级不合理
- **影响**：
  - 层级概念模糊
  - 不利于理解和维护
- **整改方向**：
  - 统一为 `est-base` 层，定位为最底层基础设施
  - 保持内部按功能划分

#### 缺陷 5：缺少应用框架层
- **问题描述**：
  - 没有专门的应用框架层
  - Web 框架作为"扩展"存在，定位偏差
  - 没有微服务框架、控制台应用框架的位置
- **影响**：
  - 应用构建体验不统一
  - 功能模块整合困难
- **整改方向**：
  - 新增 `est-app` 层，专门提供应用框架
  - 包含 web、microservice、console 等应用类型

#### 缺陷 6：工具层命名和定位不清晰
- **问题描述**：
  - `est-db-generator` 命名暗示只生成数据库代码，但功能可扩充
  - `est-ide-support` 定位模糊
  - 缺少统一的 CLI 入口
- **影响**：
  - 工具扩展性受限
  - 用户体验不统一
- **整改方向**：
  - 重命名为更通用的名字
  - 整合功能，提供统一入口

#### 缺陷 7：依赖关系不明确，缺乏规范
- **问题描述**：
  - 没有明确的依赖方向规范
  - 可能存在循环依赖风险
  - 缺少依赖检查机制
- **影响**：
  - 架构容易腐化
  - 构建和测试困难
- **整改方向**：
  - 制定严格的依赖规范
  - 高层依赖低层，禁止反向依赖
  - 同层级谨慎依赖，避免循环

#### 缺陷 8：缺少可演进性设计
- **问题描述**：
  - 没有明确的模块扩展指导
  - 新增功能时不知道放在哪里
  - 缺少预留的扩展点
- **影响**：
  - 架构容易变得混乱
  - 长期维护成本高
- **整改方向**：
  - 设计清晰的扩展路径
  - 为未来功能预留位置
  - 提供模块开发模板

---

## 二、新架构设计方案

### 2.1 设计原则

#### 核心设计原则

1. **分层清晰 (Clear Layers)**
   - 从上到下依次为：示例层 → 工具层 → 应用层 → 模块层 → 核心层 → 基础层
   - 每个层级有明确的职责边界
   - 高层依赖低层，绝不反向

2. **职责单一 (Single Responsibility)**
   - 每个模块只负责一类功能
   - 每个子模块职责明确
   - 避免"上帝模块"

3. **依赖明确 (Explicit Dependencies)**
   - 依赖关系清晰可追踪
   - 通过 API 依赖，不依赖具体实现
   - 避免循环依赖

4. **命名统一 (Consistent Naming)**
   - 采用简洁、统一的命名规范
   - 模块名直接表达领域
   - 避免冗余前缀

5. **可演进性 (Evolvability)**
   - 预留扩展空间
   - 便于新增功能
   - 支持独立演进

6. **可插拔性 (Pluggability)**
   - 模块可独立使用
   - 支持按需引入
   - 多实现可切换

7. **零依赖 (Zero Dependencies)**
   - 保持核心模块零外部依赖
   - 可选依赖通过 SPI 机制加载
   - 不强制引入不需要的依赖

8. **API/Impl 分离 (API/Impl Separation)**
   - 接口与实现严格分离
   - 面向接口编程
   - 便于测试和扩展

### 2.2 新架构图

```
┌───────────────────────────────────────────────────────────────────────────┐
│                        est-examples/                                      │ 示例层
│ ├─ est-examples-basic/       (基础示例)                                   │
│ ├─ est-examples-web/         (Web 示例)                                   │
│ ├─ est-examples-advanced/    (高级示例)                                   │
│ ├─ est-examples-ai/          (AI 示例)                                    │
│ └─ est-examples-features/    (功能模块示例)                                │
├───────────────────────────────────────────────────────────────────────────┤
│                        est-tools/                                         │ 工具层
│ ├─ est-scaffold/            (脚手架生成器)                                 │
│ ├─ est-migration/           (迁移工具)                                     │
│ ├─ est-codegen/             (代码生成器 - db-generator 扩充)            │
│ └─ est-cli/                 (命令行工具 - 整合 ide-support)                │
├───────────────────────────────────────────────────────────────────────────┤
│                        est-app/                                           │ 应用层
│ ├─ est-web/                (Web 应用框架 - 从 extensions 提升)             │
│ ├─ est-microservice/       (微服务框架 - 整合 est-microservices)        │
│ └─ est-console/            (控制台应用框架 - 新增)                          │
├───────────────────────────────────────────────────────────────────────────┤
│                        est-modules/                                       │ 模块层
│ ├─ est-cache/              (缓存模块)                                      │
│ ├─ est-logging/            (日志模块)                                      │
│ ├─ est-data/               (数据访问模块)                                  │
│ ├─ est-security/           (安全认证模块)                                  │
│ ├─ est-messaging/          (消息模块)                                      │
│ ├─ est-monitor/            (监控模块)                                      │
│ ├─ est-scheduler/          (调度模块)                                      │
│ ├─ est-event/              (事件总线模块)                                  │
│ ├─ est-circuitbreaker/     (熔断器模块)                                    │
│ ├─ est-discovery/          (服务发现模块)                                  │
│ ├─ est-config/             (配置模块 - 应用配置)                            │
│ ├─ est-performance/        (性能模块)                                      │
│ ├─ est-plugin/             (插件模块 - 从 extensions 移入)                  │
│ ├─ est-hotreload/          (热重载模块)                                    │
│ ├─ est-gateway/            (API 网关模块 - 从 microservices 移入)          │
│ └─ est-ai/                 (AI 模块)                                       │
├───────────────────────────────────────────────────────────────────────────┤
│                        est-core/                                          │ 核心层
│ ├─ est-core-container/     (依赖注入容器)                                  │
│ ├─ est-core-config/        (核心配置 - 容器、生命周期等配置)                 │
│ ├─ est-core-lifecycle/     (生命周期管理)                                  │
│ ├─ est-core-module/        (模块管理)                                      │
│ ├─ est-core-aop/           (AOP 支持)                                     │
│ └─ est-core-tx/            (事务管理)                                      │
├───────────────────────────────────────────────────────────────────────────┤
│                        est-base/                                          │ 基础层
│ ├─ est-utils/              (工具类库)                                      │
│ │  ├─ est-util-common/    (通用工具：字符串、日期、反射等)                  │
│ │  ├─ est-util-io/        (IO 工具)                                      │
│ │  └─ est-util-format/    (格式化工具：JSON、XML、YAML)                    │
│ ├─ est-collection/         (集合增强)                                      │
│ ├─ est-patterns/           (设计模式)                                      │
│ └─ est-test/               (测试框架)                                      │
└───────────────────────────────────────────────────────────────────────────┘
```

### 2.3 新模块命名规范

#### 2.3.1 通用命名规则

| 规则 | 格式 | 示例 | 说明 |
|------|------|------|------|
| 层级模块 | `est-[层级]` | `est-core`, `est-base` | 一眼识别层级 |
| 功能模块 | `est-[领域]` | `est-cache`, `est-web` | 简洁明了，领域驱动 |
| 子模块 | `est-[领域]-[细分]` | `est-cache-memory`, `est-util-common` | 清晰表达父子关系 |
| API 模块 | `est-[领域]-api` | `est-cache-api` | 接口定义层 |
| SPI 模块 | `est-[领域]-spi` | `est-cache-spi` | 服务提供接口（可选） |
| 实现模块 | `est-[领域]-[实现名]` | `est-cache-redis` | 具体实现 |

#### 2.3.2 禁止使用的命名

- ❌ 避免冗余前缀：`est-features-cache` → `est-cache`
- ❌ 避免层级标识：`est-extensions-web` → `est-web`
- ❌ 避免缩写不清：`est-db-gen` → `est-codegen`
- ❌ 避免使用复数（除非领域本身是复数）：`est-features` → `est-modules`（特殊情况）

#### 2.3.3 完整模块列表及映射

| 2.0 模块 | 1.X 来源 | 说明 |
|-----------|----------|------|
| **est-base** | | **基础层** |
| est-utils | est-foundation/est-utils | 工具类库 |
| est-collection | est-foundation/est-collection | 集合增强 |
| est-patterns | est-foundation/est-patterns | 设计模式 |
| est-test | est-foundation/est-test | 测试框架 |
| **est-core** | | **核心层** |
| est-core-container | est-core (拆分) | 依赖注入容器 |
| est-core-config | est-core (拆分) | 核心配置 |
| est-core-lifecycle | est-core (拆分) | 生命周期管理 |
| est-core-module | est-core (拆分) | 模块管理 |
| est-core-aop | est-core (拆分) | AOP 支持 |
| est-core-tx | est-core (拆分) | 事务管理 |
| **est-modules** | | **模块层** |
| est-cache | est-features/est-features-cache | 缓存模块 |
| est-logging | est-features/est-features-logging | 日志模块 |
| est-data | est-features/est-features-data | 数据访问模块 |
| est-security | est-features/est-features-security | 安全认证模块 |
| est-messaging | est-features/est-features-messaging | 消息模块 |
| est-monitor | est-features/est-features-monitor | 监控模块 |
| est-scheduler | est-features/est-features-scheduler | 调度模块 |
| est-event | est-features/est-features-event | 事件总线模块 |
| est-circuitbreaker | est-features/est-features-circuitbreaker | 熔断器模块 |
| est-discovery | est-features/est-features-discovery | 服务发现模块 |
| est-config | est-features/est-features-config | 配置模块（应用配置） |
| est-performance | est-features/est-features-performance | 性能模块 |
| est-plugin | est-extensions/est-plugin | 插件模块 |
| est-hotreload | est-features/est-features-hotreload | 热重载模块 |
| est-gateway | est-microservices/est-microservices-gateway | API 网关模块 |
| est-ai | est-features/est-features-ai | AI 模块 |
| **est-app** | | **应用层** |
| est-web | est-extensions/est-web | Web 应用框架 |
| est-microservice | est-microservices | 微服务框架 |
| est-console | (新增) | 控制台应用框架 |
| **est-tools** | | **工具层** |
| est-scaffold | est-tools/est-scaffold | 脚手架生成器 |
| est-migration | est-tools/est-migration-tool | 迁移工具 |
| est-codegen | est-tools/est-db-generator (重命名+扩充) | 代码生成器 |
| est-cli | est-tools/est-ide-support (整合) | 命令行工具 |
| **est-examples** | | **示例层** |
| est-examples-basic | est-examples/est-examples-basic | 基础示例 |
| est-examples-web | est-examples/est-examples-web | Web 示例 |
| est-examples-advanced | est-examples/est-examples-advanced | 高级示例 |
| est-examples-ai | est-examples/est-examples-ai | AI 示例 |
| est-examples-features | est-examples/est-examples-features | 功能模块示例 |

### 2.4 模块内部结构规范

#### 2.4.1 标准功能模块结构

每个功能模块采用统一的内部结构：

```
est-[领域]/
├── est-[领域]-api/        # 接口定义层（必需）
│   ├── src/main/java/
│   │   └── ltd/idcu/est/[领域]/api/
│   │       ├── [Interface1].java
│   │       ├── [Interface2].java
│   │       ├── annotation/       # 注解定义
│   │       ├── exception/        # 异常定义
│   │       ├── model/            # 数据模型（DTO、VO等）
│   │       └── spi/              # 服务提供接口（可选）
│   ├── src/test/java/
│   └── pom.xml
├── est-[领域]-spi/        # 服务提供接口（可选，独立模块时）
│   ├── src/main/java/
│   │   └── ltd/idcu/est/[领域]/spi/
│   └── pom.xml
├── est-[领域]-[实现1]/    # 具体实现 1
│   ├── src/main/java/
│   │   └── ltd/idcu/est/[领域]/[实现1]/
│   │       ├── [Implementation1].java
│   │       └── internal/         # 内部实现，不对外暴露
│   ├── src/test/java/
│   ├── pom.xml
│   └── src/main/resources/
│       └── META-INF/services/    # SPI 配置
│           └── ltd.idcu.est.[domain].spi.[ProviderInterface]
├── est-[领域]-[实现2]/    # 具体实现 2
│   └── ...
├── README.md               # 模块说明文档
└── pom.xml                 # 父POM，管理子模块
```

#### 2.4.2 核心层模块结构

核心层模块略有不同，因为它们可能更紧密耦合：

```
est-core-[能力]/
├── est-core-[能力]-api/
│   ├── src/main/java/
│   │   └── ltd/idcu/est/core/[能力]/api/
│   └── pom.xml
└── est-core-[能力]-impl/
    ├── src/main/java/
    │   └── ltd/idcu/est/core/[能力]/impl/
    └── pom.xml
```

#### 2.4.3 应用层模块结构

应用层模块整合多个功能模块：

```
est-[应用类型]/
├── est-[应用类型]-api/
│   ├── src/main/java/
│   │   └── ltd/idcu/est/[应用类型]/api/
│   └── pom.xml
└── est-[应用类型]-impl/
    ├── src/main/java/
    │   └── ltd/idcu/est/[应用类型]/impl/
    │       ├── autoconfigure/    # 自动配置
    │       └── starter/          # 启动器
    └── pom.xml
```

#### 2.4.4 包命名规范

所有包遵循统一的命名规范：

```
ltd.idcu.est
├── base                    # 基础层
│   ├── util
│   │   ├── common
│   │   ├── io
│   │   └── format
│   ├── collection
│   ├── patterns
│   └── test
├── core                    # 核心层
│   ├── container
│   │   ├── api
│   │   └── impl
│   ├── config
│   ├── lifecycle
│   ├── module
│   ├── aop
│   └── tx
├── [domain]                # 模块层（如 cache、logging）
│   ├── api
│   ├── spi
│   ├── [impl1]
│   └── [impl2]
├── [app-type]              # 应用层（如 web、microservice）
│   ├── api
│   └── impl
└── ...
```

**包命名规则**：
- 全部小写
- 使用域名词，不使用复数（除非领域本身是复数）
- `api` 包存放公开接口
- `impl` 或具体实现名存放实现
- `internal` 包存放不对外暴露的内部实现
- `spi` 包存放服务提供接口

---

## 三、架构理由详解

### 3.1 各层级设计理由详解

#### 3.1.1 基础层 (est-base)

**为什么叫 est-base 而不是 est-foundation？**
- "base" 更短、更简洁
- 更符合英文的习惯用法（base class、base library）
- "foundation" 稍显冗长

**为什么 est-utils 放在最底层？**
- 工具类库是最基础的，不依赖任何其他模块
- 所有其他模块都可能需要用到工具函数
- 工具类应该是"纯函数"，没有副作用

**est-utils 的内部结构理由：**
```
est-utils/
├── est-util-common/      # 通用工具（字符串、日期、反射等）
├── est-util-io/          # IO 工具
└── est-util-format/      # 格式化工具（JSON、XML、YAML）
```
- 按功能领域划分，而不是按实现划分
- 每个子模块可以独立使用
- 便于按需引入

#### 3.1.2 核心层 (est-core)

**为什么要拆分 est-core？**

**原问题：**
```
est-core/
├── est-core-api/    # 包含了所有核心接口
└── est-core-impl/   # 包含了所有核心实现
```
- 所有核心功能混在一起，职责不清
- 想单独用 DI 容器，却不得不引入整个 est-core
- 不利于理解和维护

**新方案：**
```
est-core/
├── est-core-container/    # DI 容器
├── est-core-config/       # 核心配置
├── est-core-lifecycle/    # 生命周期管理
├── est-core-module/       # 模块管理
├── est-core-aop/          # AOP 支持
└── est-core-tx/           # 事务管理
```

**理由：**
1. **职责单一**：每个子模块只负责一个核心能力
2. **按需使用**：可以只引入 est-core-container，不需要其他
3. **便于测试**：可以独立测试每个核心能力
4. **演进灵活**：某个核心能力需要重构时，不影响其他

**为什么 est-core-container 是核心中的核心？**
- DI 容器是框架的"骨架"，其他能力都构建在其上
- 所有组件的生命周期由容器管理
- 配置、AOP、事务等都需要容器的支持

#### 3.1.3 模块层 (est-modules)

**为什么去掉 "features" 前缀？**
- **原命名**：`est-features-cache`、`est-features-logging`
- **新命名**：`est-cache`、`est-logging`
- **理由**：
  1. **更简洁**：少了 9 个字符
  2. **更清晰**：在 `est-modules` 目录下，"features" 已是冗余信息
  3. **更好记**：`est-cache` 比 `est-features-cache` 更容易理解
  4. **更符合习惯**：类似 Spring 的 `spring-cache`、`spring-security`

**为什么 est-plugin 移到 modules 层？**
- 插件系统是一个功能模块，不是扩展
- 它和 cache、logging 等是平等的
- `extensions` 层概念模糊，什么是"扩展"没有明确定义

**为什么有两个 config 模块？**
- `est-core-config`：核心配置（容器、生命周期等框架自身的配置）
- `est-config`：应用配置（读取 properties、yaml 等应用级配置）
- 两者职责不同，分开更清晰

**为什么模块之间可以互相依赖？**
- 例如：est-web 依赖 est-security
- 这是合理的，因为 Web 框架确实需要安全认证
- 但要注意：
  - 避免循环依赖（A→B→A）
  - 依赖关系要清晰文档化
  - 可以通过 SPI 机制解耦

#### 3.1.4 应用层 (est-app)

**为什么要新增 est-app 层？**
- **原问题**：
  - est-web 在 est-extensions 层，但它是核心应用框架
  - 没有微服务、控制台应用的位置
  - 概念混乱：什么是"扩展"？什么是"核心"？

**新方案：**
```
est-app/
├── est-web/            # Web 应用框架
├── est-microservice/   # 微服务框架（新增）
└── est-console/        # 控制台应用框架（新增）
```

**理由：**
1. **定位清晰**：这一层是"应用框架"，帮助开发者快速构建应用
2. **层级提升**：Web 框架是构建应用的核心，不是"扩展"
3. **预留空间**：为未来新增应用类型（如桌面应用、移动应用）预留位置

**est-web 的职责是什么？**
- 整合 est-modules 中的功能模块
- 提供 Web 应用的"一站式"体验
- 例如：自动配置安全、日志、监控等

#### 3.1.5 工具层 (est-tools)

**为什么 est-db-generator 改名为 est-codegen？**
- "db-generator" 暗示只生成数据库代码
- "codegen" 更通用，可以生成各种代码（Controller、Service、DTO 等）
- 功能可以持续扩展，不需要改名

**为什么新增 est-cli？**
- 命令行工具是开发者的常用工具
- 可以整合 scaffold、migration、codegen 等功能
- 提供统一的入口：`est create project`、`est migrate` 等

#### 3.1.6 示例层 (est-examples)

**为什么保持独立？**
- 示例代码不应该影响框架的发布
- 用户可以参考示例，但不会依赖示例
- 便于维护：示例和框架代码分开

---

## 四、完整迁移计划

### 4.1 迁移原则

1. **渐进式迁移**：分阶段进行，避免大爆炸式重构
2. **保持兼容**：每个阶段保持 API 兼容，提供迁移工具
3. **测试先行**：每个阶段完成后确保所有测试通过
4. **文档同步**：及时更新文档，记录变更
5. **工具辅助**：提供迁移工具，减少手动工作

### 4.2 阶段一：准备阶段(Week 1-2)

#### 目标
- 建立 2.0 分支
- 完成基础设施准备
- 制定详细的迁移检查清单

#### 任务清单
- [x] 创建 `est-2.0` 分支
- [x] 建立 2.0 版本的 CI/CD 流水线
- [x] 完善代码质量检查规范
- [x] 准备迁移工具（est-migration 模块）
- [x] 制定代码风格规范
- [x] 准备文档模板
- [x] 备份 1.X 版本（打标签）
- [x] 完成所有 1.X 测试，确保基线稳定

### 4.3 阶段二：基础层重构(Week 3-4)

#### 目标
- 重命名和重构基础层
- 确保基础层稳定

#### 任务清单
- [ ] 重命名 `est-foundation` 为 `est-base`
- [ ] 调整 `est-utils` 内部结构
- [ ] 更新所有模块对 foundation 的依赖为 base
- [ ] 更新所有相关文档
- [ ] 运行所有测试，确保通过
- [ ] 性能基准测试，确保无性能回退

### 4.4 阶段三：核心层拆分(Week 5-8)

#### 目标
- 拆分 est-core 为多个子模块
- 保持 API 兼容

#### 任务清单
- [ ] 创建 est-core-container 模块
- [ ] 创建 est-core-config 模块
- [ ] 创建 est-core-lifecycle 模块
- [ ] 创建 est-core-module 模块
- [ ] 创建 est-core-aop 模块
- [ ] 创建 est-core-tx 模块
- [ ] 从 est-core-api 中拆分接口到各子模块的 api
- [ ] 从 est-core-impl 中拆分实现到各子模块的 impl
- [ ] 保留 est-core-api 作为兼容层
- [ ] 保留 est-core-impl 作为兼容层
- [ ] 标记旧模块为 Deprecated
- [ ] 提供迁移指南
- [ ] 为每个核心子模块编写单元测试
- [ ] 运行所有现有测试，确保通过
- [ ] 集成测试
- [ ] 性能测试

### 4.5 阶段四：模块层重构(Week 9-12)

#### 目标
- 重命名和重构功能模块
- 移动 est-plugin 和 est-gateway 到 modules 层

#### 任务清单
- [ ] 重命名所有 `est-features-*` 为 `est-*`
- [ ] 从 `est-extensions/est-plugin` 移动到 `est-modules/est-plugin`
- [ ] 从 `est-microservices/est-microservices-gateway` 移动到 `est-modules/est-gateway`
- [ ] 创建 est-modules 目录的 pom.xml
- [ ] 将所有功能模块移到 est-modules
- [ ] 更新根 pom.xml
- [ ] 提供兼容模块（重定向到新模块）
- [ ] 标记旧模块为 Deprecated
- [ ] 提供迁移脚本
- [ ] 更新所有测试的依赖
- [ ] 运行所有测试，确保通过

### 4.6 阶段五：应用层重构(Week 13-14)

#### 目标
- 创建应用层
- 移动和整合应用框架

#### 任务清单
- [ ] 创建 est-app 目录的 pom.xml
- [ ] 创建 est-app/est-web 模块
- [ ] 从 est-extensions/est-web 移动到 est-app/est-web
- [ ] 从 est-microservices 移动到 est-app/est-microservice
- [ ] 创建 est-app/est-console 模块
- [ ] 实现控制台应用框架
- [ ] 删除空的 est-extensions 目录
- [ ] 更新根 pom.xml
- [ ] 更新所有测试
- [ ] 运行所有测试，确保通过

### 4.7 阶段六：工具层重构(Week 15-16)

#### 目标
- 重构和完善工具层

#### 任务清单
- [ ] 重命名 est-tools/est-db-generator 为 est-tools/est-codegen
- [ ] 整合 est-tools/est-ide-support 到 est-tools/est-cli
- [ ] 重命名 est-tools/est-migration-tool 为 est-tools/est-migration
- [ ] 实现命令行工具
- [ ] 整合 scaffold、migration、codegen 功能
- [ ] 提供统一入口
- [ ] 测试所有工具
- [ ] 更新文档

### 4.8 阶段七：示例层重构(Week 17)

#### 目标
- 重构示例层
- 更新所有示例

#### 任务清单
- [ ] 重构 est-examples 内部结构
- [ ] 更新所有示例的依赖
- [ ] 更新示例代码
- [ ] 确保所有示例可运行
- [ ] 新增示例展示新功能

### 4.9 阶段八：完善和测试(Week 18-20)

#### 目标
- 完善新架构
- 全面测试
- 性能优化

#### 任务清单
- [ ] 代码审查
- [ ] 静态代码分析
- [ ] 代码格式化
- [ ] 补充 Javadoc
- [ ] 单元测试覆盖率达到 80%+
- [ ] 集成测试
- [ ] 性能基准测试
- [ ] 兼容性测试（与 1.X 对比）
- [ ] 更新所有文档
- [ ] 编写迁移指南
- [ ] 编写新架构教程
- [ ] 更新 API 文档
- [ ] 性能优化
- [ ] 依赖优化
- [ ] 构建优化

### 4.10 阶段九：发布准备(Week 21-22)

#### 目标
- 准备 2.0 发布
- 完成最终验证

#### 任务清单
- [ ] 最终测试
- [ ] 性能基准测试报告
- [ ] 更新版本号为 2.0.0
- [ ] 编写发布说明
- [ ] 准备发布包
- [ ] 发布到 Maven 仓库（可选）
- [ ] 合并到 main 分支（可选）
- [ ] 打标签 v2.0.0

---

## 五、新架构的优势总结

### 5.1 对比 1.X 的改进

| 方面 | 1.X | 2.0 | 改进 |
|------|-----|-----|------|
| 层级清晰度 | 模糊，extensions 层概念不明 | 6 层清晰分层 | ✅ 大幅提升 |
| 模块命名 | 冗长，features 前缀重复 | 简洁，est-[领域] | ✅ 大幅提升 |
| 核心层 | 职责过重，未拆分 | 拆分为 6 个子模块 | ✅ 大幅提升 |
| 应用框架 | web 在 extensions 层 | 独立 app 层 | ✅ 大幅提升 |
| 可演进性 | 缺少明确指导 | 清晰的扩展路径 | ✅ 大幅提升 |
| 依赖管理 | 缺乏规范 | 严格的依赖规范 | ✅ 提升 |
| 零依赖 | ✅ | ✅ | 保持 |
| API/Impl 分离 | ✅ | ✅ | 保持 |
| 可插拔性 | ✅ | ✅ | 保持 |

### 5.2 核心价值保持

新架构保持了 1.X 的核心价值：

1. **零依赖设计** - 核心模块不依赖任何外部库
2. **API/Impl 分离** - 接口与实现严格分离
3. **渐进式使用** - 可以按需引入模块
4. **AI 友好** - 标准化 API、清晰模式、丰富示例

### 5.3 新架构带来的好处

1. **更清晰的层级** - 从基础到应用，层级分明
2. **更简洁的命名** - 去掉冗余前缀，模块名一目了然
3. **更明确的职责** - 每个模块知道自己该做什么
4. **更好的可扩展性** - 新增功能时知道放在哪里
5. **更强的演进能力** - 为未来预留了扩展空间
6. **更灵活的依赖管理** - 可以按需引入核心能力
7. **更好的可测试性** - 模块职责单一，易于测试

---

**文档版本**: 1.0.0  
**最后更新**: 2026-03-06  
**维护者**: EST 架构团队
