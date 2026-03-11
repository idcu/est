# EST Plugin Marketplace

EST Plugin Marketplace 是 EST Framework 的插件市场系统，提供插件的发现、安装、管理和评价功能。

## 功能特性

### 核心功能
- **插件搜索**: 支持关键词、分类、标签、认证状态等多维度搜索
- **插件管理**: 插件的安装、更新、卸载
- **版本管理**: 支持多版本插件
- **插件分类**: 按功能、语言等分类浏览
- **评价系统**: 插件评分和评论功能
- **认证体系**: 官方认证插件标识
- **搜索建议**: 智能搜索建议
- **热门排行**: 下载量、评分等排行

### 架构设计
- **API与实现分离**: 清晰的接口定义和实现分离
- **可扩展**: 支持多个插件仓库
- **模块化**: 独立的插件市场模块
- **REST API**: 完整的 Web API 支持

## 快速开始

### Maven 依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-plugin-marketplace-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-plugin-marketplace-impl</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```

### 基本使用

```java
import ltd.idcu.est.plugin.marketplace.api.*;
import ltd.idcu.est.plugin.marketplace.impl.*;

// 创建插件市场实例
LocalPluginRepository repo = new LocalPluginRepository(
    "est-official", 
    "EST Official Repository", 
    "https://marketplace.est.idcu.ltd"
);

PluginMarketplace marketplace = new DefaultPluginMarketplace.Builder()
    .addRepository(repo)
    .localCacheDirectory("/tmp/est-plugins")
    .build();

// 搜索插件
List<PluginInfo> plugins = marketplace.searchPlugins("database");

// 获取热门插件
List<PluginInfo> popular = marketplace.getPopularPlugins(10);

// 安装插件
boolean installed = marketplace.installPlugin("database-plugin");

// 高级搜索
PluginSearchQuery query = PluginSearchQuery.builder()
    .keyword("security")
    .category("security")
    .certified(true)
    .sortBy("downloads")
    .page(0)
    .pageSize(20)
    .build();

SearchResult result = marketplace.searchPlugins(query);
```

## API 文档

### PluginMarketplace 接口

主要方法：
- `getPlugin(String pluginId)` - 获取插件详情
- `searchPlugins(String query)` - 简单搜索
- `searchPlugins(PluginSearchQuery query)` - 高级搜索
- `getPopularPlugins(int limit)` - 获取热门插件
- `getLatestPlugins(int limit)` - 获取最新插件
- `getCertifiedPlugins()` - 获取认证插件
- `installPlugin(String pluginId)` - 安装插件
- `updatePlugin(String pluginId)` - 更新插件
- `uninstallPlugin(String pluginId)` - 卸载插件
- `getInstalledPlugins()` - 获取已安装插件

### 搜索功能

支持的搜索条件：
- 关键词搜索（名称、描述、标签）
- 分类筛选
- 标签筛选
- 认证状态筛选
- 许可证筛选
- 多种排序方式（下载量、评分、更新时间等）
- 分页支持

## 模块结构

```
est-plugin-marketplace/
├── est-plugin-marketplace-api/    # API 接口定义
│   └── src/main/java/
│       └── ltd/idcu/est/plugin/marketplace/api/
│           ├── PluginMarketplace.java
│           ├── PluginRepository.java
│           ├── PluginReview.java
│           ├── PluginReviewService.java
│           ├── PluginCategory.java
│           ├── PluginSearchQuery.java
│           ├── PluginSearchService.java
│           └── SearchResult.java
├── est-plugin-marketplace-impl/   # 实现模块
│   └── src/main/java/
│       └── ltd/idcu/est/plugin/marketplace/impl/
│           ├── DefaultPluginMarketplace.java
│           ├── DefaultPluginReviewService.java
│           ├── LocalPluginRepository.java
│           └── PluginPublisher.java
└── pom.xml
```

## 示例

更多示例代码请参考 [est-examples-plugin](../est-examples/est-examples-plugin/) 模块。

## 相关文档

- [SDK插件市场架构设计](../../../../dev-docs/sdk-marketplace-architecture.md)
- [模块认证标准](../../../../dev-docs/module-certification-standards.md)
- [SDK开发指南](../../../../dev-docs/sdk-development-guide.md)

## 许可证

Apache License 2.0
