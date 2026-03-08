package ltd.idcu.est.features.ai.impl;

import ltd.idcu.est.features.ai.api.*;
import ltd.idcu.est.features.ai.impl.llm.LlmClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultAiAssistant implements AiAssistant {
    
    private final PromptTemplateRegistry templateRegistry;
    private final CodeGenerator codeGenerator;
    private final ProjectScaffold projectScaffold;
    private final RequirementParser requirementParser;
    private final ArchitectureDesigner architectureDesigner;
    private final TestAndDeployManager testAndDeployManager;
    private LlmClient llmClient;
    private final List<LlmMessage> conversationHistory;
    
    public DefaultAiAssistant() {
        this.templateRegistry = new DefaultPromptTemplateRegistry();
        this.codeGenerator = new DefaultCodeGenerator();
        this.projectScaffold = new DefaultProjectScaffold();
        this.requirementParser = new DefaultRequirementParser();
        this.architectureDesigner = new DefaultArchitectureDesigner();
        this.testAndDeployManager = new DefaultTestAndDeployManager();
        this.llmClient = LlmClientFactory.create();
        this.conversationHistory = new ArrayList<>();
        initializeDefaultTemplates();
    }
    
    public DefaultAiAssistant(LlmClient llmClient) {
        this();
        this.llmClient = llmClient;
    }
    
    private void initializeDefaultTemplates() {
        templateRegistry.register(new DefaultPromptTemplate(
            "web-app-basic",
            "web",
            "生成一个基础的Web应用",
            """
            使用EST框架创建一个Web应用，包含以下功能：
            1. 创建一个WebApplication实例
            2. 添加首页路由
            3. 添加API路由返回JSON
            4. 启动服务器在8080端口
            
            参考代码：
            import ltd.idcu.est.web.Web;
            import ltd.idcu.est.web.api.WebApplication;
            
            public class {{className}} {
                public static void main(String[] args) {
                    WebApplication app = Web.create("{{appName}}", "1.0.0");
                    
                    app.get("/", (req, res) -> {
                        res.html("<h1>{{welcomeMessage}}</h1>");
                    });
                    
                    app.run(8080);
                }
            }
            """
        ));
        
        templateRegistry.register(new DefaultPromptTemplate(
            "rest-api-crud",
            "api",
            "生成REST API CRUD操作",
            """
            创建一个REST API，包含完整的CRUD操作：
            - GET /api/resources - 获取所有
            - GET /api/resources/:id - 获取单个
            - POST /api/resources - 创建
            - PUT /api/resources/:id - 更新
            - DELETE /api/resources/:id - 删除
            
            使用ConcurrentHashMap作为数据存储。
            """,
            Map.of("resourceName", "", "packageName", "")
        ));
        
        templateRegistry.register(new DefaultPromptTemplate(
            "dependency-injection",
            "core",
            "使用依赖注入容器",
            """
            使用EST的依赖注入容器：
            1. 创建Container实例
            2. 注册服务接口和实现
            3. 获取服务并使用
            
            代码结构：
            Container container = new DefaultContainer();
            container.register(MyService.class, MyServiceImpl.class);
            MyService service = container.get(MyService.class);
            """
        ));
        
        templateRegistry.register(new DefaultPromptTemplate(
            "config-management",
            "core",
            "配置管理",
            """
            使用EST的配置管理功能：
            - 设置配置值
            - 获取配置值
            - 使用默认值
            
            代码示例：
            Config config = app.getConfig();
            config.set("app.name", "MyApp");
            String appName = config.getString("app.name", "DefaultApp");
            """
        ));
    }
    
    @Override
    public PromptTemplateRegistry getTemplateRegistry() {
        return templateRegistry;
    }
    
    @Override
    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }
    
    @Override
    public ProjectScaffold getProjectScaffold() {
        return projectScaffold;
    }
    
    @Override
    public LlmClient getLlmClient() {
        return llmClient;
    }
    
    @Override
    public void setLlmClient(LlmClient llmClient) {
        this.llmClient = llmClient;
    }
    
    @Override
    public RequirementParser getRequirementParser() {
        return requirementParser;
    }
    
    @Override
    public ArchitectureDesigner getArchitectureDesigner() {
        return architectureDesigner;
    }
    
    @Override
    public TestAndDeployManager getTestAndDeployManager() {
        return testAndDeployManager;
    }
    
    @Override
    public String getQuickReference(String topic) {
        return switch (topic.toLowerCase()) {
            case "web" -> """
                Web快速参考：
                - Web.create(name, version) - 创建应用
                - app.get(path, handler) - GET路由
                - app.post(path, handler) - POST路由
                - res.send(text) - 发送文本
                - res.json(data) - 发送JSON
                - res.html(html) - 发送HTML
                - app.run(port) - 启动服务器
                """;
            case "config" -> """
                配置快速参考：
                - config.set(key, value) - 设置配置
                - config.getString(key) - 获取字符串
                - config.getInt(key, defaultValue) - 获取整数
                - config.getBoolean(key) - 获取布尔值
                """;
            default -> "未知主题: " + topic;
        };
    }
    
    @Override
    public String getBestPractice(String category) {
        return switch (category.toLowerCase()) {
            case "error-handling" -> """
                错误处理最佳实践：
                1. 使用try-catch包装业务逻辑
                2. 使用res.sendError(code, message)返回错误
                3. 400 - 请求参数错误
                4. 404 - 资源不存在
                5. 500 - 服务器内部错误
                """;
            case "routing" -> """
                路由最佳实践：
                1. 使用RESTful风格的URL
                2. 对相关路由使用group
                3. 使用路径参数如 /user/:id
                4. 启用CORS支持跨域
                """;
            default -> "未知分类: " + category;
        };
    }
    
    @Override
    public String getTutorial(String topic) {
        return switch (topic.toLowerCase()) {
            case "first-app" -> """
                第一个EST应用教程：
                1. 添加est-web-impl依赖
                2. 创建主类
                3. 使用Web.create()创建应用
                4. 添加路由
                5. 调用app.run()启动
                
                详细教程请查看 docs/tutorials/beginner/01-first-app.md
                """;
            default -> "未知教程: " + topic;
        };
    }
    
    @Override
    public String generatePrompt(String templateName, Map<String, String> variables) {
        return templateRegistry.getTemplate(templateName)
                .map(template -> template.generate(variables))
                .orElse("Template not found: " + templateName);
    }
    
    @Override
    public String suggestCode(String requirement) {
        if (llmClient != null && llmClient.isAvailable()) {
            String prompt = "你是一个Java开发专家，使用EST框架。请根据以下需求生成Java代码：\n\n" + requirement +
                           "\n\n请只返回代码，不要其他解释。";
            return llmClient.generate(prompt);
        }
        if (requirement.toLowerCase().contains("web") || requirement.toLowerCase().contains("http")) {
            return "建议使用EST Web模块，查看 getQuickReference(\"web\") 获取更多信息";
        } else if (requirement.toLowerCase().contains("database") || requirement.toLowerCase().contains("data")) {
            return "建议使用EST Data模块，支持JDBC、MongoDB、Redis等";
        } else if (requirement.toLowerCase().contains("security") || requirement.toLowerCase().contains("auth")) {
            return "建议使用EST Security模块，支持JWT、Basic Auth、OAuth2等";
        }
        return "请提供更具体的需求，我可以给出更准确的建议";
    }
    
    @Override
    public String explainCode(String code) {
        if (llmClient != null && llmClient.isAvailable()) {
            String prompt = "请详细解释以下Java代码的功能和实现逻辑：\n\n" + code;
            return llmClient.generate(prompt);
        }
        if (code.contains("Web.create")) {
            return "这是创建EST Web应用的标准方式，参数是应用名称和版本";
        } else if (code.contains("app.get")) {
            return "这是添加GET路由的方法，第一个参数是路径，第二个是处理函数";
        } else if (code.contains("res.json")) {
            return "这是发送JSON响应的方法，会自动设置Content-Type为application/json";
        }
        return "代码解释功能正在完善中";
    }
    
    @Override
    public String optimizeCode(String code) {
        if (llmClient != null && llmClient.isAvailable()) {
            String prompt = "请优化以下Java代码，保持功能不变，但提高代码质量和性能：\n\n" + code;
            return llmClient.generate(prompt);
        }
        return "代码优化建议：\n" +
               "1. 检查是否有未使用的导入\n" +
               "2. 考虑使用app.onStartup()添加启动日志\n" +
               "3. 考虑添加错误处理\n" +
               "4. 遵循EST的命名约定";
    }
    
    @Override
    public String chat(String message) {
        if (llmClient == null || !llmClient.isAvailable()) {
            return "LLM client not available. Please configure API key first.";
        }
        conversationHistory.add(new LlmMessage("user", message));
        LlmResponse response = llmClient.chat(conversationHistory);
        if (response.isSuccess()) {
            conversationHistory.add(new LlmMessage("assistant", response.getContent()));
            return response.getContent();
        }
        return "Error: " + response.getErrorMessage();
    }
    
    @Override
    public String chat(List<LlmMessage> messages) {
        if (llmClient == null || !llmClient.isAvailable()) {
            return "LLM client not available. Please configure API key first.";
        }
        LlmResponse response = llmClient.chat(messages);
        if (response.isSuccess()) {
            return response.getContent();
        }
        return "Error: " + response.getErrorMessage();
    }
    
    public void clearConversationHistory() {
        conversationHistory.clear();
    }
}
