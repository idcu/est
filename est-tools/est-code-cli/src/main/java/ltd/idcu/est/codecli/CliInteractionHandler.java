package ltd.idcu.est.codecli;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.mcp.McpToolResult;
import ltd.idcu.est.codecli.context.ConversationContext;
import ltd.idcu.est.codecli.contract.ContractManager;
import ltd.idcu.est.codecli.contract.ProjectContract;
import ltd.idcu.est.codecli.mcp.EstCodeCliMcpServer;
import ltd.idcu.est.codecli.plugin.PluginManager;
import ltd.idcu.est.codecli.plugin.PluginMarketplaceManager;
import ltd.idcu.est.codecli.prompts.PromptLibrary;
import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.codecli.search.ProjectIndexer;
import ltd.idcu.est.codecli.security.ApprovalManager;
import ltd.idcu.est.codecli.web.EstWebServer;
import ltd.idcu.est.codecli.acp.AcpServer;
import ltd.idcu.est.codecli.security.HitlSecurityPolicy;
import ltd.idcu.est.codecli.skills.SkillManager;
import ltd.idcu.est.codecli.ux.CommandHistory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
    private final ConversationContext conversationContext;
    private final PluginManager pluginManager;
    private final PluginMarketplaceManager pluginMarketplaceManager;
    private final CliConfig config;
    private final Scanner scanner;
    private final String nickname;
    private final Path workDir;
    private boolean running;
    
    public CliInteractionHandler(AiAssistant aiAssistant, String workDir, String nickname) {
        this.aiAssistant = aiAssistant;
        this.config = CliConfig.load();
        this.workDir = Paths.get(workDir);
        this.contractManager = new ContractManager(this.workDir);
        this.fileIndex = new FileIndex();
        this.indexer = new ProjectIndexer(fileIndex);
        this.approvalManager = new ApprovalManager(new HitlSecurityPolicy());
        this.skillManager = new SkillManager();
        this.promptLibrary = new PromptLibrary();
        this.commandHistory = new CommandHistory();
        this.conversationContext = new ConversationContext();
        this.pluginManager = new PluginManager(this.workDir);
        this.pluginMarketplaceManager = new PluginMarketplaceManager(this.workDir);
        this.mcpServer = new EstCodeCliMcpServer(workDir, fileIndex, indexer, skillManager, promptLibrary);
        this.scanner = new Scanner(System.in);
        this.nickname = nickname != null ? nickname : config.getNickname();
        this.running = true;
        this.pluginManager.loadAllPlugins();
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
        
        if (input.equalsIgnoreCase("config")) {
            handleConfig();
            return;
        }
        
        if (input.equalsIgnoreCase("web")) {
            handleWeb();
            return;
        }
        
        if (input.equalsIgnoreCase("acp")) {
            handleAcp();
            return;
        }
        
        if (input.equalsIgnoreCase("clear") || input.equalsIgnoreCase("reset")) {
            handleClearContext();
            return;
        }
        
        if (input.equalsIgnoreCase("context")) {
            handleShowContext();
            return;
        }
        
        if (input.equalsIgnoreCase("plugins")) {
            handlePlugins();
            return;
        }
        
        if (input.equalsIgnoreCase("marketplace")) {
            handleMarketplace();
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
        System.out.println("  context    - Show conversation context");
        System.out.println("  clear/reset- Clear conversation context");
        System.out.println("  test       - Run Maven tests");
        System.out.println("  compile    - Run Maven compile");
        System.out.println("  config     - Manage configuration");
        System.out.println("  plugins    - Manage plugins");
        System.out.println("  marketplace- Browse plugin marketplace");
        System.out.println("  web        - Start web server (browser interface)");
        System.out.println("  acp        - Start ACP server (IDE integration)");
        System.out.println("  /<tool>    - Call an MCP tool directly (e.g., /list_dir)");
        System.out.println("  help       - Show this help message");
        System.out.println("  exit/quit  - Exit the program");
        System.out.println();
        System.out.println("Or just chat naturally - I'll help you with your code!");
    }
    
    private void handleConfig() {
        System.out.println();
        System.out.println("Current Configuration:");
        System.out.println("=".repeat(40));
        System.out.println("  Nickname:     " + config.getNickname());
        System.out.println("  Work Dir:     " + config.getWorkDir());
        System.out.println("  Planning Mode: " + config.isPlanningMode());
        System.out.println("  HITL Enabled: " + config.isHitlEnabled());
        if (config.getChatModelApiUrl() != null) {
            System.out.println("  Chat API URL: " + config.getChatModelApiUrl());
        }
        if (config.getChatModelName() != null) {
            System.out.println("  Chat Model:   " + config.getChatModelName());
        }
        System.out.println("=".repeat(40));
        System.out.println();
        System.out.println("Options:");
        System.out.println("  1. Set Nickname");
        System.out.println("  2. Toggle Planning Mode");
        System.out.println("  3. Toggle HITL Security");
        System.out.println("  4. Save Configuration");
        System.out.println("  5. Save to User Config");
        System.out.println("  0. Back");
        System.out.println();
        System.out.print("Choose an option: ");
        
        try {
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Enter new nickname: ");
                    String newNickname = scanner.nextLine().trim();
                    if (!newNickname.isEmpty()) {
                        config.setNickname(newNickname);
                        System.out.println("Nickname updated to: " + newNickname);
                    }
                    break;
                case "2":
                    config.setPlanningMode(!config.isPlanningMode());
                    System.out.println("Planning Mode: " + (config.isPlanningMode() ? "Enabled" : "Disabled"));
                    break;
                case "3":
                    config.setHitlEnabled(!config.isHitlEnabled());
                    System.out.println("HITL Security: " + (config.isHitlEnabled() ? "Enabled" : "Disabled"));
                    break;
                case "4":
                    config.save();
                    System.out.println("Configuration saved to est-code-cli.yml");
                    break;
                case "5":
                    config.saveToUserConfig();
                    System.out.println("Configuration saved to user config");
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid option");
            }
        } catch (IOException e) {
            System.err.println("Error saving config: " + e.getMessage());
        }
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
            
            EstSkill matchingSkill = skillManager.findMatchingSkill(message);
            if (matchingSkill != null) {
                System.out.println("💡 检测到您可能需要使用技能: " + matchingSkill.getName());
                System.out.println("   " + matchingSkill.getDescription());
                System.out.println();
                System.out.println("是否要使用该技能？(y/n，或直接继续对话)");
                System.out.print("> ");
                String choice = scanner.nextLine().trim().toLowerCase();
                
                if (choice.equals("y") || choice.equals("yes")) {
                    handleSkillInvocation(matchingSkill, message);
                    return;
                }
            }
            
            conversationContext.addUserMessage(message);
            String promptWithContext = conversationContext.buildPrompt(message);
            String response = aiAssistant.chat(promptWithContext);
            conversationContext.addAssistantMessage(response);
            System.out.println(response);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleClearContext() {
        conversationContext.clear();
        System.out.println();
        System.out.println("Conversation context cleared.");
        System.out.println("The AI will no longer remember previous messages.");
    }
    
    private void handleShowContext() {
        System.out.println();
        System.out.println("Conversation Context:");
        System.out.println("=".repeat(50));
        System.out.println("Message count: " + conversationContext.size());
        System.out.println();
        
        if (conversationContext.isEmpty()) {
            System.out.println("(No messages in context)");
        } else {
            for (ConversationContext.ConversationMessage msg : conversationContext.getMessages()) {
                System.out.println("[" + msg.getRole().toUpperCase() + "]");
                System.out.println(msg.getContent());
                System.out.println();
            }
        }
        System.out.println("=".repeat(50));
    }
    
    private void handleSkillInvocation(EstSkill skill, String userMessage) {
        try {
            System.out.println();
            System.out.println("使用技能: " + skill.getName());
            System.out.println("=".repeat(50));
            System.out.println();
            
            System.out.println("请提供要处理的代码文件路径，或直接粘贴代码：");
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            String codeContent = "";
            if (input.startsWith("/") || input.contains("\\") || input.endsWith(".java") || input.endsWith(".md")) {
                Path filePath = workDir.resolve(input);
                if (Files.exists(filePath)) {
                    codeContent = Files.readString(filePath, StandardCharsets.UTF_8);
                    System.out.println("已读取文件: " + filePath);
                } else {
                    System.err.println("文件不存在: " + filePath);
                    return;
                }
            } else {
                codeContent = input;
                System.out.println("使用提供的代码内容");
            }
            
            System.out.println();
            System.out.println("正在处理...");
            System.out.println();
            
            String prompt = skill.getPromptTemplate().replace("{{code}}", codeContent);
            String response = aiAssistant.chat(prompt);
            
            System.out.println(response);
            
        } catch (Exception e) {
            System.err.println("Error invoking skill: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleWeb() {
        System.out.println();
        System.out.println("Starting EST Code CLI Web Server...");
        System.out.print("Enter port (default 8080): ");
        String portInput = scanner.nextLine().trim();
        
        int port = 8080;
        if (!portInput.isEmpty()) {
            try {
                port = Integer.parseInt(portInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using 8080");
            }
        }
        
        try {
            EstWebServer server = new EstWebServer(port, workDir.toString());
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down web server...");
                server.stop();
            }));
            
            System.out.println("EST Code CLI Web Server started on http://localhost:" + port);
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println();
            
            server.start();
            
        } catch (IOException e) {
            System.err.println("Could not start web server: " + e.getMessage());
        }
    }
    
    private void handleAcp() {
        System.out.println();
        System.out.println("Starting EST Code CLI ACP Server...");
        System.out.print("Enter port (default 3000): ");
        String portInput = scanner.nextLine().trim();
        
        int port = 3000;
        if (!portInput.isEmpty()) {
            try {
                port = Integer.parseInt(portInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using 3000");
            }
        }
        
        try {
            AcpServer server = new AcpServer(port, workDir.toString());
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down ACP server...");
                server.stop();
            }));
            
            System.out.println("EST Code CLI ACP Server started on port " + port);
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println();
            
            server.start();
            
        } catch (IOException e) {
            System.err.println("Could not start ACP server: " + e.getMessage());
        }
    }
    
    private void handlePlugins() {
        System.out.println();
        System.out.println("Plugin Management:");
        System.out.println("=".repeat(40));
        System.out.println();
        System.out.println(pluginMarketplaceManager.listPluginsAsString());
        System.out.println();
        System.out.println("Options:");
        System.out.println("  1. Load plugin from file/directory");
        System.out.println("  2. Unload plugin");
        System.out.println("  3. Reload all plugins");
        System.out.println("  0. Back");
        System.out.println();
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleLoadPlugin();
                break;
            case "2":
                handleUnloadPlugin();
                break;
            case "3":
                handleReloadPlugins();
                break;
            case "0":
                break;
            default:
                System.out.println("Invalid option");
        }
    }
    
    private void handleLoadPlugin() {
        System.out.print("Enter plugin path (file or directory): ");
        String pathInput = scanner.nextLine().trim();
        if (pathInput.isEmpty()) {
            System.out.println("Path cannot be empty");
            return;
        }
        
        try {
            Path pluginPath = Paths.get(pathInput);
            if (!Files.exists(pluginPath)) {
                System.err.println("Path does not exist: " + pluginPath);
                return;
            }
            pluginManager.loadPlugin(pluginPath);
            System.out.println("Plugin loaded successfully!");
        } catch (Exception e) {
            System.err.println("Failed to load plugin: " + e.getMessage());
        }
    }
    
    private void handleUnloadPlugin() {
        System.out.print("Enter plugin ID to unload: ");
        String pluginId = scanner.nextLine().trim();
        if (pluginId.isEmpty()) {
            System.out.println("Plugin ID cannot be empty");
            return;
        }
        
        try {
            pluginManager.unloadPlugin(pluginId);
            System.out.println("Plugin unloaded successfully!");
        } catch (Exception e) {
            System.err.println("Failed to unload plugin: " + e.getMessage());
        }
    }
    
    private void handleReloadPlugins() {
        System.out.println("Reloading all plugins...");
        pluginManager.unloadAllPlugins();
        pluginManager.loadAllPlugins();
        System.out.println("Plugins reloaded successfully!");
    }
    
    private void handleMarketplace() {
        System.out.println();
        System.out.println("Plugin Marketplace:");
        System.out.println("=".repeat(40));
        System.out.println();
        System.out.println("Options:");
        System.out.println("  1. Search plugins");
        System.out.println("  2. Browse popular plugins");
        System.out.println("  3. Browse latest plugins");
        System.out.println("  4. Browse certified plugins");
        System.out.println("  5. Browse categories");
        System.out.println("  6. Install plugin");
        System.out.println("  7. Update plugin");
        System.out.println("  8. Check for updates");
        System.out.println("  0. Back");
        System.out.println();
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleSearchMarketplace();
                break;
            case "2":
                handlePopularPlugins();
                break;
            case "3":
                handleLatestPlugins();
                break;
            case "4":
                handleCertifiedPlugins();
                break;
            case "5":
                handleBrowseCategories();
                break;
            case "6":
                handleInstallPlugin();
                break;
            case "7":
                handleUpdatePlugin();
                break;
            case "8":
                handleCheckUpdates();
                break;
            case "0":
                break;
            default:
                System.out.println("Invalid option");
        }
    }
    
    private void handleSearchMarketplace() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("Query cannot be empty");
            return;
        }
        System.out.println();
        System.out.println(pluginMarketplaceManager.searchPluginsAsString(query));
    }
    
    private void handlePopularPlugins() {
        System.out.print("Number of plugins to show (default 10): ");
        String limitInput = scanner.nextLine().trim();
        int limit = 10;
        if (!limitInput.isEmpty()) {
            try {
                limit = Integer.parseInt(limitInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, using 10");
            }
        }
        
        var plugins = pluginMarketplaceManager.getPopularPlugins(limit);
        System.out.println();
        System.out.println("Popular Plugins (" + plugins.size() + "):");
        for (var plugin : plugins) {
            System.out.println("  - " + plugin.getName() + " v" + plugin.getVersion());
            System.out.println("    " + plugin.getDescription());
        }
    }
    
    private void handleLatestPlugins() {
        System.out.print("Number of plugins to show (default 10): ");
        String limitInput = scanner.nextLine().trim();
        int limit = 10;
        if (!limitInput.isEmpty()) {
            try {
                limit = Integer.parseInt(limitInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, using 10");
            }
        }
        
        var plugins = pluginMarketplaceManager.getLatestPlugins(limit);
        System.out.println();
        System.out.println("Latest Plugins (" + plugins.size() + "):");
        for (var plugin : plugins) {
            System.out.println("  - " + plugin.getName() + " v" + plugin.getVersion());
            System.out.println("    " + plugin.getDescription());
        }
    }
    
    private void handleCertifiedPlugins() {
        var plugins = pluginMarketplaceManager.getCertifiedPlugins();
        System.out.println();
        System.out.println("Certified Plugins (" + plugins.size() + "):");
        for (var plugin : plugins) {
            System.out.println("  - " + plugin.getName() + " v" + plugin.getVersion());
            System.out.println("    " + plugin.getDescription());
        }
    }
    
    private void handleBrowseCategories() {
        var categories = pluginMarketplaceManager.getCategories();
        System.out.println();
        System.out.println("Categories (" + categories.size() + "):");
        for (String category : categories) {
            System.out.println("  - " + category);
        }
        
        if (!categories.isEmpty()) {
            System.out.println();
            System.out.print("Enter category to browse (or press Enter to skip): ");
            String category = scanner.nextLine().trim();
            if (!category.isEmpty()) {
                var plugins = pluginMarketplaceManager.searchPluginsByCategory(category);
                System.out.println();
                System.out.println("Plugins in category '" + category + "' (" + plugins.size() + "):");
                for (var plugin : plugins) {
                    System.out.println("  - " + plugin.getName() + " v" + plugin.getVersion());
                    System.out.println("    " + plugin.getDescription());
                }
            }
        }
    }
    
    private void handleInstallPlugin() {
        System.out.print("Enter plugin ID to install: ");
        String pluginId = scanner.nextLine().trim();
        if (pluginId.isEmpty()) {
            System.out.println("Plugin ID cannot be empty");
            return;
        }
        
        System.out.print("Enter version (optional, press Enter for latest): ");
        String version = scanner.nextLine().trim();
        
        boolean success;
        if (version.isEmpty()) {
            success = pluginMarketplaceManager.installPlugin(pluginId);
        } else {
            success = pluginMarketplaceManager.installPlugin(pluginId, version);
        }
        
        if (success) {
            System.out.println("Plugin installed successfully!");
        } else {
            System.err.println("Failed to install plugin");
        }
    }
    
    private void handleUpdatePlugin() {
        System.out.print("Enter plugin ID to update: ");
        String pluginId = scanner.nextLine().trim();
        if (pluginId.isEmpty()) {
            System.out.println("Plugin ID cannot be empty");
            return;
        }
        
        boolean success = pluginMarketplaceManager.updatePlugin(pluginId);
        if (success) {
            System.out.println("Plugin updated successfully!");
        } else {
            System.err.println("Failed to update plugin");
        }
    }
    
    private void handleCheckUpdates() {
        var updates = pluginMarketplaceManager.getUpdatesAvailable();
        System.out.println();
        if (updates.isEmpty()) {
            System.out.println("All plugins are up to date!");
        } else {
            System.out.println("Available updates (" + updates.size() + "):");
            for (var plugin : updates) {
                System.out.println("  - " + plugin.getName() + " v" + plugin.getVersion());
            }
        }
    }
}
