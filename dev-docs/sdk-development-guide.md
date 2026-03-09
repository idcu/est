# EST Framework SDK 开发指南

**日期**: 2026-03-10  
**版本**: 2.4.0-SNAPSHOT  
**状态**: ✅ 持续更新中

---

## 📋 目录

1. [概述](#概述)
2. [SDK架构](#sdk架构)
3. [开发环境配置](#开发环境配置)
4. [测试指南](#测试指南)
5. [性能基准测试](#性能基准测试)
6. [发布指南](#发布指南)
7. [贡献指南](#贡献指南)
8. [常见问题](#常见问题)

---

## 概述

EST Framework 提供多语言SDK，支持与EST Framework进行无缝交互。本指南详细介绍各SDK的开发、测试和发布流程。

### 支持的SDK

| 语言 | 状态 | HTTP客户端 | 包管理器 |
|------|------|-----------|---------|
| **Python** | ✅ 生产就绪 | requests | PyPI |
| **Go** | ✅ 生产就绪 | resty | pkg.go.dev |
| **TypeScript** | ✅ 生产就绪 | axios | npm |
| **Kotlin** | ✅ 原生支持 | OkHttp | Maven/Gradle |

---

## SDK架构

### 通用架构模式

所有SDK遵循相同的架构模式：

```
SDK/
├── 核心客户端 (Client)
│   ├── HTTP请求封装
│   ├── 认证管理
│   ├── 重试机制
│   └── 错误处理
├── API客户端 (API Clients)
│   ├── 插件市场客户端
│   ├── 可观测性客户端
│   └── 其他API客户端
├── 类型定义 (Types)
│   ├── 请求类型
│   ├── 响应类型
│   └── 错误类型
└── 工具函数 (Utils)
    ├── 序列化
    ├── 验证
    └── 通用工具
```

### Python SDK架构

```python
est_sdk/
├── __init__.py           # 模块导出
├── client.py            # EstClient核心客户端
├── plugin_marketplace.py # PluginMarketplaceClient
├── types.py             # Pydantic模型定义
└── utils.py             # 工具函数
```

**关键特性**:
- 使用Pydantic进行类型安全
- requests.Session进行连接池管理
- 上下文管理器支持
- 自动重试机制

### Go SDK架构

```go
est/
├── client.go              # Client结构体
├── plugin_marketplace.go  # PluginMarketplaceClient
├── types.go               # 类型定义
└── utils.go               # 工具函数
```

**关键特性**:
- 使用resty进行HTTP请求
- 结构体类型安全
- 标准库testing包支持
- 内置基准测试支持

### TypeScript SDK架构

```typescript
src/
├── index.ts              # 主入口
├── client.ts            # EstClient类
├── plugin-marketplace.ts # PluginMarketplaceClient
├── types.ts             # TypeScript接口
└── utils.ts             # 工具函数
```

**关键特性**:
- TypeScript类型安全
- Promise/async-await支持
- axios进行HTTP请求
- Jest测试框架

---

## 开发环境配置

### Python SDK开发环境

#### 前置要求
- Python 3.8+
- pip

#### 安装步骤

```bash
# 克隆仓库
git clone https://github.com/idcu/est.git
cd est/est-modules/est-kotlin-support/est-python-sdk

# 安装开发依赖
pip install -e ".[dev]"

# 验证安装
python -c "import est_sdk; print(est_sdk.__version__)"
```

#### 开发依赖说明

```python
# setup.py中的extras_require
"dev": [
    "pytest>=7.0.0",           # 测试框架
    "pytest-cov>=4.0.0",       # 覆盖率
    "black>=23.0.0",           # 代码格式化
    "mypy>=1.0.0",             # 类型检查
    "setuptools>=65.0.0",      # 构建
    "wheel>=0.38.0",           # 轮子
    "twine>=4.0.0",            # 发布
]
```

### Go SDK开发环境

#### 前置要求
- Go 1.19+
- make (可选，推荐)

#### 安装步骤

```bash
# 克隆仓库
git clone https://github.com/idcu/est.git
cd est/est-modules/est-kotlin-support/est-go-sdk

# 下载依赖
go mod download

# 验证安装
go test -v ./...
```

#### 项目依赖

```go
// go.mod
require (
    github.com/go-resty/resty/v2 v2.10.0
)
```

### TypeScript SDK开发环境

#### 前置要求
- Node.js 16+
- npm 或 yarn

#### 安装步骤

```bash
# 克隆仓库
git clone https://github.com/idcu/est.git
cd est/est-modules/est-kotlin-support/est-typescript-sdk

# 安装依赖
npm install
# 或
yarn install

# 验证安装
npm test
```

#### 开发依赖说明

```json
{
  "devDependencies": {
    "@types/node": "^20.0.0",      // Node类型
    "typescript": "^5.0.0",         // TypeScript
    "@types/jest": "^29.0.0",      // Jest类型
    "jest": "^29.0.0",             // 测试框架
    "ts-jest": "^29.0.0"           // TypeScript预处理器
  },
  "dependencies": {
    "axios": "^1.6.0"               // HTTP客户端
  }
}
```

---

## 测试指南

### Python SDK测试

#### 运行测试

```bash
# 运行所有测试
pytest

# 运行特定测试文件
pytest tests/test_client.py

# 运行特定测试函数
pytest tests/test_client.py::TestEstClient::test_client_initialization

# 详细输出
pytest -v

# 停止在第一个失败
pytest -x
```

#### 测试覆盖率

```bash
# 运行带覆盖率的测试
pytest --cov=est_sdk

# 生成HTML报告
pytest --cov=est_sdk --cov-report=html

# 查看覆盖率阈值（如果配置了pytest.ini）
pytest --cov=est_sdk --cov-fail-under=80
```

#### 测试覆盖率配置

```ini
# pytest.ini
[pytest]
testpaths = tests
python_files = test_*.py
addopts = 
    --strict-markers
    --cov=est_sdk
    --cov-report=term-missing
    --cov-report=html:htmlcov
    --cov-fail-under=80
```

### Go SDK测试

#### 运行测试

```bash
# 使用Makefile（推荐）
make test

# 或直接使用go test
go test -v ./...

# 运行特定包的测试
go test -v ./tests

# 运行特定测试函数
go test -v -run TestClientInitialization ./...
```

#### 测试覆盖率

```bash
# 使用Makefile
make test-coverage

# 或手动生成
go test -v -coverprofile=coverage.out ./...
go tool cover -html=coverage.out -o coverage.html

# 查看覆盖率统计
go tool cover -func=coverage.out
```

### TypeScript SDK测试

#### 运行测试

```bash
# 运行所有测试
npm test
# 或
yarn test

# 监听模式
npm test -- --watch
# 或
yarn test --watch

# 运行特定测试文件
npm test -- client.test.ts
```

#### 测试覆盖率

```bash
# 运行带覆盖率的测试
npm run test:coverage
# 或
yarn test:coverage

# 查看HTML报告
# 打开 coverage/lcov-report/index.html
```

#### Jest配置

```javascript
// jest.config.js
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  collectCoverage: true,
  collectCoverageFrom: [
    'src/**/*.{ts,tsx}',
    '!src/**/*.d.ts'
  ],
  coverageThreshold: {
    global: {
      branches: 80,
      functions: 80,
      lines: 80,
      statements: 80
    }
  }
};
```

---

## 性能基准测试

### Python SDK基准测试

#### 运行基准测试

```bash
# 运行性能基准测试
pytest tests/benchmark_test.py -v

# 运行并显示详细输出
pytest tests/benchmark_test.py -v -s
```

#### 基准测试内容

- 客户端初始化性能
- 类型创建性能
- 请求处理性能
- 内存使用分析
- 并发操作性能

### Go SDK基准测试

#### 运行基准测试

```bash
# 使用Makefile
make test-bench

# 或手动运行
go test -v -bench=. -benchmem ./...

# 运行特定基准测试
go test -v -bench=BenchmarkClientInitialization -benchmem ./...

# 增加运行次数
go test -v -bench=. -benchmem -count=5 ./...
```

#### 基准测试说明

```go
// 标准基准测试函数
func BenchmarkClientInitialization(b *testing.B) {
    b.ResetTimer()  // 重置计时器
    for i := 0; i < b.N; i++ {
        _ = NewClient("http://localhost:8080")
    }
}

// 带内存统计的基准测试
// 使用 -benchmem 标志
```

#### 基准测试结果解读

```
goos: windows
goarch: amd64
pkg: github.com/idcu/est-sdk-go
BenchmarkClientInitialization-8    10000000    123.4 ns/op    48 B/op    1 allocs/op
```

- `10000000`: 运行次数
- `123.4 ns/op`: 每次操作纳秒数
- `48 B/op`: 每次操作分配字节数
- `1 allocs/op`: 每次操作分配次数

### TypeScript SDK基准测试

#### 运行基准测试

```bash
# 运行性能基准测试
npm run test:benchmark
# 或
yarn test:benchmark
```

#### 基准测试内容

- 客户端初始化性能
- 类型对象创建性能
- 内存使用分析
- 并发操作性能

---

## 发布指南

### Python SDK发布

#### 构建包

```bash
# 清理旧的构建
rm -rf build/ dist/ *.egg-info/

# 构建源码包和轮子
python setup.py sdist bdist_wheel

# 检查包
twine check dist/*
```

#### 发布到PyPI

```bash
# 测试PyPI（推荐先测试）
twine upload --repository testpypi dist/*

# 正式发布到PyPI
twine upload dist/*
```

#### 版本号管理

遵循语义化版本（Semantic Versioning）:
- `MAJOR`: 不兼容的API修改
- `MINOR`: 向下兼容的功能性新增
- `PATCH`: 向下兼容的问题修正

### Go SDK发布

#### 版本标签

```bash
# 创建标签
git tag -a v1.0.0 -m "Release version 1.0.0"

# 推送标签
git push origin v1.0.0
```

#### pkg.go.dev

Go模块会自动被pkg.go.dev索引，无需手动发布。

```bash
# 触发索引更新
GOPROXY=proxy.golang.org go list -m github.com/idcu/est-sdk-go@v1.0.0
```

### TypeScript SDK发布

#### 构建包

```bash
# 清理旧的构建
rm -rf dist/

# 构建
npm run build
```

#### 发布到npm

```bash
# 登录npm
npm login

# 检查包
npm pack

# 发布
npm publish

# 发布测试版本
npm publish --tag beta
```

---

## 贡献指南

### 代码风格

#### Python

```bash
# 代码格式化
black .

# 类型检查
mypy .

# 导入排序
isort .
```

#### Go

```bash
# 代码格式化
gofmt -w .

# 代码检查
go vet ./...

# 代码lint（如果安装了golangci-lint）
golangci-lint run
```

#### TypeScript

```bash
# 代码lint
npm run lint

# 代码格式化（如果安装了prettier）
prettier --write src/
```

### 提交规范

使用约定式提交（Conventional Commits）:

```
<type>(<scope>): <subject>

<body>

<footer>
```

类型:
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档变更
- `style`: 代码格式
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具

### Pull Request流程

1. **Fork仓库**
2. **创建特性分支**: `git checkout -b feature/amazing-feature`
3. **提交更改**: `git commit -m 'feat: add amazing feature'`
4. **推送到分支**: `git push origin feature/amazing-feature`
5. **开启Pull Request**

### 测试要求

所有PR必须满足:
- ✅ 所有现有测试通过
- ✅ 新功能有对应的测试
- ✅ 测试覆盖率不降低
- ✅ 代码风格检查通过
- ✅ 基准测试无显著性能回退

---

## 常见问题

### Python SDK

**Q: 如何处理代理？**
```python
import os
os.environ['HTTP_PROXY'] = 'http://proxy:port'
os.environ['HTTPS_PROXY'] = 'http://proxy:port'
client = EstClient(base_url="http://localhost:8080")
```

**Q: 如何自定义请求头？**
```python
client = EstClient(base_url="http://localhost:8080")
client.session.headers.update({
    'X-Custom-Header': 'value'
})
```

### Go SDK

**Q: 如何处理代理？**
```go
client := NewClient("http://localhost:8080")
client.R().SetProxy("http://proxy:port")
```

**Q: 如何自定义TLS配置？**
```go
client := NewClient("http://localhost:8080")
client.restyClient.SetTLSClientConfig(&tls.Config{
    InsecureSkipVerify: true,
})
```

### TypeScript SDK

**Q: 如何处理代理？**
```typescript
import axios from 'axios';
const config: EstClientConfig = {
    baseUrl: 'http://localhost:8080',
    headers: {
        'Proxy-Authorization': 'Basic ' + btoa('user:pass')
    }
};
const client = new EstClient(config);
```

**Q: 如何取消请求？**
```typescript
import axios from 'axios';
const source = axios.CancelToken.source();
const config: EstClientConfig = {
    baseUrl: 'http://localhost:8080'
};
const client = new EstClient(config);
// 使用source.token取消请求
setTimeout(() => source.cancel('Operation canceled'), 5000);
```

---

## 📚 相关文档

- [多语言SDK文档](../est-modules/est-kotlin-support/MULTI_LANGUAGE_SDK.md) - SDK生态系统
- [多语言SDK推进总结](multi-language-sdk-progress.md) - SDK推进记录
- [短期工作推进总结](short-term-work-progress.md) - 测试覆盖完善
- [后续工作推进总结](follow-up-work-continuation.md) - 测试覆盖率和基准测试
- [FAQ文档](faq.md) - 常见问题解答
- [路线图](roadmap.md) - 长期发展规划

---

**文档生成时间**: 2026-03-10  
**文档作者**: EST Team
