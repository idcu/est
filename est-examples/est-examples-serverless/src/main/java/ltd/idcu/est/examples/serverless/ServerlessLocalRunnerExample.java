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

        System.out.println("--- 步骤 1: 注册函数 ---");
        
        HelloWorldFunction helloFunction = new HelloWorldFunction();
        CalculatorFunction calculatorFunction = new CalculatorFunction();
        OptimizedFunction optimizedFunction = new OptimizedFunction();

        Map<String, Object> helloConfig = new HashMap<>();
        helloConfig.put("greeting", "Hello from local runner!");
        runner.registerFunction("hello", helloFunction, helloConfig);
        System.out.println("✓ 注册了 'hello' 函数");

        runner.registerFunction("calculator", calculatorFunction);
        System.out.println("✓ 注册了 'calculator' 函数");

        Map<String, Object> optimizedConfig = new HashMap<>();
        optimizedConfig.put("cacheSize", 100);
        runner.registerFunction("optimized", optimizedFunction, optimizedConfig);
        System.out.println("✓ 注册了 'optimized' 函数\n");

        System.out.println("--- 步骤 2: 测试调用函数 ---");

        System.out.println("\n2.1 测试 HelloWorld 函数:");
        testHelloFunction(runner);

        System.out.println("\n2.2 测试 Calculator 函数:");
        testCalculatorFunction(runner);

        System.out.println("\n2.3 测试 Optimized 函数:");
        testOptimizedFunction(runner);

        System.out.println("\n--- 步骤 3: 交互模式 ---");
        System.out.println("现在启动交互模式，你可以手动调用函数...");
        System.out.println("输入 'help' 查看可用命令\n");

        runner.startInteractiveMode();

        System.out.println("\n--- 步骤 4: 清理资源 ---");
        runner.shutdown();
        System.out.println("✓ 所有资源已清理");

        System.out.println("\n========================================");
        System.out.println("示例执行完成!");
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
            
            System.out.println("  请求: GET /hello?name=Local User");
            System.out.println("  响应: " + response.getBody());
        } catch (Exception e) {
            System.err.println("  错误: " + e.getMessage());
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
            
            System.out.println("  请求: GET /calculator?op=add&a=15&b=25");
            System.out.println("  响应: " + addResponse.getBody());

            ltd.idcu.est.serverless.api.ServerlessRequest mulRequest = 
                new ltd.idcu.est.serverless.api.ServerlessRequest(
                    "GET", "/calculator", 
                    Map.of("op", "mul", "a", "7", "b", "8"), 
                    Map.of("Content-Type", "application/json"),
                    ""
                );
            
            ltd.idcu.est.serverless.api.ServerlessResponse mulResponse = 
                runner.invoke("calculator", mulRequest);
            
            System.out.println("  请求: GET /calculator?op=mul&a=7&b=8");
            System.out.println("  响应: " + mulResponse.getBody());
        } catch (Exception e) {
            System.err.println("  错误: " + e.getMessage());
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
            
            System.out.println("  请求: GET /optimized?action=status");
            System.out.println("  响应: " + statusResponse.getBody());

            ltd.idcu.est.serverless.api.ServerlessRequest statsRequest = 
                new ltd.idcu.est.serverless.api.ServerlessRequest(
                    "GET", "/optimized", 
                    Map.of("action", "stats"), 
                    Map.of("Content-Type", "application/json"),
                    ""
                );
            
            ltd.idcu.est.serverless.api.ServerlessResponse statsResponse = 
                runner.invoke("optimized", statsRequest);
            
            System.out.println("  请求: GET /optimized?action=stats");
            System.out.println("  响应: " + statsResponse.getBody());
        } catch (Exception e) {
            System.err.println("  错误: " + e.getMessage());
        }
    }
}
