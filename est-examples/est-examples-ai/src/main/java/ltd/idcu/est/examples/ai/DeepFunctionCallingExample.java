package ltd.idcu.est.examples.ai;

import ltd.idcu.est.llm.LlmClientFactory;
import ltd.idcu.est.llm.api.*;
import ltd.idcu.est.llm.tools.CalculatorTool;
import ltd.idcu.est.llm.tools.StringManipulationTool;

import java.util.List;

public class DeepFunctionCallingExample {

    public static void main(String[] args) {
        System.out.println("=== EST LLM - Deep Function Calling Integration Example ===\n");

        try {
            LlmClient client = LlmClientFactory.createOpenAiClient();
            
            FunctionRegistry registry = new ltd.idcu.est.llm.DefaultFunctionRegistry();
            
            registry.register(new CalculatorTool());
            registry.register(new StringManipulationTool());
            
            client.setFunctionRegistry(registry);
            
            System.out.println("Registered tools:");
            for (FunctionTool tool : registry.listTools()) {
                System.out.println("  - " + tool.getName() + ": " + tool.getDescription());
            }
            System.out.println();

            System.out.println("=== Example 1: Calculate 25 + 17");
            LlmMessage userMessage1 = LlmMessage.user("What is 25 + 17?");
            LlmResponse response1 = client.chat(List.of(userMessage1));
            System.out.println("Answer: " + response1.getContent());
            System.out.println();

            System.out.println("=== Example 2: String manipulation");
            LlmMessage userMessage2 = LlmMessage.user("Convert 'Hello, World!' to uppercase, then count the characters");
            LlmResponse response2 = client.chat(List.of(userMessage2));
            System.out.println("Answer: " + response2.getContent());
            System.out.println();

            System.out.println("=== Example 3: Multi-turn conversation");
            List<LlmMessage> conversation = List.of(
                LlmMessage.system("You are a helpful assistant that can use the provided tools to answer questions."),
                LlmMessage.user("Please calculate 100 divided by 4, then convert the result to a string and reverse it.")
            );
            LlmResponse response3 = client.chat(conversation);
            System.out.println("Answer: " + response3.getContent());
            System.out.println();

            System.out.println("=== Example Complete ===");

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
