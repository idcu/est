package ltd.idcu.est.codecli;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.contract.ContractManager;
import ltd.idcu.est.codecli.contract.ProjectContract;
import ltd.idcu.est.codecli.mcp.EstCodeCliMcpServer;
import ltd.idcu.est.codecli.prompts.PromptLibrary;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;
import ltd.idcu.est.codecli.security.ApprovalManager;
import ltd.idcu.est.codecli.security.HitlSecurityPolicy;
import ltd.idcu.est.codecli.skills.SkillManager;
import ltd.idcu.est.codecli.ux.CommandHistory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CliInteractionHandler {
    
    private final AiAssistant aiAssistant;
    private final ContractManager contractManager;
    private final EstCodeCliMcpServer mcpServer;
    private final FileIndex fileIndex;
    private final ProjectIndexer indexer;
    private final ApprovalManager approvalManager;
    private final SkillManager skillManager;
    private final PromptLibrary promptLibrary;
    private final CommandHistory commandHistory;
    private final Scanner scanner;
    private final String nickname;
    private final Path workDir;
    private boolean running;
    
    public CliInteractionHandler(AiAssistant aiAssistant, String workDir, String nickname) {
        this.aiAssistant = aiAssistant;
        this.workDir = Paths.get(workDir);
        this.contractManager = new ContractManager(this.workDir);
        this.fileIndex = new FileIndex();
        this.indexer = new ProjectIndexer(fileIndex);
        this.approvalManager = new ApprovalManager(new HitlSecurityPolicy());
        this.skillManager = new SkillManager();
        this.promptLibrary = new PromptLibrary();
        this.commandHistory = new CommandHistory();
        this.mcpServer = new EstCodeCliMcpServer(workDir, fileIndex, indexer, skillManager, promptLibrary);
        this.scanner = new Scanner(System.in);
        this.nickname = nickname != null ? nickname : "EST";
        this.running = true;
    }
    
    public void start() {
        printWelcome();
        
        while (running) {
            System.out.print("\n> ");
            String input = scanner.nextLine();
            
            if (input == null || input.trim().isEmpty()) {
                continue;
            }
            
            handleInput(input.trim());
        }
    }
    
    private void printWelcome() {
        System.out.println(nickname + " v1.0.0");
        System.out.println(workDir.toAbsolutePath());
        System.out.println();
        System.out.println("Type 'help' for available commands.");
    }
    
    private void handleInput(String input) {
        commandHistory.add(input);
        
        if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
            running = false;
            System.out.println("Goodbye!");
            return;
        }
        
        if (input.equalsIgnoreCase("help")) {
            printHelp();
            return;
        }
        
        if (input.equalsIgnoreCase("init")) {
            handleInit();
            return;
        }
        
        if (input.equalsIgnoreCase("contract")) {
            handleShowContract();
            return;
        }
        
        if (input.equalsIgnoreCase("tools")) {
            handleListTools();
            return;
        }
        
        if (input.equalsIgnoreCase("skills")) {
            handleListSkills();
            return;
        }
        
        if (input.equalsIgnoreCase("templates")) {
            handleListTemplates();
            return;
        }
        
        if (input.equalsIgnoreCase("history")) {
            handleShowHistory();
            return;
        }
        
        if (input.equalsIgnoreCase("test") || input.equalsIgnoreCase("compile")) {
            handleRunTests(input);
            return;
        }
        
        if (input.startsWith("/")) {
            handleToolCommand(input.substring(1));
            return;
        }
        
        handleChat(input);
    }
    
    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  init       - Initialize workspace and create EST.md");
        System.out.println("  contract   - Show current project contract");
        System.out.println("  tools      - List available MCP tools");
        System.out.println("  skills     - List available EST skills");
        System.out.println("  templates  - List available prompt templates");
        System.out.println("  history    - Show command history");
        System.out.println("  test       - Run Maven tests");
        System.out.println("  compile    - Run Maven compile");
        System.out.println("  /<tool>    - Call an MCP tool directly (e.g., /list_dir)");
        System.out.println("  help       - Show this help message");
        System.out.println("  exit/quit  - Exit the program");
        System.out.println();
        System.out.println("Or just chat naturally - I'll help you with your code!");
    }
    
    private void handleListSkills() {
        System.out.println();
        System.out.println(skillManager.listSkills());
    }
    
    private void handleListTemplates() {
        System.out.println();
        System.out.println(promptLibrary.listTemplates());
    }
    
    private void handleShowHistory() {
        System.out.println();
        System.out.println("Command History:");
        System.out.println("===============");
        List<String> history = commandHistory.getAll();
        for (int i = 0; i < history.size(); i++) {
            System.out.println(String.format("  %d. %s", i + 1, history.get(i)));
        }
        System.out.println();
        System.out.println("Total: " + history.size() + " commands");
    }
    
    private void handleRunTests(String command) {
        System.out.println();
        System.out.println("Running " + command + "...");
        System.out.println();
        
        try {
            Map<String, Object> args = new HashMap<>();
            args.put("type", command.equalsIgnoreCase("compile") ? "compile" : "test");
            
            McpToolResult result = mcpServer.callTool("est_run_tests", args);
            
            if (result.isSuccess()) {
                System.out.println(result.getContent());
            } else {
                System.err.println("Error: " + result.getContent());
            }
        } catch (Exception e) {
            System.err.println("Error running tests: " + e.getMessage());
        }
    }
    
    private void handleInit() {
        System.out.println("Re-initializing workspace...");
        try {
            ProjectContract contract = contractManager.init();
            System.out.println("Workspace initialized successfully!");
            System.out.println("Project: " + contract.getProjectName());
            System.out.println("Type: " + contract.getProjectType());
            System.out.println("EST.md created.");
        } catch (IOException e) {
            System.err.println("Error initializing workspace: " + e.getMessage());
        }
    }
    
    private void handleShowContract() {
        try {
            ProjectContract contract = contractManager.load();
            if (contract == null) {
                System.out.println("No EST.md found. Run 'init' first.");
                return;
            }
            System.out.println(contract);
        } catch (IOException e) {
            System.err.println("Error reading contract: " + e.getMessage());
        }
    }
    
    private void handleListTools() {
        System.out.println();
        System.out.println(mcpServer.listTools());
    }
    
    private void handleToolCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String toolName = parts[0];
        String argsJson = parts.length > 1 ? parts[1] : "{}";
        
        try {
            Map<String, Object> arguments = parseArgs(argsJson);
            
            System.out.println();
            System.out.println("Calling tool: " + toolName);
            System.out.println();
            
            McpToolResult result = mcpServer.callTool(toolName, arguments);
            
            if (result.isSuccess()) {
                System.out.println(result.getContent());
                if (result.getMetadata() != null && !result.getMetadata().isEmpty()) {
                    System.out.println();
                    System.out.println("Metadata: " + result.getMetadata());
                }
            } else {
                System.err.println("Error: " + result.getContent());
            }
            
        } catch (Exception e) {
            System.err.println("Error calling tool: " + e.getMessage());
            System.err.println("Usage example: /list_dir {\"path\":\"src\"}");
        }
    }
    
    private Map<String, Object> parseArgs(String json) {
        Map<String, Object> result = new HashMap<>();
        
        if (json == null || json.trim().isEmpty() || "{}".equals(json.trim())) {
            return result;
        }
        
        try {
            String content = json.trim();
            if (content.startsWith("{") && content.endsWith("}")) {
                content = content.substring(1, content.length() - 1);
                String[] pairs = content.split(",");
                for (String pair : pairs) {
                    String[] kv = pair.split(":", 2);
                    if (kv.length == 2) {
                        String key = kv[0].trim().replace("\"", "").replace("'", "");
                        String value = kv[1].trim().replace("\"", "").replace("'", "");
                        result.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
        }
        
        return result;
    }
    
    private void handleChat(String message) {
        try {
            System.out.println();
            System.out.println(nickname);
            System.out.println();
            
            String response = aiAssistant.chat(message);
            System.out.println(response);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
