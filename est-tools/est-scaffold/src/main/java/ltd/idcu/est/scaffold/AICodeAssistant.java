package ltd.idcu.est.scaffold;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class AICodeAssistant {
    
    private static final Map<String, String> PATTERNS = new HashMap<>();
    
    static {
        PATTERNS.put("web-app", "创建一个EST Web应用");
        PATTERNS.put("rest-api", "创建REST API服务");
        PATTERNS.put("crud", "创建CRUD操作");
        PATTERNS.put("cache", "添加缓存功能");
        PATTERNS.put("event", "添加事件驱动");
        PATTERNS.put("logging", "添加日志功能");
        PATTERNS.put("scheduler", "添加任务调度");
        PATTERNS.put("validation", "添加数据验证");
        PATTERNS.put("microservice", "创建微服务");
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String command = args[0];
        
        switch (command) {
            case "list-patterns":
                listPatterns();
                break;
            case "generate-prompt":
                if (args.length < 2) {
                    System.err.println("Usage: java AICodeAssistant generate-prompt <pattern>");
                    return;
                }
                generatePrompt(args[1]);
                break;
            case "analyze-code":
                if (args.length < 2) {
                    System.err.println("Usage: java AICodeAssistant analyze-code <file>");
                    return;
                }
                analyzeCode(args[1]);
                break;
            case "suggest-refactor":
                if (args.length < 2) {
                    System.err.println("Usage: java AICodeAssistant suggest-refactor <file>");
                    return;
                }
                suggestRefactor(args[1]);
                break;
            default:
                System.err.println("Unknown command: " + command);
                printUsage();
        }
    }
    
    private static void printUsage() {
        System.out.println("EST AI Code Assistant");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java AICodeAssistant list-patterns");
        System.out.println("  java AICodeAssistant generate-prompt <pattern>");
        System.out.println("  java AICodeAssistant analyze-code <file>");
        System.out.println("  java AICodeAssistant suggest-refactor <file>");
        System.out.println();
        System.out.println("Available patterns:");
        for (String pattern : PATTERNS.keySet()) {
            System.out.println("  " + pattern + " - " + PATTERNS.get(pattern));
        }
    }
    
    private static void listPatterns() {
        System.out.println(ConsoleColors.bold(ConsoleColors.blue("Available AI Code Patterns:")));
        System.out.println();
        for (Map.Entry<String, String> entry : PATTERNS.entrySet()) {
            System.out.println(ConsoleColors.cyan("  " + entry.getKey()));
            System.out.println("    " + entry.getValue());
            System.out.println();
        }
    }
    
    private static void generatePrompt(String pattern) {
        String prompt = getPromptForPattern(pattern);
        if (prompt == null) {
            System.err.println(ConsoleColors.red("Unknown pattern: " + pattern));
            System.out.println("Available patterns: " + String.join(", ", PATTERNS.keySet()));
            return;
        }
        
        System.out.println(ConsoleColors.bold(ConsoleColors.blue("Generated AI Prompt:")));
        System.out.println();
        System.out.println(prompt);
    }
    
    private static String getPromptForPattern(String pattern) {
        switch (pattern) {
            case "web-app":
                return generateWebAppPrompt();
            case "rest-api":
                return generateRestApiPrompt();
            case "crud":
                return generateCrudPrompt();
            case "cache":
                return generateCachePrompt();
            case "event":
                return generateEventPrompt();
            case "logging":
                return generateLoggingPrompt();
            case "scheduler":
                return generateSchedulerPrompt();
            case "validation":
                return generateValidationPrompt();
            case "microservice":
                return generateMicroservicePrompt();
            default:
                return null;
        }
    }
    
    private static String generateWebAppPrompt() {
        return """
使用EST框架创建一个Java Web应用。

需求：
1. 项目名称：my-web-app
2. 功能模块：
   - 首页（HTML）
   - 用户管理API
   - 静态文件服务
3. 技术要求：
   - 使用EST Web模块
   - 添加日志中间件
   - 启用CORS
   - 端口：8080

参考文档：
- EST AI_CODER_GUIDE.md
- EST PRACTICAL_EXAMPLES.md
- EST QUICK_REFERENCE.md

请按照EST框架的最佳实践编写代码。
""";
    }
    
    private static String generateRestApiPrompt() {
        return """
使用EST框架创建一个完整的REST API服务。

API设计：
- GET    /api/resources      - 获取列表
- GET    /api/resources/:id  - 获取单个
- POST   /api/resources      - 创建
- PUT    /api/resources/:id  - 更新
- DELETE /api/resources/:id  - 删除

数据模型：
- id (String)
- name (String)
- description (String)
- createdAt (long)
- updatedAt (long)

技术要求：
- 使用EST Web模块
- 内存存储（ConcurrentHashMap）
- 数据验证
- 统一错误响应
- JSON格式

请参考EST框架的REST API模式实现。
""";
    }
    
    private static String generateCrudPrompt() {
        return """
为EST项目创建完整的CRUD功能。

功能要求：
1. Controller层 - REST API端点
2. Service层 - 业务逻辑
3. Repository层 - 数据访问
4. Model层 - 数据模型
5. DTO层 - 数据传输对象
6. Validator - 数据验证

技术栈：
- EST Web
- EST依赖注入容器
- 内存存储

请按照EST的分层架构设计，提供完整的CRUD实现。
""";
    }
    
    private static String generateCachePrompt() {
        return """
为EST项目添加缓存功能。

需求：
1. 创建内存缓存管理器
2. 支持TTL（过期时间）
3. 支持最大容量限制
4. 缓存命中率统计
5. LRU淘汰策略
6. 线程安全

缓存功能：
- put(key, value, ttl)
- get(key)
- remove(key)
- clear()
- getStats()

请使用Java标准库实现，遵循EST框架的零依赖原则。
""";
    }
    
    private static String generateEventPrompt() {
        return """
为EST项目添加事件驱动架构。

需求：
1. 创建事件总线
2. 支持事件订阅/发布
3. 支持同步/异步事件
4. 支持事件优先级
5. 支持事件类型过滤
6. 线程安全

事件类型示例：
- UserCreatedEvent
- UserUpdatedEvent
- UserDeletedEvent
- OrderCreatedEvent

请提供完整的事件总线实现。
""";
    }
    
    private static String generateLoggingPrompt() {
        return """
为EST项目添加日志功能。

需求：
1. 创建日志管理器
2. 支持日志级别（DEBUG, INFO, WARN, ERROR）
3. 支持控制台输出
4. 支持文件输出
5. 支持日志格式化
6. 支持多个日志处理器

日志功能：
- logger.debug(message)
- logger.info(message)
- logger.warn(message)
- logger.error(message, throwable)

请提供完整的日志系统实现。
""";
    }
    
    private static String generateSchedulerPrompt() {
        return """
为EST项目添加任务调度功能。

需求：
1. 创建任务调度器
2. 支持固定频率调度
3. 支持固定延迟调度
4. 支持一次性任务
5. 支持Cron表达式
6. 支持任务暂停/恢复/取消
7. 任务执行统计

调度模式：
- scheduleAtFixedRate(task, delay, period)
- scheduleWithFixedDelay(task, delay, period)
- scheduleOnce(task, delay)
- scheduleCron(task, cronExpression)

请提供完整的任务调度器实现。
""";
    }
    
    private static String generateValidationPrompt() {
        return """
为EST项目添加数据验证功能。

需求：
1. 创建验证器工具类
2. 支持以下验证：
   - 必填字段
   - 字符串长度
   - 邮箱格式
   - 手机号格式
   - 数字范围
   - 正数验证
3. 链式API
4. 错误信息收集
5. 验证异常

验证示例：
validator.validateRequired(name, "name")
         .validateLength(name, "name", 3, 50)
         .validateEmail(email, "email")
         .throwIfInvalid();

请提供完整的数据验证器实现。
""";
    }
    
    private static String generateMicroservicePrompt() {
        return """
使用EST框架创建一个微服务项目。

微服务架构：
1. 服务列表：
   - 用户服务 (user-service)
   - 订单服务 (order-service)
   - 商品服务 (product-service)
   - 网关服务 (gateway-service)

2. 技术要求：
   - 每个服务使用EST Web
   - 服务间REST通信
   - EST配置管理
   - EST日志记录
   - EST监控

3. 数据管理：
   - 每个服务独立数据库
   - 事件驱动数据同步
   - 最终一致性

请参考EST的MICROSERVICES_ARCHITECTURE.md，提供完整的微服务实现。
""";
    }
    
    private static void analyzeCode(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.err.println(ConsoleColors.red("File not found: " + filePath));
                return;
            }
            
            String code = Files.readString(path);
            CodeAnalysis analysis = analyzeCodeContent(code);
            
            System.out.println(ConsoleColors.bold(ConsoleColors.blue("Code Analysis Results:")));
            System.out.println();
            System.out.println("File: " + filePath);
            System.out.println();
            
            System.out.println(ConsoleColors.yellow("Structure:"));
            System.out.println("  Lines: " + analysis.lineCount);
            System.out.println("  Classes: " + analysis.classCount);
            System.out.println("  Methods: " + analysis.methodCount);
            System.out.println();
            
            System.out.println(ConsoleColors.yellow("EST Framework Usage:"));
            if (analysis.usesEstWeb) {
                System.out.println(ConsoleColors.green("  ✓ EST Web module"));
            }
            if (analysis.usesEstCore) {
                System.out.println(ConsoleColors.green("  ✓ EST Core module"));
            }
            if (analysis.usesEstFeatures) {
                System.out.println(ConsoleColors.green("  ✓ EST Features module"));
            }
            System.out.println();
            
            System.out.println(ConsoleColors.yellow("Suggestions:"));
            for (String suggestion : analysis.suggestions) {
                System.out.println("  • " + suggestion);
            }
            
        } catch (IOException e) {
            System.err.println(ConsoleColors.red("Error reading file: " + e.getMessage()));
        }
    }
    
    private static CodeAnalysis analyzeCodeContent(String code) {
        CodeAnalysis analysis = new CodeAnalysis();
        analysis.lineCount = code.split("\n").length;
        analysis.classCount = countOccurrences(code, "class ");
        analysis.methodCount = countOccurrences(code, "public ") + countOccurrences(code, "private ") + countOccurrences(code, "protected ");
        analysis.usesEstWeb = code.contains("ltd.idcu.est.web");
        analysis.usesEstCore = code.contains("ltd.idcu.est.core");
        analysis.usesEstFeatures = code.contains("ltd.idcu.est.features");
        
        if (!code.contains("/**")) {
            analysis.suggestions.add("Add Javadoc comments for public APIs");
        }
        if (analysis.lineCount > 300) {
            analysis.suggestions.add("Consider splitting into smaller classes");
        }
        if (!code.contains("try") && !code.contains("catch")) {
            analysis.suggestions.add("Consider adding error handling");
        }
        
        return analysis;
    }
    
    private static int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }
    
    private static void suggestRefactor(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.err.println(ConsoleColors.red("File not found: " + filePath));
                return;
            }
            
            String code = Files.readString(path);
            
            System.out.println(ConsoleColors.bold(ConsoleColors.blue("Refactoring Suggestions:")));
            System.out.println();
            System.out.println("File: " + filePath);
            System.out.println();
            
            List<String> suggestions = generateRefactorSuggestions(code);
            
            if (suggestions.isEmpty()) {
                System.out.println(ConsoleColors.green("No refactoring suggestions at this time."));
            } else {
                for (String suggestion : suggestions) {
                    System.out.println(ConsoleColors.yellow("  • " + suggestion));
                }
            }
            
        } catch (IOException e) {
            System.err.println(ConsoleColors.red("Error reading file: " + e.getMessage()));
        }
    }
    
    private static List<String> generateRefactorSuggestions(String code) {
        List<String> suggestions = new ArrayList<>();
        
        if (code.contains("new HashMap") && code.contains("ConcurrentHashMap")) {
            suggestions.add("Consider using ConcurrentHashMap for thread safety");
        }
        
        if (code.contains("System.out.println") && !code.contains("Logger")) {
            suggestions.add("Replace System.out.println with a proper logging framework");
        }
        
        if (code.contains("Thread.sleep")) {
            suggestions.add("Consider using ScheduledExecutorService instead of Thread.sleep");
        }
        
        if (!code.contains("Optional") && code.contains("null")) {
            suggestions.add("Consider using Optional to handle null values");
        }
        
        if (code.contains("for (") && code.contains(".get(")) {
            suggestions.add("Consider using enhanced for-loops or streams");
        }
        
        return suggestions;
    }
    
    private static class CodeAnalysis {
        int lineCount;
        int classCount;
        int methodCount;
        boolean usesEstWeb;
        boolean usesEstCore;
        boolean usesEstFeatures;
        List<String> suggestions = new ArrayList<>();
    }
}
