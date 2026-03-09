# CHANGELOG

所有重要的项目变更都将记录在此文件中。
格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)。
本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)。
---

## [2.4.0] - 2026-03-09

### ✨ 新增
- **生态系统建设**
  - 完整的插件系统实现（Plugin、PluginManager、PluginLoader、PluginListener）
  - 插件生命周期管理（加载、初始化、启动、停止、卸载）
  - 插件依赖管理和统计信息
  - 插件市场 API 设计（PluginMarketplace、PluginMetadata）
  - 插件系统示例项目（est-examples-plugin）
  
- **云原生增强**
  - 完整的可观测性集成（Metrics + Logs + Traces）
  - Prometheus Metrics 集成
  - ELK Stack Logs 集成
  - OpenTelemetry、Zipkin、Jaeger Traces 集成
  - 可观测性 UI（Observability.vue）
  - 可观测性后端 Controller（ObservabilityController.java）
  - Grafana 仪表板模板（est-dashboard.json）
  
  - 微服务治理增强
    - 断路器实现（est-circuitbreaker）
    - 限流实现（est-ratelimiter）
    - 服务发现实现（est-discovery）
    - 性能监控实现（est-performance）
    - 微服务治理 UI（MicroserviceGovernance.vue）
    - 微服务治理后端 Controller（MicroserviceGovernanceController.java）
  
  - Serverless 支持完善
    - AWS Lambda 支持
    - Azure Functions 支持
    - 阿里云函数计算支持
    - Google Cloud Functions 支持
    - 冷启动优化器（ColdStartOptimizer）
    - Serverless 本地运行器（ServerlessLocalRunner）
    - Serverless 示例项目（est-examples-serverless）
    - 完整的部署配置（deploy/serverless/）

- **多语言支持**
  - Kotlin 原生支持
  - Kotlin DSL 设计
  - 协程集成
  - Kotlin 特定扩展函数
  - Kotlin 示例项目（est-examples-kotlin）
  - Kotlin DSL 示例（KotlinDslExample.kt）
  - Kotlin 扩展函数示例（KotlinExtensionsExample.kt）

- **示例代码完善**
  - 新增 est-examples-kotlin - Kotlin 支持示例
  - 新增 est-examples-plugin - 插件系统示例
  - 新增 est-examples-serverless - Serverless 示例
  - 总计 9 个示例模块

- **管理后台 UI 增强**
  - 可观测性仪表盘（Metrics、Traces、Logs 三个标签页）
  - 微服务治理（断路器、限流、服务发现、性能监控）
  - ECharts 图表集成（7+ 图表）
  - 移动端响应式优化

### 🛠️ 修复
- 统一所有模块版本号为 2.4.0-SNAPSHOT
- 修复 Maven 依赖版本缺失问题（est-util-common/pom.xml）
- 移除多余的 JUnit 依赖声明
- 优化依赖管理结构

### 📚 文档
- 更新开发路线图（roadmap.md）
- 更新 2.4.0 开发计划（development-plan-2.4.0.md）
- 创建第三阶段完成总结（phase3-final-completion-summary.md）
- 创建 2.4.0 版本准备文档（version-2.4.0-preparation.md）
- 创建 Grafana 仪表板模板（deploy/grafana/est-dashboard.json）
- 更新所有示例模块的 README 文档

---

## [2.3.0] - 2026-06-30

### ✨ 新增
- AI功能深化：增强需求解析器的智能程度
  - 新增extractKeywords() - 技术关键词提取（支持中英文）
  - 新增analyzeSentiment() - 情感分析（正面/负面/中性）
  - 新增extractEntities() - 业务实体提取
  - 新增classifyRequirements() - 需求分类（功能/非功能/技术）
  - 新增suggestPriorities() - 智能优先级建议
- AI功能深化：优化架构设计器的模式推荐
  - 新增recommendPatternsByType() - 按项目类型和复杂度推荐架构模式
  - 新增categorizePatterns() - 架构模式分类
  - 新增suggestTechStack() - 技术栈智能推荐
  - 新增analyzeTradeoffs() - 架构权衡分析
  - 新增comparePatterns() - 架构模式对比
  - 支持电商系统特殊模式推荐（Saga模式）
  - 支持社交系统特殊模式推荐（CQRS模式）
- AI功能深化：完善测试代码生成质量
  - 新增generateUnitTest() - 生成完整的单元测试（包含Nested测试类）
  - 新增generateIntegrationTest() - 生成集成测试
  - 新增generatePerformanceTest() - 生成性能测试（响应时间/吞吐量/内存使用）
  - 新增generateTestSuite() - 生成完整测试套件
  - 新增generateMockData() - 生成模拟数据工具类
  - 新增generateTestAssertions() - 生成测试断言工具类
- AI功能深化：增强RAG能力（文档分块、重排序）
  - 新增chunkSmart() - 智能分块（自动检测最佳策略）
  - 新增chunkByParagraph() - 按段落分块
  - 新增chunkByHeading() - 按标题分块（支持Markdown）
  - 新增分块策略管理和统计
  - 新增多种重排序策略（默认、BM25、TF-IDF）
  - 新增可配置的权重管理
  - 新增重排序统计信息
  - 实现BM25算法
  - 实现TF-IDF算法
- AI功能深化：集成更多LLM提供商
  - 已支持：智谱AI、OpenAI、通义千问、文心一言、豆包、Kimi、Ollama、Anthropic、Claude、Gemini、Mistral、DeepSeek
- 性能优化：核心模块性能基准测试
  - 新增PerformanceBenchmark.java - 完整的性能基准测试套件
  - 包含容器性能测试（ArrayList vs LinkedList vs Vector）
  - 包含缓存性能测试（HashMap vs ConcurrentHashMap vs Caffeine）
  - 包含集合性能测试（HashSet vs TreeSet vs LinkedHashSet）
  - 包含流操作性能测试（Stream vs parallelStream vs for循环）
- 性能优化：优化数据结构和算法
  - 新增CollectionOptimizerUtils.java - 数据结构和算法优化工具类
  - 包含优化的集合创建方法（optimizedArrayList, optimizedHashMap等）
  - 包含快速排序算法（quickSort）
  - 包含二分查找算法（binarySearch）
  - 包含有序列表合并算法（mergeSortedLists）
  - 包含LRU缓存创建（createLRUCache）
  - 包含批处理功能（batchProcess, partitionAndFlatten）
  - 包含去重、频率统计、TopN等实用功能
  - 新增CollectionOptimizerUtilsTest.java - 完整的测试用例
- 性能优化：内存使用优化
  - 新增PerformanceUtils.java - 性能工具类
  - 包含内存快照功能（getMemorySnapshot）
  - 包含堆内存使用率监控（getHeapMemoryUsagePercent）
  - 包含内存优化建议（getMemoryOptimizationTips）
  - 新增PerformanceUtilsTest.java - 完整的测试用例
- 性能优化：启动时间优化
  - 新增启动优化建议（getStartupOptimizationTips）
  - 包含延迟初始化、类路径扫描优化、JVM参数优化等建议
- 测试覆盖提升
  - 为性能工具类添加了完整的单元测试
  - PerformanceUtilsTest包含14个测试用例
  - CollectionOptimizerUtilsTest包含24个测试用例
- 示例代码完善
  - 新增PerformanceOptimizationExample.java - 性能优化工具使用示例
  - 包含时间测量、内存监控、优化集合、排序搜索、批处理、LRU缓存、优化建议等7个实用示例

### 🛠️ 修复
- 修复Maven构建依赖解析问题
- 修复est-serverless-api依赖缺失问题
- 修复est-util-common硬编码版本号问题
- 优化依赖管理结构

### 📚 文档
- 更新开发路线图（roadmap.md）
- 创建发布计划文档（release-plan-2.3.0.md）
- 创建发布说明文档（release-notes-2.3.0.md）
- 更新变更日志（changelog.md）
---

## [2.3.0-SNAPSHOT] - 2026-03-09

### ✨ 新增
- 完善微服务模块文档（断路器、服务发现、性能优化、限流）
- 完善Web组模块文档（网关、路由、中间件、会话、模板）

### 🛠️ 修复
- 修复所有微服务子模块和Web组子模块文档的UTF-8编码问题
- 确保所有文档使用正确的中文编码

### 📚 文档
- 更新ROADMAP开发路线图
- 添加测试覆盖提升指南（TEST_COVERAGE_GUIDE.md）
- 添加文档完善指南（DOCUMENTATION_GUIDE.md）
---

## [2.1.0] - 2026-03-09

### ✨ 新增
- 完整的六层架构设计（基础层→核心层→模块层→应用层→工具层→示例层）
- API与实现分离的模块化设计
- AI增强功能（需求解析器、架构设计器、测试部署管理器）
- 完整的微服务支持（断路器、服务发现、限流、性能优化）
- Web框架（路由、网关、中间件、会话、模板）
- 企业级安全（JWT、OAuth2、RBAC、审计、多租户）
- 数据访问（JDBC、Redis、MongoDB、工作流引擎）
- 后台管理系统（Vue 3 + Element Plus）
- Docker容器化部署
- Kubernetes部署配置
- GitHub Actions CI/CD

### 📚 文档
- 项目主README
- 各模块README文档
- AI开发文档
- 部署指南
- 快速开始指南
---

[2.3.0]: https://github.com/idcu/est/compare/v2.3.0-SNAPSHOT...v2.3.0
[2.3.0-SNAPSHOT]: https://github.com/idcu/est/compare/v2.1.0...v2.3.0-SNAPSHOT
[2.1.0]: https://github.com/idcu/est/releases/tag/v2.1.0
