# EST Framework TypeScript SDK

EST Framework 的 TypeScript/JavaScript SDK，提供与 EST Framework 交互的 TypeScript 和 JavaScript 接口。

## 安装

```bash
npm install est-framework-sdk
# 或
yarn add est-framework-sdk
```

## 快速开始

### 插件市场客户端

```typescript
import { EstClient, PluginMarketplaceClient } from 'est-framework-sdk';
import type { EstClientConfig, SearchOptions } from 'est-framework-sdk';

// 创建客户端
const config: EstClientConfig = { 
  baseUrl: 'http://localhost:8080' 
};
const client = new EstClient(config);

// 使用API Key
const configWithKey: EstClientConfig = { 
  baseUrl: 'http://localhost:8080', 
  apiKey: 'your-api-key' 
};
const clientWithKey = new EstClient(configWithKey);

// 自定义超时
const configWithTimeout: EstClientConfig = { 
  baseUrl: 'http://localhost:8080', 
  timeout: 60000,
  headers: {
    'X-Custom-Header': 'value'
  }
};
const clientWithTimeout = new EstClient(configWithTimeout);

// 插件市场客户端 - 使用现有客户端
const marketplace = new PluginMarketplaceClient(client);

// 插件市场客户端 - 使用URL字符串
const marketplaceFromUrl = new PluginMarketplaceClient('http://localhost:8080');

// 插件市场客户端 - 使用URL和API Key
const marketplaceWithKey = new PluginMarketplaceClient(
  'http://localhost:8080', 
  'your-api-key'
);

// 搜索插件
const searchOptions: SearchOptions = {
  query: 'web',
  category: 'web',
  tags: ['framework', 'ui'],
  page: 1,
  pageSize: 20,
  sortBy: 'rating',
  sortOrder: 'desc'
};
const plugins = await marketplace.searchPlugins(searchOptions);
console.log(`Found ${plugins.length} plugins`);

// 获取插件详情
const plugin = await marketplace.getPlugin('plugin-id');
console.log(`Plugin: ${plugin.name}`);

// 获取热门插件
const popularPlugins = await marketplace.getPopularPlugins(10);
console.log(`Found ${popularPlugins.length} popular plugins`);

// 获取最新插件
const latestPlugins = await marketplace.getLatestPlugins(10);
console.log(`Found ${latestPlugins.length} latest plugins`);

// 获取认证插件
const certifiedPlugins = await marketplace.getCertifiedPlugins();
console.log(`Found ${certifiedPlugins.length} certified plugins`);

// 获取分类列表
const categories = await marketplace.getCategories();
console.log(`Available categories: ${categories.join(', ')}`);

// 获取插件评论
const reviews = await marketplace.getReviews('plugin-id', 1, 20);
console.log(`Found ${reviews.length} reviews`);

// 添加评论
const newReview = await marketplace.addReview(
  'plugin-id',
  'user-123',
  'Test User',
  5,
  'Great plugin!',
  'Excellent plugin, works perfectly!'
);
console.log(`Review added: ${newReview.id}`);

// 获取已安装插件
const installedPlugins = await marketplace.getInstalledPlugins();
console.log(`Found ${installedPlugins.length} installed plugins`);

// 安装插件
const installed = await marketplace.installPlugin('plugin-id', '1.0.0');
if (installed) {
  console.log('Plugin installed successfully');
}

// 卸载插件
const uninstalled = await marketplace.uninstallPlugin('plugin-id');
if (uninstalled) {
  console.log('Plugin uninstalled successfully');
}

// 获取可用更新
const updates = await marketplace.getAvailableUpdates();
console.log(`Found ${updates.length} updates`);

// 更新插件
const updated = await marketplace.updatePlugin('plugin-id');
if (updated) {
  console.log('Plugin updated successfully');
}
```

### JavaScript 使用

```javascript
const { EstClient, PluginMarketplaceClient } = require('est-framework-sdk');

// 创建客户端
const client = new EstClient({ 
  baseUrl: 'http://localhost:8080' 
});

// 使用客户端
const marketplace = new PluginMarketplaceClient(client);
const plugins = await marketplace.searchPlugins({ query: 'web' });
```

## 功能特性

- ✅ 插件市场API客户端
- ✅ 类型安全的接口（TypeScript）
- ✅ Promise/async-await支持
- ✅ 请求超时控制
- ✅ 完整的错误处理
- ✅ 基于Axios的HTTP客户端
- ✅ 支持JavaScript和TypeScript
- ✅ 灵活的客户端初始化方式

## 开发

### 安装依赖

```bash
npm install
# 或
yarn install
```

### 构建

```bash
npm run build
# 或
yarn build
```

### 开发模式（监听文件变化）

```bash
npm run dev
# 或
yarn dev
```

### 运行测试

```bash
# 运行所有测试
npm test
# 或
yarn test

# 运行带覆盖率的测试
npm run test:coverage
# 或
yarn test:coverage
```

### 性能基准测试

```bash
# 运行性能基准测试
npm run test:benchmark
# 或
yarn test:benchmark
```

### 代码质量

```bash
# 代码检查
npm run lint
# 或
yarn lint
```

## 项目结构

```
est-typescript-sdk/
├── src/                   # SDK源代码
│   ├── index.ts          # 主入口文件
│   ├── client.ts         # 核心客户端
│   ├── plugin-marketplace.ts  # 插件市场API
│   ├── types.ts          # 类型定义
│   └── utils.ts          # 工具函数
├── tests/                # 测试文件
│   ├── client.test.ts    # 客户端测试
│   └── benchmark.test.ts # 性能基准测试
├── examples/             # 示例代码
│   └── basic-usage.ts    # 基础使用示例
├── jest.config.js        # Jest配置
├── tsconfig.json         # TypeScript配置
├── package.json          # 包配置
├── .gitignore            # Git忽略文件
└── README.md             # 本文件
```

## 测试覆盖率

本SDK使用Jest + ts-jest进行测试覆盖率分析，目标覆盖率为80%以上。

```bash
# 查看覆盖率报告
npm run test:coverage

# 查看HTML覆盖率报告
# 打开 coverage/lcov-report/index.html
```

## 性能指标

SDK已通过以下性能基准测试：

- 客户端初始化：< 0.1ms/次
- PluginInfo创建：< 0.01ms/次
- PluginReview创建：< 0.01ms/次
- SearchOptions创建：< 0.005ms/次
- 内存使用：< 1KB/客户端
- 并发操作：< 1.0ms/次（10并发）

## 发布

```bash
# 构建包
npm run build

# 发布到npm（需要配置）
npm login
npm publish
```

## 许可证

Apache-2.0 License

## 相关链接

- [EST Framework 主项目](https://github.com/idcu/est)
- [多语言SDK文档](../../dev-docs/multi-language-sdk-progress.md)
- [FAQ文档](../../dev-docs/faq.md)
- [路线图](../../dev-docs/roadmap.md)
- [Axios HTTP客户端](https://axios-http.com/)
- [TypeScript](https://www.typescriptlang.org/)
