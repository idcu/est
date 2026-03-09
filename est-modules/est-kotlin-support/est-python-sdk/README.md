# EST Framework Python SDK

EST Framework 的 Python SDK，提供与 EST Framework 交互的 Python 接口。

## 安装

```bash
pip install est-framework
```

## 快速开始

### 插件市场客户端

```python
from est_sdk import EstClient, PluginMarketplaceClient

# 创建客户端
client = EstClient(base_url="http://localhost:8080")

# 使用API Key
client = EstClient(
    base_url="http://localhost:8080",
    api_key="your-api-key"
)

# 自定义超时和重试
client = EstClient(
    base_url="http://localhost:8080",
    timeout=60,
    max_retries=5
)

# 使用上下文管理器
with EstClient(base_url="http://localhost:8080") as client:
    # 插件市场客户端
    marketplace = PluginMarketplaceClient(client)
    
    # 搜索插件
    plugins = marketplace.search_plugins(
        query="web",
        category="web",
        tags=["framework", "ui"],
        page=1,
        page_size=20
    )
    
    # 获取插件详情
    plugin = marketplace.get_plugin("plugin-id")
    
    # 获取插件版本
    versions = marketplace.get_plugin_versions("plugin-id")
    
    # 获取插件评论
    reviews = marketplace.get_reviews("plugin-id", page=1, page_size=20)
    
    # 添加评论
    review = marketplace.add_review(
        plugin_id="plugin-id",
        rating=5,
        content="Great plugin!",
        title="Excellent!"
    )
    
    # 下载插件
    plugin_data = marketplace.download_plugin("plugin-id", version="1.0.0")
```

## 功能特性

- ✅ 插件市场API客户端
- ✅ 类型安全的接口（使用Pydantic）
- ✅ 自动重试机制
- ✅ 请求超时控制
- ✅ 完整的错误处理
- ✅ 上下文管理器支持
- ✅ 会话管理

## 开发

### 安装开发依赖

```bash
pip install -e ".[dev]"
```

### 运行测试

```bash
# 运行所有测试
pytest

# 运行特定测试文件
pytest tests/test_client.py

# 运行带覆盖率的测试
pytest --cov=est_sdk

# 查看HTML覆盖率报告
# 打开 htmlcov/index.html
```

### 性能基准测试

```bash
# 运行性能基准测试
pytest tests/benchmark_test.py -v
```

### 代码质量

```bash
# 代码格式化
black .

# 类型检查
mypy .

# 代码检查（如果安装了flake8）
flake8 est_sdk/
```

## 项目结构

```
est-python-sdk/
├── est_sdk/              # SDK源代码
│   ├── __init__.py
│   ├── client.py         # 核心客户端
│   ├── plugin_marketplace.py  # 插件市场API
│   ├── types.py          # 数据类型定义
│   └── utils.py          # 工具函数
├── tests/                # 测试文件
│   ├── test_client.py    # 客户端测试
│   ├── test_types.py     # 类型测试
│   ├── test_plugin_marketplace.py  # 插件市场测试
│   └── benchmark_test.py # 性能基准测试
├── examples/             # 示例代码
│   └── basic_usage.py    # 基础使用示例
├── pytest.ini            # pytest配置
├── .coveragerc           # 覆盖率配置
├── setup.py              # 包配置
└── README.md             # 本文件
```

## 测试覆盖率

本SDK使用pytest-cov进行测试覆盖率分析，目标覆盖率为80%以上。

```bash
# 查看覆盖率报告
pytest --cov=est_sdk --cov-report=term-missing
```

## 性能指标

SDK已通过以下性能基准测试：

- 客户端初始化：< 1.0ms/次
- 插件元数据创建：< 0.1ms/次
- 搜索条件创建：< 0.1ms/次
- 请求处理（模拟）：< 0.5ms/次
- 内存使用：< 1KB/客户端

## 发布

```bash
# 构建包
python setup.py sdist bdist_wheel

# 发布到PyPI（需要配置）
twine upload dist/*
```

## 许可证

MIT License

## 相关链接

- [EST Framework 主项目](https://github.com/idcu/est)
- [多语言SDK文档](../../dev-docs/multi-language-sdk-progress.md)
- [FAQ文档](../../dev-docs/faq.md)
- [路线图](../../dev-docs/roadmap.md)

