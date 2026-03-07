package ltd.idcu.est.scaffold;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class AICodeAssistant {
    
    private static final Map<String, String> PATTERNS = new HashMap<>();
    
    static {
        PATTERNS.put("web-app", "Create an EST Web Application");
        PATTERNS.put("rest-api", "Create REST API Service");
        PATTERNS.put("crud", "Create CRUD Operations");
        PATTERNS.put("cache", "Add Caching Feature");
        PATTERNS.put("event", "Add Event-driven Architecture");
        PATTERNS.put("logging", "Add Logging Feature");
        PATTERNS.put("scheduler", "Add Task Scheduling");
        PATTERNS.put("validation", "Add Data Validation");
        PATTERNS.put("microservice", "Create Microservice");
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
Create a Java Web Application using the EST Framework.
Requirements:
1. Project Name: my-web-app
2. Features:
   - Home page (HTML)
   - User management API
   - Static file service
3. Technical Requirements:
   - Use EST Web module
   - Add logging middleware
   - Enable CORS
   - Port: 8080

Reference Documentation:
- EST AI_CODER_GUIDE.md
- EST PRACTICAL_EXAMPLES.md
- EST QUICK_REFERENCE.md

Please write code following the best practices of the EST Framework.""";
    }
    
    private static String generateRestApiPrompt() {
        return """
Create a complete REST API service using the EST Framework.
API Design:
- GET    /api/resources      - Get list
- GET    /api/resources/:id  - Get single
- POST   /api/resources      - Create
- PUT    /api/resources/:id  - Update
- DELETE /api/resources/:id  - Delete

Data Model:
- id (String)
- name (String)
- description (String)
- createdAt (long)
- updatedAt (long)

Technical Requirements:
- Use EST Web module
- In-memory storage (ConcurrentHashMap)
- Data validation
- Unified error response
- JSON format

Please implement following REST API patterns of the EST Framework.""";
    }
    
    private static String generateCrudPrompt() {
        return """
Create complete CRUD functionality for an EST project.
Feature Requirements:
1. Controller - REST API endpoints
2. Service - Business logic
3. Repository - Data access
4. Model - Data model
5. DTO - Data transfer object
6. Validator - Data validation

Technology Stack:
- EST Web
- EST Dependency Injection Container
- In-memory storage

Please design according to the layered architecture of EST, providing complete CRUD implementation.""";
    }
    
    private static String generateCachePrompt() {
        return """
Add caching functionality to an EST project.
Requirements:
1. Create an in-memory cache manager
2. Support TTL (time-to-live)
3. Support maximum capacity limit
4. Cache hit rate statistics
5. LRU eviction policy
6. Thread-safe

Cache Functions:
- put(key, value, ttl)
- get(key)
- remove(key)
- clear()
- getStats()

Please implement using Java standard library, following the zero-dependency principle of the EST Framework.""";
    }
    
    private static String generateEventPrompt() {
        return """
Add event-driven architecture to an EST project.
Requirements:
1. Create an event bus
2. Support event subscription/publishing
3. Support synchronous/asynchronous events
4. Support event priority
5. Support event type filtering
6. Thread-safe

Event Type Examples:
- UserCreatedEvent
- UserUpdatedEvent
- UserDeletedEvent
- OrderCreatedEvent

Please provide complete event bus implementation.""";
    }
    
    private static String generateLoggingPrompt() {
        return """
Add logging functionality to an EST project.
Requirements:
1. Create a log manager
2. Support log levels (DEBUG, INFO, WARN, ERROR)
3. Support console output
4. Support file output
5. Support log formatting
6. Support multiple log handlers

Log Functions:
- logger.debug(message)
- logger.info(message)
- logger.warn(message)
- logger.error(message, throwable)

Please provide complete logging system implementation.""";
    }
    
    private static String generateSchedulerPrompt() {
        return """
Add task scheduling functionality to an EST project.
Requirements:
1. Create a task scheduler
2. Support fixed-rate scheduling
3. Support fixed-delay scheduling
4. Support one-time tasks
5. Support Cron expressions
6. Support task pause/resume/cancel
7. Task execution statistics

Scheduling Modes:
- scheduleAtFixedRate(task, delay, period)
- scheduleWithFixedDelay(task, delay, period)
- scheduleOnce(task, delay)
- scheduleCron(task, cronExpression)

Please provide complete task scheduler implementation.""";
    }
    
    private static String generateValidationPrompt() {
        return """
Add data validation functionality to an EST project.
Requirements:
1. Create a validator utility class
2. Support the following validations:
   - Required fields
   - String length
   - Email format
   - Phone number format
   - Number range
   - Positive number validation
3. Chained API
4. Error message collection
5. Validation exception

Validation Example:
validator.validateRequired(name, "name")
         .validateLength(name, "name", 3, 50)
         .validateEmail(email, "email")
         .throwIfInvalid();

Please provide complete data validator implementation.""";
    }
    
    private static String generateMicroservicePrompt() {
        return """
Create a microservice project using the EST Framework.
Microservice Architecture:
1. Service List:
   - User service (user-service)
   - Order service (order-service)
   - Product service (product-service)
   - Gateway service (gateway-service)

2. Technical Requirements:
   - Each service uses EST Web
   - Inter-service REST communication
   - EST configuration management
   - EST logging
   - EST monitoring

3. Data Management:
   - Each service with independent database
   - Event-driven data synchronization
   - Eventual consistency

Please refer to EST's MICROSERVICES_ARCHITECTURE.md, providing complete microservice implementation.""";
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
