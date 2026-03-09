package ltd.idcu.est.serverless.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerlessLocalRunner {
    
    private final Map<String, ServerlessFunction<?, ?>> functions = new HashMap<>();
    private final Map<String, Object> globalContext = new HashMap<>();
    
    public ServerlessLocalRunner() {
    }
    
    public <I, O> void registerFunction(String name, ServerlessFunction<I, O> function) {
        registerFunction(name, function, new HashMap<>());
    }
    
    public <I, O> void registerFunction(String name, ServerlessFunction<I, O> function, Map<String, Object> config) {
        function.initialize(config);
        functions.put(name, function);
    }
    
    @SuppressWarnings("unchecked")
    public <I, O> O invoke(String functionName, I input) {
        return invoke(functionName, input, new HashMap<>());
    }
    
    @SuppressWarnings("unchecked")
    public <I, O> O invoke(String functionName, I input, Map<String, Object> requestContext) {
        ServerlessFunction<I, O> function = (ServerlessFunction<I, O>) functions.get(functionName);
        if (function == null) {
            throw new IllegalArgumentException("Function not found: " + functionName);
        }
        
        Map<String, Object> context = new HashMap<>(globalContext);
        context.putAll(requestContext);
        context.put("functionName", functionName);
        context.put("localExecution", true);
        context.put("executionTime", System.currentTimeMillis());
        
        return function.handle(input, context);
    }
    
    public void setGlobalContext(String key, Object value) {
        globalContext.put(key, value);
    }
    
    public void startInteractiveMode() {
        System.out.println("=== EST Serverless Local Runner ===");
        System.out.println("Registered functions: " + functions.keySet());
        System.out.println("Type 'help' for commands, 'exit' to quit\n");
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            
            if (line.isEmpty()) continue;
            
            String[] parts = line.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            
            switch (command) {
                case "exit":
                case "quit":
                    System.out.println("Goodbye!");
                    return;
                case "help":
                    printHelp();
                    break;
                case "list":
                    System.out.println("Functions: " + functions.keySet());
                    break;
                case "invoke":
                    if (parts.length < 2) {
                        System.out.println("Usage: invoke <functionName> [input]");
                    } else {
                        handleInvoke(parts[1]);
                    }
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    System.out.println("Type 'help' for available commands");
            }
        }
    }
    
    private void handleInvoke(String args) {
        String[] invokeParts = args.split("\\s+", 2);
        String functionName = invokeParts[0];
        String input = invokeParts.length > 1 ? invokeParts[1] : "";
        
        try {
            Object result = invoke(functionName, input);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  list              - List all registered functions");
        System.out.println("  invoke <name> [input] - Invoke a function");
        System.out.println("  help              - Show this help");
        System.out.println("  exit/quit         - Exit the runner");
    }
    
    public void shutdown() {
        functions.values().forEach(ServerlessFunction::destroy);
    }
}
