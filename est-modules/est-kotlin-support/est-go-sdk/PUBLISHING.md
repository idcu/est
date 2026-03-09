# Go SDK 发布指南

本文档介绍如何将 EST Framework Go SDK 发布到 pkg.go.dev。

## 前置条件

在发布之前，请确保：

1. **Go 环境**
   - Go 1.18+
   - 配置好 GOPATH 和 GOROOT

2. **GitHub 仓库**
   - 代码已推送到 GitHub
   - 仓库设置为公开（public）

3. **版本标签**
   - 确保代码已提交
   - 创建语义化版本标签

## 发布步骤

### 1. 准备发布

确保所有代码已经提交，并且版本号已更新：

- 检查 `go.mod` 中的模块路径
- 更新 README.md 和文档
- 运行所有测试确保通过

```bash
# 运行测试
go test ./... -v

# 代码格式化
gofmt -w .

# 代码检查（如果安装了 golint）
golint ./...
```

### 2. 创建版本标签

遵循语义化版本规范（Semantic Versioning）：
- `MAJOR.MINOR.PATCH`
  - MAJOR：不兼容的 API 修改
  - MINOR：向下兼容的功能性新增
  - PATCH：向下兼容的问题修正

```bash
# 创建标签
git tag v1.0.0

# 推送标签到远程仓库
git push origin v1.0.0
```

### 3. 验证发布

发布完成后，Go 模块代理会自动索引您的包。

1. **访问 pkg.go.dev**
   - 访问 https://pkg.go.dev/github.com/idcu/est-sdk-go
   - 可能需要几分钟才能显示

2. **测试安装**
   ```bash
   # 在新的 Go 项目中测试安装
   go get github.com/idcu/est-sdk-go@v1.0.0
   ```

3. **测试导入**
   ```go
   package main
   
   import "github.com/idcu/est-sdk-go"
   
   func main() {
       client := est.NewClient("http://localhost:8080")
       // 使用客户端...
   }
   ```

## 版本管理

### 更新版本号

1. 修改代码（如有必要）
2. 提交更改
3. 创建新标签：`git tag v1.0.1`
4. 推送标签：`git push origin v1.0.1`

### 预发布版本

对于预发布版本，可以使用以下格式：
- `v1.0.0-alpha`
- `v1.0.0-beta.1`
- `v1.0.0-rc.1`

## 常见问题

### 问题：pkg.go.dev 没有显示我的包

如果包没有立即显示：

1. 等待几分钟（Go 代理需要时间索引）
2. 手动触发索引：
   ```bash
   GOPROXY=https://proxy.golang.org go list -m github.com/idcu/est-sdk-go@v1.0.0
   ```

3. 访问 https://pkg.go.dev/github.com/idcu/est-sdk-go?version=v1.0.0

### 问题：模块路径错误

确保 `go.mod` 中的模块路径正确：

```go
module github.com/idcu/est-sdk-go

go 1.18

require (
    github.com/go-resty/resty/v2 v2.10.0
)
```

### 问题：版本标签格式错误

Go 模块要求版本标签必须以 `v` 开头：
- ✅ `v1.0.0`
- ❌ `1.0.0`
- ❌ `version-1.0.0`

## 自动化发布（可选）

可以使用 GitHub Actions 自动化发布流程：

1. 创建 `.github/workflows/release.yml` 工作流
2. 推送标签时自动运行测试
3. 可选：自动生成发布说明

示例工作流：
```yaml
name: Release Go SDK

on:
  push:
    tags:
      - 'v*'

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Go
      uses: actions/setup-go@v2
      with:
        go-version: '1.18'
    - name: Run tests
      run: go test ./... -v
```

## 相关资源

- [Go Modules 官方文档](https://golang.org/doc/modules)
- [pkg.go.dev 文档](https://pkg.go.dev/about)
- [语义化版本规范](https://semver.org/lang/zh-CN/)
- [Go Module 版本控制](https://blog.golang.org/publishing-go-modules)

---

**最后更新：2026-03-10**
