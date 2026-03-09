# EST Framework 多语言 SDK

**版本**: 2.4.0-SNAPSHOT  
**最后更新**: 2026-03-10

---

## 📋 概览

EST Framework 提供了多种语言的 SDK，支持开发者使用他们熟悉的语言与 EST Framework 进行交互。

### 支持的语言

| 语言 | 状态 | 主要功能 |
|------|------|----------|
| **Python** | ✅ 完善中 | 插件市场API、类型安全、自动重试 |
| **Go** | ✅ 完善中 | 插件市场API、类型安全、自动重试 |
| **TypeScript** | ✅ 完善中 | 插件市场API、类型安全、Promise支持 |
| **Kotlin** | ✅ 已完成 | DSL、协程、扩展函数 |

---

## 🐍 Python SDK

### 安装

```bash
pip install est-framework
```

### 快速开始

```python
from est_sdk import EstClient, PluginMarketplaceClient

client = EstClient(base_url="http://localhost:8080")
marketplace = PluginMarketplaceClient(client)

plugins = marketplace.search_plugins(query="web", category="web")
```

### 目录结构

```
est-python-sdk/
├── est_sdk/
│   ├── __init__.py
│   ├── client.py
│   ├── plugin_marketplace.py
│   ├── types.py
│   └── utils.py
├── examples/
│   └── basic_usage.py
├── tests/
│   ├── test_client.py
│   └── test_types.py
├── setup.py
└── README.md
```

### 功能特性

- ✅ 插件市场API客户端
- ✅ 类型安全的接口（使用Pydantic）
- ✅ 自动重试机制
- ✅ 请求超时控制
- ✅ 完整的错误处理
- ✅ 上下文管理器支持

---

## 🐹 Go SDK

### 安装

```bash
go get github.com/idcu/est-sdk-go
```

### 快速开始

```go
package main

import (
    "fmt"
    "github.com/idcu/est-sdk-go"
)

func main() {
    client := est.NewClient("http://localhost:8080")
    marketplace := est.NewPluginMarketplaceClient(client)
    
    plugins, err := marketplace.SearchPlugins(est.PluginSearchCriteria{
        Query:     "web",
        Category:  "web",
    })
}
```

### 目录结构

```
est-go-sdk/
├── client.go
├── plugin_marketplace.go
├── types.go
├── utils.go
├── go.mod
├── examples/
│   └── basic_usage.go
├── tests/
│   └── client_test.go
└── README.md
```

### 功能特性

- ✅ 插件市场API客户端
- ✅ 类型安全的接口
- ✅ 自动重试机制
- ✅ 请求超时控制
- ✅ 完整的错误处理
- ✅ 使用Resty HTTP客户端

---

## 🟦 TypeScript SDK

### 安装

```bash
npm install @est-framework/sdk
# 或者
yarn add @est-framework/sdk
```

### 快速开始

```typescript
import { EstClient, PluginMarketplaceClient } from '@est-framework/sdk';

const client = new EstClient({ baseUrl: 'http://localhost:8080' });
const marketplace = new PluginMarketplaceClient(client);

const plugins = await marketplace.searchPlugins({ query: 'web', category: 'web' });
```

### 目录结构

```
est-typescript-sdk/
├── src/
│   ├── index.ts
│   ├── client.ts
│   ├── plugin-marketplace.ts
│   ├── types.ts
│   └── utils.ts
├── examples/
│   └── basic-usage.ts
├── tests/
│   └── client.test.ts
├── package.json
├── tsconfig.json
└── README.md
```

### 功能特性

- ✅ 插件市场API客户端
- ✅ 类型安全的接口
- ✅ Promise/async-await支持
- ✅ 请求超时控制
- ✅ 完整的错误处理
- ✅ 使用Axios HTTP客户端

---

## 🎯 Kotlin 支持

### 特性

- **Kotlin DSL**: 声明式API
- **协程集成**: 异步编程支持
- **扩展函数**: 丰富的工具函数
- **Flow支持**: 响应式数据流

### 快速开始

```kotlin
import ltd.idcu.est.kotlin.dsl.*
import ltd.idcu.est.kotlin.coroutines.*

fun main() {
    val app = estApplication("MyApp", "1.0.0") {
        web {
            port = 8080
            router {
                get("/") { req, res ->
                    res.send("Hello Kotlin!")
                }
            }
        }
    }
    
    app.run()
}
```

### 目录结构

```
est-kotlin-api/
├── src/main/kotlin/
│   └── ltd/idcu/est/kotlin/
│       ├── coroutines/
│       ├── dsl/
│       ├── examples/
│       ├── extensions/
│       └── flow/
├── src/test/kotlin/
└── pom.xml
```

---

## 📚 开发指南

### 通用API约定

所有SDK遵循以下约定：

1. **基础URL**: 所有请求都相对于提供的base URL
2. **认证**: 使用Bearer Token认证（api_key参数）
3. **超时**: 默认30秒超时
4. **重试**: 默认3次重试
5. **错误**: 统一的错误处理机制

### 插件市场API端点

所有SDK都实现以下插件市场API：

- `GET /api/v1/plugins/search` - 搜索插件
- `GET /api/v1/plugins/{id}` - 获取插件详情
- `GET /api/v1/plugins/{id}/versions` - 获取插件版本
- `GET /api/v1/plugins/{id}/reviews` - 获取插件评论
- `POST /api/v1/plugins/{id}/reviews` - 添加评论
- `POST /api/v1/plugins/publish` - 发布插件
- `GET /api/v1/plugins/{id}/download` - 下载插件

---

## 🤝 贡献指南

### 如何添加新的SDK

1. 创建新的SDK目录结构
2. 实现基础客户端类
3. 实现插件市场客户端
4. 添加类型定义
5. 编写示例代码
6. 添加测试用例
7. 更新本文档

### 代码规范

- Python: 遵循PEP 8，使用Black格式化
- Go: 遵循Go官方规范，使用gofmt格式化
- TypeScript: 遵循TSLint/ESLint规范
- Kotlin: 遵循Kotlin官方规范

---

## 📞 相关链接

- [EST Framework 主文档](../../README.md)
- [开发计划](../../dev-docs/development-plan-2.4.0.md)
- [路线图](../../dev-docs/roadmap.md)
- [短期实施总结](../../dev-docs/short-term-implementation-summary.md)

---

## 🎉 总结

EST Framework 的多语言 SDK 生态系统正在快速发展，为开发者提供了丰富的选择。无论您使用哪种语言，都能轻松地与 EST Framework 进行交互！

**下一步规划**:
- [ ] 完善各SDK的测试覆盖
- [ ] 添加更多API客户端（除了插件市场）
- [ ] 性能优化和基准测试
- [ ] 更多示例代码和教程
