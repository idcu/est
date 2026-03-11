# EST MCP - Model Context Protocol 模块

**版本**: 3.0.0  
**状态**: ✅ 开发中

---

## 📋 目录

1. [简介](#简介)
2. [快速开始](#快速开始)
3. [核心组件](#核心组件)
4. [API 参考](#api-参考)
5. [示例代码](#示例代码)
6. [注解驱动开发](#注解驱动开发)

---

## 🎯 简介

EST MCP 是 EST AI Suite 的 Model Context Protocol（MCP）协议集成模块，提供了完整的 MCP Server 和 MCP Client 实现。

### 主要特性

- 🔌 **MCP Server** - 构建自定义 MCP 服务器
  - 工具注册和管理
  - 资源管理
  - 提示词模板管理
- 🌐 **MCP Client** - 连接 MCP 服务器
  - 自动发现
  - 连接管理
  - 工具调用
- 📝 **注解驱动** - 使用注解快速开发 MCP 工具
- 🔄 **灵活扩展** - 轻松添加自定义工具、资源和提示词

---

## 🚀 快速开始

### 添加依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-mcp-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-mcp-server</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-mcp-client</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```

### 创建 MCP Server

```java
import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.mcp.server.DefaultMcpServer;
import java.util.List;
import java.util.Map;

public class SimpleMcpServer {
    public static void main(String[] args) {
        DefaultMcpServer server = new DefaultMcpServer("Weather Server", "1.0.0");
        
        McpTool weatherTool = new McpTool();
        weatherTool.setName("getWeather");
        weatherTool.setDescription("获取指定城市的天气");
        
        server.registerTool(weatherTool, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String city = (String) args.get("city");
            
            McpToolResult result = new McpToolResult(true);
            result.setContent(List.of(
                new McpToolResult.Content("text", city + " 的天气：晴朗，25°C")
            ));
            return result;
        });
        
        server.start();
        System.out.println("MCP Server 已启动！");
    }
}
```

### 使用 MCP Client

```java
import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.mcp.client.DefaultMcpClient;
import ltd.idcu.est.mcp.server.DefaultMcpServer;
import java.util.Map;

public class SimpleMcpClient {
    public static void main(String[] args) {
        DefaultMcpServer server = new DefaultMcpServer();
        DefaultMcpClient client = new DefaultMcpClient(server);
        
        McpTool weatherTool = new McpTool("getWeather", "获取天气");
        server.registerTool(weatherTool, arguments -> {
            McpToolResult result = new McpToolResult(true);
            result.setContent(List.of(
                new McpToolResult.Content("text", "天气信息")
            ));
            return result;
        });
        
        client.connect();
        
        List<McpTool> tools = client.listTools();
        System.out.println("可用工具: " + tools.size());
        
        McpToolResult result = client.callTool("getWeather", Map.of("city", "北京"));
        System.out.println("工具执行结果: " + result.isSuccess());
        
        client.disconnect();
    }
}
```

---

## 🏗️ 核心组件

### 1. McpTool（工具）

表示一个 MCP 工具。

```java
McpTool tool = new McpTool();
tool.setName("myTool");
tool.setDescription("我的工具描述");
tool.setInputSchema(inputSchema);
```

### 2. McpResource（资源）

表示一个 MCP 资源。

```java
McpResource resource = new McpResource();
resource.setUri("file:///path/to/file");
resource.setName("My Document");
resource.setDescription("文档描述");
resource.setMimeType("text/plain");
```

### 3. McpPrompt（提示词）

表示一个 MCP 提示词模板。

```java
McpPrompt prompt = new McpPrompt();
prompt.setName("codeReview");
prompt.setDescription("代码审查提示词");
prompt.setArguments(List.of(
    new McpPrompt.PromptArgument("code", "要审查的代码", true)
));
```

### 4. McpToolResult（工具结果）

表示工具执行的结果。

```java
McpToolResult result = new McpToolResult(true);
result.setContent(List.of(
    new McpToolResult.Content("text", "执行成功")
));
```

### 5. McpServer（MCP 服务器）

MCP 服务器接口，用于注册和管理工具、资源和提示词。

```java
DefaultMcpServer server = new DefaultMcpServer();
server.registerTool(tool, handler);
server.registerResource(resource);
server.registerPrompt(prompt);
server.start();
```

### 6. McpClient（MCP 客户端）

MCP 客户端接口，用于连接和调用 MCP 服务器。

```java
DefaultMcpClient client = new DefaultMcpClient(server);
client.connect();
List<McpTool> tools = client.listTools();
McpToolResult result = client.callTool("toolName", arguments);
client.disconnect();
```

---

## 📚 API 参考

### McpServer 接口

```java
public interface McpServer {
    void registerTool(McpTool tool);
    void registerResource(McpResource resource);
    void registerPrompt(McpPrompt prompt);
    void unregisterTool(String toolName);
    void unregisterResource(String uri);
    void unregisterPrompt(String promptName);
    List<McpTool> listTools();
    List<McpResource> listResources();
    List<McpPrompt> listPrompts();
    McpToolResult callTool(String toolName, Object arguments);
    McpResource readResource(String uri);
    String getServerInfo();
    void start();
    void stop();
    boolean isRunning();
}
```

### McpClient 接口

```java
public interface McpClient {
    void connect();
    void disconnect();
    boolean isConnected();
    List<McpTool> listTools();
    List<McpResource> listResources();
    List<McpPrompt> listPrompts();
    McpToolResult callTool(String toolName, Object arguments);
    McpResource readResource(String uri);
    String getServerInfo();
    void setServerUrl(String serverUrl);
    String getServerUrl();
}
```

---

## 💡 示例代码

### 完整的天气查询服务

```java
import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.mcp.server.DefaultMcpServer;
import java.util.*;

public class WeatherServer {
    public static void main(String[] args) {
        DefaultMcpServer server = new DefaultMcpServer("Weather Service", "1.0.0");
        
        McpTool getWeather = new McpTool("getWeather", "获取城市天气");
        McpTool getForecast = new McpTool("getForecast", "获取天气预报");
        
        server.registerTool(getWeather, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String city = (String) args.getOrDefault("city", "北京");
            
            McpToolResult result = new McpToolResult(true);
            result.setContent(List.of(
                new McpToolResult.Content("text", 
                    String.format("%s: 晴朗，温度 22-28°C，湿度 60%%", city))
            ));
            return result;
        });
        
        server.registerTool(getForecast, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String city = (String) args.getOrDefault("city", "北京");
            int days = (int) args.getOrDefault("days", 3);
            
            McpToolResult result = new McpToolResult(true);
            StringBuilder sb = new StringBuilder();
            sb.append(city).append(" 未来 ").append(days).append(" 天预报：\n");
            for (int i = 1; i <= days; i++) {
                sb.append(String.format("第%d天: 晴朗，25°C\n", i));
            }
            result.setContent(List.of(
                new McpToolResult.Content("text", sb.toString())
            ));
            return result;
        });
        
        server.start();
        System.out.println("天气服务已启动！");
    }
}
```

### 资源服务示例

```java
import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.mcp.server.DefaultMcpServer;

public class ResourceServer {
    public static void main(String[] args) {
        DefaultMcpServer server = new DefaultMcpServer("Document Server", "1.0.0");
        
        McpResource doc1 = new McpResource();
        doc1.setUri("docs://est/overview");
        doc1.setName("EST Framework 概述");
        doc1.setDescription("EST Framework 的核心特性介绍");
        doc1.setMimeType("text/markdown");
        
        McpResource doc2 = new McpResource();
        doc2.setUri("docs://est/quickstart");
        doc2.setName("快速开始指南");
        doc2.setDescription("如何快速开始使用 EST Framework");
        doc2.setMimeType("text/markdown");
        
        server.registerResource(doc1);
        server.registerResource(doc2);
        
        server.start();
        System.out.println("文档服务已启动！");
    }
}
```

---

## 📝 注解驱动开发

使用注解可以更简洁地开发 MCP 工具。

### 定义工具类

```java
import ltd.idcu.est.mcp.api.annotation.*;

@McpTool(name = "weatherTools", description = "天气相关工具")
public class WeatherTools {
    
    @McpToolMethod(name = "getCurrentWeather", description = "获取当前天气")
    public McpToolResult getCurrentWeather(
        @McpParam(name = "city", description = "城市名称", required = true) String city
    ) {
        McpToolResult result = new McpToolResult(true);
        result.setContent(List.of(
            new McpToolResult.Content("text", city + " 当前天气：晴朗")
        ));
        return result;
    }
    
    @McpToolMethod(name = "getForecast", description = "获取天气预报")
    public McpToolResult getForecast(
        @McpParam(name = "city", description = "城市名称", required = true) String city,
        @McpParam(name = "days", description = "天数", required = false) Integer days
    ) {
        int forecastDays = days != null ? days : 3;
        McpToolResult result = new McpToolResult(true);
        result.setContent(List.of(
            new McpToolResult.Content("text", 
                city + " 未来 " + forecastDays + " 天预报：...")
        ));
        return result;
    }
}
```

---

## 📖 相关文档

- [EST AI Suite README](../README.md) - AI Suite 总览
- [EST RAG README](../est-rag/README.md) - RAG 模块文档
- [EST LLM README](../est-llm/README.md) - LLM 模块文档

---

**文档生成时间**: 2026-03-11  
**文档作者**: EST AI Team
