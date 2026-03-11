package ltd.idcu.est.ai.test.integration;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.ai.impl.integration.RagIntegrationAdapter;
import ltd.idcu.est.ai.impl.integration.McpIntegrationAdapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RagMcpAiSuiteIntegrationTest {
    
    private AiAssistant aiAssistant;
    
    @BeforeEach
    void setUp() {
        aiAssistant = new DefaultAiAssistant();
    }
    
    @Test
    void testRagIntegrationBasic() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        assistant.addRagDocument("doc1", "EST Framework is a modern Java development framework", "tech");
        assistant.addRagDocument("doc2", "RAG stands for Retrieval-Augmented Generation", "ai");
        
        List<RagIntegrationAdapter.SearchResultInfo> results = assistant.retrieveWithRag("What is EST Framework?", 2);
        
        assertNotNull(results);
        assertTrue(results.size() > 0);
    }
    
    @Test
    void testRagAdapterAccess() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        RagIntegrationAdapter adapter = assistant.getRagAdapter();
        
        assertNotNull(adapter);
        assertNotNull(adapter.getRagEngine());
        assertNotNull(adapter.getVectorStore());
    }
    
    @Test
    void testMcpToolRegistration() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        assistant.registerMcpTool(
            "calculator",
            "A simple calculator tool",
            args -> {
                int a = (Integer) args.get("a");
                int b = (Integer) args.get("b");
                String op = (String) args.get("operation");
                return switch (op) {
                    case "add" -> a + b;
                    case "subtract" -> a - b;
                    case "multiply" -> a * b;
                    default -> 0;
                };
            }
        );
        
        List<McpIntegrationAdapter.ToolInfo> tools = assistant.listMcpTools();
        
        assertNotNull(tools);
        assertEquals(1, tools.size());
        assertEquals("calculator", tools.get(0).name());
    }
    
    @Test
    void testMcpToolExecution() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        assistant.registerMcpTool(
            "greeting",
            "Generate a greeting message",
            args -> {
                String name = (String) args.get("name");
                return "Hello, " + name + "!";
            }
        );
        
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("name", "EST Developer");
        
        McpIntegrationAdapter.ToolResult result = assistant.callMcpTool("greeting", arguments);
        
        assertNotNull(result);
        assertTrue(result.success());
        assertNotNull(result.content());
        assertTrue(result.content().contains("Hello"));
    }
    
    @Test
    void testMcpResourceRegistration() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        assistant.registerMcpResource(
            "resource://docs/est-guide",
            "EST Framework Guide",
            "This is the official guide for EST Framework"
        );
        
        List<McpIntegrationAdapter.ResourceInfo> resources = assistant.getMcpAdapter().listResources();
        
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("resource://docs/est-guide", resources.get(0).uri());
    }
    
    @Test
    void testMcpPromptRegistration() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        assistant.registerMcpPrompt(
            "code-review",
            "Prompt for code review",
            "Please review the following code: {{code}}"
        );
        
        List<McpIntegrationAdapter.PromptInfo> prompts = assistant.getMcpAdapter().listPrompts();
        
        assertNotNull(prompts);
        assertEquals(1, prompts.size());
        assertEquals("code-review", prompts.get(0).name());
    }
    
    @Test
    void testMcpServerLifecycle() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        assertFalse(assistant.isMcpServerRunning());
        
        assistant.startMcpServer();
        assertTrue(assistant.isMcpServerRunning());
        
        assistant.stopMcpServer();
        assertFalse(assistant.isMcpServerRunning());
    }
    
    @Test
    void testFullIntegrationWorkflow() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        assistant.addRagDocument("guide1", "EST AI Suite provides RAG and MCP capabilities", "documentation");
        assistant.addRagDocument("guide2", "Integration is easy with the adapter pattern", "tutorial");
        
        assistant.registerMcpTool(
            "get-document-count",
            "Get the number of RAG documents",
            args -> 2
        );
        
        List<RagIntegrationAdapter.SearchResultInfo> searchResults = assistant.retrieveWithRag("What capabilities does EST AI Suite provide?", 2);
        List<McpIntegrationAdapter.ToolInfo> tools = assistant.listMcpTools();
        
        assertNotNull(searchResults);
        assertTrue(searchResults.size() > 0);
        assertNotNull(tools);
        assertEquals(1, tools.size());
    }
    
    @Test
    void testCustomAdapterReplacement() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        RagIntegrationAdapter customRagAdapter = new RagIntegrationAdapter();
        McpIntegrationAdapter customMcpAdapter = new McpIntegrationAdapter();
        
        assistant.setRagAdapter(customRagAdapter);
        assistant.setMcpAdapter(customMcpAdapter);
        
        assertSame(customRagAdapter, assistant.getRagAdapter());
        assertSame(customMcpAdapter, assistant.getMcpAdapter());
    }
    
    @Test
    void testMultipleRagDocuments() {
        DefaultAiAssistant assistant = (DefaultAiAssistant) aiAssistant;
        
        for (int i = 0; i < 10; i++) {
            assistant.addRagDocument(
                "doc" + i,
                "Document content " + i + " about testing",
                "test-doc"
            );
        }
        
        List<RagIntegrationAdapter.SearchResultInfo> results = assistant.retrieveWithRag("testing", 5);
        
        assertNotNull(results);
        assertTrue(results.size() <= 5);
    }
}
