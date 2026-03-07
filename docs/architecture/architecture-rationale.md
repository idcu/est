# 架构设计理由详解

## 一、为什么选择六层架构？

### 1.1 层级设计的核心思想

新架构采用**六层递进式结构**，从底层到上层依次为：

```
est-examples (示例层)
    ↓
est-tools (工具层)
    ↓
est-app (应用层)
    ↓
est-modules (模块层)
    ↓
est-core (核心层)
    ↓
est-base (基础层)
```

### 1.2 分层的理由

#### 理由1：关注点分离 (Separation of Concerns)
- **基础层**：只关心最基础的工具函数、集合操作
- **核心层**：只关心框架的核心能力（DI、配置、生命周期）
- **模块层**：只关心各类企业级功能（缓存、日志、数据访问等）
- **应用层**：只关心如何用这些功能构建应用
- **工具层**：只关心开发辅助工具
- **示例层**：只关心如何展示使用方法

**好处**：每个层级的开发者只需要关注自己的领域，不需要了解整个框架。

#### 理由2：依赖方向清晰
- 高层依赖低层，低层绝不依赖高层
- 避免循环依赖
- 便于单元测试（可以mock低层依赖）

**依赖关系**：
```
est-examples → est-tools, est-app
est-tools, est-app → est-modules
est-modules → est-core
est-core → est-base
```

#### 理由3：可演进性强
- 新增功能时，清楚地知道放在哪个层级
- 底层稳定后，上层可以快速迭代
- 可以独立演进各个层级

---

## 二、各层级设计理由详解

### 2.1 基础层(est-base)

#### 为什么叫 est-base 而不是 est-foundation？
- "base" 更短、更简洁
- 更符合英文的习惯用法（base class、base library）
- "foundation" 稍显冗长

#### 为什么 est-utils 放在最底层？
- 工具类库是最基础的，不依赖任何其他模块
- 所有其他模块都可能需要用到工具函数
- 工具类应该是"纯函数"，没有副作用

#### est-utils 的内部结构理由：
```
est-utils/
├── est-util-common/      # 通用工具（字符串、日期、反射等）
├── est-util-io/          # IO 工具
└── est-util-format/      # 格式化工具（JSON、XML、YAML）
```
- 按功能领域划分，而不是按实现划分
- 每个子模块可以独立使用
- 便于按需引入

---

### 2.2 核心层(est-core)

#### 为什么要拆分 est-core？
**原问题**：
```
est-core/
├── est-core-api/    # 包含了所有核心接口
└── est-core-impl/   # 包含了所有核心实现
```
- 所有核心功能混在一起，职责不清
- 想单独用 DI 容器，却不得不引入整个 est-core
- 不利于理解和维护

**新方案**：
```
est-core/
├── est-core-container/    # DI 容器
├── est-core-config/       # 核心配置
├── est-core-lifecycle/    # 生命周期管理
├── est-core-module/       # 模块管理
├── est-core-aop/          # AOP 支持
└── est-core-tx/           # 事务管理
```

**理由**：
1. **职责单一**：每个子模块只负责一个核心能力
2. **按需使用**：可以只引入 est-core-container，不需要其他
3. **便于测试**：可以独立测试每个核心能力
4. **演进灵活**：某个核心能力需要重构时，不影响其他

#### 为什么 est-core-container 是核心中的核心？
- DI 容器是框架的"骨架"，其他能力都构建在其上
- 所有组件的生命周期由容器管理
- 配置、AOP、事务等都需要容器的支持

---

### 2.3 模块层(est-modules)

#### 为什么去掉 "features" 前缀？
**原命名**：`est-features-cache`、`est-features-logging`
**新命名**：`est-cache`、`est-logging`

**理由**：
1. **更简洁**：少了 9 个字符
2. **更清晰**：在 `est-modules` 目录下，"features" 已是冗余信息
3. **更好读**：`est-cache` 比 `est-features-cache` 更容易理解

#### 为什么 est-plugin 移到 modules 层？
- 插件系统是一个功能模块，不是扩展
- 它和 cache、logging 等是平等的
- 之前 extensions 层概念模糊

#### 为什么 est-config 移到 modules 层？
- est-core-config 是核心配置（容器、生命周期等）
- est-config 是应用配置（读取 properties、yaml 等）
- 两者职责不同，分开更清晰

#### 为什么模块之间可以互相依赖？
- 例如：est-web 依赖 est-security
- 这是合理的，因为 Web 框架确实需要安全认证
- 但要注意：
  - 避免循环依赖（A→B→A）
  - 依赖关系要清晰文档化
  - 可以通过 SPI 机制解耦

---

### 2.4 应用层(est-app)

#### 为什么要新增 est-app 层？
**原问题**：
- est-web 在 est-extensions 层，但它是核心应用框架
- 没有微服务、控制台应用的位置
- 概念混乱：什么是"扩展"？什么是"核心"？

**新方案**：
```
est-app/
├── est-web/            # Web 应用框架
├── est-microservice/   # 微服务框架（新增）
└── est-console/        # 控制台应用框架（新增）
```

**理由**：
1. **定位清晰**：这一层是"应用框架"，帮助开发者快速构建应用
2. **层级提升**：Web 框架是构建应用的核心，不是"扩展"
3. **预留空间**：为未来新增应用类型（如桌面应用、移动应用）预留位置

#### est-web 的职责是什么？
- 整合 est-modules 中的功能模块
- 提供 Web 应用的"一站式"体验
- 例如：自动配置安全、日志、监控等

---

### 2.5 工具层(est-tools)

#### 为什么 est-db-generator 改名为 est-codegen？
- "db-generator" 暗示只生成数据库代码
- "codegen" 更通用，可以生成各种代码（Controller、Service、DTO 等）
- 功能可以持续扩展，不需要改名

#### 为什么新增 est-cli？
- 命令行工具是开发者的常用工具
- 可以整合 scaffold、migration、codegen 等功能
- 提供统一的入口：`est create project`、`est migrate` 等

---

### 2.6 示例层(est-examples)

#### 为什么保持独立？
- 示例代码不应该影响框架的发布
- 用户可以参考示例，但不会依赖示例
- 便于维护：示例和框架代码分开

---

## 三、命名规范设计理由

### 3.1 模块命名规范

#### 规则1：层级模块用 `est-[层级]`
- `est-base`、`est-core`、`est-modules`、`est-app`、`est-tools`、`est-examples`
- 理由：一眼就能看出是层级模块

#### 规则2：功能模块用 `est-[领域]`
- `est-cache`、`est-logging`、`est-web`
- 理由：简洁明了，领域驱动

#### 规则3：子模块用 `est-[领域]-[细分]`
- `est-cache-memory`、`est-cache-redis`、`est-util-common`
- 理由：清晰表达父子关系

#### 规则4：API/Impl 分离
- `est-cache-api`、`est-cache-memory`
- 理由：
  - 接口和实现分离
  - 可以切换实现而不改变调用代码
  - 便于多实现共存

---

### 3.2 包命名规范

#### 规则：按层级 + 领域组织
```
ltd.idcu.est
├── base
│   ├── util
│   │   ├── common
│   │   ├── io
│   │   └── format
│   ├── collection
│   ├── patterns
│   └── test
├── core
│   ├── container
│   │   ├── api
│   │   └── impl
│   ├── config
│   ├── lifecycle
│   ├── module
│   ├── aop
│   └── tx
├── cache
│   ├── api
│   ├── memory
│   ├── file
│   └── redis
├── web
│   ├── api
│   └── impl
└── ...
```

**理由**：
- 与模块结构对应
- 便于查找
- 清晰的命名空间

---

## 四、依赖管理设计理由

### 4.1 依赖方向原则

#### 原则1：高层依赖低层，绝不反向
```
✓ est-web → est-security → est-core → est-base
✗ est-base → est-core  (错误)
```

**理由**：
- 低层模块更稳定，不应该依赖可能变化的高层
- 便于测试：测试低层时不需要高层
- 避免循环依赖

#### 原则2：同层级可以依赖，但要谨慎
```
✓ est-web → est-security  (同属 modules/app 层，合理)
✗ est-cache → est-web     (不合理，cache 不应该依赖 web)
```

**理由**：
- 有些功能确实需要协作
- 但要确保依赖关系合理
- 考虑用 SPI 解耦

#### 原则3：通过 API 依赖，不要依赖 Impl
```
✓ 依赖 est-cache-api
✗ 依赖 est-cache-memory
```

**理由**：
- 可以切换实现
- 符合"面向接口编程"原则
- 便于单元测试（mock 接口）

---

### 4.2 可选依赖设计

#### 使用 SPI 机制
```
META-INF/services/
└── ltd.idcu.est.cache.spi.CacheProvider
```

**理由**：
- 实现可以按需加载
- 不需要硬编码依赖
- 便于扩展

---

## 五、可扩展性设计理由

### 5.1 预留扩展点

#### 在 API 层定义 SPI
```java
package ltd.idcu.est.cache.spi;

public interface CacheProvider {
    Cache createCache(String name);
}
```

**理由**：
- 第三方可以提供实现
- 不需要修改框架代码
- 符合开闭原则（Open/Closed Principle）

---

### 5.2 模块的可插拔性

#### 每个模块都是独立的
- 可以只引入 est-cache，不需要 est-logging
- 可以只引入 est-web，不需要 est-microservice

**理由**：
- 按需使用，减少依赖
- 便于微服务架构（每个服务只用需要的模块）
- 减小部署包大小

---

## 六、总结

### 新架构的核心优势

1. **清晰**：层级分明，职责明确
2. **简洁**：命名规范，没有冗余
3. **灵活**：可插拔，可扩展
4. **稳健**：依赖清晰，避免循环
5. **演进**：预留空间，持续发展

### 保持不变的核心价值

- 零依赖设计
- API/Impl 分离
- 渐进式使用
- AI 友好

---

## 七、迁移建议

### 第一阶段：重命名（保守）
- 只改模块名和目录名
- 保持包名不变（避免大面积修改代码）
- 确保所有测试通过

### 第二阶段：重构（渐进式）
- 拆分 est-core
- 调整包名
- 保持 API 兼容

### 第三阶段：新增（创新式）
- est-microservice
- est-console
- est-cli

---

**记住**：架构是为了解决问题，不是为了架构而架构。新架构应该让开发更高效，让代码更易维护。
