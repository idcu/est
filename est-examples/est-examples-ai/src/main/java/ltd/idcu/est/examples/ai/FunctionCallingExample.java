package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.ai.impl.DefaultFunctionRegistry;
import ltd.idcu.est.ai.impl.llm.MockLlmClient;
import ltd.idcu.est.ai.impl.tools.CalculatorTool;
import ltd.idcu.est.ai.impl.tools.StringManipulationTool;

import java.util.List;
import java.util.Map;

public class FunctionCallingExample {

    public static void main(String[] args) {
        System.out.println("=== EST AI - Function Calling Example ===\n");

        LlmClient client = new MockLlmClient();
        
        FunctionRegistry registry = new DefaultFunctionRegistry();
        
        registry.register(new CalculatorTool());
        registry.register(new StringManipulationTool());
        
        client.setFunctionRegistry(registry);
        
        System.out.println("Registered tools:");
        for (FunctionTool tool : registry.listTools()) {
            System.out.println("  - " + tool.getName() + ": " + tool.getDescription());
        }
        System.out.println();
        
        System.out.println("=== Example 1: Directly call calculator tool ===");
        FunctionTool calculator = registry.getTool("calculator");
        Map<String, Object> calcArgs = Map.of(
            "operation", "add",
            "a", 10,
            "b", 25
        );
        Object calcResult = calculator.execute(calcArgs);
        System.out.println("Calculate 10 + 25: " + calcResult);
        System.out.println();
        
        System.out.println("=== Example 2: Directly call string manipulation tool ===");
        FunctionTool stringTool = registry.getTool("string_manipulation");
        Map<String, Object> stringArgs = Map.of(
            "operation", "uppercase",
            "text", "Hello, EST AI!"
        );
        Object stringResult = stringTool.execute(stringArgs);
        System.out.println("String to uppercase: " + stringResult);
        System.out.println();
        
        System.out.println("=== Example 3: Using LLM client with tool integration ===");
        System.out.println("LLM client has function registry configured, can use these tools in conversation");
        System.out.println();
        
        System.out.println("Available tool names:");
        System.out.println("  - calculator: Basic arithmetic operations");
        System.out.println("  - string_manipulation: String manipulation");
        System.out.println();
        
        System.out.println("=== Example Complete ===");
    }
}
