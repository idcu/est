# EST AI 3.0 发展建议与开发计划

**版本**: 3.0.0  
**日期**: 2026-03-10  
**状态**: 📋 规划中

---

## 📋 目录

1. [发展背景与建议](#发展背景与建议)
2. [总体目标](#总体目标)
3. [核心功能规划](#核心功能规划)
4. [详细开发计划](#详细开发计划)
5. [实施时间表](#实施时间表)
6. [验收标准](#验收标准)

---

## 🎯 发展背景与建议

### 对比分析总结

通过与 Solon AI 的全面对比，EST AI 在以下方面需要加强：

| 功能维度 | EST AI 现状 | Solon AI | 差距 |
|---------|------------|----------|------|
| **RAG（检索增强生成）** | ❌ 无 | ✅ 支持 | 🔴 高 |
| **MCP 协议** | ❌ 无 | ✅ 完整支持 | 🔴 高 |
| **AI Agent** | ❌ 无 | ✅ 完整支持 | 🔴 高 |
| **Skills 体系** | ❌ 无 | ✅ 支持 | 🔴 高 |
| **Flow 工作流** | ❌ 无 | ✅ 支持 | 🟡 中 |
| **Reranking** | ❌ 无 | ✅ 支持 | 🟡 中 |
| **CLI 工具** | ⚪ 基础 | ✅ 完善 | 🟡 中 |

### 核心发展建议

1. **补充 RAG 能力** - 实现完整的检索增强生成功能
2. **集成 MCP 协议** - 支持 MCP Server、Client、Proxy
3. **构建 AI Agent 体系** - 实现自主决策、任务拆解的 AI Agent
4. **引入 Skills 机制** - 具备生命周期和业务感知的技能架构
5. **完善 CLI 工具** - 推出 EST Code CLI，类似 Solon Code CLI

---

## 🚀 总体目标

### 版本主题
> **"从 AI 集成到 AI 原生 - 构建企业级 AI 开发生态"**

### 核心目标

1. **功能完整性** - 补齐与 Solon AI 的功能差距
2. **生态开放性** - 通过 MCP 协议接入更广泛的 AI 生态
3. **开发者体验** - 提供简单易用的 API 和工具
4. **企业级特性** - 安全、可观测、可扩展

---

## 🏗️ 核心功能规划

### 1. RAG（检索增强生成）模块

#### 模块结构
```
est-ai-suite/
├── est-rag/
│   ├── est-rag-api/
│   ├── est-rag-impl/
│   └── README.md
```

#### 核心功能
- **文档分块** - 支持多种分块策略（固定大小、语义分块、递归分块）
- **向量存储** - 统一的向量存储接口，支持多种后端
  - 内存向量存储（用于开发测试）
  - Milvus 集成
  - Chroma 集成
  - Pinecone 集成
- **检索策略** - 支持多种检索方式
  - 向量相似度检索
  - 混合检索（向量 + 关键词）
  - 重排序（Reranking）
- **文档加载器** - 支持多种文档格式
  - PDF、Word、Excel、PPT
  - Markdown、HTML、纯文本
  - 自定义格式支持

#### API 设计
```java
public interface RagEngine {
    void addDocument(Document document);
    void addDocuments(List<Document> documents);
    List<RetrievalResult> retrieve(String query, int topK);
    String generate(String query, List<RetrievalResult> contexts);
    String retrieveAndGenerate(String query, int topK);
}

public interface VectorStore {
    void addEmbeddings(List<Embedding> embeddings);
    List<SearchResult> search(float[] queryVector, int topK);
    void delete(String id);
    void clear();
}
```

---

### 2. MCP（Model Context Protocol）模块

#### 模块结构
```
est-ai-suite/
├── est-mcp/
│   ├── est-mcp-api/
│   ├── est-mcp-server/
│   ├── est-mcp-client/
│   ├── est-mcp-proxy/
│   └── README.md
```

#### 核心功能
- **MCP Server** - 构建自定义 MCP 服务器
  - 工具注册和管理
  - 资源管理
  - 提示词模板管理
  - Stdio 和 WebSocket 传输
- **MCP Client** - 连接 MCP 服务器
  - 自动发现
  - 连接管理
  - 工具调用
- **MCP Proxy** - 协议代理和转换
  - 多服务器聚合
  - 协议转换
  - 请求路由

#### API 设计
```java
public interface McpServer {
    void registerTool(Tool tool);
    void registerResource(Resource resource);
    void registerPrompt(PromptTemplate prompt);
    void start();
    void stop();
}

public interface McpClient {
    void connect(String serverUrl);
    List<Tool> listTools();
    ToolResult callTool(String toolName, Map<String, Object> arguments);
    void disconnect();
}

@McpTool
public class WeatherTool {
    @Tool(name = "getWeather", description = "获取指定城市的天气")
    public String getWeather(
        @Param(name = "city", description = "城市名称", required = true) String city
    ) {
        // 实现
    }
}
```

---

### 3. AI Agent 模块

#### 模块结构
```
est-ai-suite/
├── est-agent/
│   ├── est-agent-api/
│   ├── est-agent-impl/
│   └── README.md
```

#### 核心功能
- **Agent 核心** - 自主决策和任务执行
  - 任务规划
  - 步骤执行
  - 反思和优化
- **Skills 体系** - 具备生命周期的技能
  - Skill 接口定义
  - Skill 生命周期（初始化、执行、清理）
  - Skill 依赖管理
- **Memory 系统** - 短期和长期记忆
  - 对话历史
  - 任务状态
  - 知识库

#### API 设计
```java
public interface Agent {
    AgentResponse execute(String task);
    AgentResponse execute(AgentRequest request);
    void addSkill(Skill skill);
    void setMemory(Memory memory);
}

public interface Skill {
    String getName();
    String getDescription();
    void initialize(SkillContext context);
    SkillResult execute(SkillInput input);
    void cleanup();
    boolean canHandle(String task);
}

public interface Memory {
    void add(MemoryItem item);
    List<MemoryItem> retrieve(String query, int topK);
    void clear();
}
```

---

### 4. EST Code CLI 工具

#### 模块结构
```
est-tools/
├── est-code-cli/
│   ├── src/main/java/
│   ├── README.md
│   └── pom.xml
```

#### 核心功能
- **代码生成** - 一句话生成代码
- **代码解释** - 解释现有代码
- **代码优化** - 优化代码建议
- **Bug 修复** - 智能 bug 修复
- **文档生成** - 自动生成文档
- **项目脚手架** - 快速创建项目

---

## 📅 详细开发计划

### Phase 1: RAG 模块（优先级：高 🔴）

#### 任务清单
1. 创建 est-rag 模块结构
2. 实现核心 API 接口
3. 实现文档分块器
4. 实现向量存储接口和内存实现
5. 实现检索引擎
6. 集成 OpenAI Embeddings
7. 实现文档加载器
8. 编写单元测试
9. 编写示例代码
10. 编写文档

**预估时间**: 2 周  
**负责人**: AI Team

---

### Phase 2: MCP 模块（优先级：高 🔴）

#### 任务清单
1. 创建 est-mcp 模块结构
2. 实现 MCP 协议核心
3. 实现 MCP Server
4. 实现 MCP Client
5. 实现注解驱动的工具开发
6. 实现 Stdio 传输
7. 实现 WebSocket 传输
8. 编写单元测试
9. 编写示例代码
10. 编写文档

**预估时间**: 2 周  
**负责人**: AI Team

---

### Phase 3: AI Agent 模块（优先级：高 🔴）

#### 任务清单
1. 创建 est-agent 模块结构
2. 实现 Agent 核心接口
3. 实现 Skills 体系
4. 实现 Memory 系统
5. 实现任务规划器
6. 实现执行引擎
7. 集成 RAG 模块
8. 集成 MCP 模块
9. 编写单元测试
10. 编写示例代码
11. 编写文档

**预估时间**: 3 周  
**负责人**: AI Team

---

### Phase 4: EST Code CLI（优先级：中 🟡）

#### 任务清单
1. 增强 est-code-cli 模块
2. 集成 AI Agent
3. 实现代码生成命令
4. 实现代码解释命令
5. 实现代码优化命令
6. 实现 Bug 修复命令
7. 实现文档生成命令
8. 实现项目脚手架命令
9. 编写单元测试
10. 编写使用文档

**预估时间**: 2 周  
**负责人**: Tools Team

---

## 📊 实施时间表

| 阶段 | 开始时间 | 结束时间 | 持续时间 | 主要任务 |
|------|---------|---------|---------|---------|
| **Phase 1** | Week 1 | Week 2 | 2 周 | RAG 模块开发 |
| **Phase 2** | Week 3 | Week 4 | 2 周 | MCP 模块开发 |
| **Phase 3** | Week 5 | Week 7 | 3 周 | AI Agent 模块开发 |
| **Phase 4** | Week 8 | Week 9 | 2 周 | EST Code CLI 增强 |
| **测试与集成** | Week 10 | Week 11 | 2 周 | 集成测试、Bug 修复 |
| **文档与发布** | Week 12 | Week 12 | 1 周 | 文档完善、发布准备 |

**总计**: 12 周（约 3 个月）

---

## ✅ 验收标准

### 功能验收

1. **RAG 模块**
   - [ ] 支持至少 3 种文档格式
   - [ ] 支持至少 2 种向量存储
   - [ ] 检索准确率 &gt; 80%
   - [ ] 单元测试覆盖率 &gt; 80%

2. **MCP 模块**
   - [ ] 支持 MCP 2.0 协议
   - [ ] 支持 Stdio 和 WebSocket
   - [ ] 可运行至少 3 个示例工具
   - [ ] 单元测试覆盖率 &gt; 80%

3. **AI Agent 模块**
   - [ ] 可完成 5 步以上的复杂任务
   - [ ] 支持至少 5 个内置 Skills
   - [ ] 可集成自定义 Skills
   - [ ] 单元测试覆盖率 &gt; 70%

4. **EST Code CLI**
   - [ ] 支持 6 个核心命令
   - [ ] 代码生成可编译运行
   - [ ] 提供完整的使用文档

### 性能验收

- RAG 检索延迟 &lt; 500ms（1000 文档）
- MCP 工具调用延迟 &lt; 1s
- Agent 任务执行时间 &lt; 30s（10 步任务）
- CLI 启动时间 &lt; 2s

### 文档验收

- [ ] 完整的 API 文档
- [ ] 至少 5 个示例项目
- [ ] 快速开始指南
- [ ] 最佳实践文档
- [ ] 迁移指南（从 2.x 升级）

---

## 🎉 总结

EST AI 3.0 将从一个简单的 LLM 集成框架，升级为一个完整的 AI 开发生态系统，具备：

1. **RAG** - 强大的检索增强生成能力
2. **MCP** - 开放的生态系统接入
3. **AI Agent** - 智能的自主任务执行
4. **EST Code CLI** - 便捷的开发工具

这将使 EST AI 在功能完整性上达到甚至超越 Solon AI，同时保持 EST 框架一贯的企业级质量和开发者体验。

---

**文档生成时间**: 2026-03-10  
**文档作者**: EST AI Team
