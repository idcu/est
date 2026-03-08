package ltd.idcu.est.examples.ai;

import ltd.idcu.est.llm.LlmClientFactory;
import ltd.idcu.est.llm.api.*;
import ltd.idcu.est.llm.tools.CalculatorTool;
import ltd.idcu.est.llm.tools.StringManipulationTool;

import java.util.List;

public class DeepFunctionCallingExample {

    public static void main(String[] args) {
        System.out.println("=== EST LLM - 函数调用深度集成示例 ===\n");

        try {
            LlmClient client = LlmClientFactory.createOpenAiClient();
            
            FunctionRegistry registry = new ltd.idcu.est.llm.DefaultFunctionRegistry();
            
            registry.register(new CalculatorTool());
            registry.register(new StringManipulationTool());
            
            client.setFunctionRegistry(registry);
            
            System.out.println("已注册的工具:");
            for (FunctionTool tool : registry.listTools()) {
                System.out.println("  - " + tool.getName() + ": " + tool.getDescription());
            }
            System.out.println();

            System.out.println("=== 示例 1: 计算 25 + 17");
            LlmMessage userMessage1 = LlmMessage.user("计算 25 加 17 等于多少？");
            LlmResponse response1 = client.chat(List.of(userMessage1));
            System.out.println("回答: " + response1.getContent());
            System.out.println();

            System.out.println("=== 示例 2: 字符串处理");
            LlmMessage userMessage2 = LlmMessage.user("把 'Hello, World!' 转成大写，然后统计字符数。");
            LlmResponse response2 = client.chat(List.of(userMessage2));
            System.out.println("回答: " + response2.getContent());
            System.out.println();

            System.out.println("=== 示例 3: 多轮对话");
            List<LlmMessage> conversation = List.of(
                LlmMessage.system("你是一个有用的助手，可以使用提供的工具来回答问题。"),
                LlmMessage.user("请帮我计算 100 除以 4，然后把结果转成字符串并反转。")
            );
            LlmResponse response3 = client.chat(conversation);
            System.out.println("回答: " + response3.getContent());
            System.out.println();

            System.out.println("=== 示例完成 ===");

        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
