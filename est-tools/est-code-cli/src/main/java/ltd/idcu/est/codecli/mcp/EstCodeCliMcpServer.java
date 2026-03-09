package ltd.idcu.est.codecli.mcp;

import ltd.idcu.est.ai.api.mcp.McpTool;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.ai.api.mcp.McpServer;
import ltd.idcu.est.codecli.prompts.PromptLibrary;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;
import ltd.idcu.est.codecli.skills.SkillManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EstCodeCliMcpServer implements McpServer {
    
    private final Map<String, McpTool> tools = new ConcurrentHashMap<>();
    private final String workDir;
    private final FileIndex fileIndex;
    private final ProjectIndexer indexer;
    private final SkillManager skillManager;
    private final PromptLibrary promptLibrary;
    
    public EstCodeCliMcpServer(String workDir, FileIndex fileIndex, ProjectIndexer indexer, 
                                 SkillManager skillManager, PromptLibrary promptLibrary) {
        this.workDir = workDir;
        this.fileIndex = fileIndex;
        this.indexer = indexer;
        this.skillManager = skillManager;
        this.promptLibrary = promptLibrary;
        initializeDefaultTools();
    }
    
    private void initializeDefaultTools() {
        registerTool(new ReadFileMcpTool(workDir));
        registerTool(new WriteFileMcpTool(workDir));
        registerTool(new ListDirMcpTool(workDir));
        registerTool(new ScaffoldMcpTool());
        registerTool(new CodeGenMcpTool());
        registerTool(new IndexProjectMcpTool(fileIndex, indexer, workDir));
        registerTool(new SearchMcpTool(fileIndex, indexer));
        registerTool(new ListSkillsMcpTool(skillManager));
        registerTool(new ListTemplatesMcpTool(promptLibrary));
        registerTool(new RunTestsMcpTool(workDir));
    }
    
    @Override
    public String getName() {
        return "est-code-cli-mcp-server";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public List<McpTool> getTools() {
        return new ArrayList<>(tools.values());
    }
    
    @Override
    public void registerTool(McpTool tool) {
        tools.put(tool.getName(), tool);
    }
    
    @Override
    public void unregisterTool(String toolName) {
        tools.remove(toolName);
    }
    
    @Override
    public McpToolResult callTool(String toolName, Map<String, Object> arguments) {
        McpTool tool = tools.get(toolName);
        if (tool == null) {
            return McpToolResult.error("Tool not found: " + toolName);
        }
        
        try {
            return tool.execute(arguments);
        } catch (Exception e) {
            return McpToolResult.error("Error executing tool: " + e.getMessage());
        }
    }
    
    public String listTools() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available tools:\n\n");
        
        for (McpTool tool : tools.values()) {
            sb.append("  - ").append(tool.getName()).append("\n");
            sb.append("    ").append(tool.getDescription()).append("\n\n");
        }
        
        return sb.toString();
    }
}
