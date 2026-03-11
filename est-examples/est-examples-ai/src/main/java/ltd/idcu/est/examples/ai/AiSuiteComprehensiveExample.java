package ltd.idcu.est.examples.ai;

import ltd.idcu.est.agent.api.*;
import ltd.idcu.est.agent.impl.*;
import ltd.idcu.est.agent.skill.*;
import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.mcp.server.DefaultMcpServer;
import ltd.idcu.est.rag.api.*;
import ltd.idcu.est.rag.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AiSuiteComprehensiveExample {
    
    private static final Logger logger = LoggerFactory.getLogger(AiSuiteComprehensiveExample.class);
    
    public static void main(String[] args) {
        logger.info("=== EST AI Suite 综合示例");
        logger.info("==============================");
        
        try {
            logger.info("\n--- 1. RAG 检索增强生成演示");
            demonstrateRag();
            
            logger.info("\n--- 2. MCP 协议演示");
            demonstrateMcp();
            
            logger.info("\n--- 3. AI Agent 智能体演示");
            demonstrateAgent();
            
            logger.info("\n--- 4. Agent + MCP + RAG 集成演示");
            demonstrateIntegratedSystem();
            
        } catch (Exception e) {
            logger.error("演示过程中出现错误", e);
        }
        
        logger.info("\n=== AI Suite 综合示例完成");
    }
    
    private static void demonstrateRag() {
        logger.info("初始化 RAG 引擎...");
        
        RagEngine ragEngine = new DefaultRagEngine();
        
        TextSplitter textSplitter = new FixedSizeTextSplitter(500);
        ragEngine.setTextSplitter(textSplitter);
        
        VectorStore vectorStore = new InMemoryVectorStore();
        ragEngine.setVectorStore(vectorStore);
        
        EmbeddingModel embeddingModel = new SimpleEmbeddingModel();
        ragEngine.setEmbeddingModel(embeddingModel);
        
        logger.info("添加文档到知识库...");
        List<Document> documents = Arrays.asList(
            new Document("doc1", "EST Framework 是一个企业级 Java 开发框架。它提供了模块化设计、零依赖核心架构等特性。EST Framework 支持多种数据库、消息队列和安全机制。"),
            new Document("doc2", "EST AI Suite 是 EST Framework 的 AI 模块，包含 Agent、MCP、RAG、LLM 等功能。它提供了统一的 API 来访问各种 AI 服务。"),
            new Document("doc3", "RAG（Retrieval-Augmented Generation）是检索增强生成技术，通过检索相关文档来增强大语言模型的生成能力。EST RAG 支持多种分块策略和向量存储。"),
            new Document("doc4", "MCP（Model Context Protocol）是 AI 模型上下文协议，用于在 AI 助手和工具之间建立标准化的通信方式。EST MCP 支持 Server 和 Client 两种模式。"),
            new Document("doc5", "AI Agent 是具备自主决策能力的智能体，可以规划任务、调用工具、记忆对话。EST Agent 支持 Skills 技能体系和 Memory 记忆系统。")
        );
        
        for (Document doc : documents) {
            ragEngine.addDocument(doc);
        }
        
        logger.info("文档添加完成，共 {} 个文档", documents.size());
        
        String query1 = "EST Framework 是什么？";
        logger.info("查询 1: {}", query1);
        String answer1 = ragEngine.retrieveAndGenerate(query1, 3);
        logger.info("回答: {}", answer1);
        
        String query2 = "什么是 RAG？";
        logger.info("查询 2: {}", query2);
        String answer2 = ragEngine.retrieveAndGenerate(query2, 3);
        logger.info("回答: {}", answer2);
        
        String query3 = "AI Agent 有什么功能？";
        logger.info("查询 3: {}", query3);
        String answer3 = ragEngine.retrieveAndGenerate(query3, 3);
        logger.info("回答: {}", answer3);
        
        logger.info("RAG 演示完成");
    }
    
    private static void demonstrateMcp() {
        logger.info("初始化 MCP Server...");
        
        DefaultMcpServer server = new DefaultMcpServer("EST AI Suite Server", "1.0.0");
        
        logger.info("注册 MCP 工具...");
        
        McpTool weatherTool = new McpTool("getWeather", "获取指定城市的天气");
        server.registerTool(weatherTool, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String city = (String) args.getOrDefault("city", "北京");
            
            McpToolResult result = new McpToolResult(true);
            result.setContent(Arrays.asList(
                new McpToolResult.Content("text", city + " 的天气：晴朗，25°C，湿度 60%")
            ));
            return result;
        });
        
        McpTool calculatorTool = new McpTool("calculator", "执行数学计算");
        server.registerTool(calculatorTool, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String expression = (String) args.getOrDefault("expression", "0");
            
            try {
                double result = evaluateExpression(expression);
                McpToolResult toolResult = new McpToolResult(true);
                toolResult.setContent(Arrays.asList(
                    new McpToolResult.Content("text", "计算结果: " + expression + " = " + result)
                ));
                return toolResult;
            } catch (Exception e) {
                McpToolResult toolResult = new McpToolResult(false);
                toolResult.setContent(Arrays.asList(
                    new McpToolResult.Content("text", "计算错误: " + e.getMessage())
                ));
                return toolResult;
            }
        });
        
        McpTool timeTool = new McpTool("getCurrentTime", "获取当前时间");
        server.registerTool(timeTool, arguments -> {
            McpToolResult result = new McpToolResult(true);
            result.setContent(Arrays.asList(
                new McpToolResult.Content("text", "当前时间: " + new Date())
            ));
            return result;
        });
        
        logger.info("MCP 工具注册完成，共 {} 个工具", 3);
        
        logger.info("测试 MCP 工具调用...");
        
        Map<String, Object> weatherArgs = new HashMap<>();
        weatherArgs.put("city", "上海");
        McpToolResult weatherResult = server.callTool("getWeather", weatherArgs);
        logger.info("天气工具调用结果: {}", weatherResult.getContent().get(0).getValue());
        
        Map<String, Object> calcArgs = new HashMap<>();
        calcArgs.put("expression", "25 * 4 + 10");
        McpToolResult calcResult = server.callTool("calculator", calcArgs);
        logger.info("计算器工具调用结果: {}", calcResult.getContent().get(0).getValue());
        
        McpToolResult timeResult = server.callTool("getCurrentTime", new HashMap<>());
        logger.info("时间工具调用结果: {}", timeResult.getContent().get(0).getValue());
        
        logger.info("MCP 演示完成");
    }
    
    private static void demonstrateAgent() {
        logger.info("初始化 AI Agent...");
        
        Agent agent = new DefaultAgent();
        
        Memory memory = new InMemoryMemory();
        agent.setMemory(memory);
        
        logger.info("注册 Agent Skills...");
        
        agent.registerSkill(new CalculatorSkill());
        agent.registerSkill(new WebSearchSkill());
        
        logger.info("Skills 注册完成，共 2 个技能");
        
        logger.info("测试 Agent 单步任务...");
        
        AgentRequest request1 = new AgentRequest();
        request1.setQuery("计算 100 的平方根，然后加上 25");
        
        logger.info("Agent 请求 1: {}", request1.getQuery());
        AgentResponse response1 = agent.process(request1);
        logger.info("Agent 响应 1: {}", response1.getFinalAnswer());
        logger.info("执行步骤数: {}", response1.getSteps().size());
        
        logger.info("测试 Agent 记忆功能...");
        
        AgentRequest request2 = new AgentRequest();
        request2.setQuery("我刚才问了什么问题？");
        
        logger.info("Agent 请求 2: {}", request2.getQuery());
        AgentResponse response2 = agent.process(request2);
        logger.info("Agent 响应 2: {}", response2.getFinalAnswer());
        
        logger.info("AI Agent 演示完成");
    }
    
    private static void demonstrateIntegratedSystem() {
        logger.info("初始化集成系统（Agent + MCP + RAG）...");
        
        Agent agent = new DefaultAgent();
        Memory memory = new InMemoryMemory();
        agent.setMemory(memory);
        
        RagEngine ragEngine = new DefaultRagEngine();
        ragEngine.setTextSplitter(new FixedSizeTextSplitter(500));
        ragEngine.setVectorStore(new InMemoryVectorStore());
        ragEngine.setEmbeddingModel(new SimpleEmbeddingModel());
        
        List<Document> docs = Arrays.asList(
            new Document("est-doc1", "EST Agent 可以调用 MCP 工具来扩展功能。"),
            new Document("est-doc2", "EST RAG 可以为 Agent 提供知识库检索能力。"),
            new Document("est-doc3", "集成的 AI 系统可以完成更复杂的任务。")
        );
        for (Document doc : docs) {
            ragEngine.addDocument(doc);
        }
        
        DefaultMcpServer mcpServer = new DefaultMcpServer("Integrated Server", "1.0.0");
        
        McpTool ragTool = new McpTool("queryKnowledgeBase", "查询知识库");
        mcpServer.registerTool(ragTool, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String query = (String) args.getOrDefault("query", "");
            String answer = ragEngine.retrieveAndGenerate(query, 3);
            
            McpToolResult result = new McpToolResult(true);
            result.setContent(Arrays.asList(
                new McpToolResult.Content("text", answer)
            ));
            return result;
        });
        
        logger.info("集成系统初始化完成");
        
        AgentRequest request = new AgentRequest();
        request.setQuery("请查询知识库，告诉我 EST Agent 有什么特点？");
        
        logger.info("集成系统请求: {}", request.getQuery());
        AgentResponse response = agent.process(request);
        logger.info("集成系统响应: {}", response.getFinalAnswer());
        
        logger.info("集成系统演示完成");
    }
    
    private static double evaluateExpression(String expression) {
        try {
            javax.script.ScriptEngineManager manager = new javax.script.ScriptEngineManager();
            javax.script.ScriptEngine engine = manager.getEngineByName("JavaScript");
            Object result = engine.eval(expression);
            return ((Number) result).doubleValue();
        } catch (Exception e) {
            throw new RuntimeException("表达式计算失败: " + expression, e);
        }
    }
}
