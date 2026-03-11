package ltd.idcu.est.ai.test.integration;

import ltd.idcu.est.rag.api.*;
import ltd.idcu.est.rag.impl.*;
import ltd.idcu.est.mcp.api.*;
import ltd.idcu.est.mcp.impl.*;
import ltd.idcu.est.agent.api.*;
import ltd.idcu.est.agent.impl.*;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;
import java.util.Map;

public class EstAiSuiteIntegrationTest {
    
    @Test
    public void testRagAndAgentIntegration() {
        TextSplitter splitter = new FixedSizeTextSplitter();
        EmbeddingModel embeddingModel = new SimpleEmbeddingModel();
        VectorStore vectorStore = new InMemoryVectorStore(embeddingModel);
        RagEngine ragEngine = new DefaultRagEngine(vectorStore, splitter, embeddingModel);
        
        Document doc1 = new Document("doc1", "EST Framework is a powerful Java development framework.");
        Document doc2 = new Document("doc2", "EST AI Suite provides RAG, MCP, and Agent capabilities.");
        ragEngine.addDocuments(List.of(doc1, doc2));
        
        List<SearchResult> results = ragEngine.retrieve("EST", 5);
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        Memory memory = new InMemoryMemory();
        Agent agent = new DefaultAgent(memory);
        
        AgentRequest request = new AgentRequest();
        request.setQuery("What is EST Framework?");
        request.setMaxSteps(3);
        
        AgentResponse response = agent.process(request);
        assertNotNull(response);
        assertNotNull(response.getFinalAnswer());
    }
    
    @Test
    public void testMcpAndAgentIntegration() {
        McpServer mcpServer = new DefaultMcpServer();
        
        McpTool tool = new McpTool();
        tool.setName("test_tool");
        tool.setDescription("Test tool for integration");
        mcpServer.registerTool(tool);
        
        List<McpTool> tools = mcpServer.listTools();
        assertEquals(1, tools.size());
        
        McpToolResult result = mcpServer.callTool("test_tool", Map.of());
        assertNotNull(result);
        
        Memory memory = new InMemoryMemory();
        Agent agent = new DefaultAgent(memory);
        
        AgentRequest request = new AgentRequest();
        request.setQuery("Use the test tool");
        AgentResponse response = agent.process(request);
        
        assertNotNull(response);
        assertNotNull(response.getFinalAnswer());
    }
    
    @Test
    public void testFullWorkflow() {
        RagEngine ragEngine = new DefaultRagEngine();
        McpServer mcpServer = new DefaultMcpServer();
        Memory memory = new InMemoryMemory();
        Agent agent = new DefaultAgent(memory);
        
        Document knowledgeDoc = new Document("kb1", "EST AI Suite includes RAG for retrieval, MCP for tools, and Agent for reasoning.");
        ragEngine.addDocument(knowledgeDoc);
        
        McpTool searchTool = new McpTool();
        searchTool.setName("search_kb");
        searchTool.setDescription("Search knowledge base");
        mcpServer.registerTool(searchTool);
        
        MemoryItem convItem = new MemoryItem();
        convItem.setType(MemoryItem.MemoryType.CONVERSATION);
        convItem.setContent("User: Tell me about EST AI Suite");
        memory.add(convItem);
        
        AgentRequest request = new AgentRequest();
        request.setQuery("What capabilities does EST AI Suite provide?");
        request.setMaxSteps(5);
        
        AgentResponse response = agent.process(request);
        
        assertNotNull(response);
        assertNotNull(response.getFinalAnswer());
        assertNotNull(response.getSteps());
        
        List<MemoryItem> history = memory.getAll();
        assertFalse(history.isEmpty());
    }
    
    @Test
    public void testDocumentProcessingPipeline() {
        TextSplitter splitter = new FixedSizeTextSplitter(200, 50);
        EmbeddingModel embeddingModel = new SimpleEmbeddingModel();
        VectorStore vectorStore = new InMemoryVectorStore(embeddingModel);
        
        String longContent = "EST Framework is a comprehensive Java development framework. " +
                            "It provides modular design, zero-dependency core, and extensive plugin system. " +
                            "EST AI Suite adds AI capabilities including RAG for knowledge retrieval, " +
                            "MCP for tool integration, and Agent for autonomous reasoning. " +
                            "Developers can build powerful AI applications with ease.";
        
        Document doc = new Document("doc1", longContent);
        
        List<DocumentChunk> chunks = splitter.split(doc);
        assertFalse(chunks.isEmpty());
        assertTrue(chunks.size() >= 1);
        
        for (DocumentChunk chunk : chunks) {
            Embedding embedding = new Embedding(
                chunk.getId(),
                chunk.getDocumentId(),
                chunk.getContent(),
                embeddingModel.embedToVector(chunk.getContent())
            );
            vectorStore.addEmbedding(embedding);
        }
        
        assertTrue(vectorStore.size() >= chunks.size());
        
        List<SearchResult> results = vectorStore.search("EST AI", 3);
        assertNotNull(results);
    }
    
    @Test
    public void testAgentWithMultipleSkills() {
        Memory memory = new InMemoryMemory();
        Agent agent = new DefaultAgent(memory);
        
        Skill skill1 = new Skill() {
            @Override
            public String getName() { return "skill1"; }
            @Override
            public String getDescription() { return "Skill 1"; }
            @Override
            public void initialize(SkillContext context) {}
            @Override
            public SkillResult execute(SkillInput input, SkillContext context) {
                return SkillResult.success("Result from skill 1");
            }
            @Override
            public void cleanup(SkillContext context) {}
        };
        
        Skill skill2 = new Skill() {
            @Override
            public String getName() { return "skill2"; }
            @Override
            public String getDescription() { return "Skill 2"; }
            @Override
            public void initialize(SkillContext context) {}
            @Override
            public SkillResult execute(SkillInput input, SkillContext context) {
                return SkillResult.success("Result from skill 2");
            }
            @Override
            public void cleanup(SkillContext context) {}
        };
        
        agent.registerSkill(skill1);
        agent.registerSkill(skill2);
        
        List<Skill> skills = agent.getAllSkills();
        assertEquals(2, skills.size());
        
        Skill retrieved1 = agent.getSkill("skill1");
        Skill retrieved2 = agent.getSkill("skill2");
        
        assertNotNull(retrieved1);
        assertNotNull(retrieved2);
        
        SkillResult result1 = retrieved1.execute(new SkillInput(), null);
        SkillResult result2 = retrieved2.execute(new SkillInput(), null);
        
        assertTrue(result1.isSuccess());
        assertTrue(result2.isSuccess());
        assertEquals("Result from skill 1", result1.getData());
        assertEquals("Result from skill 2", result2.getData());
    }
}
