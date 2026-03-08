package ltd.idcu.est.scaffold;

import java.io.IOException;
import java.util.*;

public class CodeSnippetGenerator {
    
    private static final Map<String, CodeTemplate> TEMPLATES = new HashMap<>();
    
    static {
        TEMPLATES.put("controller", new CodeTemplate(
            "REST Controller",
            "创建标准的REST API Controller",
            "snippets/controller.java.template",
            Arrays.asList("package", "className", "model", "resource")
        ));
        
        TEMPLATES.put("model", new CodeTemplate(
            "数据模型",
            "创建标准的POJO数据模型�?,
            "snippets/model.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("service", new CodeTemplate(
            "Service�?,
            "创建业务逻辑Service�?,
            "snippets/service.java.template",
            Arrays.asList("package", "className", "model")
        ));
        
        TEMPLATES.put("repository", new CodeTemplate(
            "Repository�?,
            "创建数据访问Repository�?,
            "snippets/repository.java.template",
            Arrays.asList("package", "className", "model")
        ));
        
        TEMPLATES.put("dto", new CodeTemplate(
            "数据传输对象",
            "创建DTO数据传输对象",
            "snippets/dto.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("validator", new CodeTemplate(
            "数据验证�?,
            "创建数据验证器工具类",
            "snippets/validator.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("cache", new CodeTemplate(
            "缓存组件",
            "创建缓存管理器组�?,
            "snippets/cache.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("event", new CodeTemplate(
            "事件总线",
            "创建事件总线组件",
            "snippets/event.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("logger", new CodeTemplate(
            "日志组件",
            "创建日志管理器组�?,
            "snippets/logger.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("scheduler", new CodeTemplate(
            "调度�?,
            "创建任务调度器组�?,
            "snippets/scheduler.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("config", new CodeTemplate(
            "配置管理�?,
            "创建配置管理器组�?,
            "snippets/config.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("middleware", new CodeTemplate(
            "中间�?,
            "创建自定义中间件",
            "snippets/middleware.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("singleton", new CodeTemplate(
            "Singleton组件",
            "创建单例组件",
            "snippets/singleton.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("exception-handler", new CodeTemplate(
            "全局异常处理�?,
            "创建全局异常处理",
            "snippets/exception-handler.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("dependency-injection", new CodeTemplate(
            "依赖注入",
            "使用EST依赖注入容器",
            "snippets/dependency-injection.java.template",
            Arrays.asList("package", "className")
        ));
        
        TEMPLATES.put("event-listener", new CodeTemplate(
            "事件监听�?,
            "创建事件监听器组�?,
            "snippets/event-listener.java.template",
            Arrays.asList("package", "className")
        ));
    }
    
    public static List<String> listTemplates() {
        return new ArrayList<>(TEMPLATES.keySet());
    }
    
    public static CodeTemplate getTemplate(String name) {
        return TEMPLATES.get(name);
    }
    
    public static String generate(String templateName, Map<String, String> variables) throws IOException {
        CodeTemplate template = TEMPLATES.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }
        
        TemplateEngine engine = new TemplateEngine();
        variables.forEach(engine::setVariable);
        
        return engine.renderFromFile("templates/" + template.templatePath);
    }
    
    public static void listAllTemplates() {
        System.out.println(ConsoleColors.bold(ConsoleColors.blue("Available Code Snippets:")));
        System.out.println();
        for (Map.Entry<String, CodeTemplate> entry : TEMPLATES.entrySet()) {
            System.out.println(ConsoleColors.cyan("  " + entry.getKey()));
            System.out.println("    " + entry.getValue().description);
            System.out.println("    Required variables: " + String.join(", ", entry.getValue().variables));
            System.out.println();
        }
    }
    
    public static class CodeTemplate {
        public String name;
        public String description;
        public String templatePath;
        public List<String> variables;
        
        public CodeTemplate(String name, String description, String templatePath, List<String> variables) {
            this.name = name;
            this.description = description;
            this.templatePath = templatePath;
            this.variables = variables;
        }
    }
}
