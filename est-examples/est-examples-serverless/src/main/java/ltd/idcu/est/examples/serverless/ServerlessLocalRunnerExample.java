package ltd.idcu.est.examples.serverless;

import ltd.idcu.est.serverless.api.ServerlessLocalRunner;

import java.util.HashMap;
import java.util.Map;

public class ServerlessLocalRunnerExample {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("EST Serverless Local Runner Example");
        System.out.println("========================================\n");

        ServerlessLocalRunner runner = new ServerlessLocalRunner();

        System.out.println("--- Step 1: Register Functions ---");
        
        HelloWorldFunction helloFunction = new HelloWorldFunction();
        CalculatorFunction calculatorFunction = new CalculatorFunction();
        OptimizedFunction optimizedFunction = new OptimizedFunction();

        Map<String, Object> helloConfig = new HashMap<>();
        helloConfig.put("greeting", "Hello from local runner!");
        runner.registerFunction("hello", helloFunction, helloConfig);
        System.out.println("[OK] Registered 'hello' function");

        runner.registerFunction("calculator", calculatorFunction);
        System.out.println("[OK] Registered 'calculator' function");

        Map<String, Object> optimizedConfig = new HashMap<>();
        optimizedConfig.put("cacheSize", 100);
        runner.registerFunction("optimized", optimizedFunction, optimizedConfig);
        System.out.println("[OK] Registered 'optimized' function\n");

        System.out.println("--- Step 2: Test Function Invocation ---");

        System.out.println("\n2.1 Test HelloWorld Function:");
        testHelloFunction(runner);

        System.out.println("\n2.2 Test Calculator Function:");
        testCalculatorFunction(runner);

        System.out.println("\n2.3 Test Optimized Function:");
        testOptimizedFunction(runner);

        System.out.println("\n--- Step 3: Interactive Mode ---");
        System.out.println("Starting interactive mode, you can manually invoke functions...");
        System.out.println("Type 'help' to see available commands\n");

        runner.startInteractiveMode();

        System.out.println("\n--- Step 4: Cleanup Resources ---");
        runner.shutdown();
        System.out.println("[OK] All resources cleaned up");

        System.out.println("\n========================================");
        System.out.println("Example execution completed!");
        System.out.println("========================================");
    }

    private static void testHelloFunction(ServerlessLocalRunner runner) {
        try {
            ltd.idcu.est.serverless.api.ServerlessRequest request = 
                new ltd.idcu.est.serverless.api.ServerlessRequest(
                    "GET", "/hello", 
                    Map.of("name", "Local User"), 
                    Map.of("Content-Type", "application/json"),
                    ""
                );
            
            ltd.idcu.est.serverless.api.ServerlessResponse response = 
                runner.invoke("hello", request);
            
            System.out.println("  Request: GET /hello?name=Local User");
            System.out.println("  Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }
    }

    private static void testCalculatorFunction(ServerlessLocalRunner runner) {
        try {
            ltd.idcu.est.serverless.api.ServerlessRequest addRequest = 
                new ltd.idcu.est.serverless.api.ServerlessRequest(
                    "GET", "/calculator", 
                    Map.of("op", "add", "a", "15", "b", "25"), 
                    Map.of("Content-Type", "application/json"),
                    ""
                );
            
            ltd.idcu.est.serverless.api.ServerlessResponse addResponse = 
                runner.invoke("calculator", addRequest);
            
            System.out.println("  Request: GET /calculator?op=add&a=15&b=25");
            System.out.println("  Response: " + addResponse.getBody());

            ltd.idcu.est.serverless.api.ServerlessRequest mulRequest = 
                new ltd.idcu.est.serverless.api.ServerlessRequest(
                    "GET", "/calculator", 
                    Map.of("op", "mul", "a", "7", "b", "8"), 
                    Map.of("Content-Type", "application/json"),
                    ""
                );
            
            ltd.idcu.est.serverless.api.ServerlessResponse mulResponse = 
                runner.invoke("calculator", mulRequest);
            
            System.out.println("  Request: GET /calculator?op=mul&a=7&b=8");
            System.out.println("  Response: " + mulResponse.getBody());
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }
    }

    private static void testOptimizedFunction(ServerlessLocalRunner runner) {
        try {
            ltd.idcu.est.serverless.api.ServerlessRequest statusRequest = 
                new ltd.idcu.est.serverless.api.ServerlessRequest(
                    "GET", "/optimized", 
                    Map.of("action", "status"), 
                    Map.of("Content-Type", "application/json"),
                    ""
                );
            
            ltd.idcu.est.serverless.api.ServerlessResponse statusResponse = 
                runner.invoke("optimized", statusRequest);
            
            System.out.println("  Request: GET /optimized?action=status");
            System.out.println("  Response: " + statusResponse.getBody());

            ltd.idcu.est.serverless.api.ServerlessRequest statsRequest = 
                new ltd.idcu.est.serverless.api.ServerlessRequest(
                    "GET", "/optimized", 
                    Map.of("action", "stats"), 
                    Map.of("Content-Type", "application/json"),
                    ""
                );
            
            ltd.idcu.est.serverless.api.ServerlessResponse statsResponse = 
                runner.invoke("optimized", statsRequest);
            
            System.out.println("  Request: GET /optimized?action=stats");
            System.out.println("  Response: " + statsResponse.getBody());
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }
    }
}
