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
        System.out.println("=== EST AI - 函数调用示例 ===\n");

        LlmClient client = new MockLlmClient();
        
        FunctionRegistry registry = new DefaultFunctionRegistry();
        
        registry.register(new CalculatorTool());
        registry.register(new StringManipulationTool());
        
        client.setFunctionRegistry(registry);
        
        System.out.println("已注册的工具:");
        for (FunctionTool tool : registry.listTools()) {
            System.out.println("  - " + tool.getName() + ": " + tool.getDescription());
        }
        System.out.println();
        
        System.out.println("=== 示例 1: 直接调用计算器工�?===");
        FunctionTool calculator = registry.getTool("calculator");
        Map<String, Object> calcArgs = Map.of(
            "operation", "add",
            "a", 10,
            "b", 25
        );
        Object calcResult = calculator.execute(calcArgs);
        System.out.println("计算 10 + 25: " + calcResult);
        System.out.println();
        
        System.out.println("=== 示例 2: 直接调用字符串处理工�?===");
        FunctionTool stringTool = registry.getTool("string_manipulation");
        Map<String, Object> stringArgs = Map.of(
            "operation", "uppercase",
            "text", "Hello, EST AI!"
        );
        Object stringResult = stringTool.execute(stringArgs);
        System.out.println("字符串转换大�? " + stringResult);
        System.out.println();
        
        System.out.println("=== 示例 3: 使用 LLM 客户端与工具集成 ===");
        System.out.println("LLM 客户端已配置函数注册表，可以在对话中使用这些工具");
        System.out.println();
        
        System.out.println("可用的工具名�?");
        System.out.println("  - calculator: 基础算术运算");
        System.out.println("  - string_manipulation: 字符串处�?);
        System.out.println();
        
        System.out.println("=== 示例完成 ===");
    }
}
