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
        logger.info("=== EST AI Suite Comprehensive Example");
        logger.info("==============================");
        
        try {
            logger.info("\n--- 1. RAG Retrieval-Augmented Generation Demo");
            demonstrateRag();
            
            logger.info("\n--- 2. MCP Protocol Demo");
            demonstrateMcp();
            
            logger.info("\n--- 3. AI Agent Demo");
            demonstrateAgent();
            
            logger.info("\n--- 4. Agent + MCP + RAG Integrated Demo");
            demonstrateIntegratedSystem();
            
        } catch (Exception e) {
            logger.error("Error during demo", e);
        }
        
        logger.info("\n=== AI Suite Comprehensive Example Complete");
    }
    
    private static void demonstrateRag() {
        logger.info("Initializing RAG engine...");
        
        RagEngine ragEngine = new DefaultRagEngine();
        
        TextSplitter textSplitter = new FixedSizeTextSplitter(500);
        ragEngine.setTextSplitter(textSplitter);
        
        VectorStore vectorStore = new InMemoryVectorStore();
        ragEngine.setVectorStore(vectorStore);
        
        EmbeddingModel embeddingModel = new SimpleEmbeddingModel();
        ragEngine.setEmbeddingModel(embeddingModel);
        
        logger.info("Adding documents to knowledge base...");
        List<Document> documents = Arrays.asList(
            new Document("doc1", "EST Framework is an enterprise-grade Java development framework. It provides modular design, zero-dependency core architecture, and other features. EST Framework supports multiple databases, message queues, and security mechanisms."),
            new Document("doc2", "EST AI Suite is the AI module of EST Framework, including Agent, MCP, RAG, LLM and other features. It provides a unified API to access various AI services."),
            new Document("doc3", "RAG (Retrieval-Augmented Generation) is a retrieval-augmented generation technology that enhances the generation capabilities of large language models by retrieving relevant documents. EST RAG supports multiple chunking strategies and vector storage."),
            new Document("doc4", "MCP (Model Context Protocol) is an AI model context protocol for establishing standardized communication between AI assistants and tools. EST MCP supports both Server and Client modes."),
            new Document("doc5", "AI Agent is an intelligent agent with autonomous decision-making capabilities that can plan tasks, call tools, and remember conversations. EST Agent supports Skills system and Memory system.")
        );
        
        for (Document doc : documents) {
            ragEngine.addDocument(doc);
        }
        
        logger.info("Documents added, total {} documents", documents.size());
        
        String query1 = "What is EST Framework?";
        logger.info("Query 1: {}", query1);
        String answer1 = ragEngine.retrieveAndGenerate(query1, 3);
        logger.info("Answer: {}", answer1);
        
        String query2 = "What is RAG?";
        logger.info("Query 2: {}", query2);
        String answer2 = ragEngine.retrieveAndGenerate(query2, 3);
        logger.info("Answer: {}", answer2);
        
        String query3 = "What features does AI Agent have?";
        logger.info("Query 3: {}", query3);
        String answer3 = ragEngine.retrieveAndGenerate(query3, 3);
        logger.info("Answer: {}", answer3);
        
        logger.info("RAG demo complete");
    }
    
    private static void demonstrateMcp() {
        logger.info("Initializing MCP Server...");
        
        DefaultMcpServer server = new DefaultMcpServer("EST AI Suite Server", "1.0.0");
        
        logger.info("Registering MCP tools...");
        
        McpTool weatherTool = new McpTool("getWeather", "Get weather for specified city");
        server.registerTool(weatherTool, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String city = (String) args.getOrDefault("city", "Beijing");
            
            McpToolResult result = new McpToolResult(true);
            result.setContent(Arrays.asList(
                new McpToolResult.Content("text", city + " weather: Sunny, 25°C, humidity 60%")
            ));
            return result;
        });
        
        McpTool calculatorTool = new McpTool("calculator", "Perform mathematical calculations");
        server.registerTool(calculatorTool, arguments -> {
            Map<String, Object> args = (Map<String, Object>) arguments;
            String expression = (String) args.getOrDefault("expression", "0");
            
            try {
                double result = evaluateExpression(expression);
                McpToolResult toolResult = new McpToolResult(true);
                toolResult.setContent(Arrays.asList(
                    new McpToolResult.Content("text", "Calculation result: " + expression + " = " + result)
                ));
                return toolResult;
            } catch (Exception e) {
                McpToolResult toolResult = new McpToolResult(false);
                toolResult.setContent(Arrays.asList(
                    new McpToolResult.Content("text", "Calculation error: " + e.getMessage())
                ));
                return toolResult;
            }
        });
        
        McpTool timeTool = new McpTool("getCurrentTime", "Get current time");
        server.registerTool(timeTool, arguments -> {
            McpToolResult result = new McpToolResult(true);
            result.setContent(Arrays.asList(
                new McpToolResult.Content("text", "Current time: " + new Date())
            ));
            return result;
        });
        
        logger.info("MCP tools registered, total {} tools", 3);
        
        logger.info("Testing MCP tool calls...");
        
        Map<String, Object> weatherArgs = new HashMap<>();
        weatherArgs.put("city", "Shanghai");
        McpToolResult weatherResult = server.callTool("getWeather", weatherArgs);
        logger.info("Weather tool result: {}", weatherResult.getContent().get(0).getValue());
        
        Map<String, Object> calcArgs = new HashMap<>();
        calcArgs.put("expression", "25 * 4 + 10");
        McpToolResult calcResult = server.callTool("calculator", calcArgs);
        logger.info("Calculator tool result: {}", calcResult.getContent().get(0).getValue());
        
        McpToolResult timeResult = server.callTool("getCurrentTime", new HashMap<>());
        logger.info("Time tool result: {}", timeResult.getContent().get(0).getValue());
        
        logger.info("MCP demo complete");
    }
    
    private static void demonstrateAgent() {
        logger.info("Initializing AI Agent...");
        
        Agent agent = new DefaultAgent();
        
        Memory memory = new InMemoryMemory();
        agent.setMemory(memory);
        
        logger.info("Registering Agent Skills...");
        
        agent.registerSkill(new CalculatorSkill());
        agent.registerSkill(new WebSearchSkill());
        
        logger.info("Skills registered, total 2 skills");
        
        logger.info("Testing Agent single-step task...");
        
        AgentRequest request1 = new AgentRequest();
        request1.setQuery("Calculate the square root of 100, then add 25");
        
        logger.info("Agent request 1: {}", request1.getQuery());
        AgentResponse response1 = agent.process(request1);
        logger.info("Agent response 1: {}", response1.getFinalAnswer());
        logger.info("Execution steps: {}", response1.getSteps().size());
        
        logger.info("Testing Agent memory function...");
        
        AgentRequest request2 = new AgentRequest();
        request2.setQuery("What question did I just ask?");
        
        logger.info("Agent request 2: {}", request2.getQuery());
        AgentResponse response2 = agent.process(request2);
        logger.info("Agent response 2: {}", response2.getFinalAnswer());
        
        logger.info("AI Agent demo complete");
    }
    
    private static void demonstrateIntegratedSystem() {
        logger.info("Initializing integrated system (Agent + MCP + RAG)...");
        
        Agent agent = new DefaultAgent();
        Memory memory = new InMemoryMemory();
        agent.setMemory(memory);
        
        RagEngine ragEngine = new DefaultRagEngine();
        ragEngine.setTextSplitter(new FixedSizeTextSplitter(500));
        ragEngine.setVectorStore(new InMemoryVectorStore());
        ragEngine.setEmbeddingModel(new SimpleEmbeddingModel());
        
        List<Document> docs = Arrays.asList(
            new Document("est-doc1", "EST Agent can call MCP tools to extend functionality."),
            new Document("est-doc2", "EST RAG can provide knowledge base retrieval capabilities for Agent."),
            new Document("est-doc3", "Integrated AI system can complete more complex tasks.")
        );
        for (Document doc : docs) {
            ragEngine.addDocument(doc);
        }
        
        DefaultMcpServer mcpServer = new DefaultMcpServer("Integrated Server", "1.0.0");
        
        McpTool ragTool = new McpTool("queryKnowledgeBase", "Query knowledge base");
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
        
        logger.info("Integrated system initialized");
        
        AgentRequest request = new AgentRequest();
        request.setQuery("Please query the knowledge base and tell me what features does EST Agent have?");
        
        logger.info("Integrated system request: {}", request.getQuery());
        AgentResponse response = agent.process(request);
        logger.info("Integrated system response: {}", response.getFinalAnswer());
        
        logger.info("Integrated system demo complete");
    }
    
    private static double evaluateExpression(String expression) {
        try {
            javax.script.ScriptEngineManager manager = new javax.script.ScriptEngineManager();
            javax.script.ScriptEngine engine = manager.getEngineByName("JavaScript");
            Object result = engine.eval(expression);
            return ((Number) result).doubleValue();
        } catch (Exception e) {
            throw new RuntimeException("Expression evaluation failed: " + expression, e);
        }
    }
}
