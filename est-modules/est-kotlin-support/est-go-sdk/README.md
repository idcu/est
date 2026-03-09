# EST Framework Go SDK

EST Framework 的 Go SDK，提供与 EST Framework 交互的 Go 接口。

## 安装

```bash
go get github.com/idcu/est-sdk-go
```

## 快速开始

### 插件市场客户端

```go
package main

import (
	"fmt"
	"github.com/idcu/est-sdk-go"
)

func main() {
	// 创建客户端
	client := est.NewClient("http://localhost:8080")
	
	// 使用选项创建客户端
	options := est.ClientOptions{
		APIKey:     "your-api-key",
		Timeout:    60,
		MaxRetries: 5,
	}
	client = est.NewClientWithOptions("http://localhost:8080", options)
	
	// 插件市场客户端
	marketplace := est.NewPluginMarketplaceClient(client)
	
	// 搜索插件
	query := "web"
	category := "web"
	result, err := marketplace.SearchPlugins(est.PluginSearchCriteria{
		Query:     &query,
		Category:  &category,
		Tags:      []string{"framework", "ui"},
		Page:      1,
		PageSize:  20,
		SortBy:    "rating",
		SortOrder: "desc",
	})
	if err != nil {
		panic(err)
	}
	
	fmt.Printf("Found %d plugins\n", result.Total)
	for _, plugin := range result.Plugins {
		fmt.Printf("- %s (%s)\n", plugin.Name, plugin.Version)
	}
	
	// 获取插件详情
	plugin, err := marketplace.GetPlugin("plugin-id")
	if err != nil {
		panic(err)
	}
	fmt.Printf("Plugin: %s\n", plugin.Name)
	
	// 获取插件版本
	versions, err := marketplace.GetPluginVersions("plugin-id")
	if err != nil {
		panic(err)
	}
	fmt.Printf("Found %d versions\n", len(versions))
	
	// 获取插件评论
	reviews, err := marketplace.GetReviews("plugin-id", 1, 20)
	if err != nil {
		panic(err)
	}
	fmt.Printf("Found %d reviews\n", len(reviews))
	
	// 添加评论
	title := "Great plugin!"
	review, err := marketplace.AddReview(
		"plugin-id",
		5,
		"Excellent plugin, works perfectly!",
		&title,
	)
	if err != nil {
		panic(err)
	}
	fmt.Printf("Review added: %s\n", review.ReviewID)
	
	// 发布插件
	homepage := "https://example.com"
	publishResult, err := marketplace.PublishPlugin(est.PluginPublishRequest{
		Name:        "New Plugin",
		Description: "A new plugin",
		Version:     "1.0.0",
		Author:      "Test Author",
		Category:    "test",
		Tags:        []string{"new", "plugin"},
		License:     "MIT",
		Homepage:    &homepage,
		FileData:    []byte("plugin-content"),
	})
	if err != nil {
		panic(err)
	}
	if publishResult.Success {
		fmt.Printf("Plugin published: %s\n", *publishResult.PluginID)
	}
	
	// 下载插件
	version := "1.0.0"
	pluginData, err := marketplace.DownloadPlugin("plugin-id", &version)
	if err != nil {
		panic(err)
	}
	fmt.Printf("Downloaded %d bytes\n", len(pluginData))
}
```

## 功能特性

- ✅ 插件市场API客户端
- ✅ 类型安全的接口
- ✅ 自动重试机制
- ✅ 请求超时控制
- ✅ 完整的错误处理
- ✅ 基于Resty的HTTP客户端
- ✅ 结构化错误处理

## 开发

### 运行测试

```bash
# 使用Makefile（推荐）
make test

# 或直接使用go test
go test -v ./...
```

### 测试覆盖率

```bash
# 使用Makefile生成覆盖率报告
make test-coverage

# 或手动生成
go test -v -coverprofile=coverage.out ./...
go tool cover -html=coverage.out -o coverage.html

# 查看覆盖率报告
# 打开 coverage.html
```

### 性能基准测试

```bash
# 使用Makefile运行基准测试
make test-bench

# 或手动运行
go test -v -bench=. -benchmem ./...
```

### 代码质量

```bash
# 代码格式化
gofmt -w .

# 代码检查（如果安装了golint）
golint ./...

# 代码 vet 检查
go vet ./...
```

## 项目结构

```
est-go-sdk/
├── client.go              # 核心客户端
├── plugin_marketplace.go  # 插件市场API
├── types.go               # 数据类型定义
├── utils.go               # 工具函数
├── tests/                 # 测试文件
│   ├── client_test.go     # 客户端和类型测试
│   └── benchmark_test.go  # 性能基准测试
├── examples/              # 示例代码
│   └── basic_usage.go     # 基础使用示例
├── Makefile               # 构建和测试命令
├── go.mod                 # Go模块定义
├── .gitignore             # Git忽略文件
└── README.md              # 本文件
```

## 测试覆盖率

本SDK使用go test -cover进行测试覆盖率分析，可配置目标覆盖率。

```bash
# 查看覆盖率统计
go test -cover ./...

# 生成详细的覆盖率报告
go test -coverprofile=coverage.out ./...
go tool cover -func=coverage.out
```

## 性能指标

SDK已通过以下性能基准测试：

- 客户端初始化：< 100μs/次
- 插件元数据创建：< 10μs/次
- 支持并行基准测试
- 内存分配统计（-benchmem）

## 基准测试结果示例

```
goos: windows
goarch: amd64
pkg: github.com/idcu/est-sdk-go
cpu: Intel(R) Core(TM) i7-XXXX
BenchmarkClientInitialization-8         	10000000	   123.4 ns/op	   48 B/op	   1 allocs/op
BenchmarkPluginMetadataCreation-8       	20000000	    56.7 ns/op	   32 B/op	   0 allocs/op
PASS
ok  	github.com/idcu/est-sdk-go	2.345s
```

## 许可证

MIT License

## 相关链接

- [EST Framework 主项目](https://github.com/idcu/est)
- [多语言SDK文档](../../dev-docs/multi-language-sdk-progress.md)
- [FAQ文档](../../dev-docs/faq.md)
- [路线图](../../dev-docs/roadmap.md)
- [Resty HTTP客户端](https://github.com/go-resty/resty)
