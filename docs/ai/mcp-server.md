# MCP Server

MCP (Model Context Protocol) Server 用于与 AI 编程工具（如 Cursor、Windsurf）集成。

---

## 核心概念

| 概念 | 说明 |
|------|------|
| **McpServer** | MCP 服务器接口 |
| **McpTool** | MCP 工具定义 |
| **McpToolResult** | 工具执行结果 |
| **JsonRpcServer** | JSON-RPC 协议支持 |

---

## 内置 MCP 工具

EST AI 提供了以下内置 MCP 工具：

| 工具名称 | 功能描述 |
|----------|----------|
| est_generate_entity | 生成实体类代码 |
| est_generate_service | 生成服务类代码 |
| est_generate_controller | 生成控制器代码 |
| est_code_review | 代码质量审查 |
| est_get_quick_reference | 获取 EST 快速参考 |

---

## 使用 MCP Server

### 基本使用

```java
import ltd.idcu.est.features.ai.api.mcp.McpServer;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.impl.mcp.DefaultMcpServer;

import java.util.Map;

McpServer mcpServer = new DefaultMcpServer();

// 获取服务器信息
System.out.println("名称: " + mcpServer.getName());
System.out.println("版本: " + mcpServer.getVersion());

// 列出可用工具
System.out.println("可用工具:");
mcpServer.getTools().forEach(tool -> 
    System.out.println("- " + tool.getName() + ": " + tool.getDescription()));
```

### 调用 MCP 工具

```java
// 生成 Controller
McpToolResult result = mcpServer.callTool("est_generate_controller",
    Map.of("controllerName", "Product",
           "packageName", "com.example.controller"));

if (result.isSuccess()) {
    System.out.println(result.getContent());
} else {
    System.out.println("错误: " + result.getErrorMessage());
}
```

### 代码审查工具

```java
String code = """
    public class Example {
        public void doSomething() {
            System.out.println("Hello");
        }
    }
    """;

McpToolResult reviewResult = mcpServer.callTool("est_code_review",
    Map.of("code", code));

if (reviewResult.isSuccess()) {
    System.out.println(reviewResult.getContent());
}
```

### 获取快速参考

```java
McpToolResult refResult = mcpServer.callTool("est_get_quick_reference",
    Map.of("topic", "web"));

if (refResult.isSuccess()) {
    System.out.println(refResult.getContent());
}
```

---

## JSON-RPC 支持

EST MCP Server 支持 JSON-RPC 2.0 协议：

```java
import ltd.idcu.est.features.ai.impl.mcp.JsonRpcServer;

JsonRpcServer jsonRpcServer = new JsonRpcServer();

// 处理 JSON-RPC 请求
String jsonRequest = "{\"jsonrpc\":\"2.0\",\"method\":\"tools/list\",\"id\":1}";
String jsonResponse = jsonRpcServer.handleRequest(jsonRequest);

System.out.println("响应: " + jsonResponse);
```

### JSON-RPC 方法

| 方法 | 说明 |
|------|------|
| tools/list | 列出所有可用工具 |
| tools/call | 调用指定工具 |
| initialize | 初始化服务器 |

---

## 与 Cursor/Windsurf 集成

MCP Server 可以与 Cursor、Windsurf 等 AI 编程工具集成：

1. 配置 MCP Server 为独立进程
2. 在 AI 工具配置中添加 MCP Server
3. 即可使用 EST 的 AI 能力

详细配置请参考 [与 AI 编程工具集成](integration.md)。

---

## 自定义 MCP 工具

```java
import ltd.idcu.est.features.ai.api.mcp.McpTool;
import ltd.idcu.est.features.ai.api.mcp.McpToolResult;
import ltd.idcu.est.features.ai.impl.mcp.DefaultMcpServer;

import java.util.Map;

// 创建自定义工具
public class CustomTool implements McpTool {
    @Override
    public String getName() {
        return "est_custom_tool";
    }

    @Override
    public String getDescription() {
        return "自定义工具描述";
    }

    @Override
    public Map<String, Object> getInputSchema() {
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "param1", Map.of("type", "string")
            )
        );
    }

    @Override
    public McpToolResult execute(Map<String, Object> inputs) {
        McpToolResult result = new McpToolResult();
        result.setSuccess(true);
        result.setContent("Hello from custom tool!");
        return result;
    }
}

// 注册自定义工具
DefaultMcpServer server = new DefaultMcpServer();
server.registerTool(new CustomTool());
```

---

## 最佳实践

1. **工具命名**: 使用 `est_` 前缀，避免命名冲突
2. **输入验证**: 严格验证工具输入参数
3. **错误处理**: 提供清晰的错误信息
4. **文档完善**: 详细描述工具功能和参数
5. **性能优化**: 避免长时间运行的操作

---

**文档版本**: 2.0  
**最后更新**: 2026-03-07  
**维护者**: EST 架构团队
